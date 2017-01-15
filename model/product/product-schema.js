const mongoose = require('mongoose')
const Schema = mongoose.Schema
const shortId = require('shortid')
const updateTimestamps = require('mongoose-timestamps.js')
const ctyptopass = require('../../lib/cryptopass')

const productSchema = new Schema({
  _id: {
    type: String,
    unique: true,
    default: shortId.generate
  },
  name: { type: String, required: true },
  commit: { type: String, default: 'A new project!' },
  readme: { type: String },
  secret: { type: String, default: ctyptopass, select: false },
  owner: {
    type: String,
    ref: 'User',
    required: true
  },
  mods: [{
    origin: String,
    remark: String,
    vars: {},
    hidden: []
  }],
  logo: { type: String, default: '/images/default_logo.png' },
  created_at: { type: Date, default: new Date() },
  updated_at: { type: Date }
})

productSchema.pre('save', updateTimestamps('updated_at')) // 时间戳管理钩子

module.exports = mongoose.model('Product', productSchema)
