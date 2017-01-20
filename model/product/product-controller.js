const Controller = require('../../lib/controller')
const productFacade = require('./product-facade')
const deviceFacade = require('../device/device-facade')

class ProductController extends Controller {
  find (req, res, next) {
    return this.facade.find(req.query)
    .then(collection => res.status(200).json(collection))
    .catch(err => res.status(500).json({ msg: err.message, error: err }))
  }

  findById (req, res, next) {
    return this.facade.findById(req.params.id)
    .then(doc => {
      if (req.user.role === 3 || doc.owner._id === req.user.id) {
        this.facade.getSecret(req.params.id).then(r => {
          if (!doc) { return res.status(404).end() }
          doc.secret = r.secret
          return res.status(200).json(doc)
        })
        .catch(err => res.status(500).json({ msg: err.message, error: err }))
      } else return res.status(200).json(doc)
    })
    .catch(err => res.status(500).json({ msg: err.message, error: err }))
  }

  // 重写update方法用于PUT /user/:id 防止提权
  remove (req, res, next) {
    this.facade.findById(req.params.id)
    .then(product => {
      if (req.user.role <= 2 && req.user.id !== product.owner._id) return res.status(401).json({ msg: 'Unauthorized' }) // HTTP 401 无更新权限
      const conditions = { product: req.params.id }
      deviceFacade.find(conditions).then(doc => {
        if (!doc) return res.status(404).json({ msg: 'Not Found!' })
        for (let i in doc) {
          deviceFacade.remove(doc[i]._id)
          .then(doc => {
            if (!doc) return res.status(404).json({ msg: 'Not Found!' })
          })
          .catch(err => res.status(500).json({ msg: err.message, error: err }))
        }
        this.facade.remove(req.params.id)
        .then(doc => {
          if (!doc) return res.status(404).json({ msg: 'Not Found!' })
          return res.status(200).json(doc)
        }).catch(err => res.status(500).json({ msg: err.message, error: err }))
      }).catch(err => res.status(500).json({ msg: err.message, error: err }))
    }).catch(err => res.status(500).json({ msg: err.message, error: err }))
  }
}

module.exports = new ProductController(productFacade)
