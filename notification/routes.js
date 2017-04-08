const controller = require('./controller')
const Router = require('express').Router
const router = new Router()

router.route('/').get((...args) => controller.find(...args))
  .post((...args) => controller.create(...args))

router.route('/chat/:from').get((...args) => controller.getChat(...args))
router.route('/unread/:from').get((...args) => controller.unread(...args))

router.route('/rule').post((...args) => controller.addRule(...args))
router.route('/rule/:product').get((...args) => controller.getRules(...args))
router.route('/rule/:id')
  .put((...args) => controller.editRule(...args))
  .delete((...args) => controller.delRule(...args))

router.route('/:id')
  .get((...args) => controller.findById(...args))
  .delete((...args) => controller.remove(...args))

module.exports = router
