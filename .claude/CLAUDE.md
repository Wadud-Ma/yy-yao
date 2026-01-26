# YY-YAO 项目开发指南

> 本文档为 Claude AI 提供项目上下文,帮助理解项目结构、开发规范和最佳实践。

---

## 📋 项目概述

**项目名称**: YY-YAO (易经卜卦服务)
**技术栈**: Spring Boot 4.0.1 + Java 21 + Spring AI + MySQL/H2
**架构模式**: 7 层分层架构 + DDD 思想
**构建工具**: Maven 3.x
**代码风格**: Lombok + Java 21 新特性

---

## 🏗️ 项目架构

### 7 层分层架构

```
┌─────────────────────────────────────────────────────────────┐
│  yy-yao-start (启动模块) - 应用启动入口                      │
│  ↓ depends on                                                │
├─────────────────────────────────────────────────────────────┤
│  yy-yao-web (Web层) - HTTP 接口层                           │
│  ↓ depends on                                                │
├─────────────────────────────────────────────────────────────┤
│  yy-yao-biz (业务编排层) - 复杂业务流程编排 🆕               │
│  ↓ depends on                                                │
├─────────────────────────────────────────────────────────────┤
│  yy-yao-service (服务层) - 基础业务逻辑                      │
│  ↓ depends on                                                │
├─────────────────────────────────────────────────────────────┤
│  yy-yao-dao (数据访问层) - 数据持久化                        │
│  ↓ depends on                                                │
├─────────────────────────────────────────────────────────────┤
│  yy-yao-model (模型层) - 数据模型                            │
│  ↓ depends on                                                │
├─────────────────────────────────────────────────────────────┤
│  yy-yao-core (核心层) - 基础设施                             │
│  (无内部依赖)                                                 │
└─────────────────────────────────────────────────────────────┘
```

### 依赖原则

✅ **允许的依赖方向**:

- 只能依赖下层模块 (如 web → biz → service)
- 通过依赖传递访问更底层的模块

❌ **禁止的依赖**:

- 跨层依赖 (如 web → dao,跳过 biz 和 service)
- 反向依赖 (如 dao → service)
- 循环依赖 (如 A → B → A)

---

## 📁 目录结构

```
yy-yao/
├── 📄 pom.xml                          # 父 POM
├── 📁 docs/                            # 项目文档
├── 📁 yy-yao-core/                     # [1] 核心基础层
│   └── src/main/java/com/yy/yao/
│       ├── config/                     # 全局配置
│       │   ├── AppConfig.java
│       │   ├── JacksonConfig.java
│       │   └── WebConfig.java
│       └── security/                   # 安全基础设施
│           ├── JwtService.java
│           └── JwtAuthenticationFilter.java
│
├── 📁 yy-yao-model/                    # [2] 模型层
│   └── src/main/java/com/yy/yao/
│       ├── model/                      # 业务模型
│       │   ├── Hexagram.java
│       │   ├── DivinationRequest.java
│       │   ├── DivinationResult.java
│       │   └── DivinationMethod.java
│       ├── dto/                        # 数据传输对象
│       │   ├── ApiResponse.java
│       │   ├── DivinationResponse.java
│       │   ├── HexagramWithLinesDTO.java
│       │   └── MiniProgram*.java
│       └── exception/                  # 异常定义
│           └── BusinessException.java
│
├── 📁 yy-yao-dao/                      # [3] 数据访问层
│   └── src/main/java/com/yy/yao/
│       ├── entity/                     # JPA 实体
│       │   ├── Hexagram.java
│       │   ├── HexagramLine.java
│       │   ├── DivinationRecord.java
│       │   ├── User.java
│       │   └── ...
│       ├── repository/                 # Spring Data 仓库
│       │   ├── HexagramRepository.java
│       │   ├── UserRepository.java
│       │   └── ...
│       └── config/                     # 数据层配置
│           └── SecurityConfig.java     # Spring Security 配置
│
├── 📁 yy-yao-service/                  # [4] 服务层
│   └── src/main/java/com/yy/yao/
│       ├── service/                    # 业务服务
│       │   ├── DivinationService.java      # 占卜核心服务
│       │   ├── HexagramService.java        # 卦象数据服务
│       │   ├── InterpretationService.java  # 解卦服务
│       │   ├── DivinationRecordService.java # 记录服务
│       │   ├── LlmService.java             # LLM 服务
│       │   ├── DivinationAlgorithm.java    # 算法实现
│       │   └── HexagramDataInitService.java
│       └── mapper/                     # 数据映射
│           ├── HexagramMapper.java     # Entity ↔ Model
│           └── MiniProgramMapper.java  # Model → 小程序 DTO
│
├── 📁 yy-yao-biz/                      # [5] 业务编排层 🆕
│   └── src/main/java/com/yy/yao/biz/
│       ├── DivinationBizService.java   # 占卜业务编排
│       └── package-info.java
│
├── 📁 yy-yao-web/                      # [6] Web 层
│   └── src/main/java/com/yy/yao/
│       ├── controller/                 # HTTP 控制器
│       │   ├── DivinationController.java
│       │   ├── MiniProgramController.java
│       │   ├── AuthController.java
│       │   ├── LlmController.java
│       │   └── SystemController.java
│       └── exception/                  # 异常处理
│           └── GlobalExceptionHandler.java
│
└── 📁 yy-yao-start/                    # [7] 启动模块
    ├── src/main/java/com/yy/yao/
    │   └── YyYaoApplication.java       # 启动类
    └── src/main/resources/
        ├── application.yml             # 主配置
        ├── application-dev.yml         # 开发环境
        └── application-prod.yml        # 生产环境
```

