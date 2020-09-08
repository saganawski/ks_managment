ALTER TABLE employee
ADD COLUMN deleted TINYINT(1) NULL AFTER position_id;