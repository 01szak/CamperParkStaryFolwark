-- liquibase formatted sql

-- changeset 01szak:camper_place_price_nullable
-- validCheckSum: ANY
-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'camper_place' AND column_name = 'price' AND is_nullable = 'NO'

ALTER TABLE camper_place MODIFY COLUMN price DECIMAL(10,2) NULL;

-- rollback ALTER TABLE camper_place MODIFY COLUMN price DECIMAL(10,2) NOT NULL;