---

## 🎯 各层职责定义

### 1. yy-yao-core (核心基础层)

**职责**: 提供项目的基础设施和通用工具

**包含内容**:

- ✅ 全局配置 (JacksonConfig, WebConfig, AppConfig)
- ✅ 安全基础设施 (JwtService, JwtAuthenticationFilter)
- ✅ 通用工具类

**依赖**: 无内部依赖

**原则**:

- ✓ 不依赖任何业务模块
- ✓ 只包含基础设施代码
- ✗ 不包含业务逻辑

---

### 2. yy-yao-model (模型层)

**职责**: 定义数据传输对象和业务模型

**包含内容**:

- ✅ 业务领域模型 (model/)
- ✅ 数据传输对象 (dto/)
- ✅ 枚举和常量
- ✅ 基础异常类 (BusinessException)

**依赖**: yy-yao-core

**原则**:

- ✓ 纯数据对象,无业务逻辑
- ✓ 使用 Lombok 简化代码
- ✗ 不包含持久化注解 (JPA @Entity)

---

### 3. yy-yao-dao (数据访问层)

**职责**: 封装数据库访问逻辑

**包含内容**:

- ✅ JPA 实体 (entity/)
- ✅ Spring Data Repository (repository/)
- ✅ Spring Security 配置 (SecurityConfig.java)

**依赖**: yy-yao-model

**原则**:

- ✓ 只负责数据持久化
- ✓ 使用 JPA/Hibernate
- ✗ 不包含业务逻辑

**特殊说明**:

- SecurityConfig 放在这里是因为需要注入 UserRepository

---

### 4. yy-yao-service (服务层)

**职责**: 实现单一职责的基础业务逻辑

**包含内容**:

- ✅ 基础业务服务 (service/)
- ✅ 数据映射器 (mapper/)
- ✅ 算法实现 (DivinationAlgorithm)

**依赖**: yy-yao-dao

**原则**:

- ✓ 单一职责,每个 Service 只负责一个业务域
- ✓ 方法粒度细,易于复用
- ✗ 不处理 HTTP 请求
- ✗ 不编排复杂业务流程

**示例**:

```java
@Service
@RequiredArgsConstructor
public class DivinationService {
    private final HexagramRepository hexagramRepository;

    // 单一职责: 只负责占卜逻辑
    public DivinationResult performDivination(DivinationRequest request) {
        // 执行占卜
    }
}
```

---

### 5. yy-yao-biz (业务编排层) 🆕

**职责**: 组合多个 Service 实现复杂业务流程

**包含内容**:

