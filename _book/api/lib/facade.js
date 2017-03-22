class Facade {
  constructor (Schema) {
    this.Schema = Schema
  }

  create (input) {
    const schema = new this.Schema(input)
    console.log(schema)
    console.log(new Date())
    return schema.save()
  }

  update (conditions, update) {
    return this.Schema
    .update(conditions, update, { new: true })
    .exec()
  }

  find (query) {
    return this.Schema
    .find(query)
    .exec()
  }

  findOne (query) {
    return this.Schema
    .findOne(query)
    .exec()
  }

  findById (id) {
    return this.Schema
    .findById(id)
    .exec()
  }

  remove (id) {
    return this.Schema
    .findByIdAndRemove(id)
    .exec()
  }
}

module.exports = Facade
