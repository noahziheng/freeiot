<template>
  <div>
    <mu-dialog :open="dialog" title="发生错误">
      {{ error }}
      <mu-flat-button label="确定" slot="actions" @click="close" primary/>
    </mu-dialog>
  </div>
</template>


<script>
export default {
  data () {
    return {
      error: '',
      dialog: false
    }
  },
  created () {
    this.$store.watch(state => state.error, (val, old) => {
      if (val !== '') {
        this.error = val
        this.open()
        this.$store.commit('error', '')
      }
    })
  },
  methods: {
    open () {
      this.dialog = true
    },
    close () {
      this.dialog = false
    }
  }
}
</script>

<style>
</style>