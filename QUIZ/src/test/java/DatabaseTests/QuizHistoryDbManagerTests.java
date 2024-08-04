package DatabaseTests;

import org.junit.Before;
import org.junit.Test;
import quiz_web.Database.DbConnection;
import quiz_web.Database.QuizDbManager;
import quiz_web.Database.QuizHistoryDbManager;
import quiz_web.Models.Categories;
import quiz_web.Models.Quiz;
import quiz_web.Models.QuizHistory;

import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

// To test line coverage in Modify Quiz Run you have to add class QuizHistory from models and QuizHistoryDbManager from Database

public class QuizHistoryDbManagerTests {
    QuizHistoryDbManager db;
    QuizDbManager quizDb;
    DbConnection dbCon;

    @Before
    public void setUp() throws ClassNotFoundException, SQLException {
        dbCon = new DbConnection();
        dbCon.runSqlFile("testQuiz.sql");
        db = new QuizHistoryDbManager(dbCon.getConnection(), true);
        quizDb = new QuizDbManager(dbCon.getConnection(), true);
    }

    @Test
    public void testGetQuizHistoryByQuizId() throws SQLException {
        ArrayList<QuizHistory> quizHs = new ArrayList<>(Arrays.asList(
                (new QuizHistory("user6", "Asoiaf", "https://awoiaf.westeros.org/images/thumb/f/fc/Cristi_Balanescu_wolves_of_the_north_cover.jpg/900px-Cristi_Balanescu_wolves_of_the_north_cover.jpg",
                        4, 24.5, Timestamp.valueOf("2024-07-01 10:24:57"), 182)),
                (new QuizHistory("almasxit", "Asoiaf", "https://awoiaf.westeros.org/images/thumb/f/fc/Cristi_Balanescu_wolves_of_the_north_cover.jpg/900px-Cristi_Balanescu_wolves_of_the_north_cover.jpg",
                        4, 24.5, Timestamp.valueOf("2024-07-02 10:24:57"), 82)),
                (new QuizHistory("user1", "Asoiaf", "https://awoiaf.westeros.org/images/thumb/f/fc/Cristi_Balanescu_wolves_of_the_north_cover.jpg/900px-Cristi_Balanescu_wolves_of_the_north_cover.jpg",
                        4, 20.5, Timestamp.valueOf("2024-07-03 10:24:57"), 42)),
                (new QuizHistory("user2", "Asoiaf", "https://awoiaf.westeros.org/images/thumb/f/fc/Cristi_Balanescu_wolves_of_the_north_cover.jpg/900px-Cristi_Balanescu_wolves_of_the_north_cover.jpg",
                        4, 10, Timestamp.valueOf("2024-07-04 18:44:27"), 105))
        ));

        ArrayList<QuizHistory> quizHsFromDb = db.getQuizHistoryByQuizId(4);
        assertEquals(quizHs.size(), quizHsFromDb.size());

        for (int i = 0; i < quizHs.size(); i++) {
            QuizHistory qH = quizHs.get(i);
            QuizHistory qHFromDb = quizHsFromDb.get(i);

            compareQuizHistories(qH, qHFromDb);
            assertEquals(qH.getTakeDate(), qHFromDb.getTakeDate());
        }
    }

