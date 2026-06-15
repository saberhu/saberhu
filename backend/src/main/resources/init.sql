








-- ============================================================
-- 阜阳台球约战小程序 - 数据库初始化脚本
-- 兼容 H2 Database (MySQL 模式)
-- ============================================================

-- ----------------------------
-- 1. 用户表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `openid` VARCHAR(64) NOT NULL COMMENT '微信openid',
    `nickname` VARCHAR(64) DEFAULT '' COMMENT '昵称',
    `avatar_url` VARCHAR(512) DEFAULT '' COMMENT '头像URL',
    `phone` VARCHAR(20) DEFAULT '' COMMENT '手机号',
    `gender` TINYINT DEFAULT 0 COMMENT '性别 0-未知 1-男 2-女',
    `level_score` INT DEFAULT 1000 COMMENT '段位积分',
    `wins` INT DEFAULT 0 COMMENT '胜场',
    `losses` INT DEFAULT 0 COMMENT '负场',
    `credit_score` INT DEFAULT 100 COMMENT '信用分',
    `status` TINYINT DEFAULT 1 COMMENT '状态 1-正常 0-禁用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间'
);
CREATE UNIQUE INDEX IF NOT EXISTS `idx_user_openid` ON `user`(`openid`);
CREATE INDEX IF NOT EXISTS `idx_user_level_score` ON `user`(`level_score`);

-- ----------------------------
-- 2. 球房表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `ballroom` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `name` VARCHAR(128) NOT NULL COMMENT '球房名称',
    `address` VARCHAR(256) DEFAULT '' COMMENT '地址',
    `phone` VARCHAR(20) DEFAULT '' COMMENT '联系电话',
    `price_desc` VARCHAR(256) DEFAULT '' COMMENT '价格描述',
    `business_hours` VARCHAR(128) DEFAULT '' COMMENT '营业时间',
    `longitude` DECIMAL(10, 7) DEFAULT 0 COMMENT '经度',
    `latitude` DECIMAL(10, 7) DEFAULT 0 COMMENT '纬度',
    `images` TEXT COMMENT '图片列表(JSON数组)',
    `rating` DECIMAL(2, 1) DEFAULT 5.0 COMMENT '评分',
    `rating_count` INT DEFAULT 0 COMMENT '评价数',
    `status` TINYINT DEFAULT 1 COMMENT '状态 1-营业 0-休息',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间'
);
CREATE INDEX IF NOT EXISTS `idx_ballroom_rating` ON `ballroom`(`rating`);
CREATE INDEX IF NOT EXISTS `idx_ballroom_status` ON `ballroom`(`status`);

-- ----------------------------
-- 3. 约战表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `challenge` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `initiator_id` BIGINT NOT NULL COMMENT '发起人用户ID',
    `ballroom_id` BIGINT NOT NULL COMMENT '球房ID',
    `ball_type` TINYINT DEFAULT 1 COMMENT '球种 1-中式八球 2-斯诺克 3-九球',
    `format_type` TINYINT DEFAULT 1 COMMENT '赛制类型 1-局数 2-分数',
    `format_value` INT DEFAULT 9 COMMENT '赛制值(局数或分数)',
    `start_time` DATETIME NOT NULL COMMENT '开始时间',
    `max_players` INT DEFAULT 2 COMMENT '最大人数',
    `remark` VARCHAR(512) DEFAULT '' COMMENT '备注',
    `status` TINYINT DEFAULT 0 COMMENT '状态 0-待开局 1-进行中 2-已完成 3-已取消',
    `score_initiator` INT DEFAULT NULL COMMENT '发起人得分',
    `score_opponent` INT DEFAULT NULL COMMENT '对手得分',
    `winner_id` BIGINT DEFAULT NULL COMMENT '胜者用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间'
);
CREATE INDEX IF NOT EXISTS `idx_challenge_initiator` ON `challenge`(`initiator_id`);
CREATE INDEX IF NOT EXISTS `idx_challenge_ballroom` ON `challenge`(`ballroom_id`);
CREATE INDEX IF NOT EXISTS `idx_challenge_status` ON `challenge`(`status`);
CREATE INDEX IF NOT EXISTS `idx_challenge_start_time` ON `challenge`(`start_time`);

-- ----------------------------
-- 4. 约战报名表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `challenge_signup` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `challenge_id` BIGINT NOT NULL COMMENT '约战ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `status` TINYINT DEFAULT 0 COMMENT '状态 0-待确认 1-已确认 2-已拒绝',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间'
);
CREATE INDEX IF NOT EXISTS `idx_signup_challenge` ON `challenge_signup`(`challenge_id`);
CREATE INDEX IF NOT EXISTS `idx_signup_user` ON `challenge_signup`(`user_id`);
CREATE UNIQUE INDEX IF NOT EXISTS `idx_signup_unique` ON `challenge_signup`(`challenge_id`, `user_id`);

