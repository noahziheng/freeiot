const Controller = require('../lib/controller')
const notificationFacade = require('./model/notification/facade')

class NotificationController extends Controller {
  find (req, res, next) {
    req.query.to = req.user.id
    super.find(req, res, next)
  }

  getMeta (req, res, next) {
    this.facade.count(req.user.id, (count) => {
      return res.status(200).json({
        count: count
      })
    })
  }

  unread (req, res, next) {
    this.facade.findById(req.params.id)
    .then(doc => {
      if (req.user.id !== doc.to._id) return res.status(401).json({ msg: 'Unauthorized' }) // HTTP 401 无更新权限
      const conditions = { _id: req.params.id }
      this.facade.update(conditions, {unread: 0})
      .then(doc => {
        if (!doc) return res.status(404).json({ msg: 'Not Found!' })
        return res.status(200).json(doc)
      })
      .catch(err => res.status(500).json({ msg: err.message, error: err }))
    })
    .catch(err => res.status(500).json({ msg: err.message, error: err }))
  }
}

module.exports = new NotificationController(notificationFacade)
