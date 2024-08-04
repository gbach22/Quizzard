package DatabaseTests;

import org.junit.Before;
import org.junit.Test;
import quiz_web.Database.*;
import quiz_web.Models.Categories;
import quiz_web.Models.Quiz;

import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

// to check line coverage you have to add StatisticsDbManager.java from Database package in modify run configuration
public class StatisticsDbManagerTests {

    private DbConnection dbCon;
    private  StatisticsDbManager db;
    private ArrayList<Quiz> quizzes;
    @Before
    public void setup() throws SQLException, ClassNotFoundException {
        dbCon = new DbConnection();
        dbCon.runSqlFile("testQuiz.sql");
        db = new StatisticsDbManager(dbCon.getConnection(), true);
    }

    @Test
    public void testMostViewedQuizzes() throws SQLException {
        Instant now = Instant.now();
        Timestamp currentTime = Timestamp.from(now.plusSeconds(10));
        quizzes = new ArrayList<>(
                Arrays.asList(
                        (new Quiz(10, "Random1", "This quiz contains a question of each type",
                                "almasxit", "https://viralsolutions.net/wp-content/uploads/2019/06/shutterstock_749036344.jpg",
                                true, true, true, true, currentTime, Categories.MIXED, 103, 4)),
                        (new Quiz(11, "Random2", "How well do you know the history of FC Barcelona?",
                                "Bacha", "https://d1ymz67w5raq8g.cloudfront.net/Pictures/480xany/6/5/5/509655_shutterstock_1506580442_769367.jpg",
                                false, true, true, true, currentTime, Categories.SPORTS, 102, 4)),
                        (new Quiz(12, "Random3", "This quiz contains different question about geography, including flags, countries, states, capitals and so on",
                                "almasxit", "https://study.com/cimages/videopreview/p3c2j8y73a.jpg", true, true,
                                true, true,currentTime, Categories.GEOGRAPHY, 101, 5))));

        QuizDbManager quizDbManager = new QuizDbManager(dbCon.getConnection(), true);

        for(int i = 0; i < quizzes.size(); i++) {
            quizDbManager.addQuizInfo(quizzes.get(i));
        }

        for(int i = 0; i < 104; i++) {
            if(i == 101 || i == 102) {
                quizDbManager.incrementViews(10);
            }else if(i == 103) {
                quizDbManager.incrementViews(11);
            }else {
                quizDbManager.incrementViews(10);
                quizDbManager.incrementViews(11);
                quizDbManager.incrementViews(12);
            }
        }

        List<Quiz> quizzesFromBase = db.mostViewedQuizzes(3);

        for (int i = 0; i < quizzesFromBase.size(); i++) {
            Quiz quiz = quizzes.get(i);
            Quiz quizFromDb = quizzesFromBase.get(i);

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
        }

    }

    @Test
    public void testRecentQuizzes() throws SQLException {
        Instant now = Instant.now();
        Timestamp currentTime = Timestamp.from(now.plusSeconds(10));
        quizzes = new ArrayList<>(
                Arrays.asList(
                        (new Quiz(1, "Mixed Quiz", "This quiz contains a question of each type",
                                "almasxit", "https://viralsolutions.net/wp-content/uploads/2019/06/shutterstock_749036344.jpg",
                                true, true, true, true, currentTime, Categories.MIXED, 6, 4)),
                        (new Quiz(2, "Barcelona", "How well do you know the history of FC Barcelona?",
                                "Bacha", "https://d1ymz67w5raq8g.cloudfront.net/Pictures/480xany/6/5/5/509655_shutterstock_1506580442_769367.jpg",
                                false, true, true, true, currentTime, Categories.SPORTS, 17, 4)),
                        (new Quiz(3, "Geography!", "This quiz contains different question about geography, including flags, countries, states, capitals and so on",
                                "almasxit", "https://study.com/cimages/videopreview/p3c2j8y73a.jpg", true, true,
                                true, true,currentTime, Categories.GEOGRAPHY, 15, 5))));

        QuizDbManager quizDbManager = new QuizDbManager(dbCon.getConnection(), true);
        for(int i = 0;i < 3;i++){
            quizDbManager.addQuizInfo(quizzes.get(i));
        }

        List<Quiz> quizzesFromDb = db.recentQuizzes(3);
        assertEquals(quizzes.size(), quizzesFromDb.size());

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
        }

        quizzesFromDb = db.recentQuizzes(2);

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
        }
    }

}
