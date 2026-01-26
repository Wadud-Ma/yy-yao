# 易经数据库完整版设计指南

> **版本**: 3.0 Complete Edition
> **日期**: 2026-01-26
> **包含内容**: 经文 + 十翼(传) + 用户占卜记录

---

## 📋 目录

1. [概述](#概述)
2. [表结构设计](#表结构设计)
3. [数据库关系图](#数据库关系图)
4. [快速开始](#快速开始)
5. [API 接口设计](#api-接口设计)
6. [实体类说明](#实体类说明)
7. [数据导入](#数据导入)
8. [占卜记录管理](#占卜记录管理)

---

## 概述

### 设计理念

本数据库设计涵盖《易经》的完整内容:
- **经文**: 八卦、六十四卦、爻辞
- **十翼**: 彖传、象传、文言传、系辞传、序卦传、说卦传、杂卦传
- **应用**: 用户占卜记录、AI 解读

### 核心优势

✅ **完整**: 包含易经全部内容，不仅是卦辞，还有十翼传文
✅ **规范化**: 符合数据库设计范式，关系清晰
✅ **可扩展**: 支持多语言、多版本注释扩展
✅ **现代化**: 支持 AI 解读、用户反馈、数据统计

---

## 表结构设计

### 1. trigrams (八卦表)

**数据量**: 8 条

```sql
CREATE TABLE trigrams (
    id TINYINT UNSIGNED PRIMARY KEY,
    symbol CHAR(3) NOT NULL COMMENT '如 111',
    name VARCHAR(4) NOT NULL COMMENT '乾坤震巽坎离艮兑',
    unicode_symbol CHAR(1) NOT NULL COMMENT '☰☷☳☴☵☲☶☱',
    nature VARCHAR(10) COMMENT '天地雷风水火山泽',
    attribute VARCHAR(20) COMMENT '刚健、柔顺等',
    -- ... 更多字段
);
```

**核心字段**:
- `symbol`: 三爻二进制，如 `111` 表示乾卦
- `nature`: 象征属性（天、地、雷等）
- `attribute`: 性质（刚健、柔顺等）

---

### 2. hexagrams (六十四卦主表)

**数据量**: 64 条

```sql
CREATE TABLE hexagrams (
    id TINYINT UNSIGNED PRIMARY KEY COMMENT '卦序 1-64',
    symbol CHAR(6) NOT NULL COMMENT '六爻符号',
    name VARCHAR(4) NOT NULL COMMENT '卦名',
    upper_trigram_id TINYINT UNSIGNED NOT NULL,
    lower_trigram_id TINYINT UNSIGNED NOT NULL,

    -- 卦辞
    judgment TEXT NOT NULL COMMENT '卦辞原文',
    judgment_annotation TEXT COMMENT '卦辞注释',
    judgment_translation TEXT COMMENT '卦辞译文',

    -- 彖传
    tuan_text TEXT COMMENT '彖传原文',
    tuan_annotation TEXT COMMENT '彖传注释',
    tuan_translation TEXT COMMENT '彖传译文',

    -- 大象
    image_text TEXT COMMENT '大象原文',
    image_annotation TEXT COMMENT '大象注释',
    image_translation TEXT COMMENT '大象译文',

    -- 现代解读
    symbolism VARCHAR(255) COMMENT '象征意涵',
    life_lesson TEXT COMMENT '人生启示',
    advice TEXT COMMENT '实用建议'
);
```

**核心内容**:
- **卦辞**: 原文、注释、译文（三段式）
- **彖传**: 解释卦名卦辞
- **大象**: 卦象的象征意义
- **现代解读**: 实用建议和人生启示

---

### 3. hexagram_lines (爻辞表)

**数据量**: 384 条 (64 × 6)

```sql
CREATE TABLE hexagram_lines (
    id SMALLINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    hexagram_id TINYINT UNSIGNED NOT NULL,
    position TINYINT UNSIGNED NOT NULL COMMENT '爻位 1-6',
    name VARCHAR(10) NOT NULL COMMENT '初九、六二等',
    type ENUM('yang', 'yin') NOT NULL,

    -- 爻辞
    line_text TEXT NOT NULL COMMENT '爻辞原文',
    line_annotation TEXT COMMENT '爻辞注释',
    line_translation TEXT COMMENT '爻辞译文',

    -- 小象
    xiang_text TEXT COMMENT '小象原文',
    xiang_annotation TEXT COMMENT '小象注释',
    xiang_translation TEXT COMMENT '小象译文'
);
```

**说明**:
- 每个卦有 6 个爻，从下往上编号 1-6
- 包含爻辞和小象传

---

### 4. wenyan (文言传)

**数据量**: 约 20-30 条（仅乾坤两卦）

```sql
CREATE TABLE wenyan (
    id TINYINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    hexagram_id TINYINT UNSIGNED NOT NULL COMMENT '1=乾 2=坤',
    section VARCHAR(20) COMMENT '段落标识',
    text TEXT NOT NULL COMMENT '原文',
    annotation TEXT COMMENT '注释',
    translation TEXT COMMENT '译文',
    sort_order TINYINT UNSIGNED
);
```

**特点**:
- 仅用于乾坤两卦
- 是对这两卦的详细阐述

---

### 5. xici (系辞传)

**数据量**: 约 100-150 条

```sql
CREATE TABLE xici (
    id SMALLINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    part ENUM('upper', 'lower') NOT NULL COMMENT '上篇/下篇',
    chapter TINYINT UNSIGNED COMMENT '章节',
    paragraph TINYINT UNSIGNED COMMENT '段落',
    text TEXT NOT NULL COMMENT '原文',
    annotation TEXT COMMENT '注释',
    translation TEXT COMMENT '译文',
    sort_order SMALLINT UNSIGNED
);
```

**特点**:
- 分上下两篇
- 阐述易经哲学和占卜原理

---

### 6. xugua (序卦传)

**数据量**: 64 条

```sql
CREATE TABLE xugua (
    id TINYINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    hexagram_id TINYINT UNSIGNED COMMENT '关联卦',
    text TEXT NOT NULL COMMENT '原文',
    annotation TEXT COMMENT '注释',
    translation TEXT COMMENT '译文',
    sort_order TINYINT UNSIGNED
);
```

**特点**:
- 解释六十四卦的排列顺序
- 说明卦与卦之间的承接关系

---

### 7. shuogua (说卦传)

**数据量**: 约 11 章

```sql
CREATE TABLE shuogua (
    id TINYINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    chapter TINYINT UNSIGNED COMMENT '章节',
    paragraph TINYINT UNSIGNED COMMENT '段落',
    text TEXT NOT NULL COMMENT '原文',
    annotation TEXT COMMENT '注释',
    translation TEXT COMMENT '译文',
    sort_order TINYINT UNSIGNED
);
```

**特点**:
- 专门讲解八卦的性质和象征

---

### 8. zagua (杂卦传)

**数据量**: 64 条

```sql
CREATE TABLE zagua (
    id TINYINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    hexagram_id TINYINT UNSIGNED COMMENT '关联卦',
    text TEXT NOT NULL COMMENT '原文',
    annotation TEXT COMMENT '注释',
    translation TEXT COMMENT '译文',
    sort_order TINYINT UNSIGNED
);
```

**特点**:
- 简要说明每一卦的卦义
- 往往将相关或相反的卦并列

---

### 9. divination_records (占卜记录表)

**数据量**: 用户数据（动态增长）

```sql
CREATE TABLE divination_records (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT UNSIGNED NOT NULL,

    -- 占卜方式
    method ENUM('coin', 'manual', 'meihua', 'daily', 'qian'),

    -- 问题
    question VARCHAR(500),
    category VARCHAR(50) COMMENT '问题分类',

    -- 摇卦结果
    lines JSON COMMENT '[7,8,9,7,6,8]',

    -- 卦象
    original_hexagram_id TINYINT UNSIGNED NOT NULL,
    changed_hexagram_id TINYINT UNSIGNED,
    changing_lines JSON COMMENT '[3,5]',
    changing_line_count TINYINT UNSIGNED,

    -- AI解读
    ai_interpretation TEXT,
    ai_summary VARCHAR(1000),
    ai_model VARCHAR(50),

    -- 用户反馈
    is_favorited TINYINT(1),
    user_rating TINYINT UNSIGNED COMMENT '1-5',
    user_feedback VARCHAR(500),
    is_accurate TINYINT(1) COMMENT '是否应验',

    -- 状态
    is_public TINYINT(1),
    status ENUM('active', 'archived', 'deleted'),

    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
```

**核心功能**:
- 记录用户每次占卜的完整信息
- 支持多种占卜方法
- 记录 AI 解读结果
- 用户可以反馈和评分

---

## 数据库关系图

```
┌───────────┐
│ trigrams  │ 八卦(8)
└─────┬─────┘
      │ 1:N
      ▼
┌───────────┐     ┌────────────────┐
│ hexagrams │────►│ hexagram_lines │ 爻辞(384)
│  64卦     │ 1:6 └────────────────┘
└─────┬─────┘
      │
      ├──────► wenyan    文言传(乾坤专属)
      ├──────► xugua     序卦传(卦序说明)
      └──────► zagua     杂卦传(卦义简述)

┌───────────┐
│   xici    │ 系辞传(独立篇章)
├───────────┤
│  shuogua  │ 说卦传(独立篇章)
└───────────┘

┌─────────────────────┐
│ divination_records  │ 用户数据
│  (关联 hexagrams)    │
└─────────────────────┘
```

---

## 快速开始

### 步骤 1: 创建表结构

```bash
cd /Users/juvenile/code/github.com/spring-ai-community/yy-yao

mysql -u root -p yy_yao < docs/database-schema-complete.sql
```

### 步骤 2: 导入基础数据

```bash
# 1. 导入八卦数据 (8条)
mysql -u root -p yy_yao < docs/trigrams_init.sql

# 2. 导入六十四卦数据 (64条)
mysql -u root -p yy_yao < docs/hexagrams_complete_init.sql

# 3. 导入爻辞数据 (384条)
mysql -u root -p yy_yao < docs/hexagram_lines_complete_init.sql

# 4. 导入十翼数据（可选，按需导入）
mysql -u root -p yy_yao < docs/shiyi_init.sql
```

### 步骤 3: 验证数据

```sql
-- 检查数据量
SELECT 'trigrams' AS tbl, COUNT(*) AS cnt FROM trigrams
UNION ALL
SELECT 'hexagrams', COUNT(*) FROM hexagrams
UNION ALL
SELECT 'hexagram_lines', COUNT(*) FROM hexagram_lines
UNION ALL
SELECT 'wenyan', COUNT(*) FROM wenyan
UNION ALL
SELECT 'xici', COUNT(*) FROM xici;
```

---

## API 接口设计

### 1. 卦象相关 API

#### GET /api/hexagram/:id
获取完整卦象（含彖、象、爻辞）

**响应示例**:
```json
{
  "id": 1,
  "name": "乾",
  "fullName": "乾为天",
  "symbol": "111111",
  "judgment": {
    "text": "乾。元亨利贞。",
    "annotation": "...",
    "translation": "乾卦。大吉大利..."
  },
  "tuan": {
    "text": "大哉乾元...",
    "translation": "..."
  },
  "image": {
    "text": "天行健，君子以自强不息。",
    "translation": "..."
  },
  "lines": [
    {
      "position": 1,
      "name": "初九",
      "type": "yang",
      "lineText": "潜龙勿用。",
      "xiangText": "潜龙勿用，阳在下也。"
    }
    // ... 其他5爻
  ],
  "symbolism": "...",
  "lifeLesson": "...",
  "advice": "..."
}
```

#### GET /api/hexagram/:id/wenyan
获取文言传（仅乾坤）

**响应示例**:
```json
{
  "hexagramId": 1,
  "hexagramName": "乾",
  "sections": [
    {
      "section": "元者",
      "text": "元者，善之长也...",
      "annotation": "...",
      "translation": "..."
    }
    // ... 其他段落
  ]
}
```

---

### 2. 十翼相关 API

#### GET /api/xici/:part
获取系辞传（upper/lower）

**请求**: `GET /api/xici/upper`

**响应示例**:
```json
{
  "part": "upper",
  "chapters": [
    {
      "chapter": 1,
      "paragraphs": [
        {
          "text": "天尊地卑，乾坤定矣...",
          "annotation": "...",
          "translation": "..."
        }
      ]
    }
  ]
}
```

#### GET /api/xugua
获取序卦传

#### GET /api/shuogua
获取说卦传

#### GET /api/zagua
获取杂卦传

---

### 3. 占卜相关 API

#### POST /api/divine
执行占卜

**请求体**:
```json
{
  "userId": 12345,
  "method": "coin",
  "question": "今年事业运势如何？",
  "category": "事业",
  "lines": [7, 8, 9, 7, 6, 8]
}
```

**响应示例**:
```json
{
  "recordId": 987654,
  "originalHexagram": {
    "id": 14,
    "name": "大有",
    "judgment": "..."
  },
  "changedHexagram": {
    "id": 26,
    "name": "大畜"
  },
  "changingLines": [3, 5],
  "changingLineCount": 2,
  "interpretation": "根据占卜结果...",
  "aiSummary": "本次占卜得大有卦，变大畜卦...",
  "createdAt": "2026-01-26T10:30:00"
}
```

#### GET /api/divination/records
获取用户占卜记录列表

**查询参数**:
- `userId`: 用户ID（必填）
- `method`: 占卜方式（可选）
- `category`: 问题分类（可选）
- `isFavorited`: 是否收藏（可选）
- `page`: 页码（默认0）
- `size`: 每页大小（默认20）

#### GET /api/divination/records/:id
获取单条占卜记录详情

#### PUT /api/divination/records/:id
更新占卜记录（收藏、评分、反馈等）

**请求体**:
```json
{
  "isFavorited": true,
  "userRating": 5,
  "userFeedback": "很准确",
  "isAccurate": true,
  "accuracyNote": "一个月后果然升职了"
}
```

#### GET /api/divination/statistics
获取占卜统计数据

**响应示例**:
```json
{
  "userId": 12345,
  "totalCount": 156,
  "favoritedCount": 23,
  "accurateCount": 45,
  "methodStats": {
    "coin": 120,
    "meihua": 30,
    "daily": 6
  },
  "categoryStats": {
    "事业": 60,
    "感情": 40,
    "健康": 30
  },
  "lastDivinationDate": "2026-01-26"
}
```

---

## 实体类说明

### Java 实体类列表

#### 基础数据实体
- `Trigram.java` - 八卦
- `Hexagram.java` - 六十四卦
- `HexagramLine.java` - 爻辞

#### 十翼实体
- `Wenyan.java` - 文言传
- `Xici.java` - 系辞传
- `Xugua.java` - 序卦传
- `Shuogua.java` - 说卦传
- `Zagua.java` - 杂卦传

#### 应用实体
- `DivinationRecord.java` - 占卜记录

### Repository 接口列表

- `TrigramRepository.java`
- `HexagramRepository.java`
- `HexagramLineRepository.java`
- `WenyanRepository.java`
- `XiciRepository.java`
- `XuguaRepository.java`
- `ShuoguaRepository.java`
- `ZaguaRepository.java`
- `DivinationRecordRepository.java`

---

## 数据导入

### 导入顺序（重要！）

由于外键约束，必须按以下顺序导入:

1. **trigrams** (八卦) - 无依赖
2. **hexagrams** (六十四卦) - 依赖 trigrams
3. **hexagram_lines** (爻辞) - 依赖 hexagrams
4. **wenyan** (文言传) - 依赖 hexagrams
5. **xugua** (序卦传) - 依赖 hexagrams
6. **zagua** (杂卦传) - 依赖 hexagrams
7. **xici** (系辞传) - 无依赖
8. **shuogua** (说卦传) - 无依赖

### 数据文件说明

| 文件名 | 说明 | 数据量 |
|--------|------|--------|
| `trigrams_init.sql` | 八卦数据 | 8 |
| `hexagrams_complete_init.sql` | 六十四卦完整数据（含彖象） | 64 |
| `hexagram_lines_complete_init.sql` | 爻辞完整数据（含小象） | 384 |
| `shiyi_init.sql` | 十翼数据（所有传文） | ~300 |

---

## 占卜记录管理

### 占卜流程

1. **用户输入**: 问题、占卜方式
2. **生成卦象**: 6次摇卦得到数值（6-9）
3. **计算本卦和变卦**:
   - 6(老阴) 和 9(老阳) 为变爻
   - 生成本卦和变卦
4. **AI解读**: 调用AI模型生成解读
5. **保存记录**: 存入 divination_records 表

### 变爻规则

| 数值 | 名称 | 类型 | 是否变 | 变化 |
|------|------|------|--------|------|
| 6 | 老阴 | 阴 | 是 | 变阳 |
| 7 | 少阳 | 阳 | 否 | 不变 |
| 8 | 少阴 | 阴 | 否 | 不变 |
| 9 | 老阳 | 阳 | 是 | 变阴 |

### 解卦规则

- **0个变爻**: 看本卦卦辞
- **1个变爻**: 看该爻的爻辞
- **2个变爻**: 看两个变爻的爻辞，以上爻为主
- **3个变爻**: 看本卦和变卦卦辞，以本卦为主
- **4个变爻**: 看不变的两爻，以下爻为主
- **5个变爻**: 看唯一不变的爻辞
- **6个变爻**: 看变卦卦辞

---

## 常见问题

### Q1: 如何清空测试数据？

```sql
-- 顺序很重要！先删子表
TRUNCATE TABLE divination_records;
TRUNCATE TABLE wenyan;
TRUNCATE TABLE xugua;
TRUNCATE TABLE zagua;
TRUNCATE TABLE hexagram_lines;
TRUNCATE TABLE hexagrams;
TRUNCATE TABLE trigrams;
TRUNCATE TABLE xici;
TRUNCATE TABLE shuogua;
```

### Q2: 如何查询包含特定关键词的卦象？

```sql
SELECT * FROM hexagrams
WHERE judgment_translation LIKE '%吉%'
   OR life_lesson LIKE '%吉%';
```

### Q3: JSON字段如何查询？

```sql
-- 查询变爻包含位置3的记录
SELECT * FROM divination_records
WHERE JSON_CONTAINS(changing_lines, '3');

-- 查询变爻数量大于2的记录
SELECT * FROM divination_records
WHERE changing_line_count > 2;
```

---

## 总结

这个完整版数据库设计：

✅ 包含易经全部经文和十翼内容
✅ 支持用户占卜记录管理
✅ 支持AI解读和用户反馈
✅ 数据结构清晰，易于扩展
✅ 适合构建专业的易经应用

---

**文档版本**: 3.0
**最后更新**: 2026-01-26
**维护者**: Claude Code
