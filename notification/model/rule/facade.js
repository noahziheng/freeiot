const Model = require('../../../lib/facade')
const Schema = require('./schema')

class RuleModel extends Model {
 // 重写3个find，加入设备的关联查询
  find (query) {
    return this.Schema
    .find(query)
    .populate('device')
    .exec()
  }

  findOne (query) {
    return this.Schema
    .findOne(query)
    .populate('device')
    .exec()
  }

  findById (id) {
    return this.Schema
    .findById(id)
    .populate('device')
    .exec()
  }
}

module.exports = new RuleModel(Schema)
