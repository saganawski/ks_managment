ALTER TABLE employee
ADD COLUMN deleted TINYINT(1) DEFAULT false AFTER position_id;