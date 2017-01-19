<template>
  <div class="hello">
    <mu-row gutter>
      <mu-col width="100" tablet="60" desktop="75">
        <mu-tabs class="hello-tabs" @change="handleTabChange" :value="tabValue">
          <mu-tab value="devices" title="我的设备"/>
          <mu-tab value="products" title="产品原型广场"/>
          <mu-tab value="my-product" title="我的产品原型"/>
        </mu-tabs>
        <Devices v-show="tabValue === 'devices'"></Devices>
        <Products v-show="tabValue === 'products'"></Products>
        <Products v-show="tabValue === 'my-product'" v-if="role >= 2" :my="true"></Products>
      </mu-col>
      <mu-col width="100" tablet="40" desktop="25">
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
      tabValue: 'devices'
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
