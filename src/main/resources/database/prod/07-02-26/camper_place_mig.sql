-- liquibase formatted sql
-- changeset 01szak:camper_place_mig

-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM camper_place
-- preconditions onFail:MARK_RAN

INSERT INTO camper_place (number, type, price, created_at, updated_at)
SELECT number, type, price, created_at, updated_at
FROM camper_places;
