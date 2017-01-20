<template>
  <div>
    <div class="product-header">
      <mu-row class="item-row" gutter>
        <mu-col width="100" tablet="35" desktop="35">
          <mu-paper class="info-block" :zDepth="3">
            <mu-list>
              <mu-sub-header>设备信息</mu-sub-header>
              <mu-list-item>
                <span slot="left"></span>
                <span slot="title" class="device-title">{{device.name}}</span>
              </mu-list-item>
              <mu-list-item>
                <span slot="left">状态</span>
                <DeviceStatus slot="title" :status="device.status" />
              </mu-list-item>
              <mu-list-item :title="device.product.name" @click="$router.push('/product/' + device.product._id)" :open="false" toggleNested>
                <span slot="left">产品</span>
                <mu-list-item slot="nested" :title="device.product.commit">
                  <span slot="left">简介</span>
                </mu-list-item>
                <mu-list-item slot="nested" :title="device.product.owner.email">
                  <span slot="left">开发</span>
                </mu-list-item>
                <mu-list-item slot="nested">
                  <span slot="left">MOD</span>
                  <AddMod slot="title" :mods="device.product.mods" :newF="false" />
                </mu-list-item>
              </mu-list-item>
              <mu-list-item :title="device.owner.email">
                <span slot="left">用户</span>
              </mu-list-item>
            </mu-list>
            <div v-if="user.role === 3 || user.id === device.owner._id || user.id === device.product.owner._id">
              <mu-chip class="item-row">Id: {{device._id}}</mu-chip>
              <mu-chip class="item-row" v-if="!viewSecret" @click="viewSecretMethod">Secret: 点击查看</mu-chip>
              <mu-chip class="item-row" v-if="viewSecret">{{device.secret}}</mu-chip>
            </div>
          </mu-paper>
        </mu-col>
        <mu-col width="100" tablet="65" desktop="65">
          <mu-flexbox wrap="wrap" align="baseline" justify="flex-end">
            <mu-flexbox-item class="data-block">
              <mu-paper class="data-block" :zDepth="3">
                <span class="data-title">温度</span>
                <span class="data-text">22</span>
              </mu-paper>
            </mu-flexbox-item>
            <mu-flexbox-item class="data-block">
              <mu-paper class="data-block" :zDepth="3">
                <span class="data-title">温度</span>
                <span class="data-text">22</span>
              </mu-paper>
            </mu-flexbox-item>
            <mu-flexbox-item class="data-block">
              <mu-paper class="data-block" :zDepth="3">
                <span class="data-title">温度</span>
                <span class="data-text">22</span>
              </mu-paper>
            </mu-flexbox-item>
          </mu-flexbox>
        </mu-col>
      </mu-row>
      <mu-tabs class="hello-tabs" @change="handleTabChange" :value="tabValue">
        <mu-tab value="logslist" title="数据记录"/>
        <mu-tab value="options" title="设备配置"/>
      </mu-tabs>
    </div>
    <div class="content">
      <div class="btn-content" v-show="tabValue === 'options'">
        <mu-raised-button class="device-options-btn" label="改名" backgroundColor="rgb(65, 105, 225)" @click="openDialog(1)"></mu-raised-button>
        <mu-raised-button class="device-options-btn" label="强制下线" backgroundColor="rgb(0, 100, 0)" @click="openDialog(2)"></mu-raised-button>
        <mu-raised-button class="device-options-btn" label="删除设备" backgroundColor="rgb(244, 67, 54)" @click="openDialog(3)"></mu-raised-button>
      </div>
    </div>
    <mu-dialog :open="dialogPage !== -1" title="设备配置">
      <div v-if="dialogPage === 1">
        <mu-text-field label="设备名" v-model="device.name" labelFloat/>
      </div>
      <div v-if="dialogPage === 2">强制下线只适用于数据库中设备状态与实际情况不符时用于校正，不能真的用于将设备从MQTT队列中移除！如有需要请从硬件层级操作！</div>
      <div v-if="dialogPage === 3">真的要删吗？～</div>
      <mu-flat-button label="取消" slot="actions" @click="closeDialog" primary/>
      <mu-flat-button label="确定" slot="actions" @click="finalOptions" primary/>
    </mu-dialog>
  </div>
