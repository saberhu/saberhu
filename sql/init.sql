-- ============================================================
-- 阜阳台球约战小程序 - 数据库初始化脚本
-- 数据库版本: MySQL 8.0
-- ============================================================

CREATE DATABASE IF NOT EXISTS billiards DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE billiards;

-- -----------------------------------------------------------
-- 1. 用户表
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `user` (
    `id`            BIGINT       NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
    `openid`        VARCHAR(64)  NOT NULL                 COMMENT '微信openid，唯一标识',
    `nickname`      VARCHAR(64)  DEFAULT ''               COMMENT '用户昵称',
    `avatar_url`    VARCHAR(512) DEFAULT ''               COMMENT '头像URL',
    `phone`         VARCHAR(20)  DEFAULT ''               COMMENT '手机号',
    `gender`        TINYINT      DEFAULT 0                COMMENT '性别 0-未知 1-男 2-女',
    `level_score`   INT          DEFAULT 1000             COMMENT '段位积分，初始1000',
    `wins`          INT          DEFAULT 0                COMMENT '胜场数',
    `losses`        INT          DEFAULT 0                COMMENT '负场数',
    `credit_score`  INT          DEFAULT 100              COMMENT '信用分，初始100',
    `status`        TINYINT      DEFAULT 1                COMMENT '状态 1-正常 0-禁用',
    `create_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_openid` (`openid`),
    INDEX `idx_level_score` (`level_score`),
    INDEX `idx_credit_score` (`credit_score`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- -----------------------------------------------------------
-- 2. 球房表
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `ballroom` (
    `id`            BIGINT       NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
    `name`          VARCHAR(128) NOT NULL                 COMMENT '球房名称',
    `address`       VARCHAR(256) DEFAULT ''               COMMENT '详细地址',
    `phone`         VARCHAR(32)  DEFAULT ''               COMMENT '联系电话',
    `price_desc`    VARCHAR(256) DEFAULT ''               COMMENT '价格描述，如"25元/小时"',
    `business_hours` VARCHAR(64) DEFAULT ''               COMMENT '营业时间，如"09:00-02:00"',
    `longitude`     DECIMAL(10,7) DEFAULT 0               COMMENT '经度（高德坐标系）',
    `latitude`      DECIMAL(10,7) DEFAULT 0               COMMENT '纬度（高德坐标系）',
    `images`        TEXT                                   COMMENT '图片URL列表，JSON数组',
    `rating`        DECIMAL(2,1) DEFAULT 0.0              COMMENT '综合评分 0.0-5.0',
    `rating_count`  INT          DEFAULT 0                COMMENT '评价人数',
    `status`        TINYINT      DEFAULT 1                COMMENT '状态 1-营业 0-歇业',
    `create_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_location` (`longitude`, `latitude`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='球房表';

-- -----------------------------------------------------------
-- 3. 约战表
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `challenge` (
    `id`            BIGINT       NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
    `initiator_id`  BIGINT       NOT NULL                 COMMENT '发起人用户ID',
    `ballroom_id`   BIGINT       NOT NULL                 COMMENT '球房ID',
    `ball_type`     TINYINT      NOT NULL DEFAULT 1       COMMENT '球种 1-中式八球 2-斯诺克 3-九球',
    `format_type`   TINYINT      NOT NULL DEFAULT 1       COMMENT '赛制 1-抢X 2-限时',
    `format_value`  INT          NOT NULL DEFAULT 5       COMMENT '赛制值，抢X时为局数，限时时为分钟数',
    `start_time`    DATETIME     NOT NULL                 COMMENT '约定开始时间',
    `max_players`   INT          DEFAULT 2                COMMENT '最大参与人数，默认2人',
    `remark`        VARCHAR(256) DEFAULT ''               COMMENT '备注说明',
    `status`        TINYINT      DEFAULT 0                COMMENT '状态 0-待应战 1-进行中 2-已完成 3-已取消',
    `score_initiator` INT       DEFAULT NULL              COMMENT '发起人得分',
    `score_opponent`  INT       DEFAULT NULL              COMMENT '对手得分',
    `winner_id`     BIGINT       DEFAULT NULL             COMMENT '胜者用户ID',
    `create_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_initiator` (`initiator_id`),
    INDEX `idx_ballroom` (`ballroom_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_start_time` (`start_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='约战表';

-- -----------------------------------------------------------
-- 4. 约战报名表
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `challenge_signup` (
    `id`            BIGINT       NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
    `challenge_id`  BIGINT       NOT NULL                 COMMENT '约战ID',
    `user_id`       BIGINT       NOT NULL                 COMMENT '报名用户ID',
    `status`        TINYINT      DEFAULT 0                COMMENT '报名状态 0-待确认 1-已确认 2-已拒绝',
    `create_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_challenge_user` (`challenge_id`, `user_id`),
    INDEX `idx_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='约战报名表';

-- -----------------------------------------------------------
-- 5. 球房评价表
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `ballroom_review` (
    `id`            BIGINT       NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
    `ballroom_id`   BIGINT       NOT NULL                 COMMENT '球房ID',
    `user_id`       BIGINT       NOT NULL                 COMMENT '用户ID',
    `rating`        DECIMAL(2,1) NOT NULL DEFAULT 5.0     COMMENT '评分 1.0-5.0',
    `content`       VARCHAR(512) DEFAULT ''               COMMENT '评价内容',
    `images`        TEXT                                   COMMENT '图片URL列表，JSON数组',
    `create_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_ballroom` (`ballroom_id`),
    INDEX `idx_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='球房评价表';

-- -----------------------------------------------------------
-- 6. 球房收藏表
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `ballroom_favorite` (
    `id`            BIGINT       NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
    `user_id`       BIGINT       NOT NULL                 COMMENT '用户ID',
    `ballroom_id`   BIGINT       NOT NULL                 COMMENT '球房ID',
    `create_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_ballroom` (`user_id`, `ballroom_id`),
    INDEX `idx_ballroom` (`ballroom_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='球房收藏表';

-- -----------------------------------------------------------
-- 初始数据：阜阳本地球房示例
-- -----------------------------------------------------------
INSERT INTO `ballroom` (`name`, `address`, `phone`, `price_desc`, `business_hours`, `longitude`, `latitude`, `rating`) VALUES
('阜阳星牌台球俱乐部', '颍州区清河路100号', '0558-1234567', '30元/小时', '10:00-02:00', 115.8143000, 32.8901000, 4.5),
('鼎力台球会所', '颍泉区人民中路88号', '0558-7654321', '25元/小时', '09:00-01:00', 115.8205000, 32.8952000, 4.2),
('绅士台球馆', '颍东区北京东路66号', '0558-2345678', '20元/小时', '10:00-24:00', 115.8300000, 32.8880000, 4.0);
