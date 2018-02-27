const Model = require('../../lib/facade')
const userSchema = require('./user-schema')
const ctyptopass = require('../../lib/cryptopass')

class UserModel extends Model {

  update (conditions, update) {
    if (update.password !== undefined) {
      update.password = ctyptopass(update.password)
    }
    return super.update(conditions, update)
  }
}

module.exports = new UserModel(userSchema)
