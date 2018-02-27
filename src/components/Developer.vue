<template>
  <div>
    <mu-card class="hello-card" v-if="role === 0 && !info.reason">
      <mu-card-title title="你还不是开发者哦！" subTitle="由于服务器资源有限，你需要申请成为开发者才能建立自己的产品～"/>
      <mu-card-actions>
        <mu-flat-button label="立即申请" @click="open"/>
        <mu-flat-button label="别再烦我" @click="dismiss"/>
      </mu-card-actions>
    </mu-card>
    <mu-card class="hello-card" v-if="role === 1">
      <mu-card-title title="您的开发者申请正在审核" subTitle="请耐心等待～"/>
      <mu-card-actions>
        <mu-flat-button label="查看提交" @click="open"/>
      </mu-card-actions>
    </mu-card>
    <mu-card class="hello-card" v-if="role === 0 && info.reason">
      <mu-card-title title="您的开发者申请被退回" subTitle="请检查后请重新提交"/>
      <mu-card-actions>
        <mu-flat-button label="查看" @click="open"/>
      </mu-card-actions>
    </mu-card>
    <mu-dialog :open="dialog" title="开发者身份申请" v-if="role < 2">
      <div v-if="role == 0">
        <div v-show="info.reason">开发者申请被退回，原因是： {{ info.reason }}</div>
        <mu-text-field label="姓" v-model="info.name.last"/>
        <mu-text-field label="名" v-model="info.name.first"/><br/>
        <mu-text-field label="职业" v-model="info.work"/><br/>
        <mu-text-field label="公司/学校" v-model="info.company"/><br/>
        <mu-text-field label="所在城市" v-model="info.location"/>
      </div>
      <div v-else>
        开发者申请已提交
        <mu-table :showCheckbox="false">
          <mu-tbody>
            <mu-tr>
              <mu-td>姓名</mu-td>
              <mu-td>{{ info.name.last }} {{ info.name.first }}</mu-td>
            </mu-tr>
            <mu-tr>
              <mu-td>职业</mu-td>
              <mu-td>{{ info.work }}</mu-td>
            </mu-tr>
            <mu-tr>
              <mu-td>单位/学校</mu-td>
              <mu-td>{{ info.company }}</mu-td>
            </mu-tr>
            <mu-tr>
              <mu-td>所在城市</mu-td>
              <mu-td>{{ info.location }}</mu-td>
            </mu-tr>
          </mu-tbody>
        </mu-table>
      </div>
      <mu-flat-button label="取消" v-show="role === 0" slot="actions" @click="close" primary/>
      <mu-flat-button label="确定" v-show="role === 0" slot="actions" @click="submit" primary/>
      <mu-flat-button label="关闭" v-show="role === 1" slot="actions" @click="close" primary/>
    </mu-dialog>
  </div>
</template>


<script>
export default {
  data () {
    return {
      dialog: false,
      info: {
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
  },
  props: ['role'],
  computed: {
    user () {
      return this.$store.state.user
    }
  },
  created () {
    fetch(this.$root.apiurl + '/user/' + this.user.id + '?token=' + this.user.token).then(res => res.json()).then(json => {
      if (json.msg !== undefined) this.$store.commit('error', json.msg)
      else {
        if (json.dev !== undefined) this.info = json.dev
      }
    }).catch(ex => {
      console.log('parsing failed', ex)
    })
  },
  methods: {
    open () {
      this.dialog = true
    },
    close () {
      this.dialog = false
    },
    submit () {
      this.info.reason = ''
      fetch(this.$root.apiurl + '/user/' + this.user.id + '?token=' + this.user.token, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({role: 1, dev: this.info})
      }).then(res => res.json()).then(json => {
        if (json.msg !== undefined) this.$store.commit('error', '提交失败（ ' + json.msg + ' ）')
        else {
          json.type = 1
          this.$store.commit('reqdev', json)
        }
      }).catch(ex => {
        console.log('parsing failed', ex)
      })
    },
    dismiss () {
      this.close()
      this.$store.commit('nodev', true)
      window.localStorage.nodev = JSON.stringify(this.$store.state.nodev)
    }
  }
}
</script>

<style>
.hello-card {
  margin-top: 2%;
  margin-bottom: 2%;
}
</style>