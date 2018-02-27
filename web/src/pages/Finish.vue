<template>
  <div class="user-row">
    <mu-row gutter>
      <mu-col width="0" tablet="0" desktop="35"></mu-col>
      <mu-col width="80" tablet="50" desktop="30">
        <mu-card class="user-card">
          <mu-card-title title="完成账号验证"/>
          <mu-card-text class="user-card-content">
            <mu-text-field label="E-mail" type="email" labelFloat v-model="email" disabled/><br/>
            <mu-text-field label="密码" type="password" labelFloat v-model="password"/><br/>
          </mu-card-text>
          <mu-card-actions>
            <mu-flat-button label="提交" @click="submit" primary/>
            <mu-flat-button label="返回注册页" @click="$router.push('/reg')"/>
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
      password: ''
    }
  },
  created () {
    if (this.$store.state.user !== false) {
      this.$router.push('/dashboard')
    }
    this.$store.watch(state => state.user, (val, old) => {
      if (val !== false) {
        this.$router.push('/dashboard')
      }
    })
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
      else this.email = json.email
    }).catch(ex => {
      console.log('parsing failed', ex)
    })
  },
  methods: {
    submit: function (event) {
      fetch(this.$root.apiurl + '/user/finish/' + this.$route.params.id, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          password: this.password
        })
      }).then(res => {
        return res.json()
      }).then(json => {
        if (json.msg !== undefined) this.$store.commit('error', '系统错误（ ' + json.msg + ' ）')
        else {
          this.$store.dispatch('login', {
            email: this.email,
            password: this.password
          })
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
