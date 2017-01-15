<template>
  <div id="app">
    <mu-appbar title="FreeIOT控制台">
      <mu-icon-button icon='menu' slot="left" @click="drawer_toggle()"/>
      <userMenu slot="right"></userMenu>
    </mu-appbar>
    <mu-drawer :open="drawer_open" :docked="false" @close="drawer_toggle()">
      <mu-list>
        <mu-list-item title="Menu Item 1"/>
        <mu-list-item title="Menu Item 2"/>
        <mu-list-item title="Menu Item 3"/>
      </mu-list>
    </mu-drawer>
    <!-- 路由出口 -->
    <!-- 路由匹配到的组件将渲染在这里 -->
    <router-view></router-view>
  </div>
</template>

<script>
import userMenu from './components/userMenu'
export default {
  name: 'app',
  data () {
    return {
      drawer_open: false
    }
  },
  components: {
    userMenu
  },
  methods: {
    drawer_toggle () {
      this.drawer_open = !this.drawer_open
    }
  }
}
</script>

<style>
</style>
