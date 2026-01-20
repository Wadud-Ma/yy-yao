# 易经数据库表结构设计

## 概述

本文档定义了易经数据存储的完整表结构，包括卦象、爻辞、彖传、象传等所有文本内容。

## 表结构

### 1. hexagram_info - 卦象基础信息表

| 字段名 | 类型 | 说明 | 示例 |
|--------|------|------|------|
| id | BIGINT | 主键 | 1 |
| number | INT | 卦序号(1-64) | 1 |
| name | VARCHAR(10) | 卦名 | 乾 |
| symbol | VARCHAR(10) | 卦符号 | ☰☰ |
| binary | VARCHAR(6) | 二进制表示 | 111111 |
| upper_trigram | VARCHAR(10) | 上卦 | 乾 |
| lower_trigram | VARCHAR(10) | 下卦 | 乾 |
| trigram_nature | VARCHAR(50) | 卦象属性 | 天:健 |
| created_at | TIMESTAMP | 创建时间 | |
| updated_at | TIMESTAMP | 更新时间 | |

### 2. hexagram_text - 卦象文本表

| 字段名 | 类型 | 说明 | 示例 |
|--------|------|------|------|
| id | BIGINT | 主键 | 1 |
| hexagram_id | BIGINT | 关联卦象ID | 1 |
| original_text | TEXT | 卦辞原文 | 乾:元亨,利贞。 |
| tuan_text | TEXT | 《彖》曰原文 | 大哉乾元,万物资始... |
| tuan_translation | TEXT | 彖传译文 | 伟大啊乾元... |
| tuan_annotation | TEXT | 彖传注释 | 元亨利贞是四德... |
| xiang_text | TEXT | 《象》曰原文 | 天行健,君子以自强不息 |
| xiang_translation | TEXT | 象传译文 | 天道运行刚健... |
| xiang_annotation | TEXT | 象传注释 | 天行健指... |
| overall_translation | TEXT | 卦辞译文 | 乾卦象征天... |
| overall_annotation | TEXT | 卦辞注释 | 元表示开始... |
| comprehensive_meaning | TEXT | 综合寓意 | 乾卦代表刚健进取... |
| application_guidance | TEXT | 应用指导 | 在事业上应积极进取... |
| created_at | TIMESTAMP | 创建时间 | |
| updated_at | TIMESTAMP | 更新时间 | |

### 3. line_text - 爻辞文本表

| 字段名 | 类型 | 说明 | 示例 |
|--------|------|------|------|
| id | BIGINT | 主键 | 1 |
| hexagram_id | BIGINT | 关联卦象ID | 1 |
| position | INT | 爻位(1-6) | 1 |
| line_name | VARCHAR(20) | 爻名 | 初九 |
| is_yang | BOOLEAN | 是否为阳爻 | true |
| original_text | TEXT | 爻辞原文 | 潜龙勿用。 |
| xiang_text | TEXT | 小象传原文 | 潜龙勿用,阳在下也 |
| xiang_translation | TEXT | 小象传译文 | 潜藏的龙不要使用... |
| xiang_annotation | TEXT | 小象传注释 | 阳在下指... |
| translation | TEXT | 爻辞译文 | 龙潜伏在水中... |
| annotation | TEXT | 爻辞注释 | 潜龙比喻... |
| meaning | TEXT | 爻义 | 时机未到,应当等待 |
| application | TEXT | 实际应用 | 在此阶段应韬光养晦 |
| created_at | TIMESTAMP | 创建时间 | |
| updated_at | TIMESTAMP | 更新时间 | |

### 4. hexagram_relationship - 卦象关系表

| 字段名 | 类型 | 说明 | 示例 |
|--------|------|------|------|
| id | BIGINT | 主键 | 1 |
| hexagram_id | BIGINT | 卦象ID | 1 |
| related_hexagram_id | BIGINT | 相关卦象ID | 2 |
| relationship_type | VARCHAR(20) | 关系类型 | 综卦/错卦/互卦 |
| description | TEXT | 关系说明 | 乾坤为对立统一 |
| created_at | TIMESTAMP | 创建时间 | |

### 5. trigram_info - 八卦基础信息表

