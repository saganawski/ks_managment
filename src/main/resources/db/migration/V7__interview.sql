CREATE TABLE interview_confirmation_type (
  id int NOT NULL AUTO_INCREMENT,
  type varchar(255) DEFAULT NULL,
  code varchar(255) DEFAULT NULL,
  created_by int(11) DEFAULT '-1',
  created_date datetime DEFAULT CURRENT_TIMESTAMP,
  updated_by int(11) DEFAULT '-1',
  updated_date datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

insert into interview_confirmation_type
    (type,code) VALUES
    ('Email','EMAIL'),
    ('Call','CALL'),
    ('Text','TEXT'),
    ('Voicemail','VOICEMAIL'),
    ('Walk-In','WALK-IN');


CREATE TABLE interview_result (
  id int NOT NULL AUTO_INCREMENT,
  result varchar(255) DEFAULT NULL,
  code varchar(255) DEFAULT NULL,
  created_by int(11) DEFAULT '-1',
  created_date datetime DEFAULT CURRENT_TIMESTAMP,
  updated_by int(11) DEFAULT '-1',
  updated_date datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

INSERT INTO interview_result (result,code) VALUES
    ('Hired', 'HIRED'),
    ('Not Hired', 'NOT_HIRED'),
    ('Not Interested', 'No Show'),
    ('No Show', 'NO SHOW');


CREATE TABLE interview (
  id int NOT NULL AUTO_INCREMENT,
  application_id int(11) NOT NULL,
  scheduled_time datetime DEFAULT NULL,
  employee_id int(11) DEFAULT NULL,
  interview_confirmation_type_id int(11) DEFAULT NULL,
  interview_result_id int(11) DEFAULT NULL,
  created_by int DEFAULT '-1',
  created_date datetime DEFAULT CURRENT_TIMESTAMP,
  updated_by int DEFAULT '-1',
  updated_date datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY employee_id (employee_id),
  KEY interview_confirmation_type_id (interview_confirmation_type_id),
  KEY interview_result_id (interview_result_id),
  KEY application_id (application_id),
    CONSTRAINT interview_ibfk_1 FOREIGN KEY (employee_id) REFERENCES employee (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT interview_ibfk_2 FOREIGN KEY (interview_confirmation_type_id) REFERENCES interview_confirmation_type (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT interview_ibfk_3 FOREIGN KEY (interview_result_id) REFERENCES interview_result (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT interview_ibfk_4 FOREIGN KEY (application_id) REFERENCES application (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE `interview_director` (
  `employee_id` int NOT NULL,
  `interview_id` int NOT NULL,
  `created_by` int DEFAULT '-1',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_by` int DEFAULT '-1',
  `updated_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`employee_id`,`interview_id`),
  KEY `employee_id` (`employee_id`),
  KEY `interview_id` (`interview_id`),
  CONSTRAINT `interview_director_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `interview_director_ibfk_2` FOREIGN KEY (`interview_id`) REFERENCES `interview` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE interview_note(
    id int NOT NULL AUTO_INCREMENT,
    interview_id int(11) NOT NULL,
    note varchar(255) DEFAULT NULL,
    created_by int(11) DEFAULT '-1',
    created_date datetime DEFAULT CURRENT_TIMESTAMP,
    updated_by int(11) DEFAULT '-1',
    updated_date datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY interview_id (interview_id),
        CONSTRAINT fk_interview_id FOREIGN KEY (interview_id) REFERENCES interview (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);