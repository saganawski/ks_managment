CREATE TABLE `location` (
  `id` int NOT NULL AUTO_INCREMENT,
  `state_name` varchar(255) DEFAULT NULL,
  `city_name` varchar(255) DEFAULT NULL,
  `address_1` varchar(255) DEFAULT NULL,
  `address_2` varchar(255) DEFAULT NULL,
  `zip` varchar(255) DEFAULT NULL,
  `created_by` int DEFAULT '-1',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_by` int DEFAULT '-1',
  `updated_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

insert into location
	(state_name,city_name,address_1,zip) values
	('Michigan','Grand Rapids','301 Fuller Ave NE','49503'),
	('Michigan','Detroit','7375 Woodward Ave','48202'),
	('Michigan','Lansing','125 E Kalamazoo','48933');


CREATE TABLE `office` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `location_id` int DEFAULT NULL,
  `created_by` int DEFAULT '-1',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_by` int DEFAULT '-1',
  `updated_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `location_id` (`location_id`),
  CONSTRAINT `office_ibfk_1` FOREIGN KEY (`location_id`) REFERENCES `location` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

insert into office
    (name, location_id) VALUES
    ('Grand Rapids',1),
    ('Detroit',2),
    ('Lansing',3);
