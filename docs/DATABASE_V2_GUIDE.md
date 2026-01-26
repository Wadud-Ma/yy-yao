# 易经数据库 V2.0 完整指南

## 🎯 概述

V2.0 采用规范化的三层表结构设计,清晰分离八卦、六十四卦和爻辞数据。

### 核心优势

- ✅ **规范化设计**: 三层表结构,减少数据冗余
- ✅ **外键约束**: 保证数据完整性和一致性
- ✅ **查询优化**: 预定义视图和存储过程
- ✅ **易于扩展**: 支持多版本、多语言扩展

---

## 📊 表结构设计

### 1. trigrams (八卦表) - 8条记录

```sql
CREATE TABLE trigrams (
    id TINYINT UNSIGNED PRIMARY KEY,          -- 1-8
    name VARCHAR(4) NOT NULL,                 -- 乾、坤、震...
    symbol CHAR(3) NOT NULL,                  -- 111, 000...
    unicode_symbol CHAR(1) NOT NULL,          -- ☰☷☳...
    nature VARCHAR(10),                       -- 天、地、雷...
    attribute VARCHAR(20),                    -- 刚健、柔顺...
    direction VARCHAR(10),                    -- 西北、西南...
    season VARCHAR(20),                       -- 春、夏、秋、冬
    family_member VARCHAR(10),                -- 父、母、长男...
    body_part VARCHAR(20),                    -- 头、足、手...
    animal VARCHAR(20),                       -- 马、牛、龙...
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
```

**数据示例**:
| id | name | symbol | unicode | nature | attribute |
|----|------|--------|---------|--------|-----------|
| 1  | 乾   | 111    | ☰       | 天     | 刚健      |
| 8  | 坤   | 000    | ☷       | 地     | 柔顺      |

---

### 2. hexagrams (六十四卦表) - 64条记录

```sql
CREATE TABLE hexagrams (
    id TINYINT UNSIGNED PRIMARY KEY,          -- 1-64
    name VARCHAR(4) NOT NULL,                 -- 卦名
    symbol CHAR(6) NOT NULL,                  -- 111111 (从下到上)
    unicode_symbol VARCHAR(10),               -- ☰☰
    upper_trigram_id TINYINT UNSIGNED,        -- 上卦ID (FK)
    lower_trigram_id TINYINT UNSIGNED,        -- 下卦ID (FK)
    full_name VARCHAR(20),                    -- 乾为天
    judgment TEXT NOT NULL,                   -- 卦辞原文
    judgment_explain TEXT,                    -- 卦辞解释
    image_text TEXT,                          -- 大象辞
    image_explain TEXT,                       -- 大象解释
    tuan_text TEXT,                           -- 彖辞
    tuan_explain TEXT,                        -- 彖辞解释
    symbolism VARCHAR(255),                   -- 象征意涵
    life_lesson VARCHAR(500),                 -- 人生启示
    advice VARCHAR(500),                      -- 实用建议
    created_at TIMESTAMP,
    updated_at TIMESTAMP,

    FOREIGN KEY (upper_trigram_id) REFERENCES trigrams(id),
    FOREIGN KEY (lower_trigram_id) REFERENCES trigrams(id)
);
```

**数据示例**:
| id | name | symbol  | upper_id | lower_id | full_name | judgment |
|----|------|---------|----------|----------|-----------|----------|
| 1  | 乾   | 111111  | 1        | 1        | 乾为天    | 元亨利贞 |
| 2  | 坤   | 000000  | 8        | 8        | 坤为地    | 元亨...  |

---

### 3. hexagram_lines (爻辞表) - 384条记录

```sql
CREATE TABLE hexagram_lines (
    id SMALLINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    hexagram_id TINYINT UNSIGNED NOT NULL,    -- 所属卦ID (FK)
    position TINYINT UNSIGNED NOT NULL,       -- 爻位 1-6
    line_name VARCHAR(10) NOT NULL,           -- 初九、九二...
    line_type ENUM('yang', 'yin') NOT NULL,   -- 阳爻/阴爻
    line_text TEXT NOT NULL,                  -- 爻辞原文
    line_explain TEXT,                        -- 爻辞解释
    image_text TEXT,                          -- 小象辞
    image_explain TEXT,                       -- 小象解释
    meaning TEXT,                             -- 爻义
    application TEXT,                         -- 实际应用
    created_at TIMESTAMP,
    updated_at TIMESTAMP,

    FOREIGN KEY (hexagram_id) REFERENCES hexagrams(id) ON DELETE CASCADE,
    UNIQUE KEY (hexagram_id, position)
);
```

