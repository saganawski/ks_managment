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
    ('Unexcused Absence','UNEXCUSED_ABSENCE'),
    ('Shift Worked','SHIFT_WORKED'),
    ('Double Shift Worked','DOUBLE_SHIFT_WORKED'),
    ('Excused Absence','EXCUSED_ABSENCE'),
    ('Training Shift','TRAINING_SHIFT'),
    ('Director or Project Manager','DIRECTOR_OR_PROJECT_MANAGER');

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