class JSONDriver {
  parse (type, mod, data) {
    let result = {}
    data = JSON.parse(data)
    for (let index in data) {
      for (let i in mod[type]) {
        if (mod[type][i].label === index) result[mod[type][i].label] = data[index]
      }
    }
    return result
  }

  encode (obj) {
    return JSON.stringify(obj)
  }
}

module.exports = new JSONDriver()
