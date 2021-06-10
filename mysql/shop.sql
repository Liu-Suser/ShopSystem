# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.32)
# Database: shop
# Generation Time: 2020-11-29 09:11:39 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table address
# ------------------------------------------------------------

DROP TABLE IF EXISTS `address`;

CREATE TABLE `address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL,
  `is_default` tinyint(1) DEFAULT NULL,
  `is_delete` tinyint(1) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `address_user` (`user_id`),
  CONSTRAINT `address_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;

INSERT INTO `address` (`id`, `user_id`, `name`, `phone`, `address`, `is_default`, `is_delete`, `create_time`, `update_time`)
VALUES
	(1,4,'先生','1111','测试地址1',1,0,'2020-11-06 11:37:27','2020-11-06 11:37:27'),
	(2,4,'女士','111','test address',0,0,'2020-11-06 11:38:12','2020-11-06 11:38:12'),
	(3,5,'先生','3333','测试地址3',1,0,'2020-11-08 06:05:39','2020-11-08 06:05:39'),
	(4,4,'test','110','test address',0,1,'2020-11-24 05:18:42','2020-11-24 05:18:42');

/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table advert
# ------------------------------------------------------------

DROP TABLE IF EXISTS `advert`;

CREATE TABLE `advert` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `image` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table cart
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cart`;

CREATE TABLE `cart` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `quantity` int(10) DEFAULT NULL,
  `checked` tinyint(1) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `cart_user` (`user_id`),
  KEY `cart_product` (`product_id`),
  CONSTRAINT `cart_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `cart_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `cart` WRITE;
/*!40000 ALTER TABLE `cart` DISABLE KEYS */;

INSERT INTO `cart` (`id`, `user_id`, `product_id`, `quantity`, `checked`, `create_time`, `update_time`)
VALUES
	(8,5,11,1,1,'2020-11-08 04:53:03','2020-11-08 04:53:03'),
	(9,5,5,1,1,'2020-11-08 05:25:48','2020-11-08 05:25:48'),
	(11,4,8,1,1,'2020-11-08 13:43:29','2020-11-08 13:43:29'),
	(12,4,2,1,1,'2020-11-24 05:38:44','2020-11-24 05:38:44');

/*!40000 ALTER TABLE `cart` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table category
# ------------------------------------------------------------

DROP TABLE IF EXISTS `category`;

CREATE TABLE `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;

INSERT INTO `category` (`id`, `name`, `status`, `parent_id`, `create_time`, `update_time`)
VALUES
	(1,'数码',1,0,'2020-11-02 13:21:23','2020-11-02 13:21:23'),
	(2,'耳机',1,1,'2020-11-02 13:21:43','2020-11-02 13:21:43'),
	(3,'耳机',1,1,'2020-11-04 07:13:00','2020-11-04 07:13:00'),
	(4,'服饰',1,0,'2020-11-05 06:01:37','2020-11-05 06:01:37'),
	(5,'毛衣',1,4,'2020-11-05 06:01:55','2020-11-05 06:01:55'),
	(6,'休闲裤',1,4,'2020-11-05 06:02:08','2020-11-05 06:02:08'),
	(7,'T恤',1,4,'2020-11-05 06:02:20','2020-11-05 06:02:20'),
	(8,'笔记本电脑',1,1,'2020-11-07 12:46:01','2020-11-07 12:46:01'),
	(10,'牛仔裤',1,4,'2020-11-08 08:39:29','2020-11-08 08:39:29');

/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table comment
# ------------------------------------------------------------

DROP TABLE IF EXISTS `comment`;

CREATE TABLE `comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `product_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `rate` int(1) DEFAULT NULL,
  `image` json,
  `comment` varchar(500) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `comment_product` (`product_id`),
  KEY `comment_user` (`user_id`),
  CONSTRAINT `comment_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `comment_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;

