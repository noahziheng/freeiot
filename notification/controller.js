const Controller = require('../lib/controller')
const notificationFacade = require('./model/notification/facade')
const ruleFacade = require('./model/rule/facade')
const lib = require('./lib')

class NotificationController extends Controller {
  find (req, res, next) {
    this.facade.count(req.user.id, count => {
      this.facade.getList(req.user.id, lists => {
        return res.status(200).json({
          count,
          lists
        })
      })
    })
  }

  create (req, res, next) {
    return lib.sendMsg(
      req.body,
      -1,
      doc => res.status(201).json(doc),
      err => res.status(500).json({ msg: err.message, error: err })
    )
  }

  getChat (req, res, next) {
    req.query.to = req.user.id
    if (!req.params.from) return res.status(502).json({ msg: 'Bad Request' })
    else req.query.from = req.params.from
    super.find(req, res, next)
  }

  unread (req, res, next) {
    const conditions = { from: req.params.from }
    this.facade.update(conditions, {unread: 0})
    .then(doc => {
      if (!doc) return res.status(404).json({ msg: 'Not Found!' })
      return res.status(200).json(doc)
    })
    .catch(err => res.status(500).json({ msg: err.message, error: err }))
  }

  addRule (req, res, next) {
    ruleFacade.create(req.body)
      .then(doc => res.status(201).json(doc))
      .catch(err => res.status(500).json({ msg: err.message, error: err }))
  }

  getRules (req, res, next) {
    ruleFacade.find({ product: req.params.product })
      .then(doc => res.status(201).json(doc))
      .catch(err => res.status(500).json({ msg: err.message, error: err }))
  }

  editRule (req, res, next) {
    const conditions = { _id: req.params.id }
    ruleFacade.update(conditions, req.body)
    .then(doc => {
      if (!doc) return res.status(404).json({ msg: 'Not Found!' })
      return res.status(200).json(doc)
    })
    .catch(err => res.status(500).json({ msg: err.message, error: err }))
  }

  delRule (req, res, next) {
    ruleFacade.remove(req.params.id)
    .then(doc => {
      if (!doc) return res.status(404).json({ msg: 'Not Found!' })
      return res.status(200).json(doc)
    })
    .catch(err => res.status(500).json({ msg: err.message, error: err }))
  }
}

module.exports = new NotificationController(notificationFacade)
