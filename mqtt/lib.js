/**
 * FreeIOT MQTT Server Library
 *
 * Author Noah Gao
 */
class MQTTLibrary {
  setup (io) {
    this.io = io
  }

  parser (type, mod, data) {
    const driver = require('../mods/drivers/' + mod.driver + '.js')
    return driver.parse(type, mod, data)
  }

  isEmptyObject (obj) {
    for (let i in obj) {
      return false
    }
    return true
  }
}

module.exports = new MQTTLibrary()
