const controller = require('./device-controller')
const Router = require('express').Router
const router = new Router()

router.route('/')
  .get((...args) => controller.find(...args))
  .post((...args) => controller.create(...args))

router.route('/my').get((...args) => controller.findOne(...args))

router.route('/:id')
  .put((...args) => controller.update(...args))
  .get((...args) => controller.findById(...args))
  .delete((...args) => controller.remove(...args))

router.route('/:id/:datalimit').get((...args) => controller.findById(...args))
router.route('/:id/data').post((...args) => controller.datanew(...args))
router.route('/:id/empty').put((...args) => controller.dataempty(...args))
router.route('/:id/activite/:user').get((...args) => controller.activite(...args))
router.route('/:id/makeoffline').put((...args) => controller.makeoffline(...args))

module.exports = router
