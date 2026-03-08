-- liquibase formatted sql

-- changeset 01szak:schema
-- validCheckSum: ANY
-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'app_user'
CREATE TABLE IF NOT EXISTS app_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    login VARCHAR(100) NOT NULL,
    username VARCHAR(100),
    email VARCHAR(150),
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT unique_app_user_email UNIQUE (email),
    UNIQUE KEY ux_employees_login (login)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- changeset 01szak:guest_table
-- validCheckSum: ANY
-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'guest'
CREATE TABLE IF NOT EXISTS guest (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    firstname VARCHAR(100),
    lastname VARCHAR(100),
    email VARCHAR(150),
    phone_number VARCHAR(50),
    car_registration VARCHAR(50),
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT unique_guest_email UNIQUE (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- changeset 01szak:camper_place_table
-- validCheckSum: ANY
-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'camper_place'
CREATE TABLE IF NOT EXISTS camper_place (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    number VARCHAR(50) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    created_at DATETIME,
    updated_at DATETIME,
    CHECK (price > 0),
    UNIQUE KEY ux_camper_places_number (number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- changeset 01szak:reservation_table
-- validCheckSum: ANY
-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'reservation'
CREATE TABLE IF NOT EXISTS reservation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    checkin DATE NOT NULL,
    checkout DATE NOT NULL,
    camper_place_id BIGINT NOT NULL,
    guest_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    is_paid BOOLEAN DEFAULT FALSE,
    price DECIMAL(10,2) NOT NULL,
    created_at DATETIME,
    updated_at DATETIME,
    CHECK (price > 0),
    CHECK (checkout > checkin),
    CONSTRAINT fk_reservations_camper_place
    FOREIGN KEY (camper_place_id)
    REFERENCES camper_place(id),
    CONSTRAINT fk_reservations_guest
    FOREIGN KEY (guest_id)
    REFERENCES guest(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- changeset 01szak:camper_place_type_table
-- validCheckSum: ANY
-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'camper_place_type'
CREATE TABLE IF NOT EXISTS camper_place_type (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type_name VARCHAR(255) NOT NULL,
    price DECIMAL(19,2) NOT NULL,
    created_at DATETIME,
    updated_at DATETIME,
    UNIQUE KEY ux_type_name (type_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- changeset 01szak:camper_place_type_col
-- validCheckSum: ANY
-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'camper_place' AND column_name = 'camper_place_type_id'
ALTER TABLE camper_place
    ADD COLUMN camper_place_type_id BIGINT;

-- changeset 01szak:camper_place_type_fk
-- validCheckSum: ANY
-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.key_column_usage WHERE table_schema = DATABASE() AND table_name = 'camper_place' AND constraint_name = 'fk_camper_place_type'
ALTER TABLE camper_place
    ADD CONSTRAINT fk_camper_place_type
    FOREIGN KEY (camper_place_type_id)
        REFERENCES camper_place_type(id)
        ON DELETE RESTRICT;
