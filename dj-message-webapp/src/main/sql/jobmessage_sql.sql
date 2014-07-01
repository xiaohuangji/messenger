/*
SQLyog 企业版 - MySQL GUI v8.14 
MySQL - 5.1.58 : Database - DB_JOBMESSAGE
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`DB_JOBMESSAGE` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `DB_JOBMESSAGE`;

/*Table structure for table `blacklist` */

DROP TABLE IF EXISTS `blacklist`;

CREATE TABLE `blacklist` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `userId` int(11) NOT NULL COMMENT '用户id',
  `blockId` int(11) NOT NULL COMMENT '屏蔽id',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `userId` (`userId`,`blockId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='黑名单表';

/*Table structure for table `friend_requests` */

DROP TABLE IF EXISTS `friend_requests`;

CREATE TABLE `friend_requests` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NOT NULL COMMENT '请求者的用户id',
  `toId` int(11) NOT NULL COMMENT '被请求者的用户id',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `userId_toId` (`userId`,`toId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='好友请求表';

/*Table structure for table `friends` */

DROP TABLE IF EXISTS `friends`;

CREATE TABLE `friends` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NOT NULL COMMENT '用户id',
  `friendId` int(11) NOT NULL COMMENT '好友id',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `userId_friendId` (`userId`,`friendId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='好友表';

/*Table structure for table `inform_job_table` */

DROP TABLE IF EXISTS `inform_job_table`;

CREATE TABLE `inform_job_table` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `informUserId` int(11) NOT NULL DEFAULT '0' COMMENT '举报者id',
  `positionName` varchar(50) NOT NULL DEFAULT '0' COMMENT '职位名称',
  `jobId` int(11) NOT NULL DEFAULT '0' COMMENT 'jobId',
  `jobUserId` int(11) NOT NULL DEFAULT '0' COMMENT '发布job的人员id',
  `description` varchar(20000) DEFAULT '0' COMMENT '描述信息',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态（0表示未处理，1表示已经处理）',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期',
  `createTime` datetime NOT NULL COMMENT '修改日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='举报的job';

/*Table structure for table `job_description` */

DROP TABLE IF EXISTS `job_description`;

CREATE TABLE `job_description` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'descId',
  `description` varchar(20000) NOT NULL DEFAULT '0' COMMENT 'description',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `md5` varbinary(16) DEFAULT NULL COMMENT 'description的md5值',
  PRIMARY KEY (`id`),
  KEY `description` (`description`(255)),
  KEY `md5` (`md5`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='job描述信息表';

/*Table structure for table `job_description_user` */

DROP TABLE IF EXISTS `job_description_user`;

CREATE TABLE `job_description_user` (
  `userId` int(11) DEFAULT NULL COMMENT '用户id',
  `descriptionId` int(11) DEFAULT NULL COMMENT '描述id',
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `createDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建日期',
  PRIMARY KEY (`id`),
  UNIQUE KEY `userId_descriptionId` (`userId`,`descriptionId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='job描述和user相关的表';

/*Table structure for table `job_detail_cnt` */

DROP TABLE IF EXISTS `job_detail_cnt`;

CREATE TABLE `job_detail_cnt` (
  `jobId` int(11) NOT NULL COMMENT 'job的id',
  `cnt` int(11) DEFAULT '0' COMMENT 'job details 被访问的次数',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  PRIMARY KEY (`id`),
  KEY `jobId_cnt` (`jobId`,`cnt`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用于计数用户获取职位详细信息';

/*Table structure for table `job_table` */

DROP TABLE IF EXISTS `job_table`;

CREATE TABLE `job_table` (
  `userId` int(11) NOT NULL COMMENT '发布者id',
  `positionName` varchar(70) NOT NULL COMMENT '职位名称',
  `jobType` varchar(40) NOT NULL COMMENT '职位类型',
  `corpName` varchar(50) NOT NULL COMMENT '公司名称',
  `corpId` varchar(50) DEFAULT NULL COMMENT '公司id',
  `salaryStart` int(11) DEFAULT NULL COMMENT '起薪',
  `salaryEnd` int(11) DEFAULT NULL COMMENT '最高薪资',
  `workExperience` int(11) DEFAULT NULL COMMENT '工作经验',
  `educationLevel` int(11) DEFAULT NULL COMMENT '教育等级',
  `industry` int(11) DEFAULT NULL COMMENT '行业',
  `descriptionIds` varchar(64) DEFAULT NULL COMMENT '描述',
  `poiUids` varchar(256) DEFAULT NULL COMMENT '地理信息',
  `jobId` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态（0表示正常，1表示审核问题）',
  `startDate` datetime DEFAULT NULL COMMENT '开始时间',
  `endDate` datetime DEFAULT NULL COMMENT '结束时间',
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `createDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `oldJid` char(36) DEFAULT NULL COMMENT '主站对应的c2c 职位信息的jid',
  `hunter` tinyint(4) DEFAULT '0' COMMENT '是否是猎头 0表示不是，1表示是',
  PRIMARY KEY (`jobId`),
  KEY `userId` (`userId`),
  KEY `oldJid` (`oldJid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='job服务';

/*Table structure for table `job_third_part` */

DROP TABLE IF EXISTS `job_third_part`;

CREATE TABLE `job_third_part` (
  `jid` char(36) NOT NULL COMMENT '全局唯一的JOBID',
  `cid` char(36) NOT NULL DEFAULT '' COMMENT '对应企业corpid',
  `uid` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '标记谁发的职位',
  `corp_name` varchar(150) DEFAULT NULL COMMENT '公司名称',
  `title` varchar(300) NOT NULL DEFAULT '' COMMENT '职位标题',
  `name` varchar(150) NOT NULL DEFAULT '' COMMENT '职位名称',
  `profession` varchar(300) DEFAULT '' COMMENT '工作类别，用","将ID分割',
  `industry` varchar(150) DEFAULT '' COMMENT '行业，用","将ID分割',
  `kind` varchar(20) NOT NULL DEFAULT '1' COMMENT '工作性质，1=全职，0=兼职',
  `recruit_type` tinyint(2) NOT NULL DEFAULT '0' COMMENT '招聘类型:0默认;1:校招应届生;2:校招实习生;3:社招含应届生;4:社招不含应届生 ',
  `is_intern` int(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否实习, 1=是，0=不是',
  `department` varchar(50) NOT NULL DEFAULT '' COMMENT '部门',
  `city` varchar(100) NOT NULL DEFAULT '' COMMENT '工作地点',
  `head_count` int(5) unsigned NOT NULL DEFAULT '0' COMMENT '招聘人数',
  `salary` char(20) NOT NULL DEFAULT '' COMMENT '薪金',
  `welfare` varchar(200) NOT NULL DEFAULT '' COMMENT '福利',
  `corpad` varchar(300) DEFAULT '' COMMENT 'corp pad',
  `type` int(1) NOT NULL DEFAULT '0' COMMENT '职位来源类型0表示抓取，1表示从大街创建的职位',
  `status` int(1) NOT NULL DEFAULT '0' COMMENT '职位状态 0待审核,1审核通过,-1审核通过类的职位的未发布状态,-2审核未通过类的职位的未发布状态,如果重新发布，状态应该直接变成1,-3审核不通过',
  `post_status` int(11) NOT NULL DEFAULT '1' COMMENT '发布状态，0表示未发布，1表示发布，默认为发布',
  `end_date` timestamp NULL DEFAULT NULL COMMENT '截止日期',
  `validity` int(11) NOT NULL DEFAULT '30' COMMENT '有效期',
  `start_date` timestamp NULL DEFAULT NULL COMMENT '发布日期效期，create_date是创建日期，但是创建了不一定发布了',
  `cv_eng` tinyint(4) DEFAULT NULL COMMENT '是否英文简历',
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_date` timestamp NULL DEFAULT NULL COMMENT '创建日期',
  `seq` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '序列号',
  `corp_quality` int(4) DEFAULT NULL COMMENT '公司类型',
  `corp_scale` int(4) DEFAULT NULL COMMENT '公司规模',
  `corp_rank` int(4) DEFAULT NULL COMMENT '公司类型：一般企业为0，知名企业>0: 1大街认证,2企业俱乐部，4全球500，8国内500，16行业500',
  `intro` text COMMENT '职位简介',
  `email` varchar(200) DEFAULT NULL COMMENT 'Email',
  `project_tag` char(20) NOT NULL DEFAULT '' COMMENT 'project tag',
  `salary_end` char(20) DEFAULT NULL COMMENT '薪水终止',
  `internship_days` tinyint(4) NOT NULL DEFAULT '0' COMMENT '每周实习天数',
  `internship_period` tinyint(4) NOT NULL DEFAULT '0' COMMENT '实习周期',
  `position_exper` int(11) NOT NULL DEFAULT '0' COMMENT '职位级别',
  `position_function` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '职位类别',
  `position_industry` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '职位行业',
  `display_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '显示类型, 0：个人 1：公司',
  `poi` varchar(512) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '工作地址详情',
  `feature` varchar(2048) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '工作特色标签，使用'',''分割',
  `address` varchar(512) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '工作地点',
  `experience` int(10) unsigned zerofill DEFAULT '0000000000' COMMENT '工作经验',
  `degree` int(10) unsigned zerofill DEFAULT '0000000000' COMMENT '学历',
  PRIMARY KEY (`seq`),
  KEY `corp_name` (`corp_name`,`create_date`),
  KEY `type_corp_index` (`type`,`corp_name`),
  KEY `type_uid_index` (`type`,`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='第三方职位信息';

/*Table structure for table `notification_setting` */

DROP TABLE IF EXISTS `notification_setting`;

CREATE TABLE `notification_setting` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `userId` int(11) NOT NULL COMMENT '用户id',
  `newMessage` int(11) NOT NULL COMMENT '新消息开关',
  `sound` int(11) NOT NULL COMMENT '声音开关',
  `vibration` int(11) NOT NULL COMMENT '振动开关',
  `nightNoDisturbance` int(11) NOT NULL COMMENT '夜间免打扰开关',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `userId` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='客户端通知开关表';

/*Table structure for table `operation_feedback` */

DROP TABLE IF EXISTS `operation_feedback`;

CREATE TABLE `operation_feedback` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `userId` int(11) NOT NULL COMMENT '用户id',
  `content` varchar(10000) DEFAULT NULL COMMENT '反馈消息',
  `mobile` varchar(50) DEFAULT NULL COMMENT '手机号',
  `reply` varchar(2000) DEFAULT NULL COMMENT '反馈回复',
  `createTime` datetime NOT NULL COMMENT '操作时间',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态',
  `system` int(11) DEFAULT NULL COMMENT '系统',
  `systemVersion` varchar(50) DEFAULT NULL COMMENT '系统版本',
  `mobileBrand` varchar(50) DEFAULT NULL COMMENT '手机品牌',
  `mobileModel` varchar(50) DEFAULT NULL COMMENT '手机型号',
  `channel` varchar(50) DEFAULT NULL COMMENT '渠道',
  `clientVersion` varchar(50) DEFAULT NULL COMMENT '客户端软件版本',
  `mobileResolution` varchar(50) DEFAULT NULL COMMENT '手机分辨率',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `userId` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='运营小助手反馈表';

/*Table structure for table `operation_push_job` */

DROP TABLE IF EXISTS `operation_push_job`;

CREATE TABLE `operation_push_job` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `content` varchar(2000) NOT NULL DEFAULT '0' COMMENT '内容',
  `triggerDate` datetime NOT NULL COMMENT '触发时间',
  `filterIsVerified` int(11) NOT NULL DEFAULT '0' COMMENT '认证过滤',
  `filterJobType` varchar(50) NOT NULL COMMENT '工作类型过滤',
  `filterIndustry` varchar(50) NOT NULL COMMENT '行业过滤',
  `filterGender` int(11) NOT NULL DEFAULT '0' COMMENT '性别过滤',
  `filterCity` varchar(50) NOT NULL COMMENT '城市过滤',
  `userCount` int(11) NOT NULL DEFAULT '0' COMMENT '发送用户数',
  `operator` varchar(50) NOT NULL COMMENT '操作者',
  `filterDesc` varchar(100) NOT NULL COMMENT '过滤描述',
  `status` int(11) NOT NULL COMMENT '状态',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='定时推送任务';

/*Table structure for table `platform_map` */

DROP TABLE IF EXISTS `platform_map`;

CREATE TABLE `platform_map` (
  `mapId` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `userId` int(11) NOT NULL COMMENT '用户id',
  `platformType` tinyint(4) NOT NULL COMMENT '平台类型。1：dajie,2:sina_weibo,3:qq,4:renren',
  `platformUid` varchar(100) NOT NULL COMMENT '第三方uid',
  `accessToken` varchar(150) DEFAULT NULL COMMENT '第三方token',
  `secretToken` varchar(150) DEFAULT NULL COMMENT '第三方secret token',
  `tokenType` tinyint(4) DEFAULT NULL COMMENT '授权类型，默认OAuth2.0',
  `source` varchar(20) NOT NULL DEFAULT '' COMMENT '来源，ios,android',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态',
  `createTime` datetime NOT NULL COMMENT '接入时间',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`mapId`),
  UNIQUE KEY `platform_map` (`platformUid`,`platformType`),
  UNIQUE KEY `user_id_platform_type` (`userId`,`platformType`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='第三方平台映射表';

/*Table structure for table `privacy_setting` */

DROP TABLE IF EXISTS `privacy_setting`;

CREATE TABLE `privacy_setting` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `userId` int(11) NOT NULL COMMENT '用户id',
  `colleagueVisibility` int(11) NOT NULL COMMENT '同事可见',
  `visibility` int(11) NOT NULL COMMENT '可见性',
  `chatNotification` int(11) NOT NULL COMMENT '聊天提醒',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `userId` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='个人隐私设置';

/*Table structure for table `push_apikey` */

DROP TABLE IF EXISTS `push_apikey`;

CREATE TABLE `push_apikey` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `apiKey` varchar(50) DEFAULT '0' COMMENT 'apikey',
  `secretKey` varchar(50) DEFAULT '0' COMMENT 'secretKey',
  `appId` varchar(50) DEFAULT NULL COMMENT 'appId',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `apiKey` (`apiKey`),
  UNIQUE KEY `secretKey` (`secretKey`),
  UNIQUE KEY `appId` (`appId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='push认证key.';

/*Table structure for table `push_client_info` */

DROP TABLE IF EXISTS `push_client_info`;

CREATE TABLE `push_client_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `clientId` varchar(50) NOT NULL COMMENT '手机端id',
  `appId` varchar(50) NOT NULL COMMENT '应用id',
  `userId` varchar(50) NOT NULL DEFAULT '' COMMENT '用户id',
  `tagId` int(11) NOT NULL DEFAULT '0' COMMENT 'tagId',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `clientId_appId` (`clientId`,`appId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='pushclient信息';

/*Table structure for table `push_push_info` */

DROP TABLE IF EXISTS `push_push_info`;

CREATE TABLE `push_push_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `appId` varchar(50) NOT NULL DEFAULT '0' COMMENT '应用id',
  `userId` varchar(50) NOT NULL COMMENT '用户id',
  `fromUserId` varchar(50) DEFAULT '0' COMMENT '发送者id',
  `upMsgId` bigint(20) NOT NULL COMMENT '上行消息id',
  `msgId` int(11) NOT NULL DEFAULT '0' COMMENT '下行消息id',
  `payload` varchar(20000) DEFAULT NULL COMMENT 'push消息',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `userId` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='push内容';

/*Table structure for table `push_stats_info` */

DROP TABLE IF EXISTS `push_stats_info`;

CREATE TABLE `push_stats_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `serverName` varchar(50) NOT NULL DEFAULT '0' COMMENT '机器名',
  `iosConn` int(11) NOT NULL DEFAULT '0' COMMENT 'ios连接数',
  `androidConn` int(11) NOT NULL DEFAULT '0' COMMENT 'android连接数',
  `serverConn` int(11) NOT NULL DEFAULT '0' COMMENT 'server连接数',
  `fromIos` int(11) NOT NULL DEFAULT '0' COMMENT '上行ios消息',
  `fromAndroid` int(11) NOT NULL DEFAULT '0' COMMENT '上行android消息',
  `fromServer` int(11) NOT NULL DEFAULT '0' COMMENT '上行服务消息',
  `toIos` int(11) NOT NULL DEFAULT '0' COMMENT '下行ios消息',
  `toAndroid` int(11) NOT NULL DEFAULT '0' COMMENT '下行android消息',
  `toServer` int(11) NOT NULL DEFAULT '0' COMMENT '下行server消息',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='push统计信息';

/*Table structure for table `region` */

DROP TABLE IF EXISTS `region`;

CREATE TABLE `region` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `region_code` varchar(100) DEFAULT NULL COMMENT 'code',
  `region_name` varchar(100) DEFAULT NULL COMMENT '名称',
  `parent_id` int(11) DEFAULT NULL COMMENT '父id',
  `region_level` int(11) DEFAULT NULL COMMENT '层数',
  `region_order` int(11) DEFAULT NULL COMMENT '顺序',
  `region_name_en` varchar(100) DEFAULT NULL COMMENT '英文名',
  `region_short_name_en` varchar(10) DEFAULT NULL COMMENT '短英文名',
  `region_lat` double DEFAULT NULL COMMENT '纬度',
  `region_lon` double DEFAULT NULL COMMENT '经度',
  `region_unique_id` varchar(50) DEFAULT NULL COMMENT 'id',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `region_name` (`region_name`),
  KEY `parent_id` (`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='中国省市区信息';

/*Table structure for table `school_label` */

DROP TABLE IF EXISTS `school_label`;

CREATE TABLE `school_label` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `school` varchar(45) DEFAULT NULL COMMENT '学校名',
  `label` varchar(45) DEFAULT NULL COMMENT '学校标签',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='名校标签表';

/*Table structure for table `unique_poi_info` */

DROP TABLE IF EXISTS `unique_poi_info`;

CREATE TABLE `unique_poi_info` (
  `unique_id` varchar(50) NOT NULL COMMENT '唯一id',
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `lat` double DEFAULT NULL COMMENT '纬度',
  `lon` double DEFAULT NULL COMMENT '经度',
  `city` varchar(50) DEFAULT NULL COMMENT '所在城市',
  `district` varchar(50) DEFAULT NULL COMMENT '所在区域',
  `province` varchar(50) DEFAULT NULL COMMENT '所在省份',
  `street` varchar(200) DEFAULT NULL COMMENT '街道',
  `street_number` varchar(50) DEFAULT NULL COMMENT '街道号',
  `address` varchar(500) DEFAULT NULL COMMENT '详细地址',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_id` (`unique_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='poi信息表';

/*Table structure for table `user_base` */

DROP TABLE IF EXISTS `user_base`;

CREATE TABLE `user_base` (
  `userId` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `password` varchar(64) DEFAULT NULL COMMENT '密码',
  `salt` char(10) DEFAULT NULL COMMENT '盐',
  `name` varchar(45) DEFAULT NULL COMMENT '名称',
  `gender` tinyint(4) NOT NULL DEFAULT '0' COMMENT '性别。0:unknown,1:male,2:female',
  `avatar` varchar(100) DEFAULT NULL COMMENT '头像',
  `avatarMask` varchar(100) DEFAULT NULL COMMENT '遮罩头像',
  `email` varchar(45) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(25) DEFAULT NULL COMMENT '手机号',
  `birth` date DEFAULT NULL COMMENT '生日',
  `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '帐户类型',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '帐户状态',
  `feature` int(11) NOT NULL DEFAULT '0' COMMENT '帐户特征',
  `audit` tinyint(4) DEFAULT '0' COMMENT '用户资料审核结果(支持按位或) 1. 名字未通过 2.教育背景未通过  4.职业背景未通过  8.自定义标签未通过',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`userId`),
  UNIQUE KEY `mobile` (`mobile`)
) ENGINE=InnoDB AUTO_INCREMENT=10000000 DEFAULT CHARSET=utf8 COMMENT='用户基本信息表';

/*Table structure for table `user_career` */

DROP TABLE IF EXISTS `user_career`;

CREATE TABLE `user_career` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `userId` int(11) DEFAULT NULL COMMENT '用户id',
  `positionName` varchar(45) DEFAULT NULL COMMENT '职位名称',
  `positionType` int(11) DEFAULT NULL COMMENT '职位类型',
  `corpName` varchar(45) DEFAULT NULL COMMENT '公司名称',
  `corpId` int(11) DEFAULT NULL COMMENT '公司id',
  `verification` tinyint(4) DEFAULT NULL COMMENT '认证状态：0. 未认证 1. 认证通过',
  `industry` int(11) DEFAULT NULL COMMENT '行业',
  `experience` int(11) DEFAULT NULL COMMENT '工作经验',
  `education` int(11) DEFAULT NULL COMMENT '学历',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户职场背景表';

/*Table structure for table `user_device_info` */

DROP TABLE IF EXISTS `user_device_info`;

CREATE TABLE `user_device_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '逻辑主键',
  `userId` int(11) NOT NULL COMMENT '帐户id',
  `system` tinyint(4) NOT NULL DEFAULT '0' COMMENT '系统平台。0:unknown,1:ios,2:android',
  `systemVersion` varchar(50) DEFAULT NULL COMMENT '系统版本',
  `mobileBrand` varchar(50) DEFAULT NULL COMMENT '手机品牌',
  `mobileModel` varchar(50) DEFAULT NULL COMMENT '手机型号',
  `channel` varchar(50) DEFAULT NULL COMMENT '渠道',
  `clientVersion` varchar(20) NOT NULL COMMENT '客户端版本',
  `mobileResolution` varchar(50) DEFAULT NULL COMMENT '手机分辨率',
  `createTime` date DEFAULT NULL COMMENT '创建时间',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `userId` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户设备信息';

/*Table structure for table `user_education` */

DROP TABLE IF EXISTS `user_education`;

CREATE TABLE `user_education` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `userId` int(11) DEFAULT NULL COMMENT '用户id',
  `major` varchar(45) DEFAULT NULL COMMENT '专业名',
  `school` varchar(45) DEFAULT NULL COMMENT '学校名',
  `label` varchar(45) DEFAULT NULL COMMENT '学校标签',
  `degree` int(11) DEFAULT NULL COMMENT '学历',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户教育背景表';

/*Table structure for table `user_email_verify` */

DROP TABLE IF EXISTS `user_email_verify`;

CREATE TABLE `user_email_verify` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `userId` int(11) NOT NULL COMMENT '用户id',
  `corpName` varchar(100) DEFAULT NULL COMMENT '公司名',
  `email` varchar(100) DEFAULT NULL COMMENT '公司邮箱',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `verifyDate` datetime DEFAULT NULL COMMENT '用户点击验证邮件的时间',
  `updateDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `userId` (`userId`),
  KEY `verifyDate` (`verifyDate`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户的公司邮箱验证表';

/*Table structure for table `user_ios_token` */

DROP TABLE IF EXISTS `user_ios_token`;

CREATE TABLE `user_ios_token` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `userId` int(11) NOT NULL COMMENT '用户id',
  `token` varchar(200) NOT NULL COMMENT 'token',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `userId` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='iostoken表';

/*Table structure for table `user_label` */

DROP TABLE IF EXISTS `user_label`;

CREATE TABLE `user_label` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键，代表标签id',
  `userId` int(11) DEFAULT NULL COMMENT '用户id',
  `content` varchar(20000) DEFAULT NULL COMMENT '标签内 容',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户自定义标签表';

/*Table structure for table `user_label_like` */

DROP TABLE IF EXISTS `user_label_like`;

CREATE TABLE `user_label_like` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `userId` int(11) DEFAULT NULL COMMENT '用户id',
  `labelId` int(11) DEFAULT NULL COMMENT '标签id',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `userId_labelId` (`userId`,`labelId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户的喜欢标签表';

/*Table structure for table `user_statistics` */

DROP TABLE IF EXISTS `user_statistics`;

CREATE TABLE `user_statistics` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `userId` int(11) NOT NULL COMMENT '用户id',
  `profileVisit` int(11) NOT NULL DEFAULT '0' COMMENT '个人主页访问次数',
  `aCount` int(11) NOT NULL DEFAULT '0' COMMENT '预留统计字段a，扩展用',
  `bCount` int(11) NOT NULL DEFAULT '0' COMMENT '预留统计字段b，扩展用',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `userId` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户统计信息表';


--插入初始化数据--
insert into push_apikey (apiKey,secretKey,appId,createTime) values ('JMApi','46ce9YHhxUkPKu4x2IMGU7Xv','JMApp','2014-05-19 19:19:34');


--modify by chengwei--
ALTER TABLE `DB_JOBMESSAGE`.`user_base` ADD COLUMN `lastLogin` DATETIME NULL DEFAULT NULL COMMENT '用户最近一次登录时间' AFTER `createTime`;

--modify by lihui--
alter table user_base modify name varchar(100);
alter table user_base modify email varchar(100);

