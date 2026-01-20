# 易经卜卦服务 (YY-Yao)

基于 Spring Boot 4.0.1 的现代化易经卜卦服务，提供传统卜卦算法、JWT 认证、AI 智能解读等功能。

> 📘 **重构说明**: 本项目已参考 [yi-jing-divination-app](https://github.com/Wadud-Ma/yi-jing-divination-app) 进行重构优化。详见 [REFACTORING.md](./REFACTORING.md)

## ✨ 功能特点

- **🔮 多种卜卦方法**: 铜钱法、蓍草法、数字法、时间起卦法
- **📚 完整卦象数据**: 64卦数据，包含卦辞、爻辞和解释
- **🔄 动爻与变卦**: 自动识别动爻并计算变卦
- **🤖 AI 智能解读**: 支持 OpenAI、通义千问等 LLM API
- **🔐 JWT 认证**: 安全的用户认证和授权系统
- **📡 RESTful API**: 完整的 REST 接口,统一响应格式
- **💊 健康检查**: 内置系统监控和健康检查端点
- **⚙️ 配置管理**: 支持环境变量,便于容器化部署

## 🛠️ 技术栈

- **Java 21**: 最新 LTS 版本
- **Spring Boot 4.0.1**: 企业级应用框架
- **Spring Security**: 安全认证和授权
- **JWT (JJWT 0.12.6)**: 无状态令牌认证
- **Spring Data JPA**: 数据持久化
- **MySQL**: 主数据库
- **Spring Boot Actuator**: 监控和健康检查
- **Maven**: 项目管理
- **Lombok**: 简化代码

## 🚀 快速开始

### 前置要求

- **JDK 21** 或更高版本
- **Maven 3.8+**
- **MySQL 5.7+** 或其他支持的数据库
- **(可选) LLM API Key** - 用于 AI 解读功能

### 安装步骤

#### 1. 克隆项目
```bash
git clone <repository-url>
cd yy-yao
```

#### 2. 配置数据库

编辑 `src/main/resources/application.properties` 或设置环境变量:

```bash
export DATABASE_URL="jdbc:mysql://localhost:3306/yy-yao?useUnicode=true&characterEncoding=UTF-8"
export DB_USERNAME="root"
export DB_PASSWORD="your_password"
```

#### 3. 配置 JWT 密钥

**重要**: 生成安全的 JWT 密钥(至少32字符)

```bash
# 生成随机密钥
export JWT_SECRET=$(openssl rand -base64 32)

# 或手动设置
export JWT_SECRET="your-random-secret-key-at-least-32-characters-long"
```

#### 4. (可选) 配置 LLM API

如果需要 AI 解读功能:

```bash
# OpenAI
export LLM_API_URL="https://api.openai.com/v1/chat/completions"
export LLM_API_KEY="sk-your-openai-api-key"
export LLM_MODEL="gpt-3.5-turbo"

# 或使用通义千问
export LLM_API_URL="https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions"
export LLM_API_KEY="sk-your-qwen-api-key"
export LLM_MODEL="qwen-turbo"
```

#### 5. 构建并运行

```bash
mvn clean install
mvn spring-boot:run
```

服务将在 `http://localhost:8080` 启动。

#### 6. 验证服务

```bash
# 健康检查
curl http://localhost:8080/api/health

# 系统信息
curl http://localhost:8080/api/system/info
```

### 使用 .env 文件配置 (推荐)

```bash
# 复制示例文件
cp .env.example .env

# 编辑 .env 文件,填写实际配置
vim .env
```

## 📖 API 文档

### 认证相关

#### 1. 用户注册

**请求**
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123",
  "email": "test@example.com"
}
```

**响应**
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "username": "testuser",
    "role": "USER"
  },
  "timestamp": "2026-01-19T10:30:00"
}
```

#### 2. 用户登录

**请求**
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123"
}
```

**响应**
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "username": "testuser",
    "role": "USER"
  },
  "timestamp": "2026-01-19T10:30:00"
}
```

#### 3. 获取当前用户

**请求**
```http
GET /api/auth/me
Authorization: Bearer <token>
```

**响应**
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "username": "testuser",
    "role": "USER",
    "authenticated": true
  },
  "timestamp": "2026-01-19T10:30:00"
}
```

### 卜卦相关

#### 1. 执行卜卦 (需要认证)

**请求**
```http
POST /api/divination/consult
Content-Type: application/json
Authorization: Bearer <token>

