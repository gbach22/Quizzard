USE QuizDatabase;

DROP TABLE IF EXISTS quizzes;
DROP TABLE IF EXISTS questions;
DROP TABLE IF EXISTS answers;
DROP TABLE IF EXISTS options;
DROP TABLE IF EXISTS quiz_history;
DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS rate;

CREATE TABLE quizzes (
                         quiz_id INT AUTO_INCREMENT PRIMARY KEY,
                         quiz_name VARCHAR(100),
                         description VARCHAR(300),
                         creator_username VARCHAR(35),
                         picture_url VARCHAR(1024) DEFAULT 'https://d1ymz67w5raq8g.cloudfront.net/Pictures/480xany/6/5/5/509655_shutterstock_1506580442_769367.jpg',
                         multi_page TINYINT(1) DEFAULT 1,
                         random TINYINT(1) DEFAULT 1,
                         immediate_correction TINYINT(1) default 1,
                         practice_mode TINYINT(1) default 1,
                         created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         category INT,
                         views INT DEFAULT 0,
                         taken INT DEFAULT 0
);

CREATE TABLE tags (
                      tag_name VARCHAR(100),
                      quiz_id int
);

CREATE TABLE quiz_history(
                             quiz_id INT,
                             quiz_name VARCHAR(100),
                             picture_url VARCHAR(1024) DEFAULT 'https://d1ymz67w5raq8g.cloudfront.net/Pictures/480xany/6/5/5/509655_shutterstock_1506580442_769367.jpg',
                             username VARCHAR(35),
                             score DOUBLE,
                             time_needed INT COMMENT 'Seconds',
                             take_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE rate(
                     quiz_id INT,
                     username VARCHAR(35),
                     review VARCHAR(300),
                     rating INT
);

INSERT INTO quizzes(quiz_name, description, picture_url, creator_username, created_time, category, views, taken)
VALUES
    ('Mixed Quiz', 'This quiz contains a question of each type', 'https://viralsolutions.net/wp-content/uploads/2019/06/shutterstock_749036344.jpg', 'almasxit', '2024-05-29 01:48:46', 5, 6, 4);

CREATE TABLE questions (
                           question_id INT AUTO_INCREMENT PRIMARY KEY,
                           question_text TEXT,
                           question_type INT,
                           picture_url VARCHAR(1024) DEFAULT 'https://static.vecteezy.com/system/resources/previews/005/337/799/original/icon-image-not-found-free-vector.jpg',
                           sorted_relevant TINYINT(1) DEFAULT 0,
                           quiz_id INT,
                           score DOUBLE DEFAULT 1
);

INSERT INTO questions (question_text, question_type, picture_url, sorted_relevant, quiz_id, score)
VALUES
    ('What is the favorite dish of Tony Soprano?', 0, NULL, 0, 1, 1),
    ('Which is NOT a song by Kanye West?', 2, NULL, 0, 1, 1),
    ('Name top 5 countries with the highest gdp', 4, NULL, 1, 1, 5),
    ('Uruguay # Nepal # Kenya # Cuba', 6, NULL, 1, 1, 4),
    ('Select all movies by Gaspar Noe', 5, NULL, 0, 1, 3);

CREATE TABLE answers (
                         question_id INT,
                         answer VARCHAR(100),
                         answer_num INT
);

CREATE TABLE options (
                         question_id INT,
                         answer VARCHAR(100)
);

INSERT INTO options (question_id, answer)
VALUES
    (2, 'Homecoming'),
    (2, 'Ni**as in Paris'),
    (2, 'The World Is Yours'),
    (2, 'Ghost Town'),
    (4, 'Nairobi'),
    (4, 'Havana'),
    (4, 'Montevideo'),
    (4, 'Kathmandu'),
    (5, 'Climax'),
    (5, 'Irreversible'),
    (5, 'Diabolique'),
    (5, 'Enter The Void'),
    (5, 'Amelie');

INSERT INTO answers (question_id, answer, answer_num)
VALUES
    (1, 'Gabagool', -1),
    (1, 'Capocol', -1),
    (2, 'The World Is Yours', -1),
    (3, 'USA', 1),
    (3, 'China', 2),
    (3, 'Germany', 3),
    (3, 'Japan', 4),
    (3, 'India', 5),
    (4, 'Nairobi', 3),
    (4, 'Havana', 4),
    (4, 'Montevideo', 1),
    (4, 'Kathmandu', 2),
    (5, 'Climax', -1),
    (5, 'Irreversible', -1),
    (5, 'Enter The Void', -1);


INSERT INTO questions(question_text, question_type, picture_url, sorted_relevant, quiz_id)
VALUES
    ('The photo shows the best food in the world, name it', 3, 'https://cdn.tasteatlas.com/Images/Dishes/d9ae0ef06bc54f7cb4e6b5b928bc6f41.jpg', 0, 1),
    ('The national football team of Georgia will defeat # in the first game in the group stage of the European Championship and gain # points.', 1, NULL, 1, 1);

INSERT INTO answers (question_id, answer, answer_num)
VALUES
    (7, 'Turkey', 1),
    (7, '3', 2),
    (6, 'Khinkali', -1),
    (6, 'ხინკალი', -1);

-- test 2:

INSERT INTO quizzes(quiz_name, description, creator_username, created_time, multi_page, category, views, taken)
VALUES
    ('Barcelona', 'How well do you know the history of FC Barcelona?', 'Bacha', '2024-06-03 01:48:46', 0, 10, 100, 4);

INSERT INTO questions (question_text, question_type, picture_url, sorted_relevant, quiz_id)
VALUES
    ('How many Champions League has Barcelona won?', 0, NULL, 0, 2),
    ('Which of the following players did not play for Barca?', 2, NULL, 0, 2),
    ('List the seasons in which Barcelona won the Champions League', 4, NULL, 1, 2),
    ('Messi # Figo # Neymar # Suarez', 6, NULL, 1, 2),
    ('Choose from the list all the Spanish players who played for Barca', 5, NULL, 0, 2),
    ('Name the player', 3, 'https://img.bleacherreport.net/img/images/photos/002/112/495/hi-res-159596853_crop_north.jpg?1358786250&w=3072&h=2048', 0, 2),
    ('# and # together are like the perfect pairing of cheese and #', 1, NULL, 1, 2);

INSERT INTO options (question_id, answer)
VALUES
    (9, 'Franz Beckenbauer'),
    (9, 'Xavi'),
    (9, 'Ousman Demb*!*'),
    (9, 'Ter Stegen'),
    (11, 'Traitor'),
    (11, 'GOAT'),
    (11, 'Talent-Money'),
    (11, 'Teeth'),
    (12, 'Ronaldo Luis Nazario'),
    (12, 'Neymar'),
    (12, 'Ronaldinho'),
    (12, 'Victor Valdes'),
    (12, 'Luis Enrique');

INSERT INTO answers (question_id, answer, answer_num)
VALUES
    (8, '5', -1),
    (9, 'Franz Beckenbauer', -1),
    (10, '1991-1992 ', 1),
    (10, '2005-2006', 2),
    (10, '2008-2009', 3),
    (10, '2010-2011', 4),
    (10, '2014-2015', 5),
    (11, 'GOAT', 1),
    (11, 'Traitor', 2),
    (11, 'Talent-Money', 3),
    (11, 'Teeth', 4),
    (12, 'Victor Valdes', -1),
    (12, 'Luis Enrique', -1),
    (13, 'Football', -1),
    (13, 'Messi', -1),
    (13, 'GOAT', -1),
    (13, 'Better than anyone', -1),
    (14, 'Xavi', 1),
    (14, 'Iniesta', 2),
    (14, 'bread', 2);

-- tako

INSERT INTO quizzes(quiz_name, description, picture_url, creator_username, created_time, multi_page, category, views, taken)
VALUES
    ('Geography!', 'This quiz contains different question about geography, including flags, countries, states, capitals and so on', 'https://study.com/cimages/videopreview/p3c2j8y73a.jpg', 'almasxit', '2024-06-05 01:48:46', 1, 1, 105, 5),
    ('Asoiaf', 'Quiz about George RR Martin\'s \"A Song of Ice and Fire\"', 'https://awoiaf.westeros.org/images/thumb/f/fc/Cristi_Balanescu_wolves_of_the_north_cover.jpg/900px-Cristi_Balanescu_wolves_of_the_north_cover.jpg', 'almasxit', '2024-06-09 01:48:46', 1, '4', 200, 4),
    ('Tarantino', 'Quentined and Tarantined by Writtin Directino', 'https://media.tenor.com/3160R1pQhaEAAAAM/tarantino-sunglasses.gif', 'almasxit', '2024-06-11 01:48:46', 0, '6', 102, 5),
    ('Chemistry','Quiz about basic chemistry','https://i.pinimg.com/564x/cc/3d/fb/cc3dfbf61463a9b3aeaa4cdc27a1495b.jpg', 'almasxit','2024-06-15 01:48:46', 0, '9', 400, 5),
    ('Greek Mythology', 'I love greek mythology', 'https://images.squarespace-cdn.com/content/v1/5e01d4d4c4faa308239ed7b6/d4c98111-df13-428e-a209-a2bd5b95eae2/ws1ovyxqlq9afvgumsdd.jpg', 'almasxit', '2024-06-15 02:48:46', 1, '8', 10, 4);


INSERT INTO questions (question_text, question_type, picture_url, sorted_relevant, quiz_id, score)
VALUES
    ('Name the country!', 3, 'https://upload.wikimedia.org/wikipedia/en/thumb/0/05/Flag_of_Brazil.svg/640px-Flag_of_Brazil.svg.png', 0, 3, 2),
    ('Name one of the three capitals of Republic of South Africa', 0, NULL, 0, 3, 1),
    ('Miami # Seattle # Fargo # Omaha # Hartford', 6, NULL, 1, 3, 5),
    ('# is the capital of Uzbekistan. It is much bigger that the capital of # which is Bishkek.', 1, NULL, 1, 3, 4),
    ('Located in South America:', 5, NULL, '0', 3, '3'),
    ('Which American city dyes its river green of St. Patrick\'s Day?', '0', NULL, '0', '3', '2.5'),
    ('Name the country!', '3', 'https://cdn.britannica.com/48/5048-050-69610380/Flag-Cote-dIvoire.jpg', '0', '3', '3'),
    ('Name top 5 countries with LOWEST population', '4', NULL, '1', '3', '5'),
    ('Greenland is a territory of which country?', '2', NULL, '0', '3', '1'),
    ('Name the castle:','3','https://awoiaf.westeros.org/images/c/c7/Ted_Nasmith_A_Song_of_Ice_and_Fire_Highgarden.jpg','0','4','1'),
    ('Name the houses of the north:','5',NULL,'0','4','3'),
    ('Arya # Jon # Sansa # Robb # Bran # Rickon','6',NULL,'1','4','6'),
    ('The Sword of The Morning was Ser #','1',NULL,'1','4','1'),
    ('Name the house:','3','https://m.media-amazon.com/images/I/61wQO93OEJL.jpg','0','4','1'),
    ('Name all legitimate stark children from oldest to youngest:','4',NULL,'1','4','5'),
    ('What was the real name of the three eyed raven?','2',NULL,'0','4','1'),
    ('Words of the house Greyjoy:','0',NULL,'0','4','2.5'),
    ('Stark # Arryn # Lannister # Baratheon','6',NULL,'1','4','4'),
    ('Mia Wallace # Mr Pink # Hans Landa # California Mountain Snake', '6', NULL, '1', '5', '4'),
    ('Name the movie:', '3', 'https://i.pinimg.com/564x/b6/fd/a9/b6fda9cdc7cd7589ad23b8ba235cf6f6.jpg', '0', '5', '1'),
    ('What kind of burger does Jules eat in Pulp Fiction?', '0', NULL, '0', '5', '1.5'),
    ('What is the nickname of Lieutenant Aldo Raine', '2', NULL, '0', '5', '1'),
    ('Gentlemen, you had my #, now you have my #.', '1', NULL, '1', '5', '4'),
    ('Name song that appeared in \"Pulp Fiction\"', '5', NULL, '0', '5', '2'),
    ('Hydrogen # Oxygen # Sodium # Potassium','6',NULL,'1','6','2'),
    ('Covalent bond # Ionic bond # Hydrogen bond  # Metallic bond ','6',NULL,'1','6','4'),
    ('Which of the following elements is a noble gas?','2',NULL,'0','6','1'),
    ('Which of the following is a halogen?','2',NULL,'0','6','1.5'),
    ('Which of the following compounds are ionic?','5',NULL,'0','6','2'),
    ('Name two allotropes of carbon:','4',NULL,'1','6','2'),
    ('What is the pH of a neutral solution at 25Â°C?','0',NULL,'0','6','1'),
    ('The chemical formula for glucose is # and it is classified as a #.','1',NULL,'1','6','2'),
    ('Who is he?','3','https://images.newscientist.com/wp-content/uploads/2019/06/18142824/einstein.jpg','0','6','1'),
    ('Which of the following are considered Olympian gods? ', '5', NULL, '0', '7', '3'),
    ('Poseidon # Demeter # Artemis # Dionysus', '6', NULL, '1', '7', '4'),
    ('Who was punished by being tied to a wheel of fire for eternity?', '2', NULL, '0', '7', '3'),
    ('Which of the following were children of Zeus? ', '5', NULL, '0', '7', '2'),
    ('Name the three Gorgons:', '4', NULL, '0', '7', '6'),
    ('Which of the following are children of Cronus and Rhea? ', '5', NULL, '0', '7', '3'),
    ('Athena # Apollo  # Hermes  # Demeter', '6', NULL, '1', '7', '4'),
    ('The Greek hero # is known for slaying Medusa.', '1', NULL, '1', '7', '2'),
    ('Name the mythical creature:', '3', 'https://oldworldgods.com/wp-content/uploads/2023/11/chimera1.jpg', '0', '7', '2');


INSERT INTO answers (question_id, answer, answer_num)
VALUES
    (15, 'Brazil', -1),
    (16, 'Cape Town', -1),
    (16, 'Pretoria', -1),
    (16, 'Bloemfontein', -1),
    (17, 'Florida', 1),
    (17, 'Washington', 2),
    (17, 'North Dakota', 3),
    (17, 'Nebraska', 4),
    (17, 'Connecticut', 5),
    (18, 'Tashkent', 1),
    (18, 'Kyrgyzstan', 2),
    (19, 'Aconcagua', -1),
    (19, 'Angel Falls', -1),
    (19, 'Atacama Desert', -1),
    (20, 'Chicago', -1),
    (21, 'Ivory Coast', -1),
    (21, 'Cote d\'Ivoire', -1),
    (22, 'Vatican City', -1),
    (22, 'Tuvalu', 2),
    (22, 'Nauru', 3),
    (22, 'Palau', 4),
    (22, 'San Marino', 5),
    (23, 'Denmark', 1),
    ('24','High Garden','-1'),
    ('24','Highgarden','-1'),
    ('25','Bolton','-1'),
    ('25','Manderly','-1'),
    ('25','Umber','-1'),
    ('26','Nymeria','1'),
    ('26','Ghost','2'),
    ('26','Lady','3'),
    ('26','Greywind','4'),
    ('26','Summer','5'),
    ('26','Shaggydog','6'),
    ('27','Arthur Dayne','1'),
    ('27','Arthur','1'),
    ('27','Arthur of House Dayne','1'),
    ('28','Martell','-1'),
    ('28','House Martell','-1'),
    ('29','Robb','1'),
    ('29','Sansa','2'),
    ('29','Arya','3'),
    ('29','Bran','4'),
    ('29','Rickon','5'),
    ('30','Brynden Rivers','-1'),
    ('31','We do not sow','-1'),
    ('32','Winterfell','1'),
    ('32','Eyrie','2'),
    ('32','Casterly Rock','3'),
    ('32','Storm\'s End','4'),
    ('33', 'Pulp Fiction', '1'),
    ('33', 'Reservoir Dogs', '2'),
    ('33', 'Inglourious Basters', '3'),
    ('33', 'Kill Bill', '4'),
    ('34', 'Pulp Fiction', '-1'),
    ('35', 'Big Kahuna Burger', '-1'),
    ('36', 'The Apache', '-1'),
    ('37', 'curiosity', '1'),
    ('37', 'attention', '2'),
    ('38', '\"You Never Can Tell\" by Chuck Berry', '-1'),
    ('38', '\"Flowers On the Wall\" by The Statler Brothers', '-1'),
    ('39','H','1'),
    ('39','O','2'),
    ('39','Na','3'),
    ('39','K','4'),
    ('40','Involves the sharing of electron pairs between atoms.','1'),
    ('40','Attraction between oppositely charged ions.','2'),
    ('40','Weak bond between a hydrogen atom and an electronegative atom.','3'),
    ('40','Attraction between positively charged metal ions and delocalized electrons.','4'),
    ('41','Argon','-1'),
    ('42','Fluorine','-1'),
    ('43','NaCl','-1'),
    ('43','MgO','-1'),
    ('44','Diamond','1'),
    ('44','Graphite','2'),
    ('45','7','-1'),
    ('46','C6H12O6','1'),
    ('46','carbohydrate','2'),
    ('47','Albert Einstein','-1'),
    ('47','Vigac chibuxiani kaci. rato xatia ar vici','-1'),
    ('48','Hermes','-1'),
    ('48','Hestia','-1'),
    ('48','Hera','-1'),
    ('49','The Sea','1'),
    ('49','Agriculture','2'),
    ('49','The Hunt','3'),
    ('49','Wine and Festivity','4'),
    ('50','Ixion','-1'),
    ('51','Hermes','-1'),
    ('51','Athena','-1'),
    ('52','Stheno','-1'),
    ('52','Euryale','-1'),
    ('52','Medusa','-1'),
    ('53','Zeus','-1'),
    ('53','Hera','-1'),
    ('53','Hestia','-1'),
    ('54','Owl','1'),
    ('54','Lyre','2'),
    ('54','Caduceus','3'),
    ('54','Cornucopia','4'),
    ('55','Perseus','1'),
    ('56','Chimera','-1');



INSERT INTO options (question_id, answer)
VALUES
    (17, 'Florida'),
    (17, 'Washington'),
    (17, 'North Dakota'),
    (17, 'Nebraska'),
    (17, 'Connecticut'),
    (19, 'Aconcagua'),
    (19, 'Angel Falls'),
    (19, 'Atacama Desert'),
    (19, 'Fuji'),
    (19, 'Gobi Desert'),
    (19, 'Murray River'),
    (22, 'Vatican City'),
    (22, 'Tuvalu'),
    (22, 'Nauru'),
    (22, 'Palau'),
    (22, 'San Marino'),
    (23, 'Denmark'),
    (23, 'USA'),
    (23, 'Norway'),
    (23, 'Iceland'),
    ('25','Bolton'),
    ('25','Manderly'),
    ('25','Umber'),
    ('25','Blackwood'),
    ('25','Dayne'),
    ('25','Westerling'),
    ('26','Nymeria'),
    ('26','Ghost'),
    ('26','Lady'),
    ('26','Greywind'),
    ('26','Summer'),
    ('26','Shaggydog'),
    ('29','Robb'),
    ('29','Sansa'),
    ('29','Arya'),
    ('29','Bran'),
    ('29','Rickon'),
    ('30','Brynden Rivers'),
    ('30','Aemond Targaryen'),
    ('30','Jojen Reed'),
    ('30','Balerion The Dread'),
    ('32','Winterfell'),
    ('32','Eyrie'),
    ('32','Casterly Rock'),
    ('32','Storm\'s End'),
    ('33','Pulp Fiction'),
    ('33','Reservoir Dogs'),
    ('33','Inglourious Basters'),
    ('33','Kill Bill'),
    ('36','The Apache'),
    ('36','Handsome'),
    ('36','Jew Bear'),
    ('36','Nazi Killer'),
    ('38','\"You Never Can Tell\" by Chuck Berry'),
    ('38','\"Flowers On the Wall\" by The Statler Brothers'),
    ('38','\"Little Green Bag\" by George Baker'),
    ('38','\"Johnny B. Goode\" by Chuck Berry'),
    ('39','H'),
    ('39','O'),
    ('39','Na'),
    ('39','K'),
    ('40','Involves the sharing of electron pairs between atoms.'),
    ('40','Attraction between oppositely charged ions.'),
    ('40','Weak bond between a hydrogen atom and an electronegative atom.'),
    ('40','Attraction between positively charged metal ions and delocalized electrons.'),
    ('41','Argon'),
    ('41','Nitrogen'),
    ('41','Chlorine'),
    ('41','Sodium'),
    ('42','Fluorine'),
    ('42','Helium'),
    ('42','Carbon'),
    ('42','Magnesium'),
    ('43','NaCl'),
    ('43','MgO'),
    ('43','H2O'),
    ('43','CO2'),
    ('44','Diamond'),
    ('44','Graphite'),
    ('48','Hermes'),
    ('48','Hestia'),
    ('48','Hera'),
    ('48','Hades'),
    ('49','The Sea'),
    ('49','Agriculture'),
    ('49','The Hunt'),
    ('49','Wine and Festivity'),
    ('50','Ixion'),
    ('50','Tantalus'),
    ('50','Sisyphus'),
    ('50','Prometheus'),
    ('51','Hermes'),
    ('51','Athena'),
    ('51','Persephone'),
    ('51','Aphrodite'),
    ('52','Stheno'),
    ('52','Euryale'),
    ('52','Medusa'),
    ('53','Zeus'),
    ('53','Hera'),
    ('53','Hestia'),
    ('53','Apollo'),
    ('54','Owl'),
    ('54','Lyre'),
    ('54','Caduceus'),
    ('54','Cornucopia');



INSERT INTO tags(tag_name, quiz_id)
VALUES
    ('geography','3'),
    ('flags','3'),
    ('gia chanturia','3'),
    ('ivory coast','3'),
    ('continents','3'),
    ('countries','3'),
    ('capitals','3'),
    ('books','4'),
    ('game of thrones','4'),
    ('got','4'),
    ('asoiaf','4'),
    ('books','4'),
    ('game of thrones','4'),
    ('got','4'),
    ('a song of ice and fire','4'),
    ('George RR Martin','4'),
    ('Jon Snow','4'),
    ('Stark','4'),
    ('Targaryen','4'),
    ('Westeros','4'),
    ('Tarantino','5'),
    ('Movies','5'),
    ('Violence','5'),
    ('90s','5'),
    ('Pulp Fiction','5'),
    ('GOAT','5'),
    ('Blood','5'),
    ('Chemistry','6'),
    ('Science','6'),
    ('Atoms','6'),
    ('Mythology','7'),
    ('Greece','7'),
    ('Hades','7'),
    ('Zeus','7'),
    ('Gods','7');

-- gios new quizzes
INSERT INTO quizzes(quiz_name, description, picture_url, creator_username, created_time, multi_page, category, views, taken)
VALUES
    ('Journey Through History', 'Explore pivotal events and notable figures from history in this engaging seven-question quiz.', 'https://ww1.oswego.edu/history/sites/history/files/styles/16x9_xl/public/2020-12/Historyheaderimage.jpg?h=07e9ee27&itok=gFloL8E9', 'Bacha', '2024-06-18 01:48:46', 1, 2, 19, 3),
    ('European Languages Exploration', 'Discover the diversity of European languages through varied question types, including history, identification, and matching.', 'https://lighthouseonline.com/wp-content/uploads/2023/06/Wooden-signpost-with-languages-1024x683.jpeg' , 'Bacha', '2024-06-18 02:48:46', 0, 3, 20, 5);

INSERT INTO questions (question_text, question_type, picture_url, sorted_relevant, quiz_id, score)
VALUES
    ('Who was the first President of the United States?', 0, null, 0, 8, 1),
    ('# wrote the famous diary during World War II while hiding from the Nazis.', 1, null, 1, 8, 1),
    ('Which battle is considered the turning point of the American Civil War?', 2, null, 0, 8, 1),
    ('Name the British Prime Minister during World War II', 3, 'https://hips.hearstapps.com/hmg-prod/images/winston-churchill-9248164-1-402.jpg', 0, 8, 1),
    ('Mahatma Gandhi # Marie Curie # Leonardo da Vinci', 6, null, 1, 8, 1),
    ('Which of the following events contributed to the outbreak of World War I?', 4, null, 1, 8, 1),
    ('Which countries were part of the Allied Powers during World War II?', 5, null, 0, 8, 1),
    ('What is the term for a language that is no longer spoken by any community?', 0, null, 0, 9, 1),
    ('The official language of Portugal is #', 1, null, 1, 9, 1),
    ('Which of the following languages is a Uralic language?', 2, null, 0, 9, 1),
    ('Identify the language from the following text:', 3, 'https://store.dailystoic.com/cdn/shop/products/Memento-mori.png?v=1619113772', 0, 9, 1),
    ('Which languages use the Cyrillic alphabet?', 4, null, 0, 9, 1),
    ('Which of the following languages have cases in their grammatical structure?', 5, null, 0, 9, 1),
    ('Finnish # Greek # Polish # Hungarian', 6, null, 1, 9, 1);

INSERT INTO answers (question_id, answer, answer_num)
VALUES
    (57, 'George Washington', -1),
    (58, 'Anne Frank', 1),
    (59, 'Gettysburg', -1),
    (60, 'Winston Churchill', -1),
    (61, 'Led India''s independence movement', 1),
    (61, 'Discovered radioactivity', 2),
    (61, 'Painted the Mona Lisa', 3),
    (62, 'Assassination of Archduke Franz Ferdinand', 1),
    (62, 'Naval Arms Race', 2),
    (63, 'United States', -1),
    (63, 'Soviet Union', -1),
    (63, 'United Kingdom', -1),
    (64, 'Extinct', -1),
    (65, 'Portuguese', 1),
    (66, 'Estonian', -1),
    (67, 'Latin', -1),
    (68, 'Serbian', -1),
    (68, 'Bulgarian', -1),
    (68, 'Polish', -1),
    (69, 'Finnish', -1),
    (69, 'Hungarian', -1),
    (69, 'German', -1),
    (70, 'Finland', 1),
    (70, 'Greece', 2),
    (70, 'Poland', 3),
    (70, 'Hungary', 4);

INSERT INTO options (question_id, answer)
VALUES
    (59, 'Gettysburg'),
    (59, 'Didgori'),
    (59, 'Arikara'),
    (59, 'Winnebago'),
    (61, 'Led India''s independence movement'),
    (61, 'Discovered radioactivity'),
    (61, 'Painted Mona Lisa'),
    (62, 'Assassination of Archduke Franz Ferdinand'),
    (62, 'Naval Arms Race'),
    (63, 'United States'),
    (63, 'Soviet Union'),
    (63, 'United Kingdom'),
    (63, 'Japan'),
    (63, 'Germany'),
    (66, 'Estonian'),
    (66, 'French'),
    (66, 'German'),
    (66, 'Greek'),
    (68, 'Serbian'),
    (68, 'Bulgarian'),
    (68, 'Polish'),
    (69, 'Finnish'),
    (69, 'Hungarian'),
    (69, 'German'),
    (69, 'English'),
    (70, 'Finland'),
    (70, 'Greece'),
    (70, 'Poland'),
    (70, 'Hungary');

INSERT INTO tags(tag_name, quiz_id)
VALUES
    ('history', 8),
    ('years', 8),
    ('past', 8),
    ('language', 9),
    ('historyOfLanguage', 9),
    ('europeanLanguages', 9);



-- nikas quiz
INSERT INTO quizzes(quiz_name, description, picture_url, creator_username, created_time, multi_page, category, views, taken)
VALUES
    ('Music-Freak', 'From classical to hip-hop', 'https://rukminim2.flixcart.com/image/850/1000/l01blow0/poster/2/w/z/medium-music-wallpaper-on-fine-art-paper-theme-images-hd-original-imagbx2phbqcnzym.jpeg?q=90&crop=false', 'kingslayer', '2024-07-01 14:48:46', 1, 7, 15, 13);


INSERT INTO questions (question_text, question_type, picture_url, sorted_relevant, quiz_id, score)
VALUES
    ('The Rolling Stones # Aerosmith # The Beach Boys # Guns N'' Roses # Madonna # The Police', 6, null, 1, 10, 6),
    ('Which of these are(is) NOT the subtitle of a Haydn symphony?', 5, null, 0, 10, 1.5),
    ('Who wrote a piece called the ''Skittle Alley Trio''?', 0, null, 0, 10, 1),
    ('What is Beyonce''s fanbase called?', 2, null, 0, 10, 1),
    ('Name this musician', 3, 'https://img.playbuzz.com/image/upload/ar_1.5,c_pad,f_jpg,b_auto/q_auto:best,f_auto,fl_lossy,w_640,c_limit,dpr_2/cdn/289c3023-e967-4be0-a34c-6cc414caca80/5553be6b-63b6-43ac-8e34-1eb7610a3fc8.jpg', 0, 10, 0.5),
    ('#  is the rapper who was famously shot to death in #  in 1996.', 1, null, 1, 10, 2),
    ('Name all members of The Beatles', 4, null, 0, 10, 4);

INSERT INTO answers (question_id, answer, answer_num)
VALUES
    (71, 'Jumpin'' Jack Flash', 1),
    (71, 'Dream On', 2),
    (71, 'Surfin'' USA', 3),
    (71, 'Welcome to the Jungle', 4),
    (71, 'Like a Virgin', 5),
    (71, 'Roxanne', 6),
    (72, 'Tragic', -1),
    (73, 'Mozart', -1),
    (73, 'Wolfgang Amadeus Mozart', -1),
    (74, 'Bee Hive', -1),
    (75, 'Beethoven', -1),
    (76, 'Tupac', 1),
    (76, 'tupac shakur', 1),
    (76, 'Las Vegas', 2),
    (76, 'Vegas', 2),
    (77, 'John Lennon', -1),
    (77, 'Paul McCartney', -1),
    (77, 'George Harrison', -1),
    (77, 'Ringo Starr', -1);

INSERT INTO options (question_id, answer)
VALUES
    (71, 'Jumpin'' Jack Flash'),
    (71, 'Dream On'),
    (71, 'Surfin'' USA'),
    (71, 'Welcome to the Jungle'),
    (71, 'Like a Virgin'),
    (71, 'Roxanne'),
    (72, 'Tragic'),
    (72, 'Hornsignal'),
    (72, 'Lamentatione'),
    (72, 'Mercury'),
    (74, 'Bey Hive'),
    (74, 'Beybies'),
    (74, 'It doesn''t exist'),
    (74, 'Swifties'),
    (77, 'John Lennon'),
    (77, 'Paul McCartney'),
    (77, 'George Harrison'),
    (77, 'Ringo Starr');


INSERT INTO tags(tag_name, quiz_id)
VALUES
    ('history', 8),
    ('years', 8),
    ('past', 8),
    ('language', 9),
    ('historyOfLanguage', 9),
    ('europeanLanguages', 9),
    ('piano', 10),
    ('rap', 10),
    ('taylowswift', 10);




INSERT INTO quiz_history(quiz_id, quiz_name, picture_url, username, score, time_needed, take_date)
VALUES
    ('4','Asoiaf', 'https://awoiaf.westeros.org/images/thumb/f/fc/Cristi_Balanescu_wolves_of_the_north_cover.jpg/900px-Cristi_Balanescu_wolves_of_the_north_cover.jpg', 'almasxit','24.5','82','2024-06-02 10:24:57'),
    ('4','Asoiaf', 'https://awoiaf.westeros.org/images/thumb/f/fc/Cristi_Balanescu_wolves_of_the_north_cover.jpg/900px-Cristi_Balanescu_wolves_of_the_north_cover.jpg', 'user1','20.5','42','2024-06-10 10:24:57'),
    ('4','Asoiaf', 'https://awoiaf.westeros.org/images/thumb/f/fc/Cristi_Balanescu_wolves_of_the_north_cover.jpg/900px-Cristi_Balanescu_wolves_of_the_north_cover.jpg', 'user6','24.5','182','2024-06-18 10:24:57'),
    ('4','Asoiaf', 'https://awoiaf.westeros.org/images/thumb/f/fc/Cristi_Balanescu_wolves_of_the_north_cover.jpg/900px-Cristi_Balanescu_wolves_of_the_north_cover.jpg', 'user2','10','105','2024-06-29 18:44:27'),
    ('1','Mixed Quiz', 'https://viralsolutions.net/wp-content/uploads/2019/06/shutterstock_749036344.jpg', 'almasxit','14','42','2024-06-05 9:24:57'),
    ('1', 'Mixed Quiz', 'https://viralsolutions.net/wp-content/uploads/2019/06/shutterstock_749036344.jpg', 'Bacha', '16', '53', '2024-06-06 18:34:56'),
    ('1', 'Mixed Quiz', 'https://viralsolutions.net/wp-content/uploads/2019/06/shutterstock_749036344.jpg', 'user3', '12', '63', '2024-06-15 20:54:56'),
    ('1', 'Mixed Quiz', 'https://viralsolutions.net/wp-content/uploads/2019/06/shutterstock_749036344.jpg', 'user9', '13', '23', '2024-06-20 12:44:06'),
    ('2', 'Barcelona', 'https://d1ymz67w5raq8g.cloudfront.net/Pictures/480xany/6/5/5/509655_shutterstock_1506580442_769367.jpg', 'Bacha', '7', '12', '2024-06-05 17:55:23'),
    ('2', 'Barcelona', 'https://d1ymz67w5raq8g.cloudfront.net/Pictures/480xany/6/5/5/509655_shutterstock_1506580442_769367.jpg', 'user8', '4', '24', '2024-06-11 14:28:35'),
    ('2', 'Barcelona', 'https://d1ymz67w5raq8g.cloudfront.net/Pictures/480xany/6/5/5/509655_shutterstock_1506580442_769367.jpg', 'user1', '2', '34', '2024-07-03 13:19:56'),
    ('2', 'Barcelona', 'https://d1ymz67w5raq8g.cloudfront.net/Pictures/480xany/6/5/5/509655_shutterstock_1506580442_769367.jpg', 'user6', '5', '19', '2024-07-07 23:00:15'),
    ('3', 'Geography!', 'https://study.com/cimages/videopreview/p3c2j8y73a.jpg', 'almasxit', '26.5', '80', '2024-07-01 10:41:17'),
    ('3', 'Geography!', 'https://study.com/cimages/videopreview/p3c2j8y73a.jpg', 'kingslayer', '26.5', '180', '2024-06-28 10:41:17'),
    ('3', 'Geography!', 'https://study.com/cimages/videopreview/p3c2j8y73a.jpg', 'user8', '20.5', '151', '2024-07-03 19:41:17'),
    ('3', 'Geography!', 'https://study.com/cimages/videopreview/p3c2j8y73a.jpg', 'user2', '16', '217', '2024-07-08 21:41:17'),
    ('3', 'Geography!', 'https://study.com/cimages/videopreview/p3c2j8y73a.jpg', 'user7', '7', '104', '2024-06-20 23:51:17'),
    ('5', 'Tarantino', 'https://media.tenor.com/3160R1pQhaEAAAAM/tarantino-sunglasses.gif', 'almasxit', '13.5', '66', '2024-06-11 10:44:08'),
    ('5', 'Tarantino', 'https://media.tenor.com/3160R1pQhaEAAAAM/tarantino-sunglasses.gif', 'user1', '6', '79', '2024-06-15 13:44:08'),
    ('5', 'Tarantino', 'https://media.tenor.com/3160R1pQhaEAAAAM/tarantino-sunglasses.gif', 'user5', '9', '102', '2024-06-19 05:34:08'),
    ('5', 'Tarantino', 'https://media.tenor.com/3160R1pQhaEAAAAM/tarantino-sunglasses.gif', 'user7', '3', '145', '2024-07-05 01:24:08'),
    ('5', 'Tarantino', 'https://media.tenor.com/3160R1pQhaEAAAAM/tarantino-sunglasses.gif', 'user9', '7', '234', '2024-07-06 16:39:08'),
    ('7', 'Greek Mythology', 'https://images.squarespace-cdn.com/content/v1/5e01d4d4c4faa308239ed7b6/d4c98111-df13-428e-a209-a2bd5b95eae2/ws1ovyxqlq9afvgumsdd.jpg', 'almasxit', '28', '69', '2024-06-20 10:46:41'),
    ('7', 'Greek Mythology', 'https://images.squarespace-cdn.com/content/v1/5e01d4d4c4faa308239ed7b6/d4c98111-df13-428e-a209-a2bd5b95eae2/ws1ovyxqlq9afvgumsdd.jpg', 'user2', '17', '77', '2024-06-21 18:06:38'),
    ('7', 'Greek Mythology', 'https://images.squarespace-cdn.com/content/v1/5e01d4d4c4faa308239ed7b6/d4c98111-df13-428e-a209-a2bd5b95eae2/ws1ovyxqlq9afvgumsdd.jpg', 'user4', '12', '81', '2024-07-03 07:16:22'),
    ('7', 'Greek Mythology', 'https://images.squarespace-cdn.com/content/v1/5e01d4d4c4faa308239ed7b6/d4c98111-df13-428e-a209-a2bd5b95eae2/ws1ovyxqlq9afvgumsdd.jpg', 'user7', '14', '100', '2024-07-02 12:56:41'),
    ('6', 'Chemistry', 'https://i.pinimg.com/564x/cc/3d/fb/cc3dfbf61463a9b3aeaa4cdc27a1495b.jpg', 'kingslayer', '16.5', '77', '2024-06-16 19:48:46'),
    ('6', 'Chemistry', 'https://i.pinimg.com/564x/cc/3d/fb/cc3dfbf61463a9b3aeaa4cdc27a1495b.jpg', 'user1', '12', '93', '2024-06-18 11:48:46'),
    ('6', 'Chemistry', 'https://i.pinimg.com/564x/cc/3d/fb/cc3dfbf61463a9b3aeaa4cdc27a1495b.jpg', 'user3', '10.5', '83', '2024-06-21 12:48:46'),
    ('6', 'Chemistry', 'https://i.pinimg.com/564x/cc/3d/fb/cc3dfbf61463a9b3aeaa4cdc27a1495b.jpg', 'user6', '13', '103', '2024-06-25 16:48:46'),
    ('6', 'Chemistry', 'https://i.pinimg.com/564x/cc/3d/fb/cc3dfbf61463a9b3aeaa4cdc27a1495b.jpg', 'user2', '5', '44', '2024-07-07 01:48:46'),
    ('8', 'Journey Through History', 'https://ww1.oswego.edu/history/sites/history/files/styles/16x9_xl/public/2020-12/Historyheaderimage.jpg?h=07e9ee27&itok=gFloL8E9', 'Bacha', '7', '54', '2024-07-01 01:48:46'),
    ('8', 'Journey Through History', 'https://ww1.oswego.edu/history/sites/history/files/styles/16x9_xl/public/2020-12/Historyheaderimage.jpg?h=07e9ee27&itok=gFloL8E9', 'user4', '3', '44', '2024-07-02 01:48:46'),
    ('8', 'Journey Through History', 'https://ww1.oswego.edu/history/sites/history/files/styles/16x9_xl/public/2020-12/Historyheaderimage.jpg?h=07e9ee27&itok=gFloL8E9', 'user8', '4', '44', '2024-07-03 01:48:46'),

    ('10', 'Music-Freak', 'https://rukminim2.flixcart.com/image/850/1000/l01blow0/poster/2/w/z/medium-music-wallpaper-on-fine-art-paper-theme-images-hd-original-imagbx2phbqcnzym.jpeg?q=90&crop=false', 'kingslayer', '12', '198', '2024-07-04 06:18:06'),
    ('10', 'Music-Freak', 'https://rukminim2.flixcart.com/image/850/1000/l01blow0/poster/2/w/z/medium-music-wallpaper-on-fine-art-paper-theme-images-hd-original-imagbx2phbqcnzym.jpeg?q=90&crop=false', 'user1', '8', '122', '2024-07-05 06:18:00'),
    ('10', 'Music-Freak', 'https://rukminim2.flixcart.com/image/850/1000/l01blow0/poster/2/w/z/medium-music-wallpaper-on-fine-art-paper-theme-images-hd-original-imagbx2phbqcnzym.jpeg?q=90&crop=false', 'almasxit', '14', '99', '2024-07-05 08:48:11'),
    ('10', 'Music-Freak', 'https://rukminim2.flixcart.com/image/850/1000/l01blow0/poster/2/w/z/medium-music-wallpaper-on-fine-art-paper-theme-images-hd-original-imagbx2phbqcnzym.jpeg?q=90&crop=false', 'Bacha', '11', '46', '2024-07-06 09:01:47'),
    ('10', 'Music-Freak', 'https://rukminim2.flixcart.com/image/850/1000/l01blow0/poster/2/w/z/medium-music-wallpaper-on-fine-art-paper-theme-images-hd-original-imagbx2phbqcnzym.jpeg?q=90&crop=false', 'user8', '12.5', '303', '2024-07-06 10:18:32');

INSERT INTO rate(quiz_id, username, review, rating)
VALUES
    (1, 'user3', 'Very Nice!(Borat Voice)', 5),
    (1, 'user9', 'Enjoyed', 4),
    (1, 'Bacha', 'Madloba Didi', 5),

    (2, 'user1', 'I dont watch sports', 3),
    (2, 'user6', 'GOAT', 5),
    (2, 'user8', 'MESSSSSSSSSSIIIIII GOOOOAAATT', 5),

    (3, 'user2', 'San Marino Rules', 5),
    (3, 'user7', 'Thought it was Ireland', 4),
    (3, 'user8', 'Great', 5),

    (4, 'user1', 'Wow', 5),
    (4, 'user2', 'I did not any of this', 2),
    (4, 'user6', 'HOUSE MARTELL', 5),

    (5, 'user1', 'Goatino', 5),
    (5, 'user5', 'LOVED IT', 5),
    (5, 'user9', 'Me komaroveli var da nolani mirchevnia', 2),

    (6, 'user1', 'Say My Name', 5),
    (6, 'user3', 'Einstein is not a chemist >:(', 3),
    (6, 'user6', 'Too hard but not bad', 4),

    (7, 'user2', 'LOVE HADES', 5),
    (7, 'user4', 'I prefer the roman empire', 3),
    (7, 'user7', 'NICEEEE', 4),

    (8, 'Bacha', 'It is my quiz, I like it', 5),
    (8, 'user4', 'I dont like history', 3),
    (8, 'user8', 'very nice', 4),

    (8, 'Bacha', 'It is my quiz, I like it', 5),
    (8, 'user4', 'I dont like history', 3),
    (8, 'user8', 'very nice', 4),

    (10, 'almasxit', 'Well Done', 5),
    (10, 'user1', 'Loved IT', 5),
    (10, 'Bacha', 'Great Job', 5);


