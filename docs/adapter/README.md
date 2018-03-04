# Adapter 开发文档

Adapter 是 FreeIOT 的核心，设计上就是通过 Adapter 为基于 FreeIOT 开发的系统提供各项外围功能。我们为它提供了一些内部接口，这些接口还会进一步扩展功能，欢迎大家一起出谋划策。

## 顺序调用

首先我们来看一下 Adapter 的传入过程，借用快速指南中的一个启动脚本：

```python
from libfreeiot import app
from libfreeiot.adapters.mqtt import MQTTAdapter

app.run(adapters = [ MQTTAdapter() ])
```

显然，我们在核心实例的 `run` 方法中将一个存有 Adapter 实例的数组传给了 `adapters` 参数，通过这种方式启用了 MQTTAdapter。

事实上，`app.run()` 方法将按顺序调用传入的 Adapter 实例的 `init` 和 `run` 方法，所以我们可以利用 Adapter 机制将功能拼组起来。大多数场景下，我们可以这样使用：

```python
from libfreeiot import app
from libfreeiot.adapters.mqtt import MQTTAdapter
from libfreeiot.adapters.file import FileAdapter
from libfreeiot.adapters.graphql import GraphQLAdapter
from libfreeiot.adapters.visual import VisualAdapter
from libfreeiot.adapters.auth import AuthAdapter
from custom.adapters.example import CustomAdapter

app.run(adapters = [
    MQTTAdapter(), # MQTT 数据监听
    FileAdatper(), # 文件存储
    GraphQLAdapter(), # GraphQL 查询接口
    VisualAdapter(), # 数据可视化界面
    AuthAdapter(), # 用户认证
    CustomAdapter() # 自行实现，实现新接口用于下发 MQTT 消息，必须在 MQTTAdapter 后
])
```

## 顺序作用域

上节中最后的例子中提到，自行实现的用于下发 MQTT 消息的 CustomAdapter，必须在 MQTTAdapter 后，这是因为 Adapter 一项全局机制 -- 顺序作用域。

它通过 `init(app, scope)` 方法被注入到 Adapter 中，其中的 app 为执行到此时的 Flask App 实例，scope 为执行到此时的作用域变量（本质是字典）。

scope 中可能有如下值（官方提供的）：

| 字段名     | 描述                                                     |
| ---------- | -------------------------------------------------------- |
| mongo      | MongoDB 操作指针，具体使用可参考 Flask-PyMongo 文档      |
| mqttClient | 由 MQTTAdapter 注入，当前连接到 MQTT Broker 的客户端实例 |

下面我们来手动实现一个检测 MongoDB 指针注入情况的 Adapter 例子，体会一下这一机制（变量）的用法：

```python
class ExampleAdapter():
    """ Example Adapter Class """

    def init(self, app, scope):
        """ Initial Function for Adapter """
        self.app = app
        self.scope = scope
        self.mongo = scope["mongo"]

    def run(self):
        """ Main Entry for Adapter """
        print("Hello from ExampleAdapter")
        print(self.scope["mongo"] == self.mongo) # True
        print(self.mongo.db.devices.find()) # All devices
```

## BaseAdapter

我们建议所有的 Adapter 都继承于 BaseAdapter，包括官方的。

BaseAdapter 只有两个方法，`init(app, scope)` 和 `run()`。

`init(app, scope)` 方法用于供核心将 Flask App 实例和 Adapter 顺序作用域注入各 Adapter，覆盖该方法时需要调用 BaseAdapter 父类中的方法

`run()` 方法作为整个 Adapter 的入口，核心将按 Adapter 的传入顺序进行调用。

例如，我们修改上面的例子如下：

```python
from libfreeiot.adapters.base import BaseAdapter

class ExampleAdapter(BaseAdapter):
    """ Example Adapter Class """

    def init(self, app, scope):
        """ Initial Function for Adapter，not required by default """
        super(ExampleAdapter, self).init(app, scope)

    def run(self):
        """ Main Entry for Adapter """
        print("Hello from ExampleAdapter")
        mongo = self.scope["mongo"]
        print(self.scope["mongo"] == mongo) # True
        print(mongo.db.devices.find()) # All devices
```

## 增加新的 API 访问接口（Flask Endpoint）

前面已经提到，Flask App 实例也按顺序注入了 Adapter，那么我们在 Adapter 中即可增加新的接口。

例如，我们增加 `GET /api/example` 接口，返回 `{"msg": "Hello World"}`，则可以写出这样一个 Adapter（同目录 `example.py`）：

```python
from flask import jsonify
from libfreeiot.adapters.base import BaseAdapter

class ExampleAdapter(BaseAdapter):
    """ Example Adapter Class """

    def run(self):
        """ Main Entry for Adapter """
        print("Hello from ExampleAdapter")
        app = self.app

        @app.route('/api/example')
        def example():
            """ Example GET Route """
            return jsonify({"msg": "Hello World"})
```

将其挂入运行序列（同目录 `manage.py`）：

```python
from libfreeiot import app
from example import ExampleAdapter

app.run(adapters = [ ExampleAdapter() ])
```

运行后，可观察 命令行中的 `Hello from ExampleAdapter`，随后运行下面的指令可验证其的工作：

```shell
 curl http://127.0.0.1:3000/api/example\
    -H "Accept: application/json"\
    -H "Content-type: application/json"\
```

可得到

```json
{
    "msg": "Hello World"
}
```

至此，已经可以在 Adapter 中使用 Flask 的方式开发接口了~

> 注意：目前在 Adapter 中新增的端口默认均不配有 JWT 认证，需使用 Flask-JWT-Simple 提供的 jwt_required 函数，实现方式请参看该项目文档
