<template>
  <div>
    <mu-flat-button :label="email" ref="button" @click="toggle"></mu-flat-button>
    <mu-popover :trigger="trigger" :open="open" @close="handleClose">
      <mu-menu>
        <mu-menu-item title="发送反馈" @click="dialog_open(2)" />
        <mu-menu-item title="登出" @click="dialog_open(1)" />
      </mu-menu>
    </mu-popover>
    <mu-dialog :open="dialog" :title="dialog_title" @close="dialog_close">
      <span v-if="dialog_page === 1">你确定要登出吗？</span>
      <span v-if="dialog_page === 2">点击确定前往Github发表意见，请遵守<a href="https://gold.xitu.io/entry/5801d009570c35006c6ed781">《提问的艺术》</a></span>
      <mu-flat-button slot="actions" @click="dialog_close" primary label="取消"/>
      <mu-flat-button slot="actions" primary @click="final" label="确定"/>
    </mu-dialog>
  </div>
</template>


<script>
export default {
  data () {
    return {
      open: false,
      trigger: null,
      dialog: false,
      dialog_title: '',
      dialog_page: 1
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
    dialog_open (page) {
      this.dialog_page = page
      if (page === 1) this.dialog_title = '登出系统'
      else if (page === 2) this.dialog_title = '意见反馈'
      this.dialog = true
    },
    dialog_close () {
      this.dialog = false
    },
    final () {
      this.dialog_close()
      if (this.dialog_page === 1) {
        this.$store.commit('logout')
        window.localStorage.clear()
        this.$router.push('/login')
      } else if (this.dialog_page === 2) {
        location.href = 'https://github.com/noahziheng/freeiot/issues'
      }
    }
  }
}
</script>

<style scoped>
</style>