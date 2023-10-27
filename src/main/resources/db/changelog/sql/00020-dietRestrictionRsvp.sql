--liquibase formatted sql
--changeset dillon:00020-dietRestrictionRsvp


-- Move columns from rsvp_group that are more appropriate to be referenced on a rsvp level.
ALTER TABLE rsvp_group DROP COLUMN dietary_restrictions;

ALTER TABLE rsvp_group DROP COLUMN food_allergies;

ALTER TABLE rsvp ADD COLUMN dietary_restrictions VARCHAR(28) ARRAY;

ALTER TABLE rsvp ADD COLUMN food_allergies VARCHAR(28) ARRAY;