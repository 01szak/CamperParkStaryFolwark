--liquibase formatted sql
--changeset kacper:9
ALTER TABLE camper_place
    ADD COLUMN user_id int default null;