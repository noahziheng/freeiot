<template>
  <div id="app">
    <template>
      <mu-appbar title="Title">
        <mu-icon-button icon='menu' slot="left" @click="drawer_toggle()"/>
        <mu-icon-button icon='more_horiz' slot="right"/>
      </mu-appbar>
      <mu-drawer :open="drawer_open" :docked="false" @close="drawer_toggle()">
        <mu-list>
          <mu-list-item title="Menu Item 1"/>
          <mu-list-item title="Menu Item 2"/>
          <mu-list-item title="Menu Item 3"/>
        </mu-list>
      </mu-drawer>
    </template>
    <!--<img src="./assets/logo.png">-->
    <!--<hello></hello>-->
    <p>
      <!-- 使用 router-link 组件来导航. -->
      <!-- 通过传入 `to` 属性指定链接. -->
      <!-- <router-link> 默认会被渲染成一个 `<a>` 标签 -->
      <router-link to="/foo">Go to Foo</router-link>
      <router-link to="/bar">Go to Bar</router-link>
    </p>
    <!-- 路由出口 -->
    <!-- 路由匹配到的组件将渲染在这里 -->
    <router-view></router-view>
  </div>
</template>

<script>
import Hello from './components/Hello'

export default {
  name: 'app',
  data () {
    return {
      drawer_open: false
    }
  },
  methods: {
    drawer_toggle () {
      this.drawer_open = !this.drawer_open
    }
  },
  components: {
    Hello
  }
}
</script>

<style>
</style>
