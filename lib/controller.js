class Controller {
  constructor (facade) {
    this.facade = facade
  }

  find (req, res, next) {
    return this.facade.find(req.query)
    .then(collection => res.status(200).json(collection))
    .catch(err => res.status(500).json({ msg: err.message, error: err }))
  }

  findOne (req, res, next) {
    return this.facade.findOne(req.query)
    .then(doc => res.status(200).json(doc))
    .catch(err => res.status(500).json({ msg: err.message, error: err }))
  }

  findById (req, res, next) {
    return this.facade.findById(req.params.id)
    .then(doc => {
      if (!doc) { return res.status(404).end() }
      return res.status(200).json(doc)
    })
    .catch(err => res.status(500).json({ msg: err.message, error: err }))
  }

  create (req, res, next) {
    this.facade.create(req.body)
    .then(doc => res.status(201).json(doc))
    .catch(err => res.status(500).json({ msg: err.message, error: err }))
  }

  update (req, res, next) {
    this.facade.findById(req.params.id)
    .then(doc => {
      if (req.user.role <= 2 && doc._id !== req.params.id) return res.status(401).json({ msg: 'Unauthorized' }) // HTTP 401 无更新权限
      const conditions = { _id: req.params.id }
      this.facade.update(conditions, req.body)
      .then(doc => {
        if (!doc) return res.status(404).json({ msg: 'Not Found!' })
        return res.status(200).json(doc)
      })
      .catch(err => res.status(500).json({ msg: err.message, error: err }))
    })
    .catch(err => res.status(500).json({ msg: err.message, error: err }))
  }

  remove (req, res, next) {
    this.facade.findById(req.params.id)
    .then(doc => {
      console.log(doc)
      if (req.user.role <= 2 && doc._id.toString() !== req.params.id) return res.status(401).json({ msg: 'Unauthorized' }) // HTTP 401 无更新权限
      this.facade.remove(req.params.id).then(doc => {
        if (!doc) { return res.status(404).json({ msg: 'Not Found!' }) }
        return res.status(200).json({ ok: 'Success!' })
      })
      .catch(err => res.status(500).json({ msg: err.message, error: err }))
    })
    .catch(err => res.status(500).json({ msg: err.message, error: err }))
  }

}

module.exports = Controller
