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
            <div ref="copyElement" v-if="user.role === 3 || user.id === device.owner._id || user.id === device.product.owner._id">
              <mu-chip class="item-row">Id: {{device._id}}</mu-chip>
              <mu-chip class="item-row" v-if="!viewSecret" @click="viewSecretMethod">Secret: 点击查看</mu-chip>
              <mu-chip class="item-row copy-btn" v-if="viewSecret" :data-clipboard-text="device.secret" @click="showToast">{{device.secret}}</mu-chip>
              <mu-toast v-if="toast" message="Secret已复制" @close="hideToast"/>
              <mu-sub-header>温馨提示：点击Secret可复制</mu-sub-header>
            </div>
          </mu-paper>
        </mu-col>
        <mu-col width="100" tablet="65" desktop="65" style="margin-top: 1.5%">
          <mu-flexbox wrap="wrap" align="baseline" justify="flex-start">
            <mu-flexbox-item class="data-block" :grow="0" v-for="(item,index) in points">
              <mu-paper class="data-block" :zDepth="3">
                <span class="data-title">{{item.name}}({{index}})</span>
                <span class="data-text">{{item.content}}{{item.unit}}</span>
              </mu-paper>
            </mu-flexbox-item>
          </mu-flexbox>
        </mu-col>
      </mu-row>
      <mu-tabs class="hello-tabs" @change="handleTabChange" :value="tabValue">
        <mu-tab value="logslist" title="数据记录"/>
        <mu-tab value="options" title="设备配置" v-if="user.role === 3 || user.id === device.owner._id || user.id === device.product.owner._id"/>
      </mu-tabs>
    </div>
    <div class="content">
      <div v-show="tabValue === 'logslist'">
        <div class="datalimit">
          <mu-sub-header>
            按数据提交时间查询：
          </mu-sub-header>
          <mu-select-field v-model="datalimit" @change="handleDatalimit">
            <mu-menu-item :value="1" title="1小时内"/>
            <mu-menu-item :value="2" title="2小时内"/>
            <mu-menu-item :value="3" title="3小时内"/>
            <mu-menu-item :value="6" title="6小时内"/>
            <mu-menu-item :value="12" title="12小时内"/>
            <mu-menu-item :value="24" title="24小时内"/>
            <mu-menu-item :value="48" title="2天内"/>
            <mu-menu-item :value="72" title="3天内"/>
          </mu-select-field>
        </div>
        <mu-table :showCheckbox="false">
          <mu-thead>
            <mu-tr>
              <mu-th>#</mu-th>
              <mu-th>类型</mu-th>
              <mu-th>数据点</mu-th>
              <mu-th>值</mu-th>
              <mu-th>时间</mu-th>
            </mu-tr>
          </mu-thead>
          <mu-tbody>
            <mu-tr v-for="(item, index) in datas">
              <mu-td>{{ index }}</mu-td>
              <mu-td>{{ item.type | getTypeName }}</mu-td>
              <mu-td>{{ item.label }}</mu-td>
              <mu-td>{{ item.content }}</mu-td>
              <mu-td>{{ item.created_at | getDateTime }}</mu-td>
            </mu-tr>
          </mu-tbody>
        </mu-table>
      </div>
      <div class="btn-content" v-show="tabValue === 'options'" v-if="user.role === 3 || user.id === device.owner._id || user.id === device.product.owner._id">
        <mu-raised-button class="device-options-btn" label="清空数据" color="#000" backgroundColor="rgb(225，225, 225)" @click="openDialog(0)"></mu-raised-button>
        <mu-raised-button class="device-options-btn" label="改名" backgroundColor="rgb(65, 105, 225)" @click="openDialog(1)"></mu-raised-button>
        <mu-raised-button class="device-options-btn" label="强制下线" backgroundColor="rgb(0, 100, 0)" @click="openDialog(2)"></mu-raised-button>
        <mu-raised-button class="device-options-btn" label="删除设备" backgroundColor="rgb(244, 67, 54)" @click="openDialog(3)"></mu-raised-button>
      </div>
    </div>
    <mu-dialog :open="dialogPage !== -1" title="设备配置" v-if="user.role === 3 || user.id === device.owner._id || user.id === device.product.owner._id">
      <div v-if="dialogPage === 0">真的要清掉吗？～此操作不可逆！</div>
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
import Vue from 'vue'
import readable from 'readable-shijian'
import Clipboard from 'clipboard'

