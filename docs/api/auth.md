# 权限管理

FreeIOT 默认采用 [JWT](https://jwt.io) 作为权限管理手段。

## 鉴权请求头 `Authorization:Bearer <your jwt token>`

每个 FreeIOT 核心接口都需要在有认证情况下访问，均需带有这一请求头，其中的 JWT Token 可使用下文的 `/api/auth` 接口认证得到。

## 主认证：POST `/api/auth`

该接口接受以下字段：

| 字段名   | 类型   | 描述   |
| -------- | ------ | ------ |
| username | String | 用户名 |
| password | String | 密码   |

该接口返回以下字段：

| 字段名 | 类型   | 描述             |
| ------ | ------ | ---------------- |
| jwt    | String | JWT Token 字符串 |

例：

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
