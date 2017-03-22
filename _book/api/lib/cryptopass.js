const crypto = require('crypto')
const key = require('../config').key.pass

module.exports = orig => {
  if (!orig) orig = Math.floor(new Date().getTime() / 10).toString()
  return crypto.createHmac('sha1', key).update(orig).digest('hex')
}
