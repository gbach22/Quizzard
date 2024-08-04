package DatabaseTests;

import org.junit.Before;
import org.junit.Test;
import quiz_web.Database.DbConnection;
import quiz_web.Database.QuizDbManager;
import quiz_web.Models.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

// To test line coverage in Modify Quiz Run you have to add class Quiz from models and QuizDbManager from Database

public class QuizDbManagerTests {
    QuizDbManager db;
    DbConnection dbCon;

    @Before
    public void setUp() throws ClassNotFoundException, SQLException {
        dbCon = new DbConnection();
        dbCon.runSqlFile("testQuiz.sql");
        db = new QuizDbManager(dbCon.getConnection(), true);
    }

    @Test
    public void testGetQuizById() throws SQLException {
        ArrayList<Quiz> quizzes = new ArrayList<>(
                Arrays.asList(
                        (new Quiz(1, "Mixed Quiz", "This quiz contains a question of each type",
                                "almasxit", "https://viralsolutions.net/wp-content/uploads/2019/06/shutterstock_749036344.jpg",
                                true, true, true, true, null, Categories.MIXED, 6, 4)),
                        (new Quiz(2, "Barcelona", "How well do you know the history of FC Barcelona?",
                                "Bacha", "https://d1ymz67w5raq8g.cloudfront.net/Pictures/480xany/6/5/5/509655_shutterstock_1506580442_769367.jpg",
                                false, true, true, true, null, Categories.SPORTS, 17, 4)),
                        (new Quiz(3, "Geography!", "This quiz contains different question about geography, including flags, countries, states, capitals and so on",
                                "almasxit", "https://study.com/cimages/videopreview/p3c2j8y73a.jpg", true, true, true, true, null, Categories.GEOGRAPHY, 15, 4))));

        for (int i = 0; i < 3; i++) {
            Quiz resultQuiz = db.getQuizById(i + 1);
            Quiz quiz = quizzes.get(i);

            System.out.println(resultQuiz.toString());

            assertEquals(quiz.getQuizId(), resultQuiz.getQuizId());
            assertEquals(quiz.getQuizName(), resultQuiz.getQuizName());
            assertEquals(quiz.getQuizDescription(), resultQuiz.getQuizDescription());
            assertEquals(quiz.getCreatorUsername(), resultQuiz.getCreatorUsername());
            assertEquals(quiz.getPictureUrl(), resultQuiz.getPictureUrl());
            assertEquals(quiz.isMultiPage(), resultQuiz.isMultiPage());
            assertEquals(quiz.isRandom(), resultQuiz.isRandom());
            assertEquals(quiz.isImmediateCorrection(), resultQuiz.isImmediateCorrection());
            assertEquals(quiz.isPracticeMode(), resultQuiz.isPracticeMode());
            assertEquals(quiz.getCategory(), resultQuiz.getCategory());
            assertEquals(quiz.getViews(), resultQuiz.getViews());
            assertEquals(quiz.getTaken(), resultQuiz.getTaken());
        }
    }

