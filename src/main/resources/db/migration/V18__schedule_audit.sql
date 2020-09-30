CREATE TABLE schedule_audit(
  id int NOT NULL AUTO_INCREMENT,
  start_date date DEFAULT NULL,
  end_date date DEFAULT NULL,
  office_id int(11) DEFAULT NULL,
  created_by int(11) DEFAULT '-1',
  created_date datetime DEFAULT CURRENT_TIMESTAMP,
  updated_by int(11) DEFAULT '-1',
  updated_date datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (office_id) REFERENCES office(id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE schedule_audit_employee_schedule (
  schedule_audit_id int(11) NOT NULL,
  employee_schedule_id int(11) NOT NULL,
  created_by int DEFAULT '-1',
  created_date datetime DEFAULT CURRENT_TIMESTAMP,
  updated_by int DEFAULT '-1',
  updated_date datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (schedule_audit_id,employee_schedule_id),
  FOREIGN KEY (schedule_audit_id) REFERENCES schedule_audit (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  FOREIGN KEY (employee_schedule_id) REFERENCES employee_schedule (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);


