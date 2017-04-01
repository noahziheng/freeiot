const Controller = require('../lib/controller')
const notificationFacade = require('./model/notification/facade')

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

  getChat (req, res, next) {
    req.query.to = req.user.id
    if (!req.params.from) return res.status(502).json({ msg: 'Bad Request' })
    else req.query.from = req.params.from
    super.find(req, res, next)
  }

  unread (req, res, next) {
    const conditions = { from: req.params.from }
    console.log(conditions)
    this.facade.update(conditions, {unread: 0})
    .then(doc => {
      if (!doc) return res.status(404).json({ msg: 'Not Found!' })
      return res.status(200).json(doc)
    })
    .catch(err => res.status(500).json({ msg: err.message, error: err }))
  }
}

module.exports = new NotificationController(notificationFacade)