    @Test
    public void testGetQuizHistoryByUsername() throws SQLException {
        ArrayList<QuizHistory> quizHs = new ArrayList<>(Arrays.asList(
                (new QuizHistory("Bacha", "Mixed Quiz", "https://viralsolutions.net/wp-content/uploads/2019/06/shutterstock_749036344.jpg",
                        1, 16, Timestamp.valueOf("2024-07-06 18:34:56"), 53)),
                (new QuizHistory("Bacha", "Barcelona", "https://d1ymz67w5raq8g.cloudfront.net/Pictures/480xany/6/5/5/509655_shutterstock_1506580442_769367.jpg",
                        2, 7, Timestamp.valueOf("2024-07-03 17:55:23"), 12)),
                (new QuizHistory("Bacha", "Journey Through History", "https://ww1.oswego.edu/history/sites/history/files/styles/16x9_xl/public/2020-12/Historyheaderimage.jpg?h=07e9ee27&itok=gFloL8E9",
                        8, 7, Timestamp.valueOf("2024-07-02 01:48:46"), 54))
        ));

        ArrayList<QuizHistory> quizHsFromDb = db.getQuizHistoryByUsername("Bacha", -1);
        assertEquals(quizHs.size(), quizHsFromDb.size());

        for (int i = 0; i < quizHs.size(); i++) {
            QuizHistory qH = quizHs.get(i);
            QuizHistory qHFromDb = quizHsFromDb.get(i);

            compareQuizHistories(qH, qHFromDb);
            assertEquals(qH.getTakeDate(), qHFromDb.getTakeDate());
        }

        quizHsFromDb = db.getQuizHistoryByUsername("Bacha", 2);
        assertEquals(2, quizHsFromDb.size());

        for (int i = 0; i < 2; i++) {
            QuizHistory qH = quizHs.get(i);
            QuizHistory qHFromDb = quizHsFromDb.get(i);

            compareQuizHistories(qH, qHFromDb);
            assertEquals(qH.getTakeDate(), qHFromDb.getTakeDate());
        }
    }

    @Test
    public void testDeleteQuizHistory() throws SQLException {
        dbCon.runSqlFile("testQuiz.sql");
        ArrayList<QuizHistory> quizHs = new ArrayList<>(Arrays.asList(
                (new QuizHistory("user6", "Asoiaf", "https://awoiaf.westeros.org/images/thumb/f/fc/Cristi_Balanescu_wolves_of_the_north_cover.jpg/900px-Cristi_Balanescu_wolves_of_the_north_cover.jpg",
                        4, 24.5, Timestamp.valueOf("2024-07-01 10:24:57"), 182)),
                (new QuizHistory("almasxit", "Asoiaf", "https://awoiaf.westeros.org/images/thumb/f/fc/Cristi_Balanescu_wolves_of_the_north_cover.jpg/900px-Cristi_Balanescu_wolves_of_the_north_cover.jpg",
                        4, 24.5, Timestamp.valueOf("2024-07-02 10:24:57"), 82)),
                (new QuizHistory("user1", "Asoiaf", "https://awoiaf.westeros.org/images/thumb/f/fc/Cristi_Balanescu_wolves_of_the_north_cover.jpg/900px-Cristi_Balanescu_wolves_of_the_north_cover.jpg",
                        4, 20.5, Timestamp.valueOf("2024-07-03 10:24:57"), 42)),
                (new QuizHistory("user2", "Asoiaf", "https://awoiaf.westeros.org/images/thumb/f/fc/Cristi_Balanescu_wolves_of_the_north_cover.jpg/900px-Cristi_Balanescu_wolves_of_the_north_cover.jpg",
                        4, 10, Timestamp.valueOf("2024-07-04 18:44:27"), 105))
        ));

        ArrayList<QuizHistory> quizHsFromDb = db.getQuizHistoryByQuizId(4);
        assertEquals(quizHs.size(), quizHsFromDb.size());

        for (int i = 0; i < quizHs.size(); i++) {
            QuizHistory qH = quizHs.get(i);
            QuizHistory qHFromDb = quizHsFromDb.get(i);
            compareQuizHistories(qH, qHFromDb);
            assertEquals(qH.getTakeDate(), qHFromDb.getTakeDate());

        }

        db.deleteQuizHistory(null, 4);

        assertTrue(db.getQuizHistoryByQuizId(4).isEmpty());

        quizHs = new ArrayList<>(Arrays.asList(
                (new QuizHistory("Bacha", "Mixed Quiz", "https://viralsolutions.net/wp-content/uploads/2019/06/shutterstock_749036344.jpg",
                        1, 16, Timestamp.valueOf("2024-07-06 18:34:56"), 53)),
                (new QuizHistory("Bacha", "Barcelona", "https://d1ymz67w5raq8g.cloudfront.net/Pictures/480xany/6/5/5/509655_shutterstock_1506580442_769367.jpg",
                        2, 7, Timestamp.valueOf("2024-07-03 17:55:23"), 12)),
                (new QuizHistory("Bacha", "Journey Through History", "https://ww1.oswego.edu/history/sites/history/files/styles/16x9_xl/public/2020-12/Historyheaderimage.jpg?h=07e9ee27&itok=gFloL8E9",
                        8, 7, Timestamp.valueOf("2024-07-02 01:48:46"), 54))
        ));

        quizHsFromDb = db.getQuizHistoryByUsername("Bacha", -1);
        assertEquals(quizHs.size(), quizHsFromDb.size());

        for (int i = 0; i < quizHs.size(); i++) {
            QuizHistory qH = quizHs.get(i);
            QuizHistory qHFromDb = quizHsFromDb.get(i);
            compareQuizHistories(qH, qHFromDb);
            assertEquals(qH.getTakeDate(), qHFromDb.getTakeDate());
        }

        db.deleteQuizHistory("Bacha", -1);

        assertTrue(db.getQuizHistoryByUsername("Bacha", -1).isEmpty());
    }

