CREATE DATABASE `f5_lock` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `f5_lock`;

CREATE TABLE `product` (
    `id` bigint NOT NULL auto_increment,
    `name` varchar(255) NOT NULL,
    `price` bigint NOT NULL,
    `stock` bigint NOT NULL,
    `created_at` datetime NOT NULL,
    `updated_at` datetime NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
