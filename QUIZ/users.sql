USE QuizDatabase; -- you'll need to create it/ use other

SET foreign_key_checks = 0;
DROP TABLE IF EXISTS users_table;
DROP TABLE IF EXISTS friends_table;
DROP TABLE IF EXISTS announcements_table;
DROP TABLE IF EXISTS achievements_table;
DROP TABLE IF EXISTS notes;
DROP TABLE IF EXISTS unseen_announcements;
SET foreign_key_checks = 1;



-- users and friends


SET foreign_key_checks = 0;
DROP TABLE IF EXISTS users_table;
DROP TABLE IF EXISTS friends_table;
DROP TABLE IF EXISTS announcements_table;
DROP TABLE IF EXISTS achievements_table;
DROP TABLE IF EXISTS notes;
DROP TABLE IF EXISTS unseen_announcements;
SET foreign_key_checks = 1;



-- users and friends


CREATE TABLE users_table (
                             username VARCHAR(35),
                             first_name VARCHAR(35) NOT NULL,
                             last_name VARCHAR(35) NOT NULL,
                             biography TEXT,
                             hashed_pw VARCHAR(100) NOT NULL,
                             picture_url VARCHAR(1024),
                             creation_date TIMESTAMP default now(),
                             is_admin      TINYINT(1) DEFAULT '0',
                             PRIMARY KEY (username)
);

CREATE TABLE friends_table (
                               username VARCHAR(35),
                               friend_username VARCHAR(35),
                               status ENUM('pending', 'accepted', 'rejected') DEFAULT 'pending',
                               PRIMARY KEY (username, friend_username),
                               FOREIGN KEY (username) REFERENCES users_table(username),
                               FOREIGN KEY (friend_username) REFERENCES users_table(username)
);

INSERT INTO users_table (username, first_name, last_name, biography, hashed_pw, picture_url)
VALUES
    ('user1', 'John', 'Doe', 'A software developer from California.',
     '6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b', 'https://cdn.vox-cdn.com/thumbor/OhRgTL232xHKcJsUuEet-w4l2Ns=/1400x1400/filters:format(png)/cdn.vox-cdn.com/uploads/chorus_asset/file/23099830/Screen_Shot_2021_12_15_at_3.57.28_PM.png'), -- password:1
    ('user2', 'Jane', 'Smith', 'A graphic designer from New York.',
     'd4735e3a265e16eee03f59718b9b5d03019c07d8b6c51f90da3a666eec13ab35', 'https://oyster.ignimgs.com/mediawiki/apis.ign.com/avatar-the-last-airbender/d/d2/Ty_lee_img.jpg'), -- 2
    ('user3', 'Alice', 'Johnson', 'A digital marketer from Texas.',
     '4e07408562bedb8b60ce05c1decfe3ad16b72230967de01f640b7e4729b49fce', 'https://oyster.ignimgs.com/mediawiki/apis.ign.com/avatar-the-last-airbender/2/25/Katara_img.jpg'), -- 3
    ('user4', 'Emma', 'Brown', 'passionate graphic designer',
     '4b227777d4dd1fc61c6f884f48641d02b4d121d3fd328cb08b5531fcacdabf8a', 'https://oyster.ignimgs.com/mediawiki/apis.ign.com/avatar-the-last-airbender/b/b7/Momo.jpg'), -- 4
    ('user5', 'Liam', 'Johnson', 'skilled software engineer',
     'ef2d127de37b942baad06145e54b0c619a1f22327b2ebbcfbec78f5564afe39d', 'https://i.pinimg.com/474x/00/9f/9c/009f9c891a8b46d22237e7349c7d9af3.jpg'), -- 5
    ('user6', 'Ava', 'Martinez', 'dedicated teacher',
     'e7f6c011776e8db7cd330b54174fd76f7d0216b612387a5ffcfb81e6f0919683', ''), -- 6
    ('user7', 'Noah', 'Williams', 'experienced financial analyst with a keen eye for detail.',
     '7902699be42c8a8e46fbbb4501726517e86b22c56a189f7625a6da49081b2451', 'https://64.media.tumblr.com/9f3798c09a3cb7f4b6b06580570c0e5b/feb7636602078251-60/s640x960/aee3a089d3144b3b7a59d0c79f99e6cf97ff5960.png'), -- 7
    ('user8', 'Sophia', 'Davis', 'talented writer and journalist',
     '2c624232cdd221771294dfbb310aca000a0df6ac8b66b696d90ef06fdefb64a3', 'https://oyster.ignimgs.com/mediawiki/apis.ign.com/avatar-the-last-airbender/8/8d/Mai_img.jpg'), -- 8
    ('user9', 'James', 'Wilson', 'marketing strategist with over 12 years of experience',
     '19581e27de7ced00ff1ce50b2047e7a567c76b1cbaebabe5ef03f7c3017bb5b7', 'https://i.pinimg.com/474x/47/4b/12/474b12d8f41063ff28bbc769e2f35bdb.jpg'); -- 9

