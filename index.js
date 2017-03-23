const express = require('express')
const mongoose = require('mongoose')
const helmet = require('helmet')
const bodyParser = require('body-parser')
const morgan = require('morgan')
const bluebird = require('bluebird')

const jwt = require('express-jwt')

const config = require('./config')
const routes = require('./routes')

const userModel = require('./model/user/user-facade')

const MsgServer = require('./mqtt/server.js')
const cors = require('cors')
const app = express()
const httpserver = require('http').Server(app)
const io = require('socket.io')(httpserver)
const mosca = require('mosca')

mongoose.Promise = bluebird
mongoose.connect(config.mongo.url)

// Config
app.use(helmet())
app.use(bodyParser.urlencoded({ extended: true }))
app.use(bodyParser.json())
app.use(morgan('tiny'))
app.use(cors())
app.use(jwt({ secret: config.key.jwt,
  getToken: req => {
    if (req.query && req.query.token) { // 识别HTTP Params中的token
      const t = req.query.token
      req.query.token = undefined // 移除请求头中的Token字段规避错误
      return t
    }
    return null
  } }).unless({ path: ['/', '/user/auth', '/mod', { url: /^\/mod\/.*/, methods: 'POST' }, { url: '/user', methods: 'POST' }] }), // 首页、用户鉴权及注册不受JWT保护
  (err, req, res, next) => {
    // Error handler(Global in Routes)
    // Check JWT's UnauthorizedError mainly
    switch (err.name) {
      case 'UnauthorizedError':
        return res.status(401).json({ msg: 'Unauthorized!' }) // HTTP 401 token无效
      case 'BadRequestError':
      case 'UnauthorizedAccessError':
      case 'NotFoundError':
        return res.status(err.status).json(err.inner)
      default:
        return next()
    }
  },
  (req, res, next) => {
    if (req.user) { // 如识别到JWT中的 Payload 则查库进行二次验证
      userModel.findById(req.user.id).then(doc => {
        if (!doc) { return res.status(404).json({ msg: 'User is not found!' }) } // HTTP 401 token对应用户不存在
        if (doc.role !== req.user.role) { return res.status(401).json({ msg: 'Token is outdated!' }) } // HTTP 401 token已过期（权限更改）
        return next()
      })
      .catch(err => res.status(500).json({ msg: err.message, error: err }))
    } else {
      return next()
    }
  })

// Run
const server = new mosca.Server(config.mqtt)   // here we start mosca
const msg = new MsgServer(server, io)
msg.setup()
app.use((req, res, next) => {
  req.mqtt = server
  req.io = io
  next()
})

app.use('/', routes)

httpserver.listen(config.server.port, () => {
  console.log(`Magic happens on port ${config.server.port}`)
})

io.on('connection', socket => {})

module.exports = app
