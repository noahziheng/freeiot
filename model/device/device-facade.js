const Model = require('../../lib/facade')
const deviceSchema = require('./device-schema')

class DeviceModel extends Model {
 // 重写3个find，加入owner对用户的关联查询
  find (query) {
    return this.Schema
    .find(query)
    .populate('owner')
    .populate('product')
    .exec()
  }

  findOne (query) {
    return this.Schema
    .findOne(query)
    .populate('owner')
    .populate('product')
    .exec()
  }

  findById (id) {
    return this.Schema
    .findById(id)
    .populate('owner')
    .populate('product')
    .exec()
  }
}

module.exports = new DeviceModel(deviceSchema)
