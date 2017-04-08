const notificationFacade = require('./model/notification/facade')
const ruleFacade = require('./model/rule/facade')
const userFacade = require('../model/user/user-facade')
const vm = require('vm')
const mu = require('mustache')

class NotificationLib {
  sendMsg (meta, level, cb, catchCb) {
    console.log('Simulate Send a Msg:', meta, level)
    return notificationFacade.create(meta)
    .then(cb)
    .catch(catchCb)
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
