ALTER TABLE `employee`
ADD UNIQUE INDEX `email_UNIQUE` (`email`,`last_name` ASC) VISIBLE;

alter table employee DROP foreign key position_ibfk_1;
ALTER TABLE employee ADD FOREIGN KEY (position_id) REFERENCES position (id) ON DELETE RESTRICT ON UPDATE RESTRICT;