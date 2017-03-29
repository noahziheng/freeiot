const mongoose = require('mongoose')
const Schema = mongoose.Schema
const shortId = require('shortid')
const timestamps = require('mongoose-timestamp')

const rSchema = new Schema({
  _id: {
    type: String,
    unique: true,
    default: shortId.generate
  },
  name: { type: String, required: true },
  device: {
    type: String,
    ref: 'Device',
    required: true
  },
  condition: { type: String, required: true },
  template: { type: String, required: true }
})
rSchema.plugin(timestamps, {
  createdAt: 'created_at',
  updatedAt: 'updated_at'
})

module.exports = mongoose.model('Rule', rSchema)
