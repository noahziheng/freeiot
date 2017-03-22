<template>
  <div class="content-row">
    <mu-card class="content-card">
      <mu-card-title :title="($route.params.id ? '编辑' : '新建') + '产品原型'"/>
      <mu-card-text class="content-card-content"> 
        <mu-text-field label="产品名称" v-model="product.name" labelFloat fullWidth /><br/>
        <mu-text-field label="产品描述" v-model="product.commit" labelFloat fullWidth /><br/>
        <mu-text-field label="产品使用说明(Markdown)" v-model="product.readme" labelFloat multiLine :rows="6" :rowsMax="20" fullWidth /><br/>
        <mu-sub-header>模块列表</mu-sub-header>
        <AddMod :mods="product.mods" :newF="true" />
        <mu-raised-button class="delete-btn" label="删除产品原型及其设备(慎点)" backgroundColor="rgb(244, 67, 54)" v-if="$route.params.id" @click="delete_open">
          <mu-icon value="delete forever" color="white" style="width:24px"/>
        </mu-raised-button>
      </mu-card-text>
      <mu-card-actions>
        <mu-flat-button label="提交" @click="submit" primary/>
        <mu-flat-button label="返回广场" @click="$router.push('/dashboard')"/>
      </mu-card-actions>
    </mu-card>
    <mu-dialog :open="dialog" title="已完成">
      <mu-flat-button label="确定" slot="actions" @click="close" primary/>
    </mu-dialog>
    <mu-dialog :open="delete_dialog" v-if="$route.params.id" title="真的要删除吗？">
      请输入该产品名称进行确认！<br>
      <mu-text-field :hintText="product.name" v-model="delete_value" :errorText="delete_error" @input="checkDelete"/>
      <mu-flat-button label="取消" slot="actions" @click="delete_close" primary/>
      <mu-flat-button label="确定" slot="actions" @click="deleteProduct" primary/>
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
      delete_dialog: false,
      delete_value: '',
      delete_error: '',
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
    open () {
      this.dialog = true
    },
    close () {
      this.dialog = false
      if (this.$route.params.id) location.href = '/product/' + this.$route.params.id
      else this.$router.push('/dashboard')
    },
    delete_open () {
      this.delete_dialog = true
    },
    delete_close () {
      this.delete_dialog = false
    },
    deleteProduct () {
      if (this.checkDelete()) {
        fetch(this.$root.apiurl + '/product/' + this.$route.params.id + '?token=' + this.user.token, {
          method: 'DELETE'
        }).then(res => res.json()).then(json => {
          if (json.msg !== undefined) this.$store.commit('error', '产品删除失败（ ' + json.msg + ' ）')
          else location.href = '/dashboard'
        }).catch(ex => {
          console.log('parsing failed', ex)
        })
      }
    },
    checkDelete () {
      if (this.delele_value !== '' && this.delete_value === this.product.name) {
        this.delete_error = ''
        return true
      } else {
        this.delete_error = '输入错误！'
        return false
      }
    },
    submit () {
      console.log(this.product)
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
.delete-btn {
  margin: 12px;
}
</style>
