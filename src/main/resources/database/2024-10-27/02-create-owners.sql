--liquibase formatted sql
--changeset kacper:1
CREATE TABLE OWNERS (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name varchar(400) not null,
    last_name varchar(400) not null,
    role varchar(400) not null

);