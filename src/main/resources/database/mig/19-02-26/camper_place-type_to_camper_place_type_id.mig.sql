-- liquibase formatted sql
-- changeset 01szak:camper_place_type_to_camper_place_type_id
-- validCheckSum: ANY
-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'camper_place' AND column_name = 'type'

UPDATE camper_place cp
    JOIN camper_place_type cpt
ON cp.type = cpt.type_name AND cp.price = cpt.price
    SET cp.camper_place_type_id = cpt.id;

ALTER TABLE camper_place
DROP COLUMN type;
