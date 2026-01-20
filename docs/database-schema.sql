-- =====================================
-- 易经算卦服务 - 数据库表结构设计
-- =====================================
-- 数据库: yy-yao
-- 版本: 2.0
-- 日期: 2026-01-20
-- 说明: 支持小程序完整功能，包含卦象数据、用户数据、历史记录、收藏管理
-- =====================================

-- 1. 卦象基础信息表
-- 存储64卦的基础信息
CREATE TABLE IF NOT EXISTS hexagram_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    number INT NOT NULL UNIQUE COMMENT '卦序号(1-64)',
    name VARCHAR(50) NOT NULL COMMENT '卦名(乾、坤、屯...)',
    symbol VARCHAR(10) COMMENT '卦符号(☰☷...)',
    unicode_symbol VARCHAR(10) COMMENT 'Unicode卦符号',
    binary_code VARCHAR(6) NOT NULL COMMENT '二进制表示(从下到上, 0=阴, 1=阳)',
    upper_trigram VARCHAR(10) NOT NULL COMMENT '上卦(乾、兑、离、震、巽、坎、艮、坤)',
    lower_trigram VARCHAR(10) NOT NULL COMMENT '下卦(乾、兑、离、震、巽、坎、艮、坤)',
    trigram_nature VARCHAR(100) COMMENT '卦象属性(天、泽、火...)',
    full_name VARCHAR(100) COMMENT '完整卦名',

    -- 卦辞和解释
    statement TEXT COMMENT '卦辞原文',
    interpretation TEXT COMMENT '卦辞白话解释',
    symbolism TEXT COMMENT '卦象象征',
    advice TEXT COMMENT '行事建议',
    meaning TEXT COMMENT '简短释义',

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    INDEX idx_binary_code (binary_code),
    INDEX idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='卦象基础信息表';

