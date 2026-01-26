# YY-YAO 包结构规范

## 📦 包前缀

**统一包前缀**: `com.yy.yao`

所有模块都使用统一的包前缀 `com.yy.yao`,通过子包名称区分不同的职责域。

---

## 📂 完整包结构

```
com.yy.yao                                    # 根包
│
├── 📁 config/                                # 配置类
│   ├── AppConfig.java                        # [core] 应用配置
│   ├── JacksonConfig.java                    # [core] JSON配置
│   ├── WebConfig.java                        # [core] Web配置
│   └── SecurityConfig.java                   # [dao] 安全配置
│
├── 📁 security/                              # 安全基础设施
│   ├── JwtService.java                       # [core] JWT服务
│   └── JwtAuthenticationFilter.java          # [core] JWT过滤器
│
├── 📁 exception/                             # 异常定义
│   ├── BusinessException.java                # [model] 业务异常基类
│   └── GlobalExceptionHandler.java           # [web] 全局异常处理
│
├── 📁 model/                                 # 业务模型
│   ├── Hexagram.java                         # [model] 卦象模型
│   ├── DivinationRequest.java                # [model] 占卜请求
│   ├── DivinationResult.java                 # [model] 占卜结果
│   └── DivinationMethod.java                 # [model] 起卦方法枚举
│
├── 📁 dto/                                   # 数据传输对象
│   ├── ApiResponse.java                      # [model] 通用响应
│   ├── DivinationResponse.java               # [model] 占卜响应
│   ├── HexagramWithLinesDTO.java             # [model] 卦象详情DTO
│   ├── MiniProgramHexagramDTO.java           # [model] 小程序卦象DTO
│   └── MiniProgramDivinationResponse.java    # [model] 小程序占卜响应
│
├── 📁 entity/                                # JPA实体
│   ├── Hexagram.java                         # [dao] 卦象表
│   ├── HexagramLine.java                     # [dao] 爻辞表
│   ├── DivinationRecord.java                 # [dao] 占卜记录表
│   ├── User.java                             # [dao] 用户表
│   ├── Trigram.java                          # [dao] 八卦表
│   ├── Shuogua.java                          # [dao] 说卦传
│   ├── Xugua.java                            # [dao] 序卦传
│   ├── Xici.java                             # [dao] 系辞传
│   ├── Wenyan.java                           # [dao] 文言传
│   └── Zagua.java                            # [dao] 杂卦传
│
├── 📁 repository/                            # 持久层仓库
│   ├── HexagramRepository.java               # [dao] 卦象仓库
│   ├── HexagramLineRepository.java           # [dao] 爻辞仓库
│   ├── DivinationRecordRepository.java       # [dao] 占卜记录仓库
│   ├── UserRepository.java                   # [dao] 用户仓库
│   ├── TrigramRepository.java                # [dao] 八卦仓库
│   ├── ShuoguaRepository.java                # [dao]
│   ├── XuguaRepository.java                  # [dao]
│   ├── XiciRepository.java                   # [dao]
│   ├── WenyanRepository.java                 # [dao]
│   └── ZaguaRepository.java                  # [dao]
│
├── 📁 service/                               # 业务服务
│   ├── DivinationService.java                # [service] 占卜核心服务
│   ├── HexagramService.java                  # [service] 卦象数据服务
│   ├── InterpretationService.java            # [service] 解卦服务
│   ├── DivinationRecordService.java          # [service] 占卜记录服务
│   ├── LlmService.java                       # [service] 大语言模型服务
│   ├── DivinationAlgorithm.java              # [service] 起卦算法
│   └── HexagramDataInitService.java          # [service] 数据初始化
│
├── 📁 mapper/                                # 数据映射
│   ├── HexagramMapper.java                   # [service] Entity↔Model映射
│   └── MiniProgramMapper.java                # [service] Model→小程序DTO映射
│
├── 📁 biz/                                   # 业务编排
│   ├── DivinationBizService.java             # [biz] 占卜业务编排
│   ├── package-info.java                     # [biz] 包说明
│   └── (其他 BizService 待创建)
│
├── 📁 controller/                            # HTTP控制器
│   ├── DivinationController.java             # [web] 占卜API
│   ├── MiniProgramController.java            # [web] 小程序API
│   ├── AuthController.java                   # [web] 认证API
│   ├── LlmController.java                    # [web] AI对话API
│   ├── StorageController.java                # [web] 存储API
│   └── SystemController.java                 # [web] 系统监控API
│
└── YyYaoApplication.java                     # [start] 启动类
```

