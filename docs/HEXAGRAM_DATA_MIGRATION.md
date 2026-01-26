# 卦象数据迁移指南

本文档说明如何将前端 TypeScript 中的卦象数据迁移到后端数据库。

## 📋 目录

- [概述](#概述)
- [数据结构映射](#数据结构映射)
- [迁移步骤](#迁移步骤)
- [数据验证](#数据验证)
- [常见问题](#常见问题)

---

## 🎯 概述

### 迁移目标

将前端项目 `yi-app/lib/hexagrams.ts` 中硬编码的 64 卦数据迁移到后端 MySQL 数据库,实现:

- ✅ 数据集中管理
- ✅ 便于扩展和维护
- ✅ 支持多语言版本
- ✅ 支持动态更新
- ✅ 前后端数据一致性

### 涉及的数据表

| 表名 | 说明 | 记录数 |
|------|------|--------|
| `hexagram_info` | 卦象基础信息 | 64 条 |
| `line_text` | 爻辞信息 | 384 条 (64卦 × 6爻) |
| `hexagram_text` | 卦象详细文本 (可选扩展) | 64 条 |
| `trigram_info` | 八卦基础信息 (已有) | 8 条 |

---

## 📊 数据结构映射

### 前端 TypeScript 接口

```typescript
interface Hexagram {
  id: number;                  // 卦序号 1-64
  name: string;                // 卦名 (乾、坤...)
  symbol: string;              // 二进制表示 "111111"
  upperTrigram: string;        // 上卦
  lowerTrigram: string;        // 下卦
  meaning: string;             // 卦辞及释义
  lineTexts: string[];         // 6个爻辞 (从下到上)
  symbolism: string;           // 象征意义
  lifeLesson: string;          // 人生启示
  advice: string;              // 建议指导
}
```

### 后端数据库表

#### hexagram_info 表

| 字段 | 类型 | 说明 | 映射来源 |
|------|------|------|----------|
| `id` | BIGINT | 主键 (自增) | - |
| `number` | INT | 卦序号 | `id` |
| `name` | VARCHAR(50) | 卦名 | `name` |
| `symbol` | VARCHAR(10) | Unicode 符号 | 根据 `symbol` 生成 |
| `unicode_symbol` | VARCHAR(10) | 单个八卦符号 | 根据上卦生成 |
| `binary_code` | VARCHAR(6) | 二进制表示 | `symbol` |
| `upper_trigram` | VARCHAR(10) | 上卦 | `upperTrigram` |
| `lower_trigram` | VARCHAR(10) | 下卦 | `lowerTrigram` |
| `trigram_nature` | VARCHAR(100) | 卦象属性 | 自动生成 |
| `full_name` | VARCHAR(100) | 完整卦名 | 自动生成 |
| `statement` | TEXT | 卦辞原文 | 从 `meaning` 提取 |
| `interpretation` | TEXT | 卦辞解释 | `meaning` |
| `symbolism` | TEXT | 象征意义 | `symbolism` |
| `advice` | TEXT | 建议 | `advice` |
| `meaning` | TEXT | 简短释义 | `lifeLesson` |

#### line_text 表

| 字段 | 类型 | 说明 | 映射来源 |
|------|------|------|----------|
| `id` | BIGINT | 主键 (自增) | - |
| `hexagram_id` | BIGINT | 关联卦象 ID | 根据卦序号查询 |
| `position` | INT | 爻位 (1-6) | 数组索引 + 1 |
| `line_name` | VARCHAR(20) | 爻名 | 从爻辞提取 |
| `is_yang` | BOOLEAN | 是否阳爻 | 根据 `symbol` 判断 |
| `original_text` | TEXT | 爻辞原文 | `lineTexts[i]` |

---

## 🚀 迁移步骤

### 步骤 1: 从前端导出 JSON 数据

```bash
# 切换到前端项目目录
cd /Users/juvenile/code/github.com/Wadud-Ma/yi-app

# 运行导出脚本
npx tsx export-hexagrams.ts
```

**输出文件**: `hexagrams.json`

**验证**: 文件应包含 64 个卦象对象,每个卦象有 6 个爻辞。

### 步骤 2: 生成 SQL 初始化脚本

```bash
# 切换到后端项目目录
cd /Users/juvenile/code/github.com/spring-ai-community/yy-yao

# 运行 Python 脚本生成 SQL
python3 scripts/generate-hexagram-sql.py
```

**输出文件**: `docs/hexagrams-full-data-init.sql`

**内容包括**:
- 64 条 `INSERT INTO hexagram_info` 语句
- 384 条 `INSERT INTO line_text` 语句
- 使用 `ON DUPLICATE KEY UPDATE` 避免重复

### 步骤 3: 执行 SQL 脚本初始化数据库

#### 方式 1: MySQL 命令行

```bash
mysql -u root -p yy_yao < docs/hexagrams-full-data-init.sql
```

#### 方式 2: MySQL Workbench

1. 打开 MySQL Workbench
2. 连接到数据库
3. File → Open SQL Script → 选择 `hexagrams-full-data-init.sql`
4. 点击执行 (⚡ 图标)

#### 方式 3: Spring Boot 自动执行

在 `application.yml` 中配置:

```yaml
spring:
  sql:
    init:
      mode: always  # 或 never
      data-locations: classpath:data.sql
```

然后将 SQL 文件复制到 `src/main/resources/data.sql`

### 步骤 4: 验证数据

启动 Spring Boot 应用,查看日志:

```log
============================================================
检查卦象数据初始化状态...
============================================================
📊 当前数据状态:
   - 卦象数量: 64 / 64
   - 爻辞数量: 384 / 384 (64卦 × 6爻)
✅ 数据完整,系统就绪!

📚 示例数据 - 第1卦:
   卦名: 乾
   卦符: ☰☰ (111111)
   卦辞: 元亨利贞...
============================================================
```

### 步骤 5: 测试 API

```bash
# 获取所有卦象
curl http://localhost:8080/api/divination/hexagrams

# 获取单个卦象 (包含爻辞)
curl http://localhost:8080/api/divination/hexagram/1

# 根据二进制查询
curl http://localhost:8080/api/divination/hexagram/binary/111111
```

---

## ✅ 数据验证

### 验证 SQL

```sql
-- 检查卦象数量
SELECT COUNT(*) FROM hexagram_info;  -- 应该返回 64

-- 检查爻辞数量
SELECT COUNT(*) FROM line_text;      -- 应该返回 384

-- 检查每个卦象的爻辞数量
SELECT hexagram_id, COUNT(*) as line_count
FROM line_text
GROUP BY hexagram_id
HAVING line_count != 6;             -- 应该返回空结果

-- 检查数据完整性
SELECT
    h.id,
    h.number,
    h.name,
    h.binary_code,
    COUNT(l.id) as line_count
FROM hexagram_info h
LEFT JOIN line_text l ON h.id = l.hexagram_id
GROUP BY h.id, h.number, h.name, h.binary_code
ORDER BY h.number;

-- 验证阴阳爻
SELECT
    hexagram_id,
    position,
    is_yang,
    line_name
FROM line_text
WHERE hexagram_id = 1  -- 乾卦,应该全是阳爻
ORDER BY position;
```

### 验证 API

使用 Spring Boot 提供的接口:

```http
GET /api/system/health
GET /api/divination/hexagrams
GET /api/divination/hexagram/{number}
```

---

## 🛠 常见问题

### Q1: SQL 脚本执行失败 - 外键约束错误

**原因**: `line_text` 表依赖 `hexagram_info` 表的主键。

**解决方案**:
1. 确保先创建表结构 (运行 `database-schema.sql`)
2. 再执行数据初始化脚本
3. 或者临时禁用外键检查:

```sql
SET FOREIGN_KEY_CHECKS=0;
-- 执行 SQL 脚本
SET FOREIGN_KEY_CHECKS=1;
```

### Q2: 字符编码问题 - 卦象符号显示乱码

**原因**: 数据库或表的字符集不是 UTF-8。

**解决方案**:

```sql
-- 修改数据库字符集
ALTER DATABASE yy_yao CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 修改表字符集
ALTER TABLE hexagram_info CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE line_text CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### Q3: 数据重复插入

**原因**: 多次运行初始化脚本。

**解决方案**: SQL 脚本已使用 `ON DUPLICATE KEY UPDATE`,多次执行不会产生重复数据,只会更新现有记录。

如需完全重新初始化:

```sql
-- 清空数据
TRUNCATE TABLE line_text;       -- 先删除子表数据
TRUNCATE TABLE hexagram_info;   -- 再删除主表数据

-- 重新执行初始化脚本
SOURCE docs/hexagrams-full-data-init.sql;
```

### Q4: 前端如何调用新的 API

**修改前**: 直接使用本地 `hexagrams.ts`

```typescript
import { hexagrams, getHexagramById } from './lib/hexagrams';

const hex = getHexagramById(1);
```

**修改后**: 调用后端 API

```typescript
// 获取所有卦象
const response = await fetch(`${API_BASE_URL}/api/divination/hexagrams`);
const hexagrams = await response.json();

// 获取单个卦象
const response = await fetch(`${API_BASE_URL}/api/divination/hexagram/1`);
const hexagram = await response.json();
```

### Q5: 如何扩展数据 (添加多语言版本)

在 `hexagram_text` 表中添加翻译:

```sql
-- 添加英文版本字段
ALTER TABLE hexagram_info
ADD COLUMN name_en VARCHAR(50) COMMENT '英文卦名',
ADD COLUMN interpretation_en TEXT COMMENT '英文解释';

-- 更新数据
UPDATE hexagram_info SET name_en = 'Qian', interpretation_en = 'The Creative' WHERE number = 1;
```

---

## 📝 数据维护

### 更新卦象数据

1. 修改前端 `hexagrams.ts` 文件
2. 重新导出 JSON: `npx tsx export-hexagrams.ts`
3. 重新生成 SQL: `python3 scripts/generate-hexagram-sql.py`
4. 执行 SQL 脚本更新数据库

### 备份数据

```bash
# 导出数据
mysqldump -u root -p yy_yao hexagram_info line_text > hexagram_backup.sql

# 恢复数据
mysql -u root -p yy_yao < hexagram_backup.sql
```

---

## 📚 相关文件

### 前端项目

- `lib/hexagrams.ts` - 原始数据源
- `export-hexagrams.ts` - 数据导出脚本
- `hexagrams.json` - 导出的 JSON 数据

### 后端项目

- `scripts/generate-hexagram-sql.py` - SQL 生成脚本
- `docs/hexagrams-full-data-init.sql` - 完整数据初始化脚本
- `docs/database-schema.sql` - 数据库表结构
- `yy-yao-service/src/main/java/com/yy/yao/service/HexagramDataInitService.java` - 数据验证服务

### 数据库实体

- `yy-yao-dao/src/main/java/com/yy/yao/entity/HexagramInfo.java`
- `yy-yao-dao/src/main/java/com/yy/yao/entity/LineText.java`
- `yy-yao-dao/src/main/java/com/yy/yao/entity/HexagramText.java`

---

## ✨ 后续优化建议

1. **增加爻辞详细解释**: 扩展 `line_text` 表,添加译文、注释等字段
2. **多版本支持**: 添加繁体中文、英文等版本
3. **历史版本管理**: 记录数据变更历史
4. **API 缓存**: 使用 Redis 缓存热门卦象数据
5. **全文搜索**: 为卦辞和爻辞添加全文索引,支持关键词搜索

---

## 📞 联系方式

如有问题,请查阅:
- 项目 README: `/README.md`
- API 文档: `/docs/API.md`
- 数据库设计: `/docs/database-schema.sql`

---

**最后更新**: 2026-01-26
**脚本版本**: v1.0
**兼容性**: MySQL 5.7+, Spring Boot 3.x
