-- 创建用户表
CREATE TABLE IF NOT EXISTS `user`(
   `id` INT UNSIGNED AUTO_INCREMENT COMMENT 'id',
   `dc_id` VARCHAR(100) unique COMMENT 'dcId',
   `email` VARCHAR(40)  COMMENT 'email',
	 `user_name` varchar(100) COMMENT '用户名',
	 `user_password` varchar(128) COMMENT '密码',
	 `is_in_dc` varchar(2) COMMENT '是否在dc服务器中：1.在，2.不在',
   `create_date` DATE COMMENT '创建日期',
	 PRIMARY key(`id`)
);
-- 创建订单表
CREATE TABLE IF NOT EXISTS `order`(
   `id` INT UNSIGNED AUTO_INCREMENT COMMENT 'id',
   `dc_id` VARCHAR(100)  COMMENT 'dcId',
   `buy_date` DATE COMMENT '购买时间',
	 `due_date` Date comment '到期时间',
	 `buy_price` varchar(32) comment '购买价格',
	 `discount` varchar(32) comment '折扣码',
	 `buy_type` varchar(32) comment '购买流量(G)',
	 `netnut_coustomer_id` varchar(128) comment 'netnut生成的coustomerId',
	 PRIMARY key(`id`)
);
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