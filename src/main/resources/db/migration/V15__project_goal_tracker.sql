CREATE TABLE project (
  id int NOT NULL AUTO_INCREMENT,
  name varchar(255) DEFAULT NULL,
  completed tinyInt(1) DEFAULT FALSE,
  office_id int(11) DEFAULT NULL,
  created_by int(11) DEFAULT '-1',
  created_date datetime DEFAULT CURRENT_TIMESTAMP,
  updated_by int(11) DEFAULT '-1',
  updated_date datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (office_id) REFERENCES office(id) ON DELETE RESTRICT ON UPDATE RESTRICT
);


CREATE TABLE project_week (
  id int NOT NULL AUTO_INCREMENT,
  original_shift_goal int(11) DEFAULT NULL,
  current_shift_goal int(11) DEFAULT NULL,
  start_date date DEFAULT NULL,
  end_date date DEFAULT NULL,
  shifts_scheduled int(11) DEFAULT NULL,
  shifts_completed int(11) DEFAULT NULL,
  remaining_working_days int(11) DEFAULT NULL,
  shifts_needed DECIMAL(5,2) DEFAULT NULL,
  project_id int(11) NOT NULL,
  created_by int(11) DEFAULT '-1',
  created_date datetime DEFAULT CURRENT_TIMESTAMP,
  updated_by int(11) DEFAULT '-1',
  updated_date datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE RESTRICT ON UPDATE RESTRICT
);


