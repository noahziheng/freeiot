# API 扩展

基于 FreeIOT 的轻巧原则，核心库并不包含各类“高级”接口，相关功能均借由 Adapter 实现。

Adapter 中可以获取到核心库初始化后的 Flask App 实例，可使用该实例向系统中添加接口，参见 [增加新的 API 接口](../adapter/README.md#add-new-endpoint)
