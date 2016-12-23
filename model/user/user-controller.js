const Controller = require('../../lib/controller')
const userFacade = require('./user-facade')

const jwt = require('jsonwebtoken')
const config = require('../../config')
const ctyptopass = require('../../lib/cryptopass')

class UserController extends Controller {

  // 重写create方法用于POST /user 注册防止密码明文返回，及为未来加强注册控制留接口
  create (req, res, next) {
    if (req.body.role >= 2) return res.status(401).json({ msg: 'Unauthorized' })
    this.facade.create(req.body)
    .then(doc => {
      if (!doc) return res.status(404).json({ msg: 'Not Found!' })
      doc.password = undefined
      return res.status(201).json(doc)
    })
    .catch(err => res.status(500).json({ msg: err.message, error: err }))
  }


  // auth方法用于POST /user/auth 用户鉴权，返回token
  auth (req, res, next) {
    const condition = { email: req.body.email, password: ctyptopass(req.body.password) }
    this.facade.findOne(condition)
    .then(doc => {
      if (!doc) return res.status(404).json({ msg: 'Not Found!' })
      const token = { token: jwt.sign({ id: doc._id, email: doc.email, role: doc.role }, config.key.jwt, { expiresIn: '7d' }) }
      return res.status(200).json(token)
    })
    .catch(err => res.status(401).json({ msg: err.message, error: err }))
  }

  // 重写update方法用于PUT /user/:id 防止提权
  update (req, res, next) {
    if (req.user.role <= 2 && req.body.role >= 2) return res.status(401).json({ msg: 'Unauthorized' }) // HTTP 401 防止用户对自身role提交提权
    super.update(req, res, next)
  }
}

module.exports = new UserController(userFacade)
