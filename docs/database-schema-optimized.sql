-- ============================================
-- 易经卜卦系统 - 优化后完整数据库架构
-- 版本: V2.0 优化版
-- 日期: 2026-01-26
-- 说明: 基于新的扁平化表结构,移除冗余表,简化架构
-- ============================================

-- ============================================
-- 1. 八卦表 (trigrams)
-- ============================================
CREATE TABLE trigrams (
    id TINYINT UNSIGNED PRIMARY KEY COMMENT '八卦编号 1-8',
    symbol CHAR(3) NOT NULL COMMENT '三爻符号 如 111',
    name VARCHAR(4) NOT NULL COMMENT '卦名: 乾坤震巽坎离艮兑',
    unicode_symbol CHAR(1) NOT NULL COMMENT 'Unicode符号: ☰☷☳☴☵☲☶☱',
    nature VARCHAR(10) COMMENT '象征: 天地雷风水火山泽',
    attribute VARCHAR(20) COMMENT '属性: 刚健、柔顺、动、入、陷、丽、止、悦',
    direction VARCHAR(10) COMMENT '方位',
    season VARCHAR(20) COMMENT '季节',
    family_member VARCHAR(10) COMMENT '家庭成员: 父母长男长女等',
    body_part VARCHAR(20) COMMENT '身体部位',
    animal VARCHAR(20) COMMENT '动物',
    UNIQUE KEY uk_symbol (symbol)
) COMMENT '八卦基础数据';

-- ============================================
-- 2. 六十四卦主表 (hexagrams)
-- ============================================
CREATE TABLE hexagrams (
    id TINYINT UNSIGNED PRIMARY KEY COMMENT '卦序 1-64',
    symbol CHAR(6) NOT NULL COMMENT '六爻符号 从下到上',
    name VARCHAR(4) NOT NULL COMMENT '卦名',
    unicode_symbol VARCHAR(10) COMMENT 'Unicode卦象符号',
    full_name VARCHAR(20) COMMENT '完整卦名 如:乾为天',
    upper_trigram_id TINYINT UNSIGNED NOT NULL COMMENT '上卦ID',
    lower_trigram_id TINYINT UNSIGNED NOT NULL COMMENT '下卦ID',

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
    advice TEXT COMMENT '实用建议',

    UNIQUE KEY uk_symbol (symbol),
    KEY idx_upper_trigram (upper_trigram_id),
    KEY idx_lower_trigram (lower_trigram_id),
    FOREIGN KEY (upper_trigram_id) REFERENCES trigrams(id),
    FOREIGN KEY (lower_trigram_id) REFERENCES trigrams(id)
) COMMENT '六十四卦完整数据';

-- ============================================
-- 3. 爻辞表 (hexagram_lines)
-- ============================================
CREATE TABLE hexagram_lines (
    id SMALLINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    hexagram_id TINYINT UNSIGNED NOT NULL COMMENT '所属卦ID',
    position TINYINT UNSIGNED NOT NULL COMMENT '爻位 1-6',
    name VARCHAR(10) NOT NULL COMMENT '爻名: 初九、六二等',
    type ENUM('yang', 'yin') NOT NULL COMMENT '爻性',

    -- 爻辞
    line_text TEXT NOT NULL COMMENT '爻辞原文',
    line_annotation TEXT COMMENT '爻辞注释',
    line_translation TEXT COMMENT '爻辞译文',

    -- 小象
    xiang_text TEXT COMMENT '小象原文',
    xiang_annotation TEXT COMMENT '小象注释',
    xiang_translation TEXT COMMENT '小象译文',

    UNIQUE KEY uk_hexagram_position (hexagram_id, position),
    KEY idx_hexagram (hexagram_id),
    FOREIGN KEY (hexagram_id) REFERENCES hexagrams(id)
) COMMENT '爻辞详细数据';

-- ============================================
-- 4. 文言传 (wenyan) - 仅乾坤两卦
-- ============================================
CREATE TABLE wenyan (
    id TINYINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    hexagram_id TINYINT UNSIGNED NOT NULL COMMENT '卦ID: 1=乾 2=坤',
    section VARCHAR(20) COMMENT '段落标识',
    text TEXT NOT NULL COMMENT '原文',
    annotation TEXT COMMENT '注释',
    translation TEXT COMMENT '译文',
    sort_order TINYINT UNSIGNED COMMENT '排序',
    KEY idx_hexagram (hexagram_id),
    FOREIGN KEY (hexagram_id) REFERENCES hexagrams(id)
) COMMENT '文言传 - 专门解说乾坤两卦';

