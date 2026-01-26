-- ============================================
-- 易经数据库表结构设计 V2.0
-- ============================================
-- 说明: 采用规范化设计，三层结构
-- 1. trigrams (八卦表) - 8条记录
-- 2. hexagrams (六十四卦表) - 64条记录
-- 3. hexagram_lines (爻辞表) - 384条记录
-- ============================================
-- 创建时间: 2026-01-26
-- 版本: 2.0
-- ============================================

-- 切换到目标数据库
USE yy_yao;

-- ============================================
-- 1. 八卦表（基础表，8条记录）
-- ============================================
CREATE TABLE IF NOT EXISTS trigrams (
    id TINYINT UNSIGNED PRIMARY KEY COMMENT '八卦编号 1-8',
    name VARCHAR(4) NOT NULL COMMENT '卦名：乾、坤、震、巽、坎、离、艮、兑',
    symbol CHAR(3) NOT NULL COMMENT '三爻符号，如 111 表示乾',
    unicode_symbol CHAR(1) NOT NULL COMMENT 'Unicode符号：☰☷☳☴☵☲☶☱',
    nature VARCHAR(10) COMMENT '象征：天、地、雷、风、水、火、山、泽',
    attribute VARCHAR(20) COMMENT '属性：刚健、柔顺、动、入、陷、丽、止、悦',
    direction VARCHAR(10) COMMENT '方位：西北、西南、东、东南、北、南、东北、西',
    season VARCHAR(20) COMMENT '季节',
    family_member VARCHAR(10) COMMENT '家庭成员：父、母、长男、长女、中男、中女、少男、少女',
    body_part VARCHAR(20) COMMENT '身体部位',
    animal VARCHAR(20) COMMENT '动物',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    UNIQUE KEY uk_name (name),
    UNIQUE KEY uk_symbol (symbol),
    INDEX idx_nature (nature)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='八卦基础表';

-- ============================================
-- 2. 六十四卦表（主表，64条记录）
-- ============================================
CREATE TABLE IF NOT EXISTS hexagrams (
    id TINYINT UNSIGNED PRIMARY KEY COMMENT '卦序编号 1-64',
    name VARCHAR(4) NOT NULL COMMENT '卦名',
    symbol CHAR(6) NOT NULL COMMENT '六爻符号，111111表示乾卦（从下到上）',
    unicode_symbol VARCHAR(10) COMMENT 'Unicode卦象符号，如 ☰☰',
    upper_trigram_id TINYINT UNSIGNED NOT NULL COMMENT '上卦ID',
    lower_trigram_id TINYINT UNSIGNED NOT NULL COMMENT '下卦ID',
    full_name VARCHAR(20) COMMENT '完整卦名，如 乾为天',

    -- 卦辞相关
    judgment TEXT NOT NULL COMMENT '卦辞（原文）',
    judgment_explain TEXT COMMENT '卦辞解释',

    -- 象辞相关
    image_text TEXT COMMENT '大象辞原文',
    image_explain TEXT COMMENT '大象辞解释',

    -- 彖辞相关（可选扩展）
    tuan_text TEXT COMMENT '彖辞原文',
    tuan_explain TEXT COMMENT '彖辞解释',

    -- 应用相关
    symbolism VARCHAR(255) COMMENT '象征意涵',
    life_lesson VARCHAR(500) COMMENT '人生启示',
    advice VARCHAR(500) COMMENT '实用建议',

    -- 时间戳
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    UNIQUE KEY uk_name (name),
    UNIQUE KEY uk_symbol (symbol),
    INDEX idx_upper_trigram (upper_trigram_id),
    INDEX idx_lower_trigram (lower_trigram_id),
    INDEX idx_full_name (full_name),

    CONSTRAINT fk_upper_trigram FOREIGN KEY (upper_trigram_id) REFERENCES trigrams(id),
    CONSTRAINT fk_lower_trigram FOREIGN KEY (lower_trigram_id) REFERENCES trigrams(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='六十四卦主表';

-- ============================================
-- 3. 爻辞表（详情表，64×6=384条记录）
-- ============================================
CREATE TABLE IF NOT EXISTS hexagram_lines (
    id SMALLINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '自增主键',
    hexagram_id TINYINT UNSIGNED NOT NULL COMMENT '所属卦ID',
    position TINYINT UNSIGNED NOT NULL COMMENT '爻位 1-6（从下往上）',
    line_name VARCHAR(10) NOT NULL COMMENT '爻名：初九、九二、六三等',
    line_type ENUM('yang', 'yin') NOT NULL COMMENT '爻性：阳爻/阴爻',

    -- 爻辞相关
    line_text TEXT NOT NULL COMMENT '爻辞原文',
    line_explain TEXT COMMENT '爻辞解释',

    -- 小象辞
    image_text TEXT COMMENT '小象辞原文',
    image_explain TEXT COMMENT '小象辞解释',

    -- 扩展字段
    meaning TEXT COMMENT '爻义',
    application TEXT COMMENT '实际应用',

    -- 时间戳
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    UNIQUE KEY uk_hexagram_position (hexagram_id, position),
    INDEX idx_hexagram_id (hexagram_id),
    INDEX idx_line_type (line_type),

    CONSTRAINT fk_hexagram FOREIGN KEY (hexagram_id) REFERENCES hexagrams(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='爻辞详情表';

-- ============================================
-- 4. 常用查询视图
-- ============================================

-- 视图1: 卦象完整信息视图（包含上下卦名称）
CREATE OR REPLACE VIEW v_hexagrams_full AS
SELECT
    h.id,
    h.name,
    h.symbol,
    h.unicode_symbol,
    h.full_name,

    -- 上卦信息
    ut.id AS upper_trigram_id,
    ut.name AS upper_trigram_name,
    ut.symbol AS upper_trigram_symbol,
    ut.unicode_symbol AS upper_trigram_unicode,
    ut.nature AS upper_trigram_nature,
    ut.attribute AS upper_trigram_attribute,

    -- 下卦信息
    lt.id AS lower_trigram_id,
    lt.name AS lower_trigram_name,
    lt.symbol AS lower_trigram_symbol,
    lt.unicode_symbol AS lower_trigram_unicode,
    lt.nature AS lower_trigram_nature,
    lt.attribute AS lower_trigram_attribute,

    -- 卦辞和解释
    h.judgment,
    h.judgment_explain,
    h.image_text,
    h.image_explain,
    h.tuan_text,
    h.tuan_explain,
    h.symbolism,
    h.life_lesson,
    h.advice,

    h.created_at,
    h.updated_at
FROM hexagrams h
INNER JOIN trigrams ut ON h.upper_trigram_id = ut.id
INNER JOIN trigrams lt ON h.lower_trigram_id = lt.id;

-- 视图2: 统计信息视图
CREATE OR REPLACE VIEW v_hexagram_statistics AS
SELECT
    COUNT(DISTINCT id) AS total_hexagrams,
    COUNT(DISTINCT upper_trigram_id) AS used_upper_trigrams,
    COUNT(DISTINCT lower_trigram_id) AS used_lower_trigrams,
    SUM(CASE WHEN judgment IS NOT NULL THEN 1 ELSE 0 END) AS hexagrams_with_judgment,
    SUM(CASE WHEN image_text IS NOT NULL THEN 1 ELSE 0 END) AS hexagrams_with_image
FROM hexagrams;

-- ============================================
-- 5. 全文搜索索引（可选，MySQL 5.7+）
-- ============================================

-- 为卦辞和爻辞添加全文索引，支持中文搜索
ALTER TABLE hexagrams ADD FULLTEXT INDEX ft_idx_judgment (judgment, judgment_explain);
ALTER TABLE hexagrams ADD FULLTEXT INDEX ft_idx_symbolism (symbolism, life_lesson, advice);
ALTER TABLE hexagram_lines ADD FULLTEXT INDEX ft_idx_line_text (line_text, line_explain);

-- ============================================
-- 6. 存储过程 - 查询卦象完整信息（含爻辞）
-- ============================================

DELIMITER //

CREATE PROCEDURE sp_get_hexagram_detail(IN hexagram_id_param TINYINT UNSIGNED)
BEGIN
    -- 查询卦象基本信息
    SELECT * FROM v_hexagrams_full WHERE id = hexagram_id_param;

    -- 查询所有爻辞
    SELECT
        position,
        line_name,
        line_type,
        line_text,
        line_explain,
        image_text,
        image_explain,
        meaning,
        application
    FROM hexagram_lines
    WHERE hexagram_id = hexagram_id_param
    ORDER BY position;
END //

DELIMITER ;

-- ============================================
-- 7. 常用查询示例（作为注释保存）
-- ============================================

-- 示例1: 查询某卦完整信息（含上下卦名称）
-- SELECT * FROM v_hexagrams_full WHERE id = 1;

-- 示例2: 查询某卦的所有爻辞
-- SELECT * FROM hexagram_lines WHERE hexagram_id = 1 ORDER BY position;

-- 示例3: 查询某卦完整数据（JSON格式输出）
/*
SELECT
    h.id, h.name, h.symbol, h.unicode_symbol, h.full_name,
    JSON_OBJECT(
        'id', ut.id,
        'name', ut.name,
        'symbol', ut.symbol,
        'unicode', ut.unicode_symbol,
        'nature', ut.nature,
        'attribute', ut.attribute
    ) AS upper_trigram,
    JSON_OBJECT(
        'id', lt.id,
        'name', lt.name,
        'symbol', lt.symbol,
        'unicode', lt.unicode_symbol,
        'nature', lt.nature,
        'attribute', lt.attribute
    ) AS lower_trigram,
    h.judgment, h.judgment_explain,
    h.symbolism, h.life_lesson, h.advice,
    (SELECT JSON_ARRAYAGG(
        JSON_OBJECT(
            'position', position,
            'line_name', line_name,
            'line_type', line_type,
            'line_text', line_text,
            'line_explain', line_explain,
            'image_text', image_text
        ) ORDER BY position
    ) FROM hexagram_lines WHERE hexagram_id = h.id) AS lines
FROM hexagrams h
INNER JOIN trigrams ut ON h.upper_trigram_id = ut.id
INNER JOIN trigrams lt ON h.lower_trigram_id = lt.id
WHERE h.id = 1;
*/

-- 示例4: 根据二进制符号查询卦象
-- SELECT * FROM v_hexagrams_full WHERE symbol = '111111';

-- 示例5: 查询包含特定上卦或下卦的所有卦象
-- SELECT * FROM v_hexagrams_full WHERE upper_trigram_name = '乾' OR lower_trigram_name = '乾';

-- 示例6: 全文搜索卦辞
-- SELECT id, name, judgment FROM hexagrams WHERE MATCH(judgment, judgment_explain) AGAINST('吉' IN NATURAL LANGUAGE MODE);

-- 示例7: 统计每个八卦作为上卦和下卦的次数
/*
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
*/

-- ============================================
-- 8. 数据完整性检查
-- ============================================

-- 检查是否所有卦象都有6个爻辞
/*
SELECT
    h.id,
    h.name,
    COUNT(hl.id) AS line_count,
    CASE
        WHEN COUNT(hl.id) = 6 THEN '完整'
        ELSE '不完整'
    END AS status
FROM hexagrams h
LEFT JOIN hexagram_lines hl ON h.id = hl.hexagram_id
GROUP BY h.id, h.name
HAVING line_count != 6;
*/

-- 检查是否有孤立的八卦（未被任何卦象使用）
/*
SELECT t.*
FROM trigrams t
WHERE t.id NOT IN (SELECT DISTINCT upper_trigram_id FROM hexagrams)
  AND t.id NOT IN (SELECT DISTINCT lower_trigram_id FROM hexagrams);
*/

-- ============================================
-- 完成
-- ============================================
-- 表结构创建完成
-- 接下来请执行数据初始化脚本:
-- 1. trigrams_init.sql (8条八卦数据)
-- 2. hexagrams_init.sql (64条卦象数据)
-- 3. hexagram_lines_init.sql (384条爻辞数据)
-- ============================================
