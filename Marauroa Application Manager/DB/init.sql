SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';


-- -----------------------------------------------------
-- Table `marauroa_manager`.`application_type`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `marauroa_manager`.`application_type` (
  `id` INT NOT NULL ,
  `name` VARCHAR(45) NOT NULL ,
  `class` VARCHAR(255) NOT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) ,
  UNIQUE INDEX `class_UNIQUE` (`class` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `marauroa_manager`.`application`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `marauroa_manager`.`application` (
  `id` INT NOT NULL ,
  `application_type` INT NOT NULL ,
  `name` VARCHAR(45) NULL ,
  `path` VARCHAR(255) NULL ,
  `enabled` TINYINT(1)  NOT NULL DEFAULT 1 ,
  `version` VARCHAR(45) NOT NULL DEFAULT '1.0' ,
  PRIMARY KEY (`id`, `application_type`) ,
  INDEX `fk_application_application_type` (`application_type` ASC) ,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) ,
  UNIQUE INDEX `path_UNIQUE` (`path` ASC) ,
  CONSTRAINT `fk_application_application_type`
    FOREIGN KEY (`application_type` )
    REFERENCES `marauroa_manager`.`application_type` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `marauroa_manager`.`application_type`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
USE `marauroa_manager`;
INSERT INTO `marauroa_manager`.`application_type` (`id`, `name`, `class`) VALUES (1, 'Default Application', 'simple.marauroa.application.core.DefaultMarauroaApplication');

COMMIT;
