# FreeIOT Firmware
FreeIOT Firmware是FreeIOT官方实现的嵌入式客户端，并有自己的协议和规范，可供常规开发者使用，与FreeIOT各接口完全兼容。也可作为客户端开发者的参考。

## 支持的设备列表

| 设备名称 | 状态 | Github仓库 |
| ---------- | --- |
|  [**ESP8266**](esp8266.md) | 支持中 | https://github.com/noahziheng/freeiot-esp8266 |

## 组成
FreeIOT Firmware通常包括三个主要部分：
  - 串口通信：用于与硬件设备通信，接受特定的操作指令和封装好的数据报文，并通过MQTT客户端透传数据。
  - TCP 服务器（软AP模式）：用于使用Android客户端操作设备入网
  - MQTT客户端：用于与FreeIOT平台通信，使用串口或TCP服务器得到的信息连接到服务器后透传串口接收到的数据报。

以上三个部分使用统一的协议运作。
## 协议
FreeIOT Firmware向外提供的接口使用统一协议。值得注意的是，TCP和串口的功能是完全一致的，他们会共用解析操作模块。也就是说，串口也可被用于入网，TCP也可被用于数据透传。

每条数据报或指令都以 ```+``` 开始， ```;```结束，请不要使用"\\n"换行符，那也将被视为一个字符。

### 数据报文
请注意：本文提到的透传也需要使用上述的的开始和结束头来完成。

如您需要发送 ```T:12,H:78.1,S:true,R:abc``` 到服务器，请向串口或TCP服务器发送：

```
  +T:12,H:78.1,S:true,R:abc;
```

json协议也是一样，如您需要发送 ```{ "T" : 12, "H" : 78.1, "S" : true, "R" : "abc" }``` 到服务器，请向串口或TCP服务器发送：

```
  +{"T":12,"H":78.1,"S":true,"R":"abc"};
```

### 指令
FreeIOT Firmware的操作指令使用simple协议完成，有关simple协议的细节参照[模块](../design/mod.md)。

#### DEBUG指令
当您向Firmware发送```+DEBUG;```,Firmware将进入调试模式，在串口打印调试信息。

#### P指令
P指令用于向Firmware提供产品原型识别信息，只有提供该信息才能初始化设备。

格式：```+P:(product id),(product secret);```

例： ```+P:T8Dds8a_l,fjsdkghbvuidjoepwof234;```

#### D指令
D指令用于向Firmware提供设备识别信息，只有提供该信息才能连接到服务器。

格式：```+D:(device id),(device secret);```

例： ```+D:Y6Dddga_l,5y8gjtrf4w3pwofds234;```

#### O指令
O指令将在Firmware连接到服务器后被发到串口

例： ```+O;```

#### W指令
W指令只存在于使用Wifi连接的版本中，用于向Firmware提供Wifi连接信息，只有提供该信息才能连接到互联网。

格式：```+W:(SSID),(PASSWORD);```

例： ```+D:FreeIOT_WIFI,12345678;```

#### U指令
U指令只存在于支持OTA的版本中，用于向Firmware提供OTA新版本信息，Firmware收到该指令后将**停止工作**，并尝试更新，将进度等信息发到串口。

格式：```+U:(VERSION);```

例： ```+D:v1.0-preview-20170201A1TJ;```

## TCP服务器连接
通常Firmware TCP服务器的自身IP（网关）为192.168.4.1，端口号开在3000。

TCP服务器为支持Android客户端手动联网和其他需要获知设备情况的场景，在客户端连接上时将发送一个固定格式的报文。
如：（//后为注释，发送时为单行，此处为示意将其改为多行）

```
  V:
  1.0, // 版本号
  preview, // 版本标示
  20170201A1TJ, // 版本build号
  0; // 是否支持OTA,0为否，1为是
  P:
  S1Oy49-Pg, // 产品原型id
  f19f86880hfheAdsdsfsvvdbgf57c; // 产品原型秘钥
```
