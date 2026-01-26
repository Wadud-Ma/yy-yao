# YY-YAO 项目开发指南

## 项目概述

- **项目**: YY-YAO (易经卜卦服务)
- **技术栈**: Spring Boot 4.0.1 + Java 21 + Spring AI + MySQL/H2
- **构建**: Maven 3.x
- **代码风格**: Lombok + Java 21 新特性

---

## 7 层分层架构

```
yy-yao-start   → 启动入口
    ↓
yy-yao-web     → HTTP 接口 (Controller, GlobalExceptionHandler)
    ↓
yy-yao-biz     → 业务编排 (*BizService，组合多个 Service)
    ↓
yy-yao-service → 基础业务 (Service, Mapper, Algorithm)
    ↓
yy-yao-dao     → 数据访问 (Entity, Repository, SecurityConfig)
    ↓
yy-yao-model   → 数据模型 (DTO, Model, Exception)
    ↓
yy-yao-core    → 基础设施 (Config, Security, 工具类)
```

### 依赖原则

- ✅ 只能依赖下层模块
- ❌ 禁止跨层依赖 (如 web → dao)
- ❌ 禁止反向依赖 (如 dao → service)
- ❌ 禁止循环依赖

---

## 编码规范

### Java 21 新特性 (优先使用)

```java
// Record
public record UserInfo(String username, String email) {}

// Switch 表达式
return switch (method) {
    case COIN -> algorithm.coinMethod();
    case YARROW -> algorithm.yarrowMethod();
    default -> throw new IllegalArgumentException();
};

// 模式匹配
if (obj instanceof String str) {
    return str.toUpperCase();
}

// 文本块
String sql = """
    SELECT * FROM hexagram WHERE number = ?
    """;
```

### Lombok 使用

```java
// 实体/DTO
@Data @Builder @NoArgsConstructor @AllArgsConstructor

// Service/Controller
@Slf4j @RequiredArgsConstructor
```

### 命名规范

| 类型 | 后缀 | 示例 |
|------|------|------|
| Controller | Controller | DivinationController |
| 业务编排 | BizService | DivinationBizService |
| 基础服务 | Service | HexagramService |
| 仓库 | Repository | HexagramRepository |
| 映射 | Mapper | HexagramMapper |
| 实体 | 无 | Hexagram, User |
| DTO | DTO/Request/Response | ApiResponse, DivinationRequest |

---

## 开发流程

### 新功能开发 (自底向上)

1. **Model** → 定义 DTO
2. **Dao** → Entity + Repository
3. **Service** → 基础业务逻辑
4. **Biz** → 编排复杂流程
5. **Web** → Controller

### 关键原则

- Controller 调用 BizService，不要直接调用多个 Service
- Service 单一职责，方法粒度细
- 事务 `@Transactional` 放在 Service/Biz 层，不要放 Controller
- DTO 定义在 model 层，不要内嵌在 Controller

---

## API 规范

```java
// 统一响应格式
ApiResponse.success(data)
ApiResponse.error(code, message)

// 参数校验
@NotBlank @NotNull @Size @Valid
```

---

## 常用命令

```bash
# 编译
./mvnw clean compile -DskipTests

# 运行
./mvnw spring-boot:run -pl yy-yao-start

# 打包
./mvnw clean package -DskipTests
```

---

## Git Commit 格式

```
<type>(<scope>): <subject>

type: feat|fix|docs|style|refactor|perf|test|chore
```

---

## 注意事项

- SecurityConfig 在 dao 层 (因为需要 UserRepository)
- GlobalExceptionHandler 在 web 层
- 通用工具类放 core 层
- 日志: info=业务节点, debug=详细信息, error=异常