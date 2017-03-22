const mongoose = require('mongoose')
const Schema = mongoose.Schema
const shortId = require('shortid')
const ctyptopass = require('../../lib/cryptopass')
const timestamps = require('mongoose-timestamp')

const userSchema = new Schema({
  _id: {
    type: String,
    unique: true,
    default: shortId.generate
  },
  email: { type: String, required: true, unique: true },
  password: { type: String, required: true, select: false },
  dev: {
    name: { // 姓名
      first: String,
      last: String
    },
    work: String, // 工作
    company: String, // 工作单位/学校
    location: String,
    reason: String
  },
  role: { type: Number, default: 0, min: 0, max: 3 } // 权限 0-用户 1-开发者(待审核) 2-开发者 3-管理员
})
userSchema.plugin(timestamps, {
  createdAt: 'created_at',
  updatedAt: 'updated_at'
})

userSchema.pre('save', function (next) {
  if (this.password !== undefined) {
    this.password = ctyptopass(this.password) // 密码加密存储
  }
  next()
})

module.exports = mongoose.model('User', userSchema)