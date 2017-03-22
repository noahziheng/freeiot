<template>
  <div class="hello">
    <mu-row gutter>
      <mu-col width="100" tablet="60" desktop="75">
        <mu-tabs class="hello-tabs" @change="handleTabChange" :value="tabValue">
          <mu-tab value="devices" title="我的设备"/>
          <mu-tab value="products" title="产品原型广场"/>
          <mu-tab value="my-product" v-if="role >= 2" title="我的产品原型"/>
        </mu-tabs>
        <Devices v-show="tabValue === 'devices'"></Devices>
        <Products v-show="tabValue === 'products'"></Products>
        <Products v-show="tabValue === 'my-product'" v-if="role >= 2" :my="true"></Products>
      </mu-col>
      <mu-col width="100" tablet="40" desktop="25">
        <mu-card class="hello-card">
          <mu-card-title title="欢迎！"/>
          <mu-card-text>
            <span style="font-size: 28px;">{{email}}</span><br>
            <mu-badge v-if="role === 0" content="用户" class="status-0"/>
            <mu-badge v-if="role === 1" content="开发者（等待审核）" class="status-1"/>
            <mu-badge v-if="role === 2" content="开发者" class="status-2"/>
            <mu-badge v-if="role === 3" content="管理员" class="status-3"/>
          </mu-card-text>
          <mu-card-actions>
            <mu-flat-button label="修改密码" @click="passwordDialogOpen" secondary/>
          </mu-card-actions>
        </mu-card>
        <Developer v-if="role < 2 && !nodev" :role="role" />
        <mu-card class="hello-card">
          <mu-card-actions>
            <mu-flat-button label="添加新设备" @click="open" primary/>
          </mu-card-actions>
          <mu-card-actions v-if="role >= 2">
            <mu-flat-button label="添加新产品" @click="$router.push('/newproduct')" secondary/>
          </mu-card-actions>
        </mu-card>
      </mu-col>
    </mu-row>
    <mu-dialog :open="dialog" title="添加新设备">
      请在产品广场或我的产品原型中选择想要添加的设备，在产品详情页添加新设备入网
      <mu-flat-button label="确定" slot="actions" @click="close" primary/>
    </mu-dialog>
    <mu-dialog :open="passwordDialog" title="修改密码">
      <mu-text-field label="旧密码" type="password" labelFloat v-model="old_password" :errorText="old_error" @focus="this.old_error = ''"/><br/>
      <mu-text-field label="新密码" type="password" labelFloat v-model="new_password"/><br/>
      <mu-text-field label="新密码确认" type="password" labelFloat v-model="re_new_password" :errorText="new_password !== re_new_password ? '两次输入密码不一致' : ''"/><br/>
      <mu-flat-button label="取消" slot="actions" @click="passwordDialogClose" primary/>
      <mu-flat-button label="确定" slot="actions" @click="passwordSubmit" primary/>
    </mu-dialog>
  </div>
</template>

<script>
import Developer from '../components/Developer'
import Devices from '../components/Devices'
import Products from '../components/Products'

export default {
  name: 'hello',
  data () {
    return {
      dialog: false,
      passwordDialog: false,
      tabValue: 'devices',
      old_password: '',
      new_password: '',
      re_new_password: '',
      old_error: ''
    }
  },
  components: {
    Developer,
    Devices,
    Products
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
  },
  methods: {
    passwordDialogOpen () {
      this.passwordDialog = true
    },
    passwordDialogClose () {
      this.passwordDialog = false
    },
    passwordSubmit () {
      if (this.new_password === this.re_new_password && this.old_password !== '' && this.new_password !== '' && this.re_new_password !== '') {
        let payload = {
          email: this.email,
          password: this.old_password
        }
        fetch(this.$root.apiurl + '/user/auth', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(payload)
        }).then(res => {
          return res.json()
        }).then(json => {
          if (json.msg !== undefined) this.old_error = '旧密码错误'
          else {
            fetch(this.$root.apiurl + '/user/' + this.$store.state.user.id + '?token=' + this.$store.state.user.token, {
              method: 'PUT',
              headers: {
                'Content-Type': 'application/json'
              },
              body: JSON.stringify({password: this.new_password})
            }).then(res => {
              return res.json()
            }).then(json => {
              if (json.msg !== undefined) this.old_error = '旧密码错误'
              else {
                this.$store.commit('logout')
                window.localStorage.clear()
                this.$router.push('/login')
                this.passwordDialog = false
              }
            }).catch(ex => {
              console.log('parsing failed', ex)
            })
          }
        }).catch(ex => {
          console.log('parsing failed', ex)
        })
      }
    },
    open () {
      this.handleTabChange('products')
      this.dialog = true
    },
    close () {
      this.dialog = false
    },
    handleTabChange (value) {
      this.tabValue = value
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style>
.hello {
  margin-top: 0.5%;
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
.hello-tabs{
  background-color: transparent;
  color: #474a4f;
  margin-bottom: 16px;
}
.hello-tabs .mu-tab-link-highlight {
  background-color: #474a4f;
}
.hello-tabs .mu-tab-link{
  color: #7e848c;
}
</style>
