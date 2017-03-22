# 常见问题解答(FAQ)
这里提供了您使用FreeIOT中可能遇到问题的解答，如您在本页面内未能找到问题的答案，请按照以下方法反馈给我们：
- 到FreeIOT的[Github项目](https://github.com/noahziheng/freeiot)上提交issue
- 发送邮件到[noahgaocn@outlook.com](mailto:noahgaocn@outlook.com)

## Web客户端常见问题
<span id="web"></span>

### 当设备1小时内数据超过500条后可能导致加载缓慢甚至无法加载
<span id="web-1"></span>
这是因为APIv1的加载机制为了保证浏览流畅和较差网络环境下最新数据的同步，会一次推送1小时内全部数据，数据量过大时会导致客户端的解析部分响应缓慢，我们会尽快通过对API的补丁更新和客户端版本迭代优化该问题

## Android客户端常见问题
<span id="android"></span>

#### 手动入网向导可能卡在正在寻找设备中进度框
<span id="android-1"></span>
这是个确定的BUG，正在准备修复中
#### 当设备1小时内数据超过500条后可能导致严重卡顿
<span id="android-2"></span>
请参考Web客户端的[相同问题](#web-1)

## 系统开发常见问题
<span id="system"></span>

## FreeIOT Firmware常见问题
<span id="firmware"></span>

#### ESP8266-连接到WIFI的W指令和初始化设备的D指令执行后可能阻塞设备运行
<span id="android-2"></span>
是由于MQTT库的一些问题导致的，请在使用这两个指令后重新对模块上电，模块将自动完成连接的全部操作
