<template>
  <div>
    <div class="product-header">
      <mu-sub-header class="product-title">{{product.name}}</mu-sub-header>
      <mu-sub-header class="product-commit">{{product.commit}} by {{authorFullName}}({{product.owner.email}})</mu-sub-header>
      <mu-sub-header style="margin-left: 30px;margin-top: 10px;">模块列表</mu-sub-header>
      <AddMod class="product-mods" :mods="product.mods" :newF="false" />
      <div class="product-id-secret" v-if="user.role === 3 || user.id === product.owner._id">
        <mu-sub-header>产品标识</mu-sub-header>
        <mu-content-block>
          <mu-chip>ProductId: {{product._id}}</mu-chip>
          <mu-chip v-if="!viewSecret" @click="viewSecretMethod">Secret: 点击查看</mu-chip>
          <mu-chip class="copy-btn" v-if="viewSecret" :data-clipboard-text="product.secret" @click="showToast">{{product.secret}}</mu-chip>
          <mu-toast v-if="toast" message="Secret已复制" @close="hideToast"/>
          <mu-sub-header>温馨提示：点击Secret可复制</mu-sub-header>
        </mu-content-block>
      </div>
      <mu-tabs class="hello-tabs" @change="handleTabChange" :value="tabValue">
        <mu-tab value="readme" title="产品简介"/>
        <mu-tab value="devices" title="设备列表"/>
        <mu-tab value="product" title="产品原型配置" v-if="user.role === 3 || user.id === product.owner._id"/>
      </mu-tabs>
    </div>
    <div class="content">
      <vue-markdown v-show="tabValue === 'readme'" :source="product.readme" />
      <NewProduct v-if="user.role === 3 || user.id === product.owner._id" v-show="tabValue === 'product'" />
      <DevicesList v-show="tabValue === 'devices'" />
    </div>
    <mu-float-button icon="add" @click="toogleDialog" class="float-button"/>
    <NewDevice :dialog="dialog" :product="product" @close="toogleDialog"/>
  </div>
</template>

<script>
import NewProduct from './NewProduct'
import AddMod from '../components/AddMod'
import NewDevice from '../components/NewDevice'
import VueMarkdown from 'vue-markdown'
import DevicesList from '../components/DevicesList'
import Clipboard from 'clipboard'

export default {
  name: 'product',
  data () {
    return {
      viewSecret: false,
      toast: false,
      dialog: false,
      tabValue: 'readme',
      product: {
        _id: '',
        secret: '',
        name: 'N/A',
        commit: 'N/A',
        readme: '#N/A',
        mods: [],
        owner: ''
      }
    }
  },
  components: {
    NewProduct,
    NewDevice,
    AddMod,
    VueMarkdown,
    DevicesList
  },
  created () {
    if (this.$route.params.id) {
      this.getProduct()
    }
    this.clipboard = new Clipboard('.copy-btn')
  },
  computed: {
    user: function () {
      return this.$store.state.user
    },
    authorFullName: function () {
      if (this.product.owner) return this.product.owner.dev.name.last + this.product.owner.dev.name.first
      else return ''
    }
  },
  methods: {
    toogleDialog (item) {
      this.dialog = !this.dialog
    },
    handleTabChange (value) {
      this.tabValue = value
    },
    getProduct () {
      fetch(this.$root.apiurl + '/product/' + this.$route.params.id + '?token=' + this.user.token).then(res => res.json()).then(json => {
        if (json.msg !== undefined) this.$store.commit('error', '提交失败（ ' + json.msg + ' ）')
        else this.product = json
      }).catch(ex => {
        console.log('parsing failed', ex)
      })
    },
    viewSecretMethod () {
      this.viewSecret = true
    },
    showToast () {
      this.toast = true
      if (this.toastTimer) clearTimeout(this.toastTimer)
      this.toastTimer = setTimeout(() => { this.toast = false }, 2000)
    },
    hideToast () {
      this.toast = false
      if (this.toastTimer) clearTimeout(this.toastTimer)
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.product-header {
  margin-top: 0px;
  padding-top: 20px;
  background-color: #eee;
}
.hello-tabs {
  color: #222;
}
.hello-tabs .mu-tab-link{
  color: #222;
}
.content {
  margin: 0 30px;
}
.product-title {
  margin-left: 30px;
  font-size: 32px;
  color: #222;
}
.product-commit {
  margin-top: 10px;
  margin-left: 30px;
  margin-right: 3px;
  font-size: 22px;
  color: #444;
}
.product-mods {
  margin-left: 30px;
  margin-right: 3px;
  font-size: 16px;
  color: #444;
}
.product-id-secret {
  margin-top: 10px;
  margin-left: 30px;
  margin-right: 3px;
  font-size: 18px;
  color: #444;
}
.float-button {
  position: fixed;
  bottom: 30px;
  right: 30px;
  cursor: pointer;
  box-shadow: 0px 2px 5px #666;
}
</style>