    @Test
    public void testGetQuizByUser() throws SQLException {
        dbCon.runSqlFile("testQuiz.sql");
        List<Quiz> quizzesFromDb = db.getQuizzesByUser("Bacha", -1);
        assertEquals(3, quizzesFromDb.size());
        ArrayList<Quiz> quizzes = new ArrayList<>(
                Arrays.asList(
                        (new Quiz(2, "Barcelona", "How well do you know the history of FC Barcelona?",
                                "Bacha", "https://d1ymz67w5raq8g.cloudfront.net/Pictures/480xany/6/5/5/509655_shutterstock_1506580442_769367.jpg",
                                false, true, true, true, null, Categories.SPORTS, 17, 4)),
                        (new Quiz(8, "Journey Through History", "Explore pivotal events and notable figures from history in this engaging seven-question quiz.",
                                "Bacha", "https://ww1.oswego.edu/history/sites/history/files/styles/16x9_xl/public/2020-12/Historyheaderimage.jpg?h=07e9ee27&itok=gFloL8E9",
                                true, true, true, true, null, Categories.HISTORY, 19, 3)),
                        (new Quiz(9, "European Languages Exploration", "Discover the diversity of European languages through varied question types, including history, identification, and matching.",
                                "Bacha", "https://lighthouseonline.com/wp-content/uploads/2023/06/Wooden-signpost-with-languages-1024x683.jpeg", false, true, true, true, null, Categories.LANGUAGE, 20, 0))));

        for (int i = 0; i < 3; i++) {
            Quiz resultQuiz = quizzesFromDb.get(i);
            Quiz quiz = quizzes.get(i);

            assertEquals(quiz.getQuizId(), resultQuiz.getQuizId());
            assertEquals(quiz.getQuizName(), resultQuiz.getQuizName());
            assertEquals(quiz.getQuizDescription(), resultQuiz.getQuizDescription());
            assertEquals(quiz.getCreatorUsername(), resultQuiz.getCreatorUsername());
            assertEquals(quiz.getPictureUrl(), resultQuiz.getPictureUrl());
            assertEquals(quiz.isMultiPage(), resultQuiz.isMultiPage());
            assertEquals(quiz.isRandom(), resultQuiz.isRandom());
            assertEquals(quiz.isImmediateCorrection(), resultQuiz.isImmediateCorrection());
            assertEquals(quiz.isPracticeMode(), resultQuiz.isPracticeMode());
            assertEquals(quiz.getCategory(), resultQuiz.getCategory());
            assertEquals(quiz.getViews(), resultQuiz.getViews());
            assertEquals(quiz.getTaken(), resultQuiz.getTaken());
        }

        quizzesFromDb = db.getQuizzesByUser("Bacha", 2);

        quizzes = new ArrayList<>(
                Arrays.asList(
                        (new Quiz(2, "Barcelona", "How well do you know the history of FC Barcelona?",
                                "Bacha", "https://d1ymz67w5raq8g.cloudfront.net/Pictures/480xany/6/5/5/509655_shutterstock_1506580442_769367.jpg",
                                false, true, true, true, null, Categories.SPORTS, 17, 4)),
                        (new Quiz(9, "European Languages Exploration", "Discover the diversity of European languages through varied question types, including history, identification, and matching.",
                                "Bacha", "https://lighthouseonline.com/wp-content/uploads/2023/06/Wooden-signpost-with-languages-1024x683.jpeg", false, true, true, true, null, Categories.LANGUAGE, 20, 0))));

        assertEquals(2, quizzesFromDb.size());
        for (int i = 0; i < 2; i++) {
            Quiz resultQuiz = quizzesFromDb.get(i);
            Quiz quiz = quizzes.get(i);

            assertEquals(quiz.getQuizId(), resultQuiz.getQuizId());
            assertEquals(quiz.getQuizName(), resultQuiz.getQuizName());
            assertEquals(quiz.getQuizDescription(), resultQuiz.getQuizDescription());
            assertEquals(quiz.getCreatorUsername(), resultQuiz.getCreatorUsername());
            assertEquals(quiz.getPictureUrl(), resultQuiz.getPictureUrl());
            assertEquals(quiz.isMultiPage(), resultQuiz.isMultiPage());
            assertEquals(quiz.isRandom(), resultQuiz.isRandom());
            assertEquals(quiz.isImmediateCorrection(), resultQuiz.isImmediateCorrection());
            assertEquals(quiz.isPracticeMode(), resultQuiz.isPracticeMode());
            assertEquals(quiz.getCategory(), resultQuiz.getCategory());
            assertEquals(quiz.getViews(), resultQuiz.getViews());
            assertEquals(quiz.getTaken(), resultQuiz.getTaken());
        }

    }

