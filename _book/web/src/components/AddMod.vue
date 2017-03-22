<template>
  <div>
    <mu-content-block>
      <mu-chip v-for="(item, index) in mods" @click="viewMod(item, index)" @delete="deleteMod(index)" :showDelete="newF">
        {{ item.remark !== '' ? item.remark : allMods[item.origin].name }}
      </mu-chip>
      <mu-raised-button v-if="newF" label="添加新模块" @click="open" />
    </mu-content-block>
    <mu-dialog :open="dialog" :title="(mod.origin === '' ? '添加' : '编辑') + '模块'" scrollable>
      <mu-row gutter>
        <mu-col width="100" tablet="40" desktop="40" class="mod-block">
          <mu-sub-header>选择模组</mu-sub-header>
          <mu-list :value="mod.origin" @change="handleModSelect">
            <mu-list-item v-for="(item, index) in allMods" :value="index" :title="item.name"></mu-list-item>
          </mu-list>
        </mu-col>
        <mu-col width="100" tablet="60" desktop="60">
          <mu-sub-header>配置数据点</mu-sub-header>
          <mu-text-field style="padding-left: 16px" label="模块备注名称（可为空）" v-model="mod.remark" labelFloat /><br/>
          <ModVal v-for="(e,j) in modinfo.global" :data="e" :old="mod.vars[e.label]" @change="handleModValChange"/>
          <template v-for="(item,i) in modinfo.vars" v-if="mod.origin !== ''">
            <mu-sub-header class="point-title">{{item.remark}}
              <br />
              <mu-checkbox label="隐藏数据点" v-if="!item.required" class="hidden-checkbox" @change="hidden(i)" :value="hiddened(i) !== -1"/>
            </mu-sub-header>
            <ModVal v-if="j !== 'remark' && hiddened(i) === -1" v-for="(e,j) in item.content" :data="e" :old="mod.vars[e.label]" @change="handleModValChange"/><br/>
          </template>
        </mu-col>
      </mu-row>
      <mu-flat-button label="关闭" slot="actions" @click="close" primary/>
      <mu-flat-button v-if="newF" label="确定" slot="actions" @click="submit" primary/>
    </mu-dialog>
  </div>
</template>


<script>
import Vue from 'vue'
import ModVal from './ModVal'

export default {
  data () {
    return {
      dialog: false,
      mod: {
        origin: '',
        remark: '',
        vars: {},
        hidden: []
      },
      modinfo: {
        name: '',
        vars: [],
        global: []
      }
    }
  },
  computed: {
    allMods () {
      return this.$store.state.mods
    }
  },
  components: {
    ModVal
  },
  props: ['mods', 'newF'],
  methods: {
    open () {
      this.dialog = true
    },
    close () {
      this.emptyModTemp()
      this.modF = undefined
      this.dialog = false
    },
    emptyModTemp () {
      this.mod = { origin: '', remark: '', vars: {}, hidden: [] }
      this.modinfo = { name: '', vars: [], global: [] }
    },
    handleModValChange (label, val) {
      this.mod.vars[label] = val
    },
    handleModSelect (val) {
      if (!this.modF) this.emptyModTemp()
      this.mod.origin = val
      this.modinfo.name = this.allMods[val].name
      Vue.set(this.modinfo, 'vars', this.allMods[val].vars)
      Vue.set(this.modinfo, 'global', this.allMods[val].global)
    },
    submit () {
      if (this.mod.origin !== '') {
        this.mods.push({
          origin: this.mod.origin,
          remark: this.mod.remark,
          vars: this.mod.vars,
          hidden: this.mod.hidden
        })
      }
      this.close()
    },
    viewMod (item, index) {
      this.mod = item
      this.modF = true
      this.handleModSelect(item.origin)
      if (this.newF) this.deleteMod(index)
      this.open()
    },
    deleteMod (index) {
      this.mods.splice(index, 1)
    },
    hidden (i) {
      let t = this.hiddened(i)
      if (t === -1) this.mod.hidden.push(i)
      else this.mod.hidden.splice(t, 1)
    },
    hiddened (i) {
      for (let j in this.mod.hidden) if (this.mod.hidden[j] === i) return j
      return -1
    }
  }
}
</script>

<style>
.mu-item.selected {
  color: #03a9f4;
}
.mod-block {
  border: 2px solid #d9d9d9;
  overflow: hidden;
}
.point-title {
  color: #222
}
.hidden-checkbox {
  margin: 0px;
  height: auto;
  line-height: 1em;
}
</style>