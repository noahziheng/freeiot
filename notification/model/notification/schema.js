const mongoose = require('mongoose')
const Schema = mongoose.Schema
const shortId = require('shortid')
const timestamps = require('mongoose-timestamp')

const deviceSchema = new Schema({
  _id: {
    type: String,
    unique: true,
    default: shortId.generate
  },
  from: {
    type: String,
    ref: 'Device',
    required: true
  },
  to: {
    type: String,
    ref: 'User',
    required: true
  },
  content: {
    type: String,
    required: true
  },
  unread: { type: Number, default: 1 } // 设备状态 0-已读 1-未读
})
deviceSchema.plugin(timestamps, { createdAt: 'created_at' })

module.exports = mongoose.model('Notification', deviceSchema)
