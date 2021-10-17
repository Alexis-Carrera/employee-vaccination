
INSERT INTO `role` (`role_id`, `description`, `role_type`) VALUES ('1', 'Admin', 'ADMIN');
INSERT INTO `role` (`role_id`, `description`, `role_type`) VALUES ('2', 'User', 'USER');

INSERT INTO `employee` (`id`, `address`, `date_of_birth`, `email`, `last_names`, `mobile_phone_number`, `names`, `national_id_number`, `password`, `username`,`vaccination_status`,`employee_vaccination_info_id`) VALUES ('1', null, null, 'admin@gmail.com', 'Admin', null, 'Super', '0000000000', '$2a$10$zXLRvGA1qZj2K4RoAhfbrOGdCAnhDNYJq7yiKWgxubPJLSgIG/mu6', 'admin',null,null);
INSERT INTO `employee_roles` (`id`, `role_id`) VALUES ('1', '1');
