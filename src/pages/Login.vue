<template>
  <div class="user-row">
    <mu-row gutter>
      <mu-col width="0" tablet="0" desktop="35"></mu-col>
      <mu-col width="80" tablet="50" desktop="30">
        <mu-card class="user-card">
          <mu-card-title title="登录"/>
          <mu-card-text class="user-card-content">
            <mu-text-field label="E-mail" type="email" labelFloat v-model="email"/><br/>
            <mu-text-field label="密码" type="password" labelFloat v-model="password"/><br/>
          </mu-card-text>
          <mu-card-actions>
            <mu-flat-button label="登录" @click="login" primary/>
            <mu-flat-button label="注册" @click="$router.push('/reg')"/>
            <mu-flat-button label="忘记密码？" style="margin: 12px;" secondary @click="$router.push('/user/forgot')">
          </mu-card-actions>
        </mu-card>
      </mu-col>
      <mu-col width="0" tablet="0" desktop="35"></mu-col>
    </mu-row>
  </div>
</template>

<script>
export default {
  name: 'login',
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
  },
  methods: {
    login: function (event) {
      this.$store.dispatch('login', {
        email: this.email,
        password: this.password
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