    @Test
    public void testGetQuizQuestions() throws SQLException {
        List<Question> questions = new ArrayList<>(
                Arrays.asList(
                        new Question(1, "What is the favorite dish of Tony Soprano?", questionTypes.QUESTION_RESPONSE,
                                null, false, 1, 1),
                        new Question(2, "Which is NOT a song by Kanye West?", questionTypes.MULTIPLE_CHOICE,
                                null, false, 1, 1),
                        new Question(3, "Name top 5 countries with the highest gdp", questionTypes.MULTI_ANSWER,
                                null, true, 1, 5),
                        new Question(4, "Uruguay # Nepal # Kenya # Cuba", questionTypes.MATCHING,
                                null, true, 1, 4),
                        new Question(5, "Select all movies by Gaspar Noe", questionTypes.MULTI_CHOICE_MULTI_ANS,
                                null, false, 1, 3),
                        new Question(6, "The photo shows the best food in the world, name it", questionTypes.PICTURE_RESPONSE,
                                "https://cdn.tasteatlas.com/Images/Dishes/d9ae0ef06bc54f7cb4e6b5b928bc6f41.jpg", false, 1, 1),
                        new Question(7, "The national football team of Georgia will defeat # in the first game in the group stage of the European Championship and gain # points.", questionTypes.FILL_IN_THE_BLANK,
                                null, true, 1, 1)
                ));

        List<Question> questionsFromDb = db.getQuizQuestions(1);
        assertEquals(7, questionsFromDb.size());

        for (int i = 0; i < questions.size(); i++) {
            Question questionFromDb = questionsFromDb.get(i);
            Question question = questions.get(i);

            assertEquals(question.getQuestionId(), questionFromDb.getQuestionId());
            assertEquals(question.getQuestionText(), questionFromDb.getQuestionText());
            assertEquals(question.getQuestionType(), questionFromDb.getQuestionType());
            assertEquals(question.getPictureUrl(), questionFromDb.getPictureUrl());
            assertEquals(question.isSortedRelevant(), questionFromDb.isSortedRelevant());
            assertEquals(question.getQuizId(), questionFromDb.getQuizId());
            assertEquals(question.getPoint(), questionFromDb.getPoint(), 0);
        }
    }

    @Test
    public void getGetOptions() throws SQLException {
        List<String> optionsFromDb = db.getOptions(17);
        List<String> options = new ArrayList<>(Arrays.asList("Florida", "Washington", "North Dakota", "Nebraska", "Connecticut"));

        assertEquals(optionsFromDb.size(), options.size());
        for (int i = 0; i < options.size(); i++) {
            assertEquals(options.get(i), optionsFromDb.get(i));
        }

        optionsFromDb = db.getOptions(19);
        options = new ArrayList<>(Arrays.asList("Aconcagua", "Angel Falls", "Atacama Desert", "Fuji", "Gobi Desert", "Murray River"));

        assertEquals(optionsFromDb.size(), options.size());
        for (int i = 0; i < options.size(); i++) {
            assertEquals(options.get(i), optionsFromDb.get(i));
        }

        optionsFromDb = db.getOptions(23);
        options = new ArrayList<>(Arrays.asList("Denmark", "USA", "Norway", "Iceland"));

        assertEquals(optionsFromDb.size(), options.size());
        for (int i = 0; i < options.size(); i++) {
            assertEquals(options.get(i), optionsFromDb.get(i));
        }
    }

    @Test
    public void testGetAnwers() throws SQLException {
        List<Answer> answersFromDb = db.getAnswers(22);
        List<Answer> answers = new ArrayList<>(Arrays.asList(
                (new Answer(22, "Vatican City", 1)),
                (new Answer(22, "Tuvalu", 2)),
                (new Answer(22, "Nauru", 3)),
                (new Answer(22, "Palau", 4)),
                (new Answer(22, "San Marino", 5))));

        assertEquals(answersFromDb.size(), answers.size());
        for (int i = 0; i < answers.size(); i++) {
            Answer answer = answers.get(i);
            Answer answerFromDb = answersFromDb.get(i);
            assertEquals(answer.getQuestionId(), answerFromDb.getQuestionId());
            assertEquals(answer.getAnswer(), answerFromDb.getAnswer());
            assertEquals(answer.getAnswerNum(), answerFromDb.getAnswerNum());

        }

        answersFromDb = db.getAnswers(16);
        answers = new ArrayList<>(Arrays.asList(
                (new Answer(16, "Cape Town", -1)),
                (new Answer(16, "Pretoria", -1)),
                (new Answer(16, "Bloemfontein", -1))));

        assertEquals(answersFromDb.size(), answers.size());
        for (int i = 0; i < answers.size(); i++) {
            Answer answer = answers.get(i);
            Answer answerFromDb = answersFromDb.get(i);
            assertEquals(answer.getQuestionId(), answerFromDb.getQuestionId());
            assertEquals(answer.getAnswer(), answerFromDb.getAnswer());
            assertEquals(answer.getAnswerNum(), answerFromDb.getAnswerNum());

        }

        answersFromDb = db.getAnswers(32);
        answers = new ArrayList<>(Arrays.asList(
                (new Answer(32, "Winterfell", 1)),
                (new Answer(32, "Eyrie", 2)),
                (new Answer(32, "Casterly Rock", 3)),
                (new Answer(32, "Storm\'s End", 4))));

        assertEquals(answersFromDb.size(), answers.size());
        for (int i = 0; i < answers.size(); i++) {
            Answer answer = answers.get(i);
            Answer answerFromDb = answersFromDb.get(i);
            assertEquals(answer.getQuestionId(), answerFromDb.getQuestionId());
            assertEquals(answer.getAnswer(), answerFromDb.getAnswer());
            assertEquals(answer.getAnswerNum(), answerFromDb.getAnswerNum());

        }
    }

