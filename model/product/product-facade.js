const Model = require('../../lib/facade')
const productSchema = require('./product-schema')

class ProductModel extends Model {
 // 重写3个find，加入owner对用户的关联查询
  find (query) {
    return this.Schema
    .find(query)
    .populate('owner')
    .exec()
  }

  findOne (query) {
    return this.Schema
    .findOne(query)
    .populate('owner')
    .exec()
  }

  findById (id) {
    return this.Schema
    .findById(id)
    .populate('owner')
    .exec()
  }

  getSecret (id) {
    return this.Schema
    .findById(id)
    .select({secret: 1})
    .exec()
  }
}

module.exports = new ProductModel(productSchema)
