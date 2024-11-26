-- liquibase formatted sql
-- changeset kacper:1
-- validCheckSum: 9:637036636767df5bdb843aee2d0351b8

CREATE TABLE users
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name       VARCHAR(50)  NOT NULL,
    last_name        VARCHAR(50)  NOT NULL,
    email            VARCHAR(100) NOT NULL,
    password_hash    VARCHAR(255) NOT NULL,
    phone_number     VARCHAR(20)  DEFAULT NULL,
    car_registration VARCHAR(20)  DEFAULT NULL,
    country          VARCHAR(50)  DEFAULT NULL,
    city             VARCHAR(50)  DEFAULT NULL,
    street_address   VARCHAR(100) DEFAULT NULL,
    role             VARCHAR(20)  DEFAULT 'GUEST',
    CONSTRAINT uk_user_email UNIQUE (email)
);

CREATE TABLE reservation
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    checkin        DATE   NOT NULL,
    checkout       DATE   NOT NULL,
    camperPlace_id BIGINT NOT NULL,
    user_id        BIGINT NOT NULL,
    CONSTRAINT fk_reservation_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_reservation_camper_place FOREIGN KEY (camperPlace_id) REFERENCES camper_places (id),
    CONSTRAINT chk_valid_dates CHECK (checkout > checkin),
    INDEX idx_reservation_user (user_id),
    INDEX idx_reservation_camper_place (camperPlace_id)
);

CREATE TABLE camper_places
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    is_occupied TINYINT(1) default 0 NOT NULL,
    type        VARCHAR(400)         NOT NULL,
    price       DECIMAL(10, 2)       NOT NULL,
    CONSTRAINT chk_price_positive CHECK (price >= 0),
    CONSTRAINT chk_valid_type CHECK (type IN ('STANDARD', 'PREMIUM', 'DELUXE'))
);

