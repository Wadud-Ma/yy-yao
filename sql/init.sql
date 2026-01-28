-- ============================================
-- 1. 八卦表
-- ============================================
DROP TABLE IF EXISTS trigrams;
CREATE TABLE trigrams (
                          id TINYINT UNSIGNED PRIMARY KEY,
                          symbol CHAR(3) NOT NULL COMMENT '如 111',
                          name VARCHAR(4) NOT NULL COMMENT '乾坤震巽坎离艮兑',
                          nature VARCHAR(10) COMMENT '天地雷风水火山泽',
                          attribute VARCHAR(20) COMMENT '刚健、柔顺等',
                          UNIQUE KEY uk_symbol (symbol)
) COMMENT '八卦';

-- ============================================
-- 2. 六十四卦主表
-- ============================================
DROP TABLE IF EXISTS hexagrams;
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
                           advice TEXT COMMENT '实用建议',

                           UNIQUE KEY uk_symbol (symbol)
) COMMENT '六十四卦';

-- ============================================
-- 3. 爻辞表
-- ============================================
DROP TABLE IF EXISTS hexagram_lines;
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
                                xiang_translation TEXT COMMENT '小象译文',

                                UNIQUE KEY uk_hexagram_position (hexagram_id, position)
) COMMENT '爻辞';

-- ============================================
-- 4. 文言传（仅乾坤两卦）
-- ============================================
DROP TABLE IF EXISTS wenyan;
CREATE TABLE wenyan (
                        id TINYINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
                        hexagram_id TINYINT UNSIGNED NOT NULL COMMENT '1=乾 2=坤',
                        section VARCHAR(20) COMMENT '段落标识',
                        text TEXT NOT NULL COMMENT '原文',
                        annotation TEXT COMMENT '注释',
                        translation TEXT COMMENT '译文',
                        sort_order TINYINT UNSIGNED COMMENT '排序'
) COMMENT '文言传';

-- ============================================
-- 5. 系辞传
-- ============================================
DROP TABLE IF EXISTS xici;
CREATE TABLE xici (
                      id SMALLINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
                      part ENUM('upper', 'lower') NOT NULL COMMENT '上篇/下篇',
                      chapter TINYINT UNSIGNED COMMENT '章节',
                      text TEXT NOT NULL COMMENT '原文',
                      annotation TEXT COMMENT '注释',
                      translation TEXT COMMENT '译文',
                      sort_order SMALLINT UNSIGNED
) COMMENT '系辞传';

-- ============================================
-- 6. 序卦传
-- ============================================
DROP TABLE IF EXISTS xugua;
CREATE TABLE xugua (
                       id TINYINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
                       hexagram_id TINYINT UNSIGNED COMMENT '关联卦',
                       text TEXT NOT NULL COMMENT '原文',
                       annotation TEXT COMMENT '注释',
                       translation TEXT COMMENT '译文',
                       sort_order TINYINT UNSIGNED
) COMMENT '序卦传';

-- ============================================
-- 7. 说卦传
-- ============================================
DROP TABLE IF EXISTS shuogua;
CREATE TABLE shuogua (
                         id TINYINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
                         chapter TINYINT UNSIGNED COMMENT '章节',
                         text TEXT NOT NULL COMMENT '原文',
                         annotation TEXT COMMENT '注释',
                         translation TEXT COMMENT '译文',
                         sort_order TINYINT UNSIGNED
) COMMENT '说卦传';

-- ============================================
-- 8. 杂卦传
-- ============================================
DROP TABLE IF EXISTS zagua;
CREATE TABLE zagua (
                       id TINYINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
                       hexagram_id TINYINT UNSIGNED COMMENT '关联卦',
                       text TEXT NOT NULL COMMENT '原文',
                       annotation TEXT COMMENT '注释',
                       translation TEXT COMMENT '译文',
                       sort_order TINYINT UNSIGNED
) COMMENT '杂卦传';

-- ============================================
-- 9. 用户卜卦记录
-- ============================================
DROP TABLE IF EXISTS divination_records;
CREATE TABLE divination_records (
                                    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
                                    user_id BIGINT UNSIGNED NOT NULL,
                                    method ENUM('coin', 'manual', 'meihua', 'daily', 'qian') COMMENT '方式',
                                    question VARCHAR(500),
                                    line_values JSON COMMENT '[7,8,9,7,6,8]',
                                    original_hexagram_id TINYINT UNSIGNED NOT NULL,
                                    changed_hexagram_id TINYINT UNSIGNED,
                                    changing_lines JSON COMMENT '[3,5]',
                                    ai_interpretation TEXT,
                                    is_favorited TINYINT(1) DEFAULT 0,
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                    INDEX idx_user (user_id),
                                    INDEX idx_created (created_at)
) COMMENT '卜卦记录';
