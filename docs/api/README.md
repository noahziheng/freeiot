# RESTFul API

FreeIOT 使用 RESTFul API 作为“核心接口”，总体比较简单，只包括核心功能和常用接口，有几条通用的原则列在下面：

- 各接口均包含 JWT 验证头，请在了解接口前阅读 [权限管理](./auth.md)
- 所有接口提交返回的数据格式均为 [JSON](https://www.json.org)
- 本文档中的全功能代指具备以下几种能力：
  - 全局查询（GET .../XXX/）
  - 独立查询（GET .../XXX/`<id>`）
  - 新增记录（POST .../XXX/）
  - 修改记录（POST .../XXX/`<id>`）
  - 删除记录（DELETE .../XXX/`<id>`）
  - 注：与原版 REST 不同的是，FreeIOT 基于兼容考虑并未实现 PUT 方法修改
- 各接口均具有参数解析，ObjectId、DBRef 等 Mongo 类型无需提交完整对象，只需在对应字段提交 id 字符串即可
- 数据返回均返回原始数据结构，如访问 ObjectId 类型的字符串形式，需使用 _id.$oid
