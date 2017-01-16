const fs = require('fs')

class ModController {

  find (req, res, next) {
    res.status(200).json(JSON.parse(fs.readFileSync('./mods/index.json').toString()))
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
