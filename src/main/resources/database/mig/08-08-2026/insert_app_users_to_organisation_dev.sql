-- liquibase formatted sql
-- changeset 01szak:insert_app_users_to_organisation_dev
-- validCheckSum: ANY
-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'app_user' AND column_name = 'organisation_id'

UPDATE app_user SET organisation_id = 1 WHERE organisation_id = NULL;