    @Test
    public void testAddHistory() throws SQLException {
        dbCon.runSqlFile("testQuiz.sql");

        Quiz quiz = new Quiz(-1, "New Quiz", "I am a new Quiz", "almasxit",
                "https://viralsolutions.net/wp-content/uploads/2019/06/shutterstock_749036344.jpg", false, true, false, true, null, Categories.HISTORY,
                0, 0);

        int quizId = quizDb.addQuizInfo(quiz);

        ArrayList<QuizHistory> quizHs = new ArrayList<>(Arrays.asList(
                (new QuizHistory("user1", "New Quiz", "https://viralsolutions.net/wp-content/uploads/2019/06/shutterstock_749036344.jpg",
                        quizId, 10, Timestamp.valueOf("2024-07-01 10:24:56"), 182)),
                (new QuizHistory("user2", "New Quiz", "https://viralsolutions.net/wp-content/uploads/2019/06/shutterstock_749036344.jpg",
                        quizId, 10, Timestamp.valueOf("2024-07-01 10:24:57"), 182)),
                (new QuizHistory("user4", "New Quiz", "https://viralsolutions.net/wp-content/uploads/2019/06/shutterstock_749036344.jpg",
                        quizId, 10, Timestamp.valueOf("2024-07-01 10:24:58"), 182))
        ));

        ArrayList<QuizHistory> quizHsFromDb = db.getQuizHistoryByQuizId(quizId);
        assertTrue(quizHsFromDb.isEmpty());

        for (int i = 0; i < quizHs.size(); i++) {
            db.addQuizHistory(quizHs.get(i));
        }

        quizHsFromDb = db.getQuizHistoryByQuizId(quizId);
        assertEquals(3, quizHsFromDb.size());

        for (int i = 0; i < quizHs.size(); i++) {
            QuizHistory qH = quizHs.get(i);
            QuizHistory qHFromDb = quizHsFromDb.get(i);

            compareQuizHistories(qH, qHFromDb);
        }
    }


    @Test
    public void testAverageScore() throws SQLException {
        dbCon.runSqlFile("testQuiz.sql");

        Quiz quiz = new Quiz(-1, "New Quiz", "I am a new Quiz", "almasxit",
                "https://viralsolutions.net/wp-content/uploads/2019/06/shutterstock_749036344.jpg", false, true, false, true, null, Categories.HISTORY,
                0, 0);

        int quizId = quizDb.addQuizInfo(quiz);

        ArrayList<QuizHistory> quizHs = new ArrayList<>(Arrays.asList(
                (new QuizHistory("user1", "New Quiz", "https://viralsolutions.net/wp-content/uploads/2019/06/shutterstock_749036344.jpg",
                        quizId, 5, Timestamp.valueOf("2024-07-01 10:24:56"), 182)),
                (new QuizHistory("user2", "New Quiz", "https://viralsolutions.net/wp-content/uploads/2019/06/shutterstock_749036344.jpg",
                        quizId, 10, Timestamp.valueOf("2024-07-01 10:24:57"), 182)),
                (new QuizHistory("user4", "New Quiz", "https://viralsolutions.net/wp-content/uploads/2019/06/shutterstock_749036344.jpg",
                        quizId, 15, Timestamp.valueOf("2024-07-01 10:24:58"), 182))
        ));

        for (int i = 0; i < quizHs.size(); i++) {
            db.addQuizHistory(quizHs.get(i));
        }

        ArrayList<QuizHistory> quizHsFromDb = db.getQuizHistoryByQuizId(quizId);
        assertEquals(3, quizHsFromDb.size());
        assertEquals((5 + 10+ 15) / 3.0, db.getAverageScoreByQuizId(quizId), 0);
    }

