--liquibase formatted sql
--changeset kacper:9
--validCheckSum 9:9a240f5e1daf0fdb569ecfffc3431472
ALTER TABLE users
    ADD COLUMN occupied_place int not null;