INSERT INTO `comment` (`id`, `product_id`, `user_id`, `rate`, `image`, `comment`, `create_time`, `update_time`)
VALUES
	(1,2,4,5,NULL,'好东西','2020-11-07 00:36:27','2020-11-07 00:36:27'),
	(4,10,4,4,NULL,'挺好用的手机','2020-11-08 10:35:47','2020-11-08 10:35:47'),
	(5,3,4,5,NULL,'暖和','2020-11-08 10:37:14','2020-11-08 10:37:14'),
	(6,2,4,4,NULL,'好听','2020-11-08 10:37:21','2020-11-08 10:37:21'),
	(7,1,4,3,NULL,'觉得一般','2020-11-08 10:37:33','2020-11-08 10:37:33'),
	(8,1,4,5,NULL,'挺好的','2020-11-08 10:37:39','2020-11-08 10:37:39'),
	(9,6,4,5,NULL,'抢到了','2020-11-08 10:37:48','2020-11-08 10:37:48'),
	(10,1,4,1,NULL,'不行','2020-11-08 10:37:57','2020-11-08 10:37:57'),
	(11,8,4,5,NULL,'华为笔记本挺好的','2020-11-08 10:39:47','2020-11-08 10:39:47'),
	(13,1,4,5,NULL,'非常好用的手机！','2020-11-14 09:14:25','2020-11-14 09:14:25'),
	(14,2,4,4,NULL,'好好哈哈哈哈','2020-11-16 02:58:55','2020-11-16 02:58:55'),
	(15,3,4,4,NULL,'还好哈哈哈哈哈哈','2020-11-16 03:42:48','2020-11-16 03:42:48'),
	(16,1,4,4,NULL,'哈哈哈的','2020-11-16 03:51:34','2020-11-16 03:51:34'),
	(17,2,4,4,NULL,'还哈哈哈哈哈','2020-11-16 03:51:45','2020-11-16 03:51:45'),
	(18,3,4,4,NULL,'就阿我说的哈hi','2020-11-16 03:51:51','2020-11-16 03:51:51'),
	(19,1,4,4,NULL,'哈哈哈哈哈','2020-11-16 10:40:11','2020-11-16 10:40:11'),
	(20,2,4,3,NULL,'发反反复复','2020-11-16 10:40:17','2020-11-16 10:40:17'),
	(21,3,4,2,NULL,'就斤斤计较','2020-11-16 10:40:24','2020-11-16 10:40:24'),
	(22,1,4,3,NULL,'还行。。。。','2020-11-23 05:05:40','2020-11-23 05:05:40'),
	(23,8,4,4,NULL,'还好','2020-11-29 10:33:17','2020-11-29 10:33:17'),
	(24,4,4,2,NULL,'不太暖和','2020-11-29 10:33:38','2020-11-29 10:33:38');

/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table order_detail
# ------------------------------------------------------------

DROP TABLE IF EXISTS `order_detail`;

CREATE TABLE `order_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL,
  `product_id` int(11) DEFAULT NULL,
  `comment_id` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `payment` decimal(20,2) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `order_detail_product` (`product_id`),
  KEY `order_detail_comment` (`comment_id`),
  KEY `order_detail_order_total` (`order_id`),
  CONSTRAINT `order_detail_comment` FOREIGN KEY (`comment_id`) REFERENCES `comment` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `order_detail_order_total` FOREIGN KEY (`order_id`) REFERENCES `order_total` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `order_detail_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `order_detail` WRITE;
/*!40000 ALTER TABLE `order_detail` DISABLE KEYS */;