---

## 🎯 包命名规范

### 1. 基础原则

- ✅ **统一前缀**: 所有包都使用 `com.yy.yao` 前缀
- ✅ **功能分组**: 通过子包名区分功能域
- ✅ **小写命名**: 包名全部使用小写字母
- ✅ **简洁明确**: 包名应简洁且能清晰表达用途

### 2. 包名与模块对应关系

| 包名 | 模块 | 用途 |
|------|------|------|
| `com.yy.yao.config` | core, dao | 配置类 |
| `com.yy.yao.security` | core | 安全基础设施 |
| `com.yy.yao.exception` | model, web | 异常定义和处理 |
| `com.yy.yao.model` | model | 业务模型 |
| `com.yy.yao.dto` | model | 数据传输对象 |
| `com.yy.yao.entity` | dao | JPA实体 |
| `com.yy.yao.repository` | dao | 持久层仓库 |
| `com.yy.yao.service` | service | 业务服务 |
| `com.yy.yao.mapper` | service | 数据映射 |
| `com.yy.yao.biz` | biz | 业务编排 |
| `com.yy.yao.controller` | web | HTTP控制器 |
| `com.yy.yao` | start | 启动类 |

### 3. 特殊说明

#### 同名包在不同模块中

某些包名会出现在多个模块中,这是**正常且合理的**:

**`com.yy.yao.config`**:
- `yy-yao-core/config/` - 全局配置 (AppConfig, JacksonConfig, WebConfig)
- `yy-yao-dao/config/` - 安全配置 (SecurityConfig)
  - 原因: SecurityConfig 需要注入 UserRepository

**`com.yy.yao.exception`**:
- `yy-yao-model/exception/` - 异常定义 (BusinessException)
- `yy-yao-web/exception/` - 异常处理 (GlobalExceptionHandler)
  - 原因: GlobalExceptionHandler 需要访问所有 DTO

---

## 📝 新增类的包选择指南

### 决策树

```
需要新增类?
  │
  ├─ 配置类?
  │  ├─ 全局配置 → com.yy.yao.config (在 core 中)
  │  └─ 需要注入DAO层 → com.yy.yao.config (在 dao 中)
  │
  ├─ DTO/模型?
  │  ├─ 业务模型 → com.yy.yao.model (在 model 中)
  │  ├─ 数据传输对象 → com.yy.yao.dto (在 model 中)
  │  └─ 枚举/常量 → com.yy.yao.model (在 model 中)
  │
  ├─ 数据库相关?
  │  ├─ JPA实体 → com.yy.yao.entity (在 dao 中)
  │  └─ Repository → com.yy.yao.repository (在 dao 中)
  │
  ├─ 业务逻辑?
  │  ├─ 基础服务 → com.yy.yao.service (在 service 中)
  │  ├─ 复杂业务流程 → com.yy.yao.biz (在 biz 中)
  │  └─ 数据映射 → com.yy.yao.mapper (在 service 中)
  │
  ├─ HTTP接口?
  │  └─ Controller → com.yy.yao.controller (在 web 中)
  │
  └─ 异常?
     ├─ 自定义异常 → com.yy.yao.exception (在 model 中)
     └─ 异常处理器 → com.yy.yao.exception (在 web 中)
```

---

## 🔍 包依赖关系

### 依赖规则

```
com.yy.yao (start)
  ↓ depends on
com.yy.yao.controller (web)
  ↓ depends on
com.yy.yao.biz (biz)
  ↓ depends on
com.yy.yao.service (service)
  ↓ depends on
com.yy.yao.repository (dao)
  ↓ depends on
com.yy.yao.model (model)
  ↓ depends on
com.yy.yao.config (core)
```

### 允许的导入

✅ **正确示例**:

```java
// Controller 可以导入 biz, service, model, dto
package com.yy.yao.controller;

import com.yy.yao.biz.DivinationBizService;
import com.yy.yao.dto.ApiResponse;
import com.yy.yao.dto.DivinationResponse;
import com.yy.yao.model.DivinationRequest;

// Service 可以导入 repository, entity, model
package com.yy.yao.service;

import com.yy.yao.repository.HexagramRepository;
import com.yy.yao.entity.Hexagram;
import com.yy.yao.model.Hexagram;
```