INSERT INTO users_table (username, first_name, last_name, biography, hashed_pw, picture_url, is_admin)
VALUES
    ('Bacha', 'Giorgi', 'Bachaliashvili', '', '010da82154fbf7a62b8446ff61ed968bea2f5abecce17e595c96215bfc00f459', 'https://oyster.ignimgs.com/mediawiki/apis.ign.com/avatar-the-last-airbender/b/b0/Aang_img.jpg', 1),
    ('kingslayer', 'Nika', 'Gvalia', 'Cigarette enthusiast', 'd10ea3c735ec9a7132a482c917b3af82d80d27ce974a6647d95564d466e8787b', 'https://i1.sndcdn.com/artworks-aTq4Lvypyud6O100-zb2Cug-t500x500.jpg', 1),
    ('almasxit', 'Tako', 'Gelashvili', 'He is a phantom', '0bc7df544bdfd7da80bfc54132e8cee7d087cdd07b3204461d395b1ce7e2403e', 'https://a.ltrbxd.com/resized/avatar/upload/1/9/6/6/4/5/4/shard/avtr-0-1000-0-1000-crop.jpg?v=594d1064b8', 1);


-- 10 bi-directional friendships between 9 users
INSERT INTO friends_table (username, friend_username, status)
VALUES
    ('user1', 'user2', 'accepted'),
    ('user2', 'user1', 'accepted'),

    ('user1', 'user3', 'accepted'),
    ('user3', 'user1', 'accepted'),

    ('user2', 'user4', 'accepted'),
    ('user4', 'user2', 'accepted'),

    ('user3', 'user5', 'accepted'),
    ('user5', 'user3', 'accepted'),

    ('user4', 'user6', 'accepted'),
    ('user6', 'user4', 'accepted'),

    ('user5', 'user7', 'accepted'),
    ('user7', 'user5', 'accepted'),

    ('user6', 'user8', 'accepted'),
    ('user8', 'user6', 'accepted'),

    ('user7', 'user9', 'accepted'),
    ('user9', 'user7', 'accepted'),


    ('user8', 'user1', 'accepted'),
    ('user1', 'user8', 'accepted'),

    ('user9', 'user2', 'accepted'),
    ('user2', 'user9', 'accepted'),

    ('user1', 'almasxit', 'pending'),
    ('user2', 'almasxit', 'pending'),
    ('user3', 'almasxit', 'pending');

INSERT INTO friends_table (username, friend_username, status)
VALUES
    ('user4', 'Bacha', 'accepted'),
    ('Bacha', 'user4', 'accepted'),

    ('user1', 'Bacha', 'accepted'),
    ('Bacha', 'user1', 'accepted'),

    ('user9', 'Bacha', 'accepted'),
    ('Bacha', 'user9', 'accepted'),

    ('kingslayer', 'Bacha', 'accepted'),
    ('Bacha', 'kingslayer', 'accepted'),

    ('kingslayer', 'user1', 'accepted'),
    ('user1', 'kingslayer', 'accepted'),

    ('almasxit', 'user6', 'accepted'),
    ('user6', 'almasxit', 'accepted'),

    ('almasxit', 'Bacha', 'accepted'),
    ('Bacha', 'almasxit', 'accepted'),

    ('almasxit', 'kingslayer', 'accepted'),
    ('kingslayer', 'almasxit', 'accepted');



-- achievements and announcements


CREATE TABLE announcements_table (
                                     announcement_id INT AUTO_INCREMENT PRIMARY KEY,
                                     username VARCHAR(35),
                                     announcement TEXT,
                                     creation_date TIMESTAMP default NOW()
);

CREATE TABLE unseen_announcements (
                                      announcement_id INT,
                                      username VARCHAR(35),
                                      creation_date TIMESTAMP default NOW()
);

CREATE TABLE achievements_table(
                                   achievement_id INT AUTO_INCREMENT PRIMARY KEY,
                                   username VARCHAR(35),
                                   achievement_type INT,
                                   received_date TIMESTAMP default NOW()
);


