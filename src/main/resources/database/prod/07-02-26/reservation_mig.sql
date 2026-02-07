-- liquibase formatted sql
-- changeset kacper:002

INSERT INTO reservation (
    id, checkin, checkout, camper_place_id, guest_id,
    status, is_paid, price, created_at, updated_at
)
SELECT
    id,
    checkin,
    checkout,
    camper_place_id,
    user_id,
    status,
    is_paid,
    price,
    created_at,
    updated_at
FROM reservations;
