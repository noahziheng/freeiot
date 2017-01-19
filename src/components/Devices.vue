<template>
  <div class="hello-guide">
    <h1 class="hello-icon">(=・ω・=)</h1>
    <h2>欢迎！你还没有设备，添加一个吧～</h2>
    <mu-raised-button label="添加新设备" @click="$parent.$parent.$parent.open" secondary/>
    <mu-raised-button label="查阅文档" primary/>
  </div>
</template>


<script>
export default {
  data () {
    return {
    }
  },
  computed: {
    user () {
      return this.$store.state.user
    }
  },
  created () {
  },
  methods: {
  }
}
</script>

<style>
.hello-icon {
  font-size:50px;
  margin-bottom: 0.1%;
}
.hello-guide {
  margin-top: 15%;
  margin-bottom: 15%;
}
</style>