INSERT INTO `order_detail` (`id`, `order_id`, `product_id`, `comment_id`, `quantity`, `payment`, `create_time`, `update_time`)
VALUES
	(28,16,1,13,1,NULL,'2020-11-14 07:19:22','2020-11-14 07:19:22'),
	(29,16,2,14,1,NULL,'2020-11-14 07:19:22','2020-11-14 07:19:22'),
	(30,16,3,15,1,NULL,'2020-11-14 07:19:22','2020-11-14 07:19:22'),
	(31,17,1,16,1,NULL,'2020-11-14 07:54:39','2020-11-14 07:54:39'),
	(32,17,2,17,1,NULL,'2020-11-14 07:54:39','2020-11-14 07:54:39'),
	(33,17,3,18,1,NULL,'2020-11-14 07:54:39','2020-11-14 07:54:39'),
	(34,18,1,NULL,1,NULL,'2020-11-14 07:54:40','2020-11-14 07:54:40'),
	(35,18,2,NULL,1,NULL,'2020-11-14 07:54:40','2020-11-14 07:54:40'),
	(36,18,3,NULL,1,NULL,'2020-11-14 07:54:40','2020-11-14 07:54:40'),
	(37,19,1,NULL,1,NULL,'2020-11-15 03:30:59','2020-11-15 03:30:59'),
	(38,19,2,NULL,1,NULL,'2020-11-15 03:30:59','2020-11-15 03:30:59'),
	(39,19,3,NULL,1,NULL,'2020-11-15 03:30:59','2020-11-15 03:30:59'),
	(40,20,4,NULL,1,NULL,'2020-11-15 05:38:00','2020-11-15 05:38:00'),
	(41,20,5,NULL,1,NULL,'2020-11-15 05:38:00','2020-11-15 05:38:00'),
	(42,21,8,NULL,1,NULL,'2020-11-16 05:43:07','2020-11-16 05:43:07'),
	(43,21,9,NULL,1,NULL,'2020-11-16 05:43:07','2020-11-16 05:43:07'),
	(44,22,11,NULL,1,NULL,'2020-11-16 05:57:48','2020-11-16 05:57:48'),
	(45,22,5,NULL,1,NULL,'2020-11-16 05:57:48','2020-11-16 05:57:48'),
	(46,23,1,22,1,NULL,'2020-11-19 10:19:36','2020-11-19 10:19:36'),
	(47,24,8,NULL,1,NULL,'2020-11-23 04:29:10','2020-11-23 04:29:10'),
	(48,25,1,NULL,20,NULL,'2020-11-28 14:44:01','2020-11-28 14:44:01'),
	(49,25,2,NULL,20,NULL,'2020-11-28 14:44:01','2020-11-28 14:44:01'),
	(50,25,3,NULL,20,NULL,'2020-11-28 14:44:01','2020-11-28 14:44:01'),
	(51,26,1,NULL,20,NULL,'2020-11-28 14:53:11','2020-11-28 14:53:11'),
	(52,26,2,NULL,20,NULL,'2020-11-28 14:53:11','2020-11-28 14:53:11'),
	(53,26,3,NULL,20,NULL,'2020-11-28 14:53:11','2020-11-28 14:53:11'),
	(54,27,1,NULL,20,NULL,'2020-11-29 03:04:34','2020-11-29 03:04:34'),
	(55,27,2,NULL,20,NULL,'2020-11-29 03:04:34','2020-11-29 03:04:34'),
	(56,27,3,NULL,20,NULL,'2020-11-29 03:04:34','2020-11-29 03:04:34'),
	(57,28,7,NULL,1,NULL,'2020-11-29 03:36:27','2020-11-29 03:36:27'),
	(58,28,7,NULL,1,NULL,'2020-11-29 03:36:27','2020-11-29 03:36:27'),
	(59,28,7,NULL,1,NULL,'2020-11-29 03:36:27','2020-11-29 03:36:27'),
	(60,29,1,NULL,19,NULL,'2020-11-29 03:39:34','2020-11-29 03:39:34'),
	(61,29,2,NULL,19,NULL,'2020-11-29 03:39:34','2020-11-29 03:39:34'),
	(62,29,3,NULL,19,NULL,'2020-11-29 03:39:34','2020-11-29 03:39:34'),
	(63,30,1,NULL,5,NULL,'2020-11-29 03:39:47','2020-11-29 03:39:47'),
	(64,30,2,NULL,5,NULL,'2020-11-29 03:39:47','2020-11-29 03:39:47'),
	(65,30,3,NULL,5,NULL,'2020-11-29 03:39:47','2020-11-29 03:39:47'),
	(66,31,1,NULL,6,NULL,'2020-11-29 03:39:48','2020-11-29 03:39:48'),
	(67,31,2,NULL,6,NULL,'2020-11-29 03:39:48','2020-11-29 03:39:48'),
	(68,31,3,NULL,6,NULL,'2020-11-29 03:39:48','2020-11-29 03:39:48'),
	(69,32,1,NULL,13,NULL,'2020-11-29 03:39:49','2020-11-29 03:39:49'),
	(70,32,2,NULL,13,NULL,'2020-11-29 03:39:49','2020-11-29 03:39:49'),
	(71,32,3,NULL,13,NULL,'2020-11-29 03:39:49','2020-11-29 03:39:49'),
	(72,33,1,NULL,7,NULL,'2020-11-29 03:39:50','2020-11-29 03:39:50'),
	(73,33,2,NULL,7,NULL,'2020-11-29 03:39:50','2020-11-29 03:39:50'),
	(74,33,3,NULL,7,NULL,'2020-11-29 03:39:50','2020-11-29 03:39:50'),
	(75,34,1,NULL,14,NULL,'2020-11-29 03:39:51','2020-11-29 03:39:51'),
	(76,34,2,NULL,14,NULL,'2020-11-29 03:39:51','2020-11-29 03:39:51'),
	(77,34,3,NULL,14,NULL,'2020-11-29 03:39:51','2020-11-29 03:39:51'),
	(78,35,1,NULL,2,NULL,'2020-11-29 03:39:52','2020-11-29 03:39:52'),
	(79,35,2,NULL,2,NULL,'2020-11-29 03:39:52','2020-11-29 03:39:52'),
	(80,35,3,NULL,2,NULL,'2020-11-29 03:39:52','2020-11-29 03:39:52'),
	(81,36,1,NULL,3,NULL,'2020-11-29 03:39:54','2020-11-29 03:39:54'),
	(82,36,2,NULL,3,NULL,'2020-11-29 03:39:54','2020-11-29 03:39:54'),
	(83,36,3,NULL,3,NULL,'2020-11-29 03:39:54','2020-11-29 03:39:54'),
	(84,37,1,NULL,14,NULL,'2020-11-29 03:39:56','2020-11-29 03:39:56'),
	(85,37,2,NULL,14,NULL,'2020-11-29 03:39:56','2020-11-29 03:39:56'),
	(86,37,3,NULL,14,NULL,'2020-11-29 03:39:56','2020-11-29 03:39:56'),
	(87,38,10,NULL,5,NULL,'2020-11-29 03:50:21','2020-11-29 03:50:21'),
	(88,38,10,NULL,1,NULL,'2020-11-29 03:50:21','2020-11-29 03:50:21'),
	(89,39,10,NULL,5,NULL,'2020-11-29 03:51:12','2020-11-29 03:51:12'),
	(90,39,2,NULL,6,NULL,'2020-11-29 03:51:12','2020-11-29 03:51:12'),
	(91,40,12,NULL,3,NULL,'2020-11-29 03:51:28','2020-11-29 03:51:28'),
	(92,40,8,NULL,9,NULL,'2020-11-29 03:51:28','2020-11-29 03:51:28'),
	(93,40,7,NULL,19,NULL,'2020-11-29 03:51:28','2020-11-29 03:51:28'),
	(94,41,6,NULL,14,NULL,'2020-11-29 05:31:47','2020-11-29 05:31:47'),
	(95,42,1,NULL,13,NULL,'2020-11-29 05:33:48','2020-11-29 05:33:48'),
	(96,42,12,NULL,16,NULL,'2020-11-29 05:33:48','2020-11-29 05:33:48'),
	(97,43,3,NULL,4,NULL,'2020-11-29 05:39:24','2020-11-29 05:39:24'),
	(98,43,5,NULL,3,NULL,'2020-11-29 05:39:24','2020-11-29 05:39:24'),
	(99,44,5,NULL,2,NULL,'2020-11-29 05:40:45','2020-11-29 05:40:45'),
	(100,45,2,NULL,5,NULL,'2020-11-29 05:50:33','2020-11-29 05:50:33'),
	(101,46,11,NULL,5,NULL,'2020-11-29 05:51:28','2020-11-29 05:51:28'),
	(102,46,3,NULL,7,NULL,'2020-11-29 05:51:28','2020-11-29 05:51:28'),
	(103,46,6,NULL,10,NULL,'2020-11-29 05:51:28','2020-11-29 05:51:28'),
	(104,47,4,NULL,1,NULL,'2020-11-29 05:54:16','2020-11-29 05:54:16'),
	(105,47,2,NULL,20,NULL,'2020-11-29 05:54:16','2020-11-29 05:54:16'),
	(106,47,8,NULL,4,NULL,'2020-11-29 05:54:16','2020-11-29 05:54:16'),
	(107,48,8,NULL,8,NULL,'2020-11-29 05:54:31','2020-11-29 05:54:31'),
	(108,48,9,NULL,11,NULL,'2020-11-29 05:54:31','2020-11-29 05:54:31'),
	(109,49,7,NULL,9,NULL,'2020-11-29 06:00:30','2020-11-29 06:00:30'),
	(110,49,4,24,9,NULL,'2020-11-29 06:00:30','2020-11-29 06:00:30'),
	(111,49,7,NULL,17,NULL,'2020-11-29 06:00:30','2020-11-29 06:00:30'),
	(112,49,10,NULL,12,NULL,'2020-11-29 06:00:30','2020-11-29 06:00:30'),
	(113,50,11,NULL,19,NULL,'2020-11-29 06:01:24','2020-11-29 06:01:24'),
	(114,51,6,NULL,18,NULL,'2020-11-29 06:03:03','2020-11-29 06:03:03'),
	(115,52,4,NULL,19,NULL,'2020-11-29 06:03:43','2020-11-29 06:03:43'),
	(116,52,1,NULL,3,NULL,'2020-11-29 06:03:43','2020-11-29 06:03:43'),
	(117,52,11,NULL,11,NULL,'2020-11-29 06:03:43','2020-11-29 06:03:43'),
	(118,52,9,NULL,10,NULL,'2020-11-29 06:03:43','2020-11-29 06:03:43'),
	(119,53,8,23,1,NULL,'2020-11-29 09:31:00','2020-11-29 09:31:00');

