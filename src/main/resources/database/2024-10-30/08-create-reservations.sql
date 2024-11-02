--liquibase formatted sql
--changeset kacper:1
CREATE  TABLE Reservation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    date_enter date not null,
    date_checkout date not null,
    user_id int not null,
    camper_place_id int not null
    );