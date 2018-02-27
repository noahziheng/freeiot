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
              <mu-chip class="item-row copy-btn" v-if="viewSecret" :data-clipboard-text="device.secret" @click="showToast('Secret已复制')">{{device.secret}}</mu-chip>
              <mu-sub-header>温馨提示：点击Secret可复制</mu-sub-header>
            </div>
          </mu-paper>
        </mu-col>
        <mu-col width="100" tablet="65" desktop="65" style="margin-top: 1.5%">
          <mu-flexbox wrap="wrap" align="baseline" justify="flex-start">
            <mu-flexbox-item class="data-block" :grow="0" v-for="(item,index) in points">
              <mu-paper class="data-block" :zDepth="3">
                <span class="data-title">{{item.name}}({{index}})</span>
                <span :class="{ 'data-text': fontLengthMethod(item), 'data-text-s': !fontLengthMethod(item) }" v-if="item.type === 0">{{item.content}}</span>
                <span :class="{'data-text-s-unit': !fontLengthMethod(item) }" v-if="item.type === 0">{{item.format.unit}}</span>
                <template v-else>
                  <mu-text-field class="data-text" style="width:50%" v-if="item.controll.type === 'text'" v-model="item.content" @change="handleControll(item.label, $event)" fullWidth />
                  <template v-if="item.controll.type === 'number'">
                    <span :class="{ 'data-text': fontLengthMethod(item), 'data-text-s': !fontLengthMethod(item) }" @click="handleNumberChose(item)">{{item.content}}</span>
                  </template>
                  <template v-if="item.controll.type === 'switch'">
                    <span class="switch-text">{{ item.content ? 'ON' : 'OFF' }}</span><br>
                    <mu-switch v-model="item.content" @change="handleControll(item.label, $event)"/>
                  </template>
                  <span class="data-text" >{{item.format.unit}}</span>
                </template>
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
            <mu-menu-item :value="-1" title="最近一次数据"/>
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
              <mu-td>{{ getContent(item.content) }}</mu-td>
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
      <div v-if="dialogPage === 11">
        <mu-slider class="inputnumber" :max="tmp_max" :min="tmp_min" :step="tmp_step" v-model="tmp_val" />
        <mu-text-field class="inputnumber inputnumber-box" :value="tmp_val"></mu-text-field>
      </div>
      <mu-flat-button label="取消" slot="actions" @click="closeDialog" primary/>
      <mu-flat-button label="确定" slot="actions" @click="finalOptions" primary/>
    </mu-dialog>
    <mu-toast v-if="toast" :message="toastMsg" @close="hideToast"/>
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
      datalimit: -1,
      toast: false,
      toastMsg: '',
      tmp_label: '',
      tmp_val: 0,
      tmp_max: 100,
      tmp_min: 0,
      tmp_step: 1
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
  destroyed () {
    delete this.$options.sockets[this.$route.params.id + '-web']
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
      else if (val === 2) return '控制量反馈'
      else if (val === 3) return '系统消息'
      return '未知'
    },
    getDateTime (val) {
      return readable(val)
    }
  },
  methods: {
    fontLengthMethod (item) {
      if (String(item.content).length + String(item.format.unit).length > 6) return false
      else return true
    },
    getContent (val, statusF) {
      if (val === 'online') {
        if (statusF) this.device.status = 3
        return '设备上线'
      } else if (val === 'offline') {
        if (statusF) this.device.status = 2
        return '设备离线'
      } else if (val === 'empty') return '数据清空'
      else if (val === 'create') return '设备创建'
      else if (val === 'activate') return '设备激活'
      return val
    },
    handleNumberChose (item) {
      this.tmp_label = item.label
      this.tmp_val = item.content
      this.tmp_max = item.controll.vars.max
      this.tmp_min = item.controll.vars.min
      this.tmp_step = item.controll.vars.step
      this.openDialog(11)
    },
    showToast (val) {
      this.toast = true
      this.toastMsg = val
      if (this.toastTimer) clearTimeout(this.toastTimer)
      this.toastTimer = setTimeout(() => { this.toast = false }, 1200)
    },
    hideToast () {
      this.toast = false
      this.toastMsg = ''
      if (this.toastTimer) clearTimeout(this.toastTimer)
    },
    openDialog (val) {
      this.dialogPage = val
    },
    closeDialog () {
      this.dialogPage = -1
    },
    finalOptions () {
      if (this.dialogPage <= 10) {
        let query = {}
        let type = this.dialogPage
        if (type === 1) query = {name: this.device.name}
        let req = {
          method: type === 3 ? 'DELETE' : 'PUT',
          headers: {
            'Content-Type': 'application/json'
          }
        }
        if (type === 1 || type === 2) req.body = JSON.stringify(query)
        if (type === 3 && this.device.status === 3) this.$store.commit('error', '不能在设备在线时删除设备')
        else {
          fetch(this.$root.apiurl + '/device/' + this.device._id + (type === 0 ? '/empty' : (type === 2 ? '/makeoffline' : '')) + '?token=' + this.user.token, req).then(res => res.json()).then(json => {
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
      } else {
        this.handleControll(this.tmp_label, this.tmp_val)
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
      fetch(this.$root.apiurl + '/device/' + this.$route.params.id + (this.datalimit !== -1 ? '/' + this.datalimit : '') + '?token=' + this.user.token).then(res => res.json()).then(json => {
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
                  for (let j in mod.uplink) {
                    Vue.set(this.points, mod.uplink[j].label, {
                      type: 0,
                      name: mod.uplink[j].name,
                      show: mod.uplink[j].show,
                      format: mod.uplink[j].format,
                      content: 'N/A'
                    })
                  }
                  for (let j in mod.downlink) {
                    Vue.set(this.points, mod.downlink[j].label, {
                      type: 1,
                      name: mod.downlink[j].name,
                      label: mod.downlink[j].label,
                      controll: mod.downlink[j].controll,
                      format: mod.downlink[j].format,
                      content: (mod.downlink[j].controll.default !== undefined ? JSON.parse(mod.downlink[j].controll.default) : (mod.downlink[j].controll.type === 'switch' ? false : 'N/A'))
                    })
                  }
                  if (parseInt(i) === this.device.product.mods.length - 1) {
                    this.parsePoints()
                    this.initRealtime()
                  }
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
          if (!finish[this.datas[i].label]) {
            if (this.datas[i] !== undefined && this.points[this.datas[i].label] !== undefined) {
              if (this.datas[i].content !== undefined && this.datas[i].label !== 'SYS') {
                if (this.points[this.datas[i].label].format.type === 'boolean') this.datas[i].content = Boolean(this.datas[i].content)
                this.points[this.datas[i].label].content = this.datas[i].content
              }
            }
            finish[this.datas[i].label] = true
          }
        }
      }
    },
    initRealtime () {
      this.$options.sockets[this.$route.params.id + '-web'] = (data) => {
        this.datas.push(data)
        if (data.label !== 'SYS') {
          if (this.points[data.label].format.type === 'boolean') data.content = Boolean(data.content)
          this.points[data.label].content = data.content
        } else this.showToast(this.getContent(data.content, true))
        Vue.set(this, 'datas', this.datas.sort(this.sortCreate))
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
    },
    handleControll (label, val) {
      let query = {type: 1}
      query[label] = val
      fetch(this.$root.apiurl + '/device/' + this.$route.params.id + '/data' + '?token=' + this.user.token, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(query)
      }).then(res => res.json()).then(doc => {
        if (doc.msg !== undefined) this.$store.commit('error', '提交失败（ ' + doc.msg + ' ）')
        else console.log(doc)
      }).catch(ex => {
        console.log('parsing failed', ex)
      })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.product-header {
  margin-top: -15px;
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
  color: #222;
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
  margin-top: 2%;
}
.data-text-s {
  display: inline-block;
  font-size: 28px;
  margin-top: 2%;
}
.data-text-s-unit {
  display: inline-block;
  font-size: 20px;
  margin-top: 3%;
}
.switch-text {
  display: inline-block;
  font-size: 30px;
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
