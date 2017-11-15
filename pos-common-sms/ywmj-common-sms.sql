-- ----------------------------
-- 短信表
-- ----------------------------
DROP TABLE IF EXISTS `sms`;
CREATE TABLE `sms` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `type` tinyint(2) unsigned NOT NULL COMMENT '短信类型',
  `phone` char(11) NOT NULL COMMENT '接收人手机号',
  `content` varchar(1024) NOT NULL COMMENT '短信内容',
  `status` tinyint(4) NOT NULL COMMENT '发送状态',
  `send_time` datetime NOT NULL COMMENT '发送时间',
  PRIMARY KEY (`id`),
  KEY `idx_sms_1` (`type`,`send_time`),
  KEY `idx_sms_2` (`phone`,`type`,`send_time`)
) ENGINE=InnoDB AUTO_INCREMENT=352 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='短信发送记录表';