--liquibase formatted sql
--changeset kacper:1
--validCheckSum 9:3b5a318b08441e4e1ec923fc1acd7337
CREATE  TABLE guests (
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 first_name varchar(400) not null,
 last_name varchar(400) not null,
 phone_number varchar(400) not null,
 email varchar(400) not null,
 car_registration varchar(400) not null,
 occupied_place ENUM('1','3','4','5','6','7','8','9') not null,
 role varchar(400) not null
);