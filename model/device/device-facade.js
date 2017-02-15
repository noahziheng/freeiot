const Model = require('../../lib/facade')
const deviceSchema = require('./device-schema')
const dataFacade = require('../data/data-facade')

class DeviceModel extends Model {
 // 重写3个find，加入owner对用户的关联查询
  find (query) {
    dataFacade.find({created_at: {'$lt': new Date(new Date().getTime() - 72 * 60 * 60 * 1000)}}).then(doc => {
      for (let i in doc) {
        dataFacade.remove(doc[i]._id).then(r => {})
      }
    })
    console.log('clean Action Finished:' + new Date())
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
    dataFacade.find({created_at: {'$lt': new Date(new Date().getTime() - 72 * 60 * 60 * 1000)}}).then(doc => {
      for (let i in doc) {
        dataFacade.remove(doc[i]._id).then(r => {})
      }
    })
    console.log('clean Action Finished:' + new Date())
    return this.Schema
    .findById(id)
    .populate('owner')
    .populate('product')
    .exec()
  }

  getSecret (id) {
    return this.Schema
    .findById(id)
    .select({secret: 1})
    .exec()
  }
}

module.exports = new DeviceModel(deviceSchema)
