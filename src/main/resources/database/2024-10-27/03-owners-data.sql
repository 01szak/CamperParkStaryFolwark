--liquibase formatted sql
--changeset kacper:1
--validCheckSum 9:434c1920e27a1cbc12e7e3f047cac6da
INSERT INTO owners (id, first_name,last_name,role)
VALUES(null,'Kacper','Olszewski','owner')