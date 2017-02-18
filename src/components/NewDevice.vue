<template>
  <mu-dialog :open="dialog" title="新设备入网向导" scrollable>
    <template v-if="page === 0">
      <span class="guide-text">请在下方的设备入网情况中选择一项继续</span>
      <mu-list>
        <mu-list-item title="我想要激活开发者已经预置好的设备" @click="handleChosePage(1)"/>
        <mu-list-item title="我是开发者，我要预置一批设备" @click="handleChosePage(2)"  v-if="user.role >= 2 && user.id === product.owner._id"/>
        <mu-list-item title="我要把设备手动接入网络" @click="handleChosePage(3)"/>
      </mu-list>
    </template>
    <template v-if="page === 1">
      <span class="guide-text">请输入设备二维码下或开发者提供的9位ID</span><br>
      <mu-text-field label="ID" v-model="device._id"/>
      <mu-flat-button label="查询" @click="getDevice" primary/>
      <mu-table v-if="device.product._id" :showCheckbox="false">
        <mu-thead>
          <mu-tr>
            <mu-th>所属产品</mu-th>
            <mu-th>开发者</mu-th>
            <mu-th>开发者邮箱</mu-th>
            <mu-th></mu-th>
          </mu-tr>
        </mu-thead>
        <mu-tbody>
          <mu-tr>
            <mu-td>{{device.product.name}}</mu-td>
            <mu-td>{{device.owner.dev.name.last}}{{device.owner.dev.name.first}}</mu-td>
            <mu-td>{{device.owner.email}}</mu-td>
            <mu-td><mu-flat-button label="添加" @click="activeDevice" primary/></mu-td>
          </mu-tr>
        </mu-tbody>
      </mu-table>
    </template>
    <template v-if="page === 2 && product">
      <span class="guide-text">请选择预置数量，当前{{ countNum }}</span><br><br>
      <mu-slider v-model="countNum" :step="1" :min="1" :max="10"/>
      <span class="guide-text">当前预置产品：{{product.name}}</span>
      <br><br>
      <mu-flat-button label="开始生成设备" @click="defineDevice" primary/>
      <mu-flat-button label="完成" @click="handleChosePage(4)"/>
      <mu-table v-if="devices.length > 0" :showCheckbox="false">
        <mu-thead>
          <mu-tr>
            <mu-th>ID</mu-th>
            <mu-th>Secret</mu-th>
          </mu-tr>
        </mu-thead>
        <mu-tbody>
          <mu-tr v-for="(e,i) in devices">
            <mu-td>{{e.id}}</mu-td>
            <mu-td>{{e.secret}}</mu-td>
          </mu-tr>
        </mu-tbody>
      </mu-table>
    </template>
    <template v-if="page === 2 && product === undefined">
      <span class="guide-text">错误：当前产品原型未定义</span><br><br>
    </template>
    <template v-if="page === 3">
      <span class="guide-text">Web版该功能暂未上线,请点击下方链接下载AndroidAPP</span><br><br>
      <a href="https://fir.im/3b9a">Fir.im通道</a>
    </template>
    <template v-if="page === 4">
      <span class="guide-text">设备入网流程已全部完成</span><br><br>
      <mu-flat-button label="查看新设备" @click="$router.push('/device/' + final)" primary/>
      <mu-flat-button label="继续添加新设备" @click="handleChosePage(0)"/>
    </template>
    <mu-flat-button label="上一步" v-show="page !== 0" slot="actions" @click="handleChosePage(0)" primary/>
    <mu-flat-button label="关闭" slot="actions" @click="close" primary/>
  </mu-dialog>
</template>

<script>
export default {
  data () {
    return {
      page: 0, // 0-选择情况 1-预置设备 2-激活设备 3-手动入网 4-完成
      device: {
        _id: '',
        product: '',
        owner: '',
        secret: '',
        status: 0
      },
      countNum: 1,
      devices: [],
      final: ''
    }
  },
  props: {
    dialog: {
      type: Boolean,
      default: true
    },
    product: {
      type: Object
    }
  },
  computed: {
    user: function () {
      return this.$store.state.user
    }
  },
  methods: {
    close () {
      this.$emit('close')
    },
    handleChosePage (val) {
      this.page = val
    },
    getDevice () {
      fetch(this.$root.apiurl + '/device/' + this.device._id + '?token=' + this.user.token).then(res => res.json()).then(json => {
        if (json.msg !== undefined) this.$store.commit('error', '查询失败（ ' + json.msg + ' ）')
        else this.device = json.meta.device
        console.log(this.device)
      }).catch(ex => {
        console.log('parsing failed', ex)
      })
    },
    activeDevice () {
      if (this.device.status !== 0 && this.device.owner._id === this.user.id) this.$store.commit('error', '该设备已被激活')
      else {
        fetch(this.$root.apiurl + '/device/' + this.device._id + '/activite/' + this.user.id + '?token=' + this.user.token).then(res => res.json()).then(json => {
          if (json.msg !== undefined) this.$store.commit('error', '激活失败（ ' + json.msg + ' ）')
          else this.handleChosePage(4)
        }).catch(ex => {
          console.log('parsing failed', ex)
        })
        this.final = this.device._id
        this.device = {
          _id: '',
          product: '',
          owner: '',
          secret: '',
          status: 0
        }
      }
    },
    defineDevice () {
      let query = {}
      query.name = this.product.name
      query.product = this.product._id
      query.owner = this.user.id
      query.status = 0
      for (let i = 0; i < this.countNum; i++) {
        fetch(this.$root.apiurl + '/device?token=' + this.user.token, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(query)
        }).then(res => res.json()).then(json => {
          if (json.msg !== undefined) this.$store.commit('error', '提交失败（ ' + json.msg + ' ）')
          else {
            this.final = json._id
            this.devices.push({id: json._id, secret: json.secret})
          }
        }).catch(ex => {
          console.log('parsing failed', ex)
        })
      }
    }
  }
}
</script>

<style>
  .guide-text {
    color: #555;
    font-size: 18px;
  }
</style>
