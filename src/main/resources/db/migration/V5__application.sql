CREATE TABLE application_contact_type (
  id int NOT NULL AUTO_INCREMENT,
  type varchar(255) DEFAULT NULL,
  code varchar(255) DEFAULT NULL,
  created_by int(11) DEFAULT '-1',
  created_date datetime DEFAULT CURRENT_TIMESTAMP,
  updated_by int(11) DEFAULT '-1',
  updated_date datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

insert into application_contact_type
    (type,code) VALUES
    ('App','APP'),
    ('Call','CALL');

CREATE TABLE application_source (
  id int NOT NULL AUTO_INCREMENT,
  source varchar(255) DEFAULT NULL,
  code varchar(255) DEFAULT NULL,
  created_by int(11) DEFAULT '-1',
  created_date datetime DEFAULT CURRENT_TIMESTAMP,
  updated_by int(11) DEFAULT '-1',
  updated_date datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

insert into application_source
    (source,code) VALUES
    ('Indeed','INDEED'),
    ('REFERRAL','REFERRALS'),
    ('Craigs List','CRAIGS_LIST'),
    ('BFS','BFS'),
    ('Intermatch','INTERMATCH'),
    ('Idealist','IDEALIST'),
    ('ZR','ZR'),
    ('EDU','EDU'),
    ('Events','EVENTS'),
    ('CJobs','CJOBS'),
    ('Left','LEFT');

CREATE TABLE application_result (
  id int NOT NULL AUTO_INCREMENT,
  result varchar(255) DEFAULT NULL,
  code varchar(255) DEFAULT NULL,
  created_by int(11) DEFAULT '-1',
  created_date datetime DEFAULT CURRENT_TIMESTAMP,
  updated_by int(11) DEFAULT '-1',
  updated_date datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);


insert into application_result
    (result,code) VALUES
    ('Scheduled','SCHEDULED'),
    ('Voice Mail','VOICE_MAIL'),
    ('Call Back','CALL_BACK'),
    ('No Answer','NO_ANSWER'),
    ('Not Interested','NOT_INTERESTED'),
    ('Weeded - No Convo','WEEDED_NO_CONVO'),
    ('Weeded - With Convo','WEEDED_WITH_CONVO'),
    ('See Notes','SEE_NOTES');

CREATE TABLE `application` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  date_received datetime DEFAULT NULL,
  call_back_date datetime DEFAULT NULL,
   application_contact_type_id int(11) DEFAULT NULL,
   application_source_id int(11) DEFAULT NULL,
   application_result_id int(11) DEFAULT NULL,
  `created_by` int DEFAULT '-1',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_by` int DEFAULT '-1',
  `updated_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY application_contact_type_id (application_contact_type_id),
  KEY application_source_id (application_source_id),
  KEY application_result_id (application_result_id),
    CONSTRAINT application_ibfk_1 FOREIGN KEY (application_contact_type_id) REFERENCES application_contact_type (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT application_ibfk_2 FOREIGN KEY (application_source_id) REFERENCES application_source (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT application_ibfk_3 FOREIGN KEY (application_result_id) REFERENCES application_result (id) ON UPDATE NO ACTION ON DELETE NO ACTION
);


INSERT INTO application
    (first_name,last_name,phone_number,email) VALUES
    ('Matthew','Barngrover','715-379-5767','mabarngrover@gmail.com'),
    ('Christopher','Herp','616-808-7594','merchmich@yahoo.com'),
    ('Marina','Flood','616-633-4573',''),
    ('Cheyenne','Cole','616-383-4243','chey.1712@gmail.com'),
    ('David','Minard','616-204-3832','dvd_minard@hotmail.com');