-- liquibase formatted sql
-- changeset 01szak:reservation_mig

-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM reservation

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

-- rollback DELETE FROM reservation WHERE id IN (SELECT id FROM reservations);
