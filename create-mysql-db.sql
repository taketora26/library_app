DROP DATABASE IF EXISTS `book_app`;
CREATE DATABASE `book_app` DEFAULT CHARSET utf8 COLLATE utf8_bin;
GRANT ALL PRIVILEGES ON `book_app`.* TO book_app@localhost IDENTIFIED BY 'book_app';
