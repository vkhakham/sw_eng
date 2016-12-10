-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.0.67-community-nt


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema test
--

CREATE DATABASE IF NOT EXISTS test;
USE test;

--
-- Definition of table `flights`
--

DROP TABLE IF EXISTS `flights`;
CREATE TABLE `flights` (
  `num` int(10) unsigned NOT NULL auto_increment,
  `from` varchar(45) NOT NULL,
  `to` varchar(45) NOT NULL,
  `distance` int(10) unsigned NOT NULL,
  `price` float NOT NULL,
  PRIMARY KEY  (`num`)
) ENGINE=InnoDB AUTO_INCREMENT=7790 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `flights`
--

/*!40000 ALTER TABLE `flights` DISABLE KEYS */;
INSERT INTO `flights` (`num`,`from`,`to`,`distance`,`price`) VALUES 
 (2,'\"Los Angeles\"','\"Tokyo\"',5478,780),
 (7,'\"Los Angeles\"','\"Sydney\"',7487,1278),
 (13,'\"Los Angeles\"','\"Chicago\"',1749,220),
 (33,'\"Los Angeles\"','\"Honolulu\"',2551,375),
 (34,'\"Los Angeles\"','\"Honolulu\"',2551,425),
 (68,'\"Chicago\"','\"New York\"',802,202),
 (76,'\"Chicago\"','\"Los Angeles\"',1749,220),
 (99,'\"Los Angeles\"','\"Washington D.C.\"',2308,235),
 (149,'\"Pittsburgh\"','\"New York\"',303,116),
 (304,'\"Minneapolis\"','\"New York\"',991,101),
 (346,'\"Los Angeles\"','\"Dallas\"',1251,225),
 (387,'\"Los Angeles\"','\"Boston\"',2606,261),
 (701,'\"Detroit\"','\"New York\"',470,180),
 (702,'\"Madison\"','\"New York\"',789,202),
 (2223,'\"Madison\"','\"Pittsburgh\"',517,189),
 (4884,'\"Madison\"','\"Chicago\"',84,112),
 (5694,'\"Madison\"','\"Minneapolis\"',247,120),
 (7789,'\"Madison\"','\"Detroit\"',319,120);
/*!40000 ALTER TABLE `flights` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
