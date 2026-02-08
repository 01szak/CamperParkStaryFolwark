-- liquibase formatted sql
-- changeset 01szak:guest_demo context:dev


INSERT INTO guest (id, firstname, lastname, email, phone_number, car_registration, created_at, updated_at)
VALUES
    (1, 'Kacper', 'Olszewski', 'kacper@example.com', '123456789', 'DW12345', NOW(), NOW()),
    (2, 'Anna', 'Nowak', 'anna@example.com', '987654321', 'KR98765', NOW(), NOW()),
    (3, 'Marek', 'Kowalski', 'marek@example.com', '555444333', 'WA55555', NOW(), NOW());
