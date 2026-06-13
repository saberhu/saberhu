







# 阜阳台球约战小程序

> 阜阳本地台球爱好者约战平台 - 纯非盈利个人项目

## 技术栈

| 模块 | 技术 |
|------|------|
| 后端 | Spring Boot 2.7.18 + MyBatis Plus 3.5.3.1 + JDK 17 |
| 数据库 | MySQL 8.0 |
| 前端 | 微信小程序原生框架 + Vant Weapp 1.10.2 |
| 地图 | 高德地图小程序SDK |
| 本地运行 | Docker Compose |

## 项目结构

```
/workspace
├── backend/               # Spring Boot 后端项目
│   ├── src/main/java/com/billiards/
│   │   ├── controller/    # 控制层
│   │   ├── service/       # 业务层
│   │   ├── mapper/        # 数据访问层
│   │   ├── entity/        # 实体类
│   │   ├── dto/           # 数据传输对象
│   │   ├── common/        # 通用类
│   │   └── BilliardsApplication.java
│   ├── src/main/resources/
│   │   ├── application.yml
│   │   └── mapper/
│   ├── pom.xml
│   └── Dockerfile
├── frontend/              # 微信小程序前端
│   ├── pages/
│   │   ├── index/         # 首页
│   │   ├── ballroom/      # 球房
│   │   ├── challenge/     # 约战
│   │   └── user/          # 个人中心
│   ├── app.js
│   ├── app.json
│   └── app.wxss
├── sql/
│   └── init.sql           # 数据库初始化脚本
├── docker-compose.yml     # 本地开发一键启动
└── README.md
```

## 数据库设计

共 6 张核心表：

1. **user** - 用户表（openid、昵称、头像、段位积分、胜场、负场、信用分）
2. **ballroom** - 球房表（名称、地址、电话、价格、营业时间、经纬度、图片、评分）
3. **challenge** - 约战表（发起人、球房、球种、赛制、开始时间、状态、比分、胜者）
4. **challenge_signup** - 约战报名表（约战ID、用户ID、报名状态）
5. **ballroom_review** - 球房评价表（球房ID、用户ID、评分、内容）
6. **ballroom_favorite** - 球房收藏表（用户ID、球房ID）

## 本地启动步骤

### 方式一：Docker Compose 一键启动（推荐）

```bash
# 1. 启动所有服务
docker-compose up -d

# 2. 查看日志
docker-compose logs -f

# 3. 停止服务
docker-compose down
```

启动后：
- 后端 API：http://localhost:8080
- MySQL：localhost:3306

### 方式二：手动启动后端

```bash
# 1. 启动 MySQL（确保本地已安装）
mysql -u root -p < sql/init.sql

# 2. 编译后端
cd backend
mvn clean package -DskipTests

# 3. 启动后端
java -jar target/billiards-1.0.0.jar
```

### 方式三：微信小程序前端

使用微信开发者工具打开 `frontend/` 目录，修改 `app.js` 中的 `baseUrl` 为实际后端地址即可运行。

## 环境变量说明

| 变量 | 说明 | 默认值 |
|------|------|--------|
| MYSQL_HOST | 数据库主机 | db |
| MYSQL_PORT | 数据库端口 | 3306 |
| MYSQL_DATABASE | 数据库名 | billiards |
| MYSQL_USER | 数据库用户 | root |
| MYSQL_PASSWORD | 数据库密码 | root123 |

## 接口概览

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/user/login | 微信登录 |
| GET | /api/user/{id} | 获取用户信息 |
| PUT | /api/user/{id}/profile | 更新用户资料 |
| GET | /api/ballroom/page | 球房列表（分页） |
| GET | /api/ballroom/{id} | 球房详情 |
| GET | /api/ballroom/{id}/reviews | 球房评价列表 |
| POST | /api/ballroom/favorite | 收藏/取消收藏 |
| GET | /api/ballroom/favorite/check | 检查是否收藏 |
| POST | /api/challenge/create | 创建约战 |
| GET | /api/challenge/page | 约战列表（分页） |
| GET | /api/challenge/{id} | 约战详情 |
| POST | /api/challenge/{id}/cancel | 取消约战 |
| POST | /api/challenge/{id}/finish | 完成约战 |
| POST | /api/challenge/{id}/signup | 报名约战 |
| POST | /api/review/submit | 提交评价 |

## 注意事项

1. 本项目为个人非盈利项目，无支付、商家入驻、运营后台功能
2. 仅限阜阳市本地使用，数据量小，采用极简设计
3. 生产环境部署请修改 `application.yml` 中的数据库密码等敏感信息
4. 微信登录需替换 `UserServiceImpl` 中的微信 API 调用为真实接口






