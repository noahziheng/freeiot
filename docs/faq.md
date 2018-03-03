# 常见问题解答(FAQ)

这里提供了您使用FreeIOT中可能遇到问题的解答，如您在本页面内未能找到问题的答案，请按照以下方法反馈给我们：

- 到 FreeIOT 的 [Github仓库](https://github.com/noahziheng/freeiot) 提交issue。
- 发送邮件到 [noahgaocn@outlook.com](mailto:noahgaocn@outlook.com)。

## 核心库常见问题

<span id="core"></span>

### RESTFul API 只能依据数据 ObjectId 单条提取数据吗？

<span id="core-1"></span>
是的，数据信息的 REST API 接口我们仅建议小规模使用，较大规模查询建议使用 `GraphQLAdapter(WIP)` 或开发专项的 Adapter 进行数据分析后提供特定的 API 接口。

## Adapter 常见问题

<span id="adapter"></span>

### 问题1

<span id="adapter-1"></span>

### 问题2

<span id="adapter-2"></span>

## 系统开发常见问题

<span id="system"></span>

### 我能不能使用 MongoDB 以外的其他数据库

<span id="system-1"></span>
抱歉，目前版本的核心库只支持 MongoDB，未来将引入数据库驱动机制，是 MySQL, PostgreSQL, Redis 等可作为数据存储系统。
