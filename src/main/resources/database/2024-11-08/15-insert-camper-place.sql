--liquibase formatted sql
--changeset kacper:8
insert into camper_place(price)values (150);
