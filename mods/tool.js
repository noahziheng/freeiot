const fs = require('fs')
const mu = require('mustache')

module.exports = function (id, vars, hidden = false) {
  const index = JSON.parse(fs.readFileSync('./mods/index.json').toString())
  let result = mu.render(fs.readFileSync('./mods/' + id + '.json').toString(), vars)
  result = JSON.parse(result)
  if (hidden !== false) {
    const e = result.uplink ? result.uplink.length : 0
    for (let i in hidden) {
      if (hidden[i] < e) {
        result.uplink.splice(hidden[i], 1)
      } else {
        result.downlink.splice(hidden[i] - e, 1)
      }
    }
  }
  result.name = index[id]
  return result
}
