<template>
  <div class="hello">
    <mu-row gutter>
      <mu-col width="100" tablet="60" desktop="75">
        <div class="hello-guide">
          <h2>(=・ω・=)</h2>
          <p>欢迎！你还没有设备，添加一个吧～</p>
          <mu-raised-button label="添加新设备" secondary/>
          <mu-raised-button label="查阅文档" primary/>
        </div>
      </mu-col>
      <mu-col width="100" tablet="40" desktop="25">
        <mu-card class="hello-card">
          <mu-card-title title="你还不是开发者哦！" subTitle="由于服务器资源有限，你需要申请成为开发者才能建立自己的产品～"/>
          <mu-card-actions>
            <mu-flat-button label="立即申请"/>
            <mu-flat-button label="别再烦我"/>
          </mu-card-actions>
        </mu-card>
        <mu-card class="hello-card">
          <mu-card-actions>
            <mu-flat-button label="添加新设备" primary/>
          </mu-card-actions>
          <mu-card-actions>
            <mu-flat-button label="添加新产品" secondary/>
          </mu-card-actions>
        </mu-card>
      </mu-col>
    </mu-row>
  </div>
</template>

<script>
export default {
  name: 'hello',
  data () {
    return {
      msg: 'Welcome to Your Vue.js App'
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.hello {
  margin-top: 0.5%;
}
.hello-guide {
  margin-top: 15%;
  margin-bottom: 15%;
}
.hello-card {
  margin-top: 2%;
  margin-bottom: 2%;
}
.hello div[class*="col-"] {
  background: #fff;
  text-align: center;
  padding: 8px;
}
</style>
