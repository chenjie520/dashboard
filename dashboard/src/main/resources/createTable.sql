-- 创建用户表
CREATE TABLE IF NOT EXISTS `user`(
   `id` INT UNSIGNED AUTO_INCREMENT COMMENT 'id',
   `dc_id` VARCHAR(100) unique COMMENT 'dcId',
	 `is_in_dc` varchar(2) COMMENT '是否在dc服务器中：1.在，2.不在',
   `create_date` DATE COMMENT '创建日期',
	 `netnut_customer_id` varchar(32) comment 'netnut的子账号',
	 `cheap_user_name` varchar(64) comment 'cheap的子用户',
	 `smart_user_name` varchar(64) comment 'smart的子用户账号',
	 `smart_user_password` varchar(64) comment 'smart的子用户密码',
	 PRIMARY key(`id`)
);
-- 创建订单表
CREATE TABLE `order` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `dc_id` varchar(100) DEFAULT NULL COMMENT 'dcId',
  `buy_date` date DEFAULT NULL COMMENT '购买时间',
  `due_date` date DEFAULT NULL COMMENT '到期时间',
  `buy_price` varchar(32) DEFAULT NULL COMMENT '购买价格',
  `discount` varchar(32) DEFAULT NULL COMMENT '折扣码',
  `buy_type` varchar(32) DEFAULT NULL COMMENT '购买流量(G)',
  PRIMARY KEY (`id`)
) ;
--创建折扣表
CREATE TABLE IF NOT EXISTS `discount`(
   `id` INT UNSIGNED AUTO_INCREMENT COMMENT 'id',
   `number` VARCHAR(100)  COMMENT '折扣码',
   `type` varchar(10) COMMENT '折扣力度(比如9折，则0.9)',
	 `due_date` Date comment '到期时间',
	 `dc_id` varchar(100) comment '使用人的dcId',
	 `create_date` date comment '创建时间',
	 `use_date` date comment '使用时间',
	 PRIMARY key(`id`)
);
-- smart 表
CREATE TABLE IF NOT EXISTS `smart_proxy_server`(
   `id` INT UNSIGNED AUTO_INCREMENT COMMENT 'id',
   `city` varchar(32) comment '国家',
	 `proxy_address` varchar(128) comment '域名地址',
	 `port` varchar(64) comment '端口号范围',
	 PRIMARY key(`id`)
);
INSERT INTO `smart_proxy_server` VALUES (1, 'us', 'us.smartproxy.com', '10001-29999');
INSERT INTO `smart_proxy_server` VALUES (2, 'uk', 'ngb.smartproxy.com', '30001-49999');
INSERT INTO `smart_proxy_server` VALUES (3, 'ca', 'ca.smartproxy.com', '20001-29999');
INSERT INTO `smart_proxy_server` VALUES (4, 'aus', 'au.smartproxy.com', '30001-39999');
INSERT INTO `smart_proxy_server` VALUES (5, 'sg', 'sg.smartproxy.com', '10001-19999');
INSERT INTO `smart_proxy_server` VALUES (6, 'jp', 'jp.smartproxy.com', '30001-39999');
INSERT INTO `smart_proxy_server` VALUES (7, 'chn', 'cn.smartproxy.com', '30001-39999');
INSERT INTO `smart_proxy_server` VALUES (8, 'ger', 'de.smartproxy.com', '20001-29999');
INSERT INTO `smart_proxy_server` VALUES (9, 'sk', 'kr.smartproxy.com', '10001-19999');
