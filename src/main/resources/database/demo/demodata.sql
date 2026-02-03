-- liquibase formatted sql
-- changeset demodata:1

-- Demo Users
INSERT INTO app_user (id, login, username, email, password, role, created_at, updated_at)
VALUES
    (1, 'capitan', 'capitan', 'capitan@example.com', 'cpsf', 'ADMIN', NOW(), NOW()),
    (2, 'deckhand', 'deckhand', 'deckhand@example.com', 'cpsf', 'EMPLOYEE', NOW(), NOW());

-- Demo Guests
INSERT INTO guest (id, firstname, lastname, email, phone_number, car_registration, created_at, updated_at)
VALUES
    (1, 'Kacper', 'Olszewski', 'kacper@example.com', '123456789', 'DW12345', NOW(), NOW()),
    (2, 'Anna', 'Nowak', 'anna@example.com', '987654321', 'KR98765', NOW(), NOW()),
    (3, 'Marek', 'Kowalski', 'marek@example.com', '555444333', 'WA55555', NOW(), NOW());

-- Demo Camper Places
INSERT INTO camper_place (id, number, type, price, created_at, updated_at)
VALUES
    (1, '1', 'STANDARD', 150.00, NOW(), NOW()),
    (2, '2', 'VIP', 190.00, NOW(), NOW()),
    (3, '3', 'STANDARD', 150.00, NOW(), NOW()),
    (4, '4', 'PLUS', 170.00, NOW(), NOW());

-- Demo Reservations
INSERT INTO reservation (id, checkin, checkout, camper_place_id, guest_id, status, is_paid, price, created_at, updated_at)
VALUES
    (1, '2026-02-01', '2026-02-05', 1, 1, 'COMING', TRUE, 600.00, NOW(), NOW()),
    (2, '2026-03-10', '2026-03-12', 2, 2, 'COMING', FALSE, 200.00, NOW(), NOW()),
    (3, '2026-04-20', '2026-04-25', 3, 3, 'COMING', TRUE, 750.00, NOW(), NOW());
