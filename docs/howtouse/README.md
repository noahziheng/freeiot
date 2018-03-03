# FreeIOT 快速指南

<span id="header"></span>

> 以下所提到的可执行命令均以 macOS/Linux 环境为例，Windows 环境请将 `python3/pip3` 替换为 `python/pip`

## 安装

<span id="installation"></span>

> FreeIOT 使用 Python3 开发，您应在安装 FreeIOT 之前先行安装 Python3.5 以上版本。

最简单的 FreeIOT 安装方式就是通过 pip 包管理器；当然，我们也提供了基于源码构建安装的方式供选择。

### 通过包管理器安装

```shell
 pip3 install libfreeiot
```

### 构建安装

```shell
 git clone https://github.com/noahziheng/freeiot.git
 cd freeiot
 python3 setup.py install --user
```

## 基本使用

<span id="basic_usage"></span>

一个 FreeIOT 项目需要两个基础文件，一个是 `.env` 配置文件，另一个是你的程序入口，我们使用 `manage.py`。

FreeIOT 需要使用 MongoDB 数据库，我们在以下的实例中使用在本机 `27017`（默认端口）提供服务的 MongoDB 服务，如您的配置有所不同，参见 [配置文件详解](../config.md)

建立 `.env` 文件如下：（可参考项目 Github 仓库中的 [`.env.sample`](https://github.com/noahziheng/freeiot/blob/master/.env.sample) 文件）

```dotenv
PORT = 3000
DEBUG = true
FLASK_CONFIG = development
MONGO_HOST = localhost
MONGO_PORT = 27017
MONGO_DBNAME = freeiot
```

建立 `manage.py` 文件如下：（可参考项目 Github 仓库中的 [`manage.sample.py`](https://github.com/noahziheng/freeiot/blob/master/manage.sample.py) 文件）

```python
from libfreeiot import app

app.run()
```

现在，我们只需运行 `python3 manage.py` 即可看到：

![Result of Basic](../images/howtouse/1.png)

现在，FreeIOT 的核心库已经开始工作，可以通过 API 管理 `设备` 和 `数据` 啦~

## 数据

<span id="data"></span>

FreeIOT 核心库支持存管各种 [JSON](https://www.json.org) 所支持的数据格式（底层和 HTTP API 基于 JSON，设备侧传输格式由 Adapter 决定，并提供官方 Adapter 存管二进制文件）。

每条数据都通过来源设备和主题共同描述其来源，主题为字符串，如可令电池数据主题为 `battery`。

数据记录描述格式如下：

| 字段名  | 类型     | 描述                                                  |
| ------- | -------- | ----------------------------------------------------- |
| _id     | ObjectId | 数据标识 ID                                           |
| topic   | String   | 数据来源主题                                          |
| flag    | String   | 数据传输类型标志，默认支持 `u` 代表上传，`d` 代表下放 |
| device  | DBRef    | 数据来源设备指针引用                                  |
| content | Any      | 数据内容                                              |

其中涉及到的数据类型和数字代指如下：

ObjectId 类型（MongoDB）用于描述设备或数据的识别 ID，其格式为：

| 字段名 | 类型   | 描述          |
| ------ | ------ | ------------- |
| $oid   | String | ID 字符串形式 |

DBRef 类型（MongoDB）用于描述关联的其他设备或数据，其格式为：

| 字段名 | 类型   | 描述                                            |
| ------ | ------ | ----------------------------------------------- |
| $ref   | String | 可能为 `devices` 或 `datas`，分别代指设备或数据 |
| $id    | String | 关联到的设备/数据 ID                            |

## 设备

<span id="device"></span>

核心库的主要功能就是提供 `设备` 的信息存管方案，FreeIOT 对一个设备的描述格式如下：

| 字段名   | 类型            | 描述                                                      |
| -------- | --------------- | --------------------------------------------------------- |
| _id      | ObjectId        | 设备标识 ID                                               |
| remark   | String          | 设备备注名                                                |
| status   | Int             | 设备状态（参照下表）                                      |
| version  | String          | 版本信息                                                  |
| lastdata | Object(DataRef) | 最新数据指针引用（使用 Adapter 修改，禁止 REST API POST） |

其中涉及到的数据类型和数字代指如下：

Status 字段可能为：

| 实际内容 | 代指               | 描述                       |
| -------- | ------------------ | -------------------------- |
| -1       | STATUS_BROKEN      | 设备损坏                   |
| 0        | STATUS_UNKNOWN     | 状态未知                   |
| 1        | STATUS_WAIT        | 等待指示（连接后默认状态） |
| 2        | STATUS_RUNNING     | 正在运行                   |
| 3        | STATUS_CHARGING    | 正在充电                   |
| 4        | STATUS_AUTOPROTECT | 设备自我保护               |

ObjectId 类型（MongoDB）用于描述设备或数据的识别 ID，其格式为：

| 字段名 | 类型   | 描述          |
| ------ | ------ | ------------- |
| $oid   | String | ID 字符串形式 |

DBRef 类型（MongoDB）用于描述关联的其他设备或数据，其格式为：

| 字段名 | 类型   | 描述                                            |
| ------ | ------ | ----------------------------------------------- |
| $ref   | String | 可能为 `devices` 或 `datas`，分别代指设备或数据 |
| $id    | String | 关联到的设备/数据 ID                            |

DataRef 类型（FreeIOT）用于记录某个数据主题的状态，详细参考 [数据](#data) 一节的介绍，其格式为：

| 字段名   | 类型   | 描述                 |
| -------- | ------ | -------------------- |
| content  | Any    | 数据的实际内容       |
| flag     | String | 数据的上传、下载标识 |
| original | DBRef  | 对原始数据存档的引用 |

## RESTFul API

由于 FreeIOT 对于 `设备` 和 `数据` 的操作支持集成在核心库内，所以，我们现在已经可以通过 HTTP API 进行读写操作了。

FreeIOT 采用 RESTFul API 作为主接口，使用 Flask 提供服务，集成 Flask-RESTFul 进行参数处理，并引入 [JWT](https://jwt.io) 作为认证手段。

所以，我们首先获取 JWT 凭证，FreeIOT 的默认用户名密码为 `admin/admin`，运行以下命令：

```shell
 curl http://127.0.0.1:3000/api/auth\
    -H "Accept: application/json"\
    -H "Content-type: application/json"\
    -X POST\
    -d '{"username":"admin", "password": "admin"}'
```

可以得到

```json
{
    "jwt": "<your jwt token>"
}
```

再运行：

```shell
 curl http://127.0.0.1:3000/api/device\
    -H "Accept: application/json"\
    -H "Content-type: application/json"\
    -H "Authorization:Bearer <your jwt token>"\
    -X POST\
    -d '{"remark": "测试样机", "status": 0, "version": "Unknown"}'
```

可以得到

```json
{
    "remark": "\u6d4b\u8bd5\u6837\u673a",
    "_id": {
        "$oid": "5a9a8e31baf04c21194069d3"
    }
}
```

已经熟悉 FreeIOT 数据结构和现代 Web 接口的读者已经可以了解到，我们通过该接口建立了一个名为“测试样机”的设备，当前不在线，版本未知。

通过 RESTFul API 可以完整操作 FreeIOT 的各项原子，详细用法参见 [RESTFul API](../api) 一章

## Adapter

`Adapter` 是使用 FreeIOT 开发物联网系统的最主要形式，可用于建立新的查询接口、建立操作界面、兼容新的数据类型以及最关键的使用“物联网”方式收集、发送数据。

RESTFul API 方式并不适合在设备侧使用，有 HTTP 资源消耗较大，开发繁琐，接口需要鉴权一类的缺点。而专为物联网而生的 MQTT 协议就完全解决了这些问题。

如我们希望在上面建立的系统中加入 MQTT 机制，可以修改 `manage.py` 文件如下：

```python
from libfreeiot import app
from libfreeiot.adapters.mqtt import MQTTAdapter

app.run(adapters = [ MQTTAdapter() ])
```

我们可以为 libfreeiot.app 实例的 run 方法中传入 adapters 参数，给出一个包括 Adapter 实例的数组，核心库将使用对应的机制初始化传入的 Adapter。

有关机制和开发文档参见 [Adapter](../adapter) 一章。

MQTTAdpter 是 FreeIOT 官方支持的，包含在 libfreeiot 包内，它的使用请参阅 [MQTTAdapter 说明](../adapter/mqtt) 及 [实例1：MQTT 方案保存温度数据](./ex1.md)。

我们还提供了一些其他的 Adapter，它们的介绍与使用说明参见 [Adapter](../adapter) 一章。

## 写在最后

以上我们简单介绍了 FreeIOT 的基本概念、用法，FreeIOT 的设计目标是一个框架，我们对开发接口（Adapter）、核心接口（RESTFul API）的描述将贯穿整个文档，希望大家能利用 FreeIOT提供的接口方案轻松打造出自己的物联网产品。
