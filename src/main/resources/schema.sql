CREATE TABLE IF NOT EXISTS rsvp (
    passcode VARCHAR(5) PRIMARY KEY UNIQUE,
    dietary_restrictions VARCHAR(28)[],
    food_allergies VARCHAR(28)[],
    email VARCHAR(254) UNIQUE,
    name VARCHAR(28),
    attending BOOLEAN DEFAULT FALSE
);