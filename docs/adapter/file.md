# FileAdapter 说明文档

FileAdapter 是官方支持用于提供二进制文件上传能力的 Adapter。

```python
from libfreeiot.adapters.file import FileAdapter

app.run(adapters=[
    ...
    FileAdapter()
])
```

## 配置

当前版本无需配置即可使用

## （增加 API）上传文件：POST /api/upload

> 该接口不受核心库 JWT 保护，无需使用认证头
> 该接口不是标准 JSON 接口，需设置头 Content-Type: multipart/form-data

该接口接受以下字段：

| 字段名 | 类型 | 描述       |
| ------ | ---- | ---------- |
| file   | File | 待上传文件 |

该接口返回以下字段：

**注意** 该接口返回 JSON 编码的字符串，内容为上传后的存放文件名，可用其他手段（如 MQTT）上传到核心系统中。

## （增加 API） 获取文件：GET /api/upload/<filename>

该接口将直接返回请求的文件，filename query 代表存放文件名。如文件不存在，返回 HTTP 404。
