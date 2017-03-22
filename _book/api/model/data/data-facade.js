const Model = require('../../lib/facade')
const dataSchema = require('./data-schema')

class DataModel extends Model {}

module.exports = new DataModel(dataSchema)
