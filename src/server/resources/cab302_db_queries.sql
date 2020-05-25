CREATE DATABASE IF NOT EXISTS `cab302`;
USE `cab302`;
CREATE TABLE IF NOT EXISTS `users` (`idUsers` INT(11) NOT NULL AUTO_INCREMENT, `user` VARCHAR(50) NOT NULL DEFAULT '', `pass` VARCHAR(89) NOT NULL DEFAULT '', `idPermissions` INT(11) NULL DEFAULT NULL, `fName` VARCHAR(50) NULL DEFAULT NULL, `lName` VARCHAR(50) NULL DEFAULT NULL, PRIMARY KEY (`idUsers`), INDEX `FK_users_permissions` (`idPermissions`)) ENGINE=InnoDB DEFAULT CHARSET=latin1;
CREATE TABLE IF NOT EXISTS `billboards` (`idBillboards` INT(11) NOT NULL AUTO_INCREMENT, `billboardName` VARCHAR(100) NOT NULL DEFAULT '', `userId` INT(11) NOT NULL, `dateMade` TIMESTAMP NOT NULL DEFAULT current_timestamp(), `dateModify` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE current_timestamp(), `fileLocation` VARCHAR(400) NOT NULL DEFAULT '', PRIMARY KEY (`idBillboards`), INDEX `FK_billboards_users` (`userId`)) ENGINE=InnoDB DEFAULT CHARSET=latin1;
CREATE TABLE IF NOT EXISTS `schedules` (`idSchedules` INT(11) NOT NULL AUTO_INCREMENT, `weekday` VARCHAR(50) NOT NULL, `duration` INT(11) NOT NULL DEFAULT 1, `startTime` TIME NULL DEFAULT NULL, `idBillboard` INT(11) NOT NULL DEFAULT 0, `userId` INT(11) NOT NULL, PRIMARY KEY (`idSchedules`), INDEX `FK_schedules_billboards` (`idBillboard`), INDEX `FK_schedules_users` (`userId`)) ENGINE=InnoDB DEFAULT CHARSET=latin1;
CREATE TABLE IF NOT EXISTS `permissions` (`idPermissions` INT(11) NOT NULL AUTO_INCREMENT,`permissionName` VARCHAR(50) NOT NULL, PRIMARY KEY (`idPermissions`)) ENGINE=InnoDB DEFAULT CHARSET=latin1;
ALTER TABLE `billboards` ADD CONSTRAINT `FK_billboards_users` FOREIGN KEY (`userId`) REFERENCES `users` (`idUsers`) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE `schedules` ADD CONSTRAINT `FK_schedules_billboards` FOREIGN KEY (`idBillboard`) REFERENCES `billboards` (`idBillboards`);
ALTER TABLE `schedules` ADD CONSTRAINT `FK_schedules_users` FOREIGN KEY (`userId`) REFERENCES `users` (`idUsers`);
ALTER TABLE `users` ADD CONSTRAINT `FK_users_permissions` FOREIGN KEY (`idPermissions`) REFERENCES `permissions` (`idPermissions`) ON UPDATE SET NULL ON DELETE SET NULL;
INSERT INTO `cab302`.`users` (`idUsers`, `user`, `pass`, `fName`, `lName`) VALUES ('1', 'admin', 'TNBeZf5SCG+QzxWQt21TxY0orQOFZXUY6S0RHs7/TOA=$MrRy4VfAGiWGVBahO1o0iTTXjLnNhDF+OsX9Sgbzwu0=', 'John', 'Smith');
