CREATE TABLE `employee_office` (
  `employee_id` int NOT NULL,
  `office_id` int NOT NULL,
  `created_by` int DEFAULT '-1',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_by` int DEFAULT '-1',
  `updated_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`employee_id`,`office_id`),
  KEY `employee_id` (`employee_id`),
  KEY `office_id` (`office_id`),
  CONSTRAINT `employee_office_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT `employee_office_ibfk_2` FOREIGN KEY (`office_id`) REFERENCES `office` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;