SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- 地区配置表
-- ----------------------------
DROP TABLE IF EXISTS `area`;
CREATE TABLE `area` (
  `id` int(11) unsigned NOT NULL,
  `name` varchar(20) NOT NULL,
  `short_name` varchar(20) NOT NULL,
  `parent_id` int(11) unsigned DEFAULT NULL,
  `level` tinyint(1) unsigned NOT NULL COMMENT '地区层级，目前设计最多为3级(直辖市只有2级)，1级为省，2级为市，3级为县/区',
  `order_num` tinyint(2) unsigned NOT NULL COMMENT '显示排序（正序）',
  `available` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可用',
  PRIMARY KEY (`id`),
  KEY `idx_area_1` (`level`,`available`,`id`,`order_num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='地区配置表';

-- ----------------------------
-- 楼盘表
-- ----------------------------
DROP TABLE IF EXISTS `building`;
CREATE TABLE `building` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '楼盘名称',
  `area_id` int(11) unsigned NOT NULL COMMENT '地区ID（叶子节点）',
  `address` varchar(100) COMMENT '楼盘地址',
  `developer` varchar(50) COMMENT '开发商',
  `decoration` tinyint(1) COMMENT '装修标准：1 = 毛坯，2 = 精装',
  `available` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_user` int(11) unsigned DEFAULT NULL COMMENT '创建人UID',
  `update_user` int(11) unsigned DEFAULT NULL COMMENT '更新人UID',
  PRIMARY KEY (`id`),
  KEY `idx_building_1` (`area_id`,`available`,`name`(24)),
  KEY `idx_building_2` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='楼盘表';

-- ----------------------------
-- 楼盘户型表
-- ----------------------------
DROP TABLE IF EXISTS `building_layout`;
CREATE TABLE `building_layout` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '户型名称',
  `building_id` int(11) unsigned NOT NULL COMMENT '所属楼盘ID',
  `bedroom` tinyint(2) NOT NULL COMMENT '户型-室',
  `living_room` tinyint(2) NOT NULL COMMENT '户型-厅',
  `toilet` tinyint(2) NOT NULL COMMENT '户型-卫',
  `house_area` float(10, 2) NOT NULL COMMENT '户型面积',
  `layout_image` varchar(255) COMMENT '户型图',
  `available` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_user` int(11) unsigned NOT NULL COMMENT '创建人UID',
  `update_user` int(11) unsigned NOT NULL COMMENT '更新人UID',
  PRIMARY KEY (`id`),
  KEY `idx_building_layout_1` (`building_id`, `bedroom`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='楼盘户型表';

-- ----------------------------
-- APP版本配置表
-- ----------------------------
DROP TABLE IF EXISTS `app_version`;
CREATE TABLE `app_version` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `app_type` char(1) NOT NULL COMMENT 'app类型：1 = 客户端app，2 = 业者端app',
  `cur_version` varchar(20) NOT NULL COMMENT '当前生产环境运行的版本号',
  `min_version` varchar(20) NOT NULL COMMENT '支持的最小版本号，低于该版本号必须升级APP',
  `cur_url` varchar(255) NOT NULL COMMENT '当前生产环境的访问地址',
  `cur_im_key` varchar(20) NOT NULL COMMENT '当前生产环境连接第三方IM平台的Key',
  `latest_url` varchar(255) DEFAULT NULL COMMENT '最新版本的访问地址（一般情况下，该值不为空，表示有新版本处于审核/测试中）',
  `latest_im_key` varchar(20) DEFAULT NULL COMMENT '最新版本连接第三方IM平台的Key（值说明同上）',
  `web_url` varchar(255) NOT NULL COMMENT '当前生产环境的WEB访问地址,H5使用',
  `preweb_url` varchar(255) NOT NULL COMMENT '当前预发布环境的WEB访问地址，H5使用，测试用',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_app_version_appType` (`app_type`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='APP版本配置表';

-- ----------------------------
-- 品牌配置表
-- ----------------------------
DROP TABLE IF EXISTS `brand`;
CREATE TABLE `brand` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL COMMENT '品牌名称',
  `logo` varchar(255) NOT NULL COMMENT '品牌LOGO图片URL',
  `website` varchar(512) DEFAULT NULL COMMENT '品牌官网',
  `available` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_user` int(11) unsigned NOT NULL COMMENT '创建人UID',
  `update_user` int(11) unsigned DEFAULT NULL COMMENT '更新人UID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=646 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='品牌配置表';

-- ----------------------------
-- 品牌分类表
-- ----------------------------
DROP TABLE IF EXISTS `brand_class`;
CREATE TABLE `brand_class` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `brand_id` int(11) NOT NULL COMMENT '品牌ID',
  `type` tinyint(2) NOT NULL COMMENT '品牌大类',
  `sub_type` smallint(6) NOT NULL COMMENT '品牌子类',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_user` int(11) unsigned NOT NULL COMMENT '创建人UID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_brand_class` (`brand_id`,`type`,`sub_type`) USING BTREE,
  KEY `idx_brand_class_1` (`type`,`sub_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=600 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;