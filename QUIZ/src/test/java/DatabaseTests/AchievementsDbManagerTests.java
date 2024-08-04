package DatabaseTests;

import org.junit.Before;
import org.junit.Test;
import quiz_web.Database.AchievementDbManager;
import quiz_web.Database.DbConnection;
import quiz_web.Database.UserDbManager;
import quiz_web.Models.Achievement;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

// to check line coverage you have to add AchievementDbManager.java from Database package, and Achievement.java from Models
// in modify run configuration
public class AchievementsDbManagerTests {

    private DbConnection dbCon;
    AchievementDbManager db;
    @Before
    public void setup() throws SQLException, ClassNotFoundException {
        dbCon = new DbConnection();
        dbCon.runSqlFile("testUsers.sql");
        db = new AchievementDbManager(dbCon.getConnection(), true);
    }

    @Test
    public void testAddAchievement() throws SQLException {
        for(int i = 0; i < 6; i++) {
            db.addAchievement("testUser", i);
        }

        ArrayList<Achievement> achievements = db.getAchievements("testUser", -1);

        assertEquals(6, achievements.size());

        for(int i = 0; i < achievements.size(); i++) { // getAchievemets returns desc order (by take_date)
            assertEquals("testUser", achievements.get(i).getUsername());
            assertEquals(i, achievements.get(i).getAchievementType());
        }

        db.deleteAll("testUser");
        achievements.clear();

        Random random = new Random();
        for(int i = 0; i < 15; i++) { // If achievement is repeated, it should be added
            int randomAchievement = random.nextInt(6);
            db.addAchievement("testUser", randomAchievement);
        }

        achievements = db.getAchievements("testUser", -1);
        assertEquals(15, achievements.size());
    }

    @Test
    public void testDeleteAll() throws SQLException {
        ArrayList<String> usersInBase = new ArrayList<>(Arrays.asList("user1", "user2", "user3", "user4", "user5", "user6", "Bacha", "almasxit", "Kingslayer"));

        ArrayList<Achievement> getAchievemets = null;
        for(int i = 0; i < usersInBase.size(); i++) {
            db.deleteAll(usersInBase.get(i));
            getAchievemets = db.getAchievements(usersInBase.get(i), -1);
            assertTrue(getAchievemets.isEmpty());
        }

        String user = "testUser";

        for(int i = 0; i < 6; i++) {
            db.addAchievement(user, i);
        }

        db.deleteAll(user);
        getAchievemets = db.getAchievements(user, -1);
        assertEquals(0, getAchievemets.size());
    }

    @Test
    public void testGetAchievements() throws SQLException {
        ArrayList<String> usersInBase = new ArrayList<>(Arrays.asList("user1", "user2", "user3", "user4", "user5", "user6", "Bacha", "almasxit", "kingslayer"));
        int counter = 0;

        for(int i = 0; i < usersInBase.size(); i++) {
            ArrayList<Achievement> getAchievements = db.getAchievements(usersInBase.get(i), -1);
            if(!getAchievements.isEmpty()) {
                assertTrue(usersInBase.get(i).equals(getAchievements.get(0).getUsername()));
            }
            counter += getAchievements.size();
        }

        assertEquals(45, counter); // there is 45 inserted achievements in base

        String user = "testUser";
        Random random = new Random();
        for(int i = 0; i < 30; i++) {
            db.addAchievement(user, random.nextInt(6));
        }

        ArrayList<Achievement> getAchievements = db.getAchievements(user, -1);
        assertEquals(30, getAchievements.size());
        db.deleteAll(user);
        getAchievements = db.getAchievements(user, -1);
        assertEquals(0, getAchievements.size());
    }

    @Test
    public void testGetRecentlyEarnedAchievementsByFriends() throws SQLException {
        UserDbManager userDbManager = new UserDbManager(dbCon.getConnection(), false);
        ArrayList<String> friends = userDbManager.getFriendsList("Bacha");

        ArrayList<Achievement> friendsAchievements = db.getRecentlyEarnedAchievementsByFriends(friends, Integer.MAX_VALUE);
        HashSet<Integer> friendsAchievementTypes = new HashSet<Integer>();
        for(int i = 0; i < friendsAchievements.size(); i++) {
            friendsAchievementTypes.add(friendsAchievements.get(i).getAchievementType());
        }

        int counter = 0;
        for(int i = 0; i < friends.size(); i++){
            ArrayList<Achievement> achievements = db.getAchievements(friends.get(i), -1);
            for(int j = 0; j < achievements.size(); j++) {
                assertTrue(friendsAchievementTypes.contains(achievements.get(j).getAchievementType()));
            }

            counter += achievements.size();
        }

        assertEquals(counter, friendsAchievements.size());
    }

    @Test
    public void testAchievementObject() {
        ArrayList<String> usersInBase = new ArrayList<>(Arrays.asList("user1", "user2", "user3", "user4", "user5", "user6", "Bacha", "almasxit", "kingslayer"));
        ArrayList<Achievement> achievements = new ArrayList<>();

        Random random = new Random();
        Instant instant = Instant.now();
        Timestamp now = Timestamp.from(instant);
        ArrayList<Integer> types = new ArrayList<>();
        for(int i = 0; i < usersInBase.size(); i++) {
            int type = random.nextInt(6);
            types.add(type);
            Achievement achievement = new Achievement(i, usersInBase.get(i), type, now);
            achievements.add(achievement);
        }

        for(int i = 0; i < usersInBase.size(); i++) {
            assertEquals(achievements.get(i).getUsername(), usersInBase.get(i));
            assertEquals(achievements.get(i).getAchievementId(), i);
            assertEquals(achievements.get(i).getReceived_date(), now);
            assertEquals((Integer) achievements.get(i).getAchievementType(), types.get(i));
        }

        Achievement achievement1 = new Achievement(100, "Bacha", 5, now);
        Achievement achievement2 = new Achievement(105, "Bacha", 5, null);

        assertTrue(achievement1.equals(achievement2)); // if username and type equals then achievements equals

    }
}
