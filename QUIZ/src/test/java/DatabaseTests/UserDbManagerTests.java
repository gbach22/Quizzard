package DatabaseTests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import quiz_web.Database.DbConnection;
import quiz_web.Database.FriendshipStatus;
import quiz_web.Database.RelationshipDbManager;
import quiz_web.Database.UserDbManager;
import quiz_web.Models.User;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UserDbManagerTests {
    UserDbManager db;
    RelationshipDbManager relDb;
    DbConnection dbCon;
    Connection con;

    @Before
    public void setUp() throws ClassNotFoundException, SQLException {
        dbCon = new DbConnection();
        dbCon.runSqlFile("testUsers.sql");
        con = dbCon.getConnection();
        db = new UserDbManager(dbCon.getConnection(), true);
        relDb = new RelationshipDbManager(dbCon.getConnection(), true);
    }

    @Test
    public void test2() throws SQLException {
        User a = new User("William", "James", "wjames","FluffyBear123", "born to die", "", false);
        User b = new User("John", "Doe", "icecreamlover11","password", "love is love", "", false);
        User c = new User("Nika", "Gvalia", "ngval","abcde", "hehe", "", false);
        User d = new User("Tako", "Gelashvili", "tgela","fghijk", "blah", "", false);
        User e = new User("Gio", "Bachaliashvili", "gbach","lmnopqr", "", "", false);

    }


    @Test
    public void testStoreUser() throws SQLException, NoSuchAlgorithmException {
        User a = new User("William", "James", "wjames","FluffyBear123", "born to die", "", false);
        User b = new User("John", "Doe", "icecreamlover11","password", "love is love", "", false);
        User c = new User("Nika", "Gvalia", "ngval","abcde", "hehe", "", false);
        User d = new User("Tako", "Gelashvili", "tgela","fghijk", "blah", "", false);
        User e = new User("Gio", "Bachaliashvili", "gbach","lmnopqr", "", "", false);

        db.storeUser(a);
        db.storeUser(b);
        db.storeUser(c);
        db.storeUser(d);
        db.storeUser(e);


        assertEquals("John", db.getUser("icecreamlover11").getFirstName());
        assertEquals("Doe", db.getUser("icecreamlover11").getLastName());

        assertEquals("Tako", db.getUser("tgela").getFirstName());
        assertEquals("Gelashvili", db.getUser("tgela").getLastName());

        assertEquals("Gvalia", db.getUser("ngval").getLastName());
        assertEquals("hehe", db.getUser("ngval").getBio());
        assertEquals("born to die", db.getUser("wjames").getBio());

        assertEquals(e.getHashedPassword(), db.getUser("gbach").getHashedPassword());

        assertTrue(db.usernameExists("gbach"));
        assertTrue(db.usernameExists("tgela"));
        assertFalse(db.usernameExists("blabla"));
    }


    @Test
    public void testUsernameExists() throws SQLException {
        for (int i = 1; i <= 9; i++) {
            assertTrue(db.usernameExists("user" + i));
        }


        User a = new User("William", "James", "wjames","FluffyBear123", "born to die", "", false);
        User b = new User("John", "Doe", "icecreamlover11","password", "love is love", "", false);
        User c = new User("Nika", "Gvalia", "ngval","abcde", "hehe", "", false);
        User d = new User("Tako", "Gelashvili", "tgela","fghijk", "blah", "", false);
        User e = new User("Gio", "Bachaliashvili", "gbach","lmnopqr", "", "", false);

        db.storeUser(a);
        db.storeUser(b);
        db.storeUser(c);
        db.storeUser(d);
        db.storeUser(e);

        assertTrue(db.usernameExists("gbach"));
        assertTrue(db.usernameExists("tgela"));
        assertFalse(db.usernameExists("blabla"));
        assertFalse(db.usernameExists("gval"));

        a.setFirstName("George");
        a.setLastName("W");
        assertTrue(db.usernameExists(a.getUserName()));

        b.setBio("nice weather tonighjt");
        b.setPictureURL("invalid picture URL");
        assertTrue(db.usernameExists(b.getUserName()));
    }

    @Test
    public void testGetUser() throws SQLException {
        User user = db.getUser("user1");

        assertNotNull(user);
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("A software developer from California.", user.getBio());
        assertEquals("6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b", user.getHashedPassword());
        assertEquals("https://i.pinimg.com/564x/68/69/7e/68697ed39e4b7df530c3a61c1853b81a.jpg", user.getPictureURL());
        assertFalse(user.isAdmin());

        assertNull(db.getUser("hahahahha"));

        for (int i = 1; i <= 9; i++) {
            assertEquals(db.getUser("user" + i).getUserName(), "user" + i);
        }
    }

    @Test
    public void testGetUserByUsernamePart() throws SQLException {
        List<User> users = db.getUserByUsernamePart("user", 2);

        assertNotNull(users);
        assertEquals(2, users.size());

        User user1 = users.get(0);
        assertEquals("user1", user1.getUserName());
        assertEquals("John", user1.getFirstName());
        assertEquals("Doe", user1.getLastName());
        assertEquals("A software developer from California.", user1.getBio());
        assertEquals("6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b", user1.getHashedPassword());
        assertFalse(user1.isAdmin());

        User user2 = users.get(1);
        assertEquals("user2", user2.getUserName());
        assertEquals("Jane", user2.getFirstName());
        assertEquals("Smith", user2.getLastName());
        assertEquals("A graphic designer from New York.", user2.getBio());
        assertEquals("d4735e3a265e16eee03f59718b9b5d03019c07d8b6c51f90da3a666eec13ab35", user2.getHashedPassword());
        assertFalse(user2.isAdmin());

        users = db.getUserByUsernamePart("user", 9);
        for (int i = 1; i <= 9; i++) {
            User ithUser = users.get(i-1);
            assertEquals("user" + i, ithUser.getUserName());
        }

        // should have 0 matches
        List<User> users2 = db.getUserByUsernamePart("pppppp", 2);

        assertNotNull(users2);
        assertTrue(users2.isEmpty());
    }

    @Test
    public void testMakeAdmin() throws SQLException {
        for (int i = 1; i <= 9; i++) {
            String username = "user" + i;
            assertFalse(db.getUser(username).isAdmin());
        }

        for (int i = 5; i <=8; i++) {
            String username = "user" + i;
            db.makeAdmin(username);
            assertTrue(db.getUser(username).isAdmin());
        }

        for (int i = 1; i <= 4; i++) {
            String username = "user" + i;
            assertFalse(db.getUser(username).isAdmin());
        }
    }



    @Test
    public void testAllSets() throws SQLException, NoSuchAlgorithmException {
        String newFirstName = "Jonathan";
        db.setFirstName("user1", newFirstName);
        assertEquals(newFirstName, db.getUser("user1").getFirstName());

        String newLastName = "Smith";
        db.setLastName("user1", newLastName);
        assertEquals(newLastName, db.getUser("user1").getLastName());

        String newBio = "An updated bio for John.";
        db.setBio("user1", newBio);
        assertEquals(newBio, db.getUser("user1").getBio());

        String newPictureURL = "randompictureurl";
        db.setPictureURL("user1", newPictureURL);
        assertEquals(newPictureURL, db.getUser("user1").getPictureURL());

        String newHashedPassword = User.hashPassword("newhashedpassword123");
        db.setHashedPassword("user1", newHashedPassword);
        assertEquals(newHashedPassword, db.getUser("user1").getHashedPassword());

        for (int i = 1; i <= 9; i++) {
            String username = "user" + i;
            db.setBio(username, "default bio");
        }

        for (int i = 1; i <= 9; i++) {
            String username = "user" + i;
            assertEquals("default bio", db.getUser(username).getBio());
        }


        User a = new User("William", "James", "wjames","FluffyBear123", "born to die", "", false);
        db.storeUser(a);

        a.setHashedPassword(User.hashPassword("123"));
        db.setHashedPassword(a.getUserName(), User.hashPassword("123"));
        assertEquals(a.getHashedPassword(), User.hashPassword("123"));
    }


    @Test
    public void testDeleteUser() throws SQLException {
        assertTrue(userExists("user1"));
        assertTrue(friendshipExists("user1", "user2"));
        assertTrue(friendshipExists("user2", "user1"));


        relDb.deleteNoteByUsername("user1");
        db.deleteUser("user1");

        assertFalse(userExists("user1"));

        assertFalse(friendshipExists("user1", "user2"));
        assertFalse(friendshipExists("user2", "user1"));



        for (int i = 2; i <= 9; i++) {
            String username = "user" + i;
            relDb.deleteNoteByUsername(username);
            db.deleteUser(username);
        }

        for (int i = 2; i <= 9; i++) {
            String username = "user" + i;
            assertFalse(userExists(username));
        }
    }

    private boolean userExists(String username) throws SQLException {
        String usersTable = "test_users_table";
        String query = "SELECT 1 FROM " + usersTable + " WHERE username = ?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }

    private boolean friendshipExists(String username, String friendUsername) throws SQLException {
        String friendsTable = "test_friends_table";
        String query = "SELECT 1 FROM " + friendsTable + " WHERE username = ? AND friend_username = ?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, username);
        stmt.setString(2, friendUsername);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }





    @Test
    public void testGetAllUsers() throws SQLException {

        List<User> allUsers = db.getAllUsers(false);
        assertEquals(12, allUsers.size());
        assertEquals("user5", allUsers.get(7).getUserName());

        List<User> adminUsers = db.getAllUsers(true);
        assertEquals(3, adminUsers.size());
        assertEquals("almasxit", adminUsers.get(0).getUserName());
        assertEquals("Bacha", adminUsers.get(1).getUserName());
        assertEquals("kingslayer", adminUsers.get(2).getUserName());
    }


}
