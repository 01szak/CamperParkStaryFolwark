--liquibase formatted sql
--changeset kacper:1
--validCheckSum 9:ab5c36ed05e521006bf582700040fd6b
CREATE TABLE owners (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name varchar(400) not null,
    last_name varchar(400) not null,
    role varchar(400) not null

);