CREATE TABLE IF NOT EXISTS terrain (
    id bigint,
    name character varying(255),
    image character varying(255)
);

CREATE TABLE IF NOT EXISTS entities (
    id bigint,
    name character varying(255),
    code character,
    image character varying(255)
);

CREATE TABLE IF NOT EXISTS game_maps (
    id bigint,
    name character varying(255),
    terrain_id bigint,
    height int,
    width int,
    body text,
    image character varying(255)
);

CREATE TABLE IF NOT EXISTS sample_string (
    id bigint,
    content character varying(255)
);

CREATE TABLE IF NOT EXISTS roles (
    id integer,
    name character varying(20)
);

CREATE TABLE IF NOT EXISTS user_roles (
    role_id integer,
    user_id bigint
);

CREATE TABLE IF NOT EXISTS users (
    id bigint,
    email character varying(255),
    password character varying(255),
    username character varying(255)
);

CREATE TABLE IF NOT EXISTS scripts (
    date_created date,
    date_updated date,
    owner_id bigint,
    script_id bigint,
    body text,
    name character varying(255)
);

INSERT INTO roles (name)
VALUES ('ROLE_ADMIN'), ('ROLE_USER'),('ROLE_GUEST')
ON CONFLICT (name) DO NOTHING;

--username: test_user
--password: sausages
INSERT INTO users (username, password, email)
VALUES
    ('test_user' ,'$2a$10$PcSvcvAMh0UjiS8CsiNbzulmxR4ua0g3PDg.eNQGTfwXPOQdUIMZC', 'test_user@gmail.com')
ON CONFLICT (username) DO NOTHING;

--username: user123
--password: bananas
INSERT INTO users (username, password, email)
VALUES
    ('user123' ,'$2b$12$K8v/9enGmptX7wCNWcOpwuF35f.k7RYvgu50KMi/xVvgdKsAvp/y6', 'user123@gmail.com')
ON CONFLICT (username) DO NOTHING;

--username: guest_user
--password: sausages
INSERT INTO users (username, password, email)
VALUES
    ('guest_user' ,'$2a$10$PcSvcvAMh0UjiS8CsiNbzulmxR4ua0g3PDg.eNQGTfwXPOQdUIMZC', 'teamchillguest@gmail.com')
ON CONFLICT (username) DO NOTHING;

--username: admin_user
--password: sausages
INSERT INTO users (username, password, email)
VALUES
    ('admin_user' ,'$2a$10$PcSvcvAMh0UjiS8CsiNbzulmxR4ua0g3PDg.eNQGTfwXPOQdUIMZC', 'admin123@gmail.com')
ON CONFLICT (username) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
VALUES
    ((SELECT id FROM users WHERE username = 'test_user'), (SELECT id FROM roles WHERE name = 'ROLE_USER'))
ON CONFLICT (user_id, role_id) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
VALUES
    ((SELECT id FROM users WHERE username = 'user123'), (SELECT id FROM roles WHERE name = 'ROLE_USER'))
ON CONFLICT (user_id, role_id) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
VALUES
    ((SELECT id FROM users WHERE username = 'guest_user'), (SELECT id FROM roles WHERE name = 'ROLE_GUEST'))
ON CONFLICT (user_id, role_id) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
VALUES
    ((SELECT id FROM users WHERE username = 'admin_user'), (SELECT id FROM roles WHERE name = 'ROLE_ADMIN'))
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
('Simple Grass Map', 11, 11,
'XXXXXXXXXXX\nX000010000X\nX000000000X\nX000000000X\nX000000000X\nX000000000X\nX000000000X\nX000000000X\nX000000000X\nX000000000X\nXXXXXXXXXXX\n',
'src/assets/images/map-0.png');


