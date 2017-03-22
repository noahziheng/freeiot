const mongoose = require('mongoose')
const Schema = mongoose.Schema
const shortId = require('shortid')
const timestamps = require('mongoose-timestamp')

const dataSchema = new Schema({
  _id: {
    type: String,
    unique: true,
    default: shortId.generate
  },
  type: { type: Number, required: true }, // 0-上行报告 1-下行指令 2-数据反馈 3-系统级消息
  device: { type: String, required: true },
  label: { type: String, required: true },
  content: { type: mongoose.Schema.Types.Mixed, required: true }
})
dataSchema.plugin(timestamps, {
  createdAt: 'created_at',
  updatedAt: 'updated_at'
})
module.exports = mongoose.model('Data', dataSchema)
