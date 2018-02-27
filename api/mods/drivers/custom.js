const vm = require('vm')

class CustomDriver {
  parse (type, mod, data) {
    let result = {}
    let point = []
    for (let i in mod[type]) {
      point.push(mod[type][i])
    }
    const sandbox = {point: point, data: data, result: result, format: this.format, JSON: JSON}
    vm.createContext(sandbox)
    vm.runInContext(mod.driver_custom, sandbox)
    return result
  }

  encode (obj) {
  }
}

module.exports = new CustomDriver()
