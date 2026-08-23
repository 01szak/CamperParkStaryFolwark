-- liquibase formatted sql
-- changeset 01szak:create_organisation_dev.sql
-- validCheckSum: ANY
-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'organisation'
INSERT INTO organisation(owner_id, organisation_name, address, created_at, updated_at, web_app_api_key) VALUES (1, 'Camperpark Stary Folwark', 'Stary Folwark 12', NOW(), NOW(), '123abc')
