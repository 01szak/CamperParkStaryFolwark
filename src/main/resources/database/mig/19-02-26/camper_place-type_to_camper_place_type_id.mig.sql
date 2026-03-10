-- liquibase formatted sql
-- changeset 01szak:camper_place_type_to_camper_place_type_id
-- validCheckSum: ANY
-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'camper_place' AND column_name = 'type'

-- 1. Przypisanie ID typu na podstawie nazwy typu (kolumna 'type')
UPDATE camper_place cp
    JOIN camper_place_type cpt ON cp.type = cpt.type_name
    SET cp.camper_place_type_id = cpt.id;

-- 2. Wyczyszczenie cen w camper_place, które są identyczne z cenami w camper_place_type
-- Dzięki temu parcele zaczną automatycznie dziedziczyć cenę z typu (model Template + Override)
UPDATE camper_place cp
    JOIN camper_place_type cpt ON cp.camper_place_type_id = cpt.id
    SET cp.price = NULL
WHERE cp.price = cpt.price;

-- 3. Usunięcie starej kolumny tekstowej
ALTER TABLE camper_place
DROP COLUMN type;
