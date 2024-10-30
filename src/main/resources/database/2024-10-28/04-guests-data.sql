--liquibase formatted sql
--changeset kacper:2
--validCheckSum 9:1a4c3dd38950e04ec358e9f4aff3c422
INSERT INTO guests (id, first_name, last_name, phone_number, email, car_registration, occupied_place, role) VALUES
    (null, 'John', 'Doe', '123456789', 'john.doe@example.com', 'ABC123', 1, 'guest'),
    (null, 'Jane', 'Smith', '987654321', 'jane.smith@example.com', 'XYZ456', 2, 'guest'),
    (null, 'Alice', 'Johnson', '456123789', 'alice.johnson@example.com', 'DEF789', 3, 'guest'),
    (null, 'Bob', 'Brown', '789456123', 'bob.brown@example.com', 'GHI012', 4, 'guest'),
    (null, 'Charlie', 'Miller', '321654987', 'charlie.miller@example.com', 'JKL345', 5, 'guest'),
    (null, 'Eve', 'Davis', '654789321', 'eve.davis@example.com', 'MNO678', 6, 'guest'),
    (null, 'Frank', 'Wilson', '147258369', 'frank.wilson@example.com', 'PQR901', 7, 'guest'),
    (null, 'Grace', 'Taylor', '258369147', 'grace.taylor@example.com', 'STU234', 8, 'guest');
--     (null, 'Hank', 'Anderson', '369147258', 'hank.anderson@example.com', 'VWX567', 9, 'guest');
--     (null, 'Ivy', 'Thomas', '753159852', 'ivy.thomas@example.com', 'YZA890', 10, 'guest');
