--liquibase formatted sql
--changeset kacper:1
--validCheckSum 9:f1f6d8b2a004d5c92a046be7c10b3448
CREATE  TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_name varchar(400) not null
    );