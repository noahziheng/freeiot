const Controller = require('../../lib/controller')
const userFacade = require('./user-facade')

const jwt = require('jsonwebtoken')
const config = require('../../config')
const ctyptopass = require('../../lib/cryptopass')

class UserController extends Controller {

  // 重写create方法用于POST /user 注册防止密码明文返回，及为未来加强注册控制留接口
  create (req, res, next) {
    if (req.body.role >= 2) return res.status(401).json({ msg: 'Unauthorized' })
    this.facade.findOne({email: req.body.email}).then(doc => {
      if (doc) {
        if (doc.role === -1) doc.remove()
        else return res.status(500).json({ msg: 'The E-mail had registered!' })
      }
      this.facade.create(req.body)
      .then(doc => {
        if (!doc) return res.status(404).json({ msg: 'Not Found!' })
        require('../../lib/mail').send(doc.email, 'reg', {id: doc.finish})
        doc.finish = undefined
        doc.password = undefined
        return res.status(201).json(doc)
      })
      .catch(err => res.status(500).json({ msg: err.message, error: err }))
    })
    .catch(err => res.status(500).json({ msg: err.message, error: err }))
  }

  // auth方法用于POST /user/auth 用户鉴权，返回token
  auth (req, res, next) {
    const condition = { email: req.body.email, password: ctyptopass(req.body.password) }
    this.facade.findOne(condition)
    .then(doc => {
      if (!doc) {
        const condition = { email: req.body.email }
        this.facade.findOne(condition)
        .then(doc => {
          if (!doc) return res.status(404).json({ msg: 'Not Found!' })
          else return res.status(502).json({ msg: 'Password Wrong' })
        }).catch(err => res.status(401).json({ msg: err.message, error: err }))
      } else {
        if (doc.role === -1) return res.status(404).json({ msg: 'The E-Mail need be checked!' })
        const token = { id: doc._id, email: doc.email, role: doc.role, token: jwt.sign({ id: doc._id, email: doc.email, role: doc.role }, config.key.jwt, { expiresIn: '7d' }) }
        return res.status(200).json(token)
      }
    })
    .catch(err => res.status(401).json({ msg: err.message, error: err }))
  }

  // 重写update方法用于PUT /user/:id 防止提权
  update (req, res, next) {
    if (req.user.role <= 2 && req.body.role >= 2) return res.status(401).json({ msg: 'Unauthorized' }) // HTTP 401 防止用户对自身role提交提权
    this.facade.findById(req.params.id)
    .then(user => {
      if (req.user.role <= 2 && user._id !== req.params.id) return res.status(401).json({ msg: 'Unauthorized' }) // HTTP 401 无更新权限
      const conditions = { _id: req.params.id }
      this.facade.update(conditions, req.body)
      .then(doc => {
        if (!doc) return res.status(404).json({ msg: 'Not Found!' })
        doc.token = jwt.sign({ id: user._id, email: user.email, role: req.body.role ? req.body.role : user.role }, config.key.jwt, { expiresIn: '7d' })
        return res.status(200).json(doc)
      })
      .catch(err => res.status(500).json({ msg: err.message, error: err }))
    })
    .catch(err => res.status(500).json({ msg: err.message, error: err }))
  }

  finish (req, res, next) {
    let con = {finish: req.params.id}
    if (req.body.password) con.password = ctyptopass(req.body.password)
    this.facade.findOne(con).then((doc) => {
      if (!doc) return res.status(500).json({ msg: 'The E-mail don\'t have  register record!' })
      if (doc.role !== -1) return res.status(500).json({ msg: 'The E-mail had finish registered!' })
      if (req.body.password) {
        doc.role = 0
        doc.save()
      }
      return res.status(200).json(doc)
    })
  }
}

module.exports = new UserController(userFacade)
