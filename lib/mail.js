const config = require('../config')
const mu = require('mustache')
const fs = require('fs')
const nodemailer = require('nodemailer')

class Mail {
  send (address, tpl, vars) {
    tpl = require('../mail/' + tpl + '.json')
    let transporter = nodemailer.createTransport({
      'host': 'smtpdm.aliyun.com',
      'port': 465,
      'secureConnection': true, // use SSL
      'auth': {
        'user': config.mail.user, // user name
        'pass': config.mail.pass   // password
      }
    })
    let r = {
      from: 'FreeIOT<' + config.mail.user + '>',
      to: address,
      subject: tpl.title, // Subject line
      html: mu.render(fs.readFileSync('./mail/base.html').toString(), {
        user: address,
        msg: mu.render(tpl.msg, vars)
      })
    }
    transporter.sendMail(r, function (error, info) {
      if (error) {
        return console.log(error)
      }
      console.log('Message sent: ' + info.response)
    })
  }
}

module.exports = new Mail()