- ✅ 业务编排服务 (*BizService)

**依赖**: yy-yao-service

**原则**:

- ✓ 编排多个 Service 完成复杂业务
- ✓ 处理事务边界
- ✓ 实现业务补偿逻辑
- ✗ 不直接访问 DAO
- ✗ 不处理 HTTP 请求

**使用场景**:

```java
// ❌ 错误: Controller 直接调用多个 Service
@RestController
public class DivinationController {
    @Autowired DivinationService divinationService;
    @Autowired InterpretationService interpretationService;
    @Autowired DivinationRecordService recordService;

    public DivinationResponse divination(Request req) {
        var result = divinationService.divination(req);
        var interpretation = interpretationService.interpret(result);
        recordService.save(result);
        return ...;
    }
}

// ✅ 正确: Controller 调用 Biz 层
@RestController
public class DivinationController {
    @Autowired DivinationBizService divinationBiz;

    public DivinationResponse divination(Request req) {
        return divinationBiz.performCompleteDivination(req);
    }
}
```

---

### 6. yy-yao-web (Web层)

**职责**: 处理 HTTP 请求,转换数据格式

**包含内容**:

- ✅ HTTP 控制器 (controller/)
- ✅ 全局异常处理 (GlobalExceptionHandler)

**依赖**: yy-yao-biz (+ 临时依赖 yy-yao-service,待重构后移除)

**原则**:

- ✓ 只处理 HTTP 层逻辑
- ✓ 参数校验、格式转换
- ✓ 调用 Biz 层完成业务
- ✗ 不包含业务逻辑
- ✗ 不定义 DTO (DTO 应在 model 层)

**注意事项**:

```java
// ❌ 错误: Controller 内嵌 DTO
@RestController
public class AuthController {
    @Data
    public static class LoginRequest { ... }  // ✗ 应该在 model 层
}

// ✅ 正确: 引用 model 层的 DTO
@RestController
public class AuthController {
    public ApiResponse<AuthResponse> login(LoginRequest req) {
        // LoginRequest 和 AuthResponse 都定义在 yy-yao-model
    }
}
```

---

### 7. yy-yao-start (启动模块)

**职责**: 应用启动入口

**包含内容**:

- ✅ Spring Boot 启动类
- ✅ 应用配置文件

**依赖**: yy-yao-web

**原则**:

- ✓ 只包含启动类和配置
- ✗ 不包含业务代码

---

## 📝 编码规范

### Java 代码规范

#### 1. 使用 Java 21 新特性

**Record (记录类)**:

```java
// ✅ 推荐: 用于简单的数据载体
public record UserInfo(String username, String email, LocalDateTime createdAt) {}
```

**Switch 表达式**:

```java
// ✅ 推荐: 使用增强的 switch 表达式
return switch (method) {
    case COIN -> algorithm.coinMethod();
    case YARROW -> algorithm.yarrowMethod();
    case NUMBER, MEIHUA -> algorithm.numberMethod();
    case TIME, DAILY -> algorithm.timeMethod();
    default -> throw new IllegalArgumentException("Unknown method: " + method);
};

// ❌ 避免: 传统的 switch 语句
switch (method) {
    case COIN:
        return algorithm.coinMethod();
    case YARROW:
        return algorithm.yarrowMethod();
    // ...
}
```

**模式匹配**:

```java
// ✅ 推荐: instanceof 模式匹配
if (obj instanceof String str) {
    return str.toUpperCase();
}

// ❌ 避免: 传统的类型转换
if (obj instanceof String) {
    String str = (String) obj;
    return str.toUpperCase();
}
```

**文本块 (Text Blocks)**:

```java
// ✅ 推荐: 使用文本块
String sql = """
    SELECT h.id, h.name, h.symbol
    FROM hexagram h
    WHERE h.number = ?
    ORDER BY h.id
    """;
```

#### 2. Lombok 使用规范

**推荐的注解**:

