CREATE TABLE training_confirmation_type (
  id int NOT NULL AUTO_INCREMENT,
  type varchar(255) DEFAULT NULL,
  code varchar(255) DEFAULT NULL,
  created_by int(11) DEFAULT '-1',
  created_date datetime DEFAULT CURRENT_TIMESTAMP,
  updated_by int(11) DEFAULT '-1',
  updated_date datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

insert into training_confirmation_type
    (type,code) VALUES
    ('Email','EMAIL'),
    ('Call','CALL'),
    ('Text','TEXT'),
    ('Voicemail','VOICEMAIL');


CREATE TABLE training (
  id int NOT NULL AUTO_INCREMENT,
  application_id int(11) NOT NULL,
  interview_id int(11) NOT NULL,
  scheduled_time datetime DEFAULT NULL,
  employee_id int(11) DEFAULT NULL,
  training_confirmation_type_id int(11) DEFAULT NULL,
  has_show tinyint(1) DEFAULT NULL,
  created_by int DEFAULT '-1',
  created_date datetime DEFAULT CURRENT_TIMESTAMP,
  updated_by int DEFAULT '-1',
  updated_date datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY employee_id (employee_id),
  KEY training_confirmation_type_id (training_confirmation_type_id),
  KEY application_id (application_id),
  KEY interview_id (interview_id),
    CONSTRAINT training_ibfk_1 FOREIGN KEY (employee_id) REFERENCES employee (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT training_ibfk_2 FOREIGN KEY (training_confirmation_type_id) REFERENCES training_confirmation_type (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT training_ibfk_3 FOREIGN KEY (application_id) REFERENCES application (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT training_ibfk_4 FOREIGN KEY (interview_id) REFERENCES interview (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);


CREATE TABLE training_note(
    id int NOT NULL AUTO_INCREMENT,
    training_id int(11) NOT NULL,
    note varchar(255) DEFAULT NULL,
    created_by int(11) DEFAULT '-1',
    created_date datetime DEFAULT CURRENT_TIMESTAMP,
    updated_by int(11) DEFAULT '-1',
    updated_date datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY training_id (training_id),
        CONSTRAINT fk_training_id FOREIGN KEY (training_id) REFERENCES training (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);
