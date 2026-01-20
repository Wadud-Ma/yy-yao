# 易经数据库使用指南

## 数据库表结构

系统包含3个核心表，用于存储易经原文、注释和译文。

### 1. classic_text (原文表)

存储《周易》等典籍的原始文本。

```sql
CREATE TABLE classic_text (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,              -- 标题
    chapter VARCHAR(50),                      -- 章节
    content TEXT NOT NULL,                    -- 原文内容
    author VARCHAR(50),                       -- 作者
    dynasty VARCHAR(20),                      -- 朝代
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX idx_classic_title ON classic_text(title);
CREATE INDEX idx_classic_chapter ON classic_text(chapter);
CREATE INDEX idx_classic_dynasty ON classic_text(dynasty);
```

### 2. annotation (注释表)

存储对原文中特定标记的注释。

```sql
CREATE TABLE annotation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    text_id BIGINT NOT NULL,                  -- 关联原文ID
    marker VARCHAR(20) NOT NULL,              -- 标记符号 (如: [1], [2])
    content TEXT NOT NULL,                    -- 注释内容
    type VARCHAR(20),                         -- 注释类型
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (text_id) REFERENCES classic_text(id)
);

CREATE INDEX idx_annotation_text_id ON annotation(text_id);
CREATE INDEX idx_annotation_marker ON annotation(marker);
```

### 3. translation (译文表)

存储对经段的现代翻译。

```sql
CREATE TABLE translation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    text_id BIGINT NOT NULL,                  -- 关联原文ID
    content TEXT NOT NULL,                    -- 翻译内容
    translator VARCHAR(50),                   -- 译者
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (text_id) REFERENCES classic_text(id)
);

CREATE INDEX idx_translation_text_id ON translation(text_id);
CREATE INDEX idx_translation_translator ON translation(translator);
```

## 数据关系

```
classic_text (1) ----< (*) annotation
classic_text (1) ----< (*) translation
```

- 一篇原文可以有多个注释
- 一篇原文可以有多个译文(不同译者)
- 通过 text_id 关联

## 常用SQL查询

### 查询完整的经文(含注释和译文)

```sql
-- 查询乾卦的完整信息
SELECT
    ct.id,
    ct.title,
    ct.chapter,
    ct.content,
    a.marker,
    a.content AS annotation_content,
    t.content AS translation_content,
    t.translator
FROM classic_text ct
LEFT JOIN annotation a ON ct.id = a.text_id
LEFT JOIN translation t ON ct.id = t.text_id
WHERE ct.chapter = '乾卦';
```

### 搜索包含特定关键词的经文

```sql
SELECT * FROM classic_text
WHERE content LIKE '%潜龙勿用%'
   OR title LIKE '%潜龙勿用%';
```

### 查询某章节的所有注释

```sql
SELECT
    ct.chapter,
    a.marker,
    a.content,
    a.type
FROM annotation a
JOIN classic_text ct ON a.text_id = ct.id
WHERE ct.chapter = '屯卦'
ORDER BY a.marker;
```

### 统计各章节的注释数量

```sql
SELECT
    ct.chapter,
    COUNT(a.id) AS annotation_count
FROM classic_text ct
LEFT JOIN annotation a ON ct.id = a.text_id
GROUP BY ct.chapter
ORDER BY annotation_count DESC;
```

### 查询某译者的所有译文

```sql
SELECT
    ct.title,
    ct.chapter,
    t.content
FROM translation t
JOIN classic_text ct ON t.text_id = ct.id
WHERE t.translator = '朱熹';
```

## Excel导入说明

### Excel文件格式要求

#### 方式1: 单行模式(推荐)

每行包含完整的经文信息:

| 章节 | 标题 | 原文 | 作者 | 朝代 | 注释 | 标记 | 注释类型 | 译文 | 译者 |
|------|------|------|------|------|------|------|----------|------|------|
| 屯卦 | 《周易·屯卦》 | 象曰：屯，逐也... | 周文王 | 周 | 不司与长：犹言... | [1] | 注释 | 屯卦象征万物初生... | 朱熹 |

#### 方式2: 最小模式

只需要原文列:

| 原文 |
|------|
| 象曰：屯，逐也。来遇阻也... |

其他字段会自动生成默认值。

### 导入步骤

1. 准备Excel文件(格式: .xlsx)
2. 确保至少包含"原文"列
3. 调用导入API:

```bash
curl -X POST "http://localhost:8080/api/classic/import?filePath=/path/to/易经.xlsx"
```

4. 查看导入结果

### 导入示例数据

你可以直接导入提供的 `book/易经.xlsx` 文件:

```bash
curl -X POST "http://localhost:8080/api/classic/import?filePath=./book/易经.xlsx"
```

## 数据库备份与恢复

### H2数据库备份

```bash
# 备份数据库
cp -r ./data/yy-yao.mv.db ./backup/yy-yao-backup-$(date +%Y%m%d).mv.db

# 恢复数据库
cp ./backup/yy-yao-backup-20260119.mv.db ./data/yy-yao.mv.db
```

### 导出为SQL

通过H2 Console导出:

1. 访问 http://localhost:8080/h2-console
2. 连接数据库
3. 执行: `SCRIPT TO 'backup.sql'`

### 从SQL恢复

```sql
RUNSCRIPT FROM 'backup.sql';
```

## 数据库迁移

### 从H2迁移到MySQL

1. 修改 application.properties:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/yy_yao?useUnicode=true&characterEncoding=UTF-8
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

2. 创建MySQL数据库:

```sql
CREATE DATABASE yy_yao CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. 重启应用，表结构会自动创建

4. 导入数据(使用Excel或SQL脚本)

### 从H2迁移到PostgreSQL

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/yy_yao
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

## 性能优化建议

### 1. 添加全文索引

MySQL:
```sql
ALTER TABLE classic_text ADD FULLTEXT(content);
ALTER TABLE annotation ADD FULLTEXT(content);
ALTER TABLE translation ADD FULLTEXT(content);
```

PostgreSQL:
```sql
CREATE INDEX idx_classic_text_content ON classic_text USING gin(to_tsvector('chinese', content));
```

### 2. 分区表(大数据量时)

```sql
-- 按章节分区
ALTER TABLE classic_text
PARTITION BY LIST (chapter) (
    PARTITION p_qian VALUES IN ('乾卦'),
    PARTITION p_kun VALUES IN ('坤卦'),
    ...
);
```

### 3. 缓存配置

添加Redis缓存，减少数据库查询:

```properties
spring.cache.type=redis
spring.redis.host=localhost
spring.redis.port=6379
```

## 故障排查

### 数据库连接失败

检查数据库文件权限:
```bash
ls -la ./data/
chmod 755 ./data/
```

### 中文乱码

确保数据库和连接字符集都是UTF-8:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/yy_yao?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
```

### Excel导入失败

1. 检查文件路径是否正确
2. 确保文件格式为 .xlsx
3. 查看日志文件获取详细错误信息

## 开发环境数据库访问

H2 Console:
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:file:./data/yy-yao`
- Username: `sa`
- Password: (空)

## 生产环境建议

1. 使用MySQL或PostgreSQL替代H2
2. 启用主从复制，提高可用性
3. 定期备份数据
4. 使用连接池(HikariCP已内置)
5. 监控慢查询，优化索引
