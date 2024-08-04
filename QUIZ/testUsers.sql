USE QuizDatabase;

SET foreign_key_checks = 0;
DROP TABLE IF EXISTS test_users_table;
DROP TABLE IF EXISTS test_friends_table;
DROP TABLE IF EXISTS test_announcements_table;
DROP TABLE IF EXISTS test_achievements_table;
DROP TABLE IF EXISTS test_notes;
DROP TABLE IF EXISTS test_unseen_announcements;
SET foreign_key_checks = 1;

-- users and friends


CREATE TABLE test_users_table (
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

CREATE TABLE test_friends_table (
                               username VARCHAR(35),
                               friend_username VARCHAR(35),
                               status ENUM('pending', 'accepted', 'rejected') DEFAULT 'pending',
                               PRIMARY KEY (username, friend_username),
                               FOREIGN KEY (username) REFERENCES test_users_table(username),
                               FOREIGN KEY (friend_username) REFERENCES test_users_table(username)
);

INSERT INTO test_users_table (username, first_name, last_name, biography, hashed_pw, picture_url)
VALUES
    ('user1', 'John', 'Doe', 'A software developer from California.',
     '6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b', ''),
    ('user2', 'Jane', 'Smith', 'A graphic designer from New York.',
     'd4735e3a265e16eee03f59718b9b5d03019c07d8b6c51f90da3a666eec13ab35', ''),
    ('user3', 'Alice', 'Johnson', 'A digital marketer from Texas.',
     '4e07408562bedb8b60ce05c1decfe3ad16b72230967de01f640b7e4729b49fce', ''),
    ('user4', 'Emma', 'Brown', 'passionate graphic designer',
     '4b227777d4dd1fc61c6f884f48641d02b4d121d3fd328cb08b5531fcacdabf8a', ''),
    ('user5', 'Liam', 'Johnson', 'skilled software engineer',
     'ef2d127de37b942baad06145e54b0c619a1f22327b2ebbcfbec78f5564afe39d', ''),
    ('user6', 'Ava', 'Martinez', 'dedicated teacher',
     'e7f6c011776e8db7cd330b54174fd76f7d0216b612387a5ffcfb81e6f0919683', ''),
    ('user7', 'Noah', 'Williams', 'experienced financial analyst with a keen eye for detail.',
     '7902699be42c8a8e46fbbb4501726517e86b22c56a189f7625a6da49081b2451', ''),
    ('user8', 'Sophia', 'Davis', 'talented writer and journalist',
     '2c624232cdd221771294dfbb310aca000a0df6ac8b66b696d90ef06fdefb64a3', ''),
    ('user9', 'James', 'Wilson', 'marketing strategist with over 12 years of experience',
     '19581e27de7ced00ff1ce50b2047e7a567c76b1cbaebabe5ef03f7c3017bb5b7', '');

INSERT INTO test_users_table (username, first_name, last_name, biography, hashed_pw, picture_url, is_admin)
VALUES
    ('Bacha', 'Giorgi', 'Bachaliashvili', '', 'a0ee467653d311b93f73ead82f7687f4b0f5421e0f717c137d2b3d370f2dd6df', 'https://oyster.ignimgs.com/mediawiki/apis.ign.com/avatar-the-last-airbender/b/b0/Aang_img.jpg', 1),
    ('kingslayer', 'Nika', 'Gvalia', 'Cigarette enthusiast', 'd10ea3c735ec9a7132a482c917b3af82d80d27ce974a6647d95564d466e8787b', 'https://static.hbo.com/content/dam/hbodata/series/rome/character/mark-antony-1920.jpg?w=1200', 1),
    ('almasxit', 'Tako', 'Gelashvili', 'He is a phantom', '0bc7df544bdfd7da80bfc54132e8cee7d087cdd07b3204461d395b1ce7e2403e', 'https://a.ltrbxd.com/resized/avatar/upload/1/9/6/6/4/5/4/shard/avtr-0-1000-0-1000-crop.jpg?v=594d1064b8', 1);


-- 10 bi-directional friendships between 9 users
INSERT INTO test_friends_table (username, friend_username, status)
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

INSERT INTO test_friends_table (username, friend_username, status)
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


CREATE TABLE test_announcements_table (
                                     announcement_id INT AUTO_INCREMENT PRIMARY KEY,
                                     username VARCHAR(35),
                                     announcement TEXT,
                                     creation_date TIMESTAMP default NOW()
);

CREATE TABLE test_unseen_announcements (
                                      announcement_id INT,
                                      username VARCHAR(35),
                                      creation_date TIMESTAMP default NOW()
);

CREATE TABLE test_achievements_table(
                                   achievement_id INT AUTO_INCREMENT PRIMARY KEY,
                                   username VARCHAR(35),
                                   achievement_type INT,
                                   received_date TIMESTAMP default NOW()
);

INSERT INTO test_announcements_table(username, announcement)
VALUES
    ('Bacha', 'today is Saturday'),
    ('Bacha', 'This Quiz WebSite is the best'),
    ('Bacha', 'სულ მალე საქართველო ევროპაზე ითამაშებს და ჭკუაზე არ ვარ');


-- Insert 10 random announcements for almasxit
INSERT INTO test_announcements_table (username, announcement) VALUES
                                                             ('almasxit', 'Exciting news! Our new product launch is just around the corner. Stay tuned!'),
                                                             ('almasxit', 'Don\'t forget to check out our latest blog post on digital transformation.'),
                                                             ('almasxit', 'We are thrilled to announce a partnership with XYZ Corp.'),
                                                             ('almasxit', 'Join us for a webinar on the future of AI next Tuesday.'),
                                                             ('almasxit', 'Our team has been working hard on new features. Look out for the update!'),
                                                             ('almasxit', 'We are expanding our team! Check out our careers page for openings.'),
                                                             ('almasxit', 'Thank you for helping us reach 10,000 followers!'),
                                                             ('almasxit', 'Reminder: The office will be closed on Monday for the public holiday.'),
                                                             ('almasxit', 'We are hosting a community meetup this Friday. Come and join us!'),
                                                             ('almasxit', 'A special thank you to our customers for their continuous support.');

-- Insert 10 random announcements for Bacha
INSERT INTO test_announcements_table (username, announcement) VALUES
                                                             ('Bacha', 'New feature alert! You can now integrate our service with your favorite apps.'),
                                                             ('Bacha', 'Our latest software update includes several performance improvements.'),
                                                             ('Bacha', 'Our annual company retreat is scheduled for next month. Prepare for fun!'),
                                                             ('Bacha', 'We are conducting a survey to gather your feedback. Please participate!'),
                                                             ('Bacha', 'Excited to announce a collaboration with renowned artist Jane Doe.'),
                                                             ('Bacha', 'We are introducing a loyalty program for our valued customers.'),
                                                             ('Bacha', 'Our new office space is finally ready. Come visit us at our new location!'),
                                                             ('Bacha', 'Join our live Q&A session this Thursday to get your questions answered.'),
                                                             ('Bacha', 'Big thanks to our developers for their hard work on the latest patch.'),
                                                             ('Bacha', 'We are launching a new customer support portal for a better experience.');

-- Insert 10 random announcements for kingslayer
INSERT INTO test_announcements_table (username, announcement) VALUES
                                                             ('kingslayer', 'We are excited to unveil our new company logo and branding.'),
                                                             ('kingslayer', 'A major update to our mobile app is now available. Download it today!'),
                                                             ('kingslayer', 'We are hosting a virtual hackathon next month. Sign up now!'),
                                                             ('kingslayer', 'Check out our latest case study on improving user engagement.'),
                                                             ('kingslayer', 'Our end-of-year sale starts next week. Don\'t miss out on great deals!'),
                                                             ('kingslayer', 'We have expanded our services to include 24/7 customer support.'),
                                                             ('kingslayer', 'Our CEO will be speaking at the upcoming tech conference.'),
                                                             ('kingslayer', 'Join our online community forum to connect with other users.'),
                                                             ('kingslayer', 'We are proud to support local charities through our new initiative.'),
                                                             ('kingslayer', 'Our new training program for employees is launching soon.');



INSERT INTO test_unseen_announcements(announcement_id , username)
VALUES
    (1, 'almasxit'),
    (2, 'almasxit'),
    (3, 'almasxit'),
    (4, 'almasxit'),
    (5, 'almasxit'),
    (6, 'almasxit'),
    (7, 'almasxit'),
    (8, 'almasxit'),
    (9, 'almasxit'),
    (10, 'almasxit'),

    (1, 'Bacha'),
    (2, 'Bacha'),
    (3, 'Bacha'),
    (4, 'Bacha'),
    (5, 'Bacha'),
    (6, 'Bacha'),
    (7, 'Bacha'),
    (8, 'Bacha'),
    (9, 'Bacha'),
    (10, 'Bacha'),


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



INSERT INTO test_achievements_table (username, achievement_type) VALUES
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


CREATE TABLE test_notes (
                       note_id INT AUTO_INCREMENT PRIMARY KEY,
                       sender_username VARCHAR(35),
                       receiver_username VARCHAR(35),
                       content TEXT,
                       content_type VARCHAR(35),
                       timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
                       FOREIGN KEY (sender_username) REFERENCES test_users_table(username),
                       FOREIGN KEY (receiver_username) REFERENCES test_users_table(username)
);

INSERT INTO test_notes(sender_username, receiver_username, content, content_type)
VALUES
    ('user1', 'almasxit', 'has sent you a friend request.', 'friendship'),
    ('user2', 'almasxit', 'has sent you a friend request.', 'friendship'),
    ('user3', 'almasxit', 'has sent you a friend request.', 'friendship');
