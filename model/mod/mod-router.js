const controller = require('./mod-controller')
const Router = require('express').Router
const router = new Router()

router.route('/')
  .get((...args) => controller.find(...args))

router.route('/:id')
  .post((...args) => controller.findById(...args))

module.exports = router
