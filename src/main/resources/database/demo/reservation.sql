-- liquibase formatted sql
-- changeset 01szak:reservation_demo context:dev

INSERT INTO reservation (id, checkin, checkout, camper_place_id, guest_id, status, is_paid, price, created_at, updated_at)
VALUES
    (1, '2026-02-01', '2026-02-05', 1, 1, 'COMING', TRUE, 600.00, NOW(), NOW()),
    (2, '2026-03-10', '2026-03-12', 2, 2, 'COMING', FALSE, 200.00, NOW(), NOW()),
    (3, '2026-04-20', '2026-04-25', 3, 3, 'COMING', TRUE, 750.00, NOW(), NOW());
