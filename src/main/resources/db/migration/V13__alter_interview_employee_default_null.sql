ALTER TABLE `ks_management`.`interview`
DROP FOREIGN KEY `interview_ibfk_1`;
ALTER TABLE `ks_management`.`interview`
CHANGE COLUMN `employee_id` `employee_id` INT NULL ;
ALTER TABLE `ks_management`.`interview`
ADD CONSTRAINT `interview_ibfk_1`
  FOREIGN KEY (`employee_id`)
  REFERENCES `ks_management`.`employee` (`id`)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT;