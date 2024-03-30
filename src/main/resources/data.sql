INSERT INTO roles (name)
VALUES ('ROLE_ADMIN'), ('ROLE_USER'),('ROLE_GUEST')
ON CONFLICT (name) DO NOTHING;

--username: test_user
--password: sausages
INSERT INTO users (id, username, password, email)
VALUES
    ('d3abd5e5-6d0f-4968-b443-bfe03cd9157e', 'test_user' ,'$2a$10$PcSvcvAMh0UjiS8CsiNbzulmxR4ua0g3PDg.eNQGTfwXPOQdUIMZC', 'test_user@gmail.com')
ON CONFLICT (username) DO NOTHING;

--username: guest_user
--password: sausages
INSERT INTO users (id, username, password, email)
VALUES
    ('8bcdf722-ee40-4dac-9749-5d9c65f088c4', 'guest_user' ,'$2a$10$PcSvcvAMh0UjiS8CsiNbzulmxR4ua0g3PDg.eNQGTfwXPOQdUIMZC', 'teamchillguest@gmail.com')
ON CONFLICT (username) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
VALUES
    ((SELECT id FROM users WHERE username = 'test_user'), (SELECT id FROM roles WHERE name = 'ROLE_ADMIN'))
ON CONFLICT (user_id, role_id) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
VALUES
    ((SELECT id FROM users WHERE username = 'guest_user'), (SELECT id FROM roles WHERE name = 'ROLE_GUEST'))
ON CONFLICT (user_id, role_id) DO NOTHING;

--Scripts
INSERT INTO scripts (name,body,date_created,date_updated,owner_id)
VALUES
('Script 1','START','2024-01-01','2024-01-03',(SELECT id FROM users WHERE username = 'test_user')),
('Script 2','ATTACK','2024-01-04','2024-01-04',(SELECT id FROM users WHERE username = 'test_user')),
('Script 3','EAT','2024-01-06','2024-01-10',(SELECT id FROM users WHERE username = 'user123')),
('Script 4','FINISH','2024-01-09','2024-01-09',(SELECT id FROM users WHERE username = 'test_user')),
('Script 5','FORWARD','2024-01-16','2024-01-18',(SELECT id FROM users WHERE username = 'user123')),
('Script 6','LEFT','2024-01-20','2024-01-21',(SELECT id FROM users WHERE username = 'test_user'));

--guest scripts, for all users
INSERT INTO scripts (name,body,date_created,date_updated,owner_id)
VALUES
('Guest Script A','FORWARD','2024-01-28','2024-01-28',(SELECT id FROM users WHERE username = 'guest_user')),
('Guest Script B','LEFT','2024-01-28','2024-01-28',(SELECT id FROM users WHERE username = 'guest_user')),
('Guest Script C','RIGHT','2024-01-28','2024-01-28',(SELECT id FROM users WHERE username = 'guest_user')),
('Guest Script D','EAT','2024-01-28','2024-01-28',(SELECT id FROM users WHERE username = 'guest_user'));

INSERT INTO terrain (name, image)
VALUES
('Grass', 'src/assets/terrain/grass.png'),
('Wall', 'src/assets/terrain/wall.png');

INSERT INTO game_maps (name, height, width, body, image)
VALUES
('Map 1', 11, 11,
'XXXXXXXXXXX\nX000010000X\nX000000000X\nX000000000X\nX000000000X\nX000000000X\nX000000000X\nX000000000X\nX000000000X\nX000000000X\nXXXXXXXXXXX\n',
'src/assets/images/map-0.png'),
('Map 2', 11, 11,
'XXXXXXXXXXX\nX000010000X\nX000000000X\nX000000000X\nX000000000X\nX000000000X\nX000000000X\nX000000000X\nX000000000X\nX000000000X\nXXXXXXXXXXX\n',
'src/assets/images/map-1.png'),
('Map 3', 11, 11,
'XXXXXXXXXXX\nX000010000X\nX000000000X\nX000000000X\nX000000000X\nX000000000X\nX000000000X\nX000000000X\nX000000000X\nX000000000X\nXXXXXXXXXXX\n',
'src/assets/images/map-2.png');