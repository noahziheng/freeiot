const Controller = require('../../lib/controller')
const productFacade = require('./product-facade')

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
}

module.exports = new ProductController(productFacade)
