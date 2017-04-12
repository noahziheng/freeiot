<template>
  <div class="content-row">
    <mu-card class="content-card">
      <mu-card-title title="推送规则列表" />
      <mu-card-text class="content-card-content">
        <mu-table :showCheckbox="false">
          <mu-thead slot="header">
            <mu-tr>
              <mu-th>规则名</mu-th>
              <mu-th>规则类型</mu-th>
              <mu-th>操作</mu-th>
            </mu-tr>
          </mu-thead>
          <mu-tbody>
            <mu-tr v-for="item,index in rules"  :key="index">
              <mu-td>{{item.name}}</mu-td>
              <mu-td>{{item.level | getLevel}}</mu-td>
              <mu-td>
                <mu-raised-button :label="'查看' + (roleCheck ? '/编辑' : '')" @click="viewRule(item, index)" />
                <mu-raised-button v-if="roleCheck" label="删除" @click="deleteRule(index)" />
              </mu-td>
            </mu-tr>
          </mu-tbody>
          <mu-raised-button class="rule-add-btn" slot="footer" label="添加新规则" @click="open" />
        </mu-table>
      </mu-card-text>
    </mu-card>
    <mu-dialog :open="dialog" :title="(rule.name === '' ? '添加' : '编辑') + '推送规则'" scrollable>
      <mu-text-field class="rule-field" label="规则名称" v-model="rule.name" labelFloat fullWidth /><br/>
      <mu-text-field class="rule-field" label="可触发规则标志符(以英文逗号,隔断，留空不能触发)" v-model="rule.label" labelFloat fullWidth /><br/>
      <mu-select-field class="rule-field" v-model="rule.level" label="消息级别">
        <mu-menu-item :value="0" title="系统消息"/>
        <mu-menu-item :value="1" title="一般消息"/>
        <mu-menu-item :value="2" title="特别消息"/>
        <mu-menu-item :value="3" title="警告消息"/>
      </mu-select-field><br/>
      <mu-text-field class="rule-field" label="触发条件表达式" v-model="rule.condition" labelFloat fullWidth /><br/>
      <mu-text-field class="rule-field" label="消息模板" v-model="rule.template" labelFloat fullWidth multiLine :rows="3" :rowsMax="8" /><br/>
      <mu-flat-button label="关闭" slot="actions" @click="close" primary/>
      <mu-flat-button label="确定" slot="actions" @click="submit" primary/>
    </mu-dialog>
  </div>
</template>


<script>
export default {
  data () {
    return {
      dialog: false,
      rule: {
        _id: '',
        name: '',
        product: '',
        label: '',
        level: 1,
        condition: '',
        template: ''
      },
      ruleIndex: -1,
      rules: []
    }
  },
  computed: {
    user () {
      return this.$store.state.user
    },
    roleCheck () {
      return this.user.role === 3 || this.user.id === this.product.owner._id
    }
  },
  created () {
    this.getRules()
  },
  watch: {
    product () {
      this.getRules()
    }
  },
  props: ['product'],
  filters: {
    getLevel (index) {
      let v = ['系统消息', '一般消息', '特别消息', '警告消息']
      return v[index]
    }
  },
  methods: {
    getRules () {
      if (this.product._id !== '') {
        fetch(this.$root.apiurl + '/notification/rule/' + this.product._id + '?token=' + this.user.token).then(res => res.json()).then(json => {
          if (json.msg !== undefined) this.$store.commit('error', '查询失败（ ' + json.msg + ' ）')
          else this.rules = json
        }).catch(ex => {
          console.log('parsing failed', ex)
        })
      }
    },
    open () {
      this.dialog = true
      if (!this.rule._id) this.rule.product = this.product._id
    },
    close () {
      this.emptyRuleTemp()
      this.dialog = false
    },
    emptyRuleTemp () {
      this.rule = {
        _id: '',
        name: '',
        product: '',
        label: [],
        level: 1,
        condition: '',
        template: ''
      }
      this.ruleIndex = -1
    },
    submit () {
      let obj = this.rule
      let index = this.ruleIndex
      console.log(obj.label)
      if (typeof obj.label === 'string') obj.label = obj.label.split(',')
      console.log(obj.label)
      if (obj.name !== '') {
        fetch(this.$root.apiurl + '/notification/rule' + (obj._id === '' ? '' : '/' + obj._id) + '?token=' + this.user.token, {
          method: obj._id !== '' ? 'PUT' : 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(obj)
        }).then(res => res.json()).then(json => {
          if (json.msg !== undefined) this.$store.commit('error', '提交失败（ ' + json.msg + ' ）')
          else {
            if (obj._id === '') {
              this.rules.push(obj)
            } else {
              this.rules.splice(index, 1, obj)
            }
          }
        }).catch(ex => {
          console.log('parsing failed', ex)
        })
      }
      this.close()
    },
    viewRule (item, index) {
      this.rule = item
      this.ruleIndex = index
      this.open()
    },
    deleteRule (index) {
      fetch(this.$root.apiurl + '/notification/rule/' + this.rules[index]._id + '?token=' + this.user.token, {
        method: 'DELETE'
      }).then(res => res.json()).then(json => {
        if (json.msg !== undefined) this.$store.commit('error', '删除失败（ ' + json.msg + ' ）')
        else this.rules.splice(index, 1)
      }).catch(ex => {
        console.log('parsing failed', ex)
      })
    }
  }
}
</script>

<style>
.rule-field {
  padding-left: 2%;
  width: 50%;
}
.rule-add-btn {
  margin-top: 2%;
}
</style>
