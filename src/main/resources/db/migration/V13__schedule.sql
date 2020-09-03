CREATE TABLE employee_schedule_status (
  id int NOT NULL AUTO_INCREMENT,
  status varchar(255) DEFAULT NULL,
  code varchar(255) DEFAULT NULL,
  created_by int(11) DEFAULT '-1',
  created_date datetime DEFAULT CURRENT_TIMESTAMP,
  updated_by int(11) DEFAULT '-1',
  updated_date datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

insert into employee_schedule_status
    (status,code) VALUES
    ('Unexcused Absence','UNEXCUSED ABSENCE'),
    ('Shift Worked','Shift Worked'),
    ('Double Shift Worked','Double Shift Worked'),
    ('Excused Absence','Excused Absence'),
    ('Training Shift','Training Shift'),
    ('Director or Project Manager','Director or Project Manager');

CREATE TABLE employee_schedule (
    id int NOT NULL AUTO_INCREMENT,
    employee_id int(11) NOT NULL,
    scheduled_time datetime DEFAULT NULL,
    employee_schedule_status_id int(11) DEFAULT NULL,
    created_by int(11) DEFAULT '-1',
    created_date datetime DEFAULT CURRENT_TIMESTAMP,
    updated_by int(11) DEFAULT '-1',
    updated_date datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY (employee_id, scheduled_time),
    FOREIGN KEY (employee_id) REFERENCES employee(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    FOREIGN KEY (employee_schedule_status_id) REFERENCES employee_schedule_status(id) ON DELETE RESTRICT ON UPDATE RESTRICT
);