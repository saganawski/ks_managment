CREATE TABLE employee_schedule (
    id int NOT NULL AUTO_INCREMENT,
    `employee_id` int NOT NULL,
    scheduled_time datetime DEFAULT NULL,
    `created_by` int DEFAULT '-1',
    `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
    `updated_by` int DEFAULT '-1',
    `updated_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (employee_id) REFERENCES employee(id) ON DELETE RESTRICT ON UPDATE RESTRICT
);