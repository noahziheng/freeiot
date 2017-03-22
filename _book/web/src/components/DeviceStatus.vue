<template>
  <div>
    <mu-badge v-if="status === 0" content="预置" class="status-0"/>
    <mu-badge v-if="status === 1" content="等待入网" class="status-1"/>
    <mu-badge v-if="status === 2" content="离线" class="status-2"/>
    <mu-badge v-if="status === 3" content="在线" class="status-3"/>
  </div>
</template>


<script>
export default {
  data () {
    return {
      devices: []
    }
  },
  computed: {
    user () {
      return this.$store.state.user
    }
  },
  props: ['status'],
  created () {
  }
}
</script>

<style>
.status-0 .mu-badge {
  margin-top: 5%;
  font-size: 15px;
  background-color: #555;
}
.status-1 .mu-badge {
  margin-top: 5%;
  font-size: 15px;
  background-color: #fc2c7b;
}
.status-2 .mu-badge {
  margin-top: 5%;
  font-size: 15px;
  background-color: #ff4500;
}
.status-3 .mu-badge {
  margin-top: 5%;
  font-size: 15px;
  background-color: #006400;
}
</style>