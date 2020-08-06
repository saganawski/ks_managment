CREATE TABLE `user_employee` (
    id int NOT NULL AUTO_INCREMENT,
    `employee_id` int NOT NULL,
    `user_id` int NOT NULL,
    `created_by` int DEFAULT '-1',
    `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
    `updated_by` int DEFAULT '-1',
    `updated_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY `employee_id` (`employee_id`),
    KEY `user_id` (`user_id`),
    CONSTRAINT `user_employee_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `user_employee_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
);