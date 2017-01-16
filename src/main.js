// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import VueRouter from 'vue-router'
import MuseUI from 'muse-ui'
import 'muse-ui/dist/muse-ui.css'
import 'muse-ui/dist/theme-carbon.css' // 使用 carbon 主题
import './assets/font-icons/style.css'
import App from './App'
import Welcome from './Welcome'
import Alert from './components/Alert'
import routes from './routes'
import apiurl from './apiurl.json'
import store from './store'

Vue.use(MuseUI)
Vue.use(VueRouter)

const router = new VueRouter({
  mode: 'history',
  routes: routes
})

/* eslint-disable no-new */
new Vue({
  el: '#app',
  data () {
    return {
      apiurl: apiurl,
      app: false
    }
  },
  store,
  router,
  created () {
    if (this.$route.path !== '/') {
      this.app = true
    }
  },
  mounted () {
  },
  template: '<div><Alert /><App v-if="app"/><Welcome v-else /></div>',
  components: { App, Welcome, Alert }
})
