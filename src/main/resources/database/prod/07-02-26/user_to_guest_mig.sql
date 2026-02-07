-- liquibase formatted sql

INSERT INTO guest (id, firstname, lastname, email, phone_number, car_registration, created_at, updated_at)
SELECT id, first_name, last_name, email, phone_number, car_registration, created_at, updated_at
FROM users
WHERE role = 'GUEST';
