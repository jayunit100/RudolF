SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `dojo` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci ;
USE `dojo` ;

-- -----------------------------------------------------
-- Table `dojo`.`assignedshifts`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `dojo`.`assignedshifts` (
  `bmrbid` INT NOT NULL ,
  `percpeaks` INT NOT NULL ,
  `percshifts` VARCHAR(45) NOT NULL ,
  `isfinal` TINYINT(1)  NOT NULL ,
  `atomid` INT NOT NULL ,
  `shift` DECIMAL(10,4) NOT NULL ,
  `error` DECIMAL(10,4) NOT NULL ,
  `atomname` VARCHAR(5) NOT NULL ,
  `resid` INT NOT NULL ,
  PRIMARY KEY (`bmrbid`, `percshifts`, `percpeaks`, `isfinal`, `atomname`, `resid`) )
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
