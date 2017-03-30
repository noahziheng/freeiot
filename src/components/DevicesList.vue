<template>
  <div>
    <mu-table :showCheckbox="false">
      <mu-thead>
        <mu-tr>
          <mu-th>ID</mu-th>
          <mu-th>状态</mu-th>
          <mu-th>所有者</mu-th>
          <mu-th>名字</mu-th>
        </mu-tr>
      </mu-thead>
      <mu-tbody>
        <mu-tr v-for="(item, index) in devices">
          <mu-td>{{ item._id }}</mu-td>
          <mu-td><DeviceStatus :status="item.status"/></mu-td>
          <mu-td>{{ item.owner.email }}</mu-td>
          <mu-td>{{ item.name }}</mu-td>
          <mu-td>
            <mu-flat-button labelClass="dev-btn" label="查看" @click="$router.push('/device/' + item._id)" primary/>
            <mu-flat-button labelClass="dev-btn" label="删除" @click="openDialog(index)" primary/>
          </mu-td>
        </mu-tr>
      </mu-tbody>
    </mu-table>
    <mu-dialog :open="dialog" title="删除确认">
      真的要删吗？～
      <mu-flat-button label="取消" slot="actions" @click="closeDialog" primary/>
      <mu-flat-button label="确定" slot="actions" @click="deleteItem" primary/>
    </mu-dialog>
    <mu-toast v-if="toast" :message="toastMsg" @close="hideToast"/>
  </div>
</template>


<script>
import DeviceStatus from './DeviceStatus'

export default {
  data () {
    return {
      dialog: false,
      deleteIndex: 0,
      toast: false,
      toastMsg: '',
      devices: []
    }
  },
  computed: {
    user () {
      return this.$store.state.user
    }
  },
  components: {
    DeviceStatus
  },
  created () {
    fetch(this.$root.apiurl + '/device' + '?token=' + this.user.token + '&product=' + this.$route.params.id).then(res => res.json()).then(json => {
      if (json.msg !== undefined) this.$store.commit('error', '查询失败（ ' + json.msg + ' ）')
      else this.devices = json.sort(this.sortStatus)
    }).catch(ex => {
      console.log('parsing failed', ex)
    })
  },
  methods: {
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
      this.deleteIndex = val
      this.dialog = true
    },
    closeDialog () {
      this.dialog = false
    },
    deleteItem () {
      console.log(this.devices[this.deleteIndex]._id)
      fetch(this.$root.apiurl + '/device/' + this.devices[this.deleteIndex]._id + '?token=' + this.user.token, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json'
        }
      }).then(res => res.json()).then(json => {
        if (json.msg !== undefined) this.$store.commit('error', '提交失败（ ' + json.msg + ' ）')
        else {
          this.devices.splice(this.deleteIndex, 1)
          this.closeDialog()
        }
      }).catch(ex => {
        console.log('parsing failed', ex)
      })
    },
    sortStatus (a, b) {
      if (a.status < b.status) {
        return 1
      }
      if (a.status > b.status) {
        return -1
      }
      // a 必须等于 b
      return 0
    }
  }
}
</script>

<style>
</style>