```java
// ✅ 实体类/DTO
@Data                    // getter/setter/toString/equals/hashCode
@Builder                 // 构建器模式
@NoArgsConstructor       // 无参构造
@AllArgsConstructor      // 全参构造

// ✅ Service/Controller
@Slf4j                   // 日志
@RequiredArgsConstructor // 必需参数构造(final 字段)

// 示例
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DivinationResponse {
    private LocalDateTime timestamp;
    private String question;
    private Hexagram originalHexagram;
    private Hexagram changedHexagram;
}

@Slf4j
@Service
@RequiredArgsConstructor
public class DivinationService {
    private final DivinationAlgorithm algorithm;
    private final HexagramRepository hexagramRepository;

    public DivinationResult performDivination(DivinationRequest request) {
        log.info("开始卜卦: {}", request.getQuestion());
        // ...
    }
}
```

**避免的注解**:

```java
// ❌ 避免: @Value (容易引起混淆)
@Value  // ← 这个注解使类不可变,容易误用

// ✅ 推荐: 明确使用组合
@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ImmutableDTO { ... }
```

#### 3. 命名规范

**类命名**:

```java
// Controller
DivinationController, MiniProgramController

// Service
DivinationService, HexagramService

// BizService (业务编排)
DivinationBizService, UserBizService

// Repository
HexagramRepository, UserRepository

// Mapper
HexagramMapper, MiniProgramMapper

// DTO
ApiResponse, DivinationResponse, MiniProgramHexagramDTO

// Entity
Hexagram, User, DivinationRecord
```

**方法命名**:

```java
// ✅ 推荐: 动词开头,清晰表达意图
performDivination()      // 执行占卜
generateInterpretation() // 生成解读
saveDivinationRecord()   // 保存记录
findByHexagramId()       // 查询

// ❌ 避免: 含糊不清的命名
doSomething()
process()
handle()
```

**变量命名**:

```java
// ✅ 推荐: 见名知意
DivinationRequest request
DivinationResult result
List<Hexagram> hexagrams
Optional<User> userOptional

// ❌ 避免: 单字母或缩写
DivinationRequest req  // 太短
DivinationRequest divinationRequestParameter  // 太长
```

#### 4. 注释规范

**类注释**:

```java
/**
 * 占卜业务编排服务
 * 负责组合多个 Service 完成完整的占卜业务流程
 *
 * 职责:
 * 1. 组合 DivinationService 执行占卜
 * 2. 保存占卜记录到数据库
 * 3. 处理事务边界
 * 4. 实现业务补偿逻辑
 *
 * @author Claude
 * @since 0.0.1
 */
@Service
@RequiredArgsConstructor
public class DivinationBizService { ... }
```

**方法注释**:

```java
/**
 * 执行完整的占卜流程
 * 包括: 占卦 → 保存记录
 *
 * @param userId 用户ID (可选,为null时不保存记录)
 * @param request 占卜请求
 * @return 占卜结果
 * @throws BusinessException 当占卜失败时抛出
 */
@Transactional
public DivinationResult performCompleteDivination(Long userId, DivinationRequest request) {
    // ...
}
```

**行内注释**:

```java
// ✅ 推荐: 解释"为什么",而不是"做什么"
// 转换变爻位置(后端是1-6,小程序是0-5)
dto.setChangingLines(
    response.getChangingLines().stream()
        .map(line -> line - 1)
        .collect(Collectors.toList())
);

// ❌ 避免: 重复代码的注释
// 设置变爻
dto.setChangingLines(...);  // 这个注释没有提供额外信息
```

---

## 🔧 开发规范

### 1. 新增功能开发流程

#### Step 1: 确定功能层次

- 是否需要新的业务流程? → 在 **biz** 层创建 BizService
- 是否需要新的基础服务? → 在 **service** 层创建 Service
- 是否需要新的 HTTP 接口? → 在 **web** 层创建 Controller
- 是否需要新的数据表? → 在 **dao** 层创建 Entity 和 Repository
- 是否需要新的 DTO? → 在 **model** 层创建 DTO

#### Step 2: 自底向上开发

```
1. Model 层: 定义 DTO/Model
   ↓
2. Dao 层: 创建 Entity 和 Repository
   ↓
3. Service 层: 实现基础业务逻辑
   ↓
4. Biz 层: 编排复杂业务流程
   ↓
5. Web 层: 创建 Controller 暴露 HTTP 接口
```