</template>

<script>
import AddMod from '../components/AddMod'
import DeviceStatus from '../components/DeviceStatus'

export default {
  name: 'device',
  data () {
    return {
      viewSecret: false,
      dialogPage: -1,
      tabValue: 'logslist',
      device: {
        _id: '',
        name: '',
        secret: '',
        status: '',
        owner: {
          _id: '',
          email: '',
          role: 0
        },
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
    }
  },
  components: {
    AddMod,
    DeviceStatus
  },
  created () {
    if (this.$route.params.id) {
      this.getDevice()
    }
  },
  computed: {
    user: function () {
      return this.$store.state.user
    },
    allMods () {
      return this.$store.state.mods
    }
  },
  methods: {
    openDialog (val) {
      this.dialogPage = val
    },
    closeDialog () {
      this.dialogPage = -1
    },
    finalOptions () {
      let query = {}
      let type = this.dialogPage
      if (type === 1) query = {name: this.device.name}
      else if (type === 2) query = {status: 2}
      fetch(this.$root.apiurl + '/device/' + this.device._id + '?token=' + this.user.token, {
        method: type === 3 ? 'DELETE' : 'PUT',
        headers: {
          'Content-Type': 'application/json'
        },
        body: type === 3 ? '' : JSON.stringify(query)
      }).then(res => res.json()).then(json => {
        if (json.msg !== undefined) this.$store.commit('error', '提交失败（ ' + json.msg + ' ）')
        else {
          if (type === 3) this.$router.push('/dashboard')
          else if (type === 2) this.device.status = 2
        }
      }).catch(ex => {
        console.log('parsing failed', ex)
      })
      this.closeDialog()
    },
    handleTabChange (value) {
      this.tabValue = value
    },
    getDevice () {
      fetch(this.$root.apiurl + '/device/' + this.$route.params.id + '?token=' + this.user.token).then(res => res.json()).then(json => {
        if (json.msg !== undefined) this.$store.commit('error', '查询失败（ ' + json.msg + ' ）')
        else {
          fetch(this.$root.apiurl + '/user/' + json.meta.device.product.owner + '?token=' + this.user.token).then(res => res.json()).then(newjson => {
            if (newjson.msg !== undefined) this.$store.commit('error', '查询失败（ ' + newjson.msg + ' ）')
            else {
              json.meta.device.product.owner = newjson
              this.device = json.meta.device
              console.log(json)
            }
          }).catch(ex => {
            console.log('parsing failed', ex)
          })
        }
      }).catch(ex => {
        console.log('parsing failed', ex)
      })
    },
    viewSecretMethod () {
      this.viewSecret = true
      console.log(this.allMods)
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.product-header {
  margin-top: -10px;
  background-color: #eee;
}
.hello-tabs {
  color: #222;
}
.hello-tabs .mu-tab-link{
  color: #222;
}
.info-block {
  margin: 2%;
  margin-top: 8%;
  padding-bottom: 3%;
  padding-right: 0%;
  overflow: hidden;
}
.data-block {
  margin: 2%;
  padding-bottom: 1%;
  padding-right: 0%;
  width: 200px;
  height: 150px;
  text-align: center;
}
.data-title {
  display: inline-block;
  color: #7e848c;
  font-size: 16px;
  line-height: 48px;
  width: 100%;
  margin-top: 8%;
}
.data-text {
  display: inline-block;
  font-size: 40px;
  color: #222;
  margin-top: 2%;
}
.device-title {
  font-size: 24px;
  color: #222;
}
.item-row {
  margin: 1% 3%;
}
.btn-content {
  margin: 5% 10%;
  text-align: center
}
.device-options-btn {
  margin: 0 1%;
}
</style>
