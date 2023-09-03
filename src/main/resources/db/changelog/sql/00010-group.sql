--liquibase formatted sql
--changeset dillon:00010-group

-- Create group table to group together rsvp records. One to many relationship of group to rsvp records.
CREATE TABLE rsvp_group
(
    id SERIAL PRIMARY KEY,
    modify_group BOOLEAN DEFAULT FALSE,
    dietary_restrictions VARCHAR(28) ARRAY,
    food_allergies VARCHAR(28) ARRAY,
    email VARCHAR(254),
    group_lead VARCHAR(28)
);

ALTER TABLE rsvp
ADD COLUMN group_id INTEGER REFERENCES rsvp_group(id) default NULL;

-- Move columns from rsvp that are more appropriate to be referenced on a group level.
ALTER TABLE rsvp
DROP COLUMN dietary_restrictions;

ALTER TABLE rsvp
DROP COLUMN food_allergies;

ALTER TABLE rsvp
DROP COLUMN email;

-- Remove the accompanying_guests as this is not needed.
ALTER TABLE rsvp
DROP COLUMN accompanying_guests;

-- Remove the need for passcode as fetching rsvp by passcode is no longer desired. Use serial id for uniqueness.
ALTER TABLE rsvp
DROP COLUMN passcode;

ALTER TABLE rsvp
ADD COLUMN id SERIAL PRIMARY KEY;