-- ----------------------------
-- 5. 球房评价表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `ballroom_review` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `ballroom_id` BIGINT NOT NULL COMMENT '球房ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `rating` DECIMAL(2, 1) NOT NULL COMMENT '评分 1-5',
    `content` TEXT COMMENT '评价内容',
    `images` TEXT COMMENT '图片列表(JSON数组)',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间'
);
CREATE INDEX IF NOT EXISTS `idx_review_ballroom` ON `ballroom_review`(`ballroom_id`);
CREATE INDEX IF NOT EXISTS `idx_review_user` ON `ballroom_review`(`user_id`);

-- ----------------------------
-- 6. 球房收藏表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `ballroom_favorite` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `ballroom_id` BIGINT NOT NULL COMMENT '球房ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);
CREATE INDEX IF NOT EXISTS `idx_fav_user` ON `ballroom_favorite`(`user_id`);
CREATE INDEX IF NOT EXISTS `idx_fav_ballroom` ON `ballroom_favorite`(`ballroom_id`);
CREATE UNIQUE INDEX IF NOT EXISTS `idx_fav_unique` ON `ballroom_favorite`(`user_id`, `ballroom_id`);

-- ----------------------------
-- 测试数据
-- ----------------------------
INSERT INTO `user` (`openid`, `nickname`, `avatar_url`, `level_score`, `wins`, `losses`, `credit_score`, `status`)
VALUES ('mock_openid_admin', '测试球友', '', 1200, 10, 3, 100, 1);

INSERT INTO `user` (`openid`, `nickname`, `avatar_url`, `level_score`, `wins`, `losses`, `credit_score`, `status`)
VALUES ('mock_openid_zhang', '张教练', '', 1500, 25, 5, 100, 1);

INSERT INTO `user` (`openid`, `nickname`, `avatar_url`, `level_score`, `wins`, `losses`, `credit_score`, `status`)
VALUES ('mock_openid_li', '李球王', '', 1350, 18, 8, 100, 1);

INSERT INTO `user` (`openid`, `nickname`, `avatar_url`, `level_score`, `wins`, `losses`, `credit_score`, `status`)
VALUES ('mock_openid_wang', '王台球', '', 1100, 8, 6, 100, 1);

INSERT INTO `user` (`openid`, `nickname`, `avatar_url`, `level_score`, `wins`, `losses`, `credit_score`, `status`)
VALUES ('mock_openid_zhao', '赵一杆', '', 900, 5, 12, 100, 1);

INSERT INTO `ballroom` (`name`, `address`, `phone`, `price_desc`, `business_hours`, `longitude`, `latitude`, `rating`, `rating_count`, `status`)
VALUES ('阜阳星牌台球俱乐部', '颍州区清河路128号', '0558-1234567', '30元/小时', '09:00-02:00', 115.814, 32.891, 4.8, 56, 1);

INSERT INTO `ballroom` (`name`, `address`, `phone`, `price_desc`, `business_hours`, `longitude`, `latitude`, `rating`, `rating_count`, `status`)
VALUES ('绅士台球会所', '颍泉区人民路88号', '0558-7654321', '25元/小时', '10:00-01:00', 115.823, 32.898, 4.6, 38, 1);

INSERT INTO `ballroom` (`name`, `address`, `phone`, `price_desc`, `business_hours`, `longitude`, `latitude`, `rating`, `rating_count`, `status`)
VALUES ('鼎力台球俱乐部', '颍东区北京路66号', '0558-5555666', '20元/小时', '09:30-00:00', 115.835, 32.905, 4.5, 42, 1);

INSERT INTO `ballroom` (`name`, `address`, `phone`, `price_desc`, `business_hours`, `longitude`, `latitude`, `rating`, `rating_count`, `status`)
VALUES ('金杆台球馆', '颍州区万达广场3楼', '0558-3334444', '35元/小时', '10:00-22:00', 115.808, 32.886, 4.7, 29, 1);

INSERT INTO `ballroom` (`name`, `address`, `phone`, `price_desc`, `business_hours`, `longitude`, `latitude`, `rating`, `rating_count`, `status`)
VALUES ('名仕台球休闲会所', '颍泉区颍州路56号', '0558-2221111', '28元/小时', '09:00-02:00', 115.828, 32.895, 4.4, 33, 1);

INSERT INTO `challenge` (`initiator_id`, `ballroom_id`, `ball_type`, `format_type`, `format_value`, `start_time`, `max_players`, `remark`, `status`)
VALUES (1, 1, 1, 1, 9, CURRENT_TIMESTAMP + 2, 2, '求虐！', 0);

