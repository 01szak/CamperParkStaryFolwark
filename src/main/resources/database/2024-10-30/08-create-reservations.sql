--liquibase formatted sql
--changeset kacper:1
--validCheckSum 9:08937b2eae7e2f6788cb4d10aa809749
CREATE  TABLE reservation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    date_enter date not null,
    date_checkout date not null,
    user_id int not null,
    camper_place_id int not null
    );