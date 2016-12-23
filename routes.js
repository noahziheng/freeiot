const Router = require('express').Router
const router = new Router()

const user = require('./model/user/user-router')
const product = require('./model/product/product-router')
const device = require('./model/device/device-router')

router.route('/').get((req, res) => {
  res.json({ message: 'Welcome to FreeIOT API!' })
})

router.use('/user', user)
router.use('/product', product)
router.use('/device', device)

module.exports = router