    @Test
    public void testGetQuizHistoryByUsernameAndQuizId() throws SQLException {
        dbCon.runSqlFile("testQuiz.sql");

        Quiz quiz = new Quiz(-1, "New Quiz", "I am a new Quiz", "almasxit",
                "https://viralsolutions.net/wp-content/uploads/2019/06/shutterstock_749036344.jpg", false, true, false, true, null, Categories.HISTORY,
                0, 0);

        int quizId = quizDb.addQuizInfo(quiz);

        ArrayList<QuizHistory> quizHs = new ArrayList<>(Arrays.asList(
                (new QuizHistory("user1", "New Quiz", "https://viralsolutions.net/wp-content/uploads/2019/06/shutterstock_749036344.jpg",
                        quizId, 5, Timestamp.valueOf("2024-07-01 10:24:56"), 182)),
                (new QuizHistory("user1", "New Quiz", "https://viralsolutions.net/wp-content/uploads/2019/06/shutterstock_749036344.jpg",
                        quizId, 10, Timestamp.valueOf("2024-07-01 10:24:57"), 182)),
                (new QuizHistory("user4", "New Quiz", "https://viralsolutions.net/wp-content/uploads/2019/06/shutterstock_749036344.jpg",
                        quizId, 15, Timestamp.valueOf("2024-07-01 10:24:58"), 182)),
                (new QuizHistory("user4", "New Quiz", "https://viralsolutions.net/wp-content/uploads/2019/06/shutterstock_749036344.jpg",
                        quizId, 16, Timestamp.valueOf("2024-07-01 10:24:58"), 82))));

        for (int i = 0; i < quizHs.size(); i++) {
            db.addQuizHistory(quizHs.get(i));
        }

        assertTrue(db.getQuizHistoryByUsernameAndQuizId("user3", quizId, "score").isEmpty());
        assertTrue(db.getQuizHistoryByUsernameAndQuizId("user5", quizId, "time_needed").isEmpty());

        ArrayList<QuizHistory> quizHsFromDb = db.getQuizHistoryByUsernameAndQuizId("user1", quizId, "score");
        assertEquals(2, quizHsFromDb.size());

        for (int i = 0; i < quizHsFromDb.size(); i++) {
            int j = 1;
            if (i == 1) j = 0;

            QuizHistory qH = quizHs.get(j);
            QuizHistory qHFromDb = quizHsFromDb.get(i);

            compareQuizHistories(qH, qHFromDb);
        }

        quizHsFromDb = db.getQuizHistoryByUsernameAndQuizId("user4", quizId, "time_needed");
        assertEquals(2, quizHsFromDb.size());

        for (int i = 0; i < quizHsFromDb.size(); i++) {
            int j = 2;
            if (i == 1) j = 3;

            QuizHistory qH = quizHs.get(j);
            QuizHistory qHFromDb = quizHsFromDb.get(i);

            compareQuizHistories(qH, qHFromDb);
        }
    }