| 字段名 | 类型 | 说明 | 示例 |
|--------|------|------|------|
| id | BIGINT | 主键 | 1 |
| name | VARCHAR(10) | 卦名 | 乾 |
| symbol | VARCHAR(5) | 符号 | ☰ |
| binary | VARCHAR(3) | 二进制 | 111 |
| nature | VARCHAR(20) | 属性 | 天 |
| attribute | VARCHAR(20) | 特质 | 健 |
| family_member | VARCHAR(20) | 家族成员 | 父 |
| direction | VARCHAR(20) | 方位 | 西北 |
| season | VARCHAR(20) | 季节 | 秋冬之交 |
| body_part | VARCHAR(20) | 人体部位 | 头 |
| animal | VARCHAR(20) | 动物 | 马 |
| color | VARCHAR(20) | 颜色 | 大赤 |
| number | VARCHAR(20) | 数字 | 一 |
| description | TEXT | 描述 | 乾为天,为圆... |
| created_at | TIMESTAMP | 创建时间 | |

## 关系说明

```
hexagram_info (1) ----< (1) hexagram_text
hexagram_info (1) ----< (*) line_text
hexagram_info (1) ----< (*) hexagram_relationship
trigram_info (*) ----< (*) hexagram_info (通过upper_trigram和lower_trigram)
```

## 索引设计

### hexagram_info
- PRIMARY KEY (id)
- UNIQUE INDEX idx_hexagram_number (number)
- INDEX idx_hexagram_name (name)
- INDEX idx_hexagram_binary (binary)

### hexagram_text
- PRIMARY KEY (id)
- UNIQUE INDEX idx_hexagram_text_hexagram_id (hexagram_id)
- FOREIGN KEY fk_hexagram_text_hexagram (hexagram_id) REFERENCES hexagram_info(id)

### line_text
- PRIMARY KEY (id)
- INDEX idx_line_hexagram_position (hexagram_id, position)
- FOREIGN KEY fk_line_text_hexagram (hexagram_id) REFERENCES hexagram_info(id)

### hexagram_relationship
- PRIMARY KEY (id)
- INDEX idx_relationship_hexagram (hexagram_id)
- INDEX idx_relationship_related (related_hexagram_id)

### trigram_info
- PRIMARY KEY (id)
- UNIQUE INDEX idx_trigram_name (name)

## 数据示例

### 乾卦完整数据示例

**hexagram_info:**
```
number: 1
name: 乾
symbol: ☰☰
binary: 111111
upper_trigram: 乾
lower_trigram: 乾
```

**hexagram_text:**
```
original_text: 乾:元亨,利贞。
tuan_text: 大哉乾元,万物资始,乃统天。云行雨施,品物流形。大明终始,六位时成,时乘六龙以御天。乾道变化,各正性命,保合大和,乃利贞。首出庶物,万国咸宁。
xiang_text: 天行健,君子以自强不息。
overall_translation: 乾卦象征天,元始亨通,利于坚守正道。
```

**line_text (初九):**
```
position: 1
line_name: 初九
original_text: 潜龙勿用。
xiang_text: 潜龙勿用,阳在下也。
translation: 龙潜伏在深渊之中,暂时不要有所作为。
meaning: 时机未到,应当韬光养晦,积蓄力量。
```

## 查询示例

### 1. 获取完整卦象信息
```sql
SELECT
    hi.*,
    ht.*
FROM hexagram_info hi
LEFT JOIN hexagram_text ht ON hi.id = ht.hexagram_id
WHERE hi.number = 1;
```

### 2. 获取卦象的所有爻辞
```sql
SELECT *
FROM line_text
WHERE hexagram_id = (SELECT id FROM hexagram_info WHERE number = 1)
ORDER BY position;
```

### 3. 搜索包含关键词的卦象
```sql
SELECT hi.number, hi.name, ht.original_text
FROM hexagram_info hi
JOIN hexagram_text ht ON hi.id = ht.hexagram_id
WHERE ht.original_text LIKE '%亨%'
   OR ht.tuan_text LIKE '%亨%'
   OR ht.xiang_text LIKE '%亨%';
```

### 4. 查询某一爻的详细信息
```sql
SELECT
    hi.number,
    hi.name,
    lt.*
FROM line_text lt
JOIN hexagram_info hi ON lt.hexagram_id = hi.id
WHERE hi.number = 1 AND lt.position = 5;
```

## 扩展字段建议

根据实际需求,可以考虑添加以下字段:

1. **hexagram_text表**
   - `wenyan_text` - 文言传
   - `xugua_text` - 序卦传
   - `zagua_text` - 杂卦传
   - `shuogua_text` - 说卦传相关内容

2. **line_text表**
   - `historical_examples` - 历史案例
   - `modern_interpretation` - 现代诠释

3. **元数据表**
   - `source_version` - 版本来源(如:王弼注、程颐传等)
   - `author` - 注释作者