/*!40000 ALTER TABLE `order_detail` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table order_total
# ------------------------------------------------------------

DROP TABLE IF EXISTS `order_total`;

CREATE TABLE `order_total` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `address_id` int(11) DEFAULT NULL,
  `status` int(1) DEFAULT NULL,
  `is_delete` tinyint(1) DEFAULT NULL,
  `price` decimal(20,2) DEFAULT NULL,
  `pay_method` varchar(20) CHARACTER SET utf8 DEFAULT NULL,
  `payment_time` datetime DEFAULT NULL,
  `express` varchar(20) DEFAULT NULL,
  `logistics` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `order_total_address` (`address_id`),
  KEY `order_total_user` (`user_id`),
  CONSTRAINT `order_total_address` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `order_total_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `order_total` WRITE;
/*!40000 ALTER TABLE `order_total` DISABLE KEYS */;

INSERT INTO `order_total` (`id`, `user_id`, `address_id`, `status`, `is_delete`, `price`, `pay_method`, `payment_time`, `express`, `logistics`, `create_time`, `update_time`)
VALUES
	(16,4,1,4,0,8027.00,'支付宝','2020-11-14 08:19:31','顺丰','14214124','2020-11-14 07:19:22','2020-11-24 05:17:50'),
	(17,4,1,3,0,7427.00,'微信','2020-11-14 08:19:31','大卫','123412414','2020-11-14 07:54:39','2020-11-29 09:32:50'),
	(18,4,1,1,0,8027.00,NULL,NULL,NULL,NULL,'2020-11-14 07:54:40','2020-11-19 10:16:46'),
	(19,4,1,6,0,7427.00,'支付宝','2020-11-16 02:35:34','顺丰','3238124097','2020-11-15 03:30:59','2020-11-19 10:20:35'),
	(20,5,3,1,1,1198.00,'支付宝','2020-11-16 02:32:16',NULL,NULL,'2020-11-15 05:38:00','2020-11-16 02:35:15'),
	(21,5,3,1,1,11827.00,'支付宝','2020-11-16 05:43:13',NULL,NULL,'2020-11-16 05:43:07','2020-11-16 05:43:16'),
	(22,5,3,5,0,6998.00,'支付宝','2020-11-16 05:57:51','哥哥','346363466','2020-11-16 05:57:48','2020-11-23 04:50:59'),
	(23,5,3,7,0,6299.00,'支付宝','2020-11-19 10:20:15','樱桃','325623666','2020-11-19 10:19:36','2020-11-23 05:05:45'),
	(24,4,1,2,0,5999.00,'支付宝','2020-11-23 04:35:02','yz','23512351145','2020-11-23 04:29:10','2020-11-23 04:50:06'),
	(25,4,1,1,0,148540.00,NULL,'2020-11-28 14:45:11',NULL,NULL,'2020-11-28 14:44:01','2020-11-28 14:45:27'),
	(26,4,1,1,0,148540.00,NULL,NULL,NULL,NULL,'2020-11-28 14:53:11','2020-11-28 14:53:24'),
	(27,4,1,1,0,148540.00,NULL,NULL,NULL,NULL,'2020-11-29 03:04:34','2020-11-29 03:52:10'),
	(28,4,1,1,0,41997.00,NULL,NULL,NULL,NULL,'2020-11-29 03:36:27','2020-11-29 03:52:15'),
	(29,4,1,1,0,141113.00,NULL,NULL,NULL,NULL,'2020-11-29 03:39:34','2020-11-29 03:52:18'),
	(30,4,1,1,0,37135.00,NULL,NULL,NULL,NULL,'2020-11-29 03:39:47','2020-11-29 03:52:22'),
	(31,4,1,1,0,44562.00,NULL,NULL,NULL,NULL,'2020-11-29 03:39:48','2020-11-29 03:52:25'),
	(32,4,1,1,0,96551.00,NULL,NULL,NULL,NULL,'2020-11-29 03:39:49','2020-11-29 03:52:28'),
	(33,4,1,1,0,51989.00,NULL,NULL,NULL,NULL,'2020-11-29 03:39:50','2020-11-29 03:52:31'),
	(34,4,1,1,0,103978.00,NULL,NULL,NULL,NULL,'2020-11-29 03:39:51','2020-11-29 03:52:35'),
	(35,4,1,1,0,14854.00,NULL,NULL,NULL,NULL,'2020-11-29 03:39:52','2020-11-29 03:52:39'),
	(36,4,1,1,0,22281.00,NULL,NULL,NULL,NULL,'2020-11-29 03:39:54','2020-11-29 03:52:42'),
	(37,4,1,1,0,103978.00,NULL,NULL,NULL,NULL,'2020-11-29 03:39:56','2020-11-29 03:52:46'),
	(38,4,1,1,0,41994.00,NULL,NULL,NULL,NULL,'2020-11-29 03:50:21','2020-11-29 03:52:49'),
	(39,4,1,1,0,40989.00,NULL,NULL,NULL,NULL,'2020-11-29 03:51:12','2020-11-29 03:52:52'),
	(40,4,1,1,0,321079.00,NULL,NULL,NULL,NULL,'2020-11-29 03:51:28','2020-11-29 03:52:58'),
	(41,4,1,1,0,78386.00,NULL,NULL,NULL,NULL,'2020-11-29 05:31:47','2020-11-29 06:05:43'),
	(42,4,1,1,0,87791.00,NULL,NULL,NULL,NULL,'2020-11-29 05:33:48','2020-11-29 06:05:47'),
	(43,4,1,1,0,3513.00,NULL,NULL,NULL,NULL,'2020-11-29 05:39:24','2020-11-29 06:05:51'),
	(44,4,1,1,0,1998.00,NULL,NULL,NULL,NULL,'2020-11-29 05:40:45','2020-11-29 06:05:56'),
	(45,4,1,1,0,4995.00,NULL,NULL,NULL,NULL,'2020-11-29 05:50:33','2020-11-29 06:06:07'),
	(46,4,1,1,0,86888.00,NULL,NULL,NULL,NULL,'2020-11-29 05:51:28','2020-11-29 06:06:11'),
	(47,4,1,6,0,44175.00,NULL,NULL,NULL,NULL,'2020-11-29 05:54:16','2020-11-29 06:06:38'),
	(48,4,1,6,0,112100.00,NULL,NULL,NULL,NULL,'2020-11-29 05:54:31','2020-11-29 06:06:42'),
	(49,4,1,6,0,449753.00,NULL,NULL,NULL,NULL,'2020-11-29 06:00:30','2020-11-29 06:06:45'),
	(50,4,1,6,0,113981.00,NULL,NULL,NULL,NULL,'2020-11-29 06:01:24','2020-11-29 06:06:49'),
	(51,4,1,6,0,100782.00,NULL,NULL,NULL,NULL,'2020-11-29 06:03:03','2020-11-29 06:06:54'),
	(52,4,1,6,0,146947.00,NULL,NULL,NULL,NULL,'2020-11-29 06:03:43','2020-11-29 06:06:56'),
	(53,4,1,6,0,5999.00,'支付宝','2020-11-29 09:31:05','达到','3245325','2020-11-29 09:31:00','2020-11-29 09:50:44');

