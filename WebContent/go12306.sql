-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.1.37 - Source distribution
-- Server OS:                    Win32
-- HeidiSQL version:             7.0.0.4053
-- Date/time:                    2013-01-17 18:34:47
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET FOREIGN_KEY_CHECKS=0 */;

-- Dumping structure for table go12306.url
CREATE TABLE IF NOT EXISTS `url` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `userid` int(10) NOT NULL DEFAULT '0',
  `url` text,
  `comment` varchar(255) DEFAULT NULL,
  `state` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- Dumping data for table go12306.url: 1 rows
/*!40000 ALTER TABLE `url` DISABLE KEYS */;
INSERT INTO `url` (`id`, `userid`, `url`, `comment`, `state`) VALUES
	(1, 1, 'http://dynamic.12306.cn/otsweb/order/querySingleAction.do?method=queryLeftTicket&orderRequest.train_date=2013-01-17&orderRequest.from_station_telecode=GZQ&orderRequest.to_station_telecode=CSQ&orderRequest.train_no=&trainPassType=QB&trainClass=QB%23D%23Z%23T%23K%23QT%23&includeStudent=00&seatTypeAndNum=&orderRequest.start_time_str=00%3A00--24%3A00', '深圳 - 益阳', 1);
/*!40000 ALTER TABLE `url` ENABLE KEYS */;


-- Dumping structure for table go12306.user
CREATE TABLE IF NOT EXISTS `user` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `state` varchar(50) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- Dumping data for table go12306.user: 1 rows
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`id`, `username`, `password`, `email`, `state`) VALUES
	(1, 'admin@love320.com', 'admin@love320.com', 'admin@love320.com', '1');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
/*!40014 SET FOREIGN_KEY_CHECKS=1 */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
