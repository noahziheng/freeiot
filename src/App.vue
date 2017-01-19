<template>
  <div id="app">
    <mu-appbar title="FreeIOT控制台">
      <mu-icon-button icon='menu' slot="left" @click="drawer_toggle()"/>
      <userMenu slot="right"></userMenu>
    </mu-appbar>
    <app-nav @close="drawer_toggle" :open="open" :docked="docked" />
    <!-- 路由出口 -->
    <!-- 路由匹配到的组件将渲染在这里 -->
    <router-view></router-view>
  </div>
</template>

<script>
import userMenu from './components/userMenu'
import AppNavDrawer from './components/AppNavDrawer'

export default {
  name: 'app',
  data () {
    return {
      open: false,
      docked: false
    }
  },
  components: {
    userMenu,
    'app-nav': AppNavDrawer
  },
  computed: {
    user () {
      return this.$store.state.user
    }
  },
  created: function () {
    let localLogined = false
    if (window.localStorage && window.fetch) {
      if (window.localStorage.user) {
        localLogined = true
        let obj = JSON.parse(window.localStorage.user)
        this.$store.commit('login', obj) // 一次本地授权
        // 二次对本地授权中权限进行重新校验
        fetch(this.$root.apiurl + '/user/' + obj.id + '?token=' + obj.token).then(res => res.json()).then(json => {
          if (json.msg === undefined) {
            obj.role = json.role
            this.$store.commit('login', obj)
            window.localStorage.user = JSON.stringify(obj)
          }
        })
      }
      if (window.localStorage.nodev) this.$store.commit('nodev', JSON.parse(window.localStorage.nodev))
    } else {
      this.$store.commit('error', '您的浏览器不支持HTML5特性,可能造成使用不便')
    }
    this.$store.dispatch('getMods')
    if (this.$route.path !== ('/login' && '/reg') && (!this.$store.user && !localLogined)) {
      this.$router.push('/login')
    }
  },
  methods: {
    drawer_toggle () {
      this.open = !this.open
    }
  }
}
</script>

<style>
</style>
