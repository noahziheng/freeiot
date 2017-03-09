<template>
  <div>
    <mu-text-field class="modval-general" v-if="data.type === 'text'" :label="data.remark" :value="d_value" @change="handleVarChange" />
    <template v-if="data.type === 'number'">
      <mu-sub-header class="inputnumber">{{data.remark}}</mu-sub-header>
      <mu-slider class="modval-general inputnumber" :max="data.vars ? data.vars.max : 999" :min="data.vars ? data.vars.min : -999" :step="data.vars ? data.vars.step : 1" :value="d_value" @change="handleVarChange" />
      <mu-text-field class="inputnumber inputnumber-box" :value="getVar" @change="handleVarChange"></mu-text-field>
    </template>
    <mu-select-field class="modval-general" :value="d_value" :label="data.remark" v-if="data.type === 'select'" @change="handleVarChange">
      <mu-menu-item :value="e" v-for="(e,i) in data.vars" :title="e"/>
    </mu-select-field>
  </div>
</template>

<script>
export default {
  data () {
    return {
      d_value: 'N/A'
    }
  },
  props: ['data', 'old'],
  computed: {
    getVar () {
      return this.d_value
    }
  },
  created () {
    this.d_value = undefined
    if (!this.old && this.data.default) this.d_value = this.data.default
    if (this.old) this.d_value = this.old
  },
  methods: {
    handleVarChange (newVal) {
      this.d_value = newVal
      this.$emit('change', this.data.label, newVal)
    }
  }
}
</script>

<style>
.modval-general {
  margin-left: 16px;
}
.inputnumber {
  margin-top: 2px;
  margin-bottom: 0px;
  margin-right: 3px;
}
.inputnumber-box {
  margin-top: 0px;
  margin-left: 3%;
  color:black;
  text-align: center;
}
</style>
