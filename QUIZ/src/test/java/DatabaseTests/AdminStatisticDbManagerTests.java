package DatabaseTests;

import org.junit.Before;
import org.junit.Test;
import quiz_web.Database.*;
import quiz_web.Models.Categories;
import quiz_web.Models.Quiz;
import quiz_web.Models.QuizHistory;
import quiz_web.Models.User;

import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AdminStatisticDbManagerTests {

    private DbConnection dbCon;
    private AdminStatisticDbManager db;

    @Before
    public void setup() throws SQLException, ClassNotFoundException {
        dbCon = new DbConnection();
        dbCon.runSqlFile("testQuiz.sql");
        dbCon.runSqlFile("testUsers.sql");
        db = new AdminStatisticDbManager(dbCon.getConnection(), true);
    }

    @Test
    public void testNumOfUsers() throws SQLException {
        String user = "testUser";

        sleep(2000);

        UserDbManager userDbManager = new UserDbManager(dbCon.getConnection(), true);
        for(int i = 0; i < 10; i++) {
            User newUser = new User("random", "randomadze", user + i, "6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b", "", "", false);
            userDbManager.storeUser(newUser);
        }

        Instant instant = Instant.now();
        Timestamp secondBefore = Timestamp.from(instant.minusSeconds(1));
        Timestamp now = Timestamp.from(instant);

        int numOfUsers = db.numberOfUsers(null, null, true);
        assertEquals(22, numOfUsers);

        numOfUsers = db.numberOfUsers(secondBefore, now, false);
        assertEquals(10, numOfUsers);

        for(int i = 0; i < 10; i++) {
            userDbManager.deleteUser(user + i);
        }

        numOfUsers = db.numberOfUsers(null, null, true);
        assertEquals(12, numOfUsers);
    }

    @Test
    public void testNumOfQuizzes() throws SQLException {
        sleep(2000);

        QuizDbManager quizDbManager = new QuizDbManager(dbCon.getConnection(), true);
        for(int i = 0; i < 10; i++) {
            Quiz quiz = new Quiz(i + 10, "", "", "testUser", "", true, false, false,true, null,Categories.ENTERTAINMENT,10, 5);
            quizDbManager.addQuizInfo(quiz);
        }

        Instant instant = Instant.now();
        Timestamp secondBefore = Timestamp.from(instant.minusSeconds(1));
        Timestamp now = Timestamp.from(instant);

        int numOfQuizzes = db.numberOfQuizzes(null, null, true);
        assertEquals(19, numOfQuizzes);

        numOfQuizzes = db.numberOfQuizzes(secondBefore, now, false);
        assertEquals(10, numOfQuizzes);

        for(int i = 0; i < 10; i++) {
            quizDbManager.deleteQuiz(i + 10);
        }

        numOfQuizzes = db.numberOfQuizzes(null, null, true);
        assertEquals(9, numOfQuizzes);
    }

    @Test
    public void testQuizzesTakeCount() throws SQLException {

        int takeCount = 0;
        QuizDbManager quizDbManager = new QuizDbManager(dbCon.getConnection(), true);
        QuizHistoryDbManager quizHistoryDbManager = new QuizHistoryDbManager(dbCon.getConnection(), true);

        for(int i = 1; i < 10; i++) {
            Quiz quiz = quizDbManager.getQuizById(i);
            takeCount += quiz.getTaken();
        }

        int result = db.quizzesTakeCount(null, null, true);
        assertEquals(result, takeCount);

        for(int i = 0; i < 10; i++) {
            sleep(200);
            quizDbManager.incrementTaken(i + 1);
            quizHistoryDbManager.addQuizHistory(new QuizHistory(null, null, null, i + 1, 0,null, 0));
        }
        Instant instant = Instant.now();
        Timestamp start = Timestamp.from(instant.minusSeconds(20));
        Timestamp end = Timestamp.from(instant);

        result = db.quizzesTakeCount(start, end, false);
        assertEquals(10, result);


    }

    @Test
    public void testRegisteredUsers() throws SQLException {
        String user = "testUser";
        sleep(1000);

        Instant instant = Instant.now();
        Timestamp start = Timestamp.from(instant);

        sleep(1000);
        UserDbManager userDbManager = new UserDbManager(dbCon.getConnection(), true);

        for(int i = 0; i < 5; i++) {
                userDbManager.storeUser(new User(null, null, user + i, null, null, "null", false));
                if(i != 4) sleep(1000);
        }

        instant = Instant.now();
        Timestamp twoSecondBefore = Timestamp.from(instant.minusSeconds(2));
        Timestamp end = Timestamp.from(instant);

        HashMap<String, Timestamp> users = db.registeredUsers(start, end, false);
        assertEquals(5, users.size());

        for(int i = 0; i < 5; i++) {
            assertTrue(users.containsKey(user + i));
        }

        users = db.registeredUsers(twoSecondBefore, end, false);
        assertEquals(2, users.size());

        users = db.registeredUsers(null, null, true);
        assertEquals(17, users.size());
    }

    @Test
    public void testCreatedQuizzes() throws SQLException {
        Timestamp start = Timestamp.valueOf("2024-07-23 10:48:43");
        Timestamp end = Timestamp.valueOf("2024-07-29 10:48:46");

        ArrayList<Timestamp> exactCreationDates = new ArrayList<>(Arrays.asList(Timestamp.valueOf("2024-07-28 01:48:46"), Timestamp.valueOf("2024-07-25 01:48:46"), Timestamp.valueOf("2024-07-23 10:48:46")));
        ArrayList<Timestamp> reverse = new ArrayList<>(Arrays.asList(Timestamp.valueOf("2024-07-23 10:48:46"), Timestamp.valueOf("2024-07-25 01:48:46"), Timestamp.valueOf("2024-07-28 01:48:46")));

        HashMap<Integer, Timestamp> quizzes = db.createdQuizzes(start, end, false);
        assertEquals(3, quizzes.size());

        for(int i = 1; i <= 3; i++) {
            assertTrue(quizzes.containsKey(i));
            assertTrue(quizzes.get(i).equals(exactCreationDates.get(i - 1)));
        }

        quizzes = db.createdQuizzes(null, null, true);

        assertEquals(9, quizzes.size());

        QuizDbManager quizDbManager = new QuizDbManager(dbCon.getConnection(), true);

        for(int i = 1; i < 10; i++) {
            quizDbManager.deleteQuiz(i);
        }

        quizzes = db.createdQuizzes(null, null, true);
        assertEquals(0 ,quizzes.size());
    }

    private void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
