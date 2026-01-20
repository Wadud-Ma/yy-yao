# 项目重构总结

## 重构目标

参考 `yi-jing-divination-app` 项目的优秀实践,对 `yy-yao` 项目进行渐进式重构,保留 Spring Boot 框架,引入现代化的后端接口能力。

## 完成的改进

### 1. ✅ 修复 AI 集成

**问题**: Spring AI 1.0.0-M6 与 Spring Boot 4.0.1 版本不兼容,导致 AI 功能完全禁用。

**解决方案**:
- 移除 Spring AI 依赖
- 创建通用的 `LlmService` 类,使用纯 HTTP 调用
- 支持 OpenAI 兼容的所有 API (GPT, Claude, 通义千问等)
- 配置灵活,支持环境变量

**新增文件**:
- `src/main/java/com/yy/yao/service/LlmService.java`

**配置示例**:
```properties
llm.api.url=${LLM_API_URL:https://api.openai.com/v1/chat/completions}
llm.api.key=${LLM_API_KEY:}
llm.model=${LLM_MODEL:gpt-3.5-turbo}
```

**使用方式**:
```java
// 简单对话
String response = llmService.chat("你是易经专家", "请解释乾卦");

// 多轮对话
List<ChatMessage> messages = List.of(
    new ChatMessage("system", "你是易经专家"),
    new ChatMessage("user", "请解释乾卦")
);
String response = llmService.chat(messages);
```

---

### 2. ✅ 添加认证系统 (JWT-based)

**问题**: 原项目没有认证系统,所有 API 都是公开的。

**解决方案**:
- 实现基于 JWT 的认证系统
- 添加用户注册、登录功能
- 支持角色管理 (USER, ADMIN)
- 使用 Spring Security 保护 API 端点

**新增文件**:
- `src/main/java/com/yy/yao/security/JwtService.java` - JWT 令牌服务
- `src/main/java/com/yy/yao/security/JwtAuthenticationFilter.java` - JWT 认证过滤器
- `src/main/java/com/yy/yao/entity/User.java` - 用户实体
- `src/main/java/com/yy/yao/repository/UserRepository.java` - 用户仓储
- `src/main/java/com/yy/yao/controller/AuthController.java` - 认证控制器
- `src/main/java/com/yy/yao/config/SecurityConfig.java` - Security 配置

**API 端点**:
```
POST /api/auth/register  - 用户注册
POST /api/auth/login     - 用户登录
GET  /api/auth/me        - 获取当前用户
```

**认证流程**:
1. 用户注册/登录 → 返回 JWT token
2. 后续请求携带 `Authorization: Bearer <token>` 头部
3. 服务器验证 token 并提取用户信息
4. 根据用户角色判断权限

**配置**:
```properties
jwt.secret=${JWT_SECRET:your-secret-key-must-be-at-least-32-characters-long}
jwt.expiration=86400000  # 24小时
```

---

### 3. ✅ 改进 API 响应格式和错误处理

**问题**: 原项目直接返回数据或使用 `ResponseEntity.internalServerError()`,缺乏统一的响应格式。

**解决方案**:
- 创建统一的 `ApiResponse<T>` 响应包装类
- 实现全局异常处理器 `GlobalExceptionHandler`
- 规范化错误码和错误消息

**新增文件**:
- `src/main/java/com/yy/yao/dto/ApiResponse.java` - 统一响应格式
- `src/main/java/com/yy/yao/exception/GlobalExceptionHandler.java` - 全局异常处理
- `src/main/java/com/yy/yao/exception/BusinessException.java` - 业务异常

**响应格式**:
```json
{
  "code": 200,
  "message": "成功",
  "data": { ... },
  "timestamp": "2026-01-19T10:30:00"
}
```

**错误响应**:
```json
{
  "code": 400,
  "message": "请求参数验证失败",
  "timestamp": "2026-01-19T10:30:00"
}
```

**使用方式**:
```java
// 成功响应
return ResponseEntity.ok(ApiResponse.success(data));

// 错误响应
throw new BusinessException("用户不存在");
```

---

### 4. ✅ 添加系统健康检查和监控端点

**问题**: 无法实时监控系统状态和依赖服务健康。

**解决方案**:
- 集成 Spring Boot Actuator
- 创建自定义健康检查端点
- 提供系统信息和统计数据

**新增文件**:
- `src/main/java/com/yy/yao/controller/SystemController.java` - 系统监控控制器

**API 端点**:
```
GET /api/health          - 健康检查 (检查数据库、LLM 等依赖)
GET /api/system/info     - 系统信息 (版本、Java 版本等)
GET /api/system/stats    - 系统统计 (内存、CPU 等)
GET /actuator/health     - Actuator 健康端点
GET /actuator/metrics    - Actuator 指标端点
```

**健康检查响应**:
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
  }
}
```

---

### 5. ✅ 编写配置管理和环境变量支持

**问题**: 配置分散在各处,缺乏统一管理。

**解决方案**:
- 创建 `AppConfig` 类统一管理应用配置
- 支持环境变量覆盖
- 添加详细的配置说明

**新增文件**:
- `src/main/java/com/yy/yao/config/AppConfig.java` - 应用配置类

**配置项**:
```properties
# 应用配置
app.name=易经卜卦服务
app.version=1.0.0
app.auth-enabled=true
app.ai-enabled=false
app.max-divinations-per-day=10

