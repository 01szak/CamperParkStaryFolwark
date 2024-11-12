--liquibase formatted sql
--changeset kacper:9
ALTER TABLE users
    ADD COLUMN reservation_id int not null;