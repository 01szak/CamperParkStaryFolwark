--liquibase formatted sql
--changeset kacper:1
CREATE  TABLE Roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_name varchar(400) not null
    );