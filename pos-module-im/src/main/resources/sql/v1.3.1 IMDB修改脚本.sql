/*----------------------------------
----------执行前必读！！！----------
--脚本执行前请先备份以下表：
--im_user_info
--im_case_session
--im_case_session_company
--im_case_session_member
--im_case_session_message
----------------------------------*/


/*----------修改IM用户表----------*/
ALTER TABLE im_user_info ADD `company_id` int(11) unsigned COMMENT 'B端/业者所属的公司ID' AFTER `show_head`;
ALTER TABLE im_user_info ADD `public_phone` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '是否公开电话号码' AFTER `phone`;


/*----------修改IM会话表----------*/
DROP INDEX idx_imCaseSes_2 ON im_case_session;
ALTER TABLE im_case_session RENAME im_session;
ALTER TABLE im_session DROP COLUMN teams_joined;


/*----------修改IM会话成员表----------*/
ALTER TABLE im_case_session_member RENAME im_session_member;
ALTER TABLE im_session_member ADD `call_total` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '呼叫次数' AFTER `user_join_type`;
ALTER TABLE im_session_member ADD `company_id` int(11) unsigned COMMENT 'B端/业者加入会话时所属的公司ID' AFTER `call_total`;
ALTER TABLE im_session_member ADD `last_rename_time` datetime COMMENT '最近一次修改群名称的时间' AFTER `update_time`;


/*----------修改IM会话关联的公司表----------*/
ALTER TABLE im_case_session_company RENAME im_session_company;
ALTER TABLE im_session_company ADD `call_total` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '呼叫次数（冗余字段）' AFTER `company_id`;


/*----------修改IM会话消息表----------*/
ALTER TABLE im_case_session_message RENAME im_session_message;


-- ----------------------------
-- 新增IM公司信息表
-- ----------------------------
DROP TABLE IF EXISTS `im_company_info`;
CREATE TABLE `im_company_info` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `company_id` int(11) unsigned NOT NULL COMMENT '公司ID',
  `name` varchar(20) NOT NULL COMMENT '公司名称',
  `area_id` int(11) unsigned NOT NULL COMMENT '所属地区ID（叶子节点）',
  `address` varchar(100) COMMENT '公司地址',
  `logo_image` varchar(200) NOT NULL COMMENT '公司LOGO',
  `create_time` datetime NOT NULL COMMENT '公司信息初始化到IM的时间',
  `update_time` datetime NOT NULL COMMENT '公司信息最近一次同步到IM的时间',
  `available` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可用',
  `im_sms_enable` tinyint(1) NOT NULL DEFAULT '1' COMMENT 'IM短信开关，默认开启：当有用户通过IM咨询时，本公司在会话中的业者将收到一条提醒短信',
  `im_notice_enable` tinyint(1) NOT NULL DEFAULT '1' COMMENT 'IM通知开关，默认开启：当有用户通过IM咨询时，本公司在会话中的业者将收到一条站内信',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_imComInfo_comId` (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'IM公司信息表';


-- ----------------------------
-- 新增IM会话关联的案例表
-- ----------------------------
DROP TABLE IF EXISTS `im_session_case`;
CREATE TABLE `im_session_case` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `session_id` int(11) unsigned NOT NULL COMMENT '会话ID',
  `case_id` int(11) unsigned NOT NULL COMMENT '会话关联的案例ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_imSesCase_id` (`session_id`),
  KEY `idx_imSesCase_1` (`case_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='IM会话关联的案例表';


-- ----------------------------
-- 新增IM会话关联的订单表
-- ----------------------------
DROP TABLE IF EXISTS `im_session_order`;
CREATE TABLE `im_session_order` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `session_id` int(11) unsigned NOT NULL COMMENT '会话ID',
  `order_id` int(11) unsigned NOT NULL COMMENT '会话关联的订单ID',
  `order_num` bigint(15) unsigned NOT NULL COMMENT '会话关联的订单编号',
  `order_content_type` tinyint(3) unsigned NOT NULL COMMENT '会话关联订单的内容类型',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_imSesOrder_id` (`session_id`, `order_content_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='IM会话关联的订单表';