    @Test
    public void testAddQuizInfo() throws SQLException {
        Quiz quiz = new Quiz(-1, "New Quiz", "I am a new Quiz", "almasxit",
                "", false, true, false, true, null, Categories.HISTORY,
                0, 0);

        int newQuizId = db.addQuizInfo(quiz);

        Quiz quizFromDb = db.getQuizById(newQuizId);
        assertEquals(quiz.getQuizName(), quizFromDb.getQuizName());
        assertEquals(quiz.getQuizDescription(), quizFromDb.getQuizDescription());
        assertEquals(quiz.getCreatorUsername(), quizFromDb.getCreatorUsername());
        assertEquals("https://d1ymz67w5raq8g.cloudfront.net/Pictures/480xany/6/5/5/509655_shutterstock_1506580442_769367.jpg", quizFromDb.getPictureUrl());
        assertEquals(quiz.isMultiPage(), quizFromDb.isMultiPage());
        assertEquals(quiz.isRandom(), quizFromDb.isRandom());
        assertEquals(quiz.isImmediateCorrection(), quizFromDb.isImmediateCorrection());
        assertEquals(quiz.isPracticeMode(), quizFromDb.isPracticeMode());
        assertEquals(quiz.getCategory(), quizFromDb.getCategory());
        assertEquals(quiz.getViews(), quizFromDb.getViews());
        assertEquals(quiz.getTaken(), quizFromDb.getTaken());
    }

    @Test
    public void testAddQuestion() throws SQLException {
        dbCon.runSqlFile("testQuiz.sql");

        Quiz quiz = new Quiz(-1, "New Quiz", "I am a new Quiz", "almasxit",
                "https://upload.wikimedia.org/wikipedia/en/c/c6/Scrappy-doo.png", false, true, false, true, null, Categories.HISTORY,
                0, 0);

        int newQuizId = db.addQuizInfo(quiz);
        List<Question> questions = new ArrayList<>(Arrays.asList(
                new Question(-1, "one?", questionTypes.QUESTION_RESPONSE, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSw5LSG0TezTDdV8jMeLwVskZAKI7nRHuqm-Q&s",
                        false, newQuizId, 1),
                new Question(-1, "two?", questionTypes.MULTI_CHOICE_MULTI_ANS, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSw5LSG0TezTDdV8jMeLwVskZAKI7nRHuqm-Q&s",
                        true, newQuizId, 1.65),
                new Question(-1, "three?", questionTypes.MULTI_ANSWER, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSw5LSG0TezTDdV8jMeLwVskZAKI7nRHuqm-Q&s",
                        true, newQuizId, 1),
                new Question(-1, "four?", questionTypes.PICTURE_RESPONSE, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSw5LSG0TezTDdV8jMeLwVskZAKI7nRHuqm-Q&s",
                        false, newQuizId, 4)
        ));

        for (int i = 0; i < questions.size(); i++) {
            db.addQuestion(questions.get(i));
        }

        List<Question> questionsFromDb = db.getQuizQuestions(newQuizId);
        assertEquals(questions.size(), questionsFromDb.size());

        for (int i = 0; i < questions.size(); i++) {
            Question questionFromDb = questionsFromDb.get(i);
            Question question = questions.get(i);

            assertEquals(question.getQuestionText(), questionFromDb.getQuestionText());
            assertEquals(question.getQuestionType(), questionFromDb.getQuestionType());
            assertEquals(question.getPictureUrl(), questionFromDb.getPictureUrl());
            assertEquals(question.isSortedRelevant(), questionFromDb.isSortedRelevant());
            assertEquals(question.getQuizId(), questionFromDb.getQuizId());
            assertEquals(question.getPoint(), questionFromDb.getPoint(), 0);
        }
    }