-- Insert 10 random announcements for almasxit
INSERT INTO announcements_table (username, announcement, creation_date) VALUES
                                                             ('almasxit', 'Exciting news! Our new product launch is just around the corner. Stay tuned!', '2024-06-03 01:48:46'),
                                                             ('almasxit', 'Don\'t forget to check out our latest blog post on digital transformation.', '2024-06-05 01:48:46'),
                                                             ('almasxit', 'We are thrilled to announce a partnership with XYZ Corp.', '2024-06-08 01:48:46'),
                                                             ('almasxit', 'Join us for a webinar on the future of AI next Tuesday.', '2024-06-11 01:48:46'),
                                                             ('almasxit', 'Our team has been working hard on new features. Look out for the update!', '2024-06-15 01:48:46'),
                                                             ('almasxit', 'We are expanding our team! Check out our careers page for openings.', '2024-06-18 01:48:46'),
                                                             ('almasxit', 'Thank you for helping us reach 10,000 followers!', '2024-06-21 01:48:46'),
                                                             ('almasxit', 'Reminder: The office will be closed on Monday for the public holiday.', '2024-06-27 01:48:46'),
                                                             ('almasxit', 'We are hosting a community meetup this Friday. Come and join us!', '2024-07-03 01:48:46'),
                                                             ('almasxit', 'A special thank you to our customers for their continuous support.', '2024-07-08 01:48:46');

-- Insert 10 random announcements for Bacha
INSERT INTO announcements_table (username, announcement, creation_date) VALUES
                                                             ('Bacha', 'New feature alert! You can now integrate our service with your favorite apps.', '2024-06-03 01:48:46'),
                                                             ('Bacha', 'Our latest software update includes several performance improvements.', '2024-06-05 01:48:46'),
                                                             ('Bacha', 'Our annual company retreat is scheduled for next month. Prepare for fun!', '2024-06-08 01:48:46'),
                                                             ('Bacha', 'We are conducting a survey to gather your feedback. Please participate!', '2024-06-10 01:48:46'),
                                                             ('Bacha', 'Excited to announce a collaboration with renowned artist Jane Doe.', '2024-06-12 01:48:46'),
                                                             ('Bacha', 'We are introducing a loyalty program for our valued customers.', '2024-06-15 01:48:46'),
                                                             ('Bacha', 'Our new office space is finally ready. Come visit us at our new location!', '2024-06-19 01:48:46'),
                                                             ('Bacha', 'Join our live Q&A session this Thursday to get your questions answered.', '2024-07-03 01:48:46'),
                                                             ('Bacha', 'Big thanks to our developers for their hard work on the latest patch.', '2024-07-06 01:48:46'),
                                                             ('Bacha', 'We are launching a new customer support portal for a better experience.', '2024-07-08 01:48:46');

-- Insert 10 random announcements for kingslayer
INSERT INTO announcements_table (username, announcement, creation_date) VALUES
                                                             ('kingslayer', 'We are excited to unveil our new company logo and branding.', '2024-06-03 01:48:46'),
                                                             ('kingslayer', 'A major update to our mobile app is now available. Download it today!', '2024-06-03 01:48:46'),
                                                             ('kingslayer', 'We are hosting a virtual hackathon next month. Sign up now!', '2024-06-05 01:48:46'),
                                                             ('kingslayer', 'Check out our latest case study on improving user engagement.', '2024-06-09 01:48:46'),
                                                             ('kingslayer', 'Our end-of-year sale starts next week. Don\'t miss out on great deals!', '2024-06-13 01:48:46'),
                                                             ('kingslayer', 'We have expanded our services to include 24/7 customer support.', '2024-06-23 01:48:46'),
                                                             ('kingslayer', 'Our CEO will be speaking at the upcoming tech conference.', '2024-06-29 01:48:46'),
                                                             ('kingslayer', 'Join our online community forum to connect with other users.', '2024-07-03 01:48:46'),
                                                             ('kingslayer', 'We are proud to support local charities through our new initiative.', '2024-07-08 01:48:46'),
                                                             ('kingslayer', 'Our new training program for employees is launching soon.', '2024-07-09 01:48:46');


INSERT INTO announcements_table(username, announcement)
VALUES
    ('Bacha', 'today is Saturday'),
    ('Bacha', 'This Quiz WebSite is the best'),
    ('Bacha', 'სულ მალე საქართველო ევროპაზე ითამაშებს და ჭკუაზე არ ვარ');

