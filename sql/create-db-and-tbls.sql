-- Active: 1729582617199@@127.0.0.1@3306@artemis
CREATE DATABASE requests
    DEFAULT CHARACTER SET = 'utf8mb4';

CREATE DATABASE artemis
    DEFAULT CHARACTER SET = 'utf8mb4';

USE requests

-- Active: 1729072828356@@172.17.0.2@3306@requests
CREATE TABLE requests (  
    id int NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT 'Primary Key',
    create_time DATETIME COMMENT 'Create Time',
    req_ref VARCHAR(255) COMMENT 'Request Reference',
    req_payload TEXT COMMENT 'Request payload'
) COMMENT '';

CREATE TABLE responses (  
    id int NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT 'Primary Key',
    create_time DATETIME COMMENT 'Create Time',
    resp_ref VARCHAR(255) COMMENT 'Response Reference',
    resp_payload TEXT COMMENT 'Response payload'
) COMMENT '';

CREATE USER 'spring'@'%' IDENTIFIED BY 'boot';

GRANT ALL PRIVILEGES ON *.* TO 'spring'@'%' WITH GRANT OPTION;

CREATE USER 'artemis'@'%' IDENTIFIED BY 'artemis';

GRANT ALL PRIVILEGES ON *.* TO 'artemis'@'%' WITH GRANT OPTION;