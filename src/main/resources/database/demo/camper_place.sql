-- liquibase formatted sql
-- changeset 01szak:camper_place_demo context:dev
-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM camper_place

INSERT INTO camper_place (id, number, price, camper_place_type_id, created_at, updated_at)
VALUES
    (1, '01', 50.00, 1, NOW(), NOW()),
    (2, '02', 80.00, 2, NOW(), NOW()),
    (3, '03', 120.00, 3, NOW(), NOW()),
    (4, '04', 30.00, 4, NOW(), NOW()),
    (5, '05', 100.00, 5, NOW(), NOW()),
    (6, '06', 50.00, 1, NOW(), NOW()),
    (7, '07', 80.00, 2, NOW(), NOW()),
    (8, '08', 120.00, 3, NOW(), NOW()),
    (9, '09', 30.00, 4, NOW(), NOW()),
    (10, '10', 100.00, 5, NOW(), NOW()),
    (11, '11', 50.00, 1, NOW(), NOW()),
    (12, '12', 80.00, 2, NOW(), NOW()),
    (13, '13', 120.00, 3, NOW(), NOW()),
    (14, '14', 30.00, 4, NOW(), NOW()),
    (15, '15', 100.00, 5, NOW(), NOW()),
    (16, '16', 50.00, 1, NOW(), NOW()),
    (17, '17', 80.00, 2, NOW(), NOW()),
    (18, '18', 120.00, 3, NOW(), NOW()),
    (19, '19', 30.00, 4, NOW(), NOW()),
    (20, '20', 100.00, 5, NOW(), NOW());