**数据示例**:
| id | hexagram_id | position | line_name | line_type | line_text      |
|----|-------------|----------|-----------|-----------|----------------|
| 1  | 1           | 1        | 初九      | yang      | 潜龙勿用       |
| 2  | 1           | 2        | 九二      | yang      | 见龙在田...    |

---

## 🚀 快速开始

### 步骤 1: 创建表结构

```bash
cd /Users/juvenile/code/github.com/spring-ai-community/yy-yao

# 执行表结构脚本
mysql -u root -p yy_yao < docs/database-schema-v2.sql
```

### 步骤 2: 导入数据

```bash
# 1. 导入八卦数据 (8条)
mysql -u root -p yy_yao < docs/trigrams_init.sql

# 2. 导入六十四卦数据 (64条)
mysql -u root -p yy_yao < docs/hexagrams_init.sql

# 3. 导入爻辞数据 (384条)
mysql -u root -p yy_yao < docs/hexagram_lines_init.sql
```

### 步骤 3: 验证数据

```sql
-- 检查数据量
SELECT
    (SELECT COUNT(*) FROM trigrams) AS trigrams,
    (SELECT COUNT(*) FROM hexagrams) AS hexagrams,
    (SELECT COUNT(*) FROM hexagram_lines) AS lines;

-- 应该返回: 8, 64, 384
```

---

## 📖 常用查询

### 查询 1: 获取单个卦象的完整信息

```sql
SELECT
    h.*,
    ut.name AS upper_trigram_name,
    ut.unicode_symbol AS upper_symbol,
    lt.name AS lower_trigram_name,
    lt.unicode_symbol AS lower_symbol
FROM hexagrams h
JOIN trigrams ut ON h.upper_trigram_id = ut.id
JOIN trigrams lt ON h.lower_trigram_id = lt.id
WHERE h.id = 1;
```

### 查询 2: 获取卦象及其所有爻辞

```sql
-- 方式1: 使用存储过程
CALL sp_get_hexagram_detail(1);

-- 方式2: 手动查询
SELECT * FROM hexagram_lines
WHERE hexagram_id = 1
ORDER BY position;
```

### 查询 3: JSON 格式输出 (适合 API)

```sql
SELECT
    h.id, h.name, h.symbol, h.unicode_symbol, h.full_name,
    JSON_OBJECT(
        'id', ut.id,
        'name', ut.name,
        'symbol', ut.symbol,
        'unicode', ut.unicode_symbol,
        'nature', ut.nature
    ) AS upper_trigram,
    JSON_OBJECT(
        'id', lt.id,
        'name', lt.name,
        'symbol', lt.symbol,
        'unicode', lt.unicode_symbol,
        'nature', lt.nature
    ) AS lower_trigram,
    h.judgment, h.judgment_explain,
    (SELECT JSON_ARRAYAGG(
        JSON_OBJECT(
            'position', position,
            'line_name', line_name,
            'line_type', line_type,
            'line_text', line_text
        ) ORDER BY position
    ) FROM hexagram_lines WHERE hexagram_id = h.id) AS lines
FROM hexagrams h
JOIN trigrams ut ON h.upper_trigram_id = ut.id
JOIN trigrams lt ON h.lower_trigram_id = lt.id
WHERE h.id = 1;
```

### 查询 4: 根据二进制符号查询

```sql
SELECT * FROM hexagrams WHERE symbol = '111111';
```

### 查询 5: 查询包含特定八卦的所有卦象

```sql
-- 查询包含乾卦的所有卦象
SELECT h.*, ut.name AS upper, lt.name AS lower
FROM hexagrams h
JOIN trigrams ut ON h.upper_trigram_id = ut.id
JOIN trigrams lt ON h.lower_trigram_id = lt.id
WHERE ut.name = '乾' OR lt.name = '乾';
```

### 查询 6: 全文搜索

