<template>
  <div>
    <mu-appbar title="FreeIOT控制台">
      <mu-icon-button icon='menu' slot="left" @click="drawer_toggle()"/>
      <mu-icon-button slot="right" href="https://github.com/noahziheng/freeiot" icon=":mudocs-icon-custom-github"/>
    </mu-appbar>
    <mu-drawer :open="drawer_open" :docked="false" @close="drawer_toggle()">
      <mu-list>
        <mu-list-item title="Menu Item 1"/>
        <mu-list-item title="Menu Item 2"/>
        <mu-list-item title="Menu Item 3"/>
      </mu-list>
    </mu-drawer>
    <div class="mu-banner">
      <div class="mu-banner-container">
        <div class="mu-logo">
          FI
        </div>
        <h1 class="mu-banner-title">
          FreeIOT
        </h1>
        <h3 class="mu-banner-sub-title">
          为开发者准备的开源物联网套件
        </h3>
        <div class="mu-banner-actions">
          <mu-raised-button label="进入控制台" href="/dashboard" class="mu-banner-button"/>
          <mu-raised-button label="查看文档" href="/docs" primary/>
        </div>
      </div>
      <div class="mu-banner-appbar-height"></div>
    </div>
    <div class="mu-desc-wrapper">
      <div class="mu-desc">
        <h4 class="mu-desc-title">特性</h4>
        <mu-row gutter>
          <mu-col tablet="33" desktop="33" class="mu-desc-item">
            <h5 class="mu-desc-sub-title">高效</h5>
            <div class="mu-desc-text">
              基于MQTT和Node.js开发的消息服务框架效率很高，足够保证您的物联网设备稳定接入。
            </div>
          </mu-col>
          <mu-col tablet="33" desktop="33" class="mu-desc-item">
            <h5 class="mu-desc-sub-title">安全</h5>
            <div class="mu-desc-text">
              独立的用户机制和全站HTTPS保证了开发、使用过程中的安全。
            </div>
          </mu-col>
          <mu-col tablet="33" desktop="33" class="mu-desc-item">
            <h5 class="mu-desc-sub-title">易用</h5>
            <div class="mu-desc-text">
              精心制作的各种引导、齐全的文档帮助您以最快的速度完成物联网设备的开发与接入。
            </div>
          </mu-col>
        </mu-row>
      </div>
    </div>
    <mu-divider/>
    <div  class="mu-desc-wrapper">
      <div class="mu-desc">
        <h4 class="mu-desc-title">致谢</h4>
        <div class="mu-desc-item">
          <div class="mu-desc-text">
            FreeIOT Web基于非常优秀的前端框架 Vue 开发，后端API基于 Express.js+Mongoose 和 Docker+Aliyun 部署，我们在GPLv3开源协议基础上开放Muse UI的源代码，您除了使用我们的服务外也可利用开源项目自行构建和部署。
          </div>
        </div>
      </div>
    </div>
    <div class="mu-footer">
      Muse-UI is open source and released under the MIT Licence.<br/>
      Copyright (c) 2016 myron
    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      drawer_open: false
    }
  },
  methods: {
    drawer_toggle () {
      this.drawer_open = !this.drawer_open
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style>
.mu-banner{
  background-color: #474a4f;
  z-index: 100;
}
.mu-banner-appbar-height{
  height: 56px;
}
.mu-banner-container {
  margin-right: auto;
  margin-left: auto;
  padding-left: 24px;
  padding-right: 24px;
}
.mu-logo{
  width: 100px;
  height: 100px;
  line-height: 100px;
  text-align: center;
  background-color: #000000;
  margin:  0 auto;
  color: #ffffff;
  font-size: 50px;
  border-radius: 50%;
  margin-bottom: 16px;
}
.mu-banner-title{
  font-weight: 300;
  color: #ffffff;
  margin-top: 0;
  margin-bottom: 16px;
  text-align: center;
}
.mu-banner-sub-title{
  color: #ffffff;
  text-align: center;
  font-weight: 280;
  margin-bottom: 16px;
}
.mu-banner-button{
  color: #474a4f;
}
.mu-banner-actions{
  text-align: center;
}
.mu-desc-wrapper{
  background-color: fade(#ffffff, 30%);
}
.mu-desc{
  padding: 24px 24px 48px 24px;
  max-width: 800px;
  margin: auto;
}
.mu-desc-title{
  font-weight: 400;
  padding-left: 16px;
  padding-right: 16px;
}
.mu-desc-sub-title{
  font-weight: 400;
  font-size: 18px;
}
.mu-desc-item {
  padding-left: 16px;
  padding-right: 16px;
}
.mu-desc-text {
  font-size: 16px;
  line-height: 22px;
}
@media (min-width: 480px) {
  .mu-banner-appbar-height{
    height: 64px;
  }
}
.mu-footer{
  background-color: #212121;
  text-align: center;
  color: #ffffff;
  padding: 50px 40px;
}
</style>
