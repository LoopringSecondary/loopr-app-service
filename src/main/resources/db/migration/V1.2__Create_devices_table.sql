CREATE TABLE `tbl_devices` (`id` bigint(20) NOT NULL AUTO_INCREMENT,
                            `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `address` varchar(1024) not null,
                          	`bundle_identifier` varchar(128) not null,
                          	`device_token` TEXT NOT NULL,
                            `is_enabled` boolean NOT NULL DEFAULT true,
                            `is_release_mode` boolean NOT NULL DEFAULT false,
                            PRIMARY KEY (`id`)
                          ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
