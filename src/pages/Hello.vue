<template>
  <div class="hello">
    <mu-row gutter>
      <mu-col width="100" tablet="60" desktop="75">
        <div class="hello-guide">
          <h1 class="hello-icon">(=・ω・=)</h1>
          <h2>欢迎！你还没有设备，添加一个吧～</h2>
          <mu-raised-button label="添加新设备" secondary/>
          <mu-raised-button label="查阅文档" primary/>
        </div>
      </mu-col>
      <mu-col width="100" tablet="40" desktop="25">
        <Developer v-if="role < 2 && !nodev" :role="role" />
        <mu-card class="hello-card">
          <mu-card-actions>
            <mu-flat-button label="添加新设备" primary/>
          </mu-card-actions>
          <mu-card-actions v-if="role >= 2">
            <mu-flat-button label="添加新产品" secondary/>
          </mu-card-actions>
        </mu-card>
      </mu-col>
    </mu-row>
  </div>
</template>

<script>
import Developer from '../components/Developer'

export default {
  name: 'hello',
  data () {
    return {
    }
  },
  components: {
    Developer
  },
  computed: {
    email: function () {
      return this.$store.state.user.email
    },
    role: function () {
      return this.$store.state.user.role
    },
    nodev: function () {
      return this.$store.state.nodev
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.hello {
  margin-top: 0.5%;
}
.hello-icon {
  font-size:50px;
  margin-bottom: 0.1%;
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
.tabs{
  background-color: transparent;
  color: #212121;
  margin-bottom: 16px;
}
.tabs .mu-tab-link-highlight {
  background-color: #ff5252;
}
.tabs .mu-tab-link{
  color: #212121;
}
.tabs .mu-tab-active{
  color: #212121;
}
</style>
