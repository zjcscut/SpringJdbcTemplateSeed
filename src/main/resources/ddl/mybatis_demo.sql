/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50713
Source Host           : localhost:3306
Source Database       : mybatis_demo

Target Server Type    : MYSQL
Target Server Version : 50713
File Encoding         : 65001

Date: 2016-10-28 19:26:56
*/

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for hibernate_sequence
-- ----------------------------
DROP TABLE IF EXISTS `hibernate_sequence`;
CREATE TABLE `hibernate_sequence` (
  `next_val` BIGINT(20) DEFAULT NULL
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for QRTZ_BLOB_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_BLOB_TRIGGERS`;
CREATE TABLE `QRTZ_BLOB_TRIGGERS` (
  `SCHED_NAME`    VARCHAR(120) NOT NULL,
  `TRIGGER_NAME`  VARCHAR(200) NOT NULL,
  `TRIGGER_GROUP` VARCHAR(200) NOT NULL,
  `BLOB_DATA`     BLOB,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`),
  KEY `SCHED_NAME` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`),
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for QRTZ_CALENDARS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_CALENDARS`;
CREATE TABLE `QRTZ_CALENDARS` (
  `SCHED_NAME`    VARCHAR(120) NOT NULL,
  `CALENDAR_NAME` VARCHAR(200) NOT NULL,
  `CALENDAR`      BLOB         NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `CALENDAR_NAME`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for QRTZ_CRON_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_CRON_TRIGGERS`;
CREATE TABLE `QRTZ_CRON_TRIGGERS` (
  `SCHED_NAME`      VARCHAR(120) NOT NULL,
  `TRIGGER_NAME`    VARCHAR(200) NOT NULL,
  `TRIGGER_GROUP`   VARCHAR(200) NOT NULL,
  `CRON_EXPRESSION` VARCHAR(120) NOT NULL,
  `TIME_ZONE_ID`    VARCHAR(80) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`),
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for QRTZ_FIRED_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_FIRED_TRIGGERS`;
CREATE TABLE `QRTZ_FIRED_TRIGGERS` (
  `SCHED_NAME`        VARCHAR(120) NOT NULL,
  `ENTRY_ID`          VARCHAR(95)  NOT NULL,
  `TRIGGER_NAME`      VARCHAR(200) NOT NULL,
  `TRIGGER_GROUP`     VARCHAR(200) NOT NULL,
  `INSTANCE_NAME`     VARCHAR(200) NOT NULL,
  `FIRED_TIME`        BIGINT(13)   NOT NULL,
  `SCHED_TIME`        BIGINT(13)   NOT NULL,
  `PRIORITY`          INT(11)      NOT NULL,
  `STATE`             VARCHAR(16)  NOT NULL,
  `JOB_NAME`          VARCHAR(200) DEFAULT NULL,
  `JOB_GROUP`         VARCHAR(200) DEFAULT NULL,
  `IS_NONCONCURRENT`  VARCHAR(1)   DEFAULT NULL,
  `REQUESTS_RECOVERY` VARCHAR(1)   DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `ENTRY_ID`),
  KEY `IDX_QRTZ_FT_TRIG_INST_NAME` (`SCHED_NAME`, `INSTANCE_NAME`),
  KEY `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY` (`SCHED_NAME`, `INSTANCE_NAME`, `REQUESTS_RECOVERY`),
  KEY `IDX_QRTZ_FT_J_G` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`),
  KEY `IDX_QRTZ_FT_JG` (`SCHED_NAME`, `JOB_GROUP`),
  KEY `IDX_QRTZ_FT_T_G` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`),
  KEY `IDX_QRTZ_FT_TG` (`SCHED_NAME`, `TRIGGER_GROUP`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for QRTZ_JOB_DETAILS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_JOB_DETAILS`;
