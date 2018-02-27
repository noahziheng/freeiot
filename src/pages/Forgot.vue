<template>
  <div class="user-row">
    <mu-row gutter>
      <mu-col width="0" tablet="0" desktop="35"></mu-col>
      <mu-col width="80" tablet="50" desktop="30">
        <mu-card class="user-card">
          <mu-card-title title="申请重置密码"/>
          <mu-card-text class="user-card-content">
            <mu-text-field label="E-mail" type="email" labelFloat v-model="email"/><br/>
          </mu-card-text>
          <mu-card-actions>
            <mu-flat-button label="提交" @click="submit" primary/>
            <mu-flat-button label="返回登录页" @click="$router.push('/login')"/>
          </mu-card-actions>
        </mu-card>
      </mu-col>
      <mu-col width="0" tablet="0" desktop="35"></mu-col>
    </mu-row>
    <mu-dialog :open="dialog" title="邮箱验证">
      验证邮件已发到您的邮箱，请您按邮件内提示进行下一步
      <mu-flat-button label="确定" slot="actions" @click="close" primary/>
    </mu-dialog>
  </div>
</template>

<script>
export default {
  name: 'forgot',
  data () {
    return {
      email: '',
      dialog: false
    }
  },
  methods: {
    submit: function (event) {
      fetch(this.$root.apiurl + '/user/forgot', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          email: this.email
        })
      }).then(res => {
        return res.json()
      }).then(json => {
        if (json.msg !== undefined) this.$store.commit('error', '系统错误（ ' + json.msg + ' ）')
        else this.dialog = true
      }).catch(ex => {
        console.log('parsing failed', ex)
      })
    },
    close: function () {
      this.dialog = false
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.user-row {
  margin-top: 1.5%;
}
.user-card {
  margin-top: 2.5%;
  margin-bottom: 2%;
}
.user-card-content {
  padding-top: 5px;
  padding-bottom: 5px;
}
</style>
