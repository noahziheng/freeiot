# 配置文件详解

FreeIOT 采用 [dotenv](https://github.com/motdotla/dotenv) 作为配置的读取、管理手段，有关语法不再赘述，请大家阅读 dotenv 的简明文档即可很快了解。下表列出了在 `.env` 配置文件中可使用的配置项名称、描述及默认值（标有 `WIP` 的项目代表实现仍未发布，配置可能无效）：

| 配置项            | 默认值         | 描述                                                                          |
| ----------------- | -------------- | ----------------------------------------------------------------------------- |
| PORT              | 3000           | 核心 API 服务端口                                                             |
| DEBUG             | true           | 调试模式开关（布尔值，全小写）                                                |
| FLASK_CONFIG      | development    | Flask 配置项，可选production,development,testing                              |
| MONGO_HOST        | localhost      | MongoDB 数据库地址                                                            |
| MONGO_PORT        | 27017          | MongoDB 数据库端口                                                            |
| MONGO_DBNAME      | freeiot        | MongoDB 数据库名                                                              |
| ADMIN_PASSWORD    | admin          | `WIP` 核心 API 鉴权服务 admin 用户密码                                        |

注1：核心库对 MongoDB 支持更多配置，如认证等，可参照 [Flask-PyMongo 文档](https://flask-pymongo.readthedocs.io/en/latest/) 配置
注2：`.env` 文件应放置在当前运行路径（CWD）的同目录或上层目录
