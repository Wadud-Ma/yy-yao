-- ================================================
-- 易经卜卦服务 - 数据库初始化脚本
-- ================================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `yy-yao`
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE `yy-yao`;

-- 显示成功消息
SELECT '数据库 yy-yao 已创建成功!' AS message;
