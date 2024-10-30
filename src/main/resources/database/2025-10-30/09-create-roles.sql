--liquibase formatted sql
--changeset kacper:4
CREATE  TABLE Roles (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        first_name varchar(400) not null,
                        last_name varchar(400) not null,
                        phone_number varchar(400) default null,
                        email varchar(400) not null,
                        car_registration varchar(400) default null,
                        role varchar(400) not null
);