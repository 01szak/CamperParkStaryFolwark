-- liquibase formatted sql
-- changeset 01szak:camper_place_demo context:dev

INSERT INTO camper_place (id, number, type, price, created_at, updated_at)
VALUES
    (1, '1', 'STANDARD', 150.00, NOW(), NOW()),
    (2, '2', 'VIP', 190.00, NOW(), NOW()),
    (3, '3', 'STANDARD', 150.00, NOW(), NOW()),
    (4, '4', 'PLUS', 170.00, NOW(), NOW()),
    (5,  '5',  'STANDARD', 150.00, NOW(), NOW()),
    (6,  '6',  'PLUS',     170.00, NOW(), NOW()),
    (7,  '7',  'VIP',      190.00, NOW(), NOW()),
    (20,  '7a',  'STANDARD', 150.00, NOW(), NOW()),
    (8,  '8',  'STANDARD', 150.00, NOW(), NOW()),
    (9,  '9',  'PLUS',     170.00, NOW(), NOW()),
    (10, '10', 'VIP',      190.00, NOW(), NOW()),
    (11, '11', 'STANDARD', 150.00, NOW(), NOW()),
    (12, '12', 'PLUS',     170.00, NOW(), NOW()),
    (13, '13', 'VIP',      190.00, NOW(), NOW()),
    (14, '14', 'STANDARD', 150.00, NOW(), NOW()),
    (15, '15', 'PLUS',     170.00, NOW(), NOW()),
    (16, '16', 'VIP',      190.00, NOW(), NOW()),
    (17, '17', 'STANDARD', 150.00, NOW(), NOW()),
    (18, '18', 'PLUS',     170.00, NOW(), NOW()),
    (19, '19', 'VIP',      190.00, NOW(), NOW());
