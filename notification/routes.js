const controller = require('./controller')
const Router = require('express').Router
const router = new Router()

router.route('/').get((...args) => controller.find(...args))
router.route('/').post((...args) => controller.create(...args))

router.route('/meta').get((...args) => controller.getMeta(...args))
router.route('/unread/:id').get((...args) => controller.unread(...args))

router.route('/:id')
  .get((...args) => controller.findById(...args))
  .delete((...args) => controller.remove(...args))

module.exports = router