/*!40000 ALTER TABLE `order_total` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table product
# ------------------------------------------------------------

DROP TABLE IF EXISTS `product`;

CREATE TABLE `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category_id` int(11) DEFAULT NULL,
  `name` varchar(32) DEFAULT NULL,
  `subtitle` varchar(100) DEFAULT NULL,
  `image` json,
  `detail` text,
  `price` decimal(20,2) DEFAULT NULL,
  `sale` int(11) DEFAULT '0',
  `inventory` int(11) DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `product_category` (`category_id`),
  CONSTRAINT `product_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;

INSERT INTO `product` (`id`, `category_id`, `name`, `subtitle`, `image`, `detail`, `price`, `sale`, `inventory`, `status`, `create_time`, `update_time`)
VALUES
	(1,2,'IPhone 12','IPhone 12','{"names":["d1a7b8e1b7035cca71b27db11fa6de0d","411b44aed1565b6e36c1a08f527cb73f"]}','苹果2020年发布的IPhone12',6299.00,11,94,1,'2020-11-02 13:22:17','2020-11-02 13:22:17'),
	(2,3,'Sony WH-1000 XM3','Sony WH-1000 XM3 蓝牙耳机','{"names":["81a58de13cf4bd689f41bd7c3f7a3b83"]}','t',999.00,26,79,1,'2020-11-04 07:30:28','2020-11-04 07:30:28'),
	(3,5,'LaleiFY 打底衫2020秋冬新款','LaleiFY 半高领毛衣女打底衫2020秋冬新款百搭洋气内搭套头修身紧身针织衫','{"names":["041d344f9591df0cbbb05a9bf3a56ea5"]}','拉伦菲韵 半高领毛衣女打底衫2020秋冬新款百搭洋气内搭套头修身紧身针织衫 黑色 L商品编号：10021342331626店铺： 拉伦菲韵旗舰店商品毛重：1.0kg货号：LFY19B624553301388材质：其它版型：修身型风格：百搭，简约，优雅，轻奢，小清新袖长：长袖上市时间：2020年冬季厚度：加厚穿着方式：套头流行元素：拼色领型：半高领袖型：常规袖衣长：常规款图案：纯色含毛量：96%以上',129.00,4,85,1,'2020-11-05 06:02:55','2020-11-05 06:02:55'),
	(4,5,'高领毛衣','毛衣','{"names":["041d344f9591df0cbbb05a9bf3a56ea5"]}','毛衣',199.00,29,70,1,'2020-11-05 06:20:49','2020-11-05 06:20:49'),
	(5,3,'Beats  头戴式 耳机','Beats 头戴式 蓝牙无线耳机 手机耳机 游戏耳机 - 桀骜黑红','{"names":["a2ba70bf3f55eb4d4f4f37793ef78695"]}','beats 商品编号：7824797商品毛重：0.86kg商品产地：中国大陆连接类型：无线（蓝牙）佩戴方式：头戴式用途：音乐耳机',999.00,0,98,1,'2020-11-05 06:49:05','2020-11-05 06:49:05'),
	(6,8,'联想 小新Pro13 2020','联想(Lenovo)小新Pro13锐龙版轻薄本 13.3英寸全面屏办公笔记本电脑(标压R5 16G 512G 2.5k屏高色域)银','{"names":["ab01d14a4824fdb42d883775aa88c5cb"]}','联想小新Pro13商品编号：100007250507商品毛重：2.09kg商品产地：中国大陆显卡类别：集成显卡分辨率：超高清屏（2K/2.5K/3K/4K）类型：高性能轻薄笔记本优选服务：两年质保厚度：15.1mm—18.0mm机身材质：金属材质特性：全面屏，快充，面部识别屏幕尺寸：13.0英寸-13.9英寸系列：联想-小新Pro裸机重量：1-1.5KG屏幕色域：100%sRGB处理器：AMD R7屏幕刷新率：其他待机时长：＞12小时机械硬盘：无机械硬盘内存容量：16G系统：windows 10 带Office固态硬盘（SSD）：512GB颜色：灰色',5599.00,20,81,1,'2020-11-07 12:48:55','2020-11-07 12:48:55'),
	(7,8,'DELL XPS13-7390','戴尔 DELL XPS13-7390','{"names":["84d8fc774e1bd09f3707ec2f209bfd41"]}','戴尔XPS 13商品编号：100012417276商品毛重：2.445kg商品产地：中国大陆显卡类别：集成显卡分辨率：全高清屏（1920×1080）类型：高端轻薄笔记本优选服务：两年质保厚度：15.0mm及以下显卡芯片供应商：NVIDIA屏幕尺寸：13.0英寸-13.9英寸系列：戴尔 - XPS裸机重量：1-1.5KG特性：指纹识别机械硬盘：无机械硬盘处理器：Intel i7屏幕刷新率：其他待机时长：9小时以上内存容量：8G显卡型号：其他系统：windows 10 带Office固态硬盘（SSD）：512GB颜色：银色',13999.00,26,73,1,'2020-11-07 13:01:31','2020-11-07 13:01:31'),
	(8,8,'华为 MateBook 14 2020款','华为 笔记本MateBook 14 2020款','{"names":["84d8fc774e1bd09f3707ec2f209bfd41"]}','华为（HUAWEI）HUAWEI MateBook 14商品编号：10020502841610店铺： 华为兴汇聚专卖店商品毛重：1.53kg商品产地：中国大陆显卡类别：集成显卡分辨率：超高清屏（2K/2.5K/3K/4K）类型：轻薄笔记本优选服务：两年质保厚度：15.1mm—18.0mm机身材质：金属材质特性：PCI-E高速固态硬盘，触控屏，背光键盘屏幕尺寸：14.0英寸-14.9英寸系列：华为-Matebook处理器：AMD R5裸机重量：1.5-2kg系统：windows 10 带Office显卡芯片供应商：其他内存容量：16G屏幕刷新率：其他固态硬盘（SSD）：512GB机械硬盘：无机械硬盘',5999.00,15,85,1,'2020-11-07 13:02:56','2020-11-07 13:02:56'),
	(9,2,'Apple iPhone 12','Apple iPhone 12 (A2400)移动联通电信','{"names":["411b44aed1565b6e36c1a08f527cb73f"]}','AppleiPhone 12商品编号：100016034378商品毛重：320.00g商品产地：中国大陆CPU型号：其他运行内存：其他机身存储：128GB存储卡：不支持存储卡摄像头数量：后置双摄后摄主摄像素：1200万像素前摄主摄像素：1200万像素主屏幕尺寸（英寸）：6.1英寸分辨率：其它分辨率屏幕比例：其它屏幕比例屏幕前摄组合：其他充电器：其他操作系统：iOS(Apple)',5828.00,21,78,1,'2020-11-07 13:11:07','2020-11-07 13:11:07'),
	(10,2,'HUAWEI Mate 40 Pro 手机','华为 HUAWEI Mate 40 Pro 8GB+256GB 5G','{"names":["d1a7b8e1b7035cca71b27db11fa6de0d"]}','华为Mate 40 Pro商品编号：100016168278商品毛重：0.575kg商品产地：中国大陆CPU型号：其他运行内存：8GB机身存储：256GB存储卡：NM存储卡摄像头数量：后置三摄后摄主摄像素：5000万像素前摄主摄像素：1300万像素主屏幕尺寸（英寸）：6.76英寸分辨率：其它分辨率屏幕比例：其它屏幕比例屏幕前摄组合：其他充电器：10V/4A；4.5V/5A；11V/6A；5V/2A；10V/2.25A；5V/4.5A；9V/2A热点：无线充电，快速充电，防水防尘，NFC，屏幕指纹，高倍率变焦，曲面屏，5G，3D(ToF或结构光)，屏幕高刷新率操作系统：其它OS',6999.00,13,87,1,'2020-11-07 13:15:52','2020-11-07 13:15:52'),
	(11,2,'一加 8 Pro 5G 8GB+128GB','一加 OnePlus 8 Pro 5G旗舰 2K+120Hz 柔性屏 30W无线闪充 高通骁龙865 8GB+128GB 黑镜 拍照游戏手机','{"names":["411b44aed1565b6e36c1a08f527cb73f"]}','一加8 Pro商品编号：100006713417商品毛重：199.00g商品产地：中国大陆CPU型号：其他运行内存：12GB机身存储：256GB存储卡：不支持存储卡摄像头数量：后置四摄后摄主摄像素：4800万像素前摄主摄像素：1600万像素主屏幕尺寸（英寸）：6.78分辨率：高清HD+屏幕比例：19.6~20:9屏幕前摄组合：瞳孔屏充电器：5V6A操作系统：Android(安卓)',5999.00,30,68,1,'2020-11-07 13:20:33','2020-11-07 13:20:33'),
	(12,5,'BANANA BABY2020 毛衣','BANANA BABY2020秋冬新款休闲宽松复古系带领针织毛衣女开衫上衣D204MY924 红色 S','{"names":["041d344f9591df0cbbb05a9bf3a56ea5"]}','BANANA BABY2020秋冬新款休闲宽松复古系带领针织毛衣女开衫上衣D204MY924 黑色 S商品编号：10022355816792店铺： bananababy旗舰店商品毛重：1.0kg商品产地：中国大陆货号：D204MY924材质：聚丙烯腈纤维(腈纶)版型：宽松型风格：通勤袖长：长袖上市时间：2020年冬季厚度：常规穿着方式：开衫流行元素：立体装饰领型：其它袖型：常规袖衣长：常规款图案：纯色含毛量：30%以下',369.00,0,99,1,'2020-11-07 13:23:58','2020-11-07 13:23:58'),
	(13,6,'Lee Cooper 休闲裤','Lee Cooper休闲裤男秋季新款裤子男裤纯色简约磨毛商务休闲裤男时尚百搭修身直筒长裤韩版潮流西裤','{"names":["041d344f9591df0cbbb05a9bf3a56ea5"]}','Lee Cooper休闲裤男秋季新款裤子男裤纯色简约磨毛商务休闲裤男时尚百搭修身直筒长裤韩版潮流西裤 9038灰色常规+9038黑色常规 28商品编号：10021783488056店铺： Lee Cooper马龙棋专卖店商品毛重：500.00g商品产地：中国大陆货号：HDBL-5320+5315XX-EEE工艺：水洗，免烫材质：聚酯纤维适用季节：四季适用人群：青少年裤门襟：拉链腰型：自然腰裤型：直筒裤上市时间：2020年秋季厚度：常规面料：平绒流行元素：拉链，车缝线裤长：长裤风格：韩版，青春休闲弹力：微弹图案：纯色适用场景：运动，休闲',299.00,0,99,1,'2020-11-07 13:35:06','2020-11-07 13:35:06'),
	(14,7,'Emporio Armani EA 男士T恤','Emporio Armani EA男装 2020新品 简约 字母刺绣男士T恤 短袖 多色百搭','{"names":["041d344f9591df0cbbb05a9bf3a56ea5"]}','Emporio Armani EA男装 2020新品 简约 字母刺绣男士T恤 短袖 多色百搭 白色 S(120-140斤)\n',299.00,0,99,1,'2020-11-07 13:42:37','2020-11-07 13:42:37'),
	(15,3,'test','test','{"names":["041d344f9591df0cbbb05a9bf3a56ea5"]}','test',99.30,0,99,1,'2020-11-10 07:47:29','2020-11-10 07:47:29'),
	(16,3,'test1','test1','{"names":["a2ba70bf3f55eb4d4f4f37793ef78695"]}','testhh',99.00,0,99,1,'2020-11-14 09:35:01','2020-11-14 09:35:01'),
	(17,3,'t','t','{}','t',99.00,0,99,1,'2020-11-16 10:58:06','2020-11-16 10:58:06'),
	(18,5,'test3','test3','{"names":["041d344f9591df0cbbb05a9bf3a56ea5"]}','test3t',99.00,0,99,1,'2020-11-23 13:49:57','2020-11-23 13:49:57');

/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table statistic
# ------------------------------------------------------------

DROP TABLE IF EXISTS `statistic`;

CREATE TABLE `statistic` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `new_order` int(11) DEFAULT NULL,
  `cancel_order` int(11) DEFAULT NULL,
  `complete_order` int(11) DEFAULT NULL,
  `timestamp` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `statistic` WRITE;
/*!40000 ALTER TABLE `statistic` DISABLE KEYS */;

