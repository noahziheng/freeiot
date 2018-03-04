# Adapter 开发建议

## 尽量使用 BaseAdapter

由于 FreeIOT 仍处在积极开发，仍将加入、调整内部接口，所以我们建议使用注入部分已封装的 BaseAdapter 作为新 Adapter 的基类，这样也能获取到最新版本 API 对 BaseAdapter 进行完善后的便利。

## 将接口控制在 `/api` 和 `/ui` 内

为了避免全局污染，建议在开发新 Adapter 时，新增的访问点均带有 `/api`（接口）和 `/ui`（界面）前缀

文档仍在编写，此文优先级较低，如有开发问题请提出 Issue。
