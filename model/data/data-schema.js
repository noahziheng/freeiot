const mongoose = require('mongoose')
const Schema = mongoose.Schema
const shortId = require('shortid')

const dataSchema = new Schema({
  _id: {
    type: String,
    unique: true,
    default: shortId.generate
  },
  type: { type: Number, required: true }, // 0-上行报告 1-下行指令
  device: { type: String, required: true },
  point: { type: String, required: true },
  content: { type: mongoose.Schema.Types.Mixed, required: true },
  created_at: { type: Date, default: new Date() }
})

module.exports = mongoose.model('Data', dataSchema)