INSERT INTO `statistic` (`id`, `new_order`, `cancel_order`, `complete_order`, `timestamp`)
VALUES
	(3,13,6,7,'2020-11-29'),
	(7,3,2,1,'2020-11-28'),
	(8,4,2,3,'2020-11-27'),
	(9,6,1,4,'2020-11-26'),
	(10,8,3,6,'2020-11-25'),
	(11,9,2,5,'2020-11-24'),
	(12,5,4,8,'2020-11-23'),
	(13,2,4,5,'2020-11-22'),
	(14,7,6,2,'2020-11-21'),
	(15,3,1,1,'2020-11-20');

/*!40000 ALTER TABLE `statistic` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) DEFAULT NULL,
  `password` varchar(256) DEFAULT NULL,
  `nickname` varchar(20) DEFAULT NULL,
  `userpoint` int(10) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `question` varchar(100) DEFAULT NULL,
  `answer` varchar(30) DEFAULT NULL,
  `role` int(1) DEFAULT '3',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;

INSERT INTO `user` (`id`, `username`, `password`, `nickname`, `userpoint`, `phone`, `question`, `answer`, `role`, `create_time`, `update_time`)
VALUES
	(1,'admin','$2a$10$/3ZZEJ1V9I8/CHI7dtUf4O7T8U3JpcXTu9cFr6wRk9DS9ijYAvPES','admin',999999,'110','密保答案是admin','admin',0,'2020-11-02 13:18:45','2020-11-02 13:18:45'),
	(2,'service','$2a$10$4w.aUX1Qlufj6x/QO3smgu63G7DREQYOf4c1zso5LP3A6kZ460lt.','service',999999,'110','密保答案是service','service',1,'2020-11-03 03:37:05','2020-11-03 03:37:05'),
	(3,'warehouse','$2a$10$fTdNvcequ9tXRhAb4.NsP.J0AgB.gzGMKOSvCt52xML91OWSCjspS','warehouse',999999,'110','密保答案是warehouse','warehouse',2,'2020-11-03 03:37:35','2020-11-03 03:37:35'),
	(4,'test','$2a$10$TUoERTZ1XSa7hl7L4k1YV.B5xdSvLPQB/qSCFLO2syq3531JK50u2','test',59,'111','test','test',3,'2020-11-06 11:35:23','2020-11-06 11:35:23'),
	(5,'qwq','$2a$10$ISD4alRUVe3aWNysdoOEpukhnkTonJU2ybjUtQNExlD10hmjiX8XW','qwq',0,'110','d','d',3,'2020-11-03 08:38:30','2020-11-03 08:38:30'),
	(8,'qvq','$2a$10$Bm6dkLLB8NDZ9peC5IUe9u7gyXyPdjZ/FFq.fdTbhmlH16UYP1J9G','qvq',0,'110','test','test',1,'2020-11-21 05:19:45','2020-11-21 05:19:45');

/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