#### Step 3: 示例 - 新增"收藏卦象"功能

```java
// 1. Model 层: 定义 DTO
@Data
@Builder
public class FavoriteHexagramRequest {
    @NotNull
    private Long userId;
    @NotNull
    private Integer hexagramId;
}

// 2. Dao 层: 创建 Entity
@Entity
@Data
public class FavoriteHexagram {
    @Id
    @GeneratedValue
    private Long id;
    private Long userId;
    private Integer hexagramId;
    private LocalDateTime createdAt;
}

// 3. Dao 层: 创建 Repository
public interface FavoriteHexagramRepository extends JpaRepository<FavoriteHexagram, Long> {
    List<FavoriteHexagram> findByUserId(Long userId);
    boolean existsByUserIdAndHexagramId(Long userId, Integer hexagramId);
}

// 4. Service 层: 创建基础服务
@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteHexagramRepository favoriteRepository;

    public void addFavorite(Long userId, Integer hexagramId) {
        // 基础业务逻辑: 添加收藏
    }

    public List<FavoriteHexagram> getFavorites(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }
}

// 5. Biz 层: 业务编排 (如果需要组合多个 Service)
@Service
@RequiredArgsConstructor
public class HexagramBizService {
    private final FavoriteService favoriteService;
    private final HexagramService hexagramService;

    @Transactional
    public HexagramWithFavoriteStatus getHexagramWithFavorite(Long userId, Integer hexagramId) {
        var hexagram = hexagramService.getHexagramByNumber(hexagramId);
        var favorites = favoriteService.getFavorites(userId);
        var isFavorite = favorites.stream()
            .anyMatch(f -> f.getHexagramId().equals(hexagramId));
        return new HexagramWithFavoriteStatus(hexagram, isFavorite);
    }
}

// 6. Web 层: 创建 Controller
@RestController
@RequestMapping("/api/favorite")
@RequiredArgsConstructor
public class FavoriteController {
    private final HexagramBizService hexagramBiz;

    @PostMapping
    public ApiResponse<Void> addFavorite(@RequestBody FavoriteHexagramRequest request) {
        hexagramBiz.addFavorite(request.getUserId(), request.getHexagramId());
        return ApiResponse.success();
    }
}
```

### 2. 修改现有功能规范

#### 原则: 最小化修改范围

- ✅ 只修改直接相关的类
- ✅ 保持现有接口兼容性
- ✗ 不进行不必要的重构
- ✗ 不修改无关代码

#### 示例: 修改占卜服务

```java
// ✅ 正确: 只修改 DivinationService
@Service
public class DivinationService {
    public DivinationResult performDivination(DivinationRequest request) {
        // 原有逻辑
        var result = generateResult(request);

        // 新增: 添加统计信息
        result.setStatistics(calculateStatistics(result));

        return result;
    }

    // 新增私有方法
    private Statistics calculateStatistics(DivinationResult result) {
        // 统计逻辑
    }
}

// ❌ 错误: 同时修改多个不相关的类
// 不要在修改 DivinationService 的同时"顺便"重构 HexagramService
```

### 3. 数据库操作规范

#### 使用 Spring Data JPA

```java
// ✅ 推荐: 使用方法命名查询
public interface HexagramRepository extends JpaRepository<Hexagram, Integer> {
    Optional<Hexagram> findByNumber(int number);
    Optional<Hexagram> findByBinary(String binary);
    List<Hexagram> findByUpperTrigramIdAndLowerTrigramId(Integer upper, Integer lower);
}

// ✅ 推荐: 使用 @Query 处理复杂查询
@Query("SELECT h FROM Hexagram h WHERE h.upperTrigramId = :trigramId OR h.lowerTrigramId = :trigramId")
List<Hexagram> findByTrigramId(@Param("trigramId") Integer trigramId);

// ❌ 避免: N+1 查询问题
// 使用 @EntityGraph 或 JOIN FETCH 优化
@EntityGraph(attributePaths = {"lines", "trigrams"})
Optional<Hexagram> findByIdWithLines(Integer id);
```

