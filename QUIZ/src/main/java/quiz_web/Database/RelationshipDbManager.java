package quiz_web.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static quiz_web.Database.DatabaseInfo.*;

public class RelationshipDbManager {
    private final Connection connection;
    private final String notesTable;
    private String friendsTable;

    public RelationshipDbManager(Connection connection, boolean testMode) {
        this.connection = connection;
        if (testMode) {
            notesTable = "test_" + NOTES_TABLE;
            friendsTable = "test_" + FRIENDS_TABLE;
        } else {
            notesTable = NOTES_TABLE;
            friendsTable = FRIENDS_TABLE;
        }
    }

    public void sendNote(String senderUsername, String receiverUsername, String content, String type) throws SQLException {
        String sql = "INSERT INTO " + notesTable + " (sender_username, receiver_username, content, content_type) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, senderUsername);
        stmt.setString(2, receiverUsername);
        stmt.setString(3, content);
        stmt.setString(4, type);
        stmt.executeUpdate();
    }

    public List<Map<String, String>> getReceivedNotes(String username) throws SQLException {
        List<Map<String, String>> receivedNotes = new ArrayList<>();
        String sql = "SELECT note_id, sender_username, content, content_type, timestamp " +
                "FROM " + notesTable + " " +
                "WHERE receiver_username = ? " +
                "ORDER BY timestamp DESC";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            Map<String, String> note = new HashMap<>();
            note.put("noteId", rs.getString("note_id"));
            note.put("senderName", rs.getString("sender_username"));
            note.put("content", rs.getString("content"));
            note.put("contentType", rs.getString("content_type"));
            note.put("timestamp", rs.getString("timestamp"));
            receivedNotes.add(note);
        }
        return receivedNotes;
    }

    public void sendFriendRequest(String senderName, String receiverName) throws SQLException {
        String sql = "INSERT INTO " + friendsTable +  " (username, friend_username) VALUES (?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, senderName);
        stmt.setString(2, receiverName);
        stmt.executeUpdate();
        String content = "has sent you a friend request.";
        sendNote(senderName, receiverName, content, "friendship");
    }

    public boolean sentRequest(String myName, String senderName) throws SQLException {
        String sql = "SELECT username, friend_username " +
                "FROM " + friendsTable + " " +
                "WHERE status = ? AND username = ? AND friend_username = ?";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, "pending");
        stmt.setString(2, senderName);
        stmt.setString(3, myName);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }

    public void respondToRequest(String senderName, String receiverName, String status) throws SQLException {
        // receiverName responds
        // status is accepted or rejected
        String sql = "UPDATE " + friendsTable +
                " SET status= ? " +
                " WHERE friend_username = ? AND username = ? ";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, status);
        stmt.setString(2, receiverName);
        stmt.setString(3, senderName);
        stmt.executeUpdate();

        if (status.equals("accepted")) {
            sql = "INSERT INTO " + friendsTable +  " (username, friend_username, status) VALUES (?, ?, ?)";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, receiverName);
            stmt.setString(2, senderName);
            stmt.setString(3, "accepted");
            stmt.executeUpdate();
            String content = "has accepted your request.";
            sendNote(receiverName, senderName, content, "friendship");
        } else {
            sql = "DELETE FROM " + friendsTable +
                    " WHERE status = 'rejected'";
            stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();

            String content = "has rejected your request.";
            sendNote(receiverName, senderName, content, "friendship");
        }

    }

    public void removeFriendship(String senderName, String receiverName) throws SQLException {
        String sql = "DELETE FROM " + friendsTable +
                " WHERE username = ? AND friend_username = ? AND status = 'accepted'";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, senderName);
        stmt.setString(2, receiverName);
        stmt.executeUpdate();

        stmt.setString(1, receiverName);
        stmt.setString(2, senderName);
        stmt.executeUpdate();
    }

    public void cancelRequest(String senderName, String receiverName) throws SQLException {
        String sql = "DELETE FROM " + friendsTable +
                " WHERE username = ? AND friend_username = ? AND status = 'pending'";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, senderName);
        stmt.setString(2, receiverName);
        stmt.executeUpdate();

    }

    public void deleteNote(int id) throws SQLException {
        String sql = "DELETE FROM " + notesTable +
                " WHERE note_id = ? ";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }

    public void deleteNoteByUsername(String username) throws SQLException {
        String query = "DELETE FROM " + notesTable + " WHERE sender_username = '" + username + "' OR receiver_username = '" + username + "';" ;

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.executeUpdate();
    }
}