{
  "question": "今年事业运势如何？",
  "method": "COIN",
  "needAiInterpretation": true
}
```

**卜卦方法选项**:
- `COIN`: 铜钱法 (传统三枚铜钱法)
- `YARROW`: 蓍草法 (传统蓍草占卜)
- `NUMBER`: 数字法 (现代简化方法)
- `TIME`: 时间起卦法 (根据时间生成)

**响应**
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "timestamp": "2026-01-19T10:30:00",
    "question": "今年事业运势如何？",
    "originalHexagram": {
    "number": 1,
    "name": "乾",
    "symbol": "☰☰",
    "binary": "111111",
    "statement": "乾: 元亨,利贞。",
    "interpretation": "乾卦象征天,代表刚健、进取...",
    "lineStatements": [...]
  },
  "changedHexagram": {...},
  "changingLines": [2, 5],
  "lines": [...],
    "method": "COIN",
    "interpretation": "【本卦】乾卦...",
    "aiInterpretation": "根据您得到的乾卦..."
  },
  "timestamp": "2026-01-19T10:30:00"
}
```

#### 2. 自定义卦象 (需要认证)

如果您已经通过其他方式得到卦象，可以直接输入:

```http
POST /api/divination/consult
Content-Type: application/json
Authorization: Bearer <token>

{
  "question": "测试问题",
  "customLines": "789678",
  "needAiInterpretation": true
}
```

`customLines` 格式: 6位数字，从下到上，每位为:
- 6: 老阴(动爻)
- 7: 少阳
- 8: 少阴
- 9: 老阳(动爻)

#### 3. 查询单个卦象 (需要认证)
```http
GET /api/divination/hexagram/1
Authorization: Bearer <token>
```

#### 4. 获取所有卦象 (需要认证)
```http
GET /api/divination/hexagrams
Authorization: Bearer <token>
```

### 系统监控

#### 1. 健康检查 (公开)

```http
GET /api/health
```

**响应**
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "status": "UP",
    "timestamp": "2026-01-19T10:30:00",
    "dependencies": {
      "database": "UP",
      "llm": "CONFIGURED"
    }
  },
  "timestamp": "2026-01-19T10:30:00"
}
```

#### 2. 系统信息 (公开)
```http
GET /api/system/info
```

**响应**
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "applicationName": "易经卜卦服务",
    "version": "1.0.0",
    "javaVersion": "21.0.1",
    "osName": "Mac OS X",
    "osVersion": "14.0",
    "authEnabled": true,
    "aiEnabled": false
  },
  "timestamp": "2026-01-19T10:30:00"
}
```

#### 3. 系统统计 (公开)
```http
GET /api/system/stats
```

**响应**
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "totalMemory": 512,
    "freeMemory": 256,
    "usedMemory": 256,
    "maxMemory": 1024,
    "availableProcessors": 8,
    "uptime": 3600000
  },
  "timestamp": "2026-01-19T10:30:00"
}
```

## 易经基础知识

### 卦象组成

- 每个卦由6个爻组成（从下到上编号1-6）
- 阳爻 (—): 实线，代表刚、动、阳
- 阴爻 (- -): 虚线，代表柔、静、阴

### 动爻与变卦

- 老阳(9)和老阴(6)为动爻，会发生变化
- 老阳变为阴爻，老阴变为阳爻
- 变化后形成的新卦称为"变卦"

### 解卦原则

1. 无动爻: 以本卦卦辞为准
2. 有动爻: 参考动爻的爻辞
3. 有变卦: 结合本卦和变卦综合分析

## 项目结构

```
src/main/java/com/yy/yao/
├── controller/          # REST控制器
│   └── DivinationController.java
├── service/            # 业务服务
│   ├── DivinationService.java
│   ├── DivinationAlgorithm.java
│   └── InterpretationService.java
├── repository/         # 数据仓库
│   └── HexagramRepository.java
├── model/             # 数据模型
│   ├── Hexagram.java
│   ├── DivinationRequest.java
│   └── DivinationResult.java
└── YyYaoApplication.java
```

## 💡 使用示例

### 完整的使用流程

```bash
# 1. 用户注册
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123",
    "email": "test@example.com"
  }'

# 2. 保存返回的 token
TOKEN="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."

# 3. 执行卜卦
curl -X POST http://localhost:8080/api/divination/consult \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "question": "近期适合换工作吗？",
    "method": "COIN",
    "needAiInterpretation": true
  }'

# 4. 查询卦象详情
curl http://localhost:8080/api/divination/hexagram/1 \
  -H "Authorization: Bearer $TOKEN"

