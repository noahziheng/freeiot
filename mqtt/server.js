/**
 * FreeIOT MQTT Server Daemon
 *
 * Author Noah Gao
 */
const deviceFacade = require('../model/device/device-schema')
const dataFacade = require('../model/data/data-facade')
const modtool = require('../mods/tool')

class MsgServer {
  constructor (server) {
    this.devices = []
    this.mods = []
    this.server = server
    server.on('clientConnected', this.handleConnect.bind(this))
    server.on('published', this.handleMsg.bind(this))
  }

  setup () {
    console.log('FreeIOT MQTT Server Daemon is up and running')
  }

  handleConnect (client) {
    const clientMeta = client.id.split('/')
    console.log(clientMeta[1] + ' Request add')
    const clientWillMeta = client.will.payload.toString().split('/')
    console.log(clientWillMeta[0] + ' has secret ' + clientWillMeta[1])
    if (clientMeta[1] === clientWillMeta[0]) {
      for (let e in this.devices) {
        if (this.devices[e]._id === clientWillMeta[0]) {
          var f = true
          console.log(clientWillMeta[0] + ' is in the list')
          break
        }
      }
      if (!f) {
        deviceFacade.findById(clientWillMeta[0]).select('product owner secret status').populate('product').populate('owner').exec().then(doc => {
          if (doc === null) {
            console.log('Cannot found ' + clientWillMeta[0] + ',removed')
            client.close()
          } else {
            if (doc.secret === clientWillMeta[1]) {
              let e = this.devices.push(doc) - 1
              let modsP = []
              for (let i in this.devices[e].product.mods) {
                if (typeof this.devices[e].product.mods[i].origin === 'string') {
                  let t = modtool(this.devices[e].product.mods[i].origin, this.devices[e].product.mods[i].vars, this.devices[e].product.mods[i].hidden.toBSON())
                  if (t.downlink) {
                    for (let i in t.downlink) {
                      if (t.downlink[i].controll.default) {
                        let driver = require('../mods/drivers/' + t.driver + '.js')
                        let message = {
                          topic: clientWillMeta[0] + '-d',
                          payload: '',
                          qos: 0,
                          retain: false
                        }
                        message.payload = driver.encode({'t.downlink[i].label': t.downlink[i].controll.default})
                        client.server.publish(message)
                      }
                    }
                  }
                  modsP.push(t)
                }
              }
              this.mods[e] = modsP
              dataFacade.create({
                type: 3, // 0-上行报告 1-下行指令
                device: clientWillMeta[0],
                label: 'SYS',
                content: 'online'
              })
              doc.status = 3
              doc.save()
            } else {
              console.log(clientWillMeta[0] + '\'s secret is wrong')
              client.close()
            }
          }
        }).catch(err => {
          console.error(err.message)
          client.close()
        })
      }
    } else {
      console.log(clientWillMeta[0] + ' have a wrong will')
      client.close()
    }
  }

  handleMsg (packet, client) {
    switch (packet.topic) {
      case 'logout':
        const req = packet.payload.toString().split('/')
        for (let e in this.devices) {
          if (this.devices[e]._id === req[0] && this.devices[e].secret === req[1]) {
            console.log(req[0] + ' removed')
            dataFacade.create({
              type: 3, // 0-上行报告 1-下行指令
              device: req[0],
              label: 'SYS',
              content: 'offline'
            })
            delete this.devices[e]
            deviceFacade.findByIdAndUpdate(req[0], {$set: { status: 2 }}, {new: true}).exec()
            break
          }
        }
        break
      case 'uplink':
        for (let e in this.devices) {
          if (this.devices[e]._id === client.id.split('/')[1]) {
            let data = []
            for (let i in this.mods[e]) {
              let t = this.parser('uplink', this.mods[e][i], packet.payload.toString())
              data.push(t)
            }
            for (let i in data) {
              for (let j in data[i]) {
                dataFacade.create({
                  type: 0, // 0-上行报告 1-下行指令
                  device: this.devices[e]._id, // 设备代号
                  label: j, // 数据点代号
                  content: data[i][j] // 数据内容（解析完成的）
                })
              }
            }
            break
          }
        }
        break
      default:
        break
    }
  }

  parser (type, mod, data) {
    const driver = require('../mods/drivers/' + mod.driver + '.js')
    return driver.parse(type, mod, data)
  }
}

module.exports = MsgServer
