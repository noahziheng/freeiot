/**
 * FreeIOT MQTT Server Daemon for parse uplink data
 *
 * Author Noah Gao
 */
const mqtt = require('mqtt')
const productFacade = require('../model/product/product-schema')
const deviceFacade = require('../model/device/device-schema')
const dataFacade = require('../model/data/data-facade')

class MsgServer {
  constructor () {
    this.devices = []
  }

  // 主函数
  run (server, port) {
    const client = mqtt.connect('mqtt://' + server + ':' + port)
    // Register Event
    client.on('connect', this.init.bind(this, client))
    client.on('message', this.getMsg.bind(this, client))
  }

  // 初始化，订阅设备注册和离线频道
  init (client) {
    client.subscribe('reg')
    client.subscribe('logout')
  }

  // 设备注册
  regDevice (productMeta, deviceMeta, client) {
    productMeta = productMeta.split(',')
    deviceMeta = deviceMeta.split(',')
    for (let e in this.devices) {
      if (this.devices[e]._id === deviceMeta[0]) {
        client.subscribe('device-' + deviceMeta[0])
        client.publish('reg-d', deviceMeta[0] + ',ok')
        var f = true
        break
      }
    }
    if (!f) {
      deviceFacade.findById(deviceMeta[0]).select('product secret status').exec().then(doc => {
        if (productMeta[0] !== doc.product) client.publish('reg-d', deviceMeta[0] + ',sorry')
        else {
          productFacade.findById(doc.product).select('get_method secret').exec().then(product => {
            if (product.secret === productMeta[1] && doc.secret === deviceMeta[1]) {
              doc.product = product
              this.devices.push(doc)
              client.subscribe('device-' + doc._id)
              doc.status = 3
              doc.save()
              console.log('device-' + doc._id)
              client.publish('reg-d', deviceMeta[0] + ',ok')
            } else {
              client.publish('reg-d', deviceMeta[0] + ',sorry')
            }
          })
        }
      })
    }
  }

  getMsg (client, topic, message) {
    if (topic === 'reg') {
      this.regDevice(...message.toString().split(';'), client)
    } else if (topic === 'logout') {
      const req = message.toString().split(',')
      for (let e in this.devices) {
        if (this.devices[e]._id === req[0] && this.devices[e].secret === req[1]) {
          console.log(req[0] + ' removed')
          delete this.devices[e]
          client.unsubscribe('device-' + req[0])
          deviceFacade.findByIdAndUpdate(req[0], {$set: { status: 2 }}, {new: true}).exec()
          break
        }
      }
      return false
    } else {
      let data = {}
      for (let e in this.devices) {
        if (this.devices[e]._id === topic.split('-')[1]) {
          data = this.parser(this.devices[e].product.get_method, message.toString())
          break
        }
      }
      for (let i in data) {
        deviceFacade.findById(topic.split('-')[1]).exec().then(doc => {
          if (doc) {
            dataFacade.create({
              type: 0, // 0-上行报告 1-下行指令
              device: topic.split('-')[1], // 设备代号
              point: i, // 数据点代号
              content: data[i] // 数据内容（解析完成的）
            })
            if (!doc.data_latest) doc.data_latest = {}
            doc.data_latest[i] = data[i]
            console.log(doc)
            doc.save()
          } else {
            console.log('Not Found device in database')
          }
        }).catch(err => {
          console.error(err)
        })
      }
      console.log('Get from ' + topic.split('-')[1])
    }
  }

  // 上行数据解析函数
  parser (method, data) {
    let result = {}
    const forEach = require('async-foreach').forEach
    switch (method.type) {
      case 0:
        const meta = data.split(',')
        for (let e in meta) {
          meta[e] = meta[e].split(':')
          forEach(method.point, (item) => {
            if (item.name.short === meta[e][0]) result[item._id] = this.format(meta[e][1], item.format)
          })
        }
        break
      case 1:
        for (let index in JSON.parse(data)) {
          forEach(method.point, (item) => {
            if (item.name.long === index) result[item._id] = this.format(JSON.parse(data)[index], item.format)
          })
        }
        break
      case 2:
        const vm = require('vm')
        let point = []
        forEach(method.point, (item) => {
          point.push(item)
        })
        const sandbox = {point: point, data: data, result: result, format: this.format, JSON: JSON}
        vm.createContext(sandbox)
        vm.runInContext(method.custom, sandbox)
        break
    }
    return result
  }

  // 格式化数据中字符串为数据点定义中的格式
  format (data, format) {
    let result
    switch (format.name) {
      case 'int':
        result = parseInt(data)
        break
      case 'float':
        result = parseFloat(parseFloat(data).toFixed(format.round))
        break
      default:
        result = data
        break
    }
    return result
  }
}
module.exports = new MsgServer()
