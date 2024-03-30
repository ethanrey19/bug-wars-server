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
INSERT INTO scripts (script_id, name,body,date_created,date_updated,owner_id)
VALUES
('964eb7a3-5501-4b20-90f4-c738ecae1aaa', 'Script 1','START','2024-01-01','2024-01-03',(SELECT id FROM users WHERE username = 'test_user')),
('947ca7db-d34a-460d-9b14-0ff52a0bc0c5', 'Script 2','ATTACK','2024-01-04','2024-01-04',(SELECT id FROM users WHERE username = 'test_user')),
('8af6bd8a-7f15-4441-877c-0bce9d793f65', 'Script 3','EAT','2024-01-06','2024-01-10',(SELECT id FROM users WHERE username = 'user123')),
('fdd39d78-3171-4847-88e9-59da3f48cffa', 'Script 4','FINISH','2024-01-09','2024-01-09',(SELECT id FROM users WHERE username = 'test_user')),
('3425ad90-708b-4282-a390-e43b9306e7e3', 'Script 5','FORWARD','2024-01-16','2024-01-18',(SELECT id FROM users WHERE username = 'user123')),
('7db1d79b-dc33-4b0a-8b3d-4ce79133e7e1', 'Script 6','LEFT','2024-01-20','2024-01-21',(SELECT id FROM users WHERE username = 'test_user'));

--guest scripts, for all users
INSERT INTO scripts (script_id, name,body,date_created,date_updated,owner_id)
VALUES
('32900556-e043-4693-90c6-65cf220d27a3', 'Guest Script A','FORWARD','2024-01-28','2024-01-28',(SELECT id FROM users WHERE username = 'guest_user')),
('1c4ae26c-d983-4827-897a-bd99ea60d329', 'Guest Script B','LEFT','2024-01-28','2024-01-28',(SELECT id FROM users WHERE username = 'guest_user')),
('2980c080-6d2f-4986-8120-bf42e570117d', 'Guest Script C','RIGHT','2024-01-28','2024-01-28',(SELECT id FROM users WHERE username = 'guest_user')),
('3ea558d1-809c-4fca-a9e0-325d15f3deaa', 'Guest Script D','EAT','2024-01-28','2024-01-28',(SELECT id FROM users WHERE username = 'guest_user'));

INSERT INTO terrain (name, image)
VALUES
( 'Grass', 'src/assets/terrain/grass.png'),
( 'Wall', 'src/assets/terrain/wall.png');

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