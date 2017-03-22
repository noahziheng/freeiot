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

| 地址 | 端口 | 协议版本 | 状态 |
| ---------- | --- |
|  api.iot.noahgao.net | 1883 | 3.1.1 | **开放** |

### MQTT身份识别
FreeIOT使用MQTT协议中的设备名和“遗嘱”(Will)机制来解决身份识别问题：

 > 注：下表中()内为变量，()不是格式的内容

| 设备名格式 | 遗嘱topic | 遗嘱格式 |
| ---------- | --- |
|  iot/(device id)/ | logout | (device id)/(device secret) |

例如以下代码（来自FreeIOT ESP8266 Firmware,Arduino）:
```C++
MQTT::Connect con("iot/" + device[0] + "/");
con.set_will("logout", device[0] + "/" + device[1]);
```
