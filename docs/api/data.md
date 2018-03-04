# 数据接口

有关数据描述格式，参见 [FreeIOT 快速指南](../howtouse/README.md#data)

- 接口地址： `/api/data`
- 接口类型： 全功能（限制全局查询及记录修改）

## 独立查询：GET `/api/data/<id>`

该接口返回以下字段：

| 字段名  | 类型     | 描述                                                  |
| ------- | -------- | ----------------------------------------------------- |
| _id     | ObjectId | 数据标识 ID                                           |
| topic   | String   | 数据来源主题                                          |
| flag    | String   | 数据传输类型标志，默认支持 `u` 代表上传，`d` 代表下放 |
| device  | DBRef    | 数据来源设备指针引用                                  |
| content | Any      | 数据内容                                              |

例：

```shell
 curl http://127.0.0.1:3000/api/data/5a9b71a4baf04c3898fc9a2c\
    -H "Accept: application/json"\
    -H "Content-type: application/json"\
    -H "Authorization:Bearer <YOUR JWT TOKEN>"
```

可以得到

```json
{
    "_id": {
        "$oid": "5a9b71a4baf04c3898fc9a2c"
    },
    "device": {
        "$ref": "devices",
        "$id": "5a9a8e31baf04c21194069d3"
    },
    "topic": "tempature",
    "flag": "u",
    "content": 20
}
```

## 新增记录：POST `/api/data`

该接口接受以下字段：

| 字段名  | 类型   | 描述                                                  |
| ------- | ------ | ----------------------------------------------------- |
| topic   | String | 数据来源主题                                          |
| flag    | String | 数据传输类型标志，默认支持 `u` 代表上传，`d` 代表下放 |
| device  | String | 数据来源设备标识 ID                                   |
| content | Any    | 数据内容（JSON 编码字符串）                           |

该接口返回以下字段：

| 字段名 | 类型     | 描述        |
| ------ | -------- | ----------- |
| _id    | ObjectId | 数据标识 ID |

例：

```shell
 curl http://127.0.0.1:3000/api/data\
    -H "Accept: application/json"\
    -H "Content-type: application/json"\
    -H "Authorization:Bearer <YOUR JWT TOKEN>"\
    -X POST\
    -d '{"device": "5a9a8e31baf04c21194069d3", "topic": "tempature", "flag": "u", "content": "20"}'
```

可以得到

```json
{
    "$oid": "5a9b7d14baf04c3c51ad851c"
}
```

## 删除记录： DELETE `/api/data/<id>`

该接口返回以下字段：

| 字段名 | 类型  | 描述                     |
| ------ | ----- | ------------------------ |
| n      | Int   | 受影响数据个数，通常为 1 |
| ok     | Float | 执行结果 1.0 代表成功    |

例：

```shell
 curl http://127.0.0.1:3000/api/data/5a9b7d14baf04c3c51ad851c\
    -H "Accept: application/json"\
    -H "Content-type: application/json"\
    -H "Authorization:Bearer <YOUR JWT TOKEN>"\
    -X DELETE
```

可以得到

```json
{
    "n": 1,
    "ok": 1.0
}
```
