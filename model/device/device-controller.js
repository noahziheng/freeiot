const Controller = require('../../lib/controller')
const deviceFacade = require('./device-facade')
const dataFacade = require('../data/data-facade')
const modtool = require('../../mods/tool')

class DeviceController extends Controller {

  findById (req, res, next) {
    return this.facade.findById(req.params.id)
    .then(doc => {
      if (!doc) { return res.status(404).end() }
      if (!req.params.datalimit) req.params.datalimit = 1
      let timestamp = new Date().getTime()
      let con = {device: req.params.id, created_at: {'$gte': new Date(timestamp - req.params.datalimit * 60 * 60 * 1000), '$lt': new Date()}}
      dataFacade.find(con).then(datas => {
        let result = {meta: { device: doc, datalimit: req.params.datalimit }, data: datas}
        if (req.user.role === 3 || doc.owner._id === req.user.id || doc.product.owner._id === req.user.id) {
          this.facade.getSecret(req.params.id).then(r => {
            if (!doc) { return res.status(404).end() }
            result.meta.device.secret = r.secret
            return res.status(200).json(result)
          })
          .catch(err => res.status(500).json({ msg: err.message, error: err }))
        } else return res.status(200).json(result)
      })
    })
    .catch(err => res.status(500).json({ msg: err.message, error: err }))
  }

  remove (req, res, next) {
    this.facade.findById(req.params.id)
    .then(device => {
      if (req.user.role <= 2 && req.user.id !== device.owner._id && req.user.id !== device.product.owner._id) return res.status(401).json({ msg: 'Unauthorized' }) // HTTP 401 无更新权限
      const conditions = { device: req.params.id }
      dataFacade.find(conditions).then(doc => {
        if (!doc) return res.status(404).json({ msg: 'Not Found!' })
        for (let i in doc) {
          dataFacade.remove(doc[i]._id)
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

  datanew (req, res, next) {
    req.body.device = req.params.id
    return this.facade.findById(req.params.id)
    .then(doc => {
      if (!doc) { return res.status(404).end() }
      if (req.user.role <= 2 && doc.owner !== req.user.id) doc.secret = undefined // HTTP 401 无秘钥查询权限
      if (!req.body.type) req.body.type = 1
      if (req.body.type === 1) {
        let payload = []
        for (let i in doc.product.mods) {
          if (doc.product.mods[i].origin) {
            let t = modtool(doc.product.mods[i].origin, doc.product.mods[i].vars)
            let driver = require('../../mods/drivers/' + t.driver + '.js')
            let payloadT = {}
            for (let j in t.downlink) {
              if (req.body[t.downlink[j].label] !== undefined) payloadT[t.downlink[j].label] = req.body[t.downlink[j].label]
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
            dataFacade.create(obj).catch(err => res.status(500).json({ msg: err.message, error: err }))
            req.io.emit(req.body.device + '-web', obj)
          }
        }
        return res.status(201).json(payload)
      }
    })
    .catch(err => res.status(500).json({ msg: err.message, error: err }))
  }

  dataempty (req, res, next) {
    return dataFacade.find({device: req.params.id}).then(doc => {
      if (!doc) return res.status(404).json({ msg: 'Not Found!' })
      for (let i in doc) {
        dataFacade.remove(doc[i]._id)
        .then(doc => {
          if (!doc) return res.status(404).json({ msg: 'Not Found!' })
        })
        .catch(err => res.status(500).json({ msg: err.message, error: err }))
      }
      dataFacade.create({type: 3, device: req.params.id, label: 'SYS', content: 'empty'})
      return res.status(200).json({ ok: 'Success!' })
    })
    .catch(err => res.status(500).json({ msg: err.message, error: err }))
  }

  isEmptyObject (obj) {
    for (let i in obj) {
      return false
    }
    return true
  }
}

module.exports = new DeviceController(deviceFacade)
