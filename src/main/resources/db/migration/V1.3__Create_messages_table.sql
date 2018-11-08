CREATE TABLE `tbl_messages` (`id` bigint(20) NOT NULL AUTO_INCREMENT,
                             `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             `bundle_identifier` varchar(128) not null,
                             `device_token` TEXT NOT NULL,
                             `alert_body` TEXT NOT NULL,
                             PRIMARY KEY (`id`)
                          ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