CREATE TABLE `QRTZ_JOB_DETAILS` (
  `SCHED_NAME`        VARCHAR(120) NOT NULL,
  `JOB_NAME`          VARCHAR(200) NOT NULL,
  `JOB_GROUP`         VARCHAR(200) NOT NULL,
  `DESCRIPTION`       VARCHAR(250) DEFAULT NULL,
  `JOB_CLASS_NAME`    VARCHAR(250) NOT NULL,
  `IS_DURABLE`        VARCHAR(1)   NOT NULL,
  `IS_NONCONCURRENT`  VARCHAR(1)   NOT NULL,
  `IS_UPDATE_DATA`    VARCHAR(1)   NOT NULL,
  `REQUESTS_RECOVERY` VARCHAR(1)   NOT NULL,
  `JOB_DATA`          BLOB,
  PRIMARY KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`),
  KEY `IDX_QRTZ_J_REQ_RECOVERY` (`SCHED_NAME`, `REQUESTS_RECOVERY`),
  KEY `IDX_QRTZ_J_GRP` (`SCHED_NAME`, `JOB_GROUP`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for QRTZ_LOCKS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_LOCKS`;
CREATE TABLE `QRTZ_LOCKS` (
  `SCHED_NAME` VARCHAR(120) NOT NULL,
  `LOCK_NAME`  VARCHAR(40)  NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `LOCK_NAME`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for QRTZ_PAUSED_TRIGGER_GRPS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_PAUSED_TRIGGER_GRPS`;
CREATE TABLE `QRTZ_PAUSED_TRIGGER_GRPS` (
  `SCHED_NAME`    VARCHAR(120) NOT NULL,
  `TRIGGER_GROUP` VARCHAR(200) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_GROUP`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for QRTZ_SCHEDULER_STATE
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SCHEDULER_STATE`;
CREATE TABLE `QRTZ_SCHEDULER_STATE` (
  `SCHED_NAME`        VARCHAR(120) NOT NULL,
  `INSTANCE_NAME`     VARCHAR(200) NOT NULL,
  `LAST_CHECKIN_TIME` BIGINT(13)   NOT NULL,
  `CHECKIN_INTERVAL`  BIGINT(13)   NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `INSTANCE_NAME`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for QRTZ_SIMPLE_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SIMPLE_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPLE_TRIGGERS` (
  `SCHED_NAME`      VARCHAR(120) NOT NULL,
  `TRIGGER_NAME`    VARCHAR(200) NOT NULL,
  `TRIGGER_GROUP`   VARCHAR(200) NOT NULL,
  `REPEAT_COUNT`    BIGINT(7)    NOT NULL,
  `REPEAT_INTERVAL` BIGINT(12)   NOT NULL,
  `TIMES_TRIGGERED` BIGINT(10)   NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`),
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for QRTZ_SIMPROP_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SIMPROP_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPROP_TRIGGERS` (
  `SCHED_NAME`    VARCHAR(120) NOT NULL,
  `TRIGGER_NAME`  VARCHAR(200) NOT NULL,
  `TRIGGER_GROUP` VARCHAR(200) NOT NULL,
  `STR_PROP_1`    VARCHAR(512)   DEFAULT NULL,
  `STR_PROP_2`    VARCHAR(512)   DEFAULT NULL,
  `STR_PROP_3`    VARCHAR(512)   DEFAULT NULL,
  `INT_PROP_1`    INT(11)        DEFAULT NULL,
  `INT_PROP_2`    INT(11)        DEFAULT NULL,
  `LONG_PROP_1`   BIGINT(20)     DEFAULT NULL,
  `LONG_PROP_2`   BIGINT(20)     DEFAULT NULL,
  `DEC_PROP_1`    DECIMAL(13, 4) DEFAULT NULL,
  `DEC_PROP_2`    DECIMAL(13, 4) DEFAULT NULL,
  `BOOL_PROP_1`   VARCHAR(1)     DEFAULT NULL,
  `BOOL_PROP_2`   VARCHAR(1)     DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`),
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for QRTZ_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_TRIGGERS`;
CREATE TABLE `QRTZ_TRIGGERS` (
  `SCHED_NAME`     VARCHAR(120) NOT NULL,
  `TRIGGER_NAME`   VARCHAR(200) NOT NULL,
  `TRIGGER_GROUP`  VARCHAR(200) NOT NULL,
  `JOB_NAME`       VARCHAR(200) NOT NULL,
  `JOB_GROUP`      VARCHAR(200) NOT NULL,
  `DESCRIPTION`    VARCHAR(250) DEFAULT NULL,
  `NEXT_FIRE_TIME` BIGINT(13)   DEFAULT NULL,
  `PREV_FIRE_TIME` BIGINT(13)   DEFAULT NULL,
  `PRIORITY`       INT(11)      DEFAULT NULL,
  `TRIGGER_STATE`  VARCHAR(16)  NOT NULL,
  `TRIGGER_TYPE`   VARCHAR(8)   NOT NULL,
  `START_TIME`     BIGINT(13)   NOT NULL,
  `END_TIME`       BIGINT(13)   DEFAULT NULL,
  `CALENDAR_NAME`  VARCHAR(200) DEFAULT NULL,
  `MISFIRE_INSTR`  SMALLINT(2)  DEFAULT NULL,
  `JOB_DATA`       BLOB,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`),
  KEY `IDX_QRTZ_T_J` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`),
  KEY `IDX_QRTZ_T_JG` (`SCHED_NAME`, `JOB_GROUP`),
  KEY `IDX_QRTZ_T_C` (`SCHED_NAME`, `CALENDAR_NAME`),
  KEY `IDX_QRTZ_T_G` (`SCHED_NAME`, `TRIGGER_GROUP`),
  KEY `IDX_QRTZ_T_STATE` (`SCHED_NAME`, `TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_N_STATE` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`, `TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_N_G_STATE` (`SCHED_NAME`, `TRIGGER_GROUP`, `TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_NEXT_FIRE_TIME` (`SCHED_NAME`, `NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_ST` (`SCHED_NAME`, `TRIGGER_STATE`, `NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_MISFIRE` (`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE` (`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`, `TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP` (`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`, `TRIGGER_GROUP`, `TRIGGER_STATE`),
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `QRTZ_JOB_DETAILS` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for TB_AT_EVENTBUS_DEF
-- ----------------------------
DROP TABLE IF EXISTS `TB_AT_EVENTBUS_DEF`;
CREATE TABLE `TB_AT_EVENTBUS_DEF` (
  `ID`                   BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `EVENTBUS_NAME`        VARCHAR(100)
                         COLLATE utf8mb4_unicode_ci   DEFAULT NULL,
  `EVENT_NAME`           VARBINARY(100)               DEFAULT NULL,
  `LISTERNER_CLASS_NAME` VARCHAR(255)
                         COLLATE utf8mb4_unicode_ci   DEFAULT NULL,
  `MEMO`                 VARCHAR(100)
                         COLLATE utf8mb4_unicode_ci   DEFAULT NULL,
  `IS_AVAILABLE`         TINYINT(1)                   DEFAULT '1',
  `IS_ASYNC`             TINYINT(1)                   DEFAULT '0',
  `CREATE_TIME`          DATETIME                     DEFAULT NULL,
  PRIMARY KEY (`ID`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for TB_AT_SCHEDULE_JOB
-- ----------------------------
DROP TABLE IF EXISTS `TB_AT_SCHEDULE_JOB`;
CREATE TABLE `TB_AT_SCHEDULE_JOB` (
  `ID`                BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `JOBNAME`           VARCHAR(200)
                      COLLATE utf8mb4_unicode_ci   DEFAULT NULL,
  `JOBGROUP`          VARBINARY(200)               DEFAULT NULL,
  `CRON_EXPRESSION`   VARCHAR(100)
                      COLLATE utf8mb4_unicode_ci   DEFAULT NULL,
  `RUN_TYPE`          TINYINT(1)                   DEFAULT '0'
  COMMENT '1:手动 0:自动',
  `RUN_STATUS`        TINYINT(1)                   DEFAULT '0'
  COMMENT '1:启动 0:停止',
  `TARGET_CLASS_NAME` VARCHAR(200)
                      COLLATE utf8mb4_unicode_ci   DEFAULT NULL,
  `DESCRTPTION`       VARCHAR(255)
                      COLLATE utf8mb4_unicode_ci   DEFAULT NULL,
  `CREATE_TIME`       DATETIME                     DEFAULT NULL,
  `EXECUTE_TIME`      DATETIME                     DEFAULT NULL,
  `MODIFY_TIME`       DATETIME                     DEFAULT NULL,
  `IS_ENABLED`        TINYINT(1)                   DEFAULT '1'
  COMMENT '1:有效 0:无效',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `JOBNAME_UNIQUE_INDEX` (`JOBNAME`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for TB_AT_SCHEDULE_RECORD
-- ----------------------------
DROP TABLE IF EXISTS `TB_AT_SCHEDULE_RECORD`;
CREATE TABLE `TB_AT_SCHEDULE_RECORD` (
  `ID`              BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `TASK_ID`         BIGINT(20)                   DEFAULT NULL,
  `TRIGGER_INST_ID` VARCHAR(200)
                    COLLATE utf8mb4_unicode_ci   DEFAULT NULL,
  `START_TIME`      DATETIME                     DEFAULT NULL,
  `END_TIME`        DATETIME                     DEFAULT NULL,
  `COST`            BIGINT(20)                   DEFAULT NULL,
  PRIMARY KEY (`ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for tb_at_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_at_user`;
CREATE TABLE `tb_at_user` (
  `ID`      INT(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` INT(11)          DEFAULT NULL,
  `NAME`    VARCHAR(20)      DEFAULT NULL,
  `BIRTH`   DATETIME         DEFAULT NULL,
  `EMAIL`   VARCHAR(20)      DEFAULT NULL,
  PRIMARY KEY (`ID`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id`   INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(20)      DEFAULT NULL,
  `age`  INT(11)          DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 7
  DEFAULT CHARSET = utf8;