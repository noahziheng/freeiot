module.exports = {
  error (state, payload) {
    state.error = payload
  },
  login (state, payload) {
    state.user = payload
  },
  logout (state, payload) {
    state.user = false
  },
  nodev (state, payload) {
    state.nodev = payload
  },
  reqdev (state, payload) {
    state.user.role = payload.type
    state.user.token = payload.token
    window.localStorage.user = JSON.stringify(state.user)
  },
  getmods (state, payload) {
    state.mods = payload
  }
}
