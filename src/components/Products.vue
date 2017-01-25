<template>
  <div>
    <mu-flexbox class="item-row" wrap="wrap" align="baseline" justify="flex-start">
      <mu-flexbox-item class="item-col" grow="0" basis="240px" v-for="(item, i) in products">
        <mu-card>
          <mu-card-title :title="item.name" :subTitle="item.commit"/>
          <mu-card-actions>
            <mu-flat-button label="查看" @click="$router.push('/product/' + item._id)" secondary/>
          </mu-card-actions>
        </mu-card>
      </mu-flexbox-item>
    </mu-flexbox>
    <div class="hello-guide" v-if="my && products.length === 0">
      <h1 class="hello-icon">(ﾟд⊙)</h1>
      <h2>你还没有自己的产品原型呢，添加一波吧～</h2>
      <mu-raised-button label="添加新的产品原型" @click="$router.push('/newproduct')" secondary/>
      <mu-raised-button label="查阅文档" @click="goToDoc" primary/>
    </div>
  </div>
</template>


<script>
export default {
  data () {
    return {
      products: []
    }
  },
  props: [ 'my' ],
  computed: {
    user () {
      return this.$store.state.user
    }
  },
  mounted () {
    if (this.user.token) this.getProducts()
    else {
      this.stopTokenPatch = this.$store.watch(state => state.user, (val, old) => {
        this.getProducts()
        this.stopTokenPatch()
      })
    }
  },
  methods: {
    getProducts () {
      fetch(this.$root.apiurl + '/product' + '?token=' + this.user.token + (this.my ? '&owner=' + this.user.id : '')).then(res => res.json()).then(json => {
        if (json.msg !== undefined) this.$store.commit('error', '查询失败（ ' + json.msg + ' ）')
        else this.products = json.sort(this.sortUpdate)
      }).catch(ex => {
        console.log('parsing failed', ex)
      })
    },
    goToDoc () {
      location.href = '//doc.iot.noahgao.net'
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
</style>