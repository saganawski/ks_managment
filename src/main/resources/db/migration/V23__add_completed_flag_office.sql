ALTER TABLE office
ADD COLUMN is_completed tinyInt(1) DEFAULT FALSE AFTER location_id;