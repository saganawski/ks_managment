CREATE TABLE employee_schedule_payroll (
    id int NOT NULL AUTO_INCREMENT,
    employee_schedule_id int(11) NOT NULL,
    pay_rate DECIMAL(6,2) DEFAULT NULL,
    time_in datetime DEFAULT NULL,
    time_out datetime DEFAULT NULL,
    lunch tinyint(1) DEFAULT FALSE,
    overtime tinyint(1) DEFAULT FALSE,
    total_hours DECIMAL(6,2) DEFAULT NULL,
    mileage int(11) DEFAULT NULL,
    total_day_wage DECIMAL(7,2) DEFAULT NULL,
    created_by int(11) DEFAULT '-1',
    created_date datetime DEFAULT CURRENT_TIMESTAMP,
    updated_by int(11) DEFAULT '-1',
    updated_date datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (employee_schedule_id) REFERENCES employee_schedule(id) ON DELETE RESTRICT ON UPDATE RESTRICT
);