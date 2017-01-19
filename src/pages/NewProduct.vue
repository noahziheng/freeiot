<template>
  <div class="content-row">
    <mu-card class="content-card">
      <mu-card-title :title="($route.params.id ? '编辑' : '新建') + '产品原型'"/>
      <mu-card-text class="content-card-content"> 
        <mu-text-field label="产品名称" v-model="product.name" labelFloat fullWidth /><br/>
        <mu-text-field label="产品描述" v-model="product.commit" labelFloat fullWidth /><br/>
        <mu-text-field label="产品使用说明(Markdown)" v-model="product.readme" labelFloat multiLine :rows="6" :rowsMax="20" fullWidth /><br/>
        <AddMod :mods="product.mods" :newF="true" />
      </mu-card-text>
      <mu-card-actions>
        <mu-flat-button label="提交" @click="submit" primary/>
        <mu-flat-button label="返回广场" @click="$router.push('/dashboard')"/>
      </mu-card-actions>
    </mu-card>
    <mu-dialog :open="dialog" title="已完成">
      <mu-flat-button label="确定" slot="actions" @click="close" primary/>
    </mu-dialog>
  </div>
</template>

<script>
import Vue from 'vue'
import AddMod from '../components/AddMod'

export default {
  name: 'newproduct',
  data () {
    return {
      dialog: false,
      product: {
        name: '',
        commit: '',
        readme: '',
        mods: [],
        owner: ''
      }
    }
  },
  props: ['changeF'],
  components: {
    AddMod
  },
  computed: {
    user () {
      return this.$store.state.user
    },
    mods () {
      return this.$store.state.mods
    }
  },
  created () {
    if (!this.$store.state.user || this.$store.state.user.role < 2) {
      this.$router.push('/dashboard')
    }
    if (this.$route.params.id) {
      fetch(this.$root.apiurl + '/product/' + this.$route.params.id + '?token=' + this.user.token).then(res => res.json()).then(json => {
        if (json.msg !== undefined) this.$store.commit('error', '提交失败（ ' + json.msg + ' ）')
        else {
          this.product = json
          Vue.set(this.product, 'mods', json.mods)
          this.product.owner = json.owner._id
        }
      }).catch(ex => {
        console.log('parsing failed', ex)
      })
    }
    this.product.owner = this.user.id
  },
  methods: {
    open (item) {
      this.dialog = true
    },
    close (item) {
      this.dialog = false
      if (this.$route.params.id) location.href = '/product/' + this.$route.params.id
      else this.$router.push('/dashboard')
    },
    submit () {
      fetch(this.$root.apiurl + '/product' + (this.$route.params.id ? '/' + this.$route.params.id : '') + '?token=' + this.user.token, {
        method: this.$route.params.id ? 'PUT' : 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(this.product)
      }).then(res => res.json()).then(json => {
        if (json.msg !== undefined) this.$store.commit('error', '提交失败（ ' + json.msg + ' ）')
        else this.open()
      }).catch(ex => {
        console.log('parsing failed', ex)
      })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.content-row {
  margin-top: 1.5%;
}
@media screen and (min-width:800px) {
  .content-card {
    margin: 2.5% 25%;
  }
}
@media screen and (max-width:800px) {
  .content-card {
    margin: 2.5%;
  }
}
.content-card-content {
  padding-top: 5px;
  padding-bottom: 5px;
}
</style>
