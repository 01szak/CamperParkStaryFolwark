--liquibase formatted sql
--changeset kacper:3
--validCheckSum 9:66541ddd1ebcc18e791b44b15c473e03
CREATE TABLE camping_place (
    place_number BIGINT AUTO_INCREMENT PRIMARY KEY,
    is_occupied tinyint(1) default 0,
    check_out_date date default null
);