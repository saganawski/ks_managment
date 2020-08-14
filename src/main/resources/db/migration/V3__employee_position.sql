CREATE TABLE `position` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `created_by` int DEFAULT '-1',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_by` int DEFAULT '-1',
  `updated_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);

insert into position
    (name,code) values
    ('Owner','OWNER'),
    ('Director of Operations','DIRECTOR_OF_OPERATIONS'),
    ('Eastside Regional Director','EASTSIDE_REGIONAL_DIRECTOR'),
    ('Eastern Michigan Volunteer Coordinator','EASTERN_MICHIGAN_VOLUNTEER_COORDINATOR'),
    ('Regional Field Director','REGIONAL_FIELD_DIRECTOR'),
    ('Field Director','FIELD_DIRECTOR'),
    ('Statewide Volunteer Director','STATEWIDE_VOLUNTEER_DIRECTOR'),
    ('West Michigan Volunteer Coordinator','WEST_MICHIGAN_VOLUNTEER_COORDINATOR'),
    ('Central Michigan Volunteer Coordinator','CENTRAL_MICHIGAN_VOLUNTEER_COORDINATOR'),
    ('Volunteer Coordinator','VOLUNTEER COORDINATOR'),
    ('University of Michigan Campus Organizer','UNIVERSITY_OF_MICHIGAN_CAMPUS_ORGANIZER'),
    ('Canvasser','CANVASSER');

CREATE TABLE employee (
  id int NOT NULL AUTO_INCREMENT,
  first_name varchar(255) DEFAULT NULL,
  last_name varchar(255) DEFAULT NULL,
  alias varchar(255) DEFAULT NULL,
  email varchar(255) DEFAULT NULL,
  phone_number varchar(255) DEFAULT NULL,
  position_id int(11) DEFAULT NULL,
  created_by int(11) DEFAULT '-1',
  created_date datetime DEFAULT CURRENT_TIMESTAMP,
  updated_by int(11) DEFAULT '-1',
  updated_date datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY position_id (position_id),
  CONSTRAINT position_ibfk_1 FOREIGN KEY (position_id) REFERENCES position (id) ON UPDATE NO ACTION ON DELETE NO ACTION
);
insert into employee
    (first_name,last_name,email,phone_number) VALUES
    ('Mike', 'Kolehouse','mike@kolehousestrategies.com','616-293-6302'),
    ('James','Rankin','james@kolehousestrategies.com','720-352-3056'),
    ('Rafael','Mojica','raf@kolehousestrategies.com','810-875-1129'),
    ('Carol','Kang','carol@kolehousestrategies.com','616-633-8388'),
    ('John','James','john@kolehousestrategies.com','313-485-1788');