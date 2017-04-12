const notificationFacade = require('./model/notification/facade')
const ruleFacade = require('./model/rule/facade')
const userFacade = require('../model/user/user-facade')
const deviceFacade = require('../model/device/device-facade')
const vm = require('vm')
const mu = require('mustache')
const config = require('../config')
const JPush = require('jpush-sdk')

class NotificationLib {
  sendMsg (meta, level, cb, catchCb) {
    let actionCb = doc => {
      let s = -1000
      switch (level) {
        case 1:
          s = 5
          break
        case 2:
          s = 2
          break
        case 3:
          s = 1
          break
      }
      let time = s * 60 * 1000
      notificationFacade.findOne({from: doc.from, to: doc.to, created_at: {'$gt': new Date(new Date().getTime() - time)}})
        .then(noti => {
          if (!noti) this.pushMsg(doc, level)
        })
        .catch(err => {
          console.console.error(err)
        })
      if (cb) cb(doc)
    }
    return notificationFacade.create(meta)
    .then(actionCb)
    .catch(catchCb)
  }

  pushMsg (doc, level) {
    let levelArr = ['system', 'normal', 'special', 'warning']
    const client = JPush.buildClient(config.jpush.appKey, config.jpush.masterSecret)
    deviceFacade.findById(doc.from).then(device => {
      userFacade.findById(doc.to).then(user => {
        if (user && user.setting.push[levelArr[level]]) {
          client.push().setPlatform('android')
            .setAudience(JPush.alias(user._id.replace('-', '@')))
            .setNotification(JPush.android(doc.content, doc.from === 'SYS' ? '系统通知' : device.name))
            .send(function (err, res) {
              if (err) {
                if (err instanceof JPush.APIConnectionError) {
                  console.log(err.message)
                  // Response Timeout means your request to the server may have already received,
                  // please check whether or not to push
                  console.log(err.isResponseTimeout)
                } else if (err instanceof JPush.APIRequestError) {
                  console.log(err.message)
                }
              } else {
                console.log('Sendno: ' + res.sendno)
                console.log('Msg_id: ' + res.msg_id)
              }
            })
        }
      })
    })
  }

  matchRule (device, product, owner, label, newData, oldData) {
    ruleFacade.find({product, label: label})
    .then(doc => {
      for (let i in doc) {
        let result = false
        const sandbox = {newData, oldData, result}
        vm.createContext(sandbox)
        vm.runInContext('if(' + doc[i].condition + ') result = true;', sandbox)
        if (sandbox.result) {
          userFacade.findById(owner).then(user => {
            let level
            switch (doc[i].level) {
              case 1:
                level = 'special'
                break
              case 2:
                level = 'warning'
                break
              default:
                level = 'normal'
            }
            if (user.setting.push[level]) {
              this.sendMsg({
                from: device,
                to: owner,
                content: mu.render(doc[i].template, sandbox)
              }, doc[i].level)
            }
          })
        }
      }
    })
    .catch(err => console.error(err))
  }
}

module.exports = new NotificationLib()
