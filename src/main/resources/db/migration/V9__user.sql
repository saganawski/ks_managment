CREATE TABLE user (
  id int NOT NULL AUTO_INCREMENT,
  username varchar(255) UNIQUE NOT NULL,
  password varchar(255) NOT NULL,
  is_active tinyint(1) DEFAULT 1,
  roles varchar(255) DEFAULT NULL,
  permissions varchar(255) DEFAULT NULL,
  created_by int(11) DEFAULT '-1',
  created_date datetime DEFAULT CURRENT_TIMESTAMP,
  updated_by int(11) DEFAULT '-1',
  updated_date datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);