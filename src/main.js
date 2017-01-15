// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import Vuex from 'vuex'
import VueRouter from 'vue-router'
import MuseUI from 'muse-ui'
import 'muse-ui/dist/muse-ui.css'
import 'muse-ui/dist/theme-carbon.css' // 使用 carbon 主题
import './assets/typography.min.css'
import './assets/font-icons/style.css'
import App from './App'
import Welcome from './Welcome'
import routes from './routes'

Vue.use(MuseUI)
Vue.use(VueRouter)
Vue.use(Vuex)

const router = new VueRouter({
  mode: 'history',
  routes: routes
})

/* eslint-disable no-new */
new Vue({
  el: '#app',
  data: {
    app: false
  },
  router,
  created: function () {
    if (this.$route.path !== '/') {
      this.app = true
    }
  },
  template: '<App v-if="app"/><Welcome v-else />',
  components: { App, Welcome }
})
