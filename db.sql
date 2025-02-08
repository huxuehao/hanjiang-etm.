/*
 Navicat Premium Data Transfer

 Source Server         : localhost（本地）
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : localhost:3306
 Source Schema         : hanjiang-etm

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 08/02/2025 16:13:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_attach
-- ----------------------------
DROP TABLE IF EXISTS `t_attach`;
CREATE TABLE `t_attach`  (
                             `id` bigint(0) NOT NULL COMMENT '主键',
                             `file_id` bigint(0) NULL DEFAULT NULL COMMENT '文件id',
                             `link` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件地址',
                             `domain` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件域名',
                             `name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件名称',
                             `original_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件原名',
                             `extension` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件拓展名',
                             `attach_size` bigint(0) NULL DEFAULT NULL COMMENT '附件大小',
                             `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储路径',
                             `create_user` bigint(0) NULL DEFAULT NULL COMMENT '创建人',
                             `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                             `update_user` bigint(0) NULL DEFAULT NULL COMMENT '修改人',
                             `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                             `protocol` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储协议',
                             `status` int(0) NULL DEFAULT NULL COMMENT '状态',
                             `del_flag` int(0) NULL DEFAULT 0 COMMENT '是否已删除',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '附件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_attach
-- ----------------------------
INSERT INTO `t_attach`(`id`, `file_id`, `link`, `domain`, `name`, `original_name`, `extension`, `attach_size`, `path`, `create_user`, `create_time`, `update_user`, `update_time`, `protocol`, `status`, `del_flag`) VALUES (1880994709917724674, 1880994709917724673, NULL, NULL, NULL, '新建文本文档.txt', 'txt', 929, '2025/01/19', 1111111111111111111, '2025-01-19 23:04:21', NULL, NULL, 'LOCAL', NULL, 0);


-- ----------------------------
-- Table structure for t_attach_chunk
-- ----------------------------
DROP TABLE IF EXISTS `t_attach_chunk`;
CREATE TABLE `t_attach_chunk`  (
                                   `id` bigint(0) NOT NULL COMMENT '主键',
                                   `chunk_hash` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分片的hash值',
                                   `chunk_index` int(0) NULL DEFAULT NULL COMMENT '分片的索引',
                                   `chunk_totals` int(0) NULL DEFAULT NULL COMMENT '分片总数',
                                   `file_key` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件唯一标识',
                                   `file_total_size` int(0) NULL DEFAULT NULL COMMENT '文件大小',
                                   `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件名称',
                                   `create_user` bigint(0) NULL DEFAULT NULL COMMENT '创建人',
                                   `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '附件表分片记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_attach_chunk
-- ----------------------------

-- ----------------------------
-- Table structure for t_attach_log
-- ----------------------------
DROP TABLE IF EXISTS `t_attach_log`;
CREATE TABLE `t_attach_log`  (
                                 `id` bigint(0) NOT NULL COMMENT '主键',
                                 `file_id` bigint(0) NULL DEFAULT NULL COMMENT '文件id',
                                 `original_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件原名',
                                 `extension` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件拓展名',
                                 `attach_size` bigint(0) NULL DEFAULT NULL COMMENT '附件大小',
                                 `opt_user` bigint(0) NULL DEFAULT NULL COMMENT '操作人',
                                 `opt_user_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作人名称',
                                 `opt_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
                                 `opt_ip` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作IP',
                                 `opt_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作类型',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '附件操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_attach_log
-- ----------------------------
INSERT INTO `t_attach_log`(`id`, `file_id`, `original_name`, `extension`, `attach_size`, `opt_user`, `opt_user_name`, `opt_time`, `opt_ip`, `opt_type`) VALUES (1881001037180555266, 1880994709917724673, '新建文本文档.txt', 'txt', 929, 1111111111111111111, '超级管理员', '2025-01-19 23:29:29', '127.0.0.1', 'UPLOAD');

-- ----------------------------
-- Table structure for t_dingding_qr_config
-- ----------------------------
DROP TABLE IF EXISTS `t_dingding_qr_config`;
CREATE TABLE `t_dingding_qr_config`  (
                                         `id` bigint(0) NOT NULL COMMENT '主键',
                                         `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
                                         `mini_app_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '小程序miniAppId',
                                         `agent_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '小程序应用的agentId',
                                         `corp_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组织corpId',
                                         `page` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '小程序某个页面的路径',
                                         `enabled` int(0) NULL DEFAULT 0 COMMENT '是否启用',
                                         `del_flag` int(0) NULL DEFAULT 0 COMMENT '是否删除',
                                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '打开钉钉小程序二维码生成配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_dingding_qr_config
-- ----------------------------

-- ----------------------------
-- Table structure for t_exam
-- ----------------------------
DROP TABLE IF EXISTS `t_exam`;
CREATE TABLE `t_exam`  (
                           `id` bigint(0) NOT NULL COMMENT '主键',
                           `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
                           `classify_id` bigint(0) NULL DEFAULT NULL COMMENT '分类',
                           `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编号',
                           `create_user` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
                           `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                           `current_type` bigint(0) NULL DEFAULT NULL COMMENT '当前考试类型（1系统内部考试，0扫码开放考试）',
                           `limit_start_time` datetime(0) NULL DEFAULT NULL COMMENT '考试开始时间',
                           `limit_end_time` datetime(0) NULL DEFAULT NULL COMMENT '考试结束时间',
                           `valid` int(0) NULL DEFAULT 1 COMMENT '是否有效（1有效，0无效）',
                           `del_flag` int(0) NULL DEFAULT 0 COMMENT '是否删除（1删除，0未删除）',
                           `publish_time` datetime(0) NULL DEFAULT NULL COMMENT '发布时间',
                           `published` int(0) NULL DEFAULT NULL COMMENT '发布状态（1已发布，0未发布）',
                           `duration` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '考试时长',
                           `check_type` int(0) NULL DEFAULT NULL COMMENT '阅卷方式（0机器，1人工）',
                           `success_score` int(0) NULL DEFAULT NULL COMMENT '合格分数',
                           `paper_id` bigint(0) NULL DEFAULT NULL COMMENT '试卷模版ID',
                           `cheat` int(0) NULL DEFAULT 0 COMMENT '是否防作弊（0关闭，1开启）',
                           `question_rule` int(0) NULL DEFAULT NULL COMMENT '题目规则（0固定题目，1随机题目）',
                           `classifies` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '随机题目依赖分类',
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '考试表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_exam
-- ----------------------------
INSERT INTO `t_exam` VALUES (1879079215375646722, '这是一个考试测试', 1850706920052195329, NULL, '1111111111111111111', '2025-01-14 16:12:51', 1, '2025-01-14 16:10:34', '2025-01-21 16:10:35', 1, 0, '2025-01-15 15:31:24', 1, '60', 1, 60, 1846020501915930625, 0, 0, NULL);

-- ----------------------------
-- Table structure for t_exam_classify
-- ----------------------------
DROP TABLE IF EXISTS `t_exam_classify`;
CREATE TABLE `t_exam_classify`  (
                                    `id` bigint(0) NOT NULL COMMENT '主键',
                                    `parent_id` bigint(0) NULL DEFAULT 0 COMMENT '父ID',
                                    `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编号',
                                    `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
                                    `desc` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
                                    `del_flag` int(0) NULL DEFAULT 0 COMMENT '是否删除（0未删除，1删除）',
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '考试分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_exam_classify
-- ----------------------------
INSERT INTO `t_exam_classify` VALUES (1850706813621731329, 0, 'k11111', '考试1', NULL, 1);
INSERT INTO `t_exam_classify` VALUES (1850706874481082370, 0, 'k22222', '考试2', NULL, 1);
INSERT INTO `t_exam_classify` VALUES (1850706920052195329, 0, 'k111112', '测试', NULL, 0);
INSERT INTO `t_exam_classify` VALUES (1850706964226605057, 1850706813621731329, 'k222221', '考试2', NULL, 1);
INSERT INTO `t_exam_classify` VALUES (1867417699208482818, 0, '2', '122', NULL, 1);

-- ----------------------------
-- Table structure for t_exam_corrector
-- ----------------------------
DROP TABLE IF EXISTS `t_exam_corrector`;
CREATE TABLE `t_exam_corrector`  (
                                     `exam_id` bigint(0) NOT NULL COMMENT '考试ID',
                                     `user_id` bigint(0) NOT NULL COMMENT '用户ID',
                                     `user_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
                                     `user_code` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户编号',
                                     `org_id` bigint(0) NULL DEFAULT NULL COMMENT '组织机构ID',
                                     `org_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组织机构名称',
                                     UNIQUE INDEX `idx_uni`(`exam_id`, `user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '考试审批用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_exam_corrector
-- ----------------------------
INSERT INTO `t_exam_corrector` VALUES (1879079215375646722, 1111111111111111111, '超级管理员', 'administrator', 1850706813621731329, NULL);
INSERT INTO `t_exam_corrector` VALUES (1879079215375646722, 1873746662934450177, '寒江', 'HJ0001', 1850706813621731329, NULL);
INSERT INTO `t_exam_corrector` VALUES (1879079215375646722, 1873749058125398017, '东瑞', 'HJ0002', 1850706813621731329, NULL);

-- ----------------------------
-- Table structure for t_exam_paper
-- ----------------------------
DROP TABLE IF EXISTS `t_exam_paper`;
CREATE TABLE `t_exam_paper`  (
                                 `id` bigint(0) NOT NULL COMMENT '主键',
                                 `paper_id` bigint(0) NULL DEFAULT NULL COMMENT '试卷模版ID',
                                 `config` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '试题配置（基于试卷模版）',
                                 `del_flag` int(0) NULL DEFAULT 0 COMMENT '是否删除（1删除，0未删除）',
                                 `exam_id` bigint(0) NULL DEFAULT NULL COMMENT '考试ID',
                                 `sort` int(0) NULL DEFAULT NULL COMMENT '试题排序',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '考试-试卷表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_exam_paper
-- ----------------------------
INSERT INTO `t_exam_paper` VALUES (1879087704592416769, NULL, '{\"title\":\"单选题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":10,\"level\":2,\"type\":1,\"share\":1,\"success\":-1,\"rowKey\":\"0\"}', 1, 1879079215375646722, 0);
INSERT INTO `t_exam_paper` VALUES (1879087704592416770, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一选项一选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"analysis\":\"这是一个解析。\",\"level\":1,\"type\":1,\"share\":1,\"id\":\"1844589207403139073\",\"success\":-1,\"rowKey\":\"1\"}', 1, 1879079215375646722, 1);
INSERT INTO `t_exam_paper` VALUES (1879087704592416771, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一选项一选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":5,\"level\":1,\"type\":1,\"share\":0,\"id\":\"1844589234568139073\",\"success\":-1,\"rowKey\":\"2\"}', 1, 1879079215375646722, 2);
INSERT INTO `t_exam_paper` VALUES (1879087704592416772, NULL, '{\"classifyId\":\"1844561674964144130\",\"name\":\"单选题\",\"title\":\"这是一道多选题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1},{\"rowKey\":\"3\",\"title\":\"D\",\"value\":\"选项四\",\"selected\":0,\"sort\":1}],\"answer\":[\"B\",\"C\"],\"score\":10,\"level\":2,\"type\":2,\"share\":1,\"id\":\"1844631436142407682\",\"success\":-1,\"rowKey\":\"3\"}', 1, 1879079215375646722, 3);
INSERT INTO `t_exam_paper` VALUES (1879087704592416773, NULL, '{\"classifyId\":\"1844561875095359490\",\"name\":\"判断题\",\"title\":\"这是一道判断题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"level\":3,\"type\":3,\"share\":1,\"id\":\"1844631640312345921\",\"success\":-1,\"rowKey\":\"4\"}', 1, 1879079215375646722, 4);
INSERT INTO `t_exam_paper` VALUES (1879087704592416774, NULL, '{\"classifyId\":\"1844561875095359490\",\"name\":\"判断题\",\"title\":\"这是一道判断题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":5,\"level\":3,\"type\":3,\"share\":1,\"id\":\"1844631640342097921\",\"success\":-1,\"rowKey\":\"5\"}', 1, 1879079215375646722, 5);
INSERT INTO `t_exam_paper` VALUES (1879087704592416775, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"填空题\",\"title\":\"这是(__)一道填(__)空题(__)。\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"2\",\"title\":\"第三个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5},{\"rowKey\":\"2\",\"title\":\"第三个填空\",\"value\":\"答案三\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":4,\"type\":4,\"share\":1,\"id\":\"1844631905887234566\",\"success\":-1,\"rowKey\":\"6\"}', 1, 1879079215375646722, 6);
INSERT INTO `t_exam_paper` VALUES (1879087704659525633, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"填空题\",\"title\":\"这是(__)一道(__)填空题。\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":4,\"type\":4,\"share\":1,\"id\":\"1844631905887678466\",\"success\":-1,\"rowKey\":\"7\"}', 1, 1879079215375646722, 7);
INSERT INTO `t_exam_paper` VALUES (1879087704659525634, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"解答题\",\"title\":\"这是一道简答题，对此你有什么理解？这是一道简答题，对此你有什么理解？\",\"score\":15,\"level\":5,\"type\":5,\"share\":1,\"success\":-1,\"keyWords\":[\"1111\",\"2222\"],\"matchNum\":2,\"id\":\"1846925581972267010\",\"rowKey\":\"8\"}', 1, 1879079215375646722, 8);
INSERT INTO `t_exam_paper` VALUES (1879096284255096834, NULL, '{\"title\":\"单选题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":10,\"level\":2,\"type\":1,\"share\":1,\"success\":-1,\"rowKey\":\"0\"}', 1, 1879079215375646722, 0);
INSERT INTO `t_exam_paper` VALUES (1879096284255096835, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一选项一选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"analysis\":\"这是一个解析。\",\"level\":1,\"type\":1,\"share\":1,\"id\":\"1844589207403139073\",\"success\":-1,\"rowKey\":\"1\"}', 1, 1879079215375646722, 1);
INSERT INTO `t_exam_paper` VALUES (1879096284255096836, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一选项一选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":5,\"level\":1,\"type\":1,\"share\":0,\"id\":\"1844589234568139073\",\"success\":-1,\"rowKey\":\"2\"}', 1, 1879079215375646722, 2);
INSERT INTO `t_exam_paper` VALUES (1879096284255096837, NULL, '{\"classifyId\":\"1844561674964144130\",\"name\":\"单选题\",\"title\":\"这是一道多选题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1},{\"rowKey\":\"3\",\"title\":\"D\",\"value\":\"选项四\",\"selected\":0,\"sort\":1}],\"answer\":[\"B\",\"C\"],\"score\":10,\"level\":2,\"type\":2,\"share\":1,\"id\":\"1844631436142407682\",\"success\":-1,\"rowKey\":\"3\"}', 1, 1879079215375646722, 3);
INSERT INTO `t_exam_paper` VALUES (1879096284255096838, NULL, '{\"classifyId\":\"1844561875095359490\",\"name\":\"判断题\",\"title\":\"这是一道判断题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"level\":3,\"type\":3,\"share\":1,\"id\":\"1844631640312345921\",\"success\":-1,\"rowKey\":\"4\"}', 1, 1879079215375646722, 4);
INSERT INTO `t_exam_paper` VALUES (1879096284255096839, NULL, '{\"classifyId\":\"1844561875095359490\",\"name\":\"判断题\",\"title\":\"这是一道判断题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":5,\"level\":3,\"type\":3,\"share\":1,\"id\":\"1844631640342097921\",\"success\":-1,\"rowKey\":\"5\"}', 1, 1879079215375646722, 5);
INSERT INTO `t_exam_paper` VALUES (1879096284255096840, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"填空题\",\"title\":\"这是(__)一道填(__)空题(__)。\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"2\",\"title\":\"第三个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5},{\"rowKey\":\"2\",\"title\":\"第三个填空\",\"value\":\"答案三\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":4,\"type\":4,\"share\":1,\"id\":\"1844631905887234566\",\"success\":-1,\"rowKey\":\"6\"}', 1, 1879079215375646722, 6);
INSERT INTO `t_exam_paper` VALUES (1879096284255096841, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"填空题\",\"title\":\"这是(__)一道(__)填空题。\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":4,\"type\":4,\"share\":1,\"id\":\"1844631905887678466\",\"success\":-1,\"rowKey\":\"7\"}', 1, 1879079215375646722, 7);
INSERT INTO `t_exam_paper` VALUES (1879096284255096842, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"解答题\",\"title\":\"这是一道简答题，对此你有什么理解？这是一道简答题，对此你有什么理解？\",\"score\":15,\"level\":5,\"type\":5,\"share\":1,\"success\":-1,\"keyWords\":[\"1111\",\"2222\"],\"matchNum\":2,\"id\":\"1846925581972267010\",\"rowKey\":\"8\"}', 1, 1879079215375646722, 8);
INSERT INTO `t_exam_paper` VALUES (1879431156412817410, NULL, '{\"title\":\"单选题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":10,\"level\":2,\"type\":1,\"share\":1,\"success\":-1,\"rowKey\":\"0\"}', 0, 1879079215375646722, 0);
INSERT INTO `t_exam_paper` VALUES (1879431156412817411, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一选项一选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"analysis\":\"这是一个解析。\",\"level\":1,\"type\":1,\"share\":1,\"id\":\"1844589207403139073\",\"success\":-1,\"rowKey\":\"1\"}', 0, 1879079215375646722, 1);
INSERT INTO `t_exam_paper` VALUES (1879431156412817412, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一选项一选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":5,\"level\":1,\"type\":1,\"share\":0,\"id\":\"1844589234568139073\",\"success\":-1,\"rowKey\":\"2\"}', 0, 1879079215375646722, 2);
INSERT INTO `t_exam_paper` VALUES (1879431156412817413, NULL, '{\"classifyId\":\"1844561674964144130\",\"name\":\"单选题\",\"title\":\"这是一道多选题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1},{\"rowKey\":\"3\",\"title\":\"D\",\"value\":\"选项四\",\"selected\":0,\"sort\":1}],\"answer\":[\"B\",\"C\"],\"score\":10,\"level\":2,\"type\":2,\"share\":1,\"id\":\"1844631436142407682\",\"success\":-1,\"rowKey\":\"3\"}', 0, 1879079215375646722, 3);
INSERT INTO `t_exam_paper` VALUES (1879431156412817414, NULL, '{\"classifyId\":\"1844561875095359490\",\"name\":\"判断题\",\"title\":\"这是一道判断题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"level\":3,\"type\":3,\"share\":1,\"id\":\"1844631640312345921\",\"success\":-1,\"rowKey\":\"4\"}', 0, 1879079215375646722, 4);
INSERT INTO `t_exam_paper` VALUES (1879431156412817415, NULL, '{\"classifyId\":\"1844561875095359490\",\"name\":\"判断题\",\"title\":\"这是一道判断题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":5,\"level\":3,\"type\":3,\"share\":1,\"id\":\"1844631640342097921\",\"success\":-1,\"rowKey\":\"5\"}', 0, 1879079215375646722, 5);
INSERT INTO `t_exam_paper` VALUES (1879431156412817416, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"填空题\",\"title\":\"这是(__)一道填(__)空题(__)。\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"2\",\"title\":\"第三个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5},{\"rowKey\":\"2\",\"title\":\"第三个填空\",\"value\":\"答案三\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":4,\"type\":4,\"share\":1,\"id\":\"1844631905887234566\",\"success\":-1,\"rowKey\":\"6\"}', 0, 1879079215375646722, 6);
INSERT INTO `t_exam_paper` VALUES (1879431156412817417, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"填空题\",\"title\":\"这是(__)一道(__)填空题。\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":4,\"type\":4,\"share\":1,\"id\":\"1844631905887678466\",\"success\":-1,\"rowKey\":\"7\"}', 0, 1879079215375646722, 7);
INSERT INTO `t_exam_paper` VALUES (1879431156412817418, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"解答题\",\"title\":\"这是一道简答题，对此你有什么理解？这是一道简答题，对此你有什么理解？\",\"score\":15,\"level\":5,\"type\":5,\"share\":1,\"success\":-1,\"keyWords\":[\"1111\",\"2222\"],\"matchNum\":2,\"id\":\"1846925581972267010\",\"rowKey\":\"8\"}', 0, 1879079215375646722, 8);

-- ----------------------------
-- Table structure for t_exam_paper_user
-- ----------------------------
DROP TABLE IF EXISTS `t_exam_paper_user`;
CREATE TABLE `t_exam_paper_user`  (
                                      `id` bigint(0) NOT NULL COMMENT '主键',
                                      `user_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户编号',
                                      `exam_id` bigint(0) NULL DEFAULT NULL COMMENT '考试ID',
                                      `exam_paper_id` bigint(0) NULL DEFAULT NULL COMMENT '考试试题ID',
                                      `config` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '试卷配置',
                                      `do_start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始答题时间',
                                      `do_end_time` datetime(0) NULL DEFAULT NULL COMMENT '提交试卷时间',
                                      `score` int(0) NULL DEFAULT NULL COMMENT '得分',
                                      `success` int(0) NULL DEFAULT NULL COMMENT '是否合格（1合格，0不合格）',
                                      `corrected` int(0) NULL DEFAULT 0 COMMENT '是否批改（1已批改，0未批改，-1批改中）',
                                      `correct_user` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批改人',
                                      `correct_time` datetime(0) NULL DEFAULT NULL COMMENT '批改时间',
                                      `del_flag` int(0) NULL DEFAULT 0 COMMENT '是否删除',
                                      `paper_done` int(0) NULL DEFAULT 0 COMMENT '是否完成答题（1完成，0未完成）',
                                      `sort` int(0) NULL DEFAULT NULL COMMENT '排序',
                                      PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '参考试人员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_exam_paper_user
-- ----------------------------
INSERT INTO `t_exam_paper_user` VALUES (1879431170174328833, '1111111111111111111', 1879079215375646722, 1879431156412817410, '{\"already\":false,\"answer\":\"B\",\"level\":2,\"option\":[{\"selected\":0,\"sort\":1,\"title\":\"A\",\"value\":\"选项一\"},{\"selected\":1,\"sort\":1,\"title\":\"B\",\"value\":\"选项二\"},{\"selected\":0,\"sort\":1,\"title\":\"C\",\"value\":\"选项三\"}],\"rowKey\":\"0\",\"score\":10,\"scoreBySuccess\":10,\"share\":1,\"success\":1,\"title\":\"单选题\",\"type\":1}', NULL, '2025-01-15 15:35:04', 10, 1, 1, '1111111111111111111', NULL, 0, 1, 0);
INSERT INTO `t_exam_paper_user` VALUES (1879431170174328834, '1111111111111111111', 1879079215375646722, 1879431156412817411, '{\"already\":false,\"analysis\":\"这是一个解析。\",\"answer\":\"A\",\"classifyId\":1844562179480195000,\"level\":1,\"name\":\"单选题\",\"option\":[{\"selected\":0,\"sort\":1,\"title\":\"A\",\"value\":\"选项一选项一选项一\"},{\"selected\":1,\"sort\":1,\"title\":\"B\",\"value\":\"选项二\"},{\"selected\":0,\"sort\":1,\"title\":\"C\",\"value\":\"选项三\"}],\"rowKey\":\"1\",\"score\":5,\"scoreBySuccess\":0,\"share\":1,\"success\":0,\"title\":\"这是一个单选测试？\",\"type\":1}', NULL, '2025-01-15 15:35:04', 0, 0, 1, '1111111111111111111', NULL, 0, 1, 1);
INSERT INTO `t_exam_paper_user` VALUES (1879431170174328835, '1111111111111111111', 1879079215375646722, 1879431156412817412, '{\"already\":false,\"answer\":\"B\",\"classifyId\":1844562179480195000,\"level\":1,\"name\":\"单选题\",\"option\":[{\"selected\":0,\"sort\":1,\"title\":\"A\",\"value\":\"选项一选项一选项一\"},{\"selected\":1,\"sort\":1,\"title\":\"B\",\"value\":\"选项二\"},{\"selected\":0,\"sort\":1,\"title\":\"C\",\"value\":\"选项三\"}],\"rowKey\":\"2\",\"score\":5,\"scoreBySuccess\":5,\"share\":0,\"success\":1,\"title\":\"这是一个单选测试？\",\"type\":1}', NULL, '2025-01-15 15:35:04', 5, 1, 1, '1111111111111111111', NULL, 0, 1, 2);
INSERT INTO `t_exam_paper_user` VALUES (1879431170174328836, '1111111111111111111', 1879079215375646722, 1879431156412817413, '{\"already\":false,\"answer\":[\"B\",\"C\"],\"classifyId\":1844561674964144000,\"level\":2,\"name\":\"单选题\",\"option\":[{\"selected\":0,\"sort\":1,\"title\":\"A\",\"value\":\"选项一\"},{\"selected\":1,\"sort\":1,\"title\":\"B\",\"value\":\"选项二\"},{\"selected\":1,\"sort\":1,\"title\":\"C\",\"value\":\"选项三\"},{\"selected\":0,\"sort\":1,\"title\":\"D\",\"value\":\"选项四\"}],\"rowKey\":\"3\",\"score\":10,\"scoreBySuccess\":10,\"share\":1,\"success\":1,\"title\":\"这是一道多选题？\",\"type\":2}', NULL, '2025-01-15 15:35:04', 10, 1, 1, '1111111111111111111', NULL, 0, 1, 3);
INSERT INTO `t_exam_paper_user` VALUES (1879431170174328837, '1111111111111111111', 1879079215375646722, 1879431156412817414, '{\"already\":false,\"answer\":\"A\",\"classifyId\":1844561875095359500,\"level\":3,\"name\":\"判断题\",\"option\":[{\"selected\":0,\"sort\":1,\"title\":\"A\",\"value\":\"对\"},{\"selected\":1,\"sort\":1,\"title\":\"B\",\"value\":\"错\"}],\"rowKey\":\"4\",\"score\":5,\"scoreBySuccess\":0,\"share\":1,\"success\":0,\"title\":\"这是一道判断题？\",\"type\":3}', NULL, '2025-01-15 15:35:04', 0, 0, 1, '1111111111111111111', NULL, 0, 1, 4);
INSERT INTO `t_exam_paper_user` VALUES (1879431170241437697, '1111111111111111111', 1879079215375646722, 1879431156412817415, '{\"already\":false,\"answer\":\"B\",\"classifyId\":1844561875095359500,\"level\":3,\"name\":\"判断题\",\"option\":[{\"selected\":0,\"sort\":1,\"title\":\"A\",\"value\":\"对\"},{\"selected\":0,\"sort\":1,\"title\":\"B\",\"value\":\"错\"}],\"rowKey\":\"5\",\"score\":5,\"scoreBySuccess\":0,\"share\":1,\"success\":0,\"title\":\"这是一道判断题？\",\"type\":3}', NULL, '2025-01-15 15:35:04', 0, 0, 1, '1111111111111111111', NULL, 0, 1, 5);
INSERT INTO `t_exam_paper_user` VALUES (1879431170241437698, '1111111111111111111', 1879079215375646722, 1879431156412817416, '{\"already\":false,\"answer\":[{\"selected\":0,\"sort\":5,\"title\":\"第一个填空\",\"value\":\"答案一\"},{\"selected\":0,\"sort\":5,\"title\":\"第二个填空\",\"value\":\"答案二\"},{\"selected\":0,\"sort\":5,\"title\":\"第三个填空\",\"value\":\"答案三\"}],\"classifyId\":1844562179480195000,\"level\":4,\"name\":\"填空题\",\"option\":[{\"selected\":0,\"sort\":5,\"title\":\"第一个填空\"},{\"selected\":0,\"sort\":5,\"title\":\"第二个填空\"},{\"selected\":0,\"sort\":5,\"title\":\"第三个填空\"}],\"rowKey\":\"6\",\"score\":5,\"scoreBySuccess\":0,\"share\":1,\"success\":0,\"title\":\"这是(__)一道填(__)空题(__)。\",\"type\":4}', NULL, '2025-01-15 15:35:04', 0, 0, 1, '1111111111111111111', NULL, 0, 1, 6);
INSERT INTO `t_exam_paper_user` VALUES (1879431170241437699, '1111111111111111111', 1879079215375646722, 1879431156412817417, '{\"already\":false,\"answer\":[{\"selected\":0,\"sort\":5,\"title\":\"第一个填空\",\"value\":\"答案一\"},{\"selected\":0,\"sort\":5,\"title\":\"第二个填空\",\"value\":\"答案二\"}],\"classifyId\":1844562179480195000,\"level\":4,\"name\":\"填空题\",\"option\":[{\"selected\":0,\"sort\":5,\"title\":\"第一个填空\",\"value\":\"1\"},{\"selected\":0,\"sort\":5,\"title\":\"第二个填空\",\"value\":\"1\"}],\"rowKey\":\"7\",\"score\":5,\"scoreBySuccess\":5,\"share\":1,\"success\":1,\"title\":\"这是(__)一道(__)填空题。\",\"type\":4}', NULL, '2025-01-15 15:35:04', 5, 1, 1, '1111111111111111111', NULL, 0, 1, 7);
INSERT INTO `t_exam_paper_user` VALUES (1879431170241437700, '1111111111111111111', 1879079215375646722, 1879431156412817418, '{\"already\":false,\"classifyId\":1844562179480195000,\"keyWords\":[\"1111\",\"2222\"],\"level\":5,\"matchNum\":2,\"name\":\"解答题\",\"rowKey\":\"8\",\"score\":15,\"scoreBySuccess\":15,\"share\":1,\"success\":1,\"title\":\"这是一道简答题，对此你有什么理解？这是一道简答题，对此你有什么理解？\",\"type\":5}', NULL, '2025-01-15 15:35:04', 15, 1, 1, '1111111111111111111', NULL, 0, 1, 8);
INSERT INTO `t_exam_paper_user` VALUES (1879431170241437701, '1873746662934450177', 1879079215375646722, 1879431156412817410, '{\"title\":\"单选题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":10,\"level\":2,\"type\":1,\"share\":1,\"success\":-1,\"rowKey\":\"0\"}', NULL, NULL, NULL, NULL, 0, NULL, NULL, 0, 0, 0);
INSERT INTO `t_exam_paper_user` VALUES (1879431170241437702, '1873746662934450177', 1879079215375646722, 1879431156412817411, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一选项一选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"analysis\":\"这是一个解析。\",\"level\":1,\"type\":1,\"share\":1,\"id\":\"1844589207403139073\",\"success\":-1,\"rowKey\":\"1\"}', NULL, NULL, NULL, NULL, 0, NULL, NULL, 0, 0, 1);
INSERT INTO `t_exam_paper_user` VALUES (1879431170241437703, '1873746662934450177', 1879079215375646722, 1879431156412817412, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一选项一选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":5,\"level\":1,\"type\":1,\"share\":0,\"id\":\"1844589234568139073\",\"success\":-1,\"rowKey\":\"2\"}', NULL, NULL, NULL, NULL, 0, NULL, NULL, 0, 0, 2);
INSERT INTO `t_exam_paper_user` VALUES (1879431170241437704, '1873746662934450177', 1879079215375646722, 1879431156412817413, '{\"classifyId\":\"1844561674964144130\",\"name\":\"单选题\",\"title\":\"这是一道多选题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1},{\"rowKey\":\"3\",\"title\":\"D\",\"value\":\"选项四\",\"selected\":0,\"sort\":1}],\"answer\":[\"B\",\"C\"],\"score\":10,\"level\":2,\"type\":2,\"share\":1,\"id\":\"1844631436142407682\",\"success\":-1,\"rowKey\":\"3\"}', NULL, NULL, NULL, NULL, 0, NULL, NULL, 0, 0, 3);
INSERT INTO `t_exam_paper_user` VALUES (1879431170241437705, '1873746662934450177', 1879079215375646722, 1879431156412817414, '{\"classifyId\":\"1844561875095359490\",\"name\":\"判断题\",\"title\":\"这是一道判断题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"level\":3,\"type\":3,\"share\":1,\"id\":\"1844631640312345921\",\"success\":-1,\"rowKey\":\"4\"}', NULL, NULL, NULL, NULL, 0, NULL, NULL, 0, 0, 4);
INSERT INTO `t_exam_paper_user` VALUES (1879431170241437706, '1873746662934450177', 1879079215375646722, 1879431156412817415, '{\"classifyId\":\"1844561875095359490\",\"name\":\"判断题\",\"title\":\"这是一道判断题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":5,\"level\":3,\"type\":3,\"share\":1,\"id\":\"1844631640342097921\",\"success\":-1,\"rowKey\":\"5\"}', NULL, NULL, NULL, NULL, 0, NULL, NULL, 0, 0, 5);
INSERT INTO `t_exam_paper_user` VALUES (1879431170241437707, '1873746662934450177', 1879079215375646722, 1879431156412817416, '{\"classifyId\":\"1844562179480195074\",\"name\":\"填空题\",\"title\":\"这是(__)一道填(__)空题(__)。\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"2\",\"title\":\"第三个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5},{\"rowKey\":\"2\",\"title\":\"第三个填空\",\"value\":\"答案三\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":4,\"type\":4,\"share\":1,\"id\":\"1844631905887234566\",\"success\":-1,\"rowKey\":\"6\"}', NULL, NULL, NULL, NULL, 0, NULL, NULL, 0, 0, 6);
INSERT INTO `t_exam_paper_user` VALUES (1879431170241437708, '1873746662934450177', 1879079215375646722, 1879431156412817417, '{\"classifyId\":\"1844562179480195074\",\"name\":\"填空题\",\"title\":\"这是(__)一道(__)填空题。\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":4,\"type\":4,\"share\":1,\"id\":\"1844631905887678466\",\"success\":-1,\"rowKey\":\"7\"}', NULL, NULL, NULL, NULL, 0, NULL, NULL, 0, 0, 7);
INSERT INTO `t_exam_paper_user` VALUES (1879431170241437709, '1873746662934450177', 1879079215375646722, 1879431156412817418, '{\"classifyId\":\"1844562179480195074\",\"name\":\"解答题\",\"title\":\"这是一道简答题，对此你有什么理解？这是一道简答题，对此你有什么理解？\",\"score\":15,\"level\":5,\"type\":5,\"share\":1,\"success\":-1,\"keyWords\":[\"1111\",\"2222\"],\"matchNum\":2,\"id\":\"1846925581972267010\",\"rowKey\":\"8\"}', NULL, NULL, NULL, NULL, 0, NULL, NULL, 0, 0, 8);
INSERT INTO `t_exam_paper_user` VALUES (1879431170241437710, '1873749058125398017', 1879079215375646722, 1879431156412817410, '{\"title\":\"单选题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":10,\"level\":2,\"type\":1,\"share\":1,\"success\":-1,\"rowKey\":\"0\"}', NULL, NULL, NULL, NULL, 0, NULL, NULL, 0, 0, 0);
INSERT INTO `t_exam_paper_user` VALUES (1879431170241437711, '1873749058125398017', 1879079215375646722, 1879431156412817411, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一选项一选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"analysis\":\"这是一个解析。\",\"level\":1,\"type\":1,\"share\":1,\"id\":\"1844589207403139073\",\"success\":-1,\"rowKey\":\"1\"}', NULL, NULL, NULL, NULL, 0, NULL, NULL, 0, 0, 1);
INSERT INTO `t_exam_paper_user` VALUES (1879431170241437712, '1873749058125398017', 1879079215375646722, 1879431156412817412, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一选项一选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":5,\"level\":1,\"type\":1,\"share\":0,\"id\":\"1844589234568139073\",\"success\":-1,\"rowKey\":\"2\"}', NULL, NULL, NULL, NULL, 0, NULL, NULL, 0, 0, 2);
INSERT INTO `t_exam_paper_user` VALUES (1879431170241437713, '1873749058125398017', 1879079215375646722, 1879431156412817413, '{\"classifyId\":\"1844561674964144130\",\"name\":\"单选题\",\"title\":\"这是一道多选题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1},{\"rowKey\":\"3\",\"title\":\"D\",\"value\":\"选项四\",\"selected\":0,\"sort\":1}],\"answer\":[\"B\",\"C\"],\"score\":10,\"level\":2,\"type\":2,\"share\":1,\"id\":\"1844631436142407682\",\"success\":-1,\"rowKey\":\"3\"}', NULL, NULL, NULL, NULL, 0, NULL, NULL, 0, 0, 3);
INSERT INTO `t_exam_paper_user` VALUES (1879431170241437714, '1873749058125398017', 1879079215375646722, 1879431156412817414, '{\"classifyId\":\"1844561875095359490\",\"name\":\"判断题\",\"title\":\"这是一道判断题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"level\":3,\"type\":3,\"share\":1,\"id\":\"1844631640312345921\",\"success\":-1,\"rowKey\":\"4\"}', NULL, NULL, NULL, NULL, 0, NULL, NULL, 0, 0, 4);
INSERT INTO `t_exam_paper_user` VALUES (1879431170241437715, '1873749058125398017', 1879079215375646722, 1879431156412817415, '{\"classifyId\":\"1844561875095359490\",\"name\":\"判断题\",\"title\":\"这是一道判断题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":5,\"level\":3,\"type\":3,\"share\":1,\"id\":\"1844631640342097921\",\"success\":-1,\"rowKey\":\"5\"}', NULL, NULL, NULL, NULL, 0, NULL, NULL, 0, 0, 5);
INSERT INTO `t_exam_paper_user` VALUES (1879431170241437716, '1873749058125398017', 1879079215375646722, 1879431156412817416, '{\"classifyId\":\"1844562179480195074\",\"name\":\"填空题\",\"title\":\"这是(__)一道填(__)空题(__)。\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"2\",\"title\":\"第三个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5},{\"rowKey\":\"2\",\"title\":\"第三个填空\",\"value\":\"答案三\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":4,\"type\":4,\"share\":1,\"id\":\"1844631905887234566\",\"success\":-1,\"rowKey\":\"6\"}', NULL, NULL, NULL, NULL, 0, NULL, NULL, 0, 0, 6);
INSERT INTO `t_exam_paper_user` VALUES (1879431170241437717, '1873749058125398017', 1879079215375646722, 1879431156412817417, '{\"classifyId\":\"1844562179480195074\",\"name\":\"填空题\",\"title\":\"这是(__)一道(__)填空题。\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":4,\"type\":4,\"share\":1,\"id\":\"1844631905887678466\",\"success\":-1,\"rowKey\":\"7\"}', NULL, NULL, NULL, NULL, 0, NULL, NULL, 0, 0, 7);
INSERT INTO `t_exam_paper_user` VALUES (1879431170241437718, '1873749058125398017', 1879079215375646722, 1879431156412817418, '{\"classifyId\":\"1844562179480195074\",\"name\":\"解答题\",\"title\":\"这是一道简答题，对此你有什么理解？这是一道简答题，对此你有什么理解？\",\"score\":15,\"level\":5,\"type\":5,\"share\":1,\"success\":-1,\"keyWords\":[\"1111\",\"2222\"],\"matchNum\":2,\"id\":\"1846925581972267010\",\"rowKey\":\"8\"}', NULL, NULL, NULL, NULL, 0, NULL, NULL, 0, 0, 8);

-- ----------------------------
-- Table structure for t_exam_random_option
-- ----------------------------
DROP TABLE IF EXISTS `t_exam_random_option`;
CREATE TABLE `t_exam_random_option`  (
                                         `exam_id` bigint(0) NOT NULL COMMENT '考试ID',
                                         `question_type` int(0) NULL DEFAULT NULL COMMENT '题目类型',
                                         `question_num` int(0) NULL DEFAULT NULL COMMENT '题目数量',
                                         `question_score` int(0) NULL DEFAULT NULL COMMENT '每题的分数'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '随机题目配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_exam_random_option
-- ----------------------------
INSERT INTO `t_exam_random_option` VALUES (1879079215375646722, 1, 4, 5);
INSERT INTO `t_exam_random_option` VALUES (1879079215375646722, 2, 4, 5);
INSERT INTO `t_exam_random_option` VALUES (1879079215375646722, 3, 4, 5);

-- ----------------------------
-- Table structure for t_exam_user
-- ----------------------------
DROP TABLE IF EXISTS `t_exam_user`;
CREATE TABLE `t_exam_user`  (
                                `exam_id` bigint(0) NOT NULL COMMENT '考试ID',
                                `user_id` bigint(0) NOT NULL COMMENT '用户ID',
                                `user_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
                                `user_code` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户编号',
                                `org_id` bigint(0) NULL DEFAULT NULL COMMENT '组织机构ID',
                                `org_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组织机构名称',
                                `paper_done` int(0) NULL DEFAULT 0 COMMENT '是否完成答题（1完成、0未完成）',
                                `score` int(0) NULL DEFAULT 0 COMMENT '试卷得分',
                                `do_time` datetime(0) NULL DEFAULT NULL COMMENT '提交时间',
                                `corrected` int(0) NULL DEFAULT 0 COMMENT '是否已经批改',
                                `plat` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '试卷答题平台（WEB网页端，APP移动端）',
                                UNIQUE INDEX `idx_uni`(`exam_id`, `user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '考试用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_exam_user
-- ----------------------------
INSERT INTO `t_exam_user` VALUES (1879079215375646722, 1111111111111111111, '超级管理员', 'administrator', 1850706813621731329, NULL, 1, 45, '2025-01-15 15:35:03', 1, 'WEB');
INSERT INTO `t_exam_user` VALUES (1879079215375646722, 1873746662934450177, '寒江', 'HJ0001', 1850706813621731329, NULL, 0, 0, NULL, 0, NULL);
INSERT INTO `t_exam_user` VALUES (1879079215375646722, 1873749058125398017, '东瑞', 'HJ0002', 1850706813621731329, NULL, 0, 0, NULL, 0, NULL);

-- ----------------------------
-- Table structure for t_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu`  (
                           `id` bigint(0) NOT NULL COMMENT 'ID',
                           `parent_id` bigint(0) NULL DEFAULT 0 COMMENT '父ID',
                           `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
                           `icon` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
                           `alias` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单别名',
                           `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问地址',
                           `params` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求参数',
                           `code` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单编号',
                           `valid` int(0) NULL DEFAULT 1 COMMENT '是否有效（1有效，0无效）',
                           `sort` int(0) NULL DEFAULT NULL COMMENT '排序',
                           `del_flag` int(0) NULL DEFAULT 0 COMMENT '是否删除（1删除，0未删除）',
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_menu
-- ----------------------------
INSERT INTO `t_menu` VALUES (1873661939314868226, 0, '考试培训', 'Platform', '考试培训', '/etm', '[]', 'etm', 1, 1, 0);
INSERT INTO `t_menu` VALUES (1873662796710297602, 0, '任务中心', 'Comment', '任务中心', '/etm/task', '[]', 'etm_task', 1, 0, 0);
INSERT INTO `t_menu` VALUES (1873663010963734530, 1873661939314868226, '题目管理', 'Tickets', '题目管理', '/etm/question', '[]', 'etm_question', 1, 2, 0);
INSERT INTO `t_menu` VALUES (1873665587134611458, 1873661939314868226, '试卷管理', 'Memo', '试卷管理', '/etm/paper', '[]', 'etm_paper', 1, 3, 0);
INSERT INTO `t_menu` VALUES (1873717926629552129, 1873661939314868226, '考试管理', 'Edit', '考试管理', '/etm/exam', '[]', 'etm_exam', 1, 4, 0);
INSERT INTO `t_menu` VALUES (1873718395699539970, 1873661939314868226, '培训管理', 'VideoCamera', '培训管理', '/etm/train', '[]', 'etm_train', 1, 5, 0);
INSERT INTO `t_menu` VALUES (1873718566239940609, 0, '系统管理', 'Tools', '系统管理', '/system', '[]', 'system', 1, 3, 0);
INSERT INTO `t_menu` VALUES (1873718825632477186, 1873718566239940609, '组织机构', 'MapLocation', '组织机构', '/system/org', '[]', 'system_org', 1, 1, 0);
INSERT INTO `t_menu` VALUES (1873718995677949953, 1873718566239940609, '用户管理', 'User', '用户管理', '/system/user', '[]', 'system_user', 1, 2, 0);
INSERT INTO `t_menu` VALUES (1873719239731916802, 1873718566239940609, '系统菜单', 'Menu', '系统菜单', '/system/menu', '[]', 'system_menu', 1, 9, 0);
INSERT INTO `t_menu` VALUES (1879740238898245633, 1873718566239940609, '资源管理', 'Coin', '资源管理', '/resource', '[]', 'resource', 1, 4, 0);
INSERT INTO `t_menu` VALUES (1879755468701204481, 1879740238898245633, '存储配置', 'Setting', '存储配置', '/resource/storage', '[]', 'resource_storage', 1, 1, 0);
INSERT INTO `t_menu` VALUES (1879755772490448898, 1879740238898245633, '附件管理', 'FolderAdd', '附件管理', '/resource/attach', '[]', 'resource_attach', 1, 2, 0);
INSERT INTO `t_menu` VALUES (1880185438925832193, 1879740238898245633, '附件日志', 'Document', '附件日志', '/resource/attachLog', '[]', 'resource_attach_log', 1, 3, 0);
INSERT INTO `t_menu` VALUES (1880186212221272065, 1873718566239940609, '系统参数', 'Discount', '系统参数', '/system/params', '[]', 'system_params', 1, 5, 0);
INSERT INTO `t_menu` VALUES (1884974021100544001, 1884975346991341570, '按钮管理', 'Pointer', '按钮管理', '/system/btnmanager', '[]', 'system_btnmanager', 1, 5, 0);
INSERT INTO `t_menu` VALUES (1884974216035016705, 1884975346991341570, '接口管理', 'Connection', '接口管理', '/system/apimanager', '[]', 'system_apimanager', 1, 6, 0);
INSERT INTO `t_menu` VALUES (1884974935785971713, 1884975346991341570, '角色管理', 'User', '角色管理', '/system/rolemanager', '[]', 'system_rolemanager', 1, 7, 0);
INSERT INTO `t_menu` VALUES (1884975346991341570, 1873718566239940609, '系统权限', 'Lock', '系统权限', '/authmanager', '[]', 'authmanager', 1, 3, 0);
INSERT INTO `t_menu` VALUES (1887852869190205441, 0, '首页', 'HomeFilled', '首页', '/statistics', '[]', 'home', 1, -1, 0);

-- ----------------------------
-- Table structure for t_menu_api
-- ----------------------------
DROP TABLE IF EXISTS `t_menu_api`;
CREATE TABLE `t_menu_api`  (
                               `id` bigint(0) NOT NULL COMMENT '主键',
                               `menu_id` bigint(0) NULL DEFAULT NULL COMMENT '菜单ID',
                               `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接口名称',
                               `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接口编号',
                               `path` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接口路径',
                               `method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接口方法',
                               `valid` int(0) NULL DEFAULT 1 COMMENT '是否有效（1有效，0无效）',
                               `type` int(0) NULL DEFAULT NULL COMMENT '类型（1自动扫描，0手动添加）',
                               `next_time_millis` bigint(0) NULL DEFAULT NULL COMMENT '上次操作的时间戳',
                               `del_flag` int(0) NULL DEFAULT 0 COMMENT '是否删除（1删除，0未删除）',
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单接口表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_menu_api
-- ----------------------------
INSERT INTO `t_menu_api` VALUES (1887368830901669889, 1884974216035016705, '新增', 'post::menu-api:add', '/menu-api/add', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368830901669890, 1884974216035016705, '编辑', 'post::menu-api:update', '/menu-api/update', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368830972973057, 1884974216035016705, '删除', 'post::menu-api:delete', '/menu-api/delete', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368830972973058, 1884974216035016705, '分页', 'get::menu-api:page', '/menu-api/page', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368830972973059, 1884974216035016705, '设置有效', 'post::menu-api:valid', '/menu-api/valid', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368830972973060, 1884974216035016705, '根据ID唯一获取', 'get::menu-api:selectOne', '/menu-api/selectOne', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831040081922, 1884974216035016705, '设置无效', 'post::menu-api:unValid', '/menu-api/unValid', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831040081923, 1884974021100544001, '新增', 'post::menu-button:add', '/menu-button/add', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831040081924, 1884974021100544001, '编辑', 'post::menu-button:update', '/menu-button/update', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831102996481, 1884974021100544001, '删除', 'post::menu-button:delete', '/menu-button/delete', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831102996482, 1884974021100544001, '分页', 'get::menu-button:page', '/menu-button/page', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831102996483, 1884974021100544001, '设置有效', 'post::menu-button:valid', '/menu-button/valid', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831102996484, 1884974021100544001, '根据ID唯一获取', 'get::menu-button:selectOne', '/menu-button/selectOne', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831165911041, 1884974021100544001, '设置无效', 'post::menu-button:unValid', '/menu-button/unValid', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831165911042, 1884974935785971713, '新增', 'post::role:add', '/role/add', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831165911043, 1884974935785971713, '编辑', 'post::role:update', '/role/update', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831233019905, 1884974935785971713, '删除', 'post::role:delete', '/role/delete', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831233019906, 1884974935785971713, '树形列表', 'get::role:tree', '/role/tree', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831233019907, 1884974935785971713, '根据ID唯一获取', 'get::role:selectOne', '/role/selectOne', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831233019908, 1873717926629552129, '分类:列表', 'get::exam:classify:list', '/exam/classify/list', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831308517377, 1873717926629552129, '分类:树形列表', 'get::exam:classify:tree', '/exam/classify/tree', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831308517378, 1873717926629552129, '分类:新增', 'post::exam:classify:add', '/exam/classify/add', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831308517379, 1873717926629552129, '分类:编辑', 'post::exam:classify:edit', '/exam/classify/edit', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831308517380, 1873717926629552129, '分类:删除', 'post::exam:classify:delete', '/exam/classify/delete', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831367237633, 1873717926629552129, '新增', 'post::exam:template:add', '/exam/template/add', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831367237634, 1873717926629552129, '删除', 'post::exam:template:delete', '/exam/template/delete', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831367237635, 1873717926629552129, '系统内部考试发布', 'get::exam:template:publish::id', '/exam/template/publish/{id}', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831434346497, 1873717926629552129, '根据ID唯一获取', 'get::exam:template:select::id', '/exam/templateselect/{id}', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831446929409, 1873717926629552129, '编辑', 'post::exam:template:edit', '/exam/template/edit', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831446929410, 1873717926629552129, '根据OrgId获取组织', 'get::exam:template:selectOrgByOrgId::id', '/exam/template/selectOrgByOrgId/{id}', 'GET', 1, 1, 1738935865239, 1);
INSERT INTO `t_menu_api` VALUES (1887368831446929411, 1873717926629552129, '交卷+人工批改', 'post::exam:template:examAnswerV2', '/exam/template/examAnswerV2', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831446929412, 1873717926629552129, '获取考试配置', 'get::exam:template:examConfig', '/exam/template/examConfig', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831501455362, 1873717926629552129, '交卷+机器批改', 'post::exam:template:examAnswer', '/exam/template/examAnswer', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831501455363, 1873717926629552129, '人工审批', 'post::exam:template:correct', '/exam/template/correct', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831501455364, 1873717926629552129, '初始化考试(钉钉)', 'post::exam:template:initExamOfDD', '/exam/template/initExamOfDD', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831501455365, 1873717926629552129, '系统内部考试撤销', 'get::exam:template:cancelPublish::id', '/exam/template/cancelPublish/{id}', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831501455366, 1873717926629552129, '扫码开放考试发布', 'get::exam:template:publishV2::id', '/exam/template/publishV2/{id}', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831568564226, 1873717926629552129, '扫码开放考试撤销', 'get::exam:template:cancelPublishV2::id', '/exam/template/cancelPublishV2/{id}', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831568564227, 1873717926629552129, '分页', 'get::exam:template:page', '/exam/template/page', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831568564228, 1873717926629552129, '批改模式获取试卷配置', 'get::exam:template:examConfigByCorrect', '/exam/template/examConfigByCorrect', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831568564229, 1873717926629552129, '检测随机option是否合法', 'post::exam:template:checkRandomOption', '/exam/template/checkRandomOption', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831568564230, 1873717926629552129, '进展情况:导出', 'get::exam:template:examProgressExport', '/exam/template/examProgressExport', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831635673090, 1873717926629552129, '进展情况:参数人数', 'get::exam:user:counts', '/exam/user/counts', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831635673091, 1873717926629552129, '进展情况:合格情况', 'get::exam:user:qualified', '/exam/userqualified', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831635673092, 1873717926629552129, '进展情况:完成情况', 'get::exam:user:completion', '/exam/usercompletion', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831635673093, 1873717926629552129, '进展情况:分页', 'get::exam:user:page', '/exam/user/page', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831698587649, 1873719239731916802, '新增', 'post::menu:add', '/menu/add', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831698587650, 1873719239731916802, '编辑', 'post::menu:update', '/menu/update', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831698587651, 1873719239731916802, '设置有效接口', 'post::menu:set-valid', '/menu/set-valid', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831698587652, 1873719239731916802, '删除', 'post::menu:delete', '/menu/delete', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831765696513, 1873719239731916802, '列表', 'get::menu:list', '/menu/list', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831765696514, 1873719239731916802, '树形列表', 'get::menu:tree', '/menu/tree', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831765696515, 1873719239731916802, '根据ID唯一获取', 'get::menu:selectOne', '/menu/selectOne', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831765696516, 1873718825632477186, '新增', 'post::org:add', '/org/add', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831832805377, 1873718825632477186, '编辑', 'post::org:update', '/org/update', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831849582594, 1873718825632477186, '删除', 'post::org:delete', '/org/delete', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831862165505, 1873718825632477186, '树形列表', 'get::org:tree', '/org/tree', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831862165506, 1873718825632477186, '根据ID唯一获取', 'get::org:selectOne', '/org/selectOne', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831895719937, 1873665587134611458, '分类:新增', 'post::paper:classify:add', '/paper/classifyadd', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831895719938, 1873665587134611458, '分类:删除', 'post::paper:classify:delete', '/paper/classifydelete', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831895719939, 1873665587134611458, '分类:列表', 'get::paper:classify:list', '/paper/classifylist', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831895719940, 1873665587134611458, '分类:树形列表', 'get::paper:classify:tree', '/paper/classifytree', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831958634498, 1873665587134611458, '分类:根据ID唯一获取', 'get::paper:classify:select::id', '/paper/classifyselect/{id}', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831958634499, 1873665587134611458, '分类:编辑', 'post::paper:classify:edit', '/paper/classifyedit', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831958634500, 1873665587134611458, '试卷试题:新增', 'post::paper:question:add', '/paper/questionadd', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368831958634501, 1873665587134611458, '试卷试题:删除', 'post::paper:question:delete', '/paper/questiondelete', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832025743361, 1873665587134611458, '试卷试题:根据ID唯一获取', 'get::paper:question:select::id', '/paper/questionselect/{id}', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832025743362, 1873665587134611458, '试卷试题:编辑', 'post::paper:question:edit', '/paper/questionedit', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832025743363, 1873665587134611458, '试卷试题:分页', 'get::paper:question:page', '/paper/questionpage', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832025743364, 1873665587134611458, '新增', 'post::paper:template:add', '/paper/templateadd', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832092852226, 1873665587134611458, '删除', 'post::paper:template:delete', '/paper/templatedelete', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832092852227, 1873665587134611458, '根据ID唯一获取', 'get::paper:template:select::id', '/paper/templateselect/{id}', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832092852228, 1873665587134611458, '编辑', 'post::paper:template:edit', '/paper/templateedit', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832092852229, 1873665587134611458, '分页', 'get::paper:template:page', '/paper/templatepage', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832159961089, 1880186212221272065, '新增', 'post::params:add', '/params/add', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832159961090, 1880186212221272065, '编辑', 'post::params:update', '/params/update', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832159961091, 1880186212221272065, '删除', 'post::params:delete', '/params/delete', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832159961092, 1880186212221272065, '根据key获取value', 'get::params:fetch-value-by-key', '/params/fetch-value-by-key', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832222875650, 1880186212221272065, '根据ID唯一获取', 'get::params:selectOne', '/params/selectOne', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832222875651, 1880186212221272065, '分页', 'get::params:page', '/params/page', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832222875652, 1873663010963734530, '分类:新增', 'post::question:classify:add', '/question/classifyadd', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832222875653, 1873663010963734530, '分类:删除', 'post::question:classify:delete', '/question/classifydelete', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832306761729, 1873663010963734530, '分类:列表', 'get::question:classify:list', '/question/classifylist', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832306761730, 1873663010963734530, '分类:树形列表', 'get::question:classify:tree', '/question/classifytree', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832306761731, 1873663010963734530, '分类:根据ID唯一获取', 'get::question:classify:select::id', '/question/classifyselect/{id}', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832306761732, 1873663010963734530, '分类:编辑', 'post::question:classify:edit', '/question/classifyedit', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832403230721, 1879755772490448898, '删除', 'post::attach:delete', '/attach/delete', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832420007937, 1879755772490448898, '列表', 'post::attach:list', '/attach/list', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832420007938, 1879755772490448898, '上传', 'post::attach:upload', '/attach/upload', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832420007939, 1879755772490448898, '下载', 'get::attach:download::id', '/attachdownload/{id}', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832482922498, 1879755772490448898, '日志:分页', 'get::attach:log:page', '/attach/log/page', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832482922499, 1879755772490448898, '分片串行上传', 'post::attach:chunk-upload', '/attach/chunk-upload', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832482922500, 1879755772490448898, '批量下载', 'post::attach:batchDownload', '/attachbatchDownload', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832482922501, 1879755772490448898, '根据ID唯一获取', 'get::attach:selectOne', '/attach/selectOne', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832562614274, 1879755772490448898, '分页', 'get::attach:page', '/attach/page', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832571002882, 1879755468701204481, '新增', 'post::storage:add', '/storage/add', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832638111745, 1879755468701204481, '编辑', 'post::storage:update', '/storage/update', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832638111746, 1879755468701204481, '删除', 'post::storage:delete', '/storage/delete', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832701026305, 1879755468701204481, '设置有效', 'get::storage:validSuccess', '/storage/validSuccess', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832701026306, 1879755468701204481, '更新协议配置', 'post::storage:updateProtocol', '/storage/updateProtocol', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832701026307, 1879755468701204481, '根据ID唯一获取', 'get::storage:selectOne', '/storage/selectOne', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832701026308, 1879755468701204481, '分页', 'get::storage:page', '/storage/page', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832747163649, 1873662796710297602, '列表', 'get::task:list', '/tasklist', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832747163650, 1873662796710297602, '分页', 'get::task:page', '/taskpage', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832814272514, 1873718395699539970, '分类:新增', 'post::training:classify:add', '/training/classifyadd', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832814272515, 1873718395699539970, '分类:删除', 'post::training:classify:delete', '/training/classifydelete', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832814272516, 1873718395699539970, '分类:列表', 'get::training:classify:page', '/training/classifylist', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832814272517, 1873718395699539970, '分类:树形列表', 'get::training:classify:tree', '/training/classifytree', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832814272518, 1873718395699539970, '分类:根据ID唯一获取', 'get::training:classify:select::id', '/training/classifyselect/{id}', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832881381377, 1873718395699539970, '分类:编辑', 'post::training:classify:edit', '/training/classifyedit', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832881381378, 1873718395699539970, '新增', 'post::training:add', '/trainingadd', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832881381379, 1873718395699539970, '删除', 'post::training:delete', '/trainingdelete', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832881381380, 1873718395699539970, '发布', 'get::training:publish::id', '/trainingpublish/{id}', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832948490242, 1873718395699539970, '根据ID唯一获取', 'get::training:select::id', '/trainingselect/{id}', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832948490243, 1873718395699539970, '编辑', 'post::training:edit', '/trainingedit', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832948490244, 1873718395699539970, '撤销', 'get::training:cancelPublish::id', '/trainingcancelPublish/{id}', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832948490245, 1873718395699539970, '交卷', 'post::training:paperAnswer', '/trainingpaperAnswer', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368832948490246, 1873718395699539970, '分页', 'get::training:page', '/trainingpage', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368833015599106, 1873718395699539970, '进展情况:导出', 'get::training:trainProgressExport', '/training/trainProgressExport', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368833015599107, 1873718395699539970, '获取用户培训信息', 'get::training:getUserTrainConfig', '/traininggetUserTrainConfig', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368833015599108, 1873718395699539970, '进展情况:合格情况', 'get::training:user:qualified-info', '/training/userqualified-info', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368833015599109, 1873718395699539970, '进展情况:完成情况', 'get::training:user:completion-info', '/training/usercompletion-info', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368833082707970, 1873718395699539970, '进展情况:分页', 'get::training:user:page', '/training/userpage', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368833082707971, 1873718395699539970, '进展情况:参与人数', 'get::training:user:counts', '/training/usercounts', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368833082707972, 1873718995677949953, '新增', 'post::user:add', '/user/add', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368833082707973, 1873718995677949953, '更新', 'post::user:update', '/user/update', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368833145622529, 1873718995677949953, '删除', 'post::user:delete', '/user/delete', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368833145622530, 1873718995677949953, '分页', 'get::user:page', '/user/page', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368833145622531, 1873718995677949953, '解封账号', 'post::user:valid', '/user/valid', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368833145622532, 1873718995677949953, '根据ID集合批量获取', 'post::user:selectByIds', '/user/selectByIds', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368833145622533, 1873718995677949953, '登录', 'post::user:login', '/user/login', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368833212731393, 1873718995677949953, '退出', 'get::user:logout', '/user/logout', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368833212731394, 1873718995677949953, '刷新Token', 'get::user:refresh-token', '/user/refresh-token', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368833212731395, 1873718995677949953, '重置密码', 'post::user:resetPwd', '/user/resetPwd', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887368833212731396, 1873718995677949953, '根据ID唯一获取', 'get::user:selectOne', '/user/selectOne', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887386211451527169, 1884974021100544001, '菜单按钮树形列表', 'get::menu-button:menu-button-tree', '/menu-button/menu-button-tree', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887409368325550081, 1884974216035016705, '菜单接口树形列表', 'get::menu-api:menu-button-tree', '/menu-api/menu-button-tree', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887409368719814657, 1884974935785971713, '保存权限配置', 'post::role:save-auth-config', '/role/save-auth-config', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887409368719814658, 1884974935785971713, '根据RoleId获取权限配置', 'get::role:get-auth-config', '/role/get-auth-config', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887428167535693825, 1884974935785971713, '保存用户角色', 'post::role:save-user-role', '/role/save-user-role', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887428167535693826, 1884974935785971713, '根据UserId获取用户角色', 'get::role:get-user-role', '/role/get-user-role', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887440296808665089, 1884974935785971713, '根据RoleId获取用户', 'get::role:get-role-user', '/role/get-role-user', 'GET', 1, 1, 1738916184020, 1);
INSERT INTO `t_menu_api` VALUES (1887519037974904833, 1884974935785971713, '根据用户按钮权限', 'get::role:get-button-permissions', '/role/get-button-permissions', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887686303433732098, 1873719239731916802, '用户树形列表权限', 'get::menu:permission-tree', '/menu/permission-tree', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887766104051908609, 1873663010963734530, '新增', 'post::question:template:add', '/question/templateadd', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887766104085463041, 1873663010963734530, '删除', 'post::question:template:delete', '/question/templatedelete', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887766104085463042, 1873663010963734530, '取消导入', 'get::question:template:cancelImport', '/question/template/cancelImport', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887766104139988994, 1873663010963734530, '分页', 'get::question:template:page', '/question/templatepage', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887766104139988995, 1873663010963734530, '解析Excel', 'post::question:template:file-parser', '/question/template/file-parser', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887766104139988996, 1873663010963734530, '下载模版', 'get::question:template:downloadTemplate', '/question/template/downloadTemplate', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887766104139988997, 1873663010963734530, '导入', 'get::question:template:submitImport', '/question/template/submitImport', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887766104202903553, 1873663010963734530, '根据ID唯一获取', 'get::question:template:select::id', '/question/templateselect/{id}', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887766104202903554, 1873663010963734530, '编辑', 'post::question:template:edit', '/question/templateedit', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887773594084782082, 1884974216035016705, '接口采集/校验', 'get::endpoint:scan-and-save-api', '/endpoint/scan-and-save-api', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887795457590673409, 1873717926629552129, '二维码:新增', 'post::exam:qr:add', '/exam/qr/add', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887795457636810753, 1873717926629552129, '二维码:删除', 'post::exam:qr:delete', '/exam/qr/delete', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887795457636810754, 1873717926629552129, '二维码:设置有效', 'get::exam:qr:getEnabled', '/exam/qrgetEnabled', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887795457636810755, 1873717926629552129, '二维码:列表', 'get::exam:qr:list', '/exam/qr/list', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887795457699725313, 1873717926629552129, '二维码:根据ID唯一获取', 'get::exam:qr:select::id', '/exam/qr/select/{id}', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887795457699725314, 1873717926629552129, '二维码:根据ID设置有效', 'get::exam:qr:enable::id', '/exam/qrenable/{id}', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1887795457699725315, 1873717926629552129, '二维码:编辑', 'post::exam:qr:edit', '/exam/qr/edit', 'POST', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1888096197693534210, 1887852869190205441, '用户活跃次数统计', 'get::statistics:user-active', '/statistics/user-active', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1888096197743865858, 1887852869190205441, '题目类型统计', 'get::statistics:question-type', '/statistics/question-type', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1888096197743865859, 1887852869190205441, '题目难度统计', 'get::statistics:question-level', '/statistics/question-level', 'GET', 1, 1, 1739001904262, 0);
INSERT INTO `t_menu_api` VALUES (1888096197743865860, 1887852869190205441, '考试培训状态统计', 'get::statistics:exam-train-status', '/statistics/exam-train-status', 'GET', 1, 1, 1739001904262, 0);

-- ----------------------------
-- Table structure for t_menu_button
-- ----------------------------
DROP TABLE IF EXISTS `t_menu_button`;
CREATE TABLE `t_menu_button`  (
                                  `id` bigint(0) NOT NULL COMMENT '主键',
                                  `menu_id` bigint(0) NULL DEFAULT NULL COMMENT '菜单ID',
                                  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '按钮名称',
                                  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '按钮编号',
                                  `valid` int(0) NULL DEFAULT 1 COMMENT '是否有效（1有效，0无效）',
                                  `del_flag` int(0) NULL DEFAULT 0 COMMENT '是否删除（1删除，0未删除）',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单按钮表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_menu_button
-- ----------------------------
INSERT INTO `t_menu_button` VALUES (1887060807440228354, 1873662796710297602, '查看', 'task_view', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887060880920240129, 1873662796710297602, '操作', 'task_opt', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887062629131337729, 1873663010963734530, '新增', 'question_add', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887062692809261057, 1873663010963734530, '编辑', 'question_edit', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887062738749472770, 1873663010963734530, '查看', 'question_view', 1, 1);
INSERT INTO `t_menu_button` VALUES (1887062796966412290, 1873663010963734530, '删除', 'question_delete', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887062904395120641, 1873663010963734530, '批量导入', 'question_import', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887063111069450242, 1873663010963734530, '分类管理', 'question_classify', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887063179793121282, 1873663010963734530, '预览', 'question_preview', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887063459171516418, 1873665587134611458, '新增', 'paper_add', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887063504042180609, 1873665587134611458, '编辑', 'paper_edit', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887063541224685569, 1873665587134611458, '删除', 'paper_delete', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887063598833451009, 1873665587134611458, '预览', 'paper_preview', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887063662704312321, 1873665587134611458, '分类管理', 'paper_classify', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887063776751632386, 1873717926629552129, '新增', 'exam_add', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887063848511979522, 1873717926629552129, '编辑', 'exam_edit', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887063940761501698, 1873717926629552129, '删除', 'exam_delete', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887064016795844610, 1873717926629552129, '预览', 'exam_preview', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887064064778682369, 1873717926629552129, '查看', 'exam_view', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887064280571428865, 1873717926629552129, '发布/撤销', 'exam_publish', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887064412964634626, 1873717926629552129, '分类管理', 'exam_classify', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887064695383900162, 1873717926629552129, '进展情况', 'exam_progress', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887064794554023937, 1873718395699539970, '新增', 'train_add', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887064832663470081, 1873718395699539970, '编辑', 'train_edit', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887064863403524097, 1873718395699539970, '删除', 'train_delete', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887064911596077058, 1873718395699539970, '查看', 'train_view', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887064947889389569, 1873718395699539970, '预览', 'train_preview', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887065063341801473, 1873718395699539970, '发布/撤销', 'train_publish', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887065133713833986, 1873718395699539970, '分类管理', 'train_classify', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887065182485200897, 1873718395699539970, '进展情况', 'train_progress', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887065339096317954, 1873718825632477186, '新增', 'org_add', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887065378925428737, 1873718825632477186, '编辑', 'org_edit', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887065426224594946, 1873718825632477186, '删除', 'org_delete', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887065457656709122, 1873718825632477186, '查看', 'org_view', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887065597855514625, 1873718825632477186, '新增子项', 'org_add_child', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887065657364299778, 1873718995677949953, '新增', 'user_add', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887065708857769985, 1873718995677949953, '编辑', 'user_edit', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887065737819439106, 1873718995677949953, '删除', 'user_delete', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887065781016576002, 1873718995677949953, '查看', 'user_view', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887065892278878210, 1873718995677949953, '角色配置', 'user_role', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887065958897008642, 1873718995677949953, '重置密码', 'user_repassword', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887066121455648770, 1873718995677949953, '解封账号', 'user_valid', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887066272765165569, 1884974021100544001, '新增', 'btn_add', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887066321058381825, 1884974021100544001, '编辑', 'btn_edit', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887066361856376834, 1884974021100544001, '删除', 'btn_delete', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887066407792394241, 1884974021100544001, '查看', 'btn_view', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887066516273872897, 1884974021100544001, '设置有效', 'btn_valid', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887066596259250178, 1884974021100544001, '设置无效', 'btn_unvalid', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887066754980102146, 1884974216035016705, '新增', 'api_add', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887066809317310465, 1884974216035016705, '编辑', 'api_edit', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887066852942266370, 1884974216035016705, '删除', 'api_delete', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887066888501575682, 1884974216035016705, '查看', 'api_view', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887066942725537794, 1884974216035016705, '设置有效', 'api_valid', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887066997234712577, 1884974216035016705, '设置无效', 'api_unvalid', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887067172544036865, 1884974216035016705, '接口采集/校验', 'api_collection', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887067731896418305, 1884974935785971713, '新增', 'role_add', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887067767917101058, 1884974935785971713, '编辑', 'role_edit', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887067807607799810, 1884974935785971713, '删除', 'role_delete', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887067868345516033, 1884974935785971713, '查看', 'role_view', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887067953414389761, 1884974935785971713, '新增子项', 'role_add_child', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887068013644595201, 1884974935785971713, '用户设置', 'role_user', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887068081550376962, 1884974935785971713, '权限配置', 'role_auth', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887068261720899586, 1879755468701204481, '新增', 'storage_add', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887068312841076737, 1879755468701204481, '编辑', 'storage_edit', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887068342335422466, 1879755468701204481, '删除', 'storage_delete', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887068379861860354, 1879755468701204481, '查看', 'storage_view', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887068468068073473, 1879755468701204481, '协议配置', 'storage_config', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887068574238490625, 1879755772490448898, '删除', 'attach_delete', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887068642651783170, 1879755772490448898, '下载', 'attach_download', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887068727225729026, 1880186212221272065, '新增', 'params_add', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887068761853902849, 1880186212221272065, '编辑', 'params_edit', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887068791499243522, 1880186212221272065, '删除', 'params_delete', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887068838941016065, 1880186212221272065, '查看', 'params_view', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887068927986089986, 1880186212221272065, '新增', 'menu_add', 1, 1);
INSERT INTO `t_menu_button` VALUES (1887068996546183169, 1873719239731916802, '新增', 'menu_add', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887069027089104898, 1873719239731916802, '编辑', 'menu_edit', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887069058814820354, 1873719239731916802, '删除', 'menu_delete', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887069129673392130, 1873719239731916802, '查看', 'menu_view', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887069231636922369, 1873719239731916802, '新增子项', 'menu_add_child', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887069393021157377, 1873719239731916802, '设置有效', 'menu_valid', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887069437107486722, 1873719239731916802, '设置无效', 'menu_unvalid', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887799715539025922, 1887852869190205441, '回到首页', 'home', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887853120185745409, 1887852869190205441, '搜索', 'search', 1, 1);
INSERT INTO `t_menu_button` VALUES (1887853247537397761, 1887852869190205441, '全屏', 'fullscreen', 1, 1);
INSERT INTO `t_menu_button` VALUES (1887853394933628930, 1887852869190205441, '个人信息', 'userInfo', 1, 0);
INSERT INTO `t_menu_button` VALUES (1887853450512351233, 1887852869190205441, '退出', 'logout', 1, 1);
INSERT INTO `t_menu_button` VALUES (1887854002772164609, 1887852869190205441, '更多', 'more', 1, 0);

-- ----------------------------
-- Table structure for t_notic_user
-- ----------------------------
DROP TABLE IF EXISTS `t_notic_user`;
CREATE TABLE `t_notic_user`  (
                                 `id` bigint(0) NOT NULL COMMENT '主键',
                                 `notice_id` bigint(0) NULL DEFAULT NULL COMMENT '公告ID',
                                 `user_code` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '用户编号',
                                 `read_already` int(0) NULL DEFAULT NULL COMMENT '是否已读（1已读，0未读）',
                                 `read_time` datetime(0) NULL DEFAULT NULL COMMENT '阅读时间',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通知公告' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_notic_user
-- ----------------------------

-- ----------------------------
-- Table structure for t_notice
-- ----------------------------
DROP TABLE IF EXISTS `t_notice`;
CREATE TABLE `t_notice`  (
                             `id` bigint(0) NOT NULL COMMENT '主键',
                             `classify_id` bigint(0) NULL DEFAULT NULL COMMENT '公告分类ID',
                             `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
                             `cover` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封面',
                             `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
                             `topup` int(0) NULL DEFAULT 0 COMMENT '是否置顶（1置顶，0不置顶）',
                             `level` int(0) NULL DEFAULT NULL COMMENT '重要程度',
                             `create_user` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
                             `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                             `published` int(0) NULL DEFAULT 0 COMMENT '是否发布（1已发布，0未发布）',
                             `publish_time` datetime(0) NULL DEFAULT NULL COMMENT '发布时间',
                             `del_flag` int(0) NULL DEFAULT 0 COMMENT '是否删除（1删除，0未删除）',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '公告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_notice
-- ----------------------------

-- ----------------------------
-- Table structure for t_notice_classify
-- ----------------------------
DROP TABLE IF EXISTS `t_notice_classify`;
CREATE TABLE `t_notice_classify`  (
                                      `id` bigint(0) NOT NULL COMMENT '主键',
                                      `parent_id` bigint(0) NULL DEFAULT 0 COMMENT '父ID',
                                      `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编号',
                                      `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
                                      `desc` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
                                      `del_flag` int(0) NULL DEFAULT 0 COMMENT '是否删除（0未删除，1删除）',
                                      PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '公共分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_notice_classify
-- ----------------------------

-- ----------------------------
-- Table structure for t_organization
-- ----------------------------
DROP TABLE IF EXISTS `t_organization`;
CREATE TABLE `t_organization`  (
                                   `id` bigint(0) NOT NULL COMMENT 'ID',
                                   `parent_id` bigint(0) NULL DEFAULT 0 COMMENT '父ID',
                                   `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组织名称',
                                   `fullname` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组织全称',
                                   `code` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组织编号',
                                   `type` int(0) NULL DEFAULT 1 COMMENT '组织类型（1单位，0部门）',
                                   `valid` int(0) NULL DEFAULT 1 COMMENT '是否有效（1有效，0无效）',
                                   `sort` int(0) NULL DEFAULT NULL COMMENT '排序',
                                   `del_flag` int(0) NULL DEFAULT 0 COMMENT '是否删除（1删除，0未删除）',
                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '组织表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_organization
-- ----------------------------
INSERT INTO `t_organization` VALUES (1850706813621731329, NULL, '寒江科技有限公司', '寒江科技有限公司', 'HJ0001', 1, 1, 1, 0);
INSERT INTO `t_organization` VALUES (1850706813621731330, 1850706813621731332, '人事部', '人事部', 'HJ001_B001', 0, 1, 3, 0);
INSERT INTO `t_organization` VALUES (1850706813621731331, 1850706813621731332, '财务部', '财务部', 'HJ001_B002', 0, 0, 4, 0);
INSERT INTO `t_organization` VALUES (1850706813621731332, 1850706813621731329, '转转子公司', '转转子公司', 'HJ0001C_001', 1, 1, 1, 0);
INSERT INTO `t_organization` VALUES (1850706813621731333, 1850706813621731329, '合计子公司', '合计子公司', 'HJ001_C002', 1, 1, 2, 0);

-- ----------------------------
-- Table structure for t_paper_classify
-- ----------------------------
DROP TABLE IF EXISTS `t_paper_classify`;
CREATE TABLE `t_paper_classify`  (
                                     `id` bigint(0) NOT NULL COMMENT '主键',
                                     `parent_id` bigint(0) NULL DEFAULT 0 COMMENT '父ID',
                                     `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编号',
                                     `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
                                     `desc` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
                                     `del_flag` int(0) NULL DEFAULT 0 COMMENT '是否删除（0未删除，1删除）',
                                     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '试卷分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_paper_classify
-- ----------------------------
INSERT INTO `t_paper_classify` VALUES (1844737410605850626, 0, 'paper-1', '测试', NULL, 0);
INSERT INTO `t_paper_classify` VALUES (1844737457221345282, 0, 'paper-2', '试卷2', NULL, 1);
INSERT INTO `t_paper_classify` VALUES (1844737513680871425, 1844737410605850626, 'paper-1-1', '试卷1-1', NULL, 1);
INSERT INTO `t_paper_classify` VALUES (1844737553413513218, 0, '333', '我的3', NULL, 1);
INSERT INTO `t_paper_classify` VALUES (1844738895393660929, 1844737513680871425, '1111', '111', NULL, 1);
INSERT INTO `t_paper_classify` VALUES (1853739234536529922, 1844737457221345282, '2-2', '试卷2-2-单选', NULL, 1);
INSERT INTO `t_paper_classify` VALUES (1872940671053594625, 0, '345678', '测试2', NULL, 1);

-- ----------------------------
-- Table structure for t_paper_question
-- ----------------------------
DROP TABLE IF EXISTS `t_paper_question`;
CREATE TABLE `t_paper_question`  (
                                     `id` bigint(0) NOT NULL COMMENT '主键',
                                     `paper_id` bigint(0) NULL DEFAULT NULL COMMENT '所属试卷ID',
                                     `question_id` bigint(0) NULL DEFAULT NULL COMMENT '试题模版ID',
                                     `config` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '试题配置（基于试题模版）',
                                     `sort` int(0) NULL DEFAULT NULL COMMENT '排序',
                                     `del_flag` int(0) NULL DEFAULT 0 COMMENT '是否删除（0未删除，1删除）',
                                     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '试卷-试题表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_paper_question
-- ----------------------------
INSERT INTO `t_paper_question` VALUES (1850821181361467393, 1850821180526800898, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一选项一选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"analysis\":\"这是一个解析。\",\"level\":1,\"type\":1,\"share\":0,\"id\":\"1844589207403139073\",\"success\":0,\"rowKey\":\"1844589207403139073\"}', 0, 0);
INSERT INTO `t_paper_question` VALUES (1850821181361467394, 1850821180526800898, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一选项一选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":5,\"level\":1,\"type\":1,\"share\":0,\"id\":\"1844589234568139073\",\"success\":0,\"rowKey\":\"1844589234568139073\"}', 1, 0);
INSERT INTO `t_paper_question` VALUES (1853356137089937410, 1850743197669621762, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一选项一选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"analysis\":\"这是一个解析。\",\"level\":1,\"type\":1,\"share\":0,\"id\":\"1844589207403139073\",\"success\":0,\"rowKey\":\"0\"}', 0, 0);
INSERT INTO `t_paper_question` VALUES (1853356137089937411, 1850743197669621762, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一选项一选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":5,\"level\":1,\"type\":1,\"share\":0,\"id\":\"1844589234568139073\",\"success\":0,\"rowKey\":\"1\"}', 1, 0);
INSERT INTO `t_paper_question` VALUES (1853356137089937412, 1850743197669621762, NULL, '{\"classifyId\":\"1844561674964144130\",\"name\":\"多选题\",\"title\":\"这是一道多选题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1},{\"rowKey\":\"3\",\"title\":\"D\",\"value\":\"选项四\",\"selected\":0,\"sort\":1}],\"answer\":[\"B\",\"C\"],\"score\":10,\"level\":2,\"type\":2,\"share\":1,\"id\":\"1844631436142407682\",\"success\":0,\"rowKey\":\"2\"}', 2, 0);
INSERT INTO `t_paper_question` VALUES (1853356137089937413, 1850743197669621762, NULL, '{\"classifyId\":\"1844561875095359490\",\"name\":\"判断题\",\"title\":\"这是一道判断题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"level\":3,\"type\":3,\"share\":1,\"id\":\"1844631640312345921\",\"success\":0,\"rowKey\":\"3\"}', 3, 0);
INSERT INTO `t_paper_question` VALUES (1853356137089937414, 1850743197669621762, NULL, '{\"classifyId\":\"1844561875095359490\",\"name\":\"判断题\",\"title\":\"这是一道判断题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":5,\"level\":3,\"type\":3,\"share\":1,\"id\":\"1844631640342097921\",\"success\":0,\"rowKey\":\"4\"}', 4, 0);
INSERT INTO `t_paper_question` VALUES (1853356137089937415, 1850743197669621762, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"填空题\",\"title\":\"这是(__)一道填(__)空题(__)。\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"2\",\"title\":\"第三个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5},{\"rowKey\":\"2\",\"title\":\"第三个填空\",\"value\":\"答案三\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":4,\"type\":4,\"share\":1,\"id\":\"1844631905887234566\",\"success\":0,\"rowKey\":\"5\"}', 5, 0);
INSERT INTO `t_paper_question` VALUES (1853356137089937416, 1850743197669621762, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"填空题\",\"title\":\"这是(__)一道(__)填空题。\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":4,\"type\":4,\"share\":1,\"id\":\"1844631905887678466\",\"success\":0,\"rowKey\":\"6\"}', 6, 0);
INSERT INTO `t_paper_question` VALUES (1853356137089937417, 1850743197669621762, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"解答题\",\"title\":\"这是一道简答题，对此你有什么理解？这是一道简答题，对此你有什么理解？\",\"score\":15,\"level\":5,\"type\":5,\"share\":1,\"success\":0,\"keyWords\":[\"1111\",\"2222\"],\"matchNum\":2,\"id\":\"1846925581972267010\",\"rowKey\":\"7\"}', 7, 0);
INSERT INTO `t_paper_question` VALUES (1853356137089937418, 1850743197669621762, NULL, '{\"classifyId\":\"1844561674964144130\",\"name\":\"多选题\",\"title\":\"这是一道多选题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1},{\"rowKey\":\"3\",\"title\":\"D\",\"value\":\"选项四\",\"selected\":0,\"sort\":1}],\"answer\":[\"B\",\"C\"],\"score\":10,\"level\":2,\"type\":2,\"share\":1,\"id\":\"1890654436142407682\",\"success\":0,\"rowKey\":\"8\"}', 8, 0);
INSERT INTO `t_paper_question` VALUES (1853739904261345282, 1853739904248762370, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是第零个单选题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"analysis\":\"这是一个解析。\",\"level\":1,\"type\":1,\"share\":0,\"id\":\"1844589207403139073\",\"success\":0,\"rowKey\":\"1844589207403139073\"}', 0, 0);
INSERT INTO `t_paper_question` VALUES (1853739904261345283, 1853739904248762370, NULL, '{\"name\":\"单选题\",\"title\":\"这是第一个单选题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"level\":2,\"type\":1,\"share\":1,\"success\":0,\"rowKey\":\"1\"}', 1, 0);
INSERT INTO `t_paper_question` VALUES (1853739904261345284, 1853739904248762370, NULL, '{\"name\":\"单选题\",\"title\":\"这是第二个单选题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":5,\"level\":2,\"type\":1,\"share\":1,\"success\":0,\"rowKey\":\"2\"}', 2, 0);
INSERT INTO `t_paper_question` VALUES (1853739904261345285, 1853739904248762370, NULL, '{\"name\":\"单选题\",\"title\":\"这是第三个单选题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"C\",\"score\":5,\"level\":2,\"type\":1,\"share\":1,\"success\":0,\"rowKey\":\"3\"}', 3, 0);
INSERT INTO `t_paper_question` VALUES (1867416380527693826, 1867058827871465473, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一选项一选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"analysis\":\"这是一个解析。\",\"level\":1,\"type\":1,\"share\":1,\"id\":\"1844589207403139073\",\"success\":-1,\"rowKey\":\"0\"}', 0, 0);
INSERT INTO `t_paper_question` VALUES (1867416380527693827, 1867058827871465473, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一选项一选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":5,\"level\":1,\"type\":1,\"share\":0,\"id\":\"1844589234568139073\",\"success\":-1,\"rowKey\":\"1\"}', 1, 0);
INSERT INTO `t_paper_question` VALUES (1867416380527693828, 1867058827871465473, NULL, '{\"classifyId\":\"1844561674964144130\",\"name\":\"单选题\",\"title\":\"这是一道多选题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1},{\"rowKey\":\"3\",\"title\":\"D\",\"value\":\"选项四\",\"selected\":0,\"sort\":1}],\"answer\":[\"B\",\"C\"],\"score\":10,\"level\":2,\"type\":2,\"share\":1,\"id\":\"1844631436142407682\",\"success\":-1,\"rowKey\":\"2\"}', 2, 0);
INSERT INTO `t_paper_question` VALUES (1867416380527693829, 1867058827871465473, NULL, '{\"classifyId\":\"1844561875095359490\",\"name\":\"判断题\",\"title\":\"这是一道判断题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"level\":3,\"type\":3,\"share\":1,\"id\":\"1844631640312345921\",\"success\":-1,\"rowKey\":\"3\"}', 3, 0);
INSERT INTO `t_paper_question` VALUES (1873265940960501763, 1873265940960501762, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"analysis\":\"这是一个解析。\",\"level\":1,\"type\":1,\"share\":1,\"success\":-1,\"id\":\"1844589207403139073\",\"rowKey\":\"1844589207403139073\"}', 0, 0);
INSERT INTO `t_paper_question` VALUES (1873265940960501764, 1873265940960501762, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一选项一选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":5,\"level\":1,\"type\":1,\"share\":0,\"id\":\"1844589234568139073\",\"success\":-1,\"rowKey\":\"1844589234568139073\"}', 1, 0);
INSERT INTO `t_paper_question` VALUES (1873265940960501765, 1873265940960501762, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一道多选题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1},{\"rowKey\":\"3\",\"title\":\"D\",\"value\":\"选项四\",\"selected\":0,\"sort\":1}],\"answer\":[\"B\",\"C\"],\"score\":10,\"level\":2,\"type\":2,\"share\":1,\"id\":\"1844631436142407682\",\"success\":-1,\"rowKey\":\"1844631436142407682\"}', 2, 0);
INSERT INTO `t_paper_question` VALUES (1873265940960501766, 1873265940960501762, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"判断题\",\"title\":\"这是一道判断题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"level\":3,\"type\":3,\"share\":1,\"id\":\"1844631640312345921\",\"success\":-1,\"rowKey\":\"1844631640312345921\"}', 3, 0);
INSERT INTO `t_paper_question` VALUES (1873265940960501767, 1873265940960501762, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"判断题\",\"title\":\"这是一道判断题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":5,\"level\":3,\"type\":3,\"share\":1,\"id\":\"1844631640342097921\",\"success\":-1,\"rowKey\":\"1844631640342097921\"}', 4, 0);
INSERT INTO `t_paper_question` VALUES (1873265940960501768, 1873265940960501762, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"填空题\",\"title\":\"这是(__)一道填(__)空题(__)。\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"2\",\"title\":\"第三个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5},{\"rowKey\":\"2\",\"title\":\"第三个填空\",\"value\":\"答案三\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":4,\"type\":4,\"share\":1,\"id\":\"1844631905887234566\",\"success\":-1,\"rowKey\":\"1844631905887234566\"}', 5, 0);
INSERT INTO `t_paper_question` VALUES (1873265940960501769, 1873265940960501762, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"填空题\",\"title\":\"这是(__)一道(__)填空题。\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":4,\"type\":4,\"share\":1,\"id\":\"1844631905887678466\",\"success\":-1,\"rowKey\":\"1844631905887678466\"}', 6, 0);
INSERT INTO `t_paper_question` VALUES (1873265940960501770, 1873265940960501762, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"解答题\",\"title\":\"这是一道简答题，对此你有什么理解？这是一道简答题，对此你有什么理解？\",\"score\":15,\"level\":5,\"type\":5,\"share\":1,\"success\":-1,\"keyWords\":[\"1111\",\"2222\"],\"matchNum\":2,\"id\":\"1846925581972267010\",\"rowKey\":\"1846925581972267010\"}', 7, 0);
INSERT INTO `t_paper_question` VALUES (1873265940960501771, 1873265940960501762, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一个测试_单选题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":10,\"level\":2,\"type\":2,\"share\":1,\"success\":-1,\"rowKey\":\"undefined\"}', 8, 0);
INSERT INTO `t_paper_question` VALUES (1873265940960501772, 1873265940960501762, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一个测试_多选题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1},{\"rowKey\":\"3\",\"title\":\"D\",\"value\":\"选项四\",\"selected\":0,\"sort\":1}],\"answer\":[\"B\",\"C\"],\"score\":10,\"level\":2,\"type\":2,\"share\":1,\"success\":-1,\"rowKey\":\"undefined\"}', 9, 0);
INSERT INTO `t_paper_question` VALUES (1873265940960501773, 1873265940960501762, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一个测试_判断题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"level\":2,\"type\":3,\"share\":1,\"success\":-1,\"rowKey\":\"undefined\"}', 10, 0);
INSERT INTO `t_paper_question` VALUES (1873265940960501774, 1873265940960501762, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一(__)个测试_填空题(__)\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":2,\"type\":4,\"share\":1,\"success\":-1,\"id\":\"1872568030660759553\",\"rowKey\":\"1872568030660759553\"}', 11, 0);
INSERT INTO `t_paper_question` VALUES (1873265940960501775, 1873265940960501762, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一个测试_问答题？\",\"score\":15,\"level\":2,\"type\":5,\"share\":1,\"success\":-1,\"keyWords\":[\"你好\",\"中国\",\"我爱你\"],\"matchNum\":1,\"rowKey\":\"undefined\"}', 12, 0);
INSERT INTO `t_paper_question` VALUES (1873265940960501776, 1873265940960501762, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目1\",\"title\":\"这是一个导入的单选题目1吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":1,\"success\":-1,\"share\":1,\"rowKey\":\"undefined\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3}],\"answer\":\"A\",\"scoreBySuccess\":0}', 13, 0);
INSERT INTO `t_paper_question` VALUES (1873265940960501777, 1873265940960501762, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入的单选题目2吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":1,\"success\":-1,\"share\":1,\"rowKey\":\"undefined\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3}],\"answer\":\"B\",\"scoreBySuccess\":0}', 14, 0);
INSERT INTO `t_paper_question` VALUES (1873265940960501778, 1873265940960501762, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目1\",\"title\":\"这是一个导入的多选题目1吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":2,\"success\":-1,\"share\":1,\"rowKey\":\"undefined\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3},{\"title\":\"D\",\"value\":\"选项4\",\"selected\":0,\"sort\":4}],\"answer\":[\"A\",\"B\"],\"scoreBySuccess\":0}', 15, 0);
INSERT INTO `t_paper_question` VALUES (1873265940960501779, 1873265940960501762, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入的多选题目2吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":2,\"success\":-1,\"share\":1,\"rowKey\":\"undefined\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3},{\"title\":\"D\",\"value\":\"选项4\",\"selected\":0,\"sort\":4}],\"answer\":[\"B\",\"C\"],\"scoreBySuccess\":0}', 16, 0);
INSERT INTO `t_paper_question` VALUES (1873265940960501780, 1873265940960501762, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目1\",\"title\":\"这是一个导入的判断题目1吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":3,\"success\":-1,\"share\":1,\"rowKey\":\"undefined\",\"option\":[{\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":2}],\"answer\":\"A\",\"scoreBySuccess\":0}', 17, 0);
INSERT INTO `t_paper_question` VALUES (1873265940960501781, 1873265940960501762, NULL, '{\"classifyId\":\"1872929934222557186\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入的判断题目2吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":3,\"success\":-1,\"share\":1,\"rowKey\":\"undefined\",\"option\":[{\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":2}],\"answer\":\"A\",\"scoreBySuccess\":0}', 18, 0);
INSERT INTO `t_paper_question` VALUES (1873265940960501782, 1873265940960501762, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入(__)的填空题目吗。\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":4,\"success\":-1,\"share\":1,\"rowKey\":\"undefined\",\"option\":[{\"title\":\"第一个填空\",\"value\":\"\",\"selected\":0,\"sort\":1}],\"answer\":[{\"title\":\"第一个填空\",\"value\":\"这是第1个答案\",\"selected\":0,\"sort\":1}],\"scoreBySuccess\":0}', 19, 0);
INSERT INTO `t_paper_question` VALUES (1875364670916337665, 1873270331570511874, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"analysis\":\"这是一个解析。\",\"level\":1,\"type\":1,\"share\":1,\"success\":-1,\"id\":\"1844589207403139073\",\"rowKey\":\"0\"}', 0, 0);
INSERT INTO `t_paper_question` VALUES (1875364670979252226, 1873270331570511874, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一选项一选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":5,\"level\":1,\"type\":1,\"share\":0,\"id\":\"1844589234568139073\",\"success\":-1,\"rowKey\":\"1\"}', 1, 0);
INSERT INTO `t_paper_question` VALUES (1875364670979252227, 1873270331570511874, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目1\",\"title\":\"这是一个导入的单选题目1吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":1,\"success\":-1,\"share\":1,\"rowKey\":\"2\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3}],\"answer\":\"A\",\"scoreBySuccess\":0}', 2, 0);
INSERT INTO `t_paper_question` VALUES (1875364670979252228, 1873270331570511874, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入的单选题目2吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":1,\"success\":-1,\"share\":1,\"rowKey\":\"3\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3}],\"answer\":\"B\",\"scoreBySuccess\":0}', 3, 0);
INSERT INTO `t_paper_question` VALUES (1875364670979252229, 1873270331570511874, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"单选题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":10,\"level\":2,\"type\":1,\"share\":1,\"success\":-1,\"rowKey\":\"4\"}', 4, 0);
INSERT INTO `t_paper_question` VALUES (1886074224528453634, 1846020501915930625, NULL, '{\"title\":\"单选题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":10,\"level\":2,\"type\":1,\"share\":1,\"success\":-1,\"rowKey\":\"0\"}', 0, 0);
INSERT INTO `t_paper_question` VALUES (1886074224528453635, 1846020501915930625, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一选项一选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"analysis\":\"这是一个解析。\",\"level\":1,\"type\":1,\"share\":1,\"id\":\"1844589207403139073\",\"success\":-1,\"rowKey\":\"1\"}', 1, 0);
INSERT INTO `t_paper_question` VALUES (1886074224528453636, 1846020501915930625, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一选项一选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":5,\"level\":1,\"type\":1,\"share\":0,\"id\":\"1844589234568139073\",\"success\":-1,\"rowKey\":\"2\"}', 2, 0);
INSERT INTO `t_paper_question` VALUES (1886074224528453637, 1846020501915930625, NULL, '{\"classifyId\":\"1844561674964144130\",\"name\":\"单选题\",\"title\":\"这是一道多选题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1},{\"rowKey\":\"3\",\"title\":\"D\",\"value\":\"选项四\",\"selected\":0,\"sort\":1}],\"answer\":[\"B\",\"C\"],\"score\":10,\"level\":2,\"type\":2,\"share\":1,\"id\":\"1844631436142407682\",\"success\":-1,\"rowKey\":\"3\"}', 3, 0);
INSERT INTO `t_paper_question` VALUES (1886074224528453638, 1846020501915930625, NULL, '{\"classifyId\":\"1844561875095359490\",\"name\":\"判断题\",\"title\":\"这是一道判断题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"level\":3,\"type\":3,\"share\":1,\"id\":\"1844631640312345921\",\"success\":-1,\"rowKey\":\"4\"}', 4, 0);
INSERT INTO `t_paper_question` VALUES (1886074224528453639, 1846020501915930625, NULL, '{\"classifyId\":\"1844561875095359490\",\"name\":\"判断题\",\"title\":\"这是一道判断题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":5,\"level\":3,\"type\":3,\"share\":1,\"id\":\"1844631640342097921\",\"success\":-1,\"rowKey\":\"5\"}', 5, 0);
INSERT INTO `t_paper_question` VALUES (1886074224528453640, 1846020501915930625, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"填空题\",\"title\":\"这是(__)一道填(__)空题(__)。\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"2\",\"title\":\"第三个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5},{\"rowKey\":\"2\",\"title\":\"第三个填空\",\"value\":\"答案三\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":4,\"type\":4,\"share\":1,\"id\":\"1844631905887234566\",\"success\":-1,\"rowKey\":\"6\"}', 6, 0);
INSERT INTO `t_paper_question` VALUES (1886074224528453641, 1846020501915930625, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"填空题\",\"title\":\"这是(__)一道(__)填空题。\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":4,\"type\":4,\"share\":1,\"id\":\"1844631905887678466\",\"success\":-1,\"rowKey\":\"7\"}', 7, 0);
INSERT INTO `t_paper_question` VALUES (1886074224528453642, 1846020501915930625, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"解答题\",\"title\":\"这是一道简答题，对此你有什么理解？这是一道简答题，对此你有什么理解？\",\"score\":15,\"level\":5,\"type\":5,\"share\":1,\"success\":-1,\"keyWords\":[\"1111\",\"2222\"],\"matchNum\":2,\"id\":\"1846925581972267010\",\"rowKey\":\"8\"}', 8, 0);

-- ----------------------------
-- Table structure for t_paper_template
-- ----------------------------
DROP TABLE IF EXISTS `t_paper_template`;
CREATE TABLE `t_paper_template`  (
                                     `id` bigint(0) NOT NULL COMMENT '主键',
                                     `classify_id` bigint(0) NULL DEFAULT NULL COMMENT '所属分类ID',
                                     `type` int(0) NULL DEFAULT NULL COMMENT '试卷类型',
                                     `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '试卷标题',
                                     `duration` int(0) NULL DEFAULT NULL COMMENT '试卷时长（分钟）',
                                     `create_user` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
                                     `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                     `valid` int(0) NULL DEFAULT 1 COMMENT '是否有效（1有效，0无效）',
                                     `share` int(0) NULL DEFAULT 0 COMMENT '是否共享（1共享，0不共享）',
                                     `del_flag` int(0) NULL DEFAULT 0 COMMENT '是否删除（1删除，0不删除）',
                                     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '试卷模版表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_paper_template
-- ----------------------------
INSERT INTO `t_paper_template` VALUES (1846020501915930625, 1844737410605850626, 1, '这是第一场考试【2024】', 60, '1111111111111111111', '2024-10-15 02:49:19', 1, 1, 0);
INSERT INTO `t_paper_template` VALUES (1873265940960501762, 1844737410605850626, 1, '试卷测试', 60, '1111111111111111111', '2024-12-29 15:12:59', 1, 1, 1);
INSERT INTO `t_paper_template` VALUES (1873270331570511874, 1844737410605850626, 1, '几十块山卡拉开始了解', 60, '1111111111111111111', '2024-12-29 15:30:25', 1, 1, 0);

-- ----------------------------
-- Table structure for t_params
-- ----------------------------
DROP TABLE IF EXISTS `t_params`;
CREATE TABLE `t_params`  (
                             `id` bigint(0) NOT NULL COMMENT '主键',
                             `param_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数名称',
                             `param_key` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数Key',
                             `param_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数Value',
                             `del_flag` int(0) NULL DEFAULT 0 COMMENT '是否已删除',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '参数表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_params
-- ----------------------------
INSERT INTO `t_params` VALUES (1880450026120507393, '允许上传的文件类型', 'ALLOW_UPLOAD_FILE_TYPE', '.doc,.docx,.xls,.xlsx,.ppt,.pptx,.pdf,.txt,.mp4,.jpg,.jpeg,.png,.gif,.webp', 0);
INSERT INTO `t_params` VALUES (1880450374692335618, '单个文件的MaxSize（MB）', 'SINGLE_FILE_MAX_SIZE', '500', 0);
INSERT INTO `t_params` VALUES (1880450866017300482, 'TokenTTL（毫秒）', 'TOKEN_LIVE_TIME', '10800000', 0);

-- ----------------------------
-- Table structure for t_question_classify
-- ----------------------------
DROP TABLE IF EXISTS `t_question_classify`;
CREATE TABLE `t_question_classify`  (
                                        `id` bigint(0) NOT NULL COMMENT '主键',
                                        `parent_id` bigint(0) NULL DEFAULT 0 COMMENT '父ID',
                                        `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编号',
                                        `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
                                        `desc` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
                                        `del_flag` int(0) NULL DEFAULT 0 COMMENT '是否删除（0未删除，1删除）',
                                        `excel` int(0) NULL DEFAULT 0 COMMENT '是否来自于Excel导入（0否，1是）',
                                        PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '试题分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_question_classify
-- ----------------------------
INSERT INTO `t_question_classify` VALUES (1844561363260248066, 0, 'question-01', '试题1', NULL, 1, 0);
INSERT INTO `t_question_classify` VALUES (1844561674964144130, 1844561363260248066, 'question-1', '试题1-1', NULL, 1, 0);
INSERT INTO `t_question_classify` VALUES (1844561875095359490, 1844561363260248066, 'question-1-2', '试题1-2', NULL, 1, 0);
INSERT INTO `t_question_classify` VALUES (1844562179480195074, 0, 'question-2', '测试', NULL, 0, 0);
INSERT INTO `t_question_classify` VALUES (1854710937161379842, 0, 'fromExcel', '导入的分类1', NULL, 1, 1);
INSERT INTO `t_question_classify` VALUES (1854710940185473026, 0, 'fromExcel', '导入的分类2', NULL, 1, 1);
INSERT INTO `t_question_classify` VALUES (1854713012150677505, 0, 'fromExcel', '导入的分类1', NULL, 1, 1);
INSERT INTO `t_question_classify` VALUES (1854713019587178498, 0, 'fromExcel', '导入的分类2', NULL, 1, 1);
INSERT INTO `t_question_classify` VALUES (1854713246348029954, 0, 'fromExcel', '导入的分类1', NULL, 1, 1);
INSERT INTO `t_question_classify` VALUES (1854713247782481922, 0, 'fromExcel', '导入的分类2', NULL, 1, 1);
INSERT INTO `t_question_classify` VALUES (1864909335604891649, 0, '001', '应急管理', NULL, 0, 0);
INSERT INTO `t_question_classify` VALUES (1864909490228834306, 0, '002', '变化管理', NULL, 0, 0);
INSERT INTO `t_question_classify` VALUES (1864909803160735746, 0, '003', '重点工作、重点工程管理', NULL, 0, 0);
INSERT INTO `t_question_classify` VALUES (1864910036090114049, 0, '004', '调度管理', NULL, 0, 0);
INSERT INTO `t_question_classify` VALUES (1864910495711629314, 0, '005', '季节性调度', NULL, 0, 0);
INSERT INTO `t_question_classify` VALUES (1867419901003137025, 0, 'ces2', '测试2', NULL, 1, 0);
INSERT INTO `t_question_classify` VALUES (1867453170949128193, 0, 'cs2', '测试2', NULL, 1, 0);
INSERT INTO `t_question_classify` VALUES (1872837558498050050, 0, '1122465', '测试2', NULL, 1, 0);
INSERT INTO `t_question_classify` VALUES (1872837589754003458, 0, '23456', '123456', NULL, 1, 0);
INSERT INTO `t_question_classify` VALUES (1872926395677126657, 0, 'fromExcel', '导入的分类1', NULL, 1, 1);
INSERT INTO `t_question_classify` VALUES (1872926396146888706, 0, 'fromExcel', '导入的分类2', NULL, 1, 1);
INSERT INTO `t_question_classify` VALUES (1872929485595607041, 0, 'fromExcel', '导入的分类1', NULL, 1, 1);
INSERT INTO `t_question_classify` VALUES (1872929485851459586, 0, 'fromExcel', '导入的分类2', NULL, 1, 1);
INSERT INTO `t_question_classify` VALUES (1872929934000259074, 0, 'fromExcel', '导入的分类1', NULL, 0, 1);
INSERT INTO `t_question_classify` VALUES (1872929934222557186, 0, 'fromExcel', '导入的分类2', NULL, 0, 1);

-- ----------------------------
-- Table structure for t_question_template
-- ----------------------------
DROP TABLE IF EXISTS `t_question_template`;
CREATE TABLE `t_question_template`  (
                                        `id` bigint(0) NOT NULL COMMENT '主键',
                                        `classify_id` bigint(0) NULL DEFAULT NULL COMMENT '所属分类ID',
                                        `type` int(0) NULL DEFAULT NULL COMMENT '试题类型（1单选，2多选，3判断，4填空，5解答）',
                                        `name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '试题名称',
                                        `title` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '题干',
                                        `score` int(0) NULL DEFAULT NULL COMMENT '分数',
                                        `level` int(0) NULL DEFAULT NULL COMMENT '难度等级',
                                        `config` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '试题配置',
                                        `create_user` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
                                        `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                        `valid` int(0) NULL DEFAULT 1 COMMENT '是否有效（1有效，0无效）',
                                        `share` int(0) NULL DEFAULT 0 COMMENT '是否共享（1共享，0不共享）',
                                        `del_flag` int(0) NULL DEFAULT 0 COMMENT '是否删除（1删除，0不删除）',
                                        `excel` int(0) NULL DEFAULT 0 COMMENT '是否来自于Excel导入（0否，1是）',
                                        PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '试题模版表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_question_template
-- ----------------------------
INSERT INTO `t_question_template` VALUES (1844589207403139073, 1844562179480195074, 1, '单选题', '这是一个单选测试？', 5, 1, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"analysis\":\"这是一个解析。\",\"level\":1,\"type\":1,\"share\":1,\"success\":-1,\"id\":\"1844589207403139073\"}', '1111111111111111111', '2024-10-11 04:01:53', 1, 1, 0, 0);
INSERT INTO `t_question_template` VALUES (1844589234568139073, 1844562179480195074, 1, '单选题', '这是一个单选测试？', 5, 1, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一选项一选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":5,\"analysis\":null,\"level\":1,\"type\":1,\"share\":0,\"success\":-1,\"id\":\"1844589234568139073\"}', '1111111111111111111', '2024-10-11 04:01:53', 1, 1, 0, 0);
INSERT INTO `t_question_template` VALUES (1844631436142407682, 1844562179480195074, 2, '单选题', '这是一道多选题？', 10, 2, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一道多选题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1},{\"rowKey\":\"3\",\"title\":\"D\",\"value\":\"选项四\",\"selected\":0,\"sort\":1}],\"answer\":[\"B\",\"C\"],\"score\":10,\"analysis\":null,\"level\":2,\"type\":2,\"share\":1,\"success\":-1,\"id\":\"1844631436142407682\"}', '1111111111111111111', '2024-10-11 06:49:40', 1, 1, 0, 0);
INSERT INTO `t_question_template` VALUES (1844631640312345921, 1844562179480195074, 3, '判断题', '这是一道判断题？', 5, 3, '{\"classifyId\":\"1844562179480195074\",\"name\":\"判断题\",\"title\":\"这是一道判断题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"analysis\":null,\"level\":3,\"type\":3,\"share\":1,\"id\":\"1844631640312345921\",\"success\":-1}', '1111111111111111111', '2024-10-11 06:50:29', 1, 1, 0, 0);
INSERT INTO `t_question_template` VALUES (1844631640342097921, 1844562179480195074, 3, '判断题', '这是一道判断题？', 5, 3, '{\"classifyId\":\"1844562179480195074\",\"name\":\"判断题\",\"title\":\"这是一道判断题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":5,\"analysis\":null,\"level\":3,\"type\":3,\"share\":1,\"id\":\"1844631640342097921\",\"success\":-1}', '1111111111111111111', '2024-10-11 06:50:29', 1, 1, 0, 0);
INSERT INTO `t_question_template` VALUES (1844631905887234566, 1844562179480195074, 4, '填空题', '这是(__)一道填(__)空题(__)。', 5, 4, '{\"classifyId\":\"1844562179480195074\",\"name\":\"填空题\",\"title\":\"这是(__)一道填(__)空题(__)。\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":null,\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":null,\"selected\":0,\"sort\":5},{\"rowKey\":\"2\",\"title\":\"第三个填空\",\"value\":null,\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5},{\"rowKey\":\"2\",\"title\":\"第三个填空\",\"value\":\"答案三\",\"selected\":0,\"sort\":5}],\"score\":5,\"analysis\":null,\"level\":4,\"type\":4,\"share\":1,\"id\":\"1844631905887234566\",\"success\":-1}', '1111111111111111111', '2024-10-11 06:51:33', 1, 1, 0, 0);
INSERT INTO `t_question_template` VALUES (1844631905887678466, 1844562179480195074, 4, '填空题', '这是(__)一道(__)填空题。', 5, 4, '{\"classifyId\":\"1844562179480195074\",\"name\":\"填空题\",\"title\":\"这是(__)一道(__)填空题。\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":null,\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":null,\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5}],\"score\":5,\"analysis\":null,\"level\":4,\"type\":4,\"share\":1,\"id\":\"1844631905887678466\",\"success\":-1}', '1111111111111111111', '2024-10-11 06:51:33', 1, 1, 0, 0);
INSERT INTO `t_question_template` VALUES (1846925581972267010, 1844562179480195074, 5, '解答题', '这是一道简答题，对此你有什么理解？这是一道简答题，对此你有什么理解？', 15, 5, '{\"classifyId\":\"1844562179480195074\",\"name\":\"解答题\",\"title\":\"这是一道简答题，对此你有什么理解？这是一道简答题，对此你有什么理解？\",\"score\":15,\"analysis\":null,\"level\":5,\"type\":5,\"share\":1,\"success\":-1,\"keyWords\":[\"1111\",\"2222\"],\"matchNum\":2,\"id\":\"1846925581972267010\"}', '1111111111111111111', '2024-10-17 14:45:47', 1, 1, 0, 0);
INSERT INTO `t_question_template` VALUES (1872567793233793025, 1844562179480195074, 2, NULL, '测试一个测试_单选题', 10, 2, '{\"classifyId\":\"1844562179480195074\",\"name\":null,\"title\":\"测试一个测试_单选题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":10,\"analysis\":null,\"level\":2,\"type\":2,\"share\":1,\"success\":-1}', '-1', '2024-12-27 16:58:47', 1, 1, 0, 0);
INSERT INTO `t_question_template` VALUES (1872567855783448578, 1844562179480195074, 2, NULL, '测试一个测试_多选题', 10, 2, '{\"classifyId\":\"1844562179480195074\",\"name\":null,\"title\":\"测试一个测试_多选题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1},{\"rowKey\":\"3\",\"title\":\"D\",\"value\":\"选项四\",\"selected\":0,\"sort\":1}],\"answer\":[\"B\",\"C\"],\"score\":10,\"analysis\":null,\"level\":2,\"type\":2,\"share\":1,\"success\":-1}', '-1', '2024-12-27 16:59:02', 1, 1, 0, 0);
INSERT INTO `t_question_template` VALUES (1872567918505070593, 1844562179480195074, 3, NULL, '测试一个测试_判断题', 5, 2, '{\"classifyId\":\"1844562179480195074\",\"name\":null,\"title\":\"测试一个测试_判断题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"analysis\":null,\"level\":2,\"type\":3,\"share\":1,\"success\":-1}', '-1', '2024-12-27 16:59:17', 1, 1, 0, 0);
INSERT INTO `t_question_template` VALUES (1872568030660759553, 1844562179480195074, 4, NULL, '测试一(__)个测试_填空题(__)', 5, 2, '{\"classifyId\":\"1844562179480195074\",\"name\":null,\"title\":\"测试一(__)个测试_填空题(__)\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":null,\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":null,\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5}],\"score\":5,\"analysis\":null,\"level\":2,\"type\":4,\"share\":1,\"success\":-1,\"id\":\"1872568030660759553\"}', '-1', '2024-12-27 16:59:44', 1, 1, 0, 0);
INSERT INTO `t_question_template` VALUES (1872568212559335426, 1844562179480195074, 5, NULL, '测试一个测试_问答题？', 15, 2, '{\"classifyId\":\"1844562179480195074\",\"name\":null,\"title\":\"测试一个测试_问答题？\",\"score\":15,\"analysis\":null,\"level\":2,\"type\":5,\"share\":1,\"success\":-1,\"keyWords\":[\"你好\",\"中国\",\"我爱你\"],\"matchNum\":1}', '-1', '2024-12-27 17:00:27', 1, 1, 0, 0);
INSERT INTO `t_question_template` VALUES (1872931506503548930, 1872929934000259074, 1, '这是一个导入的题目1', '这是一个导入的单选题目1吗？', 2, 3, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目1\",\"title\":\"这是一个导入的单选题目1吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":1,\"success\":-1,\"share\":1,\"rowKey\":\"\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3}],\"answer\":\"A\",\"scoreBySuccess\":0}', '-1', '2024-12-28 17:04:03', 1, 1, 0, 1);
INSERT INTO `t_question_template` VALUES (1872931506503548931, 1872929934000259074, 1, '这是一个导入的题目2', '这是一个导入的单选题目2吗？', 2, 3, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入的单选题目2吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":1,\"success\":-1,\"share\":1,\"rowKey\":\"\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3}],\"answer\":\"B\",\"scoreBySuccess\":0}', '-1', '2024-12-28 17:04:03', 1, 1, 0, 1);
INSERT INTO `t_question_template` VALUES (1872931506570657794, 1872929934000259074, 2, '这是一个导入的题目1', '这是一个导入的多选题目1吗？', 2, 3, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目1\",\"title\":\"这是一个导入的多选题目1吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":2,\"success\":-1,\"share\":1,\"rowKey\":\"\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3},{\"title\":\"D\",\"value\":\"选项4\",\"selected\":0,\"sort\":4}],\"answer\":[\"A\",\"B\"],\"scoreBySuccess\":0}', '-1', '2024-12-28 17:04:03', 1, 1, 0, 1);
INSERT INTO `t_question_template` VALUES (1872931506633572353, 1872929934000259074, 2, '这是一个导入的题目2', '这是一个导入的多选题目2吗？', 2, 3, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入的多选题目2吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":2,\"success\":-1,\"share\":1,\"rowKey\":\"\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3},{\"title\":\"D\",\"value\":\"选项4\",\"selected\":0,\"sort\":4}],\"answer\":[\"B\",\"C\"],\"scoreBySuccess\":0}', '-1', '2024-12-28 17:04:03', 1, 1, 0, 1);
INSERT INTO `t_question_template` VALUES (1872931506633572354, 1872929934000259074, 3, '这是一个导入的题目1', '这是一个导入的判断题目1吗？', 2, 3, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目1\",\"title\":\"这是一个导入的判断题目1吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":3,\"success\":-1,\"share\":1,\"rowKey\":\"\",\"option\":[{\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":2}],\"answer\":\"A\",\"scoreBySuccess\":0}', '-1', '2024-12-28 17:04:03', 1, 1, 0, 1);
INSERT INTO `t_question_template` VALUES (1872931506696486913, 1872929934222557186, 3, '这是一个导入的题目2', '这是一个导入的判断题目2吗？', 2, 3, '{\"classifyId\":\"1872929934222557186\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入的判断题目2吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":3,\"success\":-1,\"share\":1,\"rowKey\":\"\",\"option\":[{\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":2}],\"answer\":\"A\",\"scoreBySuccess\":0}', '-1', '2024-12-28 17:04:03', 1, 1, 0, 1);
INSERT INTO `t_question_template` VALUES (1872931506696486914, 1872929934000259074, 4, '这是一个导入的题目2', '这是一个导入(__)的填空题目吗。', 2, 3, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入(__)的填空题目吗。\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":4,\"success\":-1,\"share\":1,\"rowKey\":\"\",\"option\":[{\"title\":\"第一个填空\",\"value\":\"\",\"selected\":0,\"sort\":1}],\"answer\":[{\"title\":\"第一个填空\",\"value\":\"这是第1个答案\",\"selected\":0,\"sort\":1}],\"scoreBySuccess\":0}', '-1', '2024-12-28 17:04:03', 1, 1, 0, 1);
INSERT INTO `t_question_template` VALUES (1872931506763595777, 1872929934222557186, 4, '这是一个导入的题目1', '这是一个(__)导入(__)的填空题目(__)。', 2, 3, '{\"classifyId\":\"1872929934222557186\",\"name\":\"这是一个导入的题目1\",\"title\":\"这是一个(__)导入(__)的填空题目(__)。\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":4,\"success\":-1,\"share\":1,\"rowKey\":\"\",\"option\":[{\"title\":\"第一个填空\",\"value\":\"\",\"selected\":0,\"sort\":1},{\"title\":\"第二个填空\",\"value\":\"\",\"selected\":0,\"sort\":2},{\"title\":\"第三个填空\",\"value\":\"\",\"selected\":0,\"sort\":3}],\"answer\":[{\"title\":\"第一个填空\",\"value\":\"这是第1个答案\",\"selected\":0,\"sort\":1},{\"title\":\"第二个填空\",\"value\":\"这是第2个答案\",\"selected\":0,\"sort\":2},{\"title\":\"第三个填空\",\"value\":\"这是第3个答案\",\"selected\":0,\"sort\":3}],\"scoreBySuccess\":0}', '-1', '2024-12-28 17:04:03', 1, 1, 0, 1);
INSERT INTO `t_question_template` VALUES (1872931506763595778, 1872929934000259074, 5, '这是一个导入的题目1', '这是一个导入的解答题目1吗？', 2, 3, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目1\",\"title\":\"这是一个导入的解答题目1吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":5,\"success\":-1,\"share\":1,\"rowKey\":\"\",\"answer\":\"\",\"keyWords\":[\"关键字1\",\"关键字3\",\"关键字2\"],\"matchNum\":1,\"scoreBySuccess\":0}', '-1', '2024-12-28 17:04:03', 1, 1, 0, 1);
INSERT INTO `t_question_template` VALUES (1872931506830704642, 1872929934222557186, 5, '这是一个导入的题目2', '这是一个导入的解答题目2吗？', 2, 3, '{\"classifyId\":\"1872929934222557186\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入的解答题目2吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":5,\"success\":-1,\"share\":1,\"rowKey\":\"\",\"answer\":\"\",\"keyWords\":[\"关键字1\",\"关键字3\",\"关键字2\"],\"matchNum\":2,\"scoreBySuccess\":0}', '-1', '2024-12-28 17:04:03', 1, 1, 0, 1);
INSERT INTO `t_question_template` VALUES (1873229853592510465, 1844562179480195074, 2, NULL, '哈哈哈万达声卡？', 10, 2, '{\"classifyId\":\"1844562179480195074\",\"name\":null,\"title\":\"哈哈哈万达声卡？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":10,\"analysis\":null,\"level\":2,\"type\":2,\"share\":1,\"success\":-1}', '1111111111111111111', '2024-12-29 12:49:35', 1, 1, 0, 0);
INSERT INTO `t_question_template` VALUES (1873264692794351618, 1844562179480195074, 2, NULL, 'HAHHAHHHAHAHAA', 10, 2, '{\"classifyId\":\"1844562179480195074\",\"name\":null,\"title\":\"HAHHAHHHAHAHAA\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":[\"B\"],\"score\":10,\"analysis\":null,\"level\":2,\"type\":2,\"share\":1,\"success\":-1,\"id\":\"1873264692794351618\"}', '1111111111111111111', '2024-12-29 15:08:01', 1, 1, 0, 0);
INSERT INTO `t_question_template` VALUES (1873265150690713601, 1844562179480195074, 1, NULL, '单选题', 10, 2, '{\"classifyId\":\"1844562179480195074\",\"name\":null,\"title\":\"单选题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":10,\"analysis\":null,\"level\":2,\"type\":1,\"share\":1,\"success\":-1}', '1111111111111111111', '2024-12-29 15:09:50', 1, 1, 0, 0);
INSERT INTO `t_question_template` VALUES (1873265244429213697, 1844562179480195074, 2, NULL, '多选题', 10, 2, '{\"classifyId\":\"1844562179480195074\",\"name\":null,\"title\":\"多选题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1},{\"rowKey\":\"3\",\"title\":\"D\",\"value\":\"选项四\",\"selected\":0,\"sort\":1}],\"answer\":[\"A\",\"B\",\"C\"],\"score\":10,\"analysis\":null,\"level\":2,\"type\":2,\"share\":1,\"success\":-1}', '1111111111111111111', '2024-12-29 15:10:13', 1, 1, 0, 0);
INSERT INTO `t_question_template` VALUES (1873265300834213889, 1844562179480195074, 3, NULL, '判断题', 5, 2, '{\"classifyId\":\"1844562179480195074\",\"name\":null,\"title\":\"判断题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"analysis\":null,\"level\":2,\"type\":3,\"share\":1,\"success\":-1}', '1111111111111111111', '2024-12-29 15:10:26', 1, 1, 0, 0);
INSERT INTO `t_question_template` VALUES (1873265448675041281, 1844562179480195074, 4, NULL, '填(__)空(__)题(__)', 5, 2, '{\"classifyId\":\"1844562179480195074\",\"name\":null,\"title\":\"填(__)空(__)题(__)\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":null,\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":null,\"selected\":0,\"sort\":5},{\"rowKey\":\"2\",\"title\":\"第三个填空\",\"value\":null,\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5},{\"rowKey\":\"2\",\"title\":\"第三个填空\",\"value\":\"答案三\",\"selected\":0,\"sort\":5}],\"score\":5,\"analysis\":null,\"level\":2,\"type\":4,\"share\":1,\"success\":-1}', '1111111111111111111', '2024-12-29 15:11:01', 1, 1, 0, 0);
INSERT INTO `t_question_template` VALUES (1873265548835020801, 1844562179480195074, 5, NULL, '解答题', 15, 2, '{\"classifyId\":\"1844562179480195074\",\"name\":null,\"title\":\"解答题\",\"score\":15,\"analysis\":\"\",\"level\":2,\"type\":5,\"share\":1,\"success\":-1,\"keyWords\":[\"A\",\"B\"],\"matchNum\":1}', '1111111111111111111', '2024-12-29 15:11:25', 1, 1, 0, 0);

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`  (
                           `id` bigint(0) NOT NULL COMMENT '主键',
                           `parent_id` bigint(0) NULL DEFAULT 0 COMMENT '父ID',
                           `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名称',
                           `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色编号',
                           `valid` int(0) NULL DEFAULT 1 COMMENT '是否有效（1有效，0无效）',
                           `sort` int(0) NULL DEFAULT NULL COMMENT '排序',
                           `del_flag` int(0) NULL DEFAULT 0 COMMENT '是否删除（1删除，0未删除）',
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES (1111111111111111111, 0, '超级管理员', 'administrator', 1, 0, 0);
INSERT INTO `t_role` VALUES (1887070057067560961, 0, '管理员', 'admin', 1, 1, 0);
INSERT INTO `t_role` VALUES (1887430996501467137, 0, '基础角色', 'base_role', 1, 99, 0);

-- ----------------------------
-- Table structure for t_role_auth
-- ----------------------------
DROP TABLE IF EXISTS `t_role_auth`;
CREATE TABLE `t_role_auth`  (
                                `role_id` bigint(0) NOT NULL COMMENT '主键',
                                `auth_id` bigint(0) NOT NULL COMMENT '父ID',
                                `auth_type` int(0) NOT NULL COMMENT '权限类型（1菜单，2按钮，3接口）'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色_权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_role_auth
-- ----------------------------
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1873662796710297602, 1);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887063179793121282, 2);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887063598833451009, 2);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887064016795844610, 2);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887064064778682369, 2);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887064911596077058, 2);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887064947889389569, 2);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887060807440228354, 2);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887065457656709122, 2);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887065781016576002, 2);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887069129673392130, 2);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887068379861860354, 2);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887068838941016065, 2);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887066407792394241, 2);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887066888501575682, 2);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887067868345516033, 2);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887060880920240129, 2);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887853120185745409, 2);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887853247537397761, 2);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887853394933628930, 2);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887853450512351233, 2);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887064695383900162, 2);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887065182485200897, 2);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887854002772164609, 2);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368833212731393, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368833145622533, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368833212731394, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368831434346497, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368831446929410, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368831446929411, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368831446929412, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368831501455362, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368831501455363, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368831501455364, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368831568564228, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368832948490242, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368832747163649, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368832747163650, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368831568564227, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368832092852229, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368832306761729, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368831895719939, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368832306761730, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368832306761731, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368831895719940, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368831958634498, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368832025743363, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368832092852227, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368832025743361, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368831233019908, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368831308517377, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368832814272516, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368832814272517, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368832814272518, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368832948490246, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368832948490245, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368831862165505, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368831862165506, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368833145622530, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368833212731396, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368831765696513, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368831765696515, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368832701026308, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368832701026307, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368832562614274, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368832482922501, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368832482922498, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368832222875651, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368832222875650, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368832159961092, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368831102996482, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368831102996484, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887386211451527169, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368830972973058, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368830972973060, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887409368325550081, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368831233019906, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368831233019907, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887409368719814658, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887428167535693826, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887519037974904833, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887686303433732098, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887766104139988994, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887795457699725313, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887795457636810755, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368831635673093, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368831635673092, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368831635673091, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368831635673090, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887795457699725314, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368831568564230, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368833015599106, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368833015599107, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368833015599108, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368833015599109, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368833082707970, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368833082707971, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887766104202903553, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368831568564229, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368833145622532, 3);
INSERT INTO `t_role_auth` VALUES (1887430996501467137, 1887368831765696514, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1873662796710297602, 1);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1873661939314868226, 1);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1873663010963734530, 1);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1873665587134611458, 1);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1873717926629552129, 1);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1873718395699539970, 1);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1873718566239940609, 1);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1873718825632477186, 1);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1873718995677949953, 1);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887852869190205441, 1);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887062629131337729, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887062692809261057, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887062796966412290, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887062904395120641, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887063111069450242, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887063179793121282, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887063459171516418, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887063504042180609, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887063541224685569, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887063598833451009, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887063662704312321, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887063776751632386, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887063848511979522, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887063940761501698, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887064016795844610, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887064064778682369, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887064280571428865, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887064412964634626, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887064695383900162, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887064794554023937, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887064832663470081, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887064863403524097, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887064911596077058, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887064947889389569, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887065063341801473, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887065133713833986, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887065182485200897, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887060807440228354, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887060880920240129, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887065339096317954, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887065378925428737, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887065426224594946, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887065457656709122, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887065597855514625, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887065657364299778, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887065708857769985, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887065737819439106, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887065781016576002, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887065892278878210, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887799715539025922, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887853394933628930, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887854002772164609, 2);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368832222875652, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368832222875653, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368832306761729, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368832306761730, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368832306761731, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368832306761732, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831895719937, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831895719938, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831895719939, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831895719940, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831958634498, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831958634499, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831958634500, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831958634501, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368832025743361, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368832025743362, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368832025743363, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368832025743364, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368832092852226, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368832092852227, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368832092852228, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368832092852229, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831233019908, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831308517377, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831308517378, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831308517379, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831308517380, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831367237633, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831367237634, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831367237635, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831434346497, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831446929409, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831446929410, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831446929411, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831446929412, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831501455362, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831501455363, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831501455364, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831501455365, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831501455366, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831568564226, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831568564227, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831568564228, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831568564229, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831568564230, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831635673090, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831635673091, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831635673092, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831635673093, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368832814272514, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368832814272515, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368832814272516, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368832814272517, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368832814272518, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368832881381377, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368832881381378, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368832881381379, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368832881381380, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368832948490242, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368832948490243, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368832948490244, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368832948490245, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368832948490246, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368833015599106, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368833015599107, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368833015599108, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368833015599109, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368833082707970, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368833082707971, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368832747163649, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368832747163650, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831765696516, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831832805377, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831849582594, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831862165505, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368831862165506, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368833082707972, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368833082707973, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368833145622529, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368833145622530, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368833145622532, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368833145622533, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368833212731393, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368833212731394, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368833212731396, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887766104051908609, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887766104085463041, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887766104085463042, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887766104139988994, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887766104139988995, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887766104139988996, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887766104139988997, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887766104202903553, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887766104202903554, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887795457590673409, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887795457636810753, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887795457636810754, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887795457636810755, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887795457699725313, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887795457699725314, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887795457699725315, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368833145622531, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1887368833212731395, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1888096197693534210, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1888096197743865858, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1888096197743865859, 3);
INSERT INTO `t_role_auth` VALUES (1887070057067560961, 1888096197743865860, 3);

-- ----------------------------
-- Table structure for t_storage_protocol
-- ----------------------------
DROP TABLE IF EXISTS `t_storage_protocol`;
CREATE TABLE `t_storage_protocol`  (
                                       `id` bigint(0) NOT NULL COMMENT '主键',
                                       `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
                                       `protocol` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储协议',
                                       `protocol_config` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '协议配置',
                                       `create_user` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
                                       `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                       `update_user` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
                                       `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                                       `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                                       `valid` int(0) NULL DEFAULT 1 COMMENT '是否有效',
                                       `del_flag` int(0) NULL DEFAULT 0 COMMENT '是否已删除',
                                       PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件存储协议配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_storage_protocol
-- ----------------------------
INSERT INTO `t_storage_protocol` VALUES (1881897103669075970, '本地存储（windows）', 'LOCAL', '{\"localDir\":\"E:\\\\tmp\\\\etm\"}', '1111111111111111111', '2025-01-22 10:50:08', '1111111111111111111', '2025-02-03 00:21:59', NULL, 1, 0);

-- ----------------------------
-- Table structure for t_task
-- ----------------------------
DROP TABLE IF EXISTS `t_task`;
CREATE TABLE `t_task`  (
                           `id` bigint(0) NOT NULL COMMENT '主键',
                           `exam_tran_id` bigint(0) NULL DEFAULT NULL COMMENT '考试及培训id',
                           `classify_id` bigint(0) NULL DEFAULT NULL COMMENT '考试及培训分类id',
                           `classify_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类名称',
                           `show_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '考试及培训名称',
                           `user_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户编码',
                           `publish_time` datetime(0) NULL DEFAULT NULL COMMENT '发布时间-做排序使用',
                           `limit_start_time` datetime(0) NULL DEFAULT NULL COMMENT '考试及培训开始时间',
                           `limit_end_time` datetime(0) NULL DEFAULT NULL COMMENT '考试及培训结束时间',
                           `do_start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始答题及开始培训时间',
                           `do_end_time` datetime(0) NULL DEFAULT NULL COMMENT '提交试卷及结束培训时间',
                           `score` int(0) NULL DEFAULT NULL COMMENT '得分',
                           `show_type` int(0) NULL DEFAULT 1 COMMENT '类型（0考试，1培训）',
                           `show_status` int(0) NULL DEFAULT 0 COMMENT '状态（0待考试及待培训，1已考试及已培训，2待审批的考试）',
                           `valid` int(0) NULL DEFAULT 1 COMMENT '是否有效（1有效，0无效）',
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户考试培训表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_task
-- ----------------------------
INSERT INTO `t_task` VALUES (1879431170308546561, 1879079215375646722, 1850706920052195329, '测试', '这是一个考试测试', '1111111111111111111', '2025-01-15 15:31:24', '2025-01-14 16:10:34', '2025-01-21 16:10:35', '2025-01-15 15:31:29', '2025-01-15 15:35:04', 45, 0, 1, 1);
INSERT INTO `t_task` VALUES (1879431170308546562, 1879079215375646722, 1850706920052195329, '测试', '这是一个考试测试', '1873746662934450177', '2025-01-15 15:31:24', '2025-01-14 16:10:34', '2025-01-21 16:10:35', NULL, NULL, NULL, 0, 0, 1);
INSERT INTO `t_task` VALUES (1879431170308546563, 1879079215375646722, 1850706920052195329, '测试', '这是一个考试测试', '1873749058125398017', '2025-01-15 15:31:24', '2025-01-14 16:10:34', '2025-01-21 16:10:35', NULL, NULL, NULL, 0, 0, 1);

-- ----------------------------
-- Table structure for t_train
-- ----------------------------
DROP TABLE IF EXISTS `t_train`;
CREATE TABLE `t_train`  (
                            `id` bigint(0) NOT NULL COMMENT '主键',
                            `classify_id` bigint(0) NULL DEFAULT NULL COMMENT '培训分类ID',
                            `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '培训标题',
                            `des` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '培训描述',
                            `limit_start_time` datetime(0) NULL DEFAULT NULL COMMENT '培训开始时间',
                            `limit_end_time` datetime(0) NULL DEFAULT NULL COMMENT '培训结束时间',
                            `qualified_score` int(0) NULL DEFAULT NULL COMMENT '合格分数',
                            `create_user` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
                            `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                            `published` int(0) NULL DEFAULT 0 COMMENT '是否发布（1已发布，0未发布）',
                            `publish_time` datetime(0) NULL DEFAULT NULL COMMENT '发布时间',
                            `duration` int(0) NULL DEFAULT NULL COMMENT '答题时长',
                            `del_flag` int(0) NULL DEFAULT 0 COMMENT '是否删除（1删除，0未删除）',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '培训表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_train
-- ----------------------------
INSERT INTO `t_train` VALUES (1881003994613665794, 1844749710083588097, '122345678', '23456', '2025-01-19 23:40:47', '2025-01-27 10:39:33', 60, '1111111111111111111', '2025-01-19 23:41:14', 0, '2025-01-21 11:21:21', 60, 0);

-- ----------------------------
-- Table structure for t_train_classify
-- ----------------------------
DROP TABLE IF EXISTS `t_train_classify`;
CREATE TABLE `t_train_classify`  (
                                     `id` bigint(0) NOT NULL COMMENT '主键',
                                     `parent_id` bigint(0) NULL DEFAULT 0 COMMENT '父ID',
                                     `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编号',
                                     `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
                                     `desc` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
                                     `del_flag` int(0) NULL DEFAULT 0 COMMENT '是否删除（0未删除，1删除）',
                                     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '培训分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_train_classify
-- ----------------------------
INSERT INTO `t_train_classify` VALUES (1844749657419907073, 0, 'train-1', '培训1', NULL, 1);
INSERT INTO `t_train_classify` VALUES (1844749710083588097, 0, 'train-2', '测试', NULL, 0);
INSERT INTO `t_train_classify` VALUES (1844749813049556994, 1844749657419907073, 'train-1-1', '培训1-1', NULL, 1);
INSERT INTO `t_train_classify` VALUES (1844749836957089794, 0, '222', '222xxx', NULL, 1);

-- ----------------------------
-- Table structure for t_train_file
-- ----------------------------
DROP TABLE IF EXISTS `t_train_file`;
CREATE TABLE `t_train_file`  (
                                 `id` bigint(0) NOT NULL COMMENT '主键',
                                 `train_id` bigint(0) NULL DEFAULT NULL COMMENT '培训ID',
                                 `file_id` bigint(0) NULL DEFAULT NULL COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件ID',
                                 `file_extension` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件后缀',
                                 `file_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件名称',
                                 `file_desc` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件描述',
                                 `file_type` int(0) NULL DEFAULT NULL COMMENT '文件类型：1 视频，2 PDF',
                                 `file_size` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件大小',
                                 `file_duration` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '时长',
                                 `upload_time` datetime(0) NULL DEFAULT NULL COMMENT '上传时间',
                                 `sort` int(0) NULL DEFAULT NULL COMMENT '排序',
                                 `del_flag` int(0) NULL DEFAULT 0 COMMENT '是否删除（0未删除，1删除）',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '培训课件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_train_file
-- ----------------------------
INSERT INTO `t_train_file` VALUES (1881003994680774657, 1881003994613665794, 1880994709917724674, 'txt', '新建文本文档', NULL, 2, '929 B', NULL, '2025-01-19 23:40:54', 0, 0);

-- ----------------------------
-- Table structure for t_train_question
-- ----------------------------
DROP TABLE IF EXISTS `t_train_question`;
CREATE TABLE `t_train_question`  (
                                     `id` bigint(0) NOT NULL COMMENT '主键',
                                     `train_id` bigint(0) NULL DEFAULT NULL COMMENT '培训ID',
                                     `question_id` bigint(0) NULL DEFAULT NULL COMMENT '试题ID',
                                     `config` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '试题配置',
                                     `into_video` int(0) NULL DEFAULT 0 COMMENT '是否插入视频中（1插入，0不插入）',
                                     `into_time` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '插入视频中的时间',
                                     `sort` int(0) NULL DEFAULT NULL COMMENT '排序',
                                     `del_flag` int(0) NULL DEFAULT 0 COMMENT '是否删除（0未删除，1删除）',
                                     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '培训试题表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_train_question
-- ----------------------------
INSERT INTO `t_train_question` VALUES (1881003994747883521, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"analysis\":\"这是一个解析。\",\"level\":1,\"type\":1,\"share\":1,\"success\":-1,\"id\":\"1844589207403139073\",\"rowKey\":\"1844589207403139073\"}', 1, NULL, 0, 1);
INSERT INTO `t_train_question` VALUES (1881003994747883522, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一选项一选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":5,\"level\":1,\"type\":1,\"share\":0,\"success\":-1,\"id\":\"1844589234568139073\",\"rowKey\":\"1844589234568139073\"}', 1, NULL, 1, 1);
INSERT INTO `t_train_question` VALUES (1881003994747883523, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一道多选题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1},{\"rowKey\":\"3\",\"title\":\"D\",\"value\":\"选项四\",\"selected\":0,\"sort\":1}],\"answer\":[\"B\",\"C\"],\"score\":10,\"level\":2,\"type\":2,\"share\":1,\"success\":-1,\"id\":\"1844631436142407682\",\"rowKey\":\"1844631436142407682\"}', 1, NULL, 2, 1);
INSERT INTO `t_train_question` VALUES (1881003994747883524, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"判断题\",\"title\":\"这是一道判断题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"level\":3,\"type\":3,\"share\":1,\"id\":\"1844631640312345921\",\"success\":-1,\"rowKey\":\"1844631640312345921\"}', 1, NULL, 3, 1);
INSERT INTO `t_train_question` VALUES (1881003994747883525, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"判断题\",\"title\":\"这是一道判断题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":5,\"level\":3,\"type\":3,\"share\":1,\"id\":\"1844631640342097921\",\"success\":-1,\"rowKey\":\"1844631640342097921\"}', 1, NULL, 4, 1);
INSERT INTO `t_train_question` VALUES (1881003994747883526, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"填空题\",\"title\":\"这是(__)一道填(__)空题(__)。\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"2\",\"title\":\"第三个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5},{\"rowKey\":\"2\",\"title\":\"第三个填空\",\"value\":\"答案三\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":4,\"type\":4,\"share\":1,\"id\":\"1844631905887234566\",\"success\":-1,\"rowKey\":\"1844631905887234566\"}', 1, NULL, 5, 1);
INSERT INTO `t_train_question` VALUES (1881003994747883527, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"填空题\",\"title\":\"这是(__)一道(__)填空题。\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":4,\"type\":4,\"share\":1,\"id\":\"1844631905887678466\",\"success\":-1,\"rowKey\":\"1844631905887678466\"}', 1, NULL, 6, 1);
INSERT INTO `t_train_question` VALUES (1881003994747883528, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"解答题\",\"title\":\"这是一道简答题，对此你有什么理解？这是一道简答题，对此你有什么理解？\",\"score\":15,\"level\":5,\"type\":5,\"share\":1,\"success\":-1,\"keyWords\":[\"1111\",\"2222\"],\"matchNum\":2,\"id\":\"1846925581972267010\",\"rowKey\":\"1846925581972267010\"}', 1, NULL, 7, 1);
INSERT INTO `t_train_question` VALUES (1881003994747883529, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一个测试_单选题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":10,\"level\":2,\"type\":2,\"share\":1,\"success\":-1,\"rowKey\":\"undefined\"}', 1, NULL, 8, 1);
INSERT INTO `t_train_question` VALUES (1881003994747883530, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一个测试_多选题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1},{\"rowKey\":\"3\",\"title\":\"D\",\"value\":\"选项四\",\"selected\":0,\"sort\":1}],\"answer\":[\"B\",\"C\"],\"score\":10,\"level\":2,\"type\":2,\"share\":1,\"success\":-1,\"rowKey\":\"undefined\"}', 1, NULL, 9, 1);
INSERT INTO `t_train_question` VALUES (1881003994747883531, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一个测试_判断题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"level\":2,\"type\":3,\"share\":1,\"success\":-1,\"rowKey\":\"undefined\"}', 1, NULL, 10, 1);
INSERT INTO `t_train_question` VALUES (1881003994747883532, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一(__)个测试_填空题(__)\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":2,\"type\":4,\"share\":1,\"success\":-1,\"id\":\"1872568030660759553\",\"rowKey\":\"1872568030660759553\"}', 1, NULL, 11, 1);
INSERT INTO `t_train_question` VALUES (1881003994747883533, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一个测试_问答题？\",\"score\":15,\"level\":2,\"type\":5,\"share\":1,\"success\":-1,\"keyWords\":[\"你好\",\"中国\",\"我爱你\"],\"matchNum\":1,\"rowKey\":\"undefined\"}', 1, NULL, 12, 1);
INSERT INTO `t_train_question` VALUES (1881003994747883534, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目1\",\"title\":\"这是一个导入的单选题目1吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":1,\"success\":-1,\"share\":1,\"rowKey\":\"undefined\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3}],\"answer\":\"A\",\"scoreBySuccess\":0}', 1, NULL, 13, 1);
INSERT INTO `t_train_question` VALUES (1881003994747883535, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入的单选题目2吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":1,\"success\":-1,\"share\":1,\"rowKey\":\"undefined\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3}],\"answer\":\"B\",\"scoreBySuccess\":0}', 1, NULL, 14, 1);
INSERT INTO `t_train_question` VALUES (1881003994747883536, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目1\",\"title\":\"这是一个导入的多选题目1吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":2,\"success\":-1,\"share\":1,\"rowKey\":\"undefined\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3},{\"title\":\"D\",\"value\":\"选项4\",\"selected\":0,\"sort\":4}],\"answer\":[\"A\",\"B\"],\"scoreBySuccess\":0}', 1, NULL, 15, 1);
INSERT INTO `t_train_question` VALUES (1881003994747883537, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入的多选题目2吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":2,\"success\":-1,\"share\":1,\"rowKey\":\"undefined\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3},{\"title\":\"D\",\"value\":\"选项4\",\"selected\":0,\"sort\":4}],\"answer\":[\"B\",\"C\"],\"scoreBySuccess\":0}', 1, NULL, 16, 1);
INSERT INTO `t_train_question` VALUES (1881003994747883538, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目1\",\"title\":\"这是一个导入的判断题目1吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":3,\"success\":-1,\"share\":1,\"rowKey\":\"undefined\",\"option\":[{\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":2}],\"answer\":\"A\",\"scoreBySuccess\":0}', 1, NULL, 17, 1);
INSERT INTO `t_train_question` VALUES (1881003994747883539, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934222557186\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入的判断题目2吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":3,\"success\":-1,\"share\":1,\"rowKey\":\"undefined\",\"option\":[{\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":2}],\"answer\":\"A\",\"scoreBySuccess\":0}', 1, NULL, 18, 1);
INSERT INTO `t_train_question` VALUES (1881003994747883540, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入(__)的填空题目吗。\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":4,\"success\":-1,\"share\":1,\"rowKey\":\"undefined\",\"option\":[{\"title\":\"第一个填空\",\"value\":\"\",\"selected\":0,\"sort\":1}],\"answer\":[{\"title\":\"第一个填空\",\"value\":\"这是第1个答案\",\"selected\":0,\"sort\":1}],\"scoreBySuccess\":0}', 1, NULL, 19, 1);
INSERT INTO `t_train_question` VALUES (1881169669390909441, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"analysis\":\"这是一个解析。\",\"level\":1,\"type\":1,\"share\":1,\"success\":-1,\"id\":\"1844589207403139073\",\"rowKey\":\"0\"}', 1, NULL, 0, 1);
INSERT INTO `t_train_question` VALUES (1881169669390909442, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一选项一选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":5,\"level\":1,\"type\":1,\"share\":0,\"success\":-1,\"id\":\"1844589234568139073\",\"rowKey\":\"1\"}', 1, NULL, 1, 1);
INSERT INTO `t_train_question` VALUES (1881169669390909443, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一道多选题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1},{\"rowKey\":\"3\",\"title\":\"D\",\"value\":\"选项四\",\"selected\":0,\"sort\":1}],\"answer\":[\"B\",\"C\"],\"score\":10,\"level\":2,\"type\":2,\"share\":1,\"success\":-1,\"id\":\"1844631436142407682\",\"rowKey\":\"2\"}', 1, NULL, 2, 1);
INSERT INTO `t_train_question` VALUES (1881169669390909444, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"判断题\",\"title\":\"这是一道判断题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"level\":3,\"type\":3,\"share\":1,\"id\":\"1844631640312345921\",\"success\":-1,\"rowKey\":\"3\"}', 1, NULL, 3, 1);
INSERT INTO `t_train_question` VALUES (1881169669390909445, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"判断题\",\"title\":\"这是一道判断题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":5,\"level\":3,\"type\":3,\"share\":1,\"id\":\"1844631640342097921\",\"success\":-1,\"rowKey\":\"4\"}', 1, NULL, 4, 1);
INSERT INTO `t_train_question` VALUES (1881169669390909446, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"填空题\",\"title\":\"这是(__)一道填(__)空题(__)。\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"2\",\"title\":\"第三个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5},{\"rowKey\":\"2\",\"title\":\"第三个填空\",\"value\":\"答案三\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":4,\"type\":4,\"share\":1,\"id\":\"1844631905887234566\",\"success\":-1,\"rowKey\":\"5\"}', 1, NULL, 5, 1);
INSERT INTO `t_train_question` VALUES (1881169669390909447, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"填空题\",\"title\":\"这是(__)一道(__)填空题。\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":4,\"type\":4,\"share\":1,\"id\":\"1844631905887678466\",\"success\":-1,\"rowKey\":\"6\"}', 1, NULL, 6, 1);
INSERT INTO `t_train_question` VALUES (1881169669390909448, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"解答题\",\"title\":\"这是一道简答题，对此你有什么理解？这是一道简答题，对此你有什么理解？\",\"score\":15,\"level\":5,\"type\":5,\"share\":1,\"success\":-1,\"keyWords\":[\"1111\",\"2222\"],\"matchNum\":2,\"id\":\"1846925581972267010\",\"rowKey\":\"7\"}', 1, NULL, 7, 1);
INSERT INTO `t_train_question` VALUES (1881169669390909449, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一个测试_单选题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":10,\"level\":2,\"type\":2,\"share\":1,\"success\":-1,\"rowKey\":\"8\"}', 1, NULL, 8, 1);
INSERT INTO `t_train_question` VALUES (1881169669390909450, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一个测试_多选题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1},{\"rowKey\":\"3\",\"title\":\"D\",\"value\":\"选项四\",\"selected\":0,\"sort\":1}],\"answer\":[\"B\",\"C\"],\"score\":10,\"level\":2,\"type\":2,\"share\":1,\"success\":-1,\"rowKey\":\"9\"}', 1, NULL, 9, 1);
INSERT INTO `t_train_question` VALUES (1881169669390909451, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一个测试_判断题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"level\":2,\"type\":3,\"share\":1,\"success\":-1,\"rowKey\":\"10\"}', 1, NULL, 10, 1);
INSERT INTO `t_train_question` VALUES (1881169669390909452, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一(__)个测试_填空题(__)\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":2,\"type\":4,\"share\":1,\"success\":-1,\"id\":\"1872568030660759553\",\"rowKey\":\"11\"}', 1, NULL, 11, 1);
INSERT INTO `t_train_question` VALUES (1881169669390909453, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一个测试_问答题？\",\"score\":15,\"level\":2,\"type\":5,\"share\":1,\"success\":-1,\"keyWords\":[\"你好\",\"中国\",\"我爱你\"],\"matchNum\":1,\"rowKey\":\"12\"}', 1, NULL, 12, 1);
INSERT INTO `t_train_question` VALUES (1881169669390909454, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目1\",\"title\":\"这是一个导入的单选题目1吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":1,\"success\":-1,\"share\":1,\"rowKey\":\"13\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3}],\"answer\":\"A\",\"scoreBySuccess\":0}', 1, NULL, 13, 1);
INSERT INTO `t_train_question` VALUES (1881169669390909455, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入的单选题目2吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":1,\"success\":-1,\"share\":1,\"rowKey\":\"14\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3}],\"answer\":\"B\",\"scoreBySuccess\":0}', 1, NULL, 14, 1);
INSERT INTO `t_train_question` VALUES (1881169669390909456, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目1\",\"title\":\"这是一个导入的多选题目1吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":2,\"success\":-1,\"share\":1,\"rowKey\":\"15\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3},{\"title\":\"D\",\"value\":\"选项4\",\"selected\":0,\"sort\":4}],\"answer\":[\"A\",\"B\"],\"scoreBySuccess\":0}', 1, NULL, 15, 1);
INSERT INTO `t_train_question` VALUES (1881169669424463874, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入的多选题目2吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":2,\"success\":-1,\"share\":1,\"rowKey\":\"16\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3},{\"title\":\"D\",\"value\":\"选项4\",\"selected\":0,\"sort\":4}],\"answer\":[\"B\",\"C\"],\"scoreBySuccess\":0}', 1, NULL, 16, 1);
INSERT INTO `t_train_question` VALUES (1881169669424463875, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目1\",\"title\":\"这是一个导入的判断题目1吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":3,\"success\":-1,\"share\":1,\"rowKey\":\"17\",\"option\":[{\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":2}],\"answer\":\"A\",\"scoreBySuccess\":0}', 1, NULL, 17, 1);
INSERT INTO `t_train_question` VALUES (1881169669424463876, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934222557186\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入的判断题目2吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":3,\"success\":-1,\"share\":1,\"rowKey\":\"18\",\"option\":[{\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":2}],\"answer\":\"A\",\"scoreBySuccess\":0}', 1, NULL, 18, 1);
INSERT INTO `t_train_question` VALUES (1881169669424463877, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入(__)的填空题目吗。\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":4,\"success\":-1,\"share\":1,\"rowKey\":\"19\",\"option\":[{\"title\":\"第一个填空\",\"value\":\"\",\"selected\":0,\"sort\":1}],\"answer\":[{\"title\":\"第一个填空\",\"value\":\"这是第1个答案\",\"selected\":0,\"sort\":1}],\"scoreBySuccess\":0}', 1, NULL, 19, 1);
INSERT INTO `t_train_question` VALUES (1881277708504772609, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"analysis\":\"这是一个解析。\",\"level\":1,\"type\":1,\"share\":1,\"success\":-1,\"id\":\"1844589207403139073\",\"rowKey\":\"0\"}', 1, NULL, 0, 1);
INSERT INTO `t_train_question` VALUES (1881277708504772610, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一选项一选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":5,\"level\":1,\"type\":1,\"share\":0,\"success\":-1,\"id\":\"1844589234568139073\",\"rowKey\":\"1\"}', 1, NULL, 1, 1);
INSERT INTO `t_train_question` VALUES (1881277708504772611, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一道多选题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1},{\"rowKey\":\"3\",\"title\":\"D\",\"value\":\"选项四\",\"selected\":0,\"sort\":1}],\"answer\":[\"B\",\"C\"],\"score\":10,\"level\":2,\"type\":2,\"share\":1,\"success\":-1,\"id\":\"1844631436142407682\",\"rowKey\":\"2\"}', 1, NULL, 2, 1);
INSERT INTO `t_train_question` VALUES (1881277708504772612, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"判断题\",\"title\":\"这是一道判断题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"level\":3,\"type\":3,\"share\":1,\"id\":\"1844631640312345921\",\"success\":-1,\"rowKey\":\"3\"}', 1, NULL, 3, 1);
INSERT INTO `t_train_question` VALUES (1881277708504772613, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"判断题\",\"title\":\"这是一道判断题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":5,\"level\":3,\"type\":3,\"share\":1,\"id\":\"1844631640342097921\",\"success\":-1,\"rowKey\":\"4\"}', 1, NULL, 4, 1);
INSERT INTO `t_train_question` VALUES (1881277708504772614, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"填空题\",\"title\":\"这是(__)一道填(__)空题(__)。\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"2\",\"title\":\"第三个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5},{\"rowKey\":\"2\",\"title\":\"第三个填空\",\"value\":\"答案三\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":4,\"type\":4,\"share\":1,\"id\":\"1844631905887234566\",\"success\":-1,\"rowKey\":\"5\"}', 1, NULL, 5, 1);
INSERT INTO `t_train_question` VALUES (1881277708504772615, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"填空题\",\"title\":\"这是(__)一道(__)填空题。\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":4,\"type\":4,\"share\":1,\"id\":\"1844631905887678466\",\"success\":-1,\"rowKey\":\"6\"}', 1, NULL, 6, 1);
INSERT INTO `t_train_question` VALUES (1881277708504772616, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"解答题\",\"title\":\"这是一道简答题，对此你有什么理解？这是一道简答题，对此你有什么理解？\",\"score\":15,\"level\":5,\"type\":5,\"share\":1,\"success\":-1,\"keyWords\":[\"1111\",\"2222\"],\"matchNum\":2,\"id\":\"1846925581972267010\",\"rowKey\":\"7\"}', 1, NULL, 7, 1);
INSERT INTO `t_train_question` VALUES (1881277708504772617, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一个测试_单选题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":10,\"level\":2,\"type\":2,\"share\":1,\"success\":-1,\"rowKey\":\"8\"}', 1, NULL, 8, 1);
INSERT INTO `t_train_question` VALUES (1881277708504772618, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一个测试_多选题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1},{\"rowKey\":\"3\",\"title\":\"D\",\"value\":\"选项四\",\"selected\":0,\"sort\":1}],\"answer\":[\"B\",\"C\"],\"score\":10,\"level\":2,\"type\":2,\"share\":1,\"success\":-1,\"rowKey\":\"9\"}', 1, NULL, 9, 1);
INSERT INTO `t_train_question` VALUES (1881277708504772619, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一个测试_判断题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"level\":2,\"type\":3,\"share\":1,\"success\":-1,\"rowKey\":\"10\"}', 1, NULL, 10, 1);
INSERT INTO `t_train_question` VALUES (1881277708504772620, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一(__)个测试_填空题(__)\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":2,\"type\":4,\"share\":1,\"success\":-1,\"id\":\"1872568030660759553\",\"rowKey\":\"11\"}', 1, NULL, 11, 1);
INSERT INTO `t_train_question` VALUES (1881277708504772621, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一个测试_问答题？\",\"score\":15,\"level\":2,\"type\":5,\"share\":1,\"success\":-1,\"keyWords\":[\"你好\",\"中国\",\"我爱你\"],\"matchNum\":1,\"rowKey\":\"12\"}', 1, NULL, 12, 1);
INSERT INTO `t_train_question` VALUES (1881277708504772622, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目1\",\"title\":\"这是一个导入的单选题目1吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":1,\"success\":-1,\"share\":1,\"rowKey\":\"13\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3}],\"answer\":\"A\",\"scoreBySuccess\":0}', 1, NULL, 13, 1);
INSERT INTO `t_train_question` VALUES (1881277708504772623, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入的单选题目2吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":1,\"success\":-1,\"share\":1,\"rowKey\":\"14\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3}],\"answer\":\"B\",\"scoreBySuccess\":0}', 1, NULL, 14, 1);
INSERT INTO `t_train_question` VALUES (1881277708504772624, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目1\",\"title\":\"这是一个导入的多选题目1吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":2,\"success\":-1,\"share\":1,\"rowKey\":\"15\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3},{\"title\":\"D\",\"value\":\"选项4\",\"selected\":0,\"sort\":4}],\"answer\":[\"A\",\"B\"],\"scoreBySuccess\":0}', 1, NULL, 15, 1);
INSERT INTO `t_train_question` VALUES (1881277708504772625, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入的多选题目2吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":2,\"success\":-1,\"share\":1,\"rowKey\":\"16\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3},{\"title\":\"D\",\"value\":\"选项4\",\"selected\":0,\"sort\":4}],\"answer\":[\"B\",\"C\"],\"scoreBySuccess\":0}', 1, NULL, 16, 1);
INSERT INTO `t_train_question` VALUES (1881277708504772626, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目1\",\"title\":\"这是一个导入的判断题目1吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":3,\"success\":-1,\"share\":1,\"rowKey\":\"17\",\"option\":[{\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":2}],\"answer\":\"A\",\"scoreBySuccess\":0}', 1, NULL, 17, 1);
INSERT INTO `t_train_question` VALUES (1881277708504772627, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934222557186\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入的判断题目2吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":3,\"success\":-1,\"share\":1,\"rowKey\":\"18\",\"option\":[{\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":2}],\"answer\":\"A\",\"scoreBySuccess\":0}', 1, NULL, 18, 1);
INSERT INTO `t_train_question` VALUES (1881277708504772628, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入(__)的填空题目吗。\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":4,\"success\":-1,\"share\":1,\"rowKey\":\"19\",\"option\":[{\"title\":\"第一个填空\",\"value\":\"\",\"selected\":0,\"sort\":1}],\"answer\":[{\"title\":\"第一个填空\",\"value\":\"这是第1个答案\",\"selected\":0,\"sort\":1}],\"scoreBySuccess\":0}', 1, NULL, 19, 1);
INSERT INTO `t_train_question` VALUES (1881961277745025025, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"analysis\":\"这是一个解析。\",\"level\":1,\"type\":1,\"share\":1,\"success\":-1,\"id\":\"1844589207403139073\",\"rowKey\":\"0\"}', 1, NULL, 0, 1);
INSERT INTO `t_train_question` VALUES (1881961277753413634, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一选项一选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":5,\"level\":1,\"type\":1,\"share\":0,\"success\":-1,\"id\":\"1844589234568139073\",\"rowKey\":\"1\"}', 1, NULL, 1, 1);
INSERT INTO `t_train_question` VALUES (1881961277753413635, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一道多选题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1},{\"rowKey\":\"3\",\"title\":\"D\",\"value\":\"选项四\",\"selected\":0,\"sort\":1}],\"answer\":[\"B\",\"C\"],\"score\":10,\"level\":2,\"type\":2,\"share\":1,\"success\":-1,\"id\":\"1844631436142407682\",\"rowKey\":\"2\"}', 1, NULL, 2, 1);
INSERT INTO `t_train_question` VALUES (1881961277753413636, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"判断题\",\"title\":\"这是一道判断题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"level\":3,\"type\":3,\"share\":1,\"id\":\"1844631640312345921\",\"success\":-1,\"rowKey\":\"3\"}', 1, NULL, 3, 1);
INSERT INTO `t_train_question` VALUES (1881961277753413637, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"判断题\",\"title\":\"这是一道判断题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":5,\"level\":3,\"type\":3,\"share\":1,\"id\":\"1844631640342097921\",\"success\":-1,\"rowKey\":\"4\"}', 1, NULL, 4, 1);
INSERT INTO `t_train_question` VALUES (1881961277753413638, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"填空题\",\"title\":\"这是(__)一道填(__)空题(__)。\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"2\",\"title\":\"第三个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5},{\"rowKey\":\"2\",\"title\":\"第三个填空\",\"value\":\"答案三\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":4,\"type\":4,\"share\":1,\"id\":\"1844631905887234566\",\"success\":-1,\"rowKey\":\"5\"}', 1, NULL, 5, 1);
INSERT INTO `t_train_question` VALUES (1881961277753413639, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"填空题\",\"title\":\"这是(__)一道(__)填空题。\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":4,\"type\":4,\"share\":1,\"id\":\"1844631905887678466\",\"success\":-1,\"rowKey\":\"6\"}', 1, NULL, 6, 1);
INSERT INTO `t_train_question` VALUES (1881961277753413640, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"解答题\",\"title\":\"这是一道简答题，对此你有什么理解？这是一道简答题，对此你有什么理解？\",\"score\":15,\"level\":5,\"type\":5,\"share\":1,\"success\":-1,\"keyWords\":[\"1111\",\"2222\"],\"matchNum\":2,\"id\":\"1846925581972267010\",\"rowKey\":\"7\"}', 1, NULL, 7, 1);
INSERT INTO `t_train_question` VALUES (1881961277753413641, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一个测试_单选题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":10,\"level\":2,\"type\":2,\"share\":1,\"success\":-1,\"rowKey\":\"8\"}', 1, NULL, 8, 1);
INSERT INTO `t_train_question` VALUES (1881961277753413642, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一个测试_多选题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1},{\"rowKey\":\"3\",\"title\":\"D\",\"value\":\"选项四\",\"selected\":0,\"sort\":1}],\"answer\":[\"B\",\"C\"],\"score\":10,\"level\":2,\"type\":2,\"share\":1,\"success\":-1,\"rowKey\":\"9\"}', 1, NULL, 9, 1);
INSERT INTO `t_train_question` VALUES (1881961277753413643, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一个测试_判断题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"level\":2,\"type\":3,\"share\":1,\"success\":-1,\"rowKey\":\"10\"}', 1, NULL, 10, 1);
INSERT INTO `t_train_question` VALUES (1881961277753413644, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一(__)个测试_填空题(__)\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":2,\"type\":4,\"share\":1,\"success\":-1,\"id\":\"1872568030660759553\",\"rowKey\":\"11\"}', 1, NULL, 11, 1);
INSERT INTO `t_train_question` VALUES (1881961277753413645, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一个测试_问答题？\",\"score\":15,\"level\":2,\"type\":5,\"share\":1,\"success\":-1,\"keyWords\":[\"你好\",\"中国\",\"我爱你\"],\"matchNum\":1,\"rowKey\":\"12\"}', 1, NULL, 12, 1);
INSERT INTO `t_train_question` VALUES (1881961277753413646, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目1\",\"title\":\"这是一个导入的单选题目1吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":1,\"success\":-1,\"share\":1,\"rowKey\":\"13\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3}],\"answer\":\"A\",\"scoreBySuccess\":0}', 1, NULL, 13, 1);
INSERT INTO `t_train_question` VALUES (1881961277753413647, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入的单选题目2吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":1,\"success\":-1,\"share\":1,\"rowKey\":\"14\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3}],\"answer\":\"B\",\"scoreBySuccess\":0}', 1, NULL, 14, 1);
INSERT INTO `t_train_question` VALUES (1881961277753413648, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目1\",\"title\":\"这是一个导入的多选题目1吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":2,\"success\":-1,\"share\":1,\"rowKey\":\"15\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3},{\"title\":\"D\",\"value\":\"选项4\",\"selected\":0,\"sort\":4}],\"answer\":[\"A\",\"B\"],\"scoreBySuccess\":0}', 1, NULL, 15, 1);
INSERT INTO `t_train_question` VALUES (1881961277753413649, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入的多选题目2吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":2,\"success\":-1,\"share\":1,\"rowKey\":\"16\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3},{\"title\":\"D\",\"value\":\"选项4\",\"selected\":0,\"sort\":4}],\"answer\":[\"B\",\"C\"],\"scoreBySuccess\":0}', 1, NULL, 16, 1);
INSERT INTO `t_train_question` VALUES (1881961277753413650, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目1\",\"title\":\"这是一个导入的判断题目1吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":3,\"success\":-1,\"share\":1,\"rowKey\":\"17\",\"option\":[{\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":2}],\"answer\":\"A\",\"scoreBySuccess\":0}', 1, NULL, 17, 1);
INSERT INTO `t_train_question` VALUES (1881961277753413651, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934222557186\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入的判断题目2吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":3,\"success\":-1,\"share\":1,\"rowKey\":\"18\",\"option\":[{\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":2}],\"answer\":\"A\",\"scoreBySuccess\":0}', 1, NULL, 18, 1);
INSERT INTO `t_train_question` VALUES (1881961277753413652, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入(__)的填空题目吗。\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":4,\"success\":-1,\"share\":1,\"rowKey\":\"19\",\"option\":[{\"title\":\"第一个填空\",\"value\":\"\",\"selected\":0,\"sort\":1}],\"answer\":[{\"title\":\"第一个填空\",\"value\":\"这是第1个答案\",\"selected\":0,\"sort\":1}],\"scoreBySuccess\":0}', 1, NULL, 19, 1);
INSERT INTO `t_train_question` VALUES (1881991436191756289, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"analysis\":\"这是一个解析。\",\"level\":1,\"type\":1,\"share\":1,\"success\":-1,\"id\":\"1844589207403139073\",\"rowKey\":\"0\"}', 1, NULL, 0, 1);
INSERT INTO `t_train_question` VALUES (1881991436191756290, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一选项一选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":5,\"level\":1,\"type\":1,\"share\":0,\"success\":-1,\"id\":\"1844589234568139073\",\"rowKey\":\"1\"}', 1, NULL, 1, 1);
INSERT INTO `t_train_question` VALUES (1881991436191756291, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一道多选题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1},{\"rowKey\":\"3\",\"title\":\"D\",\"value\":\"选项四\",\"selected\":0,\"sort\":1}],\"answer\":[\"B\",\"C\"],\"score\":10,\"level\":2,\"type\":2,\"share\":1,\"success\":-1,\"id\":\"1844631436142407682\",\"rowKey\":\"2\"}', 1, NULL, 2, 1);
INSERT INTO `t_train_question` VALUES (1881991436191756292, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"判断题\",\"title\":\"这是一道判断题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"level\":3,\"type\":3,\"share\":1,\"id\":\"1844631640312345921\",\"success\":-1,\"rowKey\":\"3\"}', 1, NULL, 3, 1);
INSERT INTO `t_train_question` VALUES (1881991436191756293, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"判断题\",\"title\":\"这是一道判断题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":5,\"level\":3,\"type\":3,\"share\":1,\"id\":\"1844631640342097921\",\"success\":-1,\"rowKey\":\"4\"}', 1, NULL, 4, 1);
INSERT INTO `t_train_question` VALUES (1881991436191756294, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"填空题\",\"title\":\"这是(__)一道填(__)空题(__)。\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"2\",\"title\":\"第三个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5},{\"rowKey\":\"2\",\"title\":\"第三个填空\",\"value\":\"答案三\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":4,\"type\":4,\"share\":1,\"id\":\"1844631905887234566\",\"success\":-1,\"rowKey\":\"5\"}', 1, NULL, 5, 1);
INSERT INTO `t_train_question` VALUES (1881991436191756295, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"填空题\",\"title\":\"这是(__)一道(__)填空题。\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":4,\"type\":4,\"share\":1,\"id\":\"1844631905887678466\",\"success\":-1,\"rowKey\":\"6\"}', 1, NULL, 6, 1);
INSERT INTO `t_train_question` VALUES (1881991436191756296, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"解答题\",\"title\":\"这是一道简答题，对此你有什么理解？这是一道简答题，对此你有什么理解？\",\"score\":15,\"level\":5,\"type\":5,\"share\":1,\"success\":-1,\"keyWords\":[\"1111\",\"2222\"],\"matchNum\":2,\"id\":\"1846925581972267010\",\"rowKey\":\"7\"}', 1, NULL, 7, 1);
INSERT INTO `t_train_question` VALUES (1881991436191756297, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一个测试_单选题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":10,\"level\":2,\"type\":2,\"share\":1,\"success\":-1,\"rowKey\":\"8\"}', 1, NULL, 8, 1);
INSERT INTO `t_train_question` VALUES (1881991436191756298, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一个测试_多选题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1},{\"rowKey\":\"3\",\"title\":\"D\",\"value\":\"选项四\",\"selected\":0,\"sort\":1}],\"answer\":[\"B\",\"C\"],\"score\":10,\"level\":2,\"type\":2,\"share\":1,\"success\":-1,\"rowKey\":\"9\"}', 1, NULL, 9, 1);
INSERT INTO `t_train_question` VALUES (1881991436191756299, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一个测试_判断题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"level\":2,\"type\":3,\"share\":1,\"success\":-1,\"rowKey\":\"10\"}', 1, NULL, 10, 1);
INSERT INTO `t_train_question` VALUES (1881991436191756300, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一(__)个测试_填空题(__)\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":2,\"type\":4,\"share\":1,\"success\":-1,\"id\":\"1872568030660759553\",\"rowKey\":\"11\"}', 1, NULL, 11, 1);
INSERT INTO `t_train_question` VALUES (1881991436191756301, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一个测试_问答题？\",\"score\":15,\"level\":2,\"type\":5,\"share\":1,\"success\":-1,\"keyWords\":[\"你好\",\"中国\",\"我爱你\"],\"matchNum\":1,\"rowKey\":\"12\"}', 1, NULL, 12, 1);
INSERT INTO `t_train_question` VALUES (1881991436191756302, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目1\",\"title\":\"这是一个导入的单选题目1吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":1,\"success\":-1,\"share\":1,\"rowKey\":\"13\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3}],\"answer\":\"A\",\"scoreBySuccess\":0}', 1, NULL, 13, 1);
INSERT INTO `t_train_question` VALUES (1881991436191756303, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入的单选题目2吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":1,\"success\":-1,\"share\":1,\"rowKey\":\"14\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3}],\"answer\":\"B\",\"scoreBySuccess\":0}', 1, NULL, 14, 1);
INSERT INTO `t_train_question` VALUES (1881991436191756304, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目1\",\"title\":\"这是一个导入的多选题目1吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":2,\"success\":-1,\"share\":1,\"rowKey\":\"15\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3},{\"title\":\"D\",\"value\":\"选项4\",\"selected\":0,\"sort\":4}],\"answer\":[\"A\",\"B\"],\"scoreBySuccess\":0}', 1, NULL, 15, 1);
INSERT INTO `t_train_question` VALUES (1881991436191756305, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入的多选题目2吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":2,\"success\":-1,\"share\":1,\"rowKey\":\"16\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3},{\"title\":\"D\",\"value\":\"选项4\",\"selected\":0,\"sort\":4}],\"answer\":[\"B\",\"C\"],\"scoreBySuccess\":0}', 1, NULL, 16, 1);
INSERT INTO `t_train_question` VALUES (1881991436191756306, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目1\",\"title\":\"这是一个导入的判断题目1吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":3,\"success\":-1,\"share\":1,\"rowKey\":\"17\",\"option\":[{\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":2}],\"answer\":\"A\",\"scoreBySuccess\":0}', 1, NULL, 17, 1);
INSERT INTO `t_train_question` VALUES (1881991436191756307, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934222557186\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入的判断题目2吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":3,\"success\":-1,\"share\":1,\"rowKey\":\"18\",\"option\":[{\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":2}],\"answer\":\"A\",\"scoreBySuccess\":0}', 1, NULL, 18, 1);
INSERT INTO `t_train_question` VALUES (1881991436191756308, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入(__)的填空题目吗。\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":4,\"success\":-1,\"share\":1,\"rowKey\":\"19\",\"option\":[{\"title\":\"第一个填空\",\"value\":\"\",\"selected\":0,\"sort\":1}],\"answer\":[{\"title\":\"第一个填空\",\"value\":\"这是第1个答案\",\"selected\":0,\"sort\":1}],\"scoreBySuccess\":0}', 1, NULL, 19, 1);
INSERT INTO `t_train_question` VALUES (1882084371621011457, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"analysis\":\"这是一个解析。\",\"level\":1,\"type\":1,\"share\":1,\"success\":-1,\"id\":\"1844589207403139073\",\"rowKey\":\"0\"}', 1, NULL, 0, 1);
INSERT INTO `t_train_question` VALUES (1882084371621011458, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一选项一选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":5,\"level\":1,\"type\":1,\"share\":0,\"success\":-1,\"id\":\"1844589234568139073\",\"rowKey\":\"1\"}', 1, NULL, 1, 1);
INSERT INTO `t_train_question` VALUES (1882084371621011459, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一道多选题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1},{\"rowKey\":\"3\",\"title\":\"D\",\"value\":\"选项四\",\"selected\":0,\"sort\":1}],\"answer\":[\"B\",\"C\"],\"score\":10,\"level\":2,\"type\":2,\"share\":1,\"success\":-1,\"id\":\"1844631436142407682\",\"rowKey\":\"2\"}', 1, NULL, 2, 1);
INSERT INTO `t_train_question` VALUES (1882084371621011460, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"判断题\",\"title\":\"这是一道判断题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"level\":3,\"type\":3,\"share\":1,\"id\":\"1844631640312345921\",\"success\":-1,\"rowKey\":\"3\"}', 1, NULL, 3, 1);
INSERT INTO `t_train_question` VALUES (1882084371621011461, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"判断题\",\"title\":\"这是一道判断题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":5,\"level\":3,\"type\":3,\"share\":1,\"id\":\"1844631640342097921\",\"success\":-1,\"rowKey\":\"4\"}', 1, NULL, 4, 1);
INSERT INTO `t_train_question` VALUES (1882084371621011462, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"填空题\",\"title\":\"这是(__)一道填(__)空题(__)。\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"2\",\"title\":\"第三个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5},{\"rowKey\":\"2\",\"title\":\"第三个填空\",\"value\":\"答案三\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":4,\"type\":4,\"share\":1,\"id\":\"1844631905887234566\",\"success\":-1,\"rowKey\":\"5\"}', 1, NULL, 5, 1);
INSERT INTO `t_train_question` VALUES (1882084371621011463, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"填空题\",\"title\":\"这是(__)一道(__)填空题。\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":4,\"type\":4,\"share\":1,\"id\":\"1844631905887678466\",\"success\":-1,\"rowKey\":\"6\"}', 1, NULL, 6, 1);
INSERT INTO `t_train_question` VALUES (1882084371621011464, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"解答题\",\"title\":\"这是一道简答题，对此你有什么理解？这是一道简答题，对此你有什么理解？\",\"score\":15,\"level\":5,\"type\":5,\"share\":1,\"success\":-1,\"keyWords\":[\"1111\",\"2222\"],\"matchNum\":2,\"id\":\"1846925581972267010\",\"rowKey\":\"7\"}', 1, NULL, 7, 1);
INSERT INTO `t_train_question` VALUES (1882084371621011465, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一个测试_单选题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":10,\"level\":2,\"type\":2,\"share\":1,\"success\":-1,\"rowKey\":\"8\"}', 1, NULL, 8, 1);
INSERT INTO `t_train_question` VALUES (1882084371621011466, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一个测试_多选题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1},{\"rowKey\":\"3\",\"title\":\"D\",\"value\":\"选项四\",\"selected\":0,\"sort\":1}],\"answer\":[\"B\",\"C\"],\"score\":10,\"level\":2,\"type\":2,\"share\":1,\"success\":-1,\"rowKey\":\"9\"}', 1, NULL, 9, 1);
INSERT INTO `t_train_question` VALUES (1882084371621011467, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一个测试_判断题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"level\":2,\"type\":3,\"share\":1,\"success\":-1,\"rowKey\":\"10\"}', 1, NULL, 10, 1);
INSERT INTO `t_train_question` VALUES (1882084371621011468, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一(__)个测试_填空题(__)\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":2,\"type\":4,\"share\":1,\"success\":-1,\"id\":\"1872568030660759553\",\"rowKey\":\"11\"}', 1, NULL, 11, 1);
INSERT INTO `t_train_question` VALUES (1882084371621011469, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一个测试_问答题？\",\"score\":15,\"level\":2,\"type\":5,\"share\":1,\"success\":-1,\"keyWords\":[\"你好\",\"中国\",\"我爱你\"],\"matchNum\":1,\"rowKey\":\"12\"}', 1, NULL, 12, 1);
INSERT INTO `t_train_question` VALUES (1882084371621011470, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目1\",\"title\":\"这是一个导入的单选题目1吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":1,\"success\":-1,\"share\":1,\"rowKey\":\"13\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3}],\"answer\":\"A\",\"scoreBySuccess\":0}', 1, NULL, 13, 1);
INSERT INTO `t_train_question` VALUES (1882084371621011471, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入的单选题目2吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":1,\"success\":-1,\"share\":1,\"rowKey\":\"14\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3}],\"answer\":\"B\",\"scoreBySuccess\":0}', 1, NULL, 14, 1);
INSERT INTO `t_train_question` VALUES (1882084371621011472, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目1\",\"title\":\"这是一个导入的多选题目1吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":2,\"success\":-1,\"share\":1,\"rowKey\":\"15\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3},{\"title\":\"D\",\"value\":\"选项4\",\"selected\":0,\"sort\":4}],\"answer\":[\"A\",\"B\"],\"scoreBySuccess\":0}', 1, NULL, 15, 1);
INSERT INTO `t_train_question` VALUES (1882084371621011473, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入的多选题目2吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":2,\"success\":-1,\"share\":1,\"rowKey\":\"16\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3},{\"title\":\"D\",\"value\":\"选项4\",\"selected\":0,\"sort\":4}],\"answer\":[\"B\",\"C\"],\"scoreBySuccess\":0}', 1, NULL, 16, 1);
INSERT INTO `t_train_question` VALUES (1882084371621011474, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目1\",\"title\":\"这是一个导入的判断题目1吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":3,\"success\":-1,\"share\":1,\"rowKey\":\"17\",\"option\":[{\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":2}],\"answer\":\"A\",\"scoreBySuccess\":0}', 1, NULL, 17, 1);
INSERT INTO `t_train_question` VALUES (1882084371621011475, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934222557186\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入的判断题目2吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":3,\"success\":-1,\"share\":1,\"rowKey\":\"18\",\"option\":[{\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":2}],\"answer\":\"A\",\"scoreBySuccess\":0}', 1, NULL, 18, 1);
INSERT INTO `t_train_question` VALUES (1882084371621011476, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入(__)的填空题目吗。\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":4,\"success\":-1,\"share\":1,\"rowKey\":\"19\",\"option\":[{\"title\":\"第一个填空\",\"value\":\"\",\"selected\":0,\"sort\":1}],\"answer\":[{\"title\":\"第一个填空\",\"value\":\"这是第1个答案\",\"selected\":0,\"sort\":1}],\"scoreBySuccess\":0}', 1, NULL, 19, 1);
INSERT INTO `t_train_question` VALUES (1882092944979779585, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"analysis\":\"这是一个解析。\",\"level\":1,\"type\":1,\"share\":1,\"success\":-1,\"id\":\"1844589207403139073\",\"rowKey\":\"0\"}', 1, NULL, 0, 0);
INSERT INTO `t_train_question` VALUES (1882092944979779586, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一个单选测试？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一选项一选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":5,\"level\":1,\"type\":1,\"share\":0,\"success\":-1,\"id\":\"1844589234568139073\",\"rowKey\":\"1\"}', 1, NULL, 1, 0);
INSERT INTO `t_train_question` VALUES (1882092944979779587, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"单选题\",\"title\":\"这是一道多选题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1},{\"rowKey\":\"3\",\"title\":\"D\",\"value\":\"选项四\",\"selected\":0,\"sort\":1}],\"answer\":[\"B\",\"C\"],\"score\":10,\"level\":2,\"type\":2,\"share\":1,\"success\":-1,\"id\":\"1844631436142407682\",\"rowKey\":\"2\"}', 1, NULL, 2, 0);
INSERT INTO `t_train_question` VALUES (1882092944979779588, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"判断题\",\"title\":\"这是一道判断题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"level\":3,\"type\":3,\"share\":1,\"id\":\"1844631640312345921\",\"success\":-1,\"rowKey\":\"3\"}', 1, NULL, 3, 0);
INSERT INTO `t_train_question` VALUES (1882092944979779589, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"判断题\",\"title\":\"这是一道判断题？\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"B\",\"score\":5,\"level\":3,\"type\":3,\"share\":1,\"id\":\"1844631640342097921\",\"success\":-1,\"rowKey\":\"4\"}', 1, NULL, 4, 0);
INSERT INTO `t_train_question` VALUES (1882092945046888449, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"填空题\",\"title\":\"这是(__)一道填(__)空题(__)。\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"2\",\"title\":\"第三个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5},{\"rowKey\":\"2\",\"title\":\"第三个填空\",\"value\":\"答案三\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":4,\"type\":4,\"share\":1,\"id\":\"1844631905887234566\",\"success\":-1,\"rowKey\":\"5\"}', 1, NULL, 5, 0);
INSERT INTO `t_train_question` VALUES (1882092945046888450, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"填空题\",\"title\":\"这是(__)一道(__)填空题。\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":4,\"type\":4,\"share\":1,\"id\":\"1844631905887678466\",\"success\":-1,\"rowKey\":\"6\"}', 1, NULL, 6, 0);
INSERT INTO `t_train_question` VALUES (1882092945046888451, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"name\":\"解答题\",\"title\":\"这是一道简答题，对此你有什么理解？这是一道简答题，对此你有什么理解？\",\"score\":15,\"level\":5,\"type\":5,\"share\":1,\"success\":-1,\"keyWords\":[\"1111\",\"2222\"],\"matchNum\":2,\"id\":\"1846925581972267010\",\"rowKey\":\"7\"}', 1, NULL, 7, 0);
INSERT INTO `t_train_question` VALUES (1882092945046888452, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一个测试_单选题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":10,\"level\":2,\"type\":2,\"share\":1,\"success\":-1,\"rowKey\":\"8\"}', 1, NULL, 8, 0);
INSERT INTO `t_train_question` VALUES (1882092945046888453, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一个测试_多选题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"选项一\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"选项二\",\"selected\":0,\"sort\":1},{\"rowKey\":\"2\",\"title\":\"C\",\"value\":\"选项三\",\"selected\":0,\"sort\":1},{\"rowKey\":\"3\",\"title\":\"D\",\"value\":\"选项四\",\"selected\":0,\"sort\":1}],\"answer\":[\"B\",\"C\"],\"score\":10,\"level\":2,\"type\":2,\"share\":1,\"success\":-1,\"rowKey\":\"9\"}', 1, NULL, 9, 0);
INSERT INTO `t_train_question` VALUES (1882092945046888454, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一个测试_判断题\",\"option\":[{\"rowKey\":\"0\",\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"rowKey\":\"1\",\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":1}],\"answer\":\"A\",\"score\":5,\"level\":2,\"type\":3,\"share\":1,\"success\":-1,\"rowKey\":\"10\"}', 1, NULL, 10, 0);
INSERT INTO `t_train_question` VALUES (1882092945046888455, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一(__)个测试_填空题(__)\",\"option\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"selected\":0,\"sort\":5}],\"answer\":[{\"rowKey\":\"0\",\"title\":\"第一个填空\",\"value\":\"答案一\",\"selected\":0,\"sort\":5},{\"rowKey\":\"1\",\"title\":\"第二个填空\",\"value\":\"答案二\",\"selected\":0,\"sort\":5}],\"score\":5,\"level\":2,\"type\":4,\"share\":1,\"success\":-1,\"id\":\"1872568030660759553\",\"rowKey\":\"11\"}', 1, NULL, 11, 0);
INSERT INTO `t_train_question` VALUES (1882092945046888456, 1881003994613665794, NULL, '{\"classifyId\":\"1844562179480195074\",\"title\":\"测试一个测试_问答题？\",\"score\":15,\"level\":2,\"type\":5,\"share\":1,\"success\":-1,\"keyWords\":[\"你好\",\"中国\",\"我爱你\"],\"matchNum\":1,\"rowKey\":\"12\"}', 1, NULL, 12, 0);
INSERT INTO `t_train_question` VALUES (1882092945046888457, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目1\",\"title\":\"这是一个导入的单选题目1吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":1,\"success\":-1,\"share\":1,\"rowKey\":\"13\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3}],\"answer\":\"A\",\"scoreBySuccess\":0}', 1, NULL, 13, 0);
INSERT INTO `t_train_question` VALUES (1882092945046888458, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入的单选题目2吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":1,\"success\":-1,\"share\":1,\"rowKey\":\"14\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3}],\"answer\":\"B\",\"scoreBySuccess\":0}', 1, NULL, 14, 0);
INSERT INTO `t_train_question` VALUES (1882092945046888459, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目1\",\"title\":\"这是一个导入的多选题目1吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":2,\"success\":-1,\"share\":1,\"rowKey\":\"15\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3},{\"title\":\"D\",\"value\":\"选项4\",\"selected\":0,\"sort\":4}],\"answer\":[\"A\",\"B\"],\"scoreBySuccess\":0}', 1, NULL, 15, 0);
INSERT INTO `t_train_question` VALUES (1882092945046888460, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入的多选题目2吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":2,\"success\":-1,\"share\":1,\"rowKey\":\"16\",\"option\":[{\"title\":\"A\",\"value\":\"选项1\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"选项2\",\"selected\":0,\"sort\":2},{\"title\":\"C\",\"value\":\"选项3\",\"selected\":0,\"sort\":3},{\"title\":\"D\",\"value\":\"选项4\",\"selected\":0,\"sort\":4}],\"answer\":[\"B\",\"C\"],\"scoreBySuccess\":0}', 1, NULL, 16, 0);
INSERT INTO `t_train_question` VALUES (1882092945046888461, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目1\",\"title\":\"这是一个导入的判断题目1吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":3,\"success\":-1,\"share\":1,\"rowKey\":\"17\",\"option\":[{\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":2}],\"answer\":\"A\",\"scoreBySuccess\":0}', 1, NULL, 17, 0);
INSERT INTO `t_train_question` VALUES (1882092945046888462, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934222557186\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入的判断题目2吗？\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":3,\"success\":-1,\"share\":1,\"rowKey\":\"18\",\"option\":[{\"title\":\"A\",\"value\":\"对\",\"selected\":0,\"sort\":1},{\"title\":\"B\",\"value\":\"错\",\"selected\":0,\"sort\":2}],\"answer\":\"A\",\"scoreBySuccess\":0}', 1, NULL, 18, 0);
INSERT INTO `t_train_question` VALUES (1882092945046888463, 1881003994613665794, NULL, '{\"classifyId\":\"1872929934000259074\",\"name\":\"这是一个导入的题目2\",\"title\":\"这是一个导入(__)的填空题目吗。\",\"analysis\":\"\",\"score\":2,\"level\":3,\"already\":false,\"type\":4,\"success\":-1,\"share\":1,\"rowKey\":\"19\",\"option\":[{\"title\":\"第一个填空\",\"value\":\"\",\"selected\":0,\"sort\":1}],\"answer\":[{\"title\":\"第一个填空\",\"value\":\"这是第1个答案\",\"selected\":0,\"sort\":1}],\"scoreBySuccess\":0}', 1, NULL, 19, 0);

-- ----------------------------
-- Table structure for t_train_question_user
-- ----------------------------
DROP TABLE IF EXISTS `t_train_question_user`;
CREATE TABLE `t_train_question_user`  (
                                          `id` bigint(0) NOT NULL COMMENT '主键',
                                          `train_id` bigint(0) NULL DEFAULT NULL COMMENT '培训ID',
                                          `train_question_id` bigint(0) NULL DEFAULT NULL COMMENT '培训-试题ID',
                                          `user_id` bigint(0) NULL DEFAULT NULL COMMENT '用户ID',
                                          `config` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '试题配置',
                                          `into_video` int(0) NULL DEFAULT 0 COMMENT '是否插入视频中（1插入，0不插入）',
                                          `into_time` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '插入视频中的时间',
                                          `sort` int(0) NULL DEFAULT NULL COMMENT '排序',
                                          `do_already` int(0) NULL DEFAULT 0 COMMENT '是否已做（1已做，0未做）',
                                          `do_time` datetime(0) NULL DEFAULT NULL COMMENT '答题时间',
                                          `score` int(0) NULL DEFAULT NULL COMMENT '得分',
                                          `success` int(0) NULL DEFAULT NULL COMMENT '是否正确（1正确，0不正确）',
                                          `del_flag` int(0) NULL DEFAULT 0 COMMENT '是否删除（0未删除，1删除）',
                                          PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '培训试题用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_train_question_user
-- ----------------------------

-- ----------------------------
-- Table structure for t_train_user
-- ----------------------------
DROP TABLE IF EXISTS `t_train_user`;
CREATE TABLE `t_train_user`  (
                                 `train_id` bigint(0) NOT NULL COMMENT '培训ID',
                                 `user_id` bigint(0) NOT NULL COMMENT '用户ID',
                                 `user_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
                                 `user_code` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户编号',
                                 `org_id` bigint(0) NULL DEFAULT NULL COMMENT '组织机构ID',
                                 `org_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组织机构名称',
                                 `paper_done` int(0) NULL DEFAULT 0 COMMENT '是否完成答题（1完成、0未完成）',
                                 `score` int(0) NULL DEFAULT 0 COMMENT '试卷得分',
                                 `do_time` datetime(0) NULL DEFAULT NULL COMMENT '提交时间',
                                 `plat` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '试卷答题平台（WEB网页端，APP移动端）',
                                 UNIQUE INDEX `idx_uni`(`train_id`, `user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '培训用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_train_user
-- ----------------------------
INSERT INTO `t_train_user` VALUES (1881003994613665794, 1111111111111111111, '超级管理员', 'administrator', 1850706813621731329, NULL, 0, 0, NULL, NULL);
INSERT INTO `t_train_user` VALUES (1881003994613665794, 1873746662934450177, '寒江', 'HJ0001', 1850706813621731329, NULL, 0, 0, NULL, NULL);
INSERT INTO `t_train_user` VALUES (1881003994613665794, 1873749058125398017, '东瑞', 'HJ0002', 1850706813621731329, NULL, 0, 0, NULL, NULL);

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
                           `id` bigint(0) NOT NULL COMMENT '主键',
                           `code` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户编号',
                           `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户姓名',
                           `nickname` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
                           `phone` bigint(0) NULL DEFAULT NULL COMMENT '手机号码',
                           `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱号码',
                           `org_id` bigint(0) NULL DEFAULT NULL COMMENT '所属组织',
                           `account` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录账号',
                           `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录密码',
                           `valid` int(0) NULL DEFAULT 1 COMMENT '是否有效',
                           `sort` int(0) NULL DEFAULT NULL COMMENT '用户排序',
                           `del_flag` int(0) NULL DEFAULT 0 COMMENT '是否删除',
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1111111111111111111, 'administrator', '超级管理员', '超级管理员', 17865454234, 'administrator@hj.com', 1850706813621731329, 'administrator', 'c0dcc99913baee40588da3bf134a2205', 1, 1, 0);
INSERT INTO `t_user` VALUES (1873746662934450177, 'HJ0001', '寒江', '寒江', 17865289765, NULL, 1850706813621731329, 'hanjiang', 'c0dcc99913baee40588da3bf134a2205', 1, 2, 0);
INSERT INTO `t_user` VALUES (1873749058125398017, 'HJ0002', '东瑞', '东瑞', 17865434567, NULL, 1850706813621731329, 'dongrui', 'c0dcc99913baee40588da3bf134a2205', 1, 3, 0);

-- ----------------------------
-- Table structure for t_user_active
-- ----------------------------
DROP TABLE IF EXISTS `t_user_active`;
CREATE TABLE `t_user_active`  (
                                  `id` bigint(0) NOT NULL COMMENT '主键',
                                  `exam_tran_id` bigint(0) NOT NULL COMMENT '考试及培训id',
                                  `user_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户编码',
                                  `active_time` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '活跃当天日期’yyyy-mm-dd‘',
                                  `active_type` int(0) NULL DEFAULT 1 COMMENT '类型（0考试，1培训）-方便后期扩展',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户考试用户活跃表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user_active
-- ----------------------------
INSERT INTO `t_user_active` VALUES (1, 1, '1', '2024-12-21', 1);
INSERT INTO `t_user_active` VALUES (2, 2, '2', '2024-12-22', 1);
INSERT INTO `t_user_active` VALUES (3, 3, '3', '2024-12-23', 1);
INSERT INTO `t_user_active` VALUES (4, 4, '4', '2024-12-24', 1);
INSERT INTO `t_user_active` VALUES (5, 5, '5', '2024-12-25', 1);
INSERT INTO `t_user_active` VALUES (6, 6, '6', '2024-12-25', 1);

-- ----------------------------
-- Table structure for t_user_question_error
-- ----------------------------
DROP TABLE IF EXISTS `t_user_question_error`;
CREATE TABLE `t_user_question_error`  (
                                          `user_code` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户编号',
                                          `exam_id` bigint(0) NOT NULL COMMENT '考试ID',
                                          `exam_paper_id` bigint(0) NOT NULL COMMENT '考试试卷ID',
                                          `question_config` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '试题配置'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户错题集表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user_question_error
-- ----------------------------

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role`  (
                                `user_id` bigint(0) NOT NULL COMMENT '用户ID',
                                `role_id` bigint(0) NOT NULL COMMENT '角色ID'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户_角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
INSERT INTO `t_user_role` VALUES (1111111111111111111, 1111111111111111111);
INSERT INTO `t_user_role` VALUES (1873746662934450177, 1887430996501467137);
INSERT INTO `t_user_role` VALUES (1873746662934450177, 1887070057067560961);
INSERT INTO `t_user_role` VALUES (1873749058125398017, 1887430996501467137);

SET FOREIGN_KEY_CHECKS = 1;
