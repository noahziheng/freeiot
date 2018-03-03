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

现在，FreeIOT 的核心库已经开始工作，可以通过 API 管理 `设备` 啦~

## 设备