-- 2. 爻辞信息表
-- 存储每个卦象的六爻爻辞
CREATE TABLE IF NOT EXISTS line_text (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    hexagram_id BIGINT NOT NULL COMMENT '关联的卦象ID',
    line_position INT NOT NULL COMMENT '爻位(1-6, 从下到上)',
    line_type VARCHAR(20) NOT NULL COMMENT '爻类型(初九、九二、九三、九四、九五、上九...)',
    text TEXT COMMENT '爻辞原文',
    interpretation TEXT COMMENT '爻辞解释',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    UNIQUE KEY uk_hexagram_line (hexagram_id, line_position),
    FOREIGN KEY (hexagram_id) REFERENCES hexagram_info(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='爻辞信息表';

-- 3. 小程序用户表
-- 存储微信小程序用户信息
CREATE TABLE IF NOT EXISTS mini_program_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    openid VARCHAR(100) NOT NULL UNIQUE COMMENT '微信OpenID',
    unionid VARCHAR(100) COMMENT '微信UnionID',
    session_key VARCHAR(100) COMMENT '会话密钥',
    nickname VARCHAR(100) COMMENT '昵称',
    avatar_url VARCHAR(500) COMMENT '头像URL',
    gender TINYINT COMMENT '性别(0=未知, 1=男, 2=女)',
    country VARCHAR(50) COMMENT '国家',
    province VARCHAR(50) COMMENT '省份',
    city VARCHAR(50) COMMENT '城市',
    language VARCHAR(20) COMMENT '语言',

    -- 用户设置
    settings JSON COMMENT '用户设置(JSON格式)',

    -- 统计信息
    total_divinations INT DEFAULT 0 COMMENT '总占卜次数',
    total_favorites INT DEFAULT 0 COMMENT '总收藏数',
    last_divination_at TIMESTAMP NULL COMMENT '最后一次占卜时间',

    -- 状态标识
    is_active BOOLEAN DEFAULT TRUE COMMENT '是否活跃',
    is_blocked BOOLEAN DEFAULT FALSE COMMENT '是否被封禁',

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    last_login_at TIMESTAMP NULL COMMENT '最后登录时间',

    INDEX idx_openid (openid),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='小程序用户表';

-- 4. 占卜记录表
-- 存储所有用户的占卜历史记录
CREATE TABLE IF NOT EXISTS divination_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',

    -- 用户信息
    user_id BIGINT COMMENT '用户ID(如果有系统用户)',
    openid VARCHAR(100) NOT NULL COMMENT '小程序用户OpenID',

    -- 问题和占卜方法
    question VARCHAR(500) COMMENT '问题/所求之事',
    divination_method VARCHAR(20) NOT NULL COMMENT '占卜方法(COIN=摇卦, TIME=梅花易数, NUMBER=手动起卦)',

    -- 本卦信息
    original_hexagram_id BIGINT NOT NULL COMMENT '本卦ID',
    original_hexagram_name VARCHAR(50) NOT NULL COMMENT '本卦名',
    original_hexagram_binary VARCHAR(6) NOT NULL COMMENT '本卦二进制',
    original_hexagram_symbol VARCHAR(10) COMMENT '本卦符号',

    -- 变卦信息(可选)
    changed_hexagram_id BIGINT COMMENT '变卦ID(无变爻时为NULL)',
    changed_hexagram_name VARCHAR(50) COMMENT '变卦名',
    changed_hexagram_binary VARCHAR(6) COMMENT '变卦二进制',
    changed_hexagram_symbol VARCHAR(10) COMMENT '变卦符号',

    -- 爻位信息
    changing_lines JSON COMMENT '动爻列表(JSON数组, 例如:[1,3,5])',
    lines_detail JSON NOT NULL COMMENT '六爻详细信息(JSON数组, 包含每爻的阴阳、是否变爻)',

    -- 解读信息
    interpretation TEXT COMMENT '基础解卦',
    ai_interpretation TEXT COMMENT 'AI智能解读',
    used_ai BOOLEAN DEFAULT FALSE COMMENT '是否使用了AI解读',

    -- 收藏和标签
    is_favorite BOOLEAN DEFAULT FALSE COMMENT '是否收藏',
    favorite_at TIMESTAMP NULL COMMENT '收藏时间',
    tags JSON COMMENT '标签(JSON数组)',
    notes TEXT COMMENT '用户备注',

    -- 元数据
    divination_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '占卜时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    ip_address VARCHAR(50) COMMENT 'IP地址',
    user_agent VARCHAR(500) COMMENT '用户代理',

    INDEX idx_openid (openid),
    INDEX idx_created_at (created_at),
    INDEX idx_divination_time (divination_time),
    INDEX idx_is_favorite (is_favorite),
    INDEX idx_openid_favorite (openid, is_favorite),
    FOREIGN KEY (original_hexagram_id) REFERENCES hexagram_info(id),
    FOREIGN KEY (changed_hexagram_id) REFERENCES hexagram_info(id),
    FOREIGN KEY (openid) REFERENCES mini_program_user(openid) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='占卜记录表';

-- 5. 用户设置表
-- 存储用户的个性化设置
CREATE TABLE IF NOT EXISTS user_settings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    openid VARCHAR(100) NOT NULL UNIQUE COMMENT '小程序用户OpenID',

    -- 外观设置
    theme VARCHAR(20) DEFAULT 'auto' COMMENT '主题(auto=自动, light=浅色, dark=深色)',
    traditional_chinese BOOLEAN DEFAULT FALSE COMMENT '是否使用繁体中文',

    -- 功能设置
    sound_enabled BOOLEAN DEFAULT FALSE COMMENT '是否启用音效',
    vibration_enabled BOOLEAN DEFAULT TRUE COMMENT '是否启用震动',
    enable_ai BOOLEAN DEFAULT TRUE COMMENT '是否启用AI解读',

    -- 隐私设置
    auto_save_history BOOLEAN DEFAULT TRUE COMMENT '是否自动保存历史',
    share_anonymous BOOLEAN DEFAULT TRUE COMMENT '分享时是否匿名',

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    FOREIGN KEY (openid) REFERENCES mini_program_user(openid) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户设置表';

-- 6. 八卦基础信息表
-- 存储八卦的基础信息
CREATE TABLE IF NOT EXISTS trigram_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    name VARCHAR(10) NOT NULL UNIQUE COMMENT '卦名(乾、兑、离、震、巽、坎、艮、坤)',
    number INT NOT NULL UNIQUE COMMENT '卦序(1-8)',
    symbol VARCHAR(5) NOT NULL COMMENT '卦符号(☰☱☲☳☴☵☶☷)',
    binary_code VARCHAR(3) NOT NULL COMMENT '二进制表示(3位)',
    nature VARCHAR(20) NOT NULL COMMENT '属性(天、泽、火、雷、风、水、山、地)',
    attribute VARCHAR(50) COMMENT '特性',
    direction VARCHAR(20) COMMENT '方位',
    season VARCHAR(20) COMMENT '季节',
    family_member VARCHAR(20) COMMENT '家庭成员',
    body_part VARCHAR(50) COMMENT '身体部位',
    animal VARCHAR(50) COMMENT '动物',
    meaning TEXT COMMENT '含义',

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    INDEX idx_name (name),
    INDEX idx_number (number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='八卦基础信息表';

-- 7. 系统配置表
-- 存储全局配置信息
CREATE TABLE IF NOT EXISTS system_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    config_key VARCHAR(100) NOT NULL UNIQUE COMMENT '配置键',
    config_value TEXT COMMENT '配置值',
    config_type VARCHAR(20) NOT NULL DEFAULT 'STRING' COMMENT '值类型(STRING, INT, BOOLEAN, JSON)',
    description VARCHAR(500) COMMENT '描述',
    is_public BOOLEAN DEFAULT FALSE COMMENT '是否公开(可被前端访问)',

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    INDEX idx_config_key (config_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- =====================================
-- 初始化数据
-- =====================================

-- 插入八卦基础数据
INSERT INTO trigram_info (name, number, symbol, binary_code, nature, attribute, direction, season, family_member, body_part, animal, meaning) VALUES
('乾', 1, '☰', '111', '天', '健', '西北', '秋冬之交', '父', '首', '马', '刚健强健、自强不息'),
('兑', 2, '☱', '110', '泽', '悦', '西', '秋', '少女', '口', '羊', '喜悦欢乐、能说会道'),
('离', 3, '☲', '101', '火', '丽', '南', '夏', '中女', '目', '雉', '光明美丽、依附外物'),
('震', 4, '☳', '100', '雷', '动', '东', '春', '长男', '足', '龙', '震动奋起、雷厉风行'),
('巽', 5, '☴', '011', '风', '入', '东南', '春夏之交', '长女', '股', '鸡', '顺从进入、无孔不入'),
('坎', 6, '☵', '010', '水', '陷', '北', '冬', '中男', '耳', '豕', '险陷危险、流动不居'),
('艮', 7, '☶', '001', '山', '止', '东北', '冬春之交', '少男', '手', '狗', '停止静止、沉稳厚重'),
('坤', 8, '☷', '000', '地', '顺', '西南', '夏秋之交', '母', '腹', '牛', '柔顺承载、厚德载物')
ON DUPLICATE KEY UPDATE name=name;

-- 插入系统配置
INSERT INTO system_config (config_key, config_value, config_type, description, is_public) VALUES
('app.name', '云卦师', 'STRING', '应用名称', TRUE),
('app.version', '2.0.0', 'STRING', '应用版本', TRUE),
('app.max_divinations_per_day', '10', 'INT', '每日最大占卜次数', FALSE),
('app.max_favorites', '100', 'INT', '最大收藏数', FALSE),
('app.max_history', '500', 'INT', '最大历史记录数', FALSE),
('ai.enabled', 'true', 'BOOLEAN', '是否启用AI解读', TRUE),
('ai.default_enabled', 'true', 'BOOLEAN', 'AI解读默认是否开启', TRUE)
ON DUPLICATE KEY UPDATE config_key=config_key;

-- =====================================
-- 视图定义
-- =====================================

-- 用户统计视图
CREATE OR REPLACE VIEW v_user_statistics AS
SELECT
    u.id,
    u.openid,
    u.nickname,
    u.total_divinations,
    u.total_favorites,
    u.last_divination_at,
    COUNT(DISTINCT d.id) AS actual_divination_count,
    COUNT(DISTINCT CASE WHEN d.is_favorite = TRUE THEN d.id END) AS actual_favorite_count,
    MAX(d.divination_time) AS last_divination_time,
    u.created_at AS register_time,
    DATEDIFF(NOW(), u.created_at) AS days_since_register
FROM mini_program_user u
LEFT JOIN divination_record d ON u.openid = d.openid
GROUP BY u.id, u.openid, u.nickname, u.total_divinations, u.total_favorites,
         u.last_divination_at, u.created_at;

-- 热门卦象视图
CREATE OR REPLACE VIEW v_popular_hexagrams AS
SELECT
    h.id,
    h.number,
    h.name,
    h.symbol,
    COUNT(d.id) AS divination_count,
    COUNT(CASE WHEN d.is_favorite = TRUE THEN 1 END) AS favorite_count
FROM hexagram_info h
LEFT JOIN divination_record d ON h.id = d.original_hexagram_id
GROUP BY h.id, h.number, h.name, h.symbol
ORDER BY divination_count DESC;

-- =====================================
-- 索引优化说明
-- =====================================
-- 1. openid: 最常用的查询条件
-- 2. created_at: 用于时间范围查询和排序
-- 3. is_favorite: 收藏筛选
-- 4. 组合索引(openid, is_favorite): 优化收藏查询

-- =====================================
-- 数据保留策略
-- =====================================
-- 建议创建定时任务:
-- 1. 清理90天前的未收藏历史记录
-- 2. 清理180天未登录的用户数据(软删除)
-- 3. 归档1年前的历史数据

-- =====================================
-- 备份策略
-- =====================================
-- 1. 每日全量备份 hexagram_info, trigram_info, system_config
-- 2. 每周增量备份 divination_record, mini_program_user
-- 3. 实时备份到从库(主从复制)