    @Test
    public void testAddAnswer() throws SQLException {
        dbCon.runSqlFile("testQuiz.sql");

        Quiz quiz = new Quiz(-1, "New Quiz", "I am a new Quiz", "almasxit",
                "https://upload.wikimedia.org/wikipedia/en/c/c6/Scrappy-doo.png", false, true, false, true, null, Categories.HISTORY,
                0, 0);

        int newQuizId = db.addQuizInfo(quiz);
        List<Question> questions = new ArrayList<>(Arrays.asList(
                new Question(-1, "one?", questionTypes.QUESTION_RESPONSE, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSw5LSG0TezTDdV8jMeLwVskZAKI7nRHuqm-Q&s",
                        false, newQuizId, 1),
                new Question(-1, "two?", questionTypes.MULTI_CHOICE_MULTI_ANS, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSw5LSG0TezTDdV8jMeLwVskZAKI7nRHuqm-Q&s",
                        true, newQuizId, 1.65),
                new Question(-1, "three?", questionTypes.MULTI_ANSWER, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSw5LSG0TezTDdV8jMeLwVskZAKI7nRHuqm-Q&s",
                        true, newQuizId, 1),
                new Question(-1, "four?", questionTypes.PICTURE_RESPONSE, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSw5LSG0TezTDdV8jMeLwVskZAKI7nRHuqm-Q&s",
                        false, newQuizId, 4)
        ));

        List<List<Answer>> allAnswers = new ArrayList<>();
        List<Integer> questionIds = new ArrayList<>();

        for (int i = 0; i < questions.size(); i++) {
            int questionId = db.addQuestion(questions.get(i));
            questionIds.add(questionId);

            List<Answer> answers = new ArrayList<>();
            for (int j = 0; j < (i + 1) * 2; j++) {
                Answer curAnswer = new Answer(questionId, "answer is" + i, j);
                answers.add(curAnswer);
                db.addAnswer(curAnswer);
            }
            allAnswers.add(answers);
        }

        for (int i = 0; i < questions.size(); i++) {
            List<Answer> answersFromDb = db.getAnswers(questionIds.get(i));
            List<Answer> answers = allAnswers.get(i);

            assertEquals(answersFromDb.size(), answers.size());

            for (int j = 0; j < answersFromDb.size(); j++) {
                Answer answer = answers.get(j);
                Answer answerFromDb = answersFromDb.get(j);
                assertEquals(answer.getQuestionId(), answerFromDb.getQuestionId());
                assertEquals(answer.getAnswer(), answerFromDb.getAnswer());
                assertEquals(answer.getAnswerNum(), answerFromDb.getAnswerNum());
            }
        }
    }