-- ============================================
-- 5. 系辞传 (xici)
-- ============================================
CREATE TABLE xici (
    id SMALLINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    part ENUM('upper', 'lower') NOT NULL COMMENT '上篇/下篇',
    chapter TINYINT UNSIGNED COMMENT '章节',
    text TEXT NOT NULL COMMENT '原文',
    annotation TEXT COMMENT '注释',
    translation TEXT COMMENT '译文',
    sort_order SMALLINT UNSIGNED,
    KEY idx_part (part)
) COMMENT '系辞传 - 阐述易理哲学';

-- ============================================
-- 6. 序卦传 (xugua)
-- ============================================
CREATE TABLE xugua (
    id TINYINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    hexagram_id TINYINT UNSIGNED COMMENT '关联卦',
    text TEXT NOT NULL COMMENT '原文',
    annotation TEXT COMMENT '注释',
    translation TEXT COMMENT '译文',
    sort_order TINYINT UNSIGNED,
    KEY idx_hexagram (hexagram_id),
    FOREIGN KEY (hexagram_id) REFERENCES hexagrams(id)
) COMMENT '序卦传 - 解释卦序排列';

-- ============================================
-- 7. 说卦传 (shuogua)
-- ============================================
CREATE TABLE shuogua (
    id TINYINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    chapter TINYINT UNSIGNED COMMENT '章节',
    text TEXT NOT NULL COMMENT '原文',
    annotation TEXT COMMENT '注释',
    translation TEXT COMMENT '译文',
    sort_order TINYINT UNSIGNED
) COMMENT '说卦传 - 说明八卦取象';

-- ============================================
-- 8. 杂卦传 (zagua)
-- ============================================
CREATE TABLE zagua (
    id TINYINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    hexagram_id TINYINT UNSIGNED COMMENT '关联卦',
    text TEXT NOT NULL COMMENT '原文',
    annotation TEXT COMMENT '注释',
    translation TEXT COMMENT '译文',
    sort_order TINYINT UNSIGNED,
    KEY idx_hexagram (hexagram_id),
    FOREIGN KEY (hexagram_id) REFERENCES hexagrams(id)
) COMMENT '杂卦传 - 对比说明卦义';

-- ============================================
-- 9. 用户表 (users)
-- ============================================
CREATE TABLE users (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码(加密)',
    email VARCHAR(100) UNIQUE COMMENT '邮箱',
    role ENUM('USER', 'ADMIN') DEFAULT 'USER' COMMENT '角色',
    enabled TINYINT(1) DEFAULT 1 COMMENT '是否启用',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_username (username),
    KEY idx_email (email)
) COMMENT '用户账号表';

-- ============================================
-- 10. 卜卦记录表 (divination_records)
-- ============================================
CREATE TABLE divination_records (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    method ENUM('coin', 'manual', 'meihua', 'daily', 'qian') COMMENT '占卜方式',
    question VARCHAR(500) COMMENT '所问之事',
    lines JSON COMMENT '六爻数值 [7,8,9,7,6,8]',
    original_hexagram_id TINYINT UNSIGNED NOT NULL COMMENT '本卦ID',
    changed_hexagram_id TINYINT UNSIGNED COMMENT '变卦ID',
    changing_lines JSON COMMENT '变爻位置 [3,5]',
    ai_interpretation TEXT COMMENT 'AI解读',
    is_favorited TINYINT(1) DEFAULT 0 COMMENT '是否收藏',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    INDEX idx_user (user_id),
    INDEX idx_created (created_at),
    INDEX idx_original_hexagram (original_hexagram_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (original_hexagram_id) REFERENCES hexagrams(id),
    FOREIGN KEY (changed_hexagram_id) REFERENCES hexagrams(id)
) COMMENT '用户卜卦记录';

-- ============================================
-- 索引优化建议
-- ============================================
-- 如果查询性能需要,可以添加以下复合索引:
-- ALTER TABLE divination_records ADD INDEX idx_user_created (user_id, created_at);
-- ALTER TABLE divination_records ADD INDEX idx_user_favorited (user_id, is_favorited);
-- ALTER TABLE hexagram_lines ADD INDEX idx_type (type);

-- ============================================
-- 表关系说明
-- ============================================
-- trigrams (8条)
--     ↓ 1:N
-- hexagrams (64条)
--     ↓ 1:6
-- hexagram_lines (384条)
--
-- hexagrams ← wenyan (仅乾坤)
-- hexagrams ← xugua (卦序说明)
-- hexagrams ← zagua (卦义对比)
--
-- xici (独立篇章)
-- shuogua (独立篇章)
--
-- users (用户) → divination_records (卜卦记录)
--
-- ============================================
-- 数据统计
-- ============================================
-- 八卦: 8条
-- 六十四卦: 64条
-- 爻辞: 384条 (64卦 × 6爻)
-- 文言传: 约20-30段 (仅乾坤)
-- 系辞传: 约100-150段
-- 序卦传: 64条
-- 说卦传: 约20-30段
-- 杂卦传: 64条
-- 总计核心数据: 约800-900条记录
