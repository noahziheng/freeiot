<template>
  <div>
    <mu-appbar :title="title">
      <mu-icon-button
        icon="keyboard_arrow_left"
        slot="left"
        v-if="page !== 0 || appMode"
        @click="returnBtn"/>
      <mu-icon-menu icon="more_vert" slot="right">
        <mu-menu-item title="通知设置" @click="menuSetting"/>
      </mu-icon-menu>
    </mu-appbar>
    <mu-list v-if="page === 0">
      <mu-list-item title="系统通知" @click="enterChat('SYS', 'FreeIOT')">
        <mu-avatar slot="after" backgroundColor="red700" :size="28" v-if="sys_unread !== 0">{{sys_unread}}</mu-avatar>
        <mu-icon value="chevron_right" slot="right"/>
      </mu-list-item>
      <mu-sub-header>设备消息</mu-sub-header>
      <mu-list-item :title="item.name" v-for="(item, index) in lists" @click="enterChat(item._id, item.name)">
        <mu-avatar slot="after" backgroundColor="red700" :size="28" v-if="item.unread !== 0">{{item.unread}}</mu-avatar>
        <mu-icon value="chevron_right" slot="right"/>
      </mu-list-item>
    </mu-list>
    <mu-list v-if="page === 1">
      <mu-list-item
        :title="item.content"
        :describeText="item.created_at | getDateTime"
        v-for="(item,index) in notifications">
        <mu-icon value="delete_forever" slot="right" @click="openDialog(index)"/>
      </mu-list-item>
    </mu-list>
    <mu-list v-if="page === 2">
      <mu-sub-header>消息推送</mu-sub-header>
      <mu-list-item disableRipple @click="handleToggle('system')" title="系统消息">
        <mu-switch v-model="system"  slot="right"/>
      </mu-list-item>
      <mu-list-item disableRipple @click="handleToggle('normal')" title="普通消息">
        <mu-switch v-model="normal"  slot="right"/>
      </mu-list-item>
      <mu-list-item disableRipple @click="handleToggle('special')" title="特别消息">
        <mu-switch v-model="special"  slot="right"/>
      </mu-list-item>
      <mu-list-item disableRipple @click="handleToggle('warning')" title="警告消息">
        <mu-switch v-model="warning"  slot="right"/>
      </mu-list-item>
    </mu-list>
    <mu-dialog :open="dialog" title="删除确认">
      真的要删吗？～
      <mu-flat-button label="取消" slot="actions" @click="closeDialog" primary/>
      <mu-flat-button label="确定" slot="actions" @click="deleteItem" primary/>
    </mu-dialog>
  </div>
</template>

<script>
import readable from 'readable-shijian'

export default {
  name: 'notifications',
  data () {
    return {
      page: 0,
      title: '通知',
      lists: [],
      sys_unread: 0,
      notifications: [],
      deleteIndex: -1,
      dialog: false,
      appMode: false,
      // Push Setting
      system: false,
      normal: false,
      special: false,
      warning: false
    }
  },
  components: {
  },
  created () {
    if (this.$route.name === 'notifications') {
      this.$parent.hide = true
      let user = JSON.parse(atob(this.$route.params.token.split('.')[1]))
      this.$store.commit('login', {id: user.id, email: user.email, token: this.$route.params.token})
      this.appMode = true
    }
    this.getLists()
    this.getUserSetting()
  },
  computed: {
    user: function () {
      return this.$store.state.user
    }
  },
  watch: {
    system: function (val) { return this.handleSetting('system', val) },
    normal: function (val) { return this.handleSetting('normal', val) },
    special: function (val) { return this.handleSetting('special', val) },
    warning: function (val) { return this.handleSetting('warning', val) }
  },
  filters: {
    getDateTime (val) {
      return readable(val)
    }
  },
  methods: {
    getLists: function () {
      fetch(this.$root.apiurl + '/notification' + '?token=' + this.user.token).then(res => res.json()).then(json => {
        this.lists = []
        for (let i in json.lists) {
          if (json.lists[i]._id !== 'SYS') this.lists.push(json.lists[i])
          else this.sys_unread = json.lists[i].unread
        }
      }).catch(ex => {
        console.log('parsing failed', ex)
      })
    },
    getUserSetting: function () {
      fetch(this.$root.apiurl + '/user/' + this.user.id + '?token=' + this.user.token).then(res => res.json()).then(json => {
        this.system = json.setting.push.system
        this.normal = json.setting.push.normal
        this.special = json.setting.push.special
        this.warning = json.setting.push.warning
      }).catch(ex => {
        console.log('parsing failed', ex)
      })
    },
    sortCreate (a, b) {
      if (a.created_at < b.created_at) {
        return 1
      }
      if (a.created_at > b.created_at) {
        return -1
      }
      // a 必须等于 b
      return 0
    },
    handleToggle: function (name) {
      this[name] = !this[name]
    },
    handleSetting: function (name, val) {
      let t = {
        push: {
          system: this.system,
          normal: this.normal,
          special: this.special,
          warning: this.warning
        }
      }
      t.push[name] = val
      fetch(this.$root.apiurl + '/user/' + this.$store.state.user.id + '?token=' + this.$store.state.user.token, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({setting: t})
      }).then(res => {
        return res.json()
      }).then(json => {
        if (json.msg !== undefined) this.$store.commit('error', json.msg)
        return true
      }).catch(ex => {
        console.log('parsing failed', ex)
      })
    },
    returnBtn: function () {
      if (this.appMode && this.page === 0) window.androidAPP.returnBtn()
      else this.changePage(0)
    },
    menuSetting: function () {
      if (this.appMode) window.androidAPP.pushSetting()
      else this.changePage(2)
    },
    changePage: function (page, name) {
      this.page = page
      if (page === 0) {
        this.title = '通知'
        this.getLists()
      } else if (page === 1 && name) {
        this.title = '来自 ' + name + ' 的通知'
      } else if (page === 2) {
        this.getUserSetting()
        this.title = '通知设置'
      }
    },
    enterChat: function (id, name) {
      fetch(this.$root.apiurl + '/notification/chat/' + id + '?token=' + this.user.token).then(res => res.json()).then(json => {
        this.notifications = json.sort(this.sortCreate)
        fetch(this.$root.apiurl + '/notification/unread/' + id + '?token=' + this.user.token).then(res => res.json()).then(json => {}).catch(ex => {
          console.log('parsing failed', ex)
        })
      }).catch(ex => {
        console.log('parsing failed', ex)
      })
      this.changePage(1, name)
    },
    openDialog (val) {
      this.deleteIndex = val
      this.dialog = true
    },
    closeDialog () {
      this.dialog = false
    },
    deleteItem () {
      fetch(this.$root.apiurl + '/notification/' + this.notifications[this.deleteIndex]._id + '?token=' + this.user.token, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json'
        }
      }).then(res => res.json()).then(json => {
        if (json.msg !== undefined) this.$store.commit('error', '提交失败（ ' + json.msg + ' ）')
        else {
          this.notifications.splice(this.deleteIndex, 1)
          this.closeDialog()
        }
      }).catch(ex => {
        console.log('parsing failed', ex)
      })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
