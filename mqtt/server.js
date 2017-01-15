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
    this.server = server
    server.on('clientConnected', this.handleConnect.bind(this))
    server.on('published', this.handleMsg.bind(this))
  }

  setup () {
    console.log('FreeIOT MQTT Server Daemon is up and running')
  }

  handleConnect (client) {
    const clientMeta = client.id.split('/')
    const clientWillMeta = JSON.parse(client.will.payload.toString())
    if (clientMeta[1] === clientWillMeta.id) {
      for (let e in this.devices) {
        if (this.devices[e]._id === clientWillMeta.id) {
          var f = true
          break
        }
      }
      if (!f) {
        deviceFacade.findById(clientWillMeta.id).select('product owner secret status').populate('product').populate('owner').exec().then(doc => {
          if (doc === null) {
            client.close()
          } else {
            if (doc.secret === clientWillMeta.secret) {
              let e = this.devices.push(doc) - 1
              let modsP = []
              for (let i in this.devices[e].product.mods) {
                if (typeof this.devices[e].product.mods[i].origin === 'string') {
                  modsP.push(modtool(this.devices[e].product.mods[i].origin, this.devices[e].product.mods[i].vars, this.devices[e].product.mods[i].hidden.toBSON()))
                }
              }
              delete this.devices[e].product.mods
              this.devices[e].product.modsP = modsP
              doc.status = 3
              doc.save()
            } else {
              client.close()
            }
          }
        }).catch(err => {
          console.error(err.message)
          client.close()
        })
      }
    } else {
      client.close()
    }
  }

  handleMsg (packet, client) {
    switch (packet.topic) {
      case 'logout':
        const req = JSON.parse(packet.payload.toString())
        for (let e in this.devices) {
          if (this.devices[e]._id === req.id && this.devices[e].secret === req.secret) {
            console.log(req.id + ' removed')
            delete this.devices[e]
            deviceFacade.findByIdAndUpdate(req.id, {$set: { status: 2 }}, {new: true}).exec()
            break
          }
        }
        break
      case 'uplink':
        for (let e in this.devices) {
          if (this.devices[e]._id === client.id.split('/')[1]) {
            let data = []
            for (let i in this.devices[e].product.modsP) {
              let t = this.parser('uplink', this.devices[e].product.modsP[i], packet.payload.toString())
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
