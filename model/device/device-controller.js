const Controller = require('../../lib/controller')
const deviceFacade = require('./device-facade')
const dataFacade = require('../data/data-facade')

class DeviceController extends Controller {

  findById (req, res, next) {
    return this.facade.findById(req.params.id)
    .then(doc => {
      if (!doc) { return res.status(404).end() }
      if (req.user.role <= 2 && doc.owner !== req.user.id) doc.secret = undefined // HTTP 401 无秘钥查询权限
      if (req.params.datalimit) req.params.datalimit = 1
      let con = {device: req.params.id, created_at: {'$gte': new Date(new Date().getTime() - req.params.datalimit * 60 * 60 * 1000), '$lt': new Date()}}
      dataFacade.find(con).then(datas => {
        return res.status(200).json({meta: { device: doc, datalimit: req.params.datalimit }, data: datas})
      })
    })
    .catch(err => res.status(500).json({ msg: err.message, error: err }))
  }

  datatest (req, res, next) {
    req.body.device = req.params.id
    dataFacade.create(req.body)
    .then(doc => res.status(201).json(doc))
    .catch(err => res.status(500).json({ msg: err.message, error: err }))
  }
}

module.exports = new DeviceController(deviceFacade)