❌ **错误示例**:

```java
// ✗ Model 不能导入 service
package com.yy.yao.model;

import com.yy.yao.service.DivinationService;  // ✗ 违反分层

// ✗ Dao 不能导入 service
package com.yy.yao.repository;

import com.yy.yao.service.HexagramService;  // ✗ 违反分层

// ✗ Controller 不能直接导入 repository
package com.yy.yao.controller;

import com.yy.yao.repository.HexagramRepository;  // ✗ 跨层依赖
```

---

## 📊 包结构统计

| 包名 | 类数量(估算) | 所属模块 | 说明 |
|------|-------------|----------|------|
| com.yy.yao | 1 | start | 启动类 |
| com.yy.yao.config | 4 | core, dao | 配置类 |
| com.yy.yao.security | 2 | core | 安全基础设施 |
| com.yy.yao.exception | 2 | model, web | 异常 |
| com.yy.yao.model | 4 | model | 业务模型 |
| com.yy.yao.dto | 7 | model | DTO |
| com.yy.yao.entity | 10 | dao | JPA实体 |
| com.yy.yao.repository | 10 | dao | Repository |
| com.yy.yao.service | 7 | service | 业务服务 |
| com.yy.yao.mapper | 2 | service | 数据映射 |
| com.yy.yao.biz | 2 | biz | 业务编排 |
| com.yy.yao.controller | 6 | web | HTTP控制器 |
| **总计** | **57** | - | - |

---

## 🛠️ Spring Boot 组件扫描

### 默认扫描配置

由于所有类都在 `com.yy.yao` 包下,Spring Boot 会自动扫描所有组件。

**启动类**:
```java
package com.yy.yao;

@SpringBootApplication  // 自动扫描 com.yy.yao 及其子包
public class YyYaoApplication {
    public static void main(String[] args) {
        SpringApplication.run(YyYaoApplication.class, args);
    }
}
```

### 自定义扫描(如果需要)

```java
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.yy.yao.config",
    "com.yy.yao.controller",
    "com.yy.yao.service",
    "com.yy.yao.biz"
})
@EntityScan("com.yy.yao.entity")
@EnableJpaRepositories("com.yy.yao.repository")
public class YyYaoApplication { }
```

---

## ✅ 验证规范

### 检查包前缀

```bash
# 检查是否所有文件都使用正确的包前缀
grep -r "^package" --include="*.java" yy-yao-*/src/main/java | grep -v "com.yy.yao"
```

如果没有输出,说明所有文件都正确使用了 `com.yy.yao` 前缀。

### 检查跨层依赖

```bash
# 检查 model 层是否导入了 service
grep -r "import com.yy.yao.service" yy-yao-model/src/main/java

# 检查 dao 层是否导入了 service
grep -r "import com.yy.yao.service" yy-yao-dao/src/main/java

# 检查 controller 是否直接导入 repository
grep -r "import com.yy.yao.repository" yy-yao-web/src/main/java
```

---

## 📚 最佳实践

### 1. 保持包的单一职责

每个包应该有明确的职责:
- `config` - 只放配置类
- `controller` - 只放 HTTP 控制器
- `service` - 只放业务服务
- `entity` - 只放 JPA 实体

### 2. 避免循环依赖

包之间不应该有循环依赖:
- ✗ A → B → A
- ✓ A → B → C

### 3. 合理使用子包

当某个包的类过多时,可以创建子包:
```
com.yy.yao.service/
├── divination/          # 占卜相关服务
├── interpretation/      # 解读相关服务
└── storage/            # 存储相关服务
```

### 4. 包名与文件夹一致

包名应该与文件夹结构完全对应:
```
src/main/java/com/yy/yao/service/DivinationService.java
                  ↓
package com.yy.yao.service;
```

---

## 🔗 相关文档

- [架构设计文档](./ARCHITECTURE.md)
- [模块结构说明](./MODULE_STRUCTURE.md)
- [开发指南](./.claude.md)

---

**文档版本**: v1.0
**最后更新**: 2026-01-26
**维护者**: YY-YAO Team
