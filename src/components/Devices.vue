<template>
  <div>
    <mu-flexbox class="item-row" wrap="wrap" align="baseline" justify="flex-start">
      <mu-flexbox-item class="item-col" grow="0" basis="240px" v-for="(item, i) in devices" v-if="item.status !== 0">
        <mu-card>
          <mu-card-title :title="item.name" :subTitle="item.product.name" />
          <mu-card-text>
            <DeviceStatus :status="item.status"/>
          </mu-card-text>
          <mu-card-actions>
            <mu-flat-button label="查看" @click="$router.push('/device/' + item._id)" secondary/>
          </mu-card-actions>
        </mu-card>
      </mu-flexbox-item>
    </mu-flexbox>
    <div class="hello-guide" v-if="devices.length === 0">
      <h1 class="hello-icon">(=・ω・=)</h1>
      <h2>欢迎！你还没有设备，添加一个吧～</h2>
      <mu-raised-button label="添加新设备" @click="$parent.$parent.$parent.open" secondary/>
      <mu-raised-button label="查阅文档" @click="goToDoc" primary/>
    </div>
  </div>
</template>


<script>
import DeviceStatus from './DeviceStatus'

export default {
  data () {
    return {
      devices: []
    }
  },
  components: {
    DeviceStatus
  },
  computed: {
    user () {
      return this.$store.state.user
    }
  },
  created () {
    if (this.user.token) this.getDevices()
    else {
      this.stopTokenPatch = this.$store.watch(state => state.user, (val, old) => {
        this.getDevices()
        this.stopTokenPatch()
      })
    }
  },
  methods: {
    getDevices () {
      fetch(this.$root.apiurl + '/device' + '?token=' + this.user.token + '&owner=' + this.user.id).then(res => res.json()).then(json => {
        if (json.msg !== undefined) this.$store.commit('error', '查询失败（ ' + json.msg + ' ）')
        else this.devices = json.sort(this.sortUpdate)
      }).catch(ex => {
        console.log('parsing failed', ex)
      })
    },
    goToDoc () {
      location.href = 'http://doc.iot.noahgao.net'
    },
    sortUpdate (a, b) {
      if (a.updated_at < b.updated_at) {
        return 1
      }
      if (a.updated_at > b.updated_at) {
        return -1
      }
      // a 必须等于 b
      return 0
    }
  }
}
</script>

<style>
.hello-icon {
  font-size:50px;
  margin-bottom: 0.1%;
}
.hello-guide {
  margin-top: 15%;
  margin-bottom: 15%;
}
.item-row {
  margin-top: 2%;
}
.item-col {
  text-align: center;
  margin: 1%
}
.mu-flexbox .mu-flexbox-item:first-child {
  margin-left: 8px!important;
  margin-top: 0!important;
}
</style>
