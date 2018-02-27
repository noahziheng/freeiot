<template>
  <div class="user-row">
    <mu-row gutter>
      <mu-col width="0" tablet="0" desktop="35"></mu-col>
      <mu-col width="80" tablet="50" desktop="30">
        <mu-card class="user-card">
          <mu-card-title title="完成账号验证"/>
          <mu-card-text class="user-card-content">
            <mu-text-field label="E-mail" type="email" labelFloat v-model="email" disabled/><br/>
            <mu-text-field label="新密码" type="password" labelFloat v-model="new_password"/><br/>
            <mu-text-field label="新密码确认" type="password" labelFloat v-model="re_new_password" :errorText="new_password !== re_new_password ? '两次输入密码不一致' : ''"/><br/>
          </mu-card-text>
          <mu-card-actions>
            <mu-flat-button label="提交" @click="submit" primary/>
          </mu-card-actions>
        </mu-card>
      </mu-col>
      <mu-col width="0" tablet="0" desktop="35"></mu-col>
    </mu-row>
  </div>
</template>

<script>
export default {
  name: 'finish',
  data () {
    return {
      email: '',
      new_password: '',
      re_new_password: ''
    }
  },
  created () {
    fetch(this.$root.apiurl + '/user/finish/' + this.$route.params.id, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({})
    }).then(res => {
      return res.json()
    }).then(json => {
      if (json.msg !== undefined) this.$store.commit('error', '系统错误（ ' + json.msg + ' ）')
      else {
        this.email = json.email
      }
    }).catch(ex => {
      console.log('parsing failed', ex)
    })
  },
  methods: {
    submit: function (event) {
      if (this.new_password === this.re_new_password && this.email !== '' && this.new_password !== '' && this.re_new_password !== '') {
        fetch(this.$root.apiurl + '/user/finishforgot/' + this.$route.params.id, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({password: this.new_password})
        }).then(res => {
          return res.json()
        }).then(json => {
          if (json.msg !== undefined) this.$store.commit('error', '系统错误（ ' + json.msg + ' ）')
          else {
            this.$store.commit('logout')
            window.localStorage.clear()
            this.$router.push('/login')
          }
        }).catch(ex => {
          console.log('parsing failed', ex)
        })
      }
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
