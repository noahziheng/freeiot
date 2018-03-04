# 设备接口

有关设备描述格式，参见 [FreeIOT 快速指南](../howtouse/README.md#device)

- 接口地址： `/api/device`
- 接口类型： 全功能

## 独立查询：GET `/api/device/<id>`

该接口返回以下字段：

| 字段名   | 类型            | 描述             |
| -------- | --------------- | ---------------- |
| _id      | ObjectId        | 设备标识 ID      |
| remark   | String          | 设备备注名       |
| status   | Int             | 设备状态         |
| version  | String          | 版本信息         |
| lastdata | Object(DataRef) | 最新数据指针引用 |

例：

```shell
 curl http://127.0.0.1:3000/api/device/5a9a8e31baf04c21194069d3\
    -H "Accept: application/json"\
    -H "Content-type: application/json"\
    -H "Authorization:Bearer <YOUR JWT TOKEN>"
```

可以得到

```json
{
    "_id": {
        "$oid": "5a9a8e31baf04c21194069d3"
    },
    "remark": "\u6d4b\u8bd5\u6837\u673a",
    "status": 1,
    "version": "test version",
    "lastdata": {
        "tempature": {
            "flag": "u",
            "content": 20,
            "original": {
                "$ref": "datas",
                "$id": {
                    "$oid": "5a9b71a4baf04c3898fc9a2c"
                }
            }
        }
    }
}
```

## 全局查询：GET `/api/device`

使用方式同独立查询，返回包含全部系统中设备的数组，略

## 新增记录：POST `/api/device`

该接口接受以下字段：

| 字段名  | 类型   | 描述                                                  |
| ------- | ------ | ----------------------------------------------------- |
| remark   | String          | 设备备注名       |
| status   | Int             | 设备状态         |
| version  | String          | 版本信息         |

该接口返回以下字段：

| 字段名 | 类型     | 描述        |
| ------ | -------- | ----------- |
| _id    | ObjectId | 数据标识 ID |

例：

```shell
 curl http://127.0.0.1:3000/api/device\
    -H "Accept: application/json"\
    -H "Content-type: application/json"\
    -H "Authorization:Bearer <YOUR JWT TOKEN>"\
    -X POST\
    -d '{"remark": "测试样机", "status": 0, "version": "Unknown"}'
```

可以得到

```json
{
    "$oid": "5a9a8e31baf04c21194069d3"
}
```

## 修改记录：POST `/api/device/<id>`

使用方式同新增记录，只提交的 URL 不同，略

## 删除记录： DELETE `/api/device/<id>`

该接口返回以下字段：

| 字段名 | 类型  | 描述                     |
| ------ | ----- | ------------------------ |
| n      | Int   | 受影响数据个数，通常为 1 |
| ok     | Float | 执行结果 1.0 代表成功    |

例：

```shell
 curl http://127.0.0.1:3000/api/device/5a9a8e31baf04c21194069d3\
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
