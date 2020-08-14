ALTER TABLE `interview`
DROP FOREIGN KEY `interview_ibfk_1`;
ALTER TABLE `interview`
CHANGE COLUMN `employee_id` `employee_id` INT NULL ;
ALTER TABLE `interview`
ADD CONSTRAINT `interview_ibfk_1`
  FOREIGN KEY (`employee_id`)
  REFERENCES `employee` (`id`)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT;