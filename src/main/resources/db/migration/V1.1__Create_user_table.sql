create table `tbl_users` (`id` bigint(20) NOT NULL AUTO_INCREMENT,
                          `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          `account_token` varchar(255) NOT NULL,
                          `config` JSON NOT NULL,
                          `is_deleted` BOOL NOT NULL DEFAULT FALSE,
                          PRIMARY KEY (`id`)
                          ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;

