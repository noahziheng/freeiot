# MQTTAdapter 说明文档

MQTTAdapter 是官方支持用于提供 MQTT 服务监听能力的 Adapter。

```python
from libfreeiot.adapters.mqtt import MQTTAdapter

app.run(adapters=[
    ...
    MQTTAdapter()
])
```

## 配置

在 `.env` 中新增部分配置如下：

| 配置项            | 默认值         | 描述                                                      |
| ----------------- | -------------- | --------------------------------------------------------- |
| MQTT_HOST         | localhost      | MQTT Broker 地址                                          |
| MQTT_PORT         | 1883           | MQTT Broker 地址                                          |
| MQTT_CLIENTID     | "mqtt-adapter" | 连接到 MQTT Broker 时使用的 clientID                      |
| TOPICS_NEED       | []             | MQTT 服务需关注的 topic，使用 JSON 数组表示               |
| MQTT_PARSE_DRIVER | json           | `WIP` MQTT 服务消息解析驱动，可选 json, msgpack, protobuf |

## 依赖

MQTTAdapter 需要一个支持 MQTT3 协议的 MQTT Broker，建议使用 [Eclipse Mosquitto](https://mosquitto.org/)。

## 设备上离线

### 设备上线识别

MQTTAdapter 将默认订阅 `SYS/online` Topic，向其中 Publish 包括以下字段的消息即可触发设备上线：

| 字段名  | 类型   | 描述         |
| ------- | ------ | ------------ |
| id      | String | 设备识别 ID  |
| version | String | 设备版本信息 |

设备上线动作触发后，MQTTAdapter 将进行如下操作：

- 将设备状态置为 `STATUS_WAIT`
- 订阅配置中 `TOPICS_NEED` 声明的对应数据主题

### 设备离线识别

MQTTAdapter 将默认订阅 `SYS/will` Topic，向其中 Publish 包括以下字段的消息即可触发设备离线：

| 字段名  | 类型   | 描述         |
| ------- | ------ | ------------ |
| id      | String | 设备识别 ID  |
| version | String | 设备版本信息 |

设备离线动作触发后，MQTTAdapter 将进行如下操作：

- 将设备状态置为 `STATUS_WAIT`
- 订阅配置中 `TOPIC_NEEDS` 声明的对应数据主题

建议使用 MQTT 中的遗嘱（will）机制处理离线消息的发送，并使用 Qos `2`，以保证设备异常失去响应时系统也能触发离线动作。

## 数据发送

MQTTAdapter 对数据相关 topic(MQTT) 中的信息做出如下规定：

- 每条消息均需有“主题”（topic(FreeIOT)）用于描述消息类别，如电池电量的 topic(FreeIOT) 定为 `battery`
- 每条消息必须有来源/目标设备，以设备识别 ID 描述，如上述电量消息来自设备 `5a9a8e31baf04c21194069d3`
- 每条消息必须有一标志字段（flag）标明传输类型（现支持 `u` 代表设备到系统，`d` 代表系统到设备），如上述电量消息由设备上传，flag 为 `u`
- 每条消息不论封装格式，发到 Topic 中就是内容（`content`）如电池电量当前为 20%，经 JSON 编码后就是 `20`
- 则上述的电量消息应 publish 到 `5a9a8e31baf04c21194069d3/battery/u` 这一 topic(MQTT)，消息内容为 `20`

MQTTAdapter 将默认在设备上线后订阅所有 `TOPICS_NEED` 中声明的 `<your device objectid>/<topic>/u` 和 `<your device objectid>/<topic>/d` Topic，向其中 Publish 任意消息即可触发数据存储：

数据存储动作触发后，MQTTAdapter 将进行如下操作：

- 新建数据记录，分段记录 `topic`、`content`、`flag`
- 更新设备 `lastdata` 字段记载的本 topic 最新数据

## `WIP` 使用 MQTT 更新设备状态

> 本功能在当前版本中暂未支持

MQTTAdapter 将默认订阅 `SYS/status` Topic，向其中 Publish 包括以下字段的消息即可触发设备状态更新：

| 字段名  | 类型   | 描述         |
| ------- | ------ | ------------ |
| id      | String | 设备识别 ID  |
| status | Int | 设备状态 |

设备上线动作触发后，MQTTAdapter 将进行如下操作：

- 新建数据记录，记录 `topic` 为 status，`content` 为 status 段内容，`flag` 为 `sys`
- 检查配置中 `TOPICS_NEED` 声明的对应数据主题的订阅状态，及时补订
- 将设备状态置为 status 段内容
