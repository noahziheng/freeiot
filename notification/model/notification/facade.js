const Model = require('../../../lib/facade')
const Schema = require('./schema')

class NotificationModel extends Model {
 // 重写3个find，加入用户的关联查询
  find (query) {
    return this.Schema
    .find(query)
    .populate('from')
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

  update (conditions, update) {
    return this.Schema
    .update(conditions, update, { multi: true })
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

  getList (uid, cb) {
    let t = {}
    let r = []
    this.Schema.find({to: uid})
    .populate('from')
    .select('from unread')
    .exec()
    .then(doc => {
      for (let index in doc) {
        console.log(doc[index].unread)
        if (doc[index].from === null && !t['SYS']) {
          t['SYS'] = r.push({
            _id: 'SYS',
            name: 'FreeIOT',
            status: 3,
            unread: (doc[index].unread === 0) ? 0 : 1
          }) - 1
        } else if (doc[index].from !== null && t[doc[index].from._id] === undefined) {
          t[doc[index].from._id] = r.push({
            _id: doc[index].from._id,
            name: doc[index].from.name,
            status: doc[index].from.status,
            unread: (doc[index].unread === 0) ? 0 : 1
          }) - 1
        } else {
          if (doc[index].from === null) {
            doc[index].from = {
              _id: 'SYS'
            }
          }
          if (doc[index].unread === 1) r[t[doc[index].from._id]].unread++
        }
      }
      cb(r)
    })
  }
}

module.exports = new NotificationModel(Schema)
