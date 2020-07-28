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
)
