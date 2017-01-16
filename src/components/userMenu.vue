<template>
  <div>
    <mu-flat-button :label="email" ref="button" @click="toggle"></mu-flat-button>
    <mu-popover :trigger="trigger" :open="open" @close="handleClose">
      <mu-menu>
        <mu-menu-item title="发送反馈" />
        <mu-menu-item title="用户设置" />
        <mu-menu-item title="登出" @click="dialog_open()" />
      </mu-menu>
    </mu-popover>
    <mu-dialog :open="dialog" title="登出系统" @close="dialog_close">
      你确定要登出吗？
      <mu-flat-button slot="actions" @click="dialog_close" primary label="取消"/>
      <mu-flat-button slot="actions" primary @click="logout" label="确定"/>
    </mu-dialog>
  </div>
</template>


<script>
export default {
  data () {
    return {
      open: false,
      trigger: null,
      dialog: false
    }
  },
  computed: {
    email: function () {
      return this.$store.state.user.email
    }
  },
  mounted () {
    this.trigger = this.$refs.button.$el
  },
  methods: {
    toggle () {
      this.open = !this.open
    },
    handleClose (e) {
      this.open = false
    },
    dialog_open () {
      this.dialog = true
    },
    dialog_close () {
      this.dialog = false
    },
    logout () {
      this.dialog_close()
      this.$store.commit('logout')
      window.localStorage.clear()
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
</style>