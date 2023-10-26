DELETE FROM rsvp;
DELETE FROM rsvp_group;

-- group one
INSERT INTO rsvp_group (email, group_lead) VALUES ('test1@test.com', 'John Smith');
INSERT INTO rsvp (dietary_restrictions, food_allergies, name, attending, group_id) VALUES (ARRAY['NO_PORK'], ARRAY['DAIRY'], 'John Smith', false, (SELECT id FROM rsvp_group WHERE group_lead = 'John Smith'));
INSERT INTO rsvp (dietary_restrictions, food_allergies, name, attending, group_id) VALUES (ARRAY['NO_PORK'], ARRAY['DAIRY'], 'Jane Doe', false, (SELECT id FROM rsvp_group WHERE group_lead = 'John Smith'));

-- group two
INSERT INTO rsvp_group (email, group_lead) VALUES ('test2@test.com', 'Homer Simpson');
INSERT INTO rsvp (dietary_restrictions, food_allergies, name, attending, group_id) VALUES (ARRAY['NO_RED_MEAT', 'NO_CHICKEN'], ARRAY['PEANUTS'], 'Homer Simpson', true, (SELECT id FROM rsvp_group WHERE group_lead = 'Homer Simpson'));
INSERT INTO rsvp (dietary_restrictions, food_allergies, name, attending, group_id) VALUES (ARRAY['NO_RED_MEAT', 'NO_CHICKEN'], ARRAY['PEANUTS'], 'Marge Simpson', true, (SELECT id FROM rsvp_group WHERE group_lead = 'Homer Simpson'));
INSERT INTO rsvp (dietary_restrictions, food_allergies, name, attending, group_id) VALUES (ARRAY['NO_RED_MEAT', 'NO_CHICKEN'], ARRAY['PEANUTS'], 'Lisa Simpson', false, (SELECT id FROM rsvp_group WHERE group_lead = 'Homer Simpson'));
INSERT INTO rsvp (dietary_restrictions, food_allergies, name, attending, group_id) VALUES (ARRAY['NO_RED_MEAT', 'NO_CHICKEN'], ARRAY['PEANUTS'], 'Bart Simpson', false, (SELECT id FROM rsvp_group WHERE group_lead = 'Homer Simpson'));
