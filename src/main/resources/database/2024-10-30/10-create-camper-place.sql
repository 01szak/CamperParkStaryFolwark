--liquibase formatted sql
--changeset kacper:1
CREATE  TABLE Camper_place (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    price decimal not null
    );