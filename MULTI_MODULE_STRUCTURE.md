# YY-YAO 多模块项目结构

## 📦 模块划分

本项目采用Maven多模块架构，参考了codelink-background的设计模式，将项目划分为6个独立模块。

```
yy-yao (parent)
├── yy-yao-model        # 数据模型层
├── yy-yao-core         # 核心工具层
├── yy-yao-dao          # 数据访问层
├── yy-yao-service      # 业务逻辑层
├── yy-yao-web          # Web控制层
└── yy-yao-start        # 启动模块
```

## 🏗️ 模块说明

### 1. yy-yao-model
**职责**: 数据模型定义
- **包含内容**:
  - `model/` - 领域模型 (DivinationRequest, DivinationResult, Hexagram等)
  - `dto/` - 数据传输对象 (ApiResponse, MiniProgramDivinationResponse等)
- **依赖**: 无内部模块依赖
- **外部依赖**:
  - spring-boot-starter-validation
  - jackson-databind

### 2. yy-yao-core
**职责**: 核心基础设施
- **包含内容**:
  - `config/` - 配置类 (AppConfig, SecurityConfig, WebConfig)
  - `security/` - 安全组件 (JwtTokenProvider, JwtAuthenticationFilter)
  - `util/` - 工具类 (各种Mapper, Util)
  - `exception/` - 异常定义
- **依赖**: yy-yao-model
- **外部依赖**:
  - spring-boot-starter
  - spring-boot-starter-security
  - jjwt (JWT处理)

### 3. yy-yao-dao
**职责**: 数据访问层
- **包含内容**:
  - `entity/` - JPA实体 (User, MiniProgramUser, DivinationRecord等)
  - `repository/` - Spring Data JPA Repository接口
- **依赖**: yy-yao-model
- **外部依赖**:
  - spring-boot-starter-data-jpa
  - mysql-connector-j
  - h2 (测试)
  - postgresql (可选)

### 4. yy-yao-service
**职责**: 业务逻辑处理
- **包含内容**:
  - `service/` - 业务服务类 (DivinationService, HexagramService, LlmService等)
- **依赖**:
  - yy-yao-dao
  - yy-yao-core
- **外部依赖**:
  - spring-boot-starter
  - spring-ai-starter-model-openai (AI功能)
  - spring-boot-starter-mail (邮件)
  - poi-ooxml (Excel处理)

### 5. yy-yao-web
**职责**: Web接口层
- **包含内容**:
  - `controller/` - REST API控制器 (MiniProgramController, DivinationController等)
  - `filter/` - 过滤器
  - `interceptor/` - 拦截器
- **依赖**: yy-yao-service
- **外部依赖**:
  - spring-boot-starter-web
  - spring-boot-starter-validation
  - spring-boot-starter-actuator

### 6. yy-yao-start
**职责**: 应用启动
- **包含内容**:
  - `YyYaoApplication.java` - Spring Boot启动类
  - `resources/` - 配置文件 (application.yml, 数据库脚本等)
  - `test/` - 集成测试
- **依赖**: yy-yao-web (自动传递所有依赖)
- **特点**:
  - 唯一可执行的模块
  - 包含@SpringBootApplication入口
  - 打包为可执行JAR

## 📊 依赖关系图

```
┌─────────────┐
│ yy-yao-start│  (启动模块)
└──────┬──────┘
       │
       ▼
┌─────────────┐
│ yy-yao-web  │  (Web层)
└──────┬──────┘
       │
       ▼
┌─────────────┐
│yy-yao-service│ (业务层)
└──┬─────────┬─┘
   │         │
   ▼         ▼
┌──────┐  ┌──────┐
│dao   │  │core  │ (数据层/核心层)
└──┬───┘  └───┬──┘
   │          │
   └────┬─────┘
        ▼
   ┌────────┐
   │ model  │  (模型层)
   └────────┘
```

## 🔨 构建命令

### 编译整个项目
```bash
mvn clean install
```

### 编译特定模块
```bash
mvn clean install -pl yy-yao-service -am
```

### 跳过测试构建
```bash
mvn clean install -DskipTests
```

### 运行应用
```bash
cd yy-yao-start
mvn spring-boot:run
```

或直接运行打包后的JAR:
```bash
java -jar yy-yao-start/target/yy-yao-start-0.0.1-SNAPSHOT.jar
```

## 🎯 设计原则

1. **单一职责**: 每个模块只负责特定的功能领域
2. **依赖隔离**: 低层模块不依赖高层模块
3. **可测试性**: 每个模块可以独立测试
4. **可扩展性**: 新功能可以在对应模块中添加，不影响其他模块
5. **清晰边界**: 模块间通过明确的接口通信

## 📝 开发规范

### 添加新功能时
1. **模型定义** → yy-yao-model
2. **数据库实体** → yy-yao-dao/entity
3. **Repository** → yy-yao-dao/repository
4. **业务逻辑** → yy-yao-service
5. **API接口** → yy-yao-web/controller
6. **配置/工具** → yy-yao-core

### 模块间通信
- 只能依赖更底层的模块
- 通过接口而非实现类依赖
- 避免循环依赖

## 🔄 迁移说明

本项目从单模块重构为多模块架构，原有代码已按如下规则迁移：

- `src/main/java/com/yy/yao/model/**` → `yy-yao-model`
- `src/main/java/com/yy/yao/dto/**` → `yy-yao-model`
- `src/main/java/com/yy/yao/entity/**` → `yy-yao-dao`
- `src/main/java/com/yy/yao/repository/**` → `yy-yao-dao`
- `src/main/java/com/yy/yao/config/**` → `yy-yao-core`
- `src/main/java/com/yy/yao/security/**` → `yy-yao-core`
- `src/main/java/com/yy/yao/util/**` → `yy-yao-core`
- `src/main/java/com/yy/yao/service/**` → `yy-yao-service`
- `src/main/java/com/yy/yao/controller/**` → `yy-yao-web`
- `src/main/java/com/yy/yao/YyYaoApplication.java` → `yy-yao-start`
- `src/main/resources/**` → `yy-yao-start/src/main/resources`

## 🚀 优势

1. **代码组织更清晰**: 职责明确，易于维护
2. **编译速度更快**: 只需编译修改的模块
3. **团队协作更好**: 不同模块可以并行开发
4. **依赖管理更简单**: 父POM统一管理版本
5. **测试更便捷**: 可以针对单个模块进行单元测试
6. **发布更灵活**: 可以独立发布某些模块(如SDK)

## 📚 参考资料

- 参考项目: [codelink-background](https://github.com/spring-ai-community/codelink-background)
- Spring Boot多模块最佳实践
- Maven多模块项目指南
