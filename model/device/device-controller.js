const Controller = require('../../lib/controller')
const deviceFacade = require('./device-facade')
const dataFacade = require('../data/data-facade')
const modtool = require('../../mods/tool')

class DeviceController extends Controller {

  findById (req, res, next) {
    return this.facade.findById(req.params.id)
    .then(doc => {
      if (!doc) { return res.status(404).end() }
      if (req.user.role <= 2 && doc.owner !== req.user.id) doc.secret = undefined // HTTP 401 无秘钥查询权限
      if (!req.params.datalimit) req.params.datalimit = 1
      let timestamp = new Date().getTime()
      let con = {device: req.params.id, created_at: {'$gte': new Date(timestamp - req.params.datalimit * 60 * 60 * 1000), '$lt': new Date()}}
      dataFacade.find(con).then(datas => {
        return res.status(200).json({meta: { device: doc, datalimit: req.params.datalimit }, data: datas})
      })
    })
    .catch(err => res.status(500).json({ msg: err.message, error: err }))
  }

  datanew (req, res, next) {
    req.body.device = req.params.id
    return this.facade.findById(req.params.id)
    .then(doc => {
      if (!doc) { return res.status(404).end() }
      if (req.user.role <= 2 && doc.owner !== req.user.id) doc.secret = undefined // HTTP 401 无秘钥查询权限
      if (req.body.type === 1) {
        let payload = []
        for (let i in doc.product.mods) {
          if (doc.product.mods[i].origin) {
            let t = modtool(doc.product.mods[i].origin, doc.product.mods[i].vars)
            let driver = require('../../mods/drivers/' + t.driver + '.js')
            let payloadT = {}
            for (let j in t.downlink) {
              if (req.body[t.downlink[j].label]) payloadT[t.downlink[j].label] = req.body[t.downlink[j].label]
            }
            if (!this.isEmptyObject(payloadT)) {
              payload.push({
                orig: payloadT,
                encode: driver.encode(payloadT)
              })
            }
          }
        }
        let message = {
          topic: req.body.device + '-d',
          payload: '',
          qos: 0,
          retain: false
        }
        for (let i in payload) {
          message.payload = payload[i].encode
          req.mqtt.publish(message)
          for (let j in payload[i].orig) {
            let obj = {
              type: 1, // 0-上行报告 1-下行指令
              device: req.body.device,
              label: j,
              content: payload[i].orig[j]
            }
            dataFacade.create(obj)
            .catch(err => res.status(500).json({ msg: err.message, error: err }))
          }
        }
        console.log(payload)
        return res.status(201).json(payload)
      }
    })
  }

  isEmptyObject (obj) {
    for (let i in obj) {
      return false
    }
    return true
  }
}

module.exports = new DeviceController(deviceFacade)
