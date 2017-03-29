const Model = require('../../../lib/facade')
const Schema = require('./schema')

class NotificationModel extends Model {
 // 重写3个find，加入用户的关联查询
  find (query) {
    return this.Schema
    .find(query)
    .populate('from')
    .populate('to')
    .exec()
  }

  findOne (query) {
    return this.Schema
    .findOne(query)
    .populate('from')
    .populate('to')
    .exec()
  }

  findById (id) {
    return this.Schema
    .findById(id)
    .populate('from')
    .populate('to')
    .exec()
  }

  count (uid, cb) {
    let con = {
      to: uid
    }
    this.Schema.count(con, (err, total) => {
      if (err) console.error(err)
      con.unread = 1
      this.Schema.count(con, (err, unread) => {
        if (err) console.error(err)
        cb({
          total: total,
          unread: unread
        })
      })
    })
  }
}

module.exports = new NotificationModel(Schema)