INSERT INTO unseen_announcements(announcement_id , username)
VALUES
    (11, 'almasxit'),
    (12, 'almasxit'),
    (13, 'almasxit'),
    (14, 'almasxit'),
    (15, 'almasxit'),
    (16, 'almasxit'),
    (17, 'almasxit'),
    (18, 'almasxit'),
    (19, 'almasxit'),
    (20, 'almasxit'),

    (21, 'Bacha'),
    (22, 'Bacha'),
    (23, 'Bacha'),
    (24, 'Bacha'),
    (25, 'Bacha'),
    (26, 'Bacha'),
    (27, 'Bacha'),
    (28, 'Bacha'),
    (29, 'Bacha'),
    (30, 'Bacha'),


    (1, 'kingslayer'),
    (2, 'kingslayer'),
    (3, 'kingslayer'),
    (4, 'kingslayer'),
    (5, 'kingslayer'),
    (6, 'kingslayer'),
    (7, 'kingslayer'),
    (8, 'kingslayer'),
    (9, 'kingslayer'),
    (10, 'kingslayer');



INSERT INTO achievements_table (username, achievement_type) VALUES
                                                                ('user1', FLOOR(RAND() * 6)),
                                                                ('user2', FLOOR(RAND() * 6)),
                                                                ('user3', FLOOR(RAND() * 6)),
                                                                ('user4', FLOOR(RAND() * 6)),
                                                                ('user5', FLOOR(RAND() * 6)),
                                                                ('user1', FLOOR(RAND() * 6)),
                                                                ('user2', FLOOR(RAND() * 6)),
                                                                ('user3', FLOOR(RAND() * 6)),
                                                                ('user4', FLOOR(RAND() * 6)),
                                                                ('user5', FLOOR(RAND() * 6)),
                                                                ('user1', FLOOR(RAND() * 6)),
                                                                ('user2', FLOOR(RAND() * 6)),
                                                                ('user3', FLOOR(RAND() * 6)),
                                                                ('user4', FLOOR(RAND() * 6)),
                                                                ('user5', FLOOR(RAND() * 6)),
                                                                ('user1', FLOOR(RAND() * 6)),
                                                                ('user2', FLOOR(RAND() * 6)),
                                                                ('user3', FLOOR(RAND() * 6)),
                                                                ('user4', FLOOR(RAND() * 6)),
                                                                ('user5', FLOOR(RAND() * 6)),
                                                                ('user1', FLOOR(RAND() * 6)),
                                                                ('user2', FLOOR(RAND() * 6)),
                                                                ('user3', FLOOR(RAND() * 6)),
                                                                ('user4', FLOOR(RAND() * 6)),
                                                                ('user5', FLOOR(RAND() * 6)),
                                                                ('user1', FLOOR(RAND() * 6)),
                                                                ('user2', FLOOR(RAND() * 6)),
                                                                ('user3', FLOOR(RAND() * 6)),
                                                                ('user4', FLOOR(RAND() * 6)),
                                                                ('user5', FLOOR(RAND() * 6)),
                                                                ('user1', FLOOR(RAND() * 6)),
                                                                ('user2', FLOOR(RAND() * 6)),
                                                                ('user4', FLOOR(RAND() * 6)),
                                                                ('user5', FLOOR(RAND() * 6)),
                                                                ('user1', FLOOR(RAND() * 6)),
                                                                ('user2', FLOOR(RAND() * 6)),
                                                                ('user3', FLOOR(RAND() * 6)),
                                                                ('user4', FLOOR(RAND() * 6)),
                                                                ('user3', FLOOR(RAND() * 6)),
                                                                ('user5', FLOOR(RAND() * 6)),
                                                                ('almasxit', FLOOR(RAND() * 6)),
                                                                ('Bacha', FLOOR(RAND() * 6)),
                                                                ('kingslayer', FLOOR(RAND() * 6)),
                                                                ('almasxit', FLOOR(RAND() * 6)),
                                                                ('Bacha', FLOOR(RAND() * 6));


CREATE TABLE notes (
                       note_id INT AUTO_INCREMENT PRIMARY KEY,
                       sender_username VARCHAR(35),
                       receiver_username VARCHAR(35),
                       content TEXT,
                       content_type VARCHAR(35),
                       timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
                       FOREIGN KEY (sender_username) REFERENCES users_table(username),
                       FOREIGN KEY (receiver_username) REFERENCES users_table(username)
);

INSERT INTO notes(sender_username, receiver_username, content, content_type)
VALUES
    ('user1', 'almasxit', 'has sent you a friend request.', 'friendship'),
    ('user2', 'almasxit', 'has sent you a friend request.', 'friendship'),
    ('user3', 'almasxit', 'has sent you a friend request.', 'friendship');
