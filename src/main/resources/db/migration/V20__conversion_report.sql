CREATE TABLE conversion(
  id int NOT NULL AUTO_INCREMENT,
  start_date date DEFAULT NULL,
  end_date date DEFAULT NULL,
  office_id int(11) DEFAULT NULL,
  total_applications int(11) DEFAULT NULL,
  total_interviews_scheduled int(11) DEFAULT NULL,
  total_interviews_show int(11) DEFAULT NULL,
  total_hires int(11) DEFAULT NULL,
  created_by int(11) DEFAULT '-1',
  created_date datetime DEFAULT CURRENT_TIMESTAMP,
  updated_by int(11) DEFAULT '-1',
  updated_date datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (office_id) REFERENCES office(id) ON DELETE RESTRICT ON UPDATE RESTRICT
);


