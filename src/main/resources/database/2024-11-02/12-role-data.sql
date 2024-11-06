--liquibase formatted sql
--changeset kacper:6
--validCheckSum 9:67df7d0a46640c076144a0f4ecc14f21
insert into roles(role_name)values
('admin'),
('guests');

