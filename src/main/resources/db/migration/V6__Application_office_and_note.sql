CREATE TABLE application_note(
    id int NOT NULL AUTO_INCREMENT,
    application_id int(11) NOT NULL,
    note varchar(255) DEFAULT NULL,
    created_by int(11) DEFAULT '-1',
    created_date datetime DEFAULT CURRENT_TIMESTAMP,
    updated_by int(11) DEFAULT '-1',
    updated_date datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY application_id (application_id),
        CONSTRAINT fk_application_id FOREIGN KEY (application_id) REFERENCES application (id) ON UPDATE NO ACTION ON DELETE NO ACTION
);
-- add office as foreign key to application
ALTER TABLE application ADD COLUMN office_id INT(11) DEFAULT NULL AFTER application_result_id;
ALTER TABLE application ADD FOREIGN KEY (office_id) REFERENCES office (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

