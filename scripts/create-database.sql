-- 创建易经卜卦系统数据库

CREATE DATABASE IF NOT EXISTS `yy-yao`
DEFAULT CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE `yy-yao`;

-- 注意: 表结构由Hibernate自动创建(spring.jpa.hibernate.ddl-auto=update)
-- 以下是表结构的参考，实际会由JPA自动生成

-- 1. classic_text - 原文表
-- 2. annotation - 注释表
-- 3. translation - 译文表
-- 4. hexagram_info - 卦象信息表
-- 5. hexagram_text - 卦象文本表
-- 6. line_text - 爻辞表
-- 7. trigram_info - 八卦信息表

-- 验证数据库创建
SELECT 'Database yy-yao created successfully!' AS message;
