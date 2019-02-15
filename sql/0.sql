-- Create syntax for TABLE 'todos'
CREATE TABLE `todos` (
  `id` varchar(64) NOT NULL,
  `user_id` varchar(64) NOT NULL,
  `item` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'CREATED,COMPLETED,DELETED',
  `created_at` bigint(11) NOT NULL,
  `updated_at` bigint(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `indx_email` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Create syntax for TABLE 'users'
CREATE TABLE `users` (
  `id` varchar(64) NOT NULL,
  `first_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `last_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `email` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `password` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `salt` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `token` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `created_at` bigint(11) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `indx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;