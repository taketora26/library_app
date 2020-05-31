DROP DATABASE IF EXISTS `book_app`;
CREATE DATABASE `book_app` DEFAULT CHARSET utf8 COLLATE utf8_bin;
CREATE USER 'book_app'@'%' identified BY 'book_app';
GRANT ALL ON `book_app`.* TO 'book_app'@'%' WITH GRANT OPTION ;
