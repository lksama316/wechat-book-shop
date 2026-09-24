# 基于微信小程序的线上图书销售平台（WeChat Book Shop）

> 本科毕业设计项目 · 微信小程序用户端 + Vue 管理后台 + Spring Boot 后端

一个面向中小图书商家的线上图书销售平台。用户端为 uni-app 开发的微信小程序（应用名「掌上图书商城APP」），管理端为 Vue + Element UI 后台，后端为 Spring Boot 提供的 RESTful 接口。系统覆盖图书浏览、购物车、下单、模拟支付、订单状态流转等完整购书流程，并配有后台的图书、分类、订单、用户管理能力。

---

## 目录

- [项目简介](#项目简介)
- [功能特性](#功能特性)
- [技术栈](#技术栈)
- [系统架构说明](#系统架构说明)
- [数据库设计](#数据库设计)
- [项目结构说明](#项目结构说明)
- [接口概览](#接口概览)
- [环境要求与安装步骤](#环境要求与安装步骤)
- [运行方式](#运行方式)
- [核心业务说明](#核心业务说明)
- [后续扩展方向](#后续扩展方向)
- [作者信息与致谢](#作者信息与致谢)

---

## 项目简介

随着移动互联网的普及，读者的购书行为逐渐从线下书店和 PC 端网页转向手机端。微信小程序无需下载安装、即用即走，且天然具备社交传播能力，非常适合中小图书商家低成本地搭建自己的线上销售渠道。

本项目以「图书销售」这一垂直场景为切入点，设计并实现了一个前后端分离的线上图书销售平台，包含三个相互独立、通过 HTTP 接口通信的应用：

| 应用 | 面向对象 | 技术形态 | 说明 |
| --- | --- | --- | --- |
| 小程序用户端 | 普通读者 | uni-app 编译为微信小程序 | 浏览、搜索、加购、下单、支付、订单管理 |
| 后台管理端 | 商家管理员 | Vue 2 + Element UI 单页应用 | 图书、分类、订单、用户管理与数据看板 |
| 服务端 | —— | Spring Boot + MyBatis | 提供全部业务接口，统一鉴权与数据持久化 |

**主要特点：**

1. 采用前后端分离架构，后端按 Controller / Service / DAO 三层组织，职责清晰、便于维护。
2. 订单生成、库存扣减、支付状态更新、销量统计、购物车清理之间做了业务联动，保证数据一致。
3. 订单明细保存下单时的图书名称、图片和价格快照，避免后台修改图书信息后影响历史订单。
4. 使用 Redis 存储登录令牌实现会话管理，前端通过令牌访问受保护接口。
5. 用户端基于 uni-app 开发，一套代码可编译为微信小程序，并具备向 Android / iOS 端扩展的能力。

---

## 功能特性

### 一、前台用户端（微信小程序）

**1. 注册登录**

- 用户注册：用户昵称、密码、确认密码、性别、手机号码；服务端校验昵称唯一性、两次密码一致性，注册用户统一分配为普通用户角色。
- 用户登录：昵称 + 密码校验，登录成功后生成令牌并缓存，小程序端缓存有效期 1 小时。
- 退出登录：清除服务端令牌与本地缓存。
- 路由拦截：`uni.addInterceptor` 对页面跳转统一拦截，未登录时自动跳转登录页（首页、商品列表、商品详情等页面可免登录浏览）。

**2. 图书商品展示**

- 首页轮播图展示。
- 分类导航：读取后台维护的商品分类，点击进入对应分类的图书列表。
- 图书列表：支持「热销 / 新品 / 打折」三个维度切换。
  - 热销：按销量 `sell_num` 倒序取前 3 条；
  - 新品：按上架时间 `create_time` 倒序取前 3 条；
  - 打折：筛选标记为折扣的商品。
- 全部商品页：支持按图书名称关键词搜索，也可按分类筛选；列表展示图书封面、名称、价格、销量。
- 图书详情页：大图展示 + 图书属性表格（上市时间、出版社、图书作者、图书字数、商品重量、商品库存）。
- 折扣价格展示：折扣图书同时展示原价（划线）与折扣价。

**3. 购物车管理**

- 加入购物车：商品列表与详情页均可一键加购；同一用户的同一本图书重复加购时自动合并数量，并再次校验库存。
- 购物车列表：展示图书信息、单价、数量、小计。
- 数量修改：编辑态下支持「- / +」调整数量，数量为 0 时给出提示，超出库存时给出提示。
- 删除图书：支持单条删除。
- 单选 / 全选：勾选需要结算的图书。
- 提交结算：将勾选的购物车项提交生成订单，并跳转订单结算页。

**4. 订单与支付**

- 创建订单：由购物车勾选项生成订单，生成前逐项校验库存。
- 收货地址：结算页展示收货人姓名、电话、地址，支持通过弹窗编辑并保存。
- 模拟支付：点击「支付订单」完成支付（模拟支付，未接入真实支付渠道）。
- 订单状态流转：未支付 → 已支付 → 已发货 → 已收货；未支付订单可取消。
- 我的订单：按「全部 / 未支付 / 已支付 / 已发货 / 已收货 / 已取消」分页签查看，展示订单号、状态、收货信息、图书明细与实付金额。
- 订单操作：未支付订单支持「支付」与「取消订单」。

**5. 个人信息管理**

- 个人资料：查看与修改用户昵称、用户密码、手机号码、用户性别。
- 头像上传：个人中心点击头像选择图片并上传。
- 我的订单：查看全部历史订单及状态。
- 退出登录。

### 二、后台管理端（Vue + Element UI）

**1. 管理员登录**

- 独立的后台登录入口，登录时校验账号角色，仅管理员角色可进入后台系统。

**2. 数据看板（首页）**

- 统计卡片：用户总数、商品总数、订单总数、今日成交额、本周成交额、本月成交额。
- 折线图：近五天交易次数（区分已完成 / 未完成）。
- 饼图：五个最高成交额的商品分类占比。
- 在线用户列表：展示当前在线（令牌有效）的用户。

**3. 用户管理**

- 用户列表：分页展示用户昵称、头像、角色、手机号码，支持按昵称搜索。
- 新增 / 修改 / 删除用户，可设置角色（普通用户 / 管理员）、性别、头像。
- 删除用户时级联清理该用户的购物车与订单数据。

**4. 图书管理**

- 图书列表：分页展示图书编号、名称、价格、封面、所属分类、出版社、作者、字数、库存、重量、销量、是否折扣、折扣价、上架时间，支持按名称搜索。
- 新增 / 修改图书：图书名称、封面图上传、价格、出版社、作者、字数、库存、重量、是否折扣、折扣价、所属分类、上架时间。
- 折扣设置：标记为折扣商品后可设置折扣价，服务端校验折扣价不得高于原价。
- 删除图书：删除图书的同时清理所有购物车中对该图书的引用。
- 库存维护：直接维护图书库存数量。

**5. 分类管理**

- 分类列表：分页展示分类编号、分类图片、排序值、分类名称，支持搜索。
- 新增 / 修改 / 删除分类，可设置分类名称、排序值与分类图片。
- 分类统计：按分类汇总成交额，供首页饼图使用。

**6. 订单管理**

- 订单列表：分页展示订单编号、订单金额、下单用户、订单状态、下单时间，支持按订单号搜索。
- 订单详情：查看订单号、收货人姓名 / 电话 / 地址以及图书明细（图书名称、单价、数量、图片、小计）。
- 修改订单状态：管理员设置订单状态（未支付 / 已支付 / 已发货 / 已收货 / 已取消），即完成发货操作。
- 删除订单：删除订单及其明细。

### 三、后续扩展方向

以下功能已在毕业设计论文中完成设计与规划，当前版本代码尚未实现，作为项目的后续完善方向：

- **售后申请模块**：用户提交退款 / 退换货申请并上传凭证，查看处理进度，可取消申请；管理员审核申请并更新处理状态，状态同步回用户端。
- **商品评价模块**：用户对已收货图书进行评分与文字评价；后台在图书管理中查看评价内容。
- **图书收藏**：用户收藏图书并在个人中心查看收藏列表。
- **个性化推荐**：基于用户浏览与购买历史，在首页生成个性化推荐位（当前首页为热销 / 新品 / 打折的通用推荐）。
- **物流信息**：订单填写快递单号、展示物流轨迹，并为小程序端补充「确认收货」操作入口（当前收货状态由后台修改订单状态完成）。
- **收货地址簿**：支持维护多个收货地址并设置默认地址（当前每个用户保存一个收货地址，在结算页编辑）。

---

## 技术栈

### 后端

| 技术 | 版本 | 说明 |
| --- | --- | --- |
| Spring Boot | 2.2.5.RELEASE | 基础框架，内嵌 Tomcat |
| JDK | 1.8 | 编译与运行环境 |
| MyBatis | mybatis-spring-boot-starter 1.3.2 | 持久层框架 |
| PageHelper | 1.2.13 | MyBatis 分页插件 |
| Druid | 1.1.19 | 数据库连接池 |
| Fastjson | 1.2.31 | JSON 序列化 |
| MySQL Connector/J | 5.1.47 | 数据库驱动 |
| Spring Data Redis | 随 Spring Boot 2.2.5 | 令牌（会话）存储 |
| spring-boot-devtools | 2.0.4.RELEASE | 热部署 |
| JUnit | 4.12 | 单元测试 |
| 构建工具 | Maven | `groupId: com.yjq.programmer`，`artifactId: business` |

### 后台管理端

| 技术 | 版本 |
| --- | --- |
| Vue | 2.6.11 |
| Element UI | 2.15.6 |
| ECharts | 5.2.2 |
| Vuex | 3.6.2 |
| Vue Router | 3.5.3（history 模式） |
| Axios | 0.24.0 |
| jQuery | 3.6.0 |
| Vue CLI | 4.5.x |
| Sass | node-sass 4.14.1 / sass-loader 8.0.2 |

### 小程序用户端

| 技术 | 版本 / 说明 |
| --- | --- |
| uni-app | 一套代码多端编译，本项目编译为微信小程序 |
| uView UI | 2.0.16（通过 easycom 自动引入） |
| Vue | 2.x |
| 接口请求 | 对 `uni.request` / `uni.uploadFile` 的轻量封装（`web/util/api.js`） |
| 本地缓存 | 对 `uni.setStorageSync` 的封装，带过期时间（`web/util/cache.js`） |

### 开发与调试工具

IntelliJ IDEA（后端）、HBuilderX（小程序）、VS Code（后台管理端）、微信开发者工具（小程序调试）、Navicat（数据库管理）。

### 系统环境

Windows 10 / Windows 11。

---

## 系统架构说明

### 1. 前后端分离架构

系统由三个独立部署的应用构成，客户端与服务器之间只通过 HTTP + JSON 交互，后端不负责页面渲染：

```
┌──────────────────────────┐        ┌──────────────────────────┐
│   微信小程序（用户端）      │        │   Web 后台管理端（Vue）     │
│  uni-app + uView UI      │        │  Vue + Element UI + Axios │
│  http://localhost:8081   │        │  http://127.0.0.1:8081    │
└────────────┬─────────────┘        └─────────────┬────────────┘
             │  HTTP / JSON（POST）                │  HTTP / JSON（POST）
             │  令牌 token 随请求提交               │  令牌 token 置于请求头
             └─────────────────┬──────────────────┘
                               ▼
             ┌─────────────────────────────────────────┐
             │        Spring Boot 服务端 :8081          │
             │  Controller  →  Service  →  DAO          │
             │  统一响应 ResponseDTO + 全局异常处理        │
             └───────┬───────────────────────┬─────────┘
                     ▼                       ▼
              ┌────────────┐          ┌────────────┐
              │   MySQL    │          │   Redis    │
              │ 业务数据    │          │ 登录令牌    │
              └────────────┘          └────────────┘
```

**跨域处理**：后端通过全局 CORS 配置开放 `/**` 路径，允许 GET / POST / PUT / DELETE / OPTIONS 请求，保证小程序端与后台管理端可以直接调用接口。

**统一响应格式**：所有接口统一返回 `ResponseDTO<T>`，包含 `code`（状态码）、`msg`（提示信息）、`data`（业务数据），状态码与提示语集中定义在 `CodeMsg` 中（如 `0` 表示成功、`-6` 表示会话失效）。前端只需判断 `code` 即可处理成功与各类失败情况。

**统一异常处理**：`ExceptionsHandler` 对业务异常与系统异常做全局捕获，避免异常堆栈直接暴露给客户端。

**鉴权方式**：用户登录成功后，服务端使用短 UUID 生成令牌并以 `USER_{token}` 为键写入 Redis（有效期 3600 秒），同时将用户信息序列化后存入；客户端保存该令牌，后续请求携带令牌，服务端据此识别当前用户。后台管理端在登录时额外校验角色，非管理员角色无法登录后台（`-1011`）。

### 2. 后端三层架构

后端按 Controller / Service / DAO 三层组织，并通过 DTO 与实体对象分离内外部数据结构：

| 层次 | 包路径 | 职责 |
| --- | --- | --- |
| 控制层 Controller | `com.yjq.programmer.controller` | 接收请求、参数校验、组装统一响应，按调用方分为 `web`（小程序端）、`admin`（后台）、`common`（公共）三组 |
| 业务层 Service | `com.yjq.programmer.service` / `.impl` | 业务规则与流程编排（库存校验、状态流转、联动处理），实现类统一标注 `@Transactional` 保证事务 |
| 持久层 DAO | `com.yjq.programmer.dao` | MyBatis Mapper 接口，`dao/my` 下为自定义查询 |
| 实体 / 传输对象 | `domain` / `dto` | `domain` 为数据库表映射实体（含 MyBatis Generator 生成的 Example 条件类），`dto` 为面向接口的数据传输对象 |
| 通用支撑 | `bean` / `enums` / `exception` / `util` / `config` | 统一状态码、枚举、异常处理、工具类与配置 |

其中控制层进一步按调用方分组，使不同端的接口互不干扰：

- `controller/web`：小程序用户端接口（`/web/**`）；
- `controller/admin`：后台管理端接口（`/admin/**`）；
- `controller/common`：公共接口，目前为图片上传与访问（`/photo/**`）。

---

## 数据库设计

数据库名：`db_book_shop_app`（MySQL）。系统共涉及 7 张业务表：

| 表名 | 说明 |
| --- | --- |
| `user` | 用户表（昵称、密码、头像、手机号码、性别、角色） |
| `category` | 图书分类表（分类名称、分类图片、排序值） |
| `product` | 图书表（名称、价格、是否折扣、折扣价、出版社、作者、字数、库存、销量、重量、封面、所属分类、上架时间） |
| `cart` | 购物车表（用户、图书、数量） |
| `orders` | 订单表（订单号、下单用户、订单状态、订单金额、收货人姓名 / 电话 / 地址、下单时间） |
| `order_item` | 订单明细表（所属订单、图书、图书名称 / 图片 / 单价快照、数量、小计） |
| `address` | 收货地址表（所属用户、收货人姓名、电话、地址） |

**核心枚举取值：**

| 枚举 | 取值 |
| --- | --- |
| 订单状态 `OrderStateEnum` | `1` 未支付、`2` 已支付、`3` 已发货、`4` 已收货、`5` 已取消 |
| 是否折扣 `DiscountEnum` | `1` 否、`2` 是 |
| 用户角色 `RoleEnum` | `1` 普通用户、`2` 管理员 |
| 用户性别 | `1` 男、`2` 女、`3` 未知 |

> 说明：订单主键 `orders.id` 为 8 位短 UUID，面向用户的订单号 `orders.no` 由雪花算法（SnowFlake）生成 18 位流水号；其余表（用户、图书、分类、购物车、订单明细、地址）主键同样使用 8 位短 UUID。

> 注意：仓库中未包含建表 SQL 脚本，需按上表结构自行创建数据库与数据表（可从本地 Navicat 导出后导入）。

---

## 项目结构说明

```
java_project/
├── BookShopApp/
│   ├── business/                          # Spring Boot 后端
│   │   ├── pom.xml                        # Maven 依赖与构建配置
│   │   └── src/main/
│   │       ├── java/com/yjq/programmer/
│   │       │   ├── BookShopApp.java       # 启动类
│   │       │   ├── annotation/            # 自定义注解（ValidateEntity 参数校验）
│   │       │   ├── bean/                  # CodeMsg 统一状态码与提示信息
│   │       │   ├── config/                # CorsConfig 全局跨域配置
│   │       │   ├── controller/
│   │       │   │   ├── admin/             # 后台管理端接口
│   │       │   │   │   ├── CategoryController.java
│   │       │   │   │   ├── OrderController.java
│   │       │   │   │   ├── ProductController.java
│   │       │   │   │   └── UserController.java
│   │       │   │   ├── common/            # 公共接口（PhotoController 图片上传与访问）
│   │       │   │   └── web/               # 小程序用户端接口
│   │       │   │       ├── AddressController.java
│   │       │   │       ├── CartController.java
│   │       │   │       ├── CategoryController.java
│   │       │   │       ├── OrderController.java
│   │       │   │       ├── ProductController.java
│   │       │   │       └── UserController.java
│   │       │   ├── dao/                   # MyBatis Mapper 接口
│   │       │   │   └── my/                # 自定义查询（MyOrderMapper、MyCategoryMapper）
│   │       │   ├── domain/                # 数据库实体与 Example 条件类
│   │       │   ├── dto/                   # 接口传输对象（ResponseDTO、PageDTO 等）
│   │       │   ├── enums/                 # DiscountEnum、OrderStateEnum、RoleEnum
│   │       │   ├── exception/             # ExceptionsHandler 全局异常处理
│   │       │   ├── service/               # 业务接口
│   │       │   │   └── impl/              # 业务实现（含 OrderServiceImpl 等）
│   │       │   └── util/                  # 工具类（SnowFlake、UuidUtil、CaptchaUtil 等）
│   │       └── resources/
│   │           ├── application.properties      # 主配置（端口、上传大小限制等）
│   │           ├── application-dev.properties  # 开发环境配置（数据源、上传路径等）
│   │           ├── generator/generatorConfig.xml  # MyBatis Generator 逆向工程配置
│   │           ├── mapper/                # MyBatis 映射文件
│   │           │   └── my/               # 自定义 SQL（统计类查询）
│   │           └── upload/photo/          # 图片上传目录
│   └── admin/                             # Vue 后台管理端
│       ├── package.json                   # 依赖与脚本
│       ├── .env.dev                       # 开发环境变量（VUE_APP_SERVER）
│       ├── vue.config.js                  # webpack 配置（全局注入 jQuery）
│       ├── public/static/js/              # tool.js、session-storage.js（全局脚本）
│       └── src/
│           ├── main.js                    # 入口（axios 拦截器、路由守卫）
│           ├── App.vue
│           ├── router.js                  # 路由配置（登录校验）
│           ├── components/
│           │   ├── CommonAside.vue        # 侧边导航菜单
│           │   ├── CommonHeader.vue       # 顶栏（面包屑、用户信息、退出登录）
│           │   └── ECharts.vue            # ECharts 图表封装组件
│           ├── store/                     # Vuex（index.js、tab.js）
│           └── views/
│               ├── Login.vue              # 后台登录
│               ├── Main.vue               # 整体布局
│               ├── Home.vue               # 数据看板
│               ├── UserList.vue           # 用户管理
│               ├── CategoryList.vue       # 分类管理
│               ├── ProductList.vue        # 图书管理
│               └── OrderList.vue          # 订单管理
└── web/                                   # uni-app 小程序用户端
    ├── main.js                            # 入口（挂载请求方法）
    ├── App.vue                            # 应用配置（globalData）
    ├── pages.json                         # 页面路由、tabBar、全局样式
    ├── manifest.json                      # 应用配置（AppID、打包信息）
    ├── uni.scss                           # 全局样式变量
    ├── pages/
    │   ├── index/index.vue                # 首页（轮播、分类导航、图书列表）
    │   ├── product/product.vue            # 全部图书（关键词搜索、分类筛选）
    │   ├── product-detail/product-detail.vue  # 图书详情
    │   ├── cart/cart.vue                  # 购物车
    │   ├── order/order.vue                # 订单结算（收货地址、支付）
    │   ├── member/member.vue              # 个人中心（头像、入口菜单）
    │   ├── member-info/member-info.vue    # 个人资料
    │   ├── member-order/member-order.vue  # 我的订单
    │   ├── login/login.vue                # 登录
    │   └── register/register.vue          # 注册
    ├── router/index.js                    # 页面跳转拦截（登录校验）
    ├── util/
    │   ├── api.js                         # 请求封装与 BASE_URL
    │   ├── cache.js                       # 带过期时间的本地缓存封装
    │   └── tool.js                        # 通用工具方法
    ├── static/                            # 静态资源（轮播、导航、图书、tabBar 图标）
    ├── uview-ui/                          # uView UI 组件库（2.0.16）
    └── uni_modules/                       # uni-app 插件模块
```

---

## 接口概览

后端统一监听 `8081` 端口，接口均以 POST 方式提交 JSON 数据，按调用方分为 `/web`（小程序端）、`/admin`（后台管理端）、`/photo`（公共）三组。

### 小程序用户端 `/web/**`

| 模块 | 接口 | 说明 |
| --- | --- | --- |
| 用户 | `/web/user/register` | 用户注册 |
| 用户 | `/web/user/login` | 用户登录 |
| 用户 | `/web/user/logout` | 退出登录 |
| 用户 | `/web/user/get` | 获取当前用户信息 |
| 用户 | `/web/user/update` | 修改用户信息 |
| 分类 | `/web/category/list` | 分类列表 |
| 图书 | `/web/product/hot` | 热销图书 |
| 图书 | `/web/product/new` | 新品图书 |
| 图书 | `/web/product/discount` | 折扣图书 |
| 图书 | `/web/product/all` | 图书列表 / 关键词搜索 / 分类筛选 |
| 图书 | `/web/product/get` | 图书详情 |
| 购物车 | `/web/cart/add` | 加入购物车 |
| 购物车 | `/web/cart/get` | 购物车列表 |
| 购物车 | `/web/cart/update` | 修改购买数量 |
| 购物车 | `/web/cart/remove` | 删除购物车项 |
| 地址 | `/web/address/save` | 保存收货地址 |
| 地址 | `/web/address/get` | 获取收货地址 |
| 订单 | `/web/order/generate` | 生成订单（下单） |
| 订单 | `/web/order/get` | 订单详情 |
| 订单 | `/web/order/all` | 我的订单列表（可按状态筛选） |
| 订单 | `/web/order/pay` | 支付订单 |
| 订单 | `/web/order/cancel` | 取消订单 |
| 订单 | `/web/order/update` | 更新订单信息 / 状态 |

### 后台管理端 `/admin/**`

| 模块 | 接口 | 说明 |
| --- | --- | --- |
| 用户 | `/admin/user/login` | 管理员登录（校验角色） |
| 用户 | `/admin/user/logout` | 退出登录 |
| 用户 | `/admin/user/check_login` | 校验登录状态 |
| 用户 | `/admin/user/online` | 在线用户列表 |
| 用户 | `/admin/user/list` | 用户分页列表 |
| 用户 | `/admin/user/save` | 新增 / 修改用户 |
| 用户 | `/admin/user/remove` | 删除用户 |
| 用户 | `/admin/user/total` | 用户总数 |
| 分类 | `/admin/category/list` | 分类分页列表 |
| 分类 | `/admin/category/all` | 全部分类（下拉选择用） |
| 分类 | `/admin/category/save` | 新增 / 修改分类 |
| 分类 | `/admin/category/remove` | 删除分类 |
| 分类 | `/admin/category/total-price` | 分类成交额统计 |
| 图书 | `/admin/product/list` | 图书分页列表 / 名称搜索 |
| 图书 | `/admin/product/save` | 新增 / 修改图书 |
| 图书 | `/admin/product/remove` | 删除图书 |
| 图书 | `/admin/product/total` | 图书总数 |
| 订单 | `/admin/order/list` | 订单分页列表 / 订单号搜索 |
| 订单 | `/admin/order/order-item` | 订单详情与明细 |
| 订单 | `/admin/order/edit-state` | 修改订单状态（发货等） |
| 订单 | `/admin/order/remove` | 删除订单 |
| 订单 | `/admin/order/total` | 订单总数 |
| 订单 | `/admin/order/today-price` | 今日成交额 |
| 订单 | `/admin/order/week-price` | 本周成交额 |
| 订单 | `/admin/order/month-price` | 本月成交额 |
| 订单 | `/admin/order/count-state-date` | 近五天各状态订单统计 |

### 公共接口 `/photo/**`

| 接口 | 说明 |
| --- | --- |
| `/photo/upload_photo` | 图片上传（图书封面、分类图片、用户头像） |
| `/photo/view?filename=xxx` | 图片访问 |

---

## 环境要求与安装步骤

### 1. 环境要求

| 组件 | 版本要求 | 说明 |
| --- | --- | --- |
| JDK | 1.8 | 后端编译与运行 |
| Maven | 3.x | 后端依赖管理（IDEA 内置亦可） |
| MySQL | 5.7 | 业务数据库（驱动为 `mysql-connector-java` 5.1.47） |
| Redis | 5.x 及以上 | 存储登录令牌，默认 `127.0.0.1:6379`（配置文件中未显式指定，使用 Spring Boot 默认值） |
| Node.js | 建议 14.x | 后台管理端构建（`node-sass` 4.14 对 Node 版本较敏感，不建议使用过高版本） |
| 浏览器 | Chrome / Edge | 后台管理端运行 |
| HBuilderX | 最新版 | 小程序端开发与编译 |
| 微信开发者工具 | 最新版 | 小程序预览与调试 |

### 2. 安装步骤

**（1）准备数据库**

在 MySQL 中创建数据库 `db_book_shop_app`，并按[数据库设计](#数据库设计)一节创建 7 张业务表，导入初始数据（分类、图书、管理员账号等）。

> 说明：仓库中未提供建表 SQL 脚本，需自行准备。

**（2）启动 Redis**

确保本机 Redis 服务已启动（默认端口 6379）。若 Redis 设置了密码或使用非默认端口，需在 `application-dev.properties` 中补充相应配置。

**（3）修改后端配置**

编辑 `BookShopApp/business/src/main/resources/application-dev.properties`，按本机环境修改数据库连接信息：

```properties
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/db_book_shop_app?serverTimezone=GMT%2b8&useUnicode=true&characterEncoding=utf8
spring.datasource.username=root
spring.datasource.password=你的数据库密码
```

同时修改图片上传目录为**本机实际路径**（该配置为绝对路径，默认值指向开发者的本地目录）：

```properties
yjq.upload.photo.path=D:/java_project/BookShopApp/business/src/main/resources/upload/photo/
```

**（4）安装后台管理端依赖**

```bash
cd BookShopApp/admin
npm install
```

**（5）配置接口地址（如后端未部署在本机默认端口）**

- 后台管理端：修改 `BookShopApp/admin/.env.dev` 中的 `VUE_APP_SERVER`；
- 小程序端：修改 `web/util/api.js` 中的 `BASE_URL`。

---

## 运行方式

### 1. 后端（Spring Boot）

**方式一：IDEA 运行**

使用 IntelliJ IDEA 以 Maven 项目方式打开 `BookShopApp/business`，等待依赖下载完成后，运行启动类 `com.yjq.programmer.BookShopApp` 的 `main` 方法。

**方式二：Maven 命令运行**

```bash
cd BookShopApp/business
mvn spring-boot:run
```

启动成功后服务监听 `http://localhost:8081`，接口基础路径即为该地址（如登录接口为 `http://localhost:8081/web/user/login`）。

后端主要配置项（`application.properties`）：

```properties
server.port=8081                      # 服务端口
server.servlet.session.timeout=1800   # 会话超时（秒）
spring.profiles.active=dev            # 激活的开发环境配置
spring.servlet.multipart.max-file-size=300MB      # 单文件上传上限
spring.servlet.multipart.max-request-size=300MB   # 单次请求上传上限
```

### 2. 后台管理端（Vue）

```bash
cd BookShopApp/admin
npm install
npm run serve-dev
```

- 开发环境**必须使用 `npm run serve-dev`**：环境变量文件为 `.env.dev`，只有 `--mode dev` 模式会加载它；使用默认的 `npm run serve` 将无法读取 `VUE_APP_SERVER`，导致接口地址为空。
- 启动后按控制台输出的本地地址（默认 `http://localhost:8080`）在浏览器中访问。
- 使用管理员账号登录后台（需数据库中存在 `roleId = 2` 的管理员用户）。
- 生产构建：`npm run build`（构建产物输出至 `dist`，部署前需注意上述环境变量问题）。

### 3. 小程序端（uni-app → 微信小程序）

**方式一：HBuilderX（推荐）**

1. 使用 HBuilderX 打开 `web` 目录；
2. 在 `manifest.json` → 「微信小程序配置」中确认/填写自己的微信小程序 AppID；
3. 点击菜单「运行」→「运行到小程序模拟器」→「微信开发者工具」，HBuilderX 会自动编译并唤起微信开发者工具；
4. 若微信开发者工具未开启服务端口，需在其「设置」→「安全设置」中开启「服务端口」。

**方式二：微信开发者工具直接打开**

在 HBuilderX 中执行「发行」→「小程序-微信」生成编译产物，再用微信开发者工具导入该产物目录进行预览与真机调试。

**注意事项：**

- `web/util/api.js` 中的 `BASE_URL` 默认为 `http://localhost:8081`。在微信开发者工具中调试时需勾选「不校验合法域名」；使用真机预览时，需将其改为**本机局域网 IP**（如 `http://192.168.1.100:8081`），并确保手机与电脑处于同一网络。
- 小程序端本地缓存的登录令牌有效期为 3600 秒，超时后需重新登录。

---

## 核心业务说明

### 1. 订单与支付流程

完整链路：**加入购物车 → 勾选结算 → 生成订单（校验并扣减库存）→ 填写/确认收货地址 → 支付订单（更新状态、累计销量、清理购物车）→ 后台发货 → 确认收货**。

**（1）生成订单** —— `POST /web/order/generate`

1. 校验登录状态，令牌失效时返回「还未登录或会话失效，请重新登录」；
2. 校验提交的购物车项列表非空（为空返回「请选择要结算的商品」）；
3. 逐项读取图书信息，校验 `图书库存 ≥ 购买数量`，库存不足时返回相应提示；
4. 生成订单主键 `id`（8 位短 UUID），并为每个购物车项写入一条订单明细 `order_item`，同时**立即扣减对应图书的库存**；
5. 计算订单总金额：若图书为折扣商品，按折扣价 `newPrice` 计算，否则按原价 `price` 计算，逐项累加得到 `totalPrice`；
6. 生成面向用户的 18 位订单号 `no`（雪花算法），订单状态置为「未支付（1）」；
7. 返回新订单的 `id`，小程序端据此跳转订单结算页。

> 订单明细中保存了图书的**名称、图片与单价快照**，即使后台后续修改或删除该图书，历史订单展示的仍是下单时的信息，保证订单数据的可追溯性。

**（2）支付订单** —— `POST /web/order/pay`

1. 校验订单 `id` 与收货地址 `addressId`（未选择收货地址时返回「收货地址不能为空」）；
2. 将收货地址中的收货人姓名、电话、地址**快照保存到订单**中，避免用户后续修改地址影响历史订单；
3. 订单状态更新为「已支付（2）」；
4. 逐项累计图书销量 `sellNum += 购买数量`，并删除该用户购物车中对应的图书记录，完成购物车清理。

**（3）订单状态流转**

| 状态值 | 状态 | 触发方 |
| --- | --- | --- |
| `1` | 未支付 | 下单后默认状态 |
| `2` | 已支付 | 用户支付订单 |
| `3` | 已发货 | 管理员在后台修改订单状态 |
| `4` | 已收货 | 管理员在后台修改订单状态 |
| `5` | 已取消 | 用户取消未支付订单；管理后台取消订单 |

### 2. 库存联动

库存与销量的联动贯穿下单、支付、取消三个环节，全部由 `OrderServiceImpl` 在同一个事务中完成：

| 环节 | 库存变化 | 销量变化 | 购物车变化 |
| --- | --- | --- | --- |
| 生成订单 | **扣减**（`stock - 数量`） | 不变 | 不变 |
| 支付订单 | 不变 | **增加**（`sellNum + 数量`） | **删除已下单的购物车项** |
| 取消订单 | **回滚**（`stock + 数量`） | 不变 | 不变 |

此外：

- 加入购物车与修改购买数量时都会校验库存，数量超过库存会被拒绝，从源头减少无效订单；
- 库存扣减发生在**订单生成**阶段（而非支付阶段），以降低并发下超卖的风险；
- 删除图书时，会先清理所有购物车中对该图书的引用，再删除图书记录。

### 3. 数据统计联动

后台数据看板的数据来自 `MyOrderMapper` 中的自定义统计查询，与订单状态保持一致：

- **成交额统计**（今日 / 本周 / 本月）：对 `orders` 表按时间范围汇总 `total_price`，统计时**排除未支付（1）与已取消（5）的订单**，保证成交额口径准确；
- **近五天交易次数折线图**：按天分别统计「已完成（已支付 / 已发货 / 已收货）」与「未完成（未支付 / 已取消）」的订单数量；
- **分类成交额饼图**：按图书分类汇总成交金额，取前五名展示；
- 图书销量 `sellNum` 随支付累计，首页「热销」列表即按该字段倒序取前三条。

### 4. 售后流程（设计规划）

售后模块目前尚未实现，论文中规划的设计流程为：用户在个人中心对已支付/已收货订单发起退款或退换货申请并上传凭证 → 管理员在后台审核并更新处理状态 → 处理进度同步回用户端，用户可查看进度或在处理前取消申请。该模块的数据库表、接口与管理端菜单均未在当前版本代码中落地，列于[后续扩展方向](#三后续扩展方向)中。

---

## 后续扩展方向

1. **售后模块落地**：新增售后申请表，实现申请提交、凭证上传、管理员审核、状态同步的完整流程。
2. **商品评价体系**：已收货订单支持评分与评价，后台可查看评价内容并据此优化选品。
3. **个性化推荐**：基于用户浏览、收藏与购买记录生成推荐位，替代当前的热销/新品/打折通用推荐。
4. **物流信息完善**：订单增加快递单号与物流轨迹，小程序端补充「确认收货」入口。
5. **地址簿**：支持维护多个收货地址与默认地址。
6. **支付与安全增强**：接入真实支付渠道；密码加密存储；为登录注册增加图形验证码（项目已内置验证码工具类，尚未接入）。
7. **工程化完善**：补充建表 SQL 脚本与初始化数据、统一分页大小配置（当前部分列表分页大小固定为 5）、为数据看板图表增加窗口自适应。

---