export default {
  name: 'device',
  data () {
    return {
      viewSecret: false,
      dialogPage: -1,
      tabValue: 'logslist',
      points: {},
      datas: [],
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
      },
      datalimit: 1,
      toast: false
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
    this.clipboard = new Clipboard('.copy-btn')
  },
  computed: {
    user: function () {
      return this.$store.state.user
    }
  },
  filters: {
    getTypeName (val) {
      if (val === 0) return '上传'
      else if (val === 1) return '下控'
      return '未知'
    },
    getDateTime (val) {
      return readable(val)
    }
  },
  methods: {
    showToast () {
      this.toast = true
      if (this.toastTimer) clearTimeout(this.toastTimer)
      this.toastTimer = setTimeout(() => { this.toast = false }, 2000)
    },
    hideToast () {
      this.toast = false
      if (this.toastTimer) clearTimeout(this.toastTimer)
    },
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
      let req = {
        method: type === 3 ? 'DELETE' : 'PUT',
        headers: {
          'Content-Type': 'application/json'
        }
      }
      if (type === 1 || type === 2) req.body = JSON.stringify(query)
      if (type === 3 && this.device.status === 3) this.$store.commit('error', '不能在设备在线时删除设备')
      else {
        fetch(this.$root.apiurl + '/device/' + this.device._id + (type === 0 ? '/empty' : '') + '?token=' + this.user.token, req).then(res => res.json()).then(json => {
          if (json.msg !== undefined) this.$store.commit('error', '提交失败（ ' + json.msg + ' ）')
          else {
            if (type === 3) this.$router.push('/dashboard')
            else if (type === 2) this.device.status = 2
            else if (type === 0) location.reload()
          }
        }).catch(ex => {
          console.log('parsing failed', ex)
        })
      }
      this.closeDialog()
    },
    handleDatalimit (val) {
      this.datalimit = val
      this.getDevice()
    },
    handleTabChange (value) {
      this.tabValue = value
    },
    getDevice () {
      fetch(this.$root.apiurl + '/device/' + this.$route.params.id + '/' + this.datalimit + '?token=' + this.user.token).then(res => res.json()).then(json => {
        if (json.msg !== undefined) this.$store.commit('error', '查询失败（ ' + json.msg + ' ）')
        else {
          fetch(this.$root.apiurl + '/user/' + json.meta.device.product.owner + '?token=' + this.user.token).then(res => res.json()).then(newjson => {
            if (newjson.msg !== undefined) this.$store.commit('error', '查询失败（ ' + newjson.msg + ' ）')
            else {
              json.meta.device.product.owner = newjson
              this.device = json.meta.device
              Vue.set(this, 'datas', json.data.sort(this.sortCreate))
              for (let i in this.device.product.mods) {
                let query = this.device.product.mods[i].vars
                query.hidden = this.device.product.mods[i].hidden
                fetch(this.$root.apiurl + '/mod/' + this.device.product.mods[i].origin + '?token=' + this.user.token, {
                  method: 'POST',
                  headers: {
                    'Content-Type': 'application/json'
                  },
                  body: JSON.stringify(query)
                }).then(res => res.json()).then(mod => {
                  for (let i in mod.uplink) Vue.set(this.points, mod.uplink[i].label, {name: mod.uplink[i].name, unit: mod.uplink[i].format.unit, content: 'N/A'})
                  for (let i in mod.downlink) Vue.set(this.points, mod.uplink[i].label, {name: mod.uplink[i].name, unit: mod.uplink[i].format.unit, content: 'N/A'})
                  if (parseInt(i) === this.device.product.mods.length - 1) this.parsePoints()
                }).catch(ex => {
                  console.log('parsing failed', ex)
                })
              }
            }
          }).catch(ex => {
            console.log('parsing failed', ex)
          })
        }
      }).catch(ex => {
        console.log('parsing failed', ex)
      })
    },
    parsePoints () {
      if (this.device.status === 3) {
        let finish = {}
        for (let i in this.datas) {
          console.log(finish)
          if (!finish[this.datas[i].label]) {
            this.points[this.datas[i].label].content = this.datas[i].content
            finish[this.datas[i].label] = true
          }
        }
      }
    },
    viewSecretMethod () {
      this.viewSecret = true
    },
    sortCreate (a, b) {
      if (a.created_at < b.created_at) {
        return 1
      }
      if (a.created_at > b.created_at) {
        return -1
      }
      // a 必须等于 b
      return 0
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
.datalimit {
  text-align: center;
}
</style>
