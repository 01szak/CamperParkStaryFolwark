-- liquibase formatted sql
-- changeset 01szak:camper_place_mig

-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM camper_place

INSERT INTO camper_place (id, number, type, price, created_at, updated_at)
SELECT id, number, type, price, created_at, updated_at
FROM camper_places;

-- rollback DELETE FROM camper_place;