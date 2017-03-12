const mongoose = require('mongoose')
const Schema = mongoose.Schema
const shortId = require('shortid')
const ctyptopass = require('../../lib/cryptopass')
const timestamps = require('mongoose-timestamp')

const deviceSchema = new Schema({
  _id: {
    type: String,
    unique: true,
    default: shortId.generate
  },
  name: { type: String, required: true },
  secret: { type: String, default: ctyptopass, select: false },
  owner: {
    type: String,
    ref: 'User',
    required: true
  },
  product: {
    type: String,
    ref: 'Product',
    required: true
  },
  status: { type: Number, default: 1 } // 设备状态 0-预置 1-等待入网 2-离线 3-在线
})
deviceSchema.plugin(timestamps, {
  createdAt: 'created_at',
  updatedAt: 'updated_at'
})

module.exports = mongoose.model('Device', deviceSchema)
