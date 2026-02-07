-- liquibase formatted sql
-- changeset 01szak:user_to_guest_mig

-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM guest WHERE id IN (SELECT id FROM users WHERE role = 'GUEST')

INSERT INTO guest (id, firstname, lastname, email, phone_number, car_registration, created_at, updated_at)
SELECT id, first_name, last_name, email, phone_number, car_registration, created_at, updated_at
FROM users
WHERE role = 'GUEST';

-- rollback DELETE FROM guest WHERE id IN (SELECT id FROM users WHERE role = 'GUEST');