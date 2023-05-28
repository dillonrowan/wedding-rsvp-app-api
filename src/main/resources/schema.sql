CREATE TABLE IF NOT EXISTS rsvp (
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    passcode VARCHAR(5) UNIQUE,
    dietary_restrictions VARCHAR(28)[],
    food_allergies VARCHAR(28)[],
    email VARCHAR(254) UNIQUE,
    name VARCHAR(28),
    attending BOOLEAN DEFAULT FALSE
);