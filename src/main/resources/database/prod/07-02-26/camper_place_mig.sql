-- liquibase formatted sql
-- changeset kacper:001

INSERT INTO camper_place (number, type, price, created_at, updated_at)
SELECT number, type, price, created_at, updated_at
FROM camper_places;