    @Test
    public void testTopPerformers() throws SQLException {
        dbCon.runSqlFile("testQuiz.sql");


        ArrayList<QuizHistory> quizHs = new ArrayList<>(Arrays.asList(
                (new QuizHistory("almasxit", "Asoiaf", "https://awoiaf.westeros.org/images/thumb/f/fc/Cristi_Balanescu_wolves_of_the_north_cover.jpg/900px-Cristi_Balanescu_wolves_of_the_north_cover.jpg",
                        4, 24.5, Timestamp.valueOf("2024-07-02 10:24:57"), 82)),
                (new QuizHistory("user1", "Asoiaf", "https://awoiaf.westeros.org/images/thumb/f/fc/Cristi_Balanescu_wolves_of_the_north_cover.jpg/900px-Cristi_Balanescu_wolves_of_the_north_cover.jpg",
                        4, 20.5, Timestamp.valueOf("2024-07-03 10:24:57"), 42)),
                (new QuizHistory("user6", "Asoiaf", "https://awoiaf.westeros.org/images/thumb/f/fc/Cristi_Balanescu_wolves_of_the_north_cover.jpg/900px-Cristi_Balanescu_wolves_of_the_north_cover.jpg",
                        4, 24.5, Timestamp.valueOf("2024-07-01 10:24:57"), 182)),
                (new QuizHistory("user2", "Asoiaf", "https://awoiaf.westeros.org/images/thumb/f/fc/Cristi_Balanescu_wolves_of_the_north_cover.jpg/900px-Cristi_Balanescu_wolves_of_the_north_cover.jpg",
                        4, 10, Timestamp.valueOf("2024-07-04 18:44:27"), 105))
        ));

        ArrayList<QuizHistory> quizHsFromDb = db.topPerformers(4, null, null, true);
        assertEquals(4, quizHsFromDb.size());

        for (int i = 0; i < quizHsFromDb.size(); i++) {
            QuizHistory qH = quizHs.get(i);
            QuizHistory qHFromDb = quizHsFromDb.get(i);

            compareQuizHistories(qH, qHFromDb);
            assertEquals(qH.getTakeDate(), qHFromDb.getTakeDate());
        }

        quizHs = new ArrayList<>(Arrays.asList(
                (new QuizHistory("almasxit", "Asoiaf", "https://awoiaf.westeros.org/images/thumb/f/fc/Cristi_Balanescu_wolves_of_the_north_cover.jpg/900px-Cristi_Balanescu_wolves_of_the_north_cover.jpg",
                        4, 24.5, Timestamp.valueOf("2024-07-02 10:24:57"), 82)),
                (new QuizHistory("user1", "Asoiaf", "https://awoiaf.westeros.org/images/thumb/f/fc/Cristi_Balanescu_wolves_of_the_north_cover.jpg/900px-Cristi_Balanescu_wolves_of_the_north_cover.jpg",
                        4, 20.5, Timestamp.valueOf("2024-07-03 10:24:57"), 42)),
                (new QuizHistory("user6", "Asoiaf", "https://awoiaf.westeros.org/images/thumb/f/fc/Cristi_Balanescu_wolves_of_the_north_cover.jpg/900px-Cristi_Balanescu_wolves_of_the_north_cover.jpg",
                        4, 24.5, Timestamp.valueOf("2024-07-01 10:24:57"), 182)),
                (new QuizHistory("user2", "Asoiaf", "https://awoiaf.westeros.org/images/thumb/f/fc/Cristi_Balanescu_wolves_of_the_north_cover.jpg/900px-Cristi_Balanescu_wolves_of_the_north_cover.jpg",
                        4, 10, Timestamp.valueOf("2024-07-04 18:44:27"), 105))));

        quizHsFromDb = db.topPerformers(4, Timestamp.valueOf("2024-07-02 10:24:00"), Timestamp.valueOf("2024-07-04 14:44:27"), false);
        assertEquals(2, quizHsFromDb.size());

        for (int i = 0; i < quizHsFromDb.size(); i++) {
            QuizHistory qH = quizHs.get(i);
            QuizHistory qHFromDb = quizHsFromDb.get(i);

            compareQuizHistories(qH, qHFromDb);
            assertEquals(qH.getTakeDate(), qHFromDb.getTakeDate());
        }
    }