INSERT INTO `challenge` (`initiator_id`, `ballroom_id`, `ball_type`, `format_type`, `format_value`, `start_time`, `max_players`, `remark`, `status`)
VALUES (2, 2, 2, 2, 100, CURRENT_TIMESTAMP + 5, 2, '斯诺克走起', 0);

INSERT INTO `challenge` (`initiator_id`, `ballroom_id`, `ball_type`, `format_type`, `format_value`, `start_time`, `max_players`, `remark`, `status`)
VALUES (3, 3, 1, 1, 13, CURRENT_TIMESTAMP + 1, 4, '双打缺两人', 0);

INSERT INTO `challenge` (`initiator_id`, `ballroom_id`, `ball_type`, `format_type`, `format_value`, `start_time`, `max_players`, `remark`, `status`)
VALUES (4, 4, 3, 1, 7, CURRENT_TIMESTAMP + 3, 2, '九球娱乐', 0);

INSERT INTO `challenge` (`initiator_id`, `ballroom_id`, `ball_type`, `format_type`, `format_value`, `start_time`, `max_players`, `remark`, `status`)
VALUES (5, 5, 1, 2, 60, CURRENT_TIMESTAMP + 4, 2, '下班后来一局', 0);

INSERT INTO `challenge` (`initiator_id`, `ballroom_id`, `ball_type`, `format_type`, `format_value`, `start_time`, `max_players`, `remark`, `status`)
VALUES (1, 3, 1, 1, 9, CURRENT_TIMESTAMP - 3, 2, '昨晚的局', 2);

INSERT INTO `challenge` (`initiator_id`, `ballroom_id`, `ball_type`, `format_type`, `format_value`, `start_time`, `max_players`, `remark`, `status`)
VALUES (2, 1, 2, 2, 90, CURRENT_TIMESTAMP - 7, 2, '上周斯诺克', 2);

INSERT INTO `challenge` (`initiator_id`, `ballroom_id`, `ball_type`, `format_type`, `format_value`, `start_time`, `max_players`, `remark`, `status`)
VALUES (3, 4, 1, 1, 11, CURRENT_TIMESTAMP - 1, 2, '临时有事取消', 3);

INSERT INTO `challenge_signup` (`challenge_id`, `user_id`, `status`)
VALUES (1, 2, 0);

INSERT INTO `challenge_signup` (`challenge_id`, `user_id`, `status`)
VALUES (2, 1, 1);

INSERT INTO `challenge_signup` (`challenge_id`, `user_id`, `status`)
VALUES (3, 1, 1);

INSERT INTO `challenge_signup` (`challenge_id`, `user_id`, `status`)
VALUES (3, 4, 0);

INSERT INTO `challenge_signup` (`challenge_id`, `user_id`, `status`)
VALUES (4, 5, 0);

INSERT INTO `challenge_signup` (`challenge_id`, `user_id`, `status`)
VALUES (6, 2, 1);

INSERT INTO `challenge_signup` (`challenge_id`, `user_id`, `status`)
VALUES (7, 1, 1);

INSERT INTO `ballroom_review` (`ballroom_id`, `user_id`, `rating`, `content`)
VALUES (1, 1, 5.0, '环境很好，球桌标准，推荐！');

INSERT INTO `ballroom_review` (`ballroom_id`, `user_id`, `rating`, `content`)
VALUES (1, 2, 4.5, '服务态度不错，就是价格稍贵');

INSERT INTO `ballroom_review` (`ballroom_id`, `user_id`, `rating`, `content`)
VALUES (2, 1, 4.0, '球桌保养一般，但氛围很好');

INSERT INTO `ballroom_review` (`ballroom_id`, `user_id`, `rating`, `content`)
VALUES (3, 3, 5.0, '性价比很高，经常来');

INSERT INTO `ballroom_review` (`ballroom_id`, `user_id`, `rating`, `content`)
VALUES (4, 4, 4.5, '新开的球馆，设施很新');

INSERT INTO `ballroom_review` (`ballroom_id`, `user_id`, `rating`, `content`)
VALUES (5, 5, 4.0, '位置好找，停车方便');

INSERT INTO `ballroom_favorite` (`user_id`, `ballroom_id`)
VALUES (1, 1);

INSERT INTO `ballroom_favorite` (`user_id`, `ballroom_id`)
VALUES (1, 3);

INSERT INTO `ballroom_favorite` (`user_id`, `ballroom_id`)
VALUES (2, 2);

INSERT INTO `ballroom_favorite` (`user_id`, `ballroom_id`)
VALUES (3, 3);

INSERT INTO `ballroom_favorite` (`user_id`, `ballroom_id`)
VALUES (4, 4);