-- liquibase formatted sql
-- changeset kacper:1
CREATE TABLE demo_camper_park_sf.users
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

CREATE TABLE demo_camper_park_sf.camper_places
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    is_occupied BOOLEAN  NOT NULL default FALSE,
    type        VARCHAR(400)          NOT NULL,
    price       DECIMAL(10, 2)        NOT NULL,
    CONSTRAINT chk_price_positive CHECK (price >= 0),
    CONSTRAINT chk_valid_type CHECK (type IN ('STANDARD', 'VIP', 'PLUS'))
);

CREATE TABLE demo_camper_park_sf.reservations
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    checkin         DATE        NOT NULL,
    checkout        DATE        NOT NULL,
    camper_place_id BIGINT      NOT NULL,
    user_id         BIGINT      NOT NULL,
    status          VARCHAR(10) NOT NULL DEFAULT 'COMING',
    CONSTRAINT fk_reservation_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_reservation_camper_place FOREIGN KEY (camper_place_id) REFERENCES camper_places (id),
    CONSTRAINT chk_valid_dates CHECK (checkout > checkin),
    INDEX idx_reservation_user (user_id),
    INDEX idx_reservation_camper_place (camper_place_id),
    CONSTRAINT chk_valid_status CHECK (status IN ('EXPIRED', 'ACTIVE', 'COMING'))
);
-- changeset kacper:2
-- validCheckSum: 9:054846e02e4e354624b21659cfb4bec5
insert into users(first_name,last_name,email,password_hash,role)value ('test','test','test','$2a$10$2DwqzkRT5DSeSOUjKkJe/.QPXCDj6JZRePHmd2YNmtKEBBoRYgrWO','ADMIN');

-- changeset kacper:3
-- validCheckSum: 9:de39d94f7b173dfc055cbce6287ed736
alter table camper_places add column number Integer not null;

-- changeset kacper:4
ALTER TABLE users MODIFY COLUMN password_hash VARCHAR(255) DEFAULT NULL;

