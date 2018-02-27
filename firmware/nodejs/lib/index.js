const MQTT = require('mqtt')

module.exports = class FreeIOT {
  constructor (id, secret, debug = false) {
    let client = MQTT.connect('mqtt://api.iot.noahgao.net', {
      clientId: 'iot/' + id + '/',
      will: {
        topic: 'logout',
        payload: id + '/' + secret
      }
    })
    this.client = client
    this.debug = debug
    client.on('connect', function () {
      client.subscribe(id + '-d')
      console.log('Connected!')
    })
    client.on('close', function () {
      console.log('Disconnected!')
    })
  }

  send (order) {
    order = order.split(/[+,:;]+/)
    order.shift()
    order.pop()
    console.log(order)
  }
}
