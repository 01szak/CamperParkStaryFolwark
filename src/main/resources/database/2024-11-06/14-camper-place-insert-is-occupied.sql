--liquibase formatted sql
--changeset kacper:8
--validCheckSum 9:32e2d2e47530bb70921a6b6cd5c151a4
ALTER TABLE camper_place
    ADD COLUMN is_occupied TINYINT(1) default 0;