```sql
-- 搜索卦辞
SELECT id, name, judgment
FROM hexagrams
WHERE judgment LIKE '%吉%'
   OR judgment_explain LIKE '%吉%';

-- 搜索爻辞
SELECT hl.*, h.name AS hexagram_name
FROM hexagram_lines hl
JOIN hexagrams h ON hl.hexagram_id = h.id
WHERE line_text LIKE '%龙%';
```

---

## 💻 Java 实体类

### Trigram.java

```java
@Entity
@Table(name = "trigrams")
public class Trigram {
    @Id
    private Integer id;

    private String name;
    private String symbol;
    private String unicodeSymbol;
    private String nature;
    private String attribute;
    // ... 其他字段
}
```

### Hexagram.java

```java
@Entity
@Table(name = "hexagrams")
public class Hexagram {
    @Id
    private Integer id;

    private String name;
    private String symbol;
    private String unicodeSymbol;
    private Integer upperTrigramId;
    private Integer lowerTrigramId;
    private String fullName;
    private String judgment;
    private String judgmentExplain;
    // ... 其他字段

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upper_trigram_id", insertable = false, updatable = false)
    private Trigram upperTrigram;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lower_trigram_id", insertable = false, updatable = false)
    private Trigram lowerTrigram;
}
```

### HexagramLine.java

```java
@Entity
@Table(name = "hexagram_lines")
public class HexagramLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer hexagramId;
    private Integer position;
    private String lineName;

    @Enumerated(EnumType.STRING)
    private LineType lineType;

    private String lineText;
    private String lineExplain;
    // ... 其他字段

    public enum LineType {
        yang,  // 阳爻
        yin    // 阴爻
    }
}
```

---

## 🔌 API 接口示例

### Controller 伪代码

```java
@RestController
@RequestMapping("/api/v2/hexagrams")
public class HexagramControllerV2 {

    @Autowired
    private HexagramService hexagramService;

    /**
     * 获取所有卦象列表
     */
    @GetMapping
    public List<HexagramDTO> getAllHexagrams() {
        return hexagramService.findAll();
    }

    /**
     * 获取单个卦象详情（含爻辞）
     */
    @GetMapping("/{id}")
    public HexagramDetailDTO getHexagramDetail(@PathVariable Integer id) {
        return hexagramService.getHexagramWithLines(id);
    }

    /**
     * 根据二进制查询卦象
     */
    @GetMapping("/by-symbol/{symbol}")
    public HexagramDetailDTO getBySymbol(@PathVariable String symbol) {
        return hexagramService.getBySymbol(symbol);
    }

    /**
     * 搜索卦象
     */
    @GetMapping("/search")
    public List<HexagramDTO> search(@RequestParam String keyword) {
        return hexagramService.search(keyword);
    }
}
```

---

## 📦 DTO 设计

### HexagramDTO (简化版)

```java
public class HexagramDTO {
    private Integer id;
    private String name;
    private String symbol;
    private String unicodeSymbol;
    private String fullName;
    private String judgment;
    private String symbolism;
    private String advice;

    // 上下卦信息
    private TrigramDTO upperTrigram;
    private TrigramDTO lowerTrigram;
}
```

### HexagramDetailDTO (完整版)

```java
public class HexagramDetailDTO extends HexagramDTO {
    private String judgmentExplain;
    private String imageText;
    private String imageExplain;
    private String lifeLesson;

    // 六爻信息
    private List<HexagramLineDTO> lines;
}
```

### HexagramLineDTO

```java
public class HexagramLineDTO {
    private Integer position;       // 1-6
    private String lineName;        // 初九、九二...
    private String lineType;        // yang/yin
    private String lineText;        // 爻辞原文
    private String lineExplain;     // 爻辞解释
    private String meaning;         // 爻义
}
```

---

## 🎯 API 响应示例

**GET /api/v2/hexagrams/1**