    @Test
    public void testAddOptions() throws SQLException {
        dbCon.runSqlFile("testQuiz.sql");

        Quiz quiz = new Quiz(-1, "New Quiz", "I am a new Quiz", "almasxit",
                "https://upload.wikimedia.org/wikipedia/en/c/c6/Scrappy-doo.png", false, true, false, true, null, Categories.HISTORY,
                0, 0);

        int newQuizId = db.addQuizInfo(quiz);
        List<Question> questions = new ArrayList<>(Arrays.asList(
                new Question(-1, "one?", questionTypes.QUESTION_RESPONSE, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSw5LSG0TezTDdV8jMeLwVskZAKI7nRHuqm-Q&s",
                        false, newQuizId, 1),
                new Question(-1, "two?", questionTypes.MULTI_CHOICE_MULTI_ANS, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSw5LSG0TezTDdV8jMeLwVskZAKI7nRHuqm-Q&s",
                        true, newQuizId, 1.65),
                new Question(-1, "three?", questionTypes.MULTI_ANSWER, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSw5LSG0TezTDdV8jMeLwVskZAKI7nRHuqm-Q&s",
                        true, newQuizId, 1),
                new Question(-1, "four?", questionTypes.PICTURE_RESPONSE, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSw5LSG0TezTDdV8jMeLwVskZAKI7nRHuqm-Q&s",
                        false, newQuizId, 4)
        ));

        List<List<String>> allOptions = new ArrayList<>();
        List<Integer> questionIds = new ArrayList<>();

        for (int i = 0; i < questions.size(); i++) {
            int questionId = db.addQuestion(questions.get(i));
            questionIds.add(questionId);

            List<String> options = new ArrayList<>();
            for (int j = 0; j < (i + 1) * 3; j++) {
                db.addOption(questionId, "options is" + j);
                options.add("options is" + j);
            }
            allOptions.add(options);
        }

        for (int i = 0; i < questions.size(); i++) {
            List<String> optionsFromDb = db.getOptions(questionIds.get(i));
            List<String> options = allOptions.get(i);

            assertEquals(options.size(), optionsFromDb.size());

            for (int j = 0; j < optionsFromDb.size(); j++) {
                assertEquals(options.get(j), optionsFromDb.get(j));
            }
        }
    }

    @Test
    public void testDeleteQuiz() throws SQLException {
        dbCon.runSqlFile("testQuiz.sql");

        List<Quiz> quizzes = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Quiz quiz = new Quiz(-1, "New Quiz" + i, "I am a new Quiz" + i, "almasxit",
                    "https://upload.wikimedia.org/wikipedia/en/c/c6/Scrappy-doo.png", ((i % 2) == 0), ((i % 2) == 1), ((i % 2) == 0), ((i % 2) == 0), null, Categories.getByValue(i),
                    0, 0);
            quizzes.add(quiz);
            ids.add(db.addQuizInfo(quiz));
        }

