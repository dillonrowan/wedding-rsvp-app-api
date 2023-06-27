--liquibase formatted sql
--changeset dillon:00000-root


CREATE TABLE rsvp
(
    passcode VARCHAR(5) PRIMARY KEY UNIQUE,
    dietary_restrictions VARCHAR(28) ARRAY,
    food_allergies VARCHAR(28) ARRAY,
    email VARCHAR(254) UNIQUE,
    name VARCHAR(28),
    attending BOOLEAN DEFAULT FALSE,
    accompanying_guests INTEGER DEFAULT 0
);