-- liquibase formatted sql
-- changeset kacper:1
delete from camper_park.users
    where id = 1;

insert into camper_park.users(first_name, last_name, email, password_hash, role)
    value ('Paweł', 'Olszewski', 'camperpark.sfolwark@gmail.com','$2y$10$.SkN425//TLbbYJi91hMNuYxbN5SLMjQq0EyFCX.i9oCuK.vT.d7u','ADMIN');
-- changeset kacper:2
insert into camper_park.camper_places(is_occupied,type,price,number)
values
    (default,'STANDARD',150.00,1),
    (default,'STANDARD',150.00,2),
    (default,'STANDARD',150.00,3),
    (default,'STANDARD',150.00,4),
    (default,'STANDARD',150.00,5),
    (default,'STANDARD',150.00,6),
    (default,'STANDARD',150.00,7),
    (default,'STANDARD',150.00,8),
    (default,'STANDARD',150.00,9),
    (default,'STANDARD',150.00,10),
    (default,'STANDARD',150.00,11),
    (default,'STANDARD',150.00,12),
    (default,'STANDARD',150.00,13),
    (default,'VIP',190.00,14),
    (default,'VIP',190.00,15),
    (default,'PLUS',170.00,16),
    (default,'PLUS',170.00,17),
    (default,'PLUS',170.00,18),
    (default,'PLUS',170.00,19),
    (default,'PLUS',170.00,20);

-- changeset kacper:3

ALTER TABLE reservations
    DROP FOREIGN KEY fk_reservation_user;

ALTER TABLE reservations
    ADD CONSTRAINT fk_reservation_user
        FOREIGN KEY (user_id) REFERENCES users (id)
            ON DELETE CASCADE;

ALTER TABLE reservations
    DROP FOREIGN KEY fk_reservation_camper_place;

ALTER TABLE reservations
    ADD CONSTRAINT fk_reservation_camper_place
        FOREIGN KEY (camper_place_id) REFERENCES camper_places (id)
            ON DELETE CASCADE;

-- changeset kacper:4

ALTER TABLE camper_places
    MODIFY COLUMN number VARCHAR(2) NOT NULL;
-- changeset kacper:5
ALTER TABLE users
    DROP INDEX uk_user_email;
-- changeset kacper:6
ALTER TABLE users DROP CONSTRAINT uk_user_email;
