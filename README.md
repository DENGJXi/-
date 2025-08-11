📦 项目简介
本项目是一个后台管理系统，适合中小型电商业务，采用分层架构开发，具备以下功能：

✅ 商品管理（增删改查、上架/下架、库存管理、逻辑删除、redis库存缓存）

✅ 订单管理（下单、分页查询、按时间区间查询）

✅ 用户登录认证（JWT + Redis 实现 token 机制）//实现中

✅ MyBatis 多表联查 + VO 封装展示

✅ 使用 RESTful 风格接口设计，前后端分离

❌ 权限控制（Spring Security）//未实现

✅MySQL索引优化
## 查询优化效果对比

下图展示了在 `goods` 表上添加联合索引 (`category_id`, `status`, `create_time`) 前后的执行计划差异：
<img width="1437" height="87" alt="image" src="https://github.com/user-attachments/assets/72f6cb45-d713-447f-9014-65a3dd2b061f" />
<img width="1539" height="105" alt="image" src="https://github.com/user-attachments/assets/85416b3f-b6a8-41f7-a343-3ec1a41b1aae" />
<img width="831" height="433" alt="image" src="https://github.com/user-attachments/assets/1c651343-52c8-420a-9d10-b22125cc672d" />