# 5. 健康检查
curl http://localhost:8080/api/health
```

## ⚙️ 配置说明

项目支持多种配置方式,按优先级排序:

1. **环境变量** (最高优先级)
2. **application.properties 文件**
3. **默认值** (最低优先级)

### 主要配置项

```properties
# 数据库
spring.datasource.url=jdbc:mysql://localhost:3306/yy-yao
spring.datasource.username=root
spring.datasource.password=password

# JWT
jwt.secret=your-secret-key-at-least-32-characters
jwt.expiration=86400000

# LLM API
llm.api.url=https://api.openai.com/v1/chat/completions
llm.api.key=sk-your-api-key
llm.model=gpt-3.5-turbo

# 应用
app.name=易经卜卦服务
app.auth-enabled=true
app.ai-enabled=false
```

详细配置参考 [.env.example](./.env.example)

## 📝 注意事项

1. **JWT 密钥安全**:
   - 生产环境必须使用强随机密钥
   - 不要将密钥提交到版本控制
   - 定期更换密钥

2. **AI 解读成本**:
   - LLM API 调用会产生费用
   - 建议设置使用限额
   - 可以设置 `needAiInterpretation: false` 禁用

3. **数据库初始化**:
   - 首次运行会自动创建表结构
   - 卦象数据需要手动导入(待实现数据迁移脚本)

4. **CORS 配置**:
   - 当前允许所有来源访问
   - 生产环境应限制为具体域名

## 🗺️ 开发路线图

### 已完成 ✅
- [x] JWT 认证系统
- [x] 通用 LLM API 集成
- [x] 统一 API 响应格式
- [x] 全局异常处理
- [x] 健康检查和监控
- [x] 配置管理优化

### 进行中 🚧
- [ ] 统一数据存储(迁移到 JPA)
- [ ] 完整的64卦数据导入

### 计划中 📋
- [ ] 文件存储服务 (S3/本地)
- [ ] 卜卦历史记录
- [ ] 用户卦象收藏
- [ ] 批量卜卦API
- [ ] Web 管理界面
- [ ] Docker 容器化
- [ ] API 文档 (Swagger/OpenAPI)
- [ ] 单元测试和集成测试
- [ ] 性能优化 (Redis 缓存)

## 🏗️ 项目结构

```
yy-yao/
├── src/main/java/com/yy/yao/
│   ├── config/              # 配置类
│   │   ├── AppConfig.java
│   │   └── SecurityConfig.java
│   ├── controller/          # REST 控制器
│   │   ├── AuthController.java
│   │   ├── DivinationController.java
│   │   ├── ClassicTextController.java
│   │   └── SystemController.java
│   ├── service/             # 业务服务
│   │   ├── DivinationService.java
│   │   ├── DivinationAlgorithm.java
│   │   ├── InterpretationService.java
│   │   └── LlmService.java
│   ├── security/            # 安全相关
│   │   ├── JwtService.java
│   │   └── JwtAuthenticationFilter.java
│   ├── repository/          # 数据访问
│   │   ├── UserRepository.java
│   │   ├── HexagramRepository.java
│   │   └── ... (JPA Repositories)
│   ├── entity/              # JPA 实体
│   │   ├── User.java
│   │   ├── HexagramInfo.java
│   │   └── ...
│   ├── model/               # 业务模型
│   │   ├── Hexagram.java
│   │   ├── DivinationRequest.java
│   │   └── DivinationResult.java
│   ├── dto/                 # 数据传输对象
│   │   └── ApiResponse.java
│   ├── exception/           # 异常处理
│   │   ├── BusinessException.java
│   │   └── GlobalExceptionHandler.java
│   └── YyYaoApplication.java
├── src/main/resources/
│   └── application.properties
├── .env.example             # 环境变量示例
├── REFACTORING.md          # 重构说明文档
├── README.md               # 本文件
└── pom.xml                 # Maven 配置
```

## 🤝 贡献指南

欢迎贡献代码、报告问题或提出建议!

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

本项目仅供学习和研究使用。

## 📧 联系方式

- 问题反馈: [GitHub Issues](https://github.com/spring-ai-community/yy-yao/issues)
- 项目参考: [yi-jing-divination-app](https://github.com/Wadud-Ma/yi-jing-divination-app)

## 🙏 致谢

- 参考项目: [yi-jing-divination-app](https://github.com/Wadud-Ma/yi-jing-divination-app)
- Spring Boot 社区
- 易经研究社区
