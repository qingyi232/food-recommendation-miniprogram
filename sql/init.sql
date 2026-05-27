-- =============================================
-- 基于微信小程序的校园周边美食推荐系统 数据库初始化脚本
-- =============================================

CREATE DATABASE IF NOT EXISTS campus_food DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE campus_food;

-- 1. 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `openid` VARCHAR(64) DEFAULT NULL COMMENT '微信openid',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码',
  `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `gender` TINYINT DEFAULT 0 COMMENT '性别 0未知 1男 2女',
  `status` TINYINT DEFAULT 1 COMMENT '状态 0禁用 1正常',
  `taste_preference` VARCHAR(500) DEFAULT NULL COMMENT '口味偏好(JSON)',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_openid` (`openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 2. 商家表
DROP TABLE IF EXISTS `merchant`;
CREATE TABLE `merchant` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '商家ID',
  `username` VARCHAR(50) NOT NULL COMMENT '登录用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码',
  `name` VARCHAR(100) NOT NULL COMMENT '商家名称',
  `contact_name` VARCHAR(50) DEFAULT NULL COMMENT '联系人',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '商家头像',
  `status` TINYINT DEFAULT 0 COMMENT '状态 0待审核 1正常 2禁用',
  `violation_count` INT DEFAULT 0 COMMENT '违规次数',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商家表';

-- 3. 管理员表
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码',
  `name` VARCHAR(50) DEFAULT NULL COMMENT '姓名',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像',
  `role` VARCHAR(20) DEFAULT 'admin' COMMENT '角色',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 4. 美食分类表
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
  `icon` VARCHAR(255) DEFAULT NULL COMMENT '分类图标',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `status` TINYINT DEFAULT 1 COMMENT '状态 0禁用 1正常',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='美食分类表';

-- 5. 店铺表
DROP TABLE IF EXISTS `shop`;
CREATE TABLE `shop` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '店铺ID',
  `merchant_id` BIGINT NOT NULL COMMENT '商家ID',
  `name` VARCHAR(100) NOT NULL COMMENT '店铺名称',
  `logo` VARCHAR(255) DEFAULT NULL COMMENT '店铺Logo',
  `images` TEXT DEFAULT NULL COMMENT '店铺图片(JSON数组)',
  `description` TEXT DEFAULT NULL COMMENT '店铺描述',
  `address` VARCHAR(255) DEFAULT NULL COMMENT '店铺地址',
  `longitude` DECIMAL(10,7) DEFAULT NULL COMMENT '经度',
  `latitude` DECIMAL(10,7) DEFAULT NULL COMMENT '纬度',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
  `business_hours` VARCHAR(100) DEFAULT NULL COMMENT '营业时间',
  `avg_price` DECIMAL(10,2) DEFAULT NULL COMMENT '人均消费',
  `rating` DECIMAL(3,2) DEFAULT 0.00 COMMENT '评分',
  `total_sales` INT DEFAULT 0 COMMENT '总销量',
  `category_id` BIGINT DEFAULT NULL COMMENT '分类ID',
  `tags` VARCHAR(255) DEFAULT NULL COMMENT '标签(逗号分隔)',
  `status` TINYINT DEFAULT 1 COMMENT '状态 0关闭 1营业',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_merchant_id` (`merchant_id`),
  KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='店铺表';

-- 6. 菜品表
DROP TABLE IF EXISTS `dish`;
CREATE TABLE `dish` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '菜品ID',
  `shop_id` BIGINT NOT NULL COMMENT '店铺ID',
  `name` VARCHAR(100) NOT NULL COMMENT '菜品名称',
  `image` VARCHAR(255) DEFAULT NULL COMMENT '菜品图片',
  `description` TEXT DEFAULT NULL COMMENT '菜品描述',
  `price` DECIMAL(10,2) NOT NULL COMMENT '价格',
  `original_price` DECIMAL(10,2) DEFAULT NULL COMMENT '原价',
  `category_id` BIGINT DEFAULT NULL COMMENT '分类ID',
  `tags` VARCHAR(255) DEFAULT NULL COMMENT '标签',
  `spicy_level` TINYINT DEFAULT 0 COMMENT '辣度 0不辣 1微辣 2中辣 3特辣',
  `sales` INT DEFAULT 0 COMMENT '销量',
  `rating` DECIMAL(3,2) DEFAULT 0.00 COMMENT '评分',
  `status` TINYINT DEFAULT 1 COMMENT '状态 0下架 1上架',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_shop_id` (`shop_id`),
  KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜品表';

-- 7. 订单表
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` VARCHAR(64) NOT NULL COMMENT '订单编号',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `shop_id` BIGINT NOT NULL COMMENT '店铺ID',
  `merchant_id` BIGINT NOT NULL COMMENT '商家ID',
  `total_amount` DECIMAL(10,2) NOT NULL COMMENT '总金额',
  `status` TINYINT DEFAULT 0 COMMENT '状态 0待确认 1已确认 2已完成 3已取消',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_shop_id` (`shop_id`),
  KEY `idx_merchant_id` (`merchant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 8. 订单明细表
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `order_id` BIGINT NOT NULL COMMENT '订单ID',
  `dish_id` BIGINT NOT NULL COMMENT '菜品ID',
  `dish_name` VARCHAR(100) DEFAULT NULL COMMENT '菜品名称',
  `dish_image` VARCHAR(255) DEFAULT NULL COMMENT '菜品图片',
  `price` DECIMAL(10,2) NOT NULL COMMENT '单价',
  `quantity` INT NOT NULL DEFAULT 1 COMMENT '数量',
  `amount` DECIMAL(10,2) NOT NULL COMMENT '小计金额',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';

-- 9. 评价表
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '评价ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `shop_id` BIGINT NOT NULL COMMENT '店铺ID',
  `dish_id` BIGINT DEFAULT NULL COMMENT '菜品ID',
  `order_id` BIGINT DEFAULT NULL COMMENT '订单ID',
  `content` TEXT DEFAULT NULL COMMENT '评价内容',
  `rating` TINYINT NOT NULL COMMENT '评分 1-5',
  `images` TEXT DEFAULT NULL COMMENT '评价图片(JSON数组)',
  `taste_rating` TINYINT DEFAULT NULL COMMENT '口味评分',
  `service_rating` TINYINT DEFAULT NULL COMMENT '服务评分',
  `environment_rating` TINYINT DEFAULT NULL COMMENT '环境评分',
  `status` TINYINT DEFAULT 1 COMMENT '状态 0隐藏 1正常 2违规',
  `reply` TEXT DEFAULT NULL COMMENT '商家回复',
  `reply_time` DATETIME DEFAULT NULL COMMENT '回复时间',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_shop_id` (`shop_id`),
  KEY `idx_dish_id` (`dish_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评价表';

-- 10. 收藏店铺表
DROP TABLE IF EXISTS `favorite_shop`;
CREATE TABLE `favorite_shop` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `shop_id` BIGINT NOT NULL COMMENT '店铺ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_shop` (`user_id`, `shop_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏店铺表';

-- 11. 收藏美食表
DROP TABLE IF EXISTS `favorite_food`;
CREATE TABLE `favorite_food` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `dish_id` BIGINT NOT NULL COMMENT '菜品ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_dish` (`user_id`, `dish_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏美食表';

-- 12. 分享记录表
DROP TABLE IF EXISTS `share_record`;
CREATE TABLE `share_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `target_type` VARCHAR(20) NOT NULL COMMENT '分享类型 shop/dish',
  `target_id` BIGINT NOT NULL COMMENT '目标ID',
  `share_type` VARCHAR(20) DEFAULT NULL COMMENT '分享渠道',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '分享时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分享记录表';

-- 13. 用户行为记录表(用于个性化推荐)
DROP TABLE IF EXISTS `user_behavior`;
CREATE TABLE `user_behavior` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `behavior_type` VARCHAR(20) NOT NULL COMMENT '行为类型 view/click/order/review/favorite/share',
  `target_type` VARCHAR(20) NOT NULL COMMENT '目标类型 shop/dish/category',
  `target_id` BIGINT NOT NULL COMMENT '目标ID',
  `score` DECIMAL(5,2) DEFAULT 1.00 COMMENT '行为权重分',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_target` (`target_type`, `target_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户行为记录表';

-- 14. 违规记录表
DROP TABLE IF EXISTS `violation_record`;
CREATE TABLE `violation_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `target_type` VARCHAR(20) NOT NULL COMMENT '违规对象类型 user/merchant',
  `target_id` BIGINT NOT NULL COMMENT '违规对象ID',
  `reason` TEXT NOT NULL COMMENT '违规原因',
  `action` VARCHAR(50) NOT NULL COMMENT '处理措施 warn/disable/ban',
  `admin_id` BIGINT DEFAULT NULL COMMENT '处理管理员ID',
  `status` TINYINT DEFAULT 1 COMMENT '状态 0已撤销 1生效中',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '处理时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_target` (`target_type`, `target_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='违规记录表';

-- =============================================
-- 初始数据
-- =============================================

-- 管理员账号 (密码: admin123, MD5加密)
INSERT INTO `admin` (`username`, `password`, `name`, `role`) VALUES
('admin', '0192023a7bbd73250516f069df18b500', '系统管理员', 'admin');

-- 美食分类
INSERT INTO `category` (`name`, `icon`, `sort_order`) VALUES
('中餐', '/images/category/chinese.png', 1),
('西餐', '/images/category/western.png', 2),
('日韩料理', '/images/category/japanese.png', 3),
('快餐简餐', '/images/category/fast.png', 4),
('火锅烧烤', '/images/category/hotpot.png', 5),
('小吃零食', '/images/category/snack.png', 6),
('饮品甜点', '/images/category/drink.png', 7),
('面食粉丝', '/images/category/noodle.png', 8),
('东南亚菜', '/images/category/sea.png', 9),
('其他', '/images/category/other.png', 10);

-- 测试商家 (密码: 123456)
INSERT INTO `merchant` (`username`, `password`, `name`, `contact_name`, `phone`, `status`) VALUES
('merchant01', 'e10adc3949ba59abbe56e057f20f883e', '校园美食坊', '张三', '13800138001', 1),
('merchant02', 'e10adc3949ba59abbe56e057f20f883e', '学府小厨', '李四', '13800138002', 1);

-- 测试店铺
INSERT INTO `shop` (`merchant_id`, `name`, `logo`, `description`, `address`, `phone`, `business_hours`, `avg_price`, `rating`, `category_id`, `tags`, `status`) VALUES
(1, '校园美食坊', 'https://picsum.photos/seed/shop1/400/300', '校门口最受欢迎的美食店，主营各类中式快餐', '大学东门100米', '13800138001', '08:00-22:00', 18.00, 4.50, 1, '中餐,快餐,实惠', 1),
(1, '鲜果茶语', 'https://picsum.photos/seed/shop2/400/300', '新鲜水果现榨果汁，各类奶茶饮品', '大学南门50米', '13800138001', '09:00-23:00', 12.00, 4.70, 7, '饮品,奶茶,果汁', 1),
(2, '学府小厨', 'https://picsum.photos/seed/shop3/400/300', '家常菜馆，味道正宗，分量足', '大学西门200米', '13800138002', '10:00-21:00', 25.00, 4.30, 1, '中餐,家常菜,实惠', 1),
(2, '辣么香火锅', 'https://picsum.photos/seed/shop4/400/300', '正宗川味火锅，麻辣鲜香', '大学北门300米', '13800138002', '11:00-02:00', 60.00, 4.60, 5, '火锅,川菜,辣', 1);

-- 测试菜品
INSERT INTO `dish` (`shop_id`, `name`, `image`, `description`, `price`, `category_id`, `tags`, `spicy_level`, `sales`, `rating`, `status`) VALUES
(1, '红烧肉饭', 'https://picsum.photos/seed/dish1/400/300', '肥瘦相间，入口即化', 15.00, 1, '米饭,肉类', 0, 520, 4.60, 1),
(1, '宫保鸡丁饭', 'https://picsum.photos/seed/dish2/400/300', '经典川菜，鸡肉嫩滑', 14.00, 1, '米饭,鸡肉', 1, 380, 4.50, 1),
(1, '番茄鸡蛋面', 'https://picsum.photos/seed/dish3/400/300', '酸甜可口，家的味道', 12.00, 8, '面条,素食', 0, 290, 4.40, 1),
(2, '珍珠奶茶', 'https://picsum.photos/seed/dish4/400/300', 'Q弹珍珠，醇香奶茶', 10.00, 7, '奶茶,冷饮', 0, 860, 4.80, 1),
(2, '芒果沙冰', 'https://picsum.photos/seed/dish5/400/300', '新鲜芒果，冰爽清凉', 15.00, 7, '果汁,冷饮', 0, 420, 4.70, 1),
(3, '酸菜鱼', 'https://picsum.photos/seed/dish6/400/300', '鲜嫩鱼片，酸辣开胃', 35.00, 1, '鱼,酸菜', 2, 230, 4.50, 1),
(3, '麻婆豆腐', 'https://picsum.photos/seed/dish7/400/300', '麻辣鲜香，下饭神器', 18.00, 1, '豆腐,川菜', 2, 310, 4.30, 1),
(4, '经典牛油锅底', 'https://picsum.photos/seed/dish8/400/300', '浓郁牛油，麻辣鲜香', 68.00, 5, '火锅,牛油', 3, 150, 4.70, 1),
(4, '番茄锅底', 'https://picsum.photos/seed/dish9/400/300', '酸甜清淡，老少皆宜', 48.00, 5, '火锅,番茄', 0, 180, 4.50, 1);

-- 测试用户 (密码: 123456)
INSERT INTO `user` (`username`, `password`, `nickname`, `phone`, `gender`, `taste_preference`, `status`) VALUES
('user01', 'e10adc3949ba59abbe56e057f20f883e', '美食达人', '13900139001', 1, '{"spicy": 3, "sweet": 2, "sour": 1, "categories": [1,5]}', 1),
('user02', 'e10adc3949ba59abbe56e057f20f883e', '吃货小王', '13900139002', 2, '{"spicy": 1, "sweet": 4, "sour": 2, "categories": [7,4]}', 1);
