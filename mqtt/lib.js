/**
 * FreeIOT MQTT Server Library
 *
 * Author Noah Gao
 */
const dataFacade = require('../model/data/data-facade')
const notificationLib = require('../notification/lib')

class MQTTLibrary {
  // 库初始化(Socket.io)
  setup (io) {
    this.io = io
  }

  // 清理无用数据
  clean () {
    dataFacade.find({created_at: {'$lt': new Date(new Date().getTime() - 72 * 60 * 60 * 1000)}}).then(doc => {
      for (let i in doc) {
        dataFacade.remove(doc[i]._id).then(r => {})
      }
    })
    console.log('clean Action Finished:' + new Date())
  }

  // 发送设备系统级消息
  sendSystemMsg (device, content) {
    this.saveData(3, device, 'SYS', content)
  }

  // 保存数据
  saveData (type, device, label, content) {
    let obj = {
      type: type,
      device: device,
      label: label,
      content: content
    }
    dataFacade.create(obj).then(doc => { this.io.emit(device + '-web', doc) })
  }

  // 匹配消息推送规则
  matchRule (device, product, owner, label, newData, oldData) {
    notificationLib.matchRule(device, product, owner, label, newData, oldData)
  }

  // 驱动解析器映射函数
  parser (type, mod, data) {
    const driver = require('../mods/drivers/' + mod.driver + '.js')
    return driver.parse(type, mod, data)
  }

  // 空对象检验函数
  isEmptyObject (obj) {
    for (let i in obj) {
      return false
    }
    return true
  }
}

module.exports = new MQTTLibrary()
