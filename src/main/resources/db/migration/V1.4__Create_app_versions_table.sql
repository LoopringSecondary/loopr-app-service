CREATE TABLE `tbl_app_versions` (`id` bigint(20) NOT NULL AUTO_INCREMENT,
                                 `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	                               `version` varchar(128) not null,
                                 `must_update` boolean NOT NULL DEFAULT false,
                                 `description` TEXT NOT NULL,
                                 `current_installed_version` varchar(128) not null DEFAULT '0.0.1',
                                 PRIMARY KEY (`id`)
                                ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
