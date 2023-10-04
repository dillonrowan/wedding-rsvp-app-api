DELETE FROM rsvp;
DELETE FROM rsvp_group;

-- group one
INSERT INTO rsvp_group (dietary_restrictions, food_allergies, email, group_lead) VALUES (ARRAY['NO_PORK'], ARRAY['DAIRY'], 'test1@test.com', 'John Smith');
INSERT INTO rsvp (name, attending, group_id) VALUES ('John Smith', false, (SELECT id FROM rsvp_group WHERE group_lead = 'John Smith'));
INSERT INTO rsvp (name, attending, group_id) VALUES ('Jane Doe', true, (SELECT id FROM rsvp_group WHERE group_lead = 'John Smith'));

-- group two
INSERT INTO rsvp_group (dietary_restrictions, food_allergies, email, group_lead) VALUES (ARRAY['NO_RED_MEAT', 'NO_CHICKEN'], ARRAY['PEANUTS'], 'test2@test.com', 'Homer Simpson');
INSERT INTO rsvp (name, attending, group_id) VALUES ('Homer Simpson', true, (SELECT id FROM rsvp_group WHERE group_lead = 'Homer Simpson'));
INSERT INTO rsvp (name, attending, group_id) VALUES ('Marge Simpson', true, (SELECT id FROM rsvp_group WHERE group_lead = 'Homer Simpson'));
INSERT INTO rsvp (name, attending, group_id) VALUES ('Lisa Simpson', false, (SELECT id FROM rsvp_group WHERE group_lead = 'Homer Simpson'));
INSERT INTO rsvp (name, attending, group_id) VALUES ('Bart Simpson', false, (SELECT id FROM rsvp_group WHERE group_lead = 'Homer Simpson'));
