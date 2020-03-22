CREATE DATABASE IF NOT EXISTS `cab302`;
USE `cab302`;
CREATE TABLE IF NOT EXISTS `billboards` (`idbillboards` int(11) NOT NULL, PRIMARY KEY (`idbillboards`)) ENGINE=InnoDB DEFAULT CHARSET=latin1;
CREATE TABLE IF NOT EXISTS `schedules` (`idschedules` int(11) NOT NULL, PRIMARY KEY (`idschedules`)) ENGINE=InnoDB DEFAULT CHARSET=latin1;
CREATE TABLE IF NOT EXISTS `users` (`idusers` int(11) NOT NULL, PRIMARY KEY (`idusers`)) ENGINE=InnoDB DEFAULT CHARSET=latin1;