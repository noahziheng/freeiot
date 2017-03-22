const fs = require('fs')

class ModController {

  find (req, res, next) {
    let t = {}
    const index = JSON.parse(fs.readFileSync('./mods/index.json').toString())
    for (let i in index) {
      t[index[i]] = JSON.parse(fs.readFileSync('./mods/index/' + index[i] + '.json').toString())
    }
    res.status(200).json(t)
  }

  findById (req, res, next) {
    try {
      const result = require('../../mods/tool')(req.params.id, req.body, req.body.hidden)
      res.status(200).json(result)
    } catch (error) {
      res.status(500).json({ msg: error.message, error: error })
    }
  }
}

module.exports = new ModController()
