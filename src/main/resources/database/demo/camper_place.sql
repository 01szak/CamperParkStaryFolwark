-- liquibase formatted sql
-- changeset 01szak:camper_place_demo context:dev

INSERT INTO camper_place (id, number, type, price, created_at, updated_at)
VALUES
    (1, '1', 'STANDARD', 150.00, NOW(), NOW()),
    (2, '2', 'VIP', 190.00, NOW(), NOW()),
    (3, '3', 'STANDARD', 150.00, NOW(), NOW()),
    (4, '4', 'PLUS', 170.00, NOW(), NOW());
