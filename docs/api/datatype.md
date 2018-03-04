# 附录: 数据类型

设备的 Status 字段可能为：

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

DataRef 类型（FreeIOT）用于记录某个数据主题的状态，详细参考 [数据](../howtouse/README.md#data) 一节的介绍，其格式为：

| 字段名   | 类型   | 描述                 |
| -------- | ------ | -------------------- |
| content  | Any    | 数据的实际内容       |
| flag     | String | 数据的上传、下载标识 |
| original | DBRef  | 对原始数据存档的引用 |