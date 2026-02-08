-- liquibase formatted sql
-- changeset schema:1
CREATE TABLE IF NOT EXISTS app_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    login VARCHAR(100) NOT NULL,
    username VARCHAR(100),
    email VARCHAR(150),
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,

    created_at DATETIME,
    updated_at DATETIME,

    CONSTRAINT  unique_email UNIQUE (email),

    UNIQUE KEY ux_employees_login (login)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS guest (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    firstname VARCHAR(100),
    lastname VARCHAR(100),
    email VARCHAR(150),
    phone_number VARCHAR(50),
    car_registration VARCHAR(50),

    created_at DATETIME,
    updated_at DATETIME,

    CONSTRAINT  unique_email UNIQUE (email)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS camper_place (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    number VARCHAR(50) NOT NULL,
    type VARCHAR(50) NOT NULL,
    price DECIMAL(10,2) NOT NULL,

    created_at DATETIME,
    updated_at DATETIME,

    CHECK (price > 0),

    UNIQUE KEY ux_camper_places_number (number)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


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
