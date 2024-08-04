package DatabaseTests;

import org.junit.Before;
import org.junit.Test;
import quiz_web.Database.DbConnection;
import quiz_web.Database.RelationshipDbManager;
import quiz_web.Database.UserDbManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

// To test line coverage in Modify Quiz Run you have to add RelationshipDbManager from Database and UserDbManager
public class RelationshipDbManagerTests {
    RelationshipDbManager db;
    UserDbManager userDb;
    DbConnection dbCon;

    @Before
    public void setUp() throws ClassNotFoundException, SQLException {
        dbCon = new DbConnection();
        dbCon.runSqlFile("testUsers.sql");
        db = new RelationshipDbManager(dbCon.getConnection(), true);
        userDb = new UserDbManager(dbCon.getConnection(), true);
    }

    @Test
    public void testGetReceivedNotes() throws SQLException {
        List<Map<String, String>> notes = db.getReceivedNotes("almasxit");

        List<String> senders = new ArrayList<>(Arrays.asList("user1", "user2", "user3"));

        for (int i = 0; i < senders.size(); i++) {
            Map<String, String> curNote = notes.get(i);
            assertEquals(i + 1, Integer.parseInt(curNote.get("noteId")));
            assertEquals(senders.get(i), curNote.get("senderName"));
            assertEquals("has sent you a friend request.", curNote.get("content"));
            assertEquals("friendship", curNote.get("contentType"));

        }
    }

    @Test
    public void testSendNote() throws SQLException  {
        dbCon.runSqlFile("testUsers.sql");
        List<Map<String, String>> notes = db.getReceivedNotes("user5");

        assertTrue(notes.isEmpty());

        db.sendNote("user4", "user5", "lalalalla", "note");

        notes = db.getReceivedNotes("user5");
        assertEquals(1, notes.size());

        Map<String, String> curNote = notes.get(0);
        assertEquals(4, Integer.parseInt(curNote.get("noteId")));
        assertEquals("user4", curNote.get("senderName"));
        assertEquals("lalalalla", curNote.get("content"));
        assertEquals("note", curNote.get("contentType"));


        db.sendNote("user6", "user5", "homelander", "note");

        notes = db.getReceivedNotes("user5");
        assertEquals(2, notes.size());

        curNote = notes.get(1);
        assertEquals(5, Integer.parseInt(curNote.get("noteId")));
        assertEquals("user6", curNote.get("senderName"));
        assertEquals("homelander", curNote.get("content"));
        assertEquals("note", curNote.get("contentType"));

    }

    @Test
    public void testFriendRequest() throws SQLException  {
        dbCon.runSqlFile("testUsers.sql");
        List<Map<String, String>> notes = db.getReceivedNotes("user5");

        assertTrue(notes.isEmpty());

        db.sendFriendRequest("user4", "user5");

        notes = db.getReceivedNotes("user5");
        assertEquals(1, notes.size());

        Map<String, String> curNote = notes.get(0);
        assertEquals(4, Integer.parseInt(curNote.get("noteId")));
        assertEquals("user4", curNote.get("senderName"));
        assertEquals("has sent you a friend request.", curNote.get("content"));
        assertEquals("friendship", curNote.get("contentType"));


        db.sendFriendRequest("user6", "user5");

        notes = db.getReceivedNotes("user5");
        assertEquals(2, notes.size());

        curNote = notes.get(1);
        assertEquals(5, Integer.parseInt(curNote.get("noteId")));
        assertEquals("user6", curNote.get("senderName"));
        assertEquals("has sent you a friend request.", curNote.get("content"));
        assertEquals("friendship", curNote.get("contentType"));
    }

    @Test
    public void testSendRequest() throws SQLException {
        dbCon.runSqlFile("testUsers.sql");
        List<Map<String, String>> notes = db.getReceivedNotes("user9");
        assertTrue(notes.isEmpty());

        List<String> senders = new ArrayList<>(Arrays.asList("user1", "user6", "user3", "user4"));
        for (int i = 0; i < senders.size(); i++) {
            db.sendFriendRequest(senders.get(i), "user9");
        }

        for (int i = 0; i < senders.size(); i++) {
            assertTrue(db.sentRequest("user9", senders.get(i)));
        }
    }

