CREATE TABLE employee_note(
    id int NOT NULL AUTO_INCREMENT,
    employee_id int(11) NOT NULL,
    note varchar(255) DEFAULT NULL,
    created_by int(11) DEFAULT '-1',
    created_date datetime DEFAULT CURRENT_TIMESTAMP,
    updated_by int(11) DEFAULT '-1',
    updated_date datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (employee_id) REFERENCES employee(id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

ALTER TABLE employee
    ADD COLUMN start_date date DEFAULT NULL AFTER deleted,
    ADD COLUMN end_date date DEFAULT NULL AFTER start_date,
    ADD COLUMN voluntary TINYINT(1) DEFAULT NULL AFTER end_date;