    @Test
    public void testRecentlyByFriends() throws SQLException {
        dbCon.runSqlFile("testQuiz.sql");

        Quiz quiz = new Quiz(-1, "New Quiz", "I am a new Quiz", "almasxit",
                "https://viralsolutions.net/wp-content/uploads/2019/06/shutterstock_749036344.jpg", false, true, false, true, null, Categories.HISTORY,
                0, 0);

        int quizId = quizDb.addQuizInfo(quiz);

        ArrayList<QuizHistory> quizHs = new ArrayList<>(Arrays.asList(
                (new QuizHistory("user10", "New Quiz", "https://viralsolutions.net/wp-content/uploads/2019/06/shutterstock_749036344.jpg",
                        quizId, 5, Timestamp.valueOf("2024-08-15 10:24:56"), 182)),
                (new QuizHistory("user11", "New Quiz", "https://viralsolutions.net/wp-content/uploads/2019/06/shutterstock_749036344.jpg",
                        quizId, 10, Timestamp.valueOf("2024-08-10 10:24:57"), 182)),
                (new QuizHistory("user12", "New Quiz", "https://viralsolutions.net/wp-content/uploads/2019/06/shutterstock_749036344.jpg",
                        quizId, 15, Timestamp.valueOf("2024-08-05 10:24:58"), 182)),
                (new QuizHistory("user12", "New Quiz", "https://viralsolutions.net/wp-content/uploads/2019/06/shutterstock_749036344.jpg",
                        quizId, 16, Timestamp.valueOf("2024-08-01 10:24:58"), 82))));

        for (int i = 0; i < quizHs.size(); i++) {
            db.addQuizHistory(quizHs.get(i));
        }

        ArrayList<String> friends = new ArrayList<>(Arrays.asList("user10", "user11", "user12", "user13"));

        ArrayList<QuizHistory> quizHsFromDb = db.getQuizzesRecentlyTakenByFriends(friends, 4);
        assertEquals(4, quizHsFromDb.size());

        for (int i = 0; i < quizHsFromDb.size(); i++) {
            QuizHistory qH = quizHs.get(i);
            QuizHistory qHFromDb = quizHsFromDb.get(i);

            compareQuizHistories(qH, qHFromDb);
        }

        quizHsFromDb = db.getQuizzesRecentlyTakenByFriends(friends, 2);
        assertEquals(2, quizHsFromDb.size());

        for (int i = 0; i < quizHsFromDb.size(); i++) {
            QuizHistory qH = quizHs.get(i);
            QuizHistory qHFromDb = quizHsFromDb.get(i);

            compareQuizHistories(qH, qHFromDb);
        }
    }

    @Test
    public void testTookCount() throws SQLException {
        dbCon.runSqlFile("testQuiz.sql");

        assertEquals(3, db.tookQuizCount("Bacha"));
        assertEquals(5, db.tookQuizCount("almasxit"));
        assertEquals(2, db.tookQuizCount("kingslayer"));
    }

    @Test
    public void testHighestScore() throws SQLException {
        dbCon.runSqlFile("testQuiz.sql");

        assertEquals(24.5, db.highestScoreOnQuiz(4), 0);
        assertEquals(16, db.highestScoreOnQuiz(1), 0);
        assertEquals(16.5, db.highestScoreOnQuiz(6), 0);
        assertEquals(0.00, db.highestScoreOnQuiz(-1), 0);
    }

    private void compareQuizHistories(QuizHistory qH, QuizHistory qHFromDb) {
        assertEquals(qH.getQuizId(), qHFromDb.getQuizId());
        assertEquals(qH.getQuizName(), qHFromDb.getQuizName());
        assertEquals(qH.getQuizPic(), qHFromDb.getQuizPic());
        assertEquals(qH.getTimeNeeded(), qHFromDb.getTimeNeeded());
        assertEquals(qH.getUsername(), qHFromDb.getUsername());
        assertEquals(qH.getScore(), qHFromDb.getScore(), 0);
    }
}
