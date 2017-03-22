# 嵌入式客户端 概述
FreeIOT是一个物联网平台，标准客户端用来展示云端的来自硬件设备的数据，而沟通云端和硬件设备的就是嵌入式客户端。FreeIOT使用MQTT协议交换设备实时数据，一个嵌入式客户端需要符合FreeIOT规范才能被服务器接纳入网。

## 已适配的嵌入式客户端列表

| 客户端名称 | 开发者 | Github仓库 |
| ---------- | --- |
|  [**FreeIOT Firmware**](firmware.md) | FreeIOT | https://github.com/noahziheng/freeiot |

> 请注意：以下内容主要用于嵌入式客户端原理的了解和开发新的嵌入式客户端，如您想简单的使用FreeIOT，请直接了解使用上面的开源嵌入式客户端。

## 开发新的嵌入式客户端
> 注：如您开发了新的嵌入式客户端，我们希望您将项目能将其开源，并共享给大家，有关事宜请联系noahgaocn@gmail.com

### MQTT协议连接
如您还不了解MQTT，请自行Google了解协议的基本原理，本文不在赘述，首先您需要连接到FreeIOT的MQTT Broker，它的情况如下：

| 地址 | 端口 | 协议版本 | 状态 | Qos级别 |
| ---------- | --- |
|  api.iot.noahgao.net | 1883 | 3.1.1 | **开放** | 0 |
|  test.iot.noahgao.net | 1883 | 3.1.1 | **关闭** | 1 |

### MQTT身份识别
FreeIOT使用MQTT协议中的设备名和“遗嘱”(Will)机制来解决身份识别问题：

 > 注：下表中()内为变量，()不是格式的内容

| 设备名格式 | 遗嘱topic | 遗嘱格式 |
| ---------- | --- |
|  iot/(device id)/ | logout | (device id)/(device secret) |

例如以下代码（来自FreeIOT ESP8266 Firmware,Arduino）:

```C++
MQTT::Connect con("iot/" + device[0] + "/"); // 以指定设备名连接，device[0]是设备的id
con.set_will("logout", device[0] + "/" + device[1]); // 设定遗嘱，device[0]是设备的id，device[1]是设备的secret
```
### 数据报文
> 建议您先阅读系统设计理念一章中[数据](../design/data.md)和[模块](../design/mod.md)两小节

连接到服务器后您可按当前设备对应的产品原型中的定义发送数据报文，请注意不同驱动的模块数据不能集合发送，需要拆分为两条报文。

上传量和控制反馈可以被发到 ```uplink``` 这一topic中，系统将对其进行解析。

 > 注：下面的()内为变量，()不是格式的内容

而控制量需要订阅 ```(device id)-d``` topic来获取控制量报文，如```H7ydaf9-d```

### 设备入网
根据前面的介绍，我们会发现，设备发送数据需要先了解设备的id和秘钥，而嵌入式客户端获取该信息、连接到互联网、连接到服务器，服务器接收，这一过程我们称之为入网。

入网方式无非两种，预置入网，由硬件设备通过串口等方式将信息发到嵌入式客户端，并操作嵌入式客户端连接互联网完成入网流程；另一种是手动适配入网，我们的Android客户端对其提供了支持，使用FreeIOT Firmware协议和SoftAP模式进行。故如您想使用Android客户端兼容的手动入网方式，请参照[FreeIOT Firmware](firmware.md)中的介绍。
