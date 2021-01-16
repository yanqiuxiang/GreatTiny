/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50528
 Source Host           : localhost:3306
 Source Schema         : yqx

 Target Server Type    : MySQL
 Target Server Version : 50528
 File Encoding         : 65001

 Date: 03/02/2020 12:16:25
*/
CREATE DATABASE IF NOT EXISTS yqx;
use yqx;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `icon` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  `url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `p_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6214 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_menu
-- ----------------------------
INSERT INTO `t_menu` VALUES (1, 'menu-plugin', '系统菜单', 1, NULL, -1);
INSERT INTO `t_menu` VALUES (10, '&#xe68e;', '内容管理', 1, NULL, 1);
INSERT INTO `t_menu` VALUES (60, '&#xe631;', '系统管理', 1, NULL, 1);
INSERT INTO `t_menu` VALUES (61, '&#xe705;', '新闻资讯', 1, 'http://www.ifeng.com/', 1);
INSERT INTO `t_menu` VALUES (62, '&#xe756;', '测试e', 1, 'www.baidu.com', 1);
INSERT INTO `t_menu` VALUES (1000, 'icon-text', '文章管理', 2, 'https://www.hongxiu.com/', 10);
INSERT INTO `t_menu` VALUES (6000, '&#xe631;', '菜单管理', 2, '/menu/tomunemanage', 60);
INSERT INTO `t_menu` VALUES (6010, 'icon-icon10', '角色管理', 2, '/role/torolemanage', 60);
INSERT INTO `t_menu` VALUES (6020, '&#xe612;', '用户管理', 2, '/user/tousermanage', 60);
INSERT INTO `t_menu` VALUES (6030, '&#xe631;', 'sql监控', 2, 'druid/index.html', 60);
INSERT INTO `t_menu` VALUES (6040, 'icon-ziliao', '修改密码', 2, '/user/toUpdatePassword', 60);
INSERT INTO `t_menu` VALUES (6050, 'icon-tuichu', '安全退出', 2, '/user/logout', 60);
INSERT INTO `t_menu` VALUES (6100, 'icon-text', '凤凰网', 2, 'http://www.ifeng.com/', 61);
INSERT INTO `t_menu` VALUES (6200, 'icon-tuichu', '百度16', 2, 'http://www.baidu.com', 62);
INSERT INTO `t_menu` VALUES (6201, 'icon-guanbi', '百度', 2, 'http://www.baidu.com', 62);
INSERT INTO `t_menu` VALUES (6202, 'icon-mokuai', '百度2', 2, 'http://www.badu.com', 62);
INSERT INTO `t_menu` VALUES (6203, 'icon-caidan', '百度3', 2, 'http://www.baidu.com', 62);
INSERT INTO `t_menu` VALUES (6204, 'icon-caidan', '百度4', 2, 'http://www.baidu.com', 62);
INSERT INTO `t_menu` VALUES (6205, 'icon-caidan', '百度5', 2, 'http://www.baidu.com', 62);
INSERT INTO `t_menu` VALUES (6206, 'icon-caidan', '百度6', 2, 'http://www.baidu.com', 62);
INSERT INTO `t_menu` VALUES (6207, 'icon-caidan', '百度7', 2, 'http://www.baidu.com', 62);
INSERT INTO `t_menu` VALUES (6208, 'icon-caidan', '百度8', 2, 'http://www.baidu.com', 62);
INSERT INTO `t_menu` VALUES (6209, 'icon-caidan', '百度9', 2, 'http://www.baidu.com', 62);
INSERT INTO `t_menu` VALUES (6210, 'icon-caidan', '百度10', 2, 'http://www.baidu.com', 62);
INSERT INTO `t_menu` VALUES (6211, 'icon-caidan', '百度11', 2, 'http://www.baidu.com', 62);
INSERT INTO `t_menu` VALUES (6212, 'icon-caidan', '百度12', 2, 'http://www.baidu.com', 62);
INSERT INTO `t_menu` VALUES (6213, 'icon-caidan', '百度13', 2, 'http://www.baidu.com', 62);

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bz` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `remarks` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES (1, '系统管理员 最高权限', '管理员', NULL);
INSERT INTO `t_role` VALUES (2, '主管', '主管', NULL);
INSERT INTO `t_role` VALUES (4, '采购员', '采购员', NULL);
INSERT INTO `t_role` VALUES (5, '销售经理', '销售经理', '22');
INSERT INTO `t_role` VALUES (7, '仓库管理员', '仓库管理员', NULL);
INSERT INTO `t_role` VALUES (9, '总经理', '总经理', NULL);


-- ----------------------------
-- Table structure for t_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_role_menu`;
CREATE TABLE `t_role_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `menu_id` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 364 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_role_menu
-- ----------------------------
INSERT INTO `t_role_menu` VALUES (36, 10, 2);
INSERT INTO `t_role_menu` VALUES (42, 1, 2);
INSERT INTO `t_role_menu` VALUES (45, 1, 4);
INSERT INTO `t_role_menu` VALUES (55, 1, 9);
INSERT INTO `t_role_menu` VALUES (65, 1, 7);
INSERT INTO `t_role_menu` VALUES (66, 10, 7);
INSERT INTO `t_role_menu` VALUES (126, 60, 15);
INSERT INTO `t_role_menu` VALUES (127, 6010, 15);
INSERT INTO `t_role_menu` VALUES (128, 6020, 15);
INSERT INTO `t_role_menu` VALUES (129, 6030, 15);
INSERT INTO `t_role_menu` VALUES (130, 6040, 15);
INSERT INTO `t_role_menu` VALUES (131, 6050, 15);
INSERT INTO `t_role_menu` VALUES (248, 2000, 1);
INSERT INTO `t_role_menu` VALUES (259, 100000, 1);
INSERT INTO `t_role_menu` VALUES (303, 60, 5);
INSERT INTO `t_role_menu` VALUES (304, 6000, 5);
INSERT INTO `t_role_menu` VALUES (305, 6010, 5);
INSERT INTO `t_role_menu` VALUES (306, 6020, 5);
INSERT INTO `t_role_menu` VALUES (307, 6030, 5);
INSERT INTO `t_role_menu` VALUES (308, 6040, 5);
INSERT INTO `t_role_menu` VALUES (309, 6050, 5);
INSERT INTO `t_role_menu` VALUES (310, 6051, 5);
INSERT INTO `t_role_menu` VALUES (311, 61, 5);
INSERT INTO `t_role_menu` VALUES (312, 6100, 5);
INSERT INTO `t_role_menu` VALUES (338, 10, 1);
INSERT INTO `t_role_menu` VALUES (339, 1000, 1);
INSERT INTO `t_role_menu` VALUES (340, 60, 1);
INSERT INTO `t_role_menu` VALUES (341, 6000, 1);
INSERT INTO `t_role_menu` VALUES (342, 6010, 1);
INSERT INTO `t_role_menu` VALUES (343, 6020, 1);
INSERT INTO `t_role_menu` VALUES (344, 6030, 1);
INSERT INTO `t_role_menu` VALUES (345, 6040, 1);
INSERT INTO `t_role_menu` VALUES (346, 6050, 1);
INSERT INTO `t_role_menu` VALUES (347, 61, 1);
INSERT INTO `t_role_menu` VALUES (348, 6100, 1);
INSERT INTO `t_role_menu` VALUES (349, 62, 1);
INSERT INTO `t_role_menu` VALUES (350, 6200, 1);
INSERT INTO `t_role_menu` VALUES (351, 6201, 1);
INSERT INTO `t_role_menu` VALUES (352, 6202, 1);
INSERT INTO `t_role_menu` VALUES (353, 6203, 1);
INSERT INTO `t_role_menu` VALUES (354, 6204, 1);
INSERT INTO `t_role_menu` VALUES (355, 6205, 1);
INSERT INTO `t_role_menu` VALUES (356, 6206, 1);
INSERT INTO `t_role_menu` VALUES (357, 6207, 1);
INSERT INTO `t_role_menu` VALUES (358, 6208, 1);
INSERT INTO `t_role_menu` VALUES (359, 6209, 1);
INSERT INTO `t_role_menu` VALUES (360, 6210, 1);
INSERT INTO `t_role_menu` VALUES (361, 6211, 1);
INSERT INTO `t_role_menu` VALUES (362, 6212, 1);
INSERT INTO `t_role_menu` VALUES (363, 6213, 1);

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bz` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `true_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `user_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `remarks` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, '管理员', 'e10adc3949ba59abbe56e057f20f883e', '管理员', 'admin', '1');
INSERT INTO `t_user` VALUES (2, '主管', 'e10adc3949ba59abbe56e057f20f883e', '王大锤', 'jack', '1');
INSERT INTO `t_user` VALUES (3, '销售经理', 'e10adc3949ba59abbe56e057f20f883e', '玛丽', 'marry', '11');


-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 59 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
INSERT INTO `t_user_role` VALUES (1, 1, 1);
INSERT INTO `t_user_role` VALUES (28, 2, 3);
INSERT INTO `t_user_role` VALUES (29, 4, 3);
INSERT INTO `t_user_role` VALUES (30, 5, 3);
INSERT INTO `t_user_role` VALUES (31, 7, 3);
INSERT INTO `t_user_role` VALUES (55, 2, 2);
INSERT INTO `t_user_role` VALUES (56, 4, 2);
INSERT INTO `t_user_role` VALUES (57, 5, 2);
INSERT INTO `t_user_role` VALUES (58, 1, 2);

SET FOREIGN_KEY_CHECKS = 1;
