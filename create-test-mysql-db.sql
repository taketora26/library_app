DROP DATABASE IF EXISTS `book_app_test`;
CREATE DATABASE `book_app_test` DEFAULT CHARSET utf8 COLLATE utf8_bin;
GRANT ALL PRIVILEGES ON `book_app_test`.* TO book_app@localhost IDENTIFIED BY 'book_app';