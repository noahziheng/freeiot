const Router = require('express').Router
const router = new Router()

const user = require('./model/user/user-router')
const product = require('./model/product/product-router')
const device = require('./model/device/device-router')
const mod = require('./model/mod/mod-router')

router.route('/').get((req, res) => {
  res.json({ message: 'Welcome to FreeIOT API!' })
})

router.use('/user', user)
router.use('/product', product)
router.use('/device', device)
router.use('/mod', mod)

module.exports = router
