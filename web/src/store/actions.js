import apiurl from '../apiurl.json'

const fetch = window.fetch

module.exports = {
  login ({ commit }, payload) {
    fetch(apiurl + '/user/auth', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(payload)
    }).then(res => {
      return res.json()
    }).then(json => {
      if (json.msg !== undefined) commit('error', '登录失败（ ' + json.msg + ' ）')
      else {
        commit('login', json)
        json.expTime = new Date().getTime()
        window.localStorage.user = JSON.stringify(json)
      }
    }).catch(ex => {
      console.log('parsing failed', ex)
    })
  },
  getMods ({ commit }) {
    fetch(apiurl + '/mod').then(res => {
      return res.json()
    }).then(json => {
      if (json.msg !== undefined) commit('error', '数据同步失败（ ' + json.msg + ' ）')
      else commit('getmods', json)
    }).catch(ex => {
      console.log('parsing failed', ex)
    })
  }
}
