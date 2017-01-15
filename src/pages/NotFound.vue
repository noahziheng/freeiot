<template>
  <div class="hello">
    <div class="hello-guide">
      <h2> ╮(๑•́ ₃•̀๑)╭</h2>
      <h5>你要找的家伙可能跑掉了～回去看看别的吧</h5>
      <mu-raised-button href="/" label="返回首页" primary/>
    </div>
  </div>
</template>

<script>
export default {
  name: 'notfound'
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.hello {
  margin-top: 30px;
  text-align: center;
}
.hello-guide {
  margin-top: 10%;
  margin-bottom: 10%;
}
.hello-guide h5 {
  margin-top: 2%;
  margin-bottom: 1%;
}
</style>
