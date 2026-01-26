-- ============================================
-- 易经数据库完整表结构设计
-- ============================================
-- 说明: 包含经文、十翼(传)、用户占卜记录
-- ============================================
-- 创建时间: 2026-01-26
-- 版本: 3.0 Complete Edition
-- ============================================

USE yy_yao;

-- ============================================
-- 1. 八卦表
-- ============================================
CREATE TABLE IF NOT EXISTS trigrams (
    id TINYINT UNSIGNED PRIMARY KEY COMMENT '八卦编号 1-8',
    symbol CHAR(3) NOT NULL COMMENT '三爻符号，如 111 表示乾',
    name VARCHAR(4) NOT NULL COMMENT '卦名：乾、坤、震、巽、坎、离、艮、兑',
    unicode_symbol CHAR(1) NOT NULL COMMENT 'Unicode符号：☰☷☳☴☵☲☶☱',
    nature VARCHAR(10) COMMENT '象征：天、地、雷、风、水、火、山、泽',
    attribute VARCHAR(20) COMMENT '属性：刚健、柔顺、动、入、陷、丽、止、悦',
    direction VARCHAR(10) COMMENT '方位',
    season VARCHAR(20) COMMENT '季节',
    family_member VARCHAR(10) COMMENT '家庭成员：父、母、长男、长女、中男、中女、少男、少女',
    body_part VARCHAR(20) COMMENT '身体部位',
    animal VARCHAR(20) COMMENT '动物',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    UNIQUE KEY uk_symbol (symbol),
    UNIQUE KEY uk_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='八卦基础表';

-- ============================================
-- 2. 六十四卦主表
-- ============================================
CREATE TABLE IF NOT EXISTS hexagrams (
    id TINYINT UNSIGNED PRIMARY KEY COMMENT '卦序编号 1-64',
    symbol CHAR(6) NOT NULL COMMENT '六爻符号，111111表示乾卦（从下到上）',
    name VARCHAR(4) NOT NULL COMMENT '卦名',
    full_name VARCHAR(20) COMMENT '完整卦名，如 乾为天',
    unicode_symbol VARCHAR(10) COMMENT 'Unicode卦象符号',
    upper_trigram_id TINYINT UNSIGNED NOT NULL COMMENT '上卦ID',
    lower_trigram_id TINYINT UNSIGNED NOT NULL COMMENT '下卦ID',
    sequence_number TINYINT UNSIGNED COMMENT '传统卦序',

    -- ========== 卦辞 ==========
    judgment TEXT NOT NULL COMMENT '卦辞原文',
    judgment_annotation TEXT COMMENT '卦辞注释',
    judgment_translation TEXT COMMENT '卦辞白话译文',

    -- ========== 彖传 ==========
    tuan_text TEXT COMMENT '彖传原文',
    tuan_annotation TEXT COMMENT '彖传注释',
    tuan_translation TEXT COMMENT '彖传译文',

    -- ========== 大象传 ==========
    image_text TEXT COMMENT '大象传原文',
    image_annotation TEXT COMMENT '大象传注释',
    image_translation TEXT COMMENT '大象传译文',

    -- ========== 现代解读 ==========
    symbolism VARCHAR(255) COMMENT '象征意涵',
    life_lesson TEXT COMMENT '人生启示',
    advice TEXT COMMENT '实用建议',
    keywords VARCHAR(200) COMMENT '关键词，逗号分隔',

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    UNIQUE KEY uk_symbol (symbol),
    UNIQUE KEY uk_name (name),
    INDEX idx_upper_trigram (upper_trigram_id),
    INDEX idx_lower_trigram (lower_trigram_id),
    INDEX idx_sequence (sequence_number),

    CONSTRAINT fk_hexagram_upper_trigram FOREIGN KEY (upper_trigram_id) REFERENCES trigrams(id),
    CONSTRAINT fk_hexagram_lower_trigram FOREIGN KEY (lower_trigram_id) REFERENCES trigrams(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='六十四卦主表';

-- ============================================
-- 3. 爻辞表
-- ============================================
CREATE TABLE IF NOT EXISTS hexagram_lines (
    id SMALLINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    hexagram_id TINYINT UNSIGNED NOT NULL COMMENT '所属卦ID',
    position TINYINT UNSIGNED NOT NULL COMMENT '爻位 1-6（从下往上）',
    name VARCHAR(10) NOT NULL COMMENT '爻名：初九、九二、六三等',
    type ENUM('yang', 'yin') NOT NULL COMMENT '爻性：阳爻/阴爻',

    -- ========== 爻辞 ==========
    line_text TEXT NOT NULL COMMENT '爻辞原文',
    line_annotation TEXT COMMENT '爻辞注释',
    line_translation TEXT COMMENT '爻辞译文',

    -- ========== 小象传 ==========
    xiang_text TEXT COMMENT '小象传原文',
    xiang_annotation TEXT COMMENT '小象传注释',
    xiang_translation TEXT COMMENT '小象传译文',

    -- ========== 扩展字段 ==========
    meaning TEXT COMMENT '爻义说明',
    application TEXT COMMENT '实际应用',

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    UNIQUE KEY uk_hexagram_position (hexagram_id, position),
    INDEX idx_hexagram_id (hexagram_id),
    INDEX idx_line_type (line_type),

    CONSTRAINT fk_hexagram_line FOREIGN KEY (hexagram_id) REFERENCES hexagrams(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='爻辞详情表';

-- ============================================
-- 4. 文言传（仅乾坤两卦）
-- ============================================
CREATE TABLE IF NOT EXISTS wenyan (
    id TINYINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    hexagram_id TINYINT UNSIGNED NOT NULL COMMENT '1=乾 2=坤',
    section VARCHAR(20) COMMENT '段落标识',
    text TEXT NOT NULL COMMENT '原文',
    annotation TEXT COMMENT '注释',
    translation TEXT COMMENT '译文',
    sort_order TINYINT UNSIGNED COMMENT '排序',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    INDEX idx_hexagram (hexagram_id),
    INDEX idx_sort (sort_order),

    CONSTRAINT fk_wenyan_hexagram FOREIGN KEY (hexagram_id) REFERENCES hexagrams(id),
    CONSTRAINT chk_wenyan_hexagram CHECK (hexagram_id IN (1, 2))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文言传(乾坤专属)';

-- ============================================
-- 5. 系辞传
-- ============================================
CREATE TABLE IF NOT EXISTS xici (
    id SMALLINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    part ENUM('upper', 'lower') NOT NULL COMMENT '上篇/下篇',
    chapter TINYINT UNSIGNED COMMENT '章节号',
    paragraph TINYINT UNSIGNED COMMENT '段落号',
    text TEXT NOT NULL COMMENT '原文',
    annotation TEXT COMMENT '注释',
    translation TEXT COMMENT '译文',
    sort_order SMALLINT UNSIGNED COMMENT '排序',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    INDEX idx_part_chapter (part, chapter),
    INDEX idx_sort (sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系辞传';

-- ============================================
-- 6. 序卦传
-- ============================================
CREATE TABLE IF NOT EXISTS xugua (
    id TINYINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    hexagram_id TINYINT UNSIGNED COMMENT '关联卦象ID',
    text TEXT NOT NULL COMMENT '原文',
    annotation TEXT COMMENT '注释',
    translation TEXT COMMENT '译文',
    sort_order TINYINT UNSIGNED COMMENT '排序',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    INDEX idx_hexagram (hexagram_id),
    INDEX idx_sort (sort_order),

    CONSTRAINT fk_xugua_hexagram FOREIGN KEY (hexagram_id) REFERENCES hexagrams(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='序卦传';

-- ============================================
-- 7. 说卦传
-- ============================================
CREATE TABLE IF NOT EXISTS shuogua (
    id TINYINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    chapter TINYINT UNSIGNED COMMENT '章节号',
    paragraph TINYINT UNSIGNED COMMENT '段落号',
    text TEXT NOT NULL COMMENT '原文',
    annotation TEXT COMMENT '注释',
    translation TEXT COMMENT '译文',
    sort_order TINYINT UNSIGNED COMMENT '排序',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    INDEX idx_chapter (chapter),
    INDEX idx_sort (sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='说卦传';

-- ============================================
-- 8. 杂卦传
-- ============================================
CREATE TABLE IF NOT EXISTS zagua (
    id TINYINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    hexagram_id TINYINT UNSIGNED COMMENT '关联卦象ID',
    text TEXT NOT NULL COMMENT '原文',
    annotation TEXT COMMENT '注释',
    translation TEXT COMMENT '译文',
    sort_order TINYINT UNSIGNED COMMENT '排序',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    INDEX idx_hexagram (hexagram_id),
    INDEX idx_sort (sort_order),

    CONSTRAINT fk_zagua_hexagram FOREIGN KEY (hexagram_id) REFERENCES hexagrams(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='杂卦传';

-- ============================================
-- 9. 用户占卜记录表
-- ============================================
CREATE TABLE IF NOT EXISTS divination_records (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '占卜记录ID',
    user_id BIGINT UNSIGNED NOT NULL COMMENT '用户ID',

    -- ========== 占卜方式 ==========
    method ENUM('coin', 'manual', 'meihua', 'daily', 'qian') DEFAULT 'coin'
        COMMENT '占卜方式：coin=铜钱法, manual=手工, meihua=梅花易数, daily=每日一卦, qian=抽签',

    -- ========== 问题 ==========
    question VARCHAR(500) COMMENT '所问之事',
    category VARCHAR(50) COMMENT '问题分类：事业/感情/健康/学业等',

    -- ========== 摇卦结果 ==========
    lines JSON NOT NULL COMMENT '6爻数值 [7,8,9,7,6,8]',

    -- ========== 卦象 ==========
    original_hexagram_id TINYINT UNSIGNED NOT NULL COMMENT '本卦ID',
    changed_hexagram_id TINYINT UNSIGNED COMMENT '变卦ID（无变爻则为NULL）',
    changing_lines JSON COMMENT '变爻位置 [3,5]',
    changing_line_count TINYINT UNSIGNED DEFAULT 0 COMMENT '变爻数量',

    -- ========== AI解读 ==========
    ai_interpretation TEXT COMMENT 'AI完整解读',
    ai_summary VARCHAR(1000) COMMENT 'AI解读摘要',
    ai_model VARCHAR(50) COMMENT 'AI模型名称',
    ai_model_version VARCHAR(50) COMMENT 'AI模型版本',

    -- ========== 元数据 ==========
    divination_date DATE COMMENT '占卜日期',
    divination_time TIME COMMENT '占卜时间',
    location VARCHAR(100) COMMENT '占卜地点',

    -- ========== 用户反馈 ==========
    is_favorited TINYINT(1) DEFAULT 0 COMMENT '是否收藏',
    user_rating TINYINT UNSIGNED COMMENT '用户评分 1-5',
    user_feedback VARCHAR(500) COMMENT '用户反馈',
    is_accurate TINYINT(1) COMMENT '是否应验',
    accuracy_note TEXT COMMENT '应验情况说明',

    -- ========== 状态 ==========
    is_public TINYINT(1) DEFAULT 0 COMMENT '是否公开',
    status ENUM('active', 'archived', 'deleted') DEFAULT 'active' COMMENT '记录状态',

    -- ========== 时间戳 ==========
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    -- ========== 索引 ==========
    INDEX idx_user_id (user_id),
    INDEX idx_original_hexagram (original_hexagram_id),
    INDEX idx_changed_hexagram (changed_hexagram_id),
    INDEX idx_method (method),
    INDEX idx_category (category),
    INDEX idx_created_at (created_at),
    INDEX idx_status (status),
    INDEX idx_user_created (user_id, created_at),
    INDEX idx_favorited (is_favorited),
    INDEX idx_public (is_public),

    -- ========== 外键约束 ==========
    CONSTRAINT fk_divination_original_hexagram
        FOREIGN KEY (original_hexagram_id) REFERENCES hexagrams(id),
    CONSTRAINT fk_divination_changed_hexagram
        FOREIGN KEY (changed_hexagram_id) REFERENCES hexagrams(id),

    -- ========== 数据约束 ==========
    CONSTRAINT chk_changing_line_count CHECK (changing_line_count BETWEEN 0 AND 6),
    CONSTRAINT chk_user_rating CHECK (user_rating IS NULL OR user_rating BETWEEN 1 AND 5)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户占卜记录表';

-- ============================================
-- 10. 用户表（可选，如果系统需要用户管理）
-- ============================================
CREATE TABLE IF NOT EXISTS users (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    nickname VARCHAR(50) COMMENT '昵称',
    avatar_url VARCHAR(255) COMMENT '头像URL',
    password_hash VARCHAR(255) COMMENT '密码哈希',
    status ENUM('active', 'inactive', 'banned') DEFAULT 'active' COMMENT '状态',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login_at TIMESTAMP NULL COMMENT '最后登录时间',

    UNIQUE KEY uk_username (username),
    UNIQUE KEY uk_email (email),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ============================================
-- 常用查询视图
-- ============================================

-- 视图1: 卦象完整信息视图
CREATE OR REPLACE VIEW v_hexagrams_full AS
SELECT
    h.id,
    h.name,
    h.full_name,
    h.symbol,
    h.unicode_symbol,
    h.sequence_number,

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

    -- 卦辞
    h.judgment,
    h.judgment_annotation,
    h.judgment_translation,

    -- 彖传
    h.tuan_text,
    h.tuan_translation,

    -- 大象
    h.image_text,
    h.image_translation,

    -- 现代解读
    h.symbolism,
    h.life_lesson,
    h.advice,
    h.keywords
FROM hexagrams h
INNER JOIN trigrams ut ON h.upper_trigram_id = ut.id
INNER JOIN trigrams lt ON h.lower_trigram_id = lt.id;

-- 视图2: 占卜记录完整视图
CREATE OR REPLACE VIEW v_divination_records_full AS
SELECT
    dr.id,
    dr.user_id,
    dr.method,
    dr.question,
    dr.category,

    -- 摇卦数据
    dr.lines,
    dr.changing_lines,
    dr.changing_line_count,

    -- 本卦信息
    dr.original_hexagram_id,
    oh.name AS original_hexagram_name,
    oh.full_name AS original_hexagram_full_name,
    oh.symbol AS original_hexagram_symbol,
    oh.judgment AS original_judgment,

    -- 变卦信息
    dr.changed_hexagram_id,
    ch.name AS changed_hexagram_name,
    ch.full_name AS changed_hexagram_full_name,
    ch.symbol AS changed_hexagram_symbol,
    ch.judgment AS changed_judgment,

    -- AI解读
    dr.ai_interpretation,
    dr.ai_summary,
    dr.ai_model,

    -- 用户反馈
    dr.is_favorited,
    dr.user_rating,
    dr.is_accurate,

    -- 元数据
    dr.divination_date,
    dr.divination_time,
    dr.is_public,
    dr.status,
    dr.created_at,
    dr.updated_at
FROM divination_records dr
INNER JOIN hexagrams oh ON dr.original_hexagram_id = oh.id
LEFT JOIN hexagrams ch ON dr.changed_hexagram_id = ch.id
WHERE dr.status != 'deleted';

-- 视图3: 用户占卜统计
CREATE OR REPLACE VIEW v_user_divination_stats AS
SELECT
    user_id,
    COUNT(*) AS total_count,
    COUNT(CASE WHEN changed_hexagram_id IS NOT NULL THEN 1 END) AS has_change_count,
    COUNT(CASE WHEN is_favorited = 1 THEN 1 END) AS favorited_count,
    AVG(user_rating) AS avg_rating,
    COUNT(CASE WHEN is_accurate = 1 THEN 1 END) AS accurate_count,
    MAX(created_at) AS last_divination_at,
    MIN(created_at) AS first_divination_at
FROM divination_records
WHERE status = 'active'
GROUP BY user_id;

-- 视图4: 卦象使用统计
CREATE OR REPLACE VIEW v_hexagram_usage_stats AS
SELECT
    h.id,
    h.name,
    h.full_name,
    COUNT(DISTINCT dr1.id) AS as_original_count,
    COUNT(DISTINCT dr2.id) AS as_changed_count,
    COUNT(DISTINCT dr1.id) + COUNT(DISTINCT dr2.id) AS total_count
FROM hexagrams h
LEFT JOIN divination_records dr1 ON h.id = dr1.original_hexagram_id AND dr1.status = 'active'
LEFT JOIN divination_records dr2 ON h.id = dr2.changed_hexagram_id AND dr2.status = 'active'
GROUP BY h.id, h.name, h.full_name
ORDER BY total_count DESC;

-- ============================================
-- 存储过程
-- ============================================

DELIMITER //

-- 存储过程1: 获取卦象完整信息（含爻辞）
CREATE PROCEDURE sp_get_hexagram_complete(IN hexagram_id_param TINYINT UNSIGNED)
BEGIN
    -- 1. 卦象基本信息
    SELECT * FROM v_hexagrams_full WHERE id = hexagram_id_param;

    -- 2. 六爻信息
    SELECT
        position,
        name,
        type,
        line_text,
        line_annotation,
        line_translation,
        xiang_text,
        xiang_translation,
        meaning
    FROM hexagram_lines
    WHERE hexagram_id = hexagram_id_param
    ORDER BY position;

    -- 3. 文言传（仅乾坤）
    SELECT
        section,
        text,
        annotation,
        translation
    FROM wenyan
    WHERE hexagram_id = hexagram_id_param
    ORDER BY sort_order;
END //

-- 存储过程2: 创建占卜记录
CREATE PROCEDURE sp_create_divination_record(
    IN p_user_id BIGINT UNSIGNED,
    IN p_method VARCHAR(20),
    IN p_question VARCHAR(500),
    IN p_lines JSON,
    IN p_original_hexagram_id TINYINT UNSIGNED,
    IN p_changed_hexagram_id TINYINT UNSIGNED,
    IN p_changing_lines JSON,
    OUT p_record_id BIGINT UNSIGNED
)
BEGIN
    DECLARE v_changing_count TINYINT DEFAULT 0;

    -- 计算变爻数量
    IF p_changing_lines IS NOT NULL THEN
        SET v_changing_count = JSON_LENGTH(p_changing_lines);
    END IF;

    -- 插入记录
    INSERT INTO divination_records (
        user_id,
        method,
        question,
        lines,
        original_hexagram_id,
        changed_hexagram_id,
        changing_lines,
        changing_line_count,
        divination_date,
        divination_time
    ) VALUES (
        p_user_id,
        p_method,
        p_question,
        p_lines,
        p_original_hexagram_id,
        p_changed_hexagram_id,
        p_changing_lines,
        v_changing_count,
        CURRENT_DATE,
        CURRENT_TIME
    );

    SET p_record_id = LAST_INSERT_ID();
END //

-- 存储过程3: 获取占卜记录详情
CREATE PROCEDURE sp_get_divination_detail(IN record_id_param BIGINT UNSIGNED)
BEGIN
    -- 1. 占卜记录基本信息
    SELECT * FROM v_divination_records_full WHERE id = record_id_param;

    -- 2. 变爻的爻辞信息
    SELECT
        hl.position,
        hl.name,
        hl.type,
        hl.line_text,
        hl.line_translation,
        hl.xiang_text,
        hl.xiang_translation
    FROM divination_records dr
    INNER JOIN hexagram_lines hl ON dr.original_hexagram_id = hl.hexagram_id
    WHERE dr.id = record_id_param
      AND JSON_CONTAINS(dr.changing_lines, CAST(hl.position AS JSON))
    ORDER BY hl.position;
END //

DELIMITER ;

-- ============================================
-- 全文搜索索引
-- ============================================

ALTER TABLE hexagrams ADD FULLTEXT INDEX ft_idx_judgment (judgment, judgment_translation);
ALTER TABLE hexagrams ADD FULLTEXT INDEX ft_idx_content (symbolism, life_lesson, advice);
ALTER TABLE hexagram_lines ADD FULLTEXT INDEX ft_idx_line (line_text, line_translation);

-- ============================================
-- 数据完整性检查查询
-- ============================================

-- 检查每个卦象是否都有6个爻辞
/*
SELECT
    h.id,
    h.name,
    COUNT(hl.id) AS line_count,
    CASE WHEN COUNT(hl.id) = 6 THEN '✅' ELSE '❌' END AS status
FROM hexagrams h
LEFT JOIN hexagram_lines hl ON h.id = hl.hexagram_id
GROUP BY h.id, h.name
HAVING line_count != 6;
*/

-- ============================================
-- 完成
-- ============================================