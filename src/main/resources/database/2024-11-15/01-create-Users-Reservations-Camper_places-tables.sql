-- liquibase formatted sql
-- changeset kacper:1
-- validCheckSum: 9:637036636767df5bdb843aee2d0351b8

CREATE TABLE user (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      first_name VARCHAR(400) NOT NULL,
                      last_name VARCHAR(400) NOT NULL,
                      email VARCHAR(400) NOT NULL,
                      phone_number VARCHAR(400) DEFAULT NULL,
                      car_registration VARCHAR(400) DEFAULT NULL,
                      country VARCHAR(400) DEFAULT NULL,
                      city VARCHAR(400) DEFAULT NULL,
                      street_address VARCHAR(400) DEFAULT NULL,
                      role VARCHAR(400) DEFAULT 'GUEST'
);

CREATE TABLE reservation (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             checkin DATE NOT NULL,
                             checkout DATE NOT NULL,
                             camperPlace_id INT NOT NULL,
                             user_id INT NOT NULL
);

CREATE TABLE camperPlace (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             is_occupied TINYINT(1) default 0 NOT NULL,
                             type VARCHAR(400) NOT NULL,
                             price DOUBLE NOT NULL
                         );
