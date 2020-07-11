CREATE TABLE `interviewee` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `created_by` int DEFAULT '-1',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_by` int DEFAULT '-1',
  `updated_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


INSERT INTO interviewee
    (first_name,last_name,phone_number,email) VALUES
    ('Matthew','Barngrover','715-379-5767','mabarngrover@gmail.com'),
    ('Christopher','Herp','616-808-7594','merchmich@yahoo.com'),
    ('Marina','Flood','616-633-4573',''),
    ('Cheyenne','Cole','616-383-4243','chey.1712@gmail.com'),
    ('David','Minard','616-204-3832','dvd_minard@hotmail.com');