#### 事务管理

```java
// ✅ 推荐: 在 Service 或 Biz 层使用 @Transactional
@Service
@Transactional  // 类级别: 所有方法都是事务性的
public class DivinationRecordService {

    @Transactional(readOnly = true)  // 只读事务
    public List<DivinationRecord> findUserRecords(Long userId) {
        return repository.findByUserId(userId);
    }

    @Transactional  // 读写事务
    public void saveDivinationRecord(Long userId, DivinationResult result) {
        var record = new DivinationRecord();
        // ...
        repository.save(record);
    }
}

// ❌ 避免: 在 Controller 层使用 @Transactional
@RestController
public class DivinationController {
    @Transactional  // ✗ 不应该在 Controller 使用事务
    public ApiResponse<DivinationResponse> divination(...) {
        // ...
    }
}
```

### 4. 异常处理规范

#### 自定义业务异常

```java
// ✅ 推荐: 继承 BusinessException
public class HexagramNotFoundException extends BusinessException {
    public HexagramNotFoundException(Integer number) {
        super("未找到卦象: " + number);
    }
}

// ✅ 推荐: 在 Service 层抛出异常
@Service
public class HexagramService {
    public Hexagram getHexagramByNumber(int number) {
        return hexagramRepository.findByNumber(number)
            .orElseThrow(() -> new HexagramNotFoundException(number));
    }
}

// ✅ 推荐: 在 GlobalExceptionHandler 统一处理
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HexagramNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleHexagramNotFound(HexagramNotFoundException ex) {
        log.warn("卦象未找到: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.error(404, ex.getMessage()));
    }
}
```

### 5. 日志规范

```java
@Slf4j
@Service
public class DivinationService {

    public DivinationResult performDivination(DivinationRequest request) {
        // ✅ 推荐: info 记录业务关键节点
        log.info("开始卜卦 - 问题: {}, 方法: {}", request.getQuestion(), request.getMethod());

        try {
            var result = doPerformDivination(request);

            // ✅ 推荐: debug 记录详细信息
            log.debug("占卜完成 - 本卦: {}, 变卦: {}",
                result.getOriginalHexagram().getName(),
                result.getChangedHexagram() != null ? result.getChangedHexagram().getName() : "无");

            return result;

        } catch (Exception e) {
            // ✅ 推荐: error 记录异常堆栈
            log.error("占卜失败 - 问题: {}", request.getQuestion(), e);
            throw new BusinessException("占卜失败: " + e.getMessage(), e);
        }
    }
}

// ❌ 避免: 日志级别使用不当
log.info("变量 x 的值为: {}", x);  // ✗ 应该用 debug
log.error("用户未登录");  // ✗ 应该用 warn
```

### 6. API 设计规范

#### RESTful API 规范

```java
// ✅ 推荐: 遵循 REST 规范
@RestController
@RequestMapping("/api/hexagrams")
public class HexagramController {

    // GET /api/hexagrams - 获取列表
    @GetMapping
    public ApiResponse<List<HexagramDTO>> getAllHexagrams() { }

    // GET /api/hexagrams/{id} - 获取单个
    @GetMapping("/{id}")
    public ApiResponse<HexagramDTO> getHexagram(@PathVariable Integer id) { }

    // POST /api/hexagrams - 创建 (如果支持)
    @PostMapping
    public ApiResponse<HexagramDTO> createHexagram(@RequestBody @Valid CreateHexagramRequest request) { }

    // PUT /api/hexagrams/{id} - 更新 (如果支持)
    @PutMapping("/{id}")
    public ApiResponse<HexagramDTO> updateHexagram(@PathVariable Integer id, @RequestBody @Valid UpdateHexagramRequest request) { }

    // DELETE /api/hexagrams/{id} - 删除 (如果支持)
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteHexagram(@PathVariable Integer id) { }
}

// ✅ 推荐: 使用统一的响应格式
@Data
@Builder
public class ApiResponse<T> {
    private int code;       // 业务状态码
    private String message; // 提示信息
    private T data;         // 数据载体
    private Long timestamp; // 时间戳

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
            .code(200)
            .message("success")
            .data(data)
            .timestamp(System.currentTimeMillis())
            .build();
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        return ApiResponse.<T>builder()
            .code(code)
            .message(message)
            .timestamp(System.currentTimeMillis())
            .build();
    }
}
```

