const crypto = require('crypto')
const key = require('../config').key.pass
const dateTime = new Date().getTime()
const timestamp = Math.floor(dateTime / 1000)

module.exports = orig => {
  if (!orig) orig = timestamp.toString()
  return crypto.createHmac('sha1', key).update(orig).digest('hex')
}
