const mongoose = require('mongoose')
const Schema = mongoose.Schema
const shortId = require('shortid')
const ctyptopass = require('../../lib/cryptopass')
const timestamps = require('mongoose-timestamp')

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
  logo: { type: String, default: '/images/default_logo.png' }
})
productSchema.plugin(timestamps, {
  createdAt: 'created_at',
  updatedAt: 'updated_at'
})

module.exports = mongoose.model('Product', productSchema)