#### 参数校验

```java
// ✅ 推荐: 使用 Bean Validation
@Data
public class DivinationRequest {
    @NotBlank(message = "问题不能为空")
    @Size(max = 200, message = "问题长度不能超过200字符")
    private String question;

    @NotNull(message = "占卜方法不能为空")
    private DivinationMethod method;

    @Size(min = 6, max = 6, message = "自定义卦象必须是6爻")
    private List<Integer> customLines;
}

// ✅ 推荐: Controller 使用 @Valid
@PostMapping("/api/divination")
public ApiResponse<DivinationResponse> performDivination(
        @RequestBody @Valid DivinationRequest request) {
    // Spring 会自动校验,失败时由 GlobalExceptionHandler 处理
}
```

---

## 🛠️ 常用命令

### Maven 命令

```bash
# 编译项目
./mvnw clean compile

# 跳过测试编译
./mvnw clean compile -DskipTests

# 运行项目
./mvnw spring-boot:run -pl yy-yao-start

# 打包项目
./mvnw clean package -DskipTests

# 安装到本地仓库
./mvnw clean install -DskipTests

# 依赖树分析
./mvnw dependency:tree

# 更新依赖
./mvnw versions:display-dependency-updates
```

### Git 规范

#### Commit Message 格式

```
<type>(<scope>): <subject>

<body>

<footer>
```

**Type 类型**:

- `feat`: 新功能
- `fix`: Bug 修复
- `docs`: 文档更新
- `style`: 代码格式(不影响功能)
- `refactor`: 重构(不是新功能也不是修复)
- `perf`: 性能优化
- `test`: 测试相关
- `chore`: 构建过程或辅助工具变动

**示例**:

```
feat(divination): 添加时间起卦法支持

- 实现时间起卦算法
- 添加 TimeMethod 枚举值
- 更新 DivinationAlgorithm.timeMethod()

Closes #123
```

---

## 📊 性能优化建议

### 1. 数据库查询优化

```java
// ❌ 避免: N+1 查询
List<Hexagram> hexagrams = hexagramRepository.findAll();
for (Hexagram hexagram : hexagrams) {
    List<HexagramLine> lines = lineRepository.findByHexagramId(hexagram.getId());  // N次查询
    hexagram.setLines(lines);
}

// ✅ 推荐: 使用 JOIN FETCH 或 @EntityGraph
@Query("SELECT h FROM Hexagram h LEFT JOIN FETCH h.lines")
List<Hexagram> findAllWithLines();

// 或
@EntityGraph(attributePaths = {"lines"})
List<Hexagram> findAll();
```

### 2. 缓存使用

```java
// ✅ 推荐: 缓存不常变化的数据
@Service
@CacheConfig(cacheNames = "hexagrams")
public class HexagramService {

    @Cacheable(key = "#number")
    public Hexagram getHexagramByNumber(int number) {
        return hexagramRepository.findByNumber(number)
            .orElseThrow(() -> new HexagramNotFoundException(number));
    }

    @CacheEvict(allEntries = true)
    public void refreshCache() {
        // 刷新缓存
    }
}
```

### 3. 异步处理

```java
// ✅ 推荐: 耗时操作使用异步
@Service
public class InterpretationService {

    @Async
    public CompletableFuture<String> generateAiInterpretationAsync(String question, Hexagram hexagram) {
        String interpretation = llmService.generateInterpretation(question, hexagram);
        return CompletableFuture.completedFuture(interpretation);
    }
}
```

---

## 🔒 安全规范

### 1. JWT 认证

```java
// JWT 配置已在 SecurityConfig 中设置
// 需要认证的端点会自动检查 JWT Token

// ✅ 在 Controller 获取当前用户
@RestController
public class UserController {
    @GetMapping("/api/user/profile")
    public ApiResponse<UserProfile> getProfile(@AuthenticationPrincipal User user) {
        // user 是当前登录用户
        return ApiResponse.success(userService.getProfile(user.getId()));
    }
}
```