# JWT 配置
jwt.secret=${JWT_SECRET}
jwt.expiration=86400000

# LLM 配置
llm.api.url=${LLM_API_URL}
llm.api.key=${LLM_API_KEY}
llm.model=${LLM_MODEL:gpt-3.5-turbo}

# 数据库配置
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

---

## 依赖更新

### 新增依赖

```xml
<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- JWT -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.6</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.6</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.6</version>
    <scope>runtime</scope>
</dependency>

<!-- Actuator -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

<!-- Configuration Processor -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <optional>true</optional>
</dependency>
```

### 移除依赖

```xml
<!-- Spring AI (不兼容) -->
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-openai-spring-boot-starter</artifactId>
    <version>1.0.0-M6</version>
</dependency>
```

---

## 待完成的改进

### 🔲 统一数据存储

**问题**:
- 当前使用内存 `HexagramRepository` 存储卦象数据
- 数据库中有 `HexagramInfo` 等 JPA 实体
- 两套数据系统不同步

**解决方案**:
1. 创建数据迁移脚本,将内存数据导入数据库
2. 修改 `DivinationService` 使用 JPA Repository
3. 删除内存 `HexagramRepository`
4. 统一使用 `HexagramInfo` 等实体

**预计工作量**: 2-3小时

---

### 🔲 添加文件存储服务

**问题**: 缺少文件存储能力 (如卜卦记录导出、用户头像等)

**解决方案**:
1. 创建 `StorageService` 接口
2. 实现本地文件存储 `LocalStorageService`
3. (可选) 实现 S3 存储 `S3StorageService`
4. 添加文件上传/下载 API

**预计工作量**: 3-4小时

---

## 使用指南

### 启动项目

1. **配置数据库**:
   ```bash
   # 确保 MySQL 运行中
   # 或修改 application.properties 使用其他数据库
   ```

2. **配置环境变量** (可选):
   ```bash
   export JWT_SECRET="your-random-secret-key-at-least-32-chars"
   export LLM_API_KEY="your-openai-api-key"
   export LLM_API_URL="https://api.openai.com/v1/chat/completions"
   ```

3. **构建并运行**:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **访问服务**:
   ```
   服务地址: http://localhost:8080
   健康检查: http://localhost:8080/api/health
   ```

### API 测试

#### 1. 用户注册
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123",
    "email": "test@example.com"
  }'
```

#### 2. 用户登录
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

响应:
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "username": "testuser",
    "role": "USER"
  }
}
```

#### 3. 卜卦 (需要认证)
```bash
TOKEN="your-jwt-token-here"

curl -X POST http://localhost:8080/api/divination/consult \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "question": "今年事业运势如何？",
    "method": "COIN",
    "needAiInterpretation": true
  }'
```

#### 4. 健康检查
```bash
curl http://localhost:8080/api/health
```

---

## 架构改进对比

### 改进前
```
客户端请求
    ↓
Spring Controller (无认证)
    ↓
Service (AI功能禁用)
    ↓
内存Repository / JPA Repository (混合)
    ↓
响应 (格式不统一)
```

### 改进后
```
客户端请求
    ↓
JWT 认证过滤器
    ↓
Spring Security 授权检查
    ↓
Controller (统一异常处理)
    ↓
Service (LLM集成可用)
    ↓
JPA Repository (待统一)
    ↓
统一 ApiResponse 响应
```

---

## 技术亮点

1. **现代化认证**: JWT + Spring Security,支持无状态认证
2. **灵活的 AI 集成**: 支持任何 OpenAI 兼容的 LLM API
3. **统一的响应格式**: 便于前端处理
4. **全局异常处理**: 规范化错误响应
5. **健康检查**: 便于监控和运维
6. **配置管理**: 支持环境变量,适合容器化部署

---

## 参考项目对比

| 功能 | yi-jing-divination-app | yy-yao (重构后) |
|------|------------------------|-----------------|
| 后端框架 | Express + tRPC | Spring Boot |
| 认证系统 | OAuth 2.0 (Manus) | JWT |
| AI 集成 | LLM API (Gemini) | LLM API (通用) |
| 数据库 | Drizzle ORM (MySQL/TiDB) | JPA (MySQL) |
| 文件存储 | S3 (Forge API) | 待实现 |
| 类型安全 | TypeScript 端到端 | Java 类型安全 |
| API 风格 | tRPC | REST |

---

## 后续优化建议

1. **性能优化**:
   - 添加 Redis 缓存卦象数据
   - 实现卜卦记录的异步存储
   - 添加 LLM 响应缓存

2. **功能增强**:
   - 实现卜卦历史记录
   - 添加卦象收藏功能
   - 支持多语言解读

3. **运维改进**:
   - 添加 Docker 支持
   - 编写 Kubernetes 部署配置
   - 集成日志聚合 (ELK)

4. **测试完善**:
   - 添加单元测试
   - 添加集成测试
   - 添加 API 测试

---

## 总结

本次重构成功引入了 `yi-jing-divination-app` 项目的优秀实践:
- ✅ 修复了 AI 集成问题
- ✅ 添加了完整的认证系统
- ✅ 规范化了 API 响应格式
- ✅ 增强了系统监控能力
- ✅ 改善了配置管理

项目现在具备了现代化后端服务的基础能力,为后续功能扩展打下了坚实基础。