        for (int i = 0; i < 5; i++) {
            Quiz quiz = db.getQuizById(ids.get(i));
            assertEquals(quiz.getQuizName(), quizzes.get(i).getQuizName());
            assertEquals(quiz.getQuizDescription(), quizzes.get(i).getQuizDescription());
            assertEquals(quiz.getCreatorUsername(), quizzes.get(i).getCreatorUsername());
            assertEquals(quiz.getPictureUrl(), quizzes.get(i).getPictureUrl());
            assertEquals(quiz.isMultiPage(), quizzes.get(i).isMultiPage());
            assertEquals(quiz.isRandom(), quizzes.get(i).isRandom());
            assertEquals(quiz.isImmediateCorrection(), quizzes.get(i).isImmediateCorrection());
            assertEquals(quiz.isPracticeMode(), quizzes.get(i).isPracticeMode());
            assertEquals(quiz.getCategory(), quizzes.get(i).getCategory());
            assertEquals(quiz.getViews(), quizzes.get(i).getViews());
            assertEquals(quiz.getTaken(), quizzes.get(i).getTaken());

            db.deleteQuiz(ids.get(i));

            assertNull(db.getQuizById(ids.get(i)));
        }
    }

    @Test
    public void testViewsAndTaken() throws SQLException {
        dbCon.runSqlFile("testQuiz.sql");

        List<Quiz> quizzes = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Quiz quiz = new Quiz(-1, "New Quiz" + i, "I am a new Quiz" + i, "almasxit",
                    "https://upload.wikimedia.org/wikipedia/en/c/c6/Scrappy-doo.png", ((i % 2) == 0), ((i % 2) == 1), ((i % 2) == 0), ((i % 2) == 0), null, Categories.getByValue(i),
                    0, 0);
            quizzes.add(quiz);
            ids.add(db.addQuizInfo(quiz));
        }

        for (int i = 0; i < quizzes.size(); i++) {
            assertEquals(0, db.viewCount(ids.get(i)));
            assertEquals(0, db.takeCount(ids.get(i)));

            db.incrementViews(ids.get(i));
            db.incrementTaken(ids.get(i));

            assertEquals(1, db.viewCount(ids.get(i)));
            assertEquals(1, db.takeCount(ids.get(i)));
        }

        // test already added quizzes

        assertEquals(6, db.viewCount( 1));
        assertEquals(4, db.takeCount(1));

        db.incrementViews(1);
        db.incrementTaken(1);

        assertEquals(7, db.viewCount(1));
        assertEquals(5, db.takeCount(1));

        assertEquals(17, db.viewCount( 2));
        assertEquals(4, db.takeCount(2));

        db.incrementViews(2);
        db.incrementTaken(2);

        assertEquals(18, db.viewCount(2));
        assertEquals(5, db.takeCount(2));

        assertEquals(15, db.viewCount( 3));
        assertEquals(4, db.takeCount(3));

        db.incrementViews(3);
        db.incrementTaken(3);

        assertEquals(16, db.viewCount(3));
        assertEquals(5, db.takeCount(3));

    }


    @Test
    public void testGetTags() throws SQLException {
        List<String> tags = new ArrayList<>(Arrays.asList("geography", "flags", "gia chanturia", "ivory coast",
                "continents", "countries", "capitals"));
        List<String> tagsFromDb = db.getTags(3);

        assertEquals(tags.size(), tagsFromDb.size());

        for (int i = 0; i < tags.size(); i++) {
            assertEquals(tags.get(i), tagsFromDb.get(i));
        }

        tags = new ArrayList<>(Arrays.asList("Mythology", "Greece", "Hades", "Zeus", "Gods"));
        tagsFromDb = db.getTags(7);

        assertEquals(tags.size(), tagsFromDb.size());

        for (int i = 0; i < tags.size(); i++) {
            assertEquals(tags.get(i), tagsFromDb.get(i));
        }
    }

    @Test
    public void testAddTags() throws SQLException {
        dbCon.runSqlFile("testQuiz.sql");

        List<Quiz> quizzes = new ArrayList<>();
        List<List<String>> allTags = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Quiz quiz = new Quiz(-1, "New Quiz" + i, "I am a new Quiz" + i, "almasxit",
                    "https://upload.wikimedia.org/wikipedia/en/c/c6/Scrappy-doo.png", ((i % 2) == 0), ((i % 2) == 1), ((i % 2) == 0), ((i % 2) == 0), null, Categories.getByValue(i),
                    0, 0);
            quizzes.add(quiz);
            int quizId = db.addQuizInfo(quiz);
            ids.add(quizId);

            List<String> tags = new ArrayList<>();
            for (int j = 0; j < (i + 1) * 2; j++) {
                tags.add("tag is" + i);
            }

            allTags.add(tags);
            db.addTags(tags, quizId);
        }

        for (int i = 0; i < quizzes.size(); i++) {
            List<String> tags = allTags.get(i);
            List<String> tagsFromDb = db.getTags(ids.get(i));

            assertEquals(tags.size(), tagsFromDb.size());

            for (int j = 0; j < tags.size(); j++) {
                assertEquals(tags.get(i), tagsFromDb.get(i));
            }
        }
    }

    @Test
    public void testRecentlyCreatedQuizzesByFriends() throws SQLException {
        dbCon.runSqlFile("testQuiz.sql");
        ArrayList<String> friends = new ArrayList<>(Arrays.asList("almasxit", "Bacha"));

        ArrayList<Quiz> quizzes = new ArrayList<>(
                Arrays.asList(
                        (new Quiz(1, "Mixed Quiz", "This quiz contains a question of each type",
                                "almasxit", "https://viralsolutions.net/wp-content/uploads/2019/06/shutterstock_749036344.jpg",
                                true, true, true, true, Timestamp.valueOf("2024-07-28 01:48:46"), Categories.MIXED, 6, 4)),
                        (new Quiz(2, "Barcelona", "How well do you know the history of FC Barcelona?",
                                "Bacha", "https://d1ymz67w5raq8g.cloudfront.net/Pictures/480xany/6/5/5/509655_shutterstock_1506580442_769367.jpg",
                                false, true, true, true, Timestamp.valueOf("2024-07-25 01:48:46"), Categories.SPORTS, 17, 4)),
                        (new Quiz(3, "Geography!", "This quiz contains different question about geography, including flags, countries, states, capitals and so on",
                                "almasxit", "https://study.com/cimages/videopreview/p3c2j8y73a.jpg", true, true,
                                true, true, Timestamp.valueOf("2024-07-23 10:48:46"), Categories.GEOGRAPHY, 15, 4))));


        List<Quiz> quizzesFromDb = db.recentQuizzesCreatedByFriends(friends, 3);
        assertEquals(quizzes.size(), quizzesFromDb.size());


        for (int i = 0; i < quizzes.size(); i++) {
            Quiz quiz = quizzes.get(i);
            Quiz quizFromDb = quizzesFromDb.get(i);


            assertEquals(quiz.getQuizName(), quizFromDb.getQuizName());
            assertEquals(quiz.getQuizDescription(), quizFromDb.getQuizDescription());
            assertEquals(quiz.getCreatorUsername(), quizFromDb.getCreatorUsername());
            assertEquals(quiz.getPictureUrl(), quizFromDb.getPictureUrl());
            assertEquals(quiz.isMultiPage(), quizFromDb.isMultiPage());
            assertEquals(quiz.isRandom(), quizFromDb.isRandom());
            assertEquals(quiz.isImmediateCorrection(), quizFromDb.isImmediateCorrection());
            assertEquals(quiz.isPracticeMode(), quizFromDb.isPracticeMode());
            assertEquals(quiz.getCategory(), quizFromDb.getCategory());
            assertEquals(quiz.getViews(), quizFromDb.getViews());
            assertEquals(quiz.getTaken(), quizFromDb.getTaken());
        }

        quizzesFromDb = db.recentQuizzesCreatedByFriends(friends, 2);

        for (int i = 0; i < quizzesFromDb.size(); i++) {
            Quiz quiz = quizzes.get(i);
            Quiz quizFromDb = quizzesFromDb.get(i);

            assertEquals(quiz.getQuizName(), quizFromDb.getQuizName());
            assertEquals(quiz.getQuizDescription(), quizFromDb.getQuizDescription());
            assertEquals(quiz.getCreatorUsername(), quizFromDb.getCreatorUsername());
            assertEquals(quiz.getPictureUrl(), quizFromDb.getPictureUrl());
            assertEquals(quiz.isMultiPage(), quizFromDb.isMultiPage());
            assertEquals(quiz.isRandom(), quizFromDb.isRandom());
            assertEquals(quiz.isImmediateCorrection(), quizFromDb.isImmediateCorrection());
            assertEquals(quiz.isPracticeMode(), quizFromDb.isPracticeMode());
            assertEquals(quiz.getCategory(), quizFromDb.getCategory());
            assertEquals(quiz.getViews(), quizFromDb.getViews());
            assertEquals(quiz.getTaken(), quizFromDb.getTaken());
        }
    }

    @Test
    public void testGetQuizzesBy() throws SQLException {
        dbCon.runSqlFile("testQuiz.sql");


        ArrayList<Quiz> quizzesFromDb = db.getQuizzesBy(Categories.MIXED.name(), "", "", null, null, true);
        assertEquals(1, quizzesFromDb.size());
        assertEquals(1, quizzesFromDb.get(0).getQuizId());

        quizzesFromDb = db.getQuizzesBy(Categories.SPORTS.name(), "barc", "", null, null, true);
        assertEquals(1, quizzesFromDb.size());
        assertEquals(2, quizzesFromDb.get(0).getQuizId());

        quizzesFromDb = db.getQuizzesBy("", "", "flags", null, null, true);
        assertEquals(1, quizzesFromDb.size());
        assertEquals(3, quizzesFromDb.get(0).getQuizId());

        quizzesFromDb = db.getQuizzesBy("", "", "", Timestamp.valueOf("2024-07-22 10:48:46"), Timestamp.valueOf("2024-07-26 10:48:46"), false);
        assertEquals(2, quizzesFromDb.size());
        assertEquals(2, quizzesFromDb.get(0).getQuizId());
        assertEquals(3, quizzesFromDb.get(1).getQuizId());
    }

}
