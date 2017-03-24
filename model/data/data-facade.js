const Model = require('../../lib/facade')
const dataSchema = require('./data-schema')
const modtool = require('../../mods/tool')

class DataModel extends Model {
  getDatas (id, datalimit, device) {
    let con = { device: id }
    let timestamp = new Date().getTime()
    if (datalimit !== 0) {
      con.created_at = {
        '$gte': new Date(timestamp - datalimit * 60 * 60 * 1000),
        '$lt': new Date()
      }
      return super.find(con)
    } else {
      let t = []
      for (let p in device.product.mods.toBSON()) {
        let m = modtool(device.product.mods[p].origin, device.product.mods[p].vars)
        if(m.uplink) {
          for (let i in m.uplink) {
            con.label = m.uplink[i].label
            t.push(super.findOne(con))
          }
        }
        if(m.downlink) {
          for (let i in m.downlink) {
            con.label = m.downlink[i].label
            t.push(super.findOne(con))
          }
        }
      }
      return Promise.all(t)
    }
  }
}

module.exports = new DataModel(dataSchema)