### 2. 参数校验

```java
// ✅ 推荐: 使用 Bean Validation
// ✅ 推荐: 在 Service 层也进行业务校验
@Service
public class DivinationService {
    public DivinationResult performDivination(DivinationRequest request) {
        // 业务规则校验
        if (request.getMethod() == DivinationMethod.MANUAL &&
            (request.getCustomLines() == null || request.getCustomLines().size() != 6)) {
            throw new BusinessException("手动起卦需要提供6爻数据");
        }
        // ...
    }
}
```

### 3. SQL 注入防护

```java
// ✅ 推荐: 使用参数化查询
@Query("SELECT h FROM Hexagram h WHERE h.name LIKE %:keyword%")
List<Hexagram> searchByName(@Param("keyword") String keyword);

// ❌ 避免: 字符串拼接 SQL
@Query(value = "SELECT * FROM hexagram WHERE name LIKE '%" + keyword + "%'", nativeQuery = true)  // 危险!
List<Hexagram> searchByName(String keyword);
```

---

## 📚 相关文档

- [架构设计文档](../docs/ARCHITECTURE.md) - 详细的架构说明
- [模块结构说明](../docs/MODULE_STRUCTURE.md) - 模块总览
- [POM 重构总结](../docs/POM_REFACTOR_SUMMARY.md) - 依赖管理说明
- [Spring Boot 文档](https://spring.io/projects/spring-boot)
- [Spring AI 文档](https://docs.spring.io/spring-ai/reference/)

---

## 🤝 贡献指南

### 开发流程

1. **理解需求** - 确认要实现的功能
2. **设计方案** - 确定在哪一层实现,如何与现有代码集成
3. **编写代码** - 遵循本文档的编码规范
4. **测试验证** - 编写单元测试和集成测试
5. **提交代码** - 遵循 Git Commit 规范

### Code Review 检查项

- [ ]  代码是否放在正确的模块和包中?
- [ ]  依赖关系是否符合分层架构?
- [ ]  是否使用了 Java 21 新特性?
- [ ]  是否正确使用了 Lombok?
- [ ]  命名是否清晰规范?
- [ ]  是否有必要的注释?
- [ ]  是否有适当的异常处理?
- [ ]  是否有适当的日志记录?
- [ ]  API 设计是否符合 REST 规范?
- [ ]  是否进行了参数校验?

---

## ❓ 常见问题

### Q: 为什么 SecurityConfig 在 dao 层而不在 core 层?

**A**: SecurityConfig 需要注入 UserRepository 来加载用户信息,而 UserRepository 在 dao 层。如果放在 core 层会导致 core → dao 的依赖,违反了"core 无内部依赖"的原则。

### Q: 为什么 GlobalExceptionHandler 在 web 层而不在 core 层?

**A**: GlobalExceptionHandler 需要访问所有的 DTO (如 ApiResponse),而这些 DTO 定义在 model 层。如果放在 core 层,会导致 core → model 的依赖,同样违反架构原则。

### Q: Controller 可以直接调用 Service 吗?

**A**: 可以,但不推荐。推荐的做法是 Controller 调用 Biz 层,由 Biz 层编排多个 Service。对于简单的 CRUD 操作,可以临时直接调用 Service,但长期应该重构为调用 Biz 层。

### Q: 什么时候应该创建新的模块?

**A**: 目前的 7 层架构已经足够清晰,一般不需要新增模块。如果确实需要(如增加独立的报表模块、定时任务模块),应该先讨论架构设计,确保不破坏现有的分层结构。

### Q: 如何处理跨模块的工具类?

**A**: 通用工具类应放在 yy-yao-core 模块。如果工具类依赖特定的业务模型,应该放在对应的模块中(如 HexagramMapper 在 service 层)。

---

**文档版本**: v2.0
**最后更新**: 2026-01-26
**维护者**: Claude AI

---

> 💡 **提示**: 本文档会随项目演进持续更新。如有疑问或建议,请及时反馈。
