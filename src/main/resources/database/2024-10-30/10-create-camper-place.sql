--liquibase formatted sql
--changeset kacper:1
--validCheckSum 9:536aa756c5de35209018bbb98c26b8ab
CREATE  TABLE camper_place (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    price decimal not null
    );