<template>
  <div class="user-row">
    <mu-card class="user-card">
      <mu-card-title title="开发者申请"/>
      <mu-card-text class="user-card-content">
        <mu-table :showCheckbox="false">
          <mu-thead>
            <mu-tr>
              <mu-th>邮箱</mu-th>
              <mu-th>姓名</mu-th>
              <mu-th>操作</mu-th>
            </mu-tr>
          </mu-thead>
          <mu-tbody>
            <mu-tr v-for="(item, index) in devs">
              <mu-td>{{ item.email }}</mu-td>
              <mu-td>{{ item.dev.name.last }} {{ item.dev.name.first }}</mu-td>
              <mu-td>
                <mu-flat-button labelClass="dev-btn" label="处理" @click="open(item)" primary/>
              </mu-td>
            </mu-tr>
          </mu-tbody>
        </mu-table>
      </mu-card-text>
    </mu-card>
    <mu-card class="user-card">
      <mu-card-title title="用户管理"/>
      <mu-card-text class="user-card-content">
        <mu-table :showCheckbox="false">
          <mu-thead>
            <mu-tr>
              <mu-th>邮箱</mu-th>
              <mu-th>姓名</mu-th>
              <mu-th>权限</mu-th>
              <mu-th>操作</mu-th>
            </mu-tr>
          </mu-thead>
          <mu-tbody>
            <mu-tr v-for="(item, index) in users">
              <mu-td>{{ item.email }}</mu-td>
              <mu-td>{{ item.dev ? item.dev.name.last : '未知' }} {{ item.dev ? item.dev.name.first : '' }}</mu-td>
              <mu-td>
                <mu-select-field v-model="item.role" @change="setRole(item._id, $event)">
                  <mu-menu-item :value="1" title="开发者（未审）"/>
                  <mu-menu-item :value="2" title="开发者"/>
                  <mu-menu-item :value="3" title="管理员"/>
                </mu-select-field>
              </mu-td>
              <mu-td>
                <mu-flat-button labelClass="dev-btn" label="删除" @click="deleteUser(item._id)" primary/>
              </mu-td>
            </mu-tr>
          </mu-tbody>
        </mu-table>
      </mu-card-text>
    </mu-card>
    <mu-dialog :open="dialog" title="开发者身份申请">
      <mu-table :showCheckbox="false">
        <mu-tbody>
          <mu-tr>
            <mu-td>邮箱</mu-td>
            <mu-td>{{ info.email }}</mu-td>
          </mu-tr>
          <mu-tr>
            <mu-td>姓名</mu-td>
            <mu-td>{{ info.dev.name.last }} {{ info.dev.name.first }}</mu-td>
          </mu-tr>
          <mu-tr>
            <mu-td>职业</mu-td>
            <mu-td>{{ info.dev.work }}</mu-td>
          </mu-tr>
          <mu-tr>
            <mu-td>单位/学校</mu-td>
            <mu-td>{{ info.dev.company }}</mu-td>
          </mu-tr>
          <mu-tr>
            <mu-td>所在城市</mu-td>
            <mu-td>{{ info.dev.location }}</mu-td>
          </mu-tr>
        </mu-tbody>
      </mu-table>
      <mu-text-field class="user-card" label="拒绝理由" labelFloat v-model="info.dev.reason"/><br/>
      <mu-flat-button label="拒绝" slot="actions" @click="refuseDev" primary/>
      <mu-flat-button label="批准" slot="actions" @click="acceptDev" primary/>
    </mu-dialog>
  </div>
</template>

<script>
export default {
  name: 'useradmin',
  data () {
    return {
      dialog: false,
      users: [],
      devs: [],
      info: {
        _id: '',
        email: '',
        dev: {
          name: {
            last: '',
            first: ''
          },
          work: '',
          company: '',
          location: '',
          reason: ''
        }
      }
    }
  },
  computed: {
    user () {
      return this.$store.state.user
    }
  },
  created () {
    if (!this.$store.state.user || this.$store.state.user.role !== 3) {
      this.$router.push('/dashboard')
    }
    this.generateUsersData()
  },
  methods: {
    open (item) {
      this.info._id = item._id
      this.info.email = item.email
      this.info.dev = item.dev
      this.dialog = true
    },
    generateUsersData () {
      this.devs.splice(0, this.devs.length)
      fetch(this.$root.apiurl + '/user' + '?token=' + this.user.token).then(res => res.json()).then(json => {
        if (json.msg !== undefined) this.$store.commit('error', json.msg)
        else this.users = json
        for (let i in json) {
          if (json[i].role === 1) this.devs.push(json[i])
        }
      }).catch(ex => {
        console.log('parsing failed', ex)
      })
    },
    submit (id, data) {
      fetch(this.$root.apiurl + '/user/' + id + '?token=' + this.user.token, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      }).then(res => res.json()).then(json => {
        if (json.msg !== undefined) this.$store.commit('error', '提交失败（ ' + json.msg + ' ）')
        else {
          this.generateUsersData()
          this.dialog = false
        }
      }).catch(ex => {
        console.log('parsing failed', ex)
      })
    },
    acceptDev (event) {
      this.submit(this.info._id, {role: 2})
    },
    refuseDev (event) {
      this.submit(this.info._id, {role: 0, dev: this.info.dev})
    },
    deleteUser (id) {
      fetch(this.$root.apiurl + '/user/' + id + '?token=' + this.user.token, {
        method: 'DELETE'
      }).then(res => res.json()).then(json => {
        if (json.msg !== undefined) this.$store.commit('error', '提交失败（ ' + json.msg + ' ）')
        else {
          this.generateUsersData()
        }
      }).catch(ex => {
        console.log('parsing failed', ex)
      })
    },
    setRole (id, val) {
      if (id === this.user.id) this.$store.commit('error', '你疯了吗')
      else this.submit(id, {role: val})
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
  margin: 2.5%;
}
.user-card-content {
  padding-top: 5px;
  padding-bottom: 5px;
}
.dev-btn {
  width: 50%;
}
</style>