    @Test
    public void testRespondToRequest() throws SQLException {
        dbCon.runSqlFile("testUsers.sql");
        List<Map<String, String>> notes = db.getReceivedNotes("user9");
        assertTrue(notes.isEmpty());

        List<String> senders = new ArrayList<>(Arrays.asList("user1", "user6", "user3", "user4"));
        for (int i = 0; i < senders.size(); i++) {
            db.sendFriendRequest(senders.get(i), "user9");
            db.respondToRequest(senders.get(i), "user9", "accepted");
        }

        for (int i = 0; i < senders.size(); i++) {
            notes = db.getReceivedNotes(senders.get(i));
            assertEquals(1, notes.size());

            Map<String, String> curNote = notes.get(0);

            assertEquals("user9", curNote.get("senderName"));
            assertEquals("has accepted your request.", curNote.get("content"));
            assertEquals("friendship", curNote.get("contentType"));
        }

        dbCon.runSqlFile("testUsers.sql");
        notes = db.getReceivedNotes("user9");
        assertTrue(notes.isEmpty());

        for (int i = 0; i < senders.size(); i++) {
            db.sendFriendRequest(senders.get(i), "user9");
            db.respondToRequest(senders.get(i), "user9", "rejected");
        }

        for (int i = 0; i < senders.size(); i++) {
            notes = db.getReceivedNotes(senders.get(i));
            assertEquals(1, notes.size());

            Map<String, String> curNote = notes.get(0);

            assertEquals("user9", curNote.get("senderName"));
            assertEquals("has rejected your request.", curNote.get("content"));
            assertEquals("friendship", curNote.get("contentType"));
        }
    }

    @Test
    public void testRemoveFriendship() throws SQLException {
        dbCon.runSqlFile("testUsers.sql");
        ArrayList<String> friends = userDb.getFriendsList("almasxit");
        for (int i = 0; i < friends.size(); i++) {
            db.removeFriendship("almasxit", friends.get(i));
        }

        assertTrue(userDb.getFriendsList("almasxit").isEmpty());
    }

    @Test
    public void testCancelRequest() throws SQLException {
        dbCon.runSqlFile("testUsers.sql");

        List<Map<String, String>> notes = db.getReceivedNotes("user9");
        List<String> senders = new ArrayList<>(Arrays.asList("user1", "user4", "user5"));

        for (int i = 0; i < senders.size(); i++) {
            db.sendFriendRequest(senders.get(i), "user9");
        }

        for (int i = 0; i < senders.size(); i++) {
            assertTrue(db.sentRequest("user9", senders.get(i)));
        }

        for (int i = 0; i < senders.size(); i++) {
            db.cancelRequest(senders.get(i), "user9");
        }

        for (int i = 0; i < senders.size(); i++) {
            assertFalse(db.sentRequest("user9", senders.get(i)));
        }
    }

    @Test
    public void testDeleteNote() throws SQLException {
        dbCon.runSqlFile("testUsers.sql");
        List<Map<String, String>> notes = db.getReceivedNotes("almasxit");

        List<String> senders = new ArrayList<>(Arrays.asList("user1", "user2", "user3"));

        for (int i = 0; i < senders.size(); i++) {
            Map<String, String> curNote = notes.get(i);
            assertEquals(i + 1, Integer.parseInt(curNote.get("noteId")));
            assertEquals(senders.get(i), curNote.get("senderName"));
            assertEquals("has sent you a friend request.", curNote.get("content"));
            assertEquals("friendship", curNote.get("contentType"));
        }

        for (int i = 0; i < senders.size(); i++) {
            db.deleteNote(i + 1);
        }

        notes = db.getReceivedNotes("almasxit");
        assertTrue(notes.isEmpty());
    }

    @Test
    public void testDeleteNoteByUsername() throws SQLException {
        dbCon.runSqlFile("testUsers.sql");
        List<Map<String, String>> notes = db.getReceivedNotes("almasxit");

        List<String> senders = new ArrayList<>(Arrays.asList("user1", "user2", "user3"));

        for (int i = 0; i < senders.size(); i++) {
            Map<String, String> curNote = notes.get(i);
            assertEquals(i + 1, Integer.parseInt(curNote.get("noteId")));
            assertEquals(senders.get(i), curNote.get("senderName"));
            assertEquals("has sent you a friend request.", curNote.get("content"));
            assertEquals("friendship", curNote.get("contentType"));
        }

        db.deleteNoteByUsername("almasxit");

        notes = db.getReceivedNotes("almasxit");
        assertTrue(notes.isEmpty());
    }

}
