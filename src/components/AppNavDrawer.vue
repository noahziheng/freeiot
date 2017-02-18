<template>
<mu-drawer @close="handleClose" :open="open" :docked="docked" class="app-drawer" :zDepth="1">
  <mu-appbar class="drawer-nav-appbar" :zDepth="0">
    <a class="drawer-appbar-title" href="/" style="display:inline-block;">FreeIOT</a>
    <mu-badge v-if="version.tag !== 'Release'" :content="version.tag" class="drawer-version" secondary/>
  </mu-appbar>
  <mu-divider/>
  <mu-list>
    <mu-list-item href="/" title="首页"/>
    <mu-list-item href="/dashboard" title="控制面板"/>
    <mu-list-item v-if="$store.state.user.role === 3" href="/useradmin" title="用户管理面板"/>
    <mu-list-item href="//doc.iot.noahgao.net" title="文档"/>
    <mu-list-item href="//fir.im/3b9a" title="Android客户端"/>
    <mu-list-item href="//doc.iot.noahgao.net" title="ESP8266 Firmware"/>
  </mu-list>
  <mu-divider/>
  <mu-sub-header>友情链接</mu-sub-header>
  <mu-list>
    <mu-list-item href="https://museui.github.io/" target="_blank" title="MuseUI"/>
    <mu-list-item href="http://cn.vuejs.org/" target="_blank" title="VueJS"/>
  </mu-list>
  <mu-divider/>
  <mu-sub-header>版本信息</mu-sub-header>
  <div class="mu-version-box">
    <span class="mu-version-text">Version: v{{version.num}} {{version.tag}}</span>
  </div>
  <div class="mu-version-box">
    <span class="mu-version-text">Build: {{version.build}}</span>
  </div>
  <div class="mu-version-box">
    <span class="mu-version-text">API Version: {{apiVersion.version}} build{{apiVersion.build_version}}</span>
  </div>
</mu-drawer>
</template>

<script>
import Version from '../version.json'

export default {
  props: {
    open: {
      type: Boolean,
      default: true
    },
    docked: {
      type: Boolean,
      default: true
    }
  },
  data () {
    return {
      version: Version,
      apiVersion: {
        version: '',
        build_version: ''
      }
    }
  },
  created () {
    fetch(this.$root.apiurl).then(res => res.json()).then(json => {
      if (json.msg !== undefined) this.$store.commit('error', '查询失败（ ' + json.msg + ' ）')
      else this.apiVersion = json
    }).catch(ex => {
      console.log('parsing failed', ex)
    })
  },
  methods: {
    handleClose () {
      this.$emit('close')
    }
  }
}
</script>

<style>
.drawer{
  box-shadow: none;
  border-right: 1px solid #edeff2;
}

.drawer-nav-appbar.mu-appbar{
  background-color: #fff;
  color: #7e848c;
}
.drawer-appbar-title{
  color: #7e848c;
}

.drawer-version {
  margin-left: 10px;
  vertical-align: text-top;
}

.drawer-version .mu-badge {
  margin-top: 5%;
  font-size: 15px;
  background-color: #030e48;
}

.drawer-nav-sub-header {
  padding-left: 34px;
}

.mu-version-box{
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 48px;
  margin-top: 8px;
  padding-left: 16px;
  padding-right: 16px;
}
.mu-version-text {
  font-size: 16px;
  margin-top: 8px;
  width: 100%;
}
</style>