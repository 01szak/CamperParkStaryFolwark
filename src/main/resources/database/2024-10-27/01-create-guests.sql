--liquibase formatted sql
--changeset kacper:1
--validCheckSum 9:7624783a1f9f6c5460a4b4c05d28bf98
CREATE  TABLE GUESTS (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         first_name varchar(400) not null,
                         last_name varchar(400) not null,
                         phone_number varchar(400) not null,
                         email varchar(400) not null,
                         car_registration varchar(400) not null,
                         occupied_place int(4) not null,
                         role varchar(400) not null
);