```json
{
  "id": 1,
  "name": "乾",
  "symbol": "111111",
  "unicodeSymbol": "☰☰",
  "fullName": "乾为天",
  "judgment": "元亨利贞",
  "judgmentExplain": "元亨利贞。象征天道运行,刚健有为。",
  "symbolism": "天、创造、刚健、阳气、领导力、积极进取",
  "lifeLesson": "天道酬勤。坚持不懈,发奋图强,方能成就大业。",
  "advice": "此时正是发奋进取的好时机,但需警惕过度自信。",
  "upperTrigram": {
    "id": 1,
    "name": "乾",
    "symbol": "111",
    "unicode": "☰",
    "nature": "天",
    "attribute": "刚健"
  },
  "lowerTrigram": {
    "id": 1,
    "name": "乾",
    "symbol": "111",
    "unicode": "☰",
    "nature": "天",
    "attribute": "刚健"
  },
  "lines": [
    {
      "position": 1,
      "lineName": "初九",
      "lineType": "yang",
      "lineText": "初九:潜龙勿用。龙德而隐者也...",
      "lineExplain": null,
      "meaning": null
    },
    // ... 其他5爻
  ]
}
```

---

## ✅ 数据完整性检查

```sql
-- 1. 检查每个卦象是否都有6个爻辞
SELECT
    h.id,
    h.name,
    COUNT(hl.id) AS line_count,
    CASE
        WHEN COUNT(hl.id) = 6 THEN '✅ 完整'
        ELSE '❌ 不完整'
    END AS status
FROM hexagrams h
LEFT JOIN hexagram_lines hl ON h.id = hl.hexagram_id
GROUP BY h.id, h.name
HAVING line_count != 6;
-- 应该返回空结果

-- 2. 检查是否有未使用的八卦
SELECT t.*
FROM trigrams t
WHERE t.id NOT IN (SELECT DISTINCT upper_trigram_id FROM hexagrams)
  AND t.id NOT IN (SELECT DISTINCT lower_trigram_id FROM hexagrams);
-- 应该返回空结果

-- 3. 统计八卦使用频率
SELECT
    t.name,
    COUNT(DISTINCT h1.id) AS as_upper_count,
    COUNT(DISTINCT h2.id) AS as_lower_count,
    COUNT(DISTINCT h1.id) + COUNT(DISTINCT h2.id) AS total_count
FROM trigrams t
LEFT JOIN hexagrams h1 ON t.id = h1.upper_trigram_id
LEFT JOIN hexagrams h2 ON t.id = h2.lower_trigram_id
GROUP BY t.id, t.name
ORDER BY t.id;
```

---

## 📚 相关文件

| 文件路径 | 说明 |
|---------|------|
| `docs/database-schema-v2.sql` | 表结构定义 |
| `docs/trigrams_init.sql` | 八卦数据初始化 (8条) |
| `docs/hexagrams_init.sql` | 六十四卦数据初始化 (64条) |
| `docs/hexagram_lines_init.sql` | 爻辞数据初始化 (384条) |
| `scripts/generate-hexagram-sql-v2.py` | SQL 生成工具 |
| `yy-yao-dao/.../Trigram.java` | 八卦实体类 |
| `yy-yao-dao/.../Hexagram.java` | 六十四卦实体类 |
| `yy-yao-dao/.../HexagramLine.java` | 爻辞实体类 |

---

## 🔄 从 V1 迁移到 V2

如果你已经使用了 V1 版本的表结构,可以通过以下步骤迁移:

1. **备份现有数据**
```bash
mysqldump -u root -p yy_yao > backup_v1.sql
```

2. **删除旧表**
```sql
DROP TABLE IF EXISTS line_text;
DROP TABLE IF EXISTS hexagram_text;
DROP TABLE IF EXISTS hexagram_info;
DROP TABLE IF EXISTS trigram_info;
```

3. **执行 V2 脚本** (参见"快速开始"部分)

---

## 🎉 总结

V2.0 版本的优势:

1. ✅ **规范化设计**: 符合数据库设计范式
2. ✅ **清晰的层次**: trigrams → hexagrams → hexagram_lines
3. ✅ **外键约束**: 保证数据完整性
4. ✅ **易于扩展**: 可轻松添加多语言、历史版本等
5. ✅ **优化查询**: 预定义视图和存储过程
6. ✅ **完整文档**: 详细的使用指南和示例

**推荐使用 V2.0 版本进行新项目开发!**

---

**文档版本**: 2.0
**最后更新**: 2026-01-26
**维护者**: Claude Code
