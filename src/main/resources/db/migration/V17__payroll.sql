CREATE TABLE employee_schedule_payroll (
    id int NOT NULL AUTO_INCREMENT,
    pay_rate DECIMAL(6,2) DEFAULT NULL,
    time_in time DEFAULT NULL,
    time_out time DEFAULT NULL,
    lunch tinyint(1) DEFAULT FALSE,
    overtime tinyint(1) DEFAULT FALSE,
    total_minutes DECIMAL(6,2) DEFAULT NULL,
    mileage int(11) DEFAULT NULL,
    total_day_wage DECIMAL(7,2) DEFAULT NULL,
    created_by int(11) DEFAULT '-1',
    created_date datetime DEFAULT CURRENT_TIMESTAMP,
    updated_by int(11) DEFAULT '-1',
    updated_date datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

ALTER TABLE employee_schedule ADD COLUMN employee_schedule_payroll_id int(11) DEFAULT NULL AFTER employee_schedule_status_id;
ALTER TABLE employee_schedule ADD FOREIGN KEY (employee_schedule_payroll_id) REFERENCES employee_schedule_payroll(id) ON DELETE RESTRICT ON UPDATE RESTRICT;