DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS user_friends;
DROP TABLE IF EXISTS user_with_friends;

CREATE TABLE user
(
 id INT AUTO_INCREMENT PRIMARY KEY,
 name VARCHAR(250) NOT NULL,
 city VARCHAR(250) NOT NULL
);

CREATE TABLE user_friends
(
 id INT AUTO_INCREMENT PRIMARY KEY,
 user_id INT NOT NULL,
 user_friend_id INT NOT NULL
);

ALTER TABLE user_friends
 ADD FOREIGN KEY (user_id) REFERENCES user (id);

ALTER TABLE user_friends
 ADD FOREIGN KEY (user_friend_id) REFERENCES user (id);

INSERT INTO user (name, city)
VALUES ('Alice', 'Dublin'),
('Bob', 'Dublin'),
('Charlie', 'London'),
('Davina', 'Belfast'),
('John', 'Galway');

INSERT INTO user_friends (user_id, user_friend_id)
VALUES (1, 3), (2, 3), (2, 4), (2, 5), (3, 1), (3, 2), (3, 5), (4, 2), (4, 5),
 (5, 2), (5, 3), (5, 4);

CREATE OR REPLACE TABLE user_with_friends AS
(
 SELECT u.id, u.name, u.city, l.id friendid, l.name friendName, l.city friendcity
 FROM user u INNER JOIN user_friends uf ON u.id = uf.user_id
             INNER JOIN user l ON uf.user_friend_id = l.id
)
