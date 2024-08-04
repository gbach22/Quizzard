package quiz_web.Database;

import org.apache.commons.dbcp2.PStmtKey;
import quiz_web.Models.Announcement;
import quiz_web.Models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static quiz_web.Database.DatabaseInfo.*;

public class AnnouncementDbManager {
    private Connection connection;
    private String announcementsTable;
    private String unseenAnnouncementsTable;
    public AnnouncementDbManager(Connection connection, boolean testMode) {
        this.connection = connection;
        if (testMode) {
            announcementsTable = "test_" + ANNOUNCEMENTS_TABLE;
            unseenAnnouncementsTable = "test_" + UNSEEN_ANNOUNCEMENTS_TABLE;
        } else {
            announcementsTable = ANNOUNCEMENTS_TABLE;
            unseenAnnouncementsTable = UNSEEN_ANNOUNCEMENTS_TABLE;
        }
    }

    public void addAnnouncement(String username, String announcement) throws SQLException {
        String query = "INSERT INTO " + announcementsTable + "(username, announcement) VALUES (?, ?)";

        PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, username);
        stmt.setString(2, announcement);

        int affectedRows = stmt.executeUpdate();
        int id = -1;

        if (affectedRows > 0) {
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        }

        UserDbManager userDbManager = new UserDbManager(connection);
        List<User> allUsers = userDbManager.getAllUsers(false);
        for (User user : allUsers) {
            if (!user.getUserName().equals(username)) {
                query = "INSERT INTO " + unseenAnnouncementsTable + "(announcement_id, username) VALUES (?, ?)";

                stmt = connection.prepareStatement(query);
                stmt.setInt(1, id);
                stmt.setString(2, user.getUserName());
                stmt.executeUpdate();
            }
        }
    }


    public void deleteAll(String username) throws SQLException {
        String query = "DELETE FROM " + unseenAnnouncementsTable + " WHERE announcement_id IN " +
                "(SELECT announcement_id FROM " + announcementsTable + " WHERE username = '" + username+ "');";


        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.executeUpdate();

        query = "DELETE FROM " + announcementsTable + " WHERE username = '" + username + "';";
        stmt = connection.prepareStatement(query);
        stmt.executeUpdate();
    }

    public ArrayList<Announcement> getAnnouncements(String userName) throws SQLException {
        ArrayList<Announcement> result = new ArrayList<>();

        String query = "SELECT * FROM  " + announcementsTable + " WHERE username = '" + userName + "';";
        PreparedStatement stmt = connection.prepareStatement(query);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int announcement_id = rs.getInt("announcement_id");
            String username = rs.getString("username");
            String announcement = rs.getString("announcement");
            Timestamp creationDate = rs.getTimestamp("creation_date");
            result.add(new Announcement(announcement_id, username, announcement, creationDate));
        }

        return result;
    }

    public ArrayList<Announcement> getUnseenAnnouncements(String username, int num) throws SQLException {
        ArrayList<Integer> ids = new ArrayList<>();

        String query = "SELECT * FROM  " +  unseenAnnouncementsTable + " WHERE username = '" + username + "' ORDER BY creation_date DESC LIMIT " + num + ";";
        PreparedStatement stmt = connection.prepareStatement(query);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int announcement_id = rs.getInt("announcement_id");
            ids.add(announcement_id);
        }

        ArrayList<Announcement> result = new ArrayList<>();
        for (Integer id : ids) {
            result.add(getAnnouncementById(id));
        }

        return result;
    }

    public Announcement getAnnouncementById(int id) throws SQLException {
        String query = "SELECT * FROM  " + announcementsTable + " WHERE announcement_id = '" + id + "';";
        PreparedStatement stmt = connection.prepareStatement(query);

        ResultSet rs = stmt.executeQuery();
        rs.next();
        String username = rs.getString("username");
        String announcement = rs.getString("announcement");
        Timestamp creationDate = rs.getTimestamp("creation_date");
        return (new Announcement(id, username, announcement, creationDate));
    }

    public ArrayList<Announcement> getAnnouncementsByTimeRange(Timestamp startTime, Timestamp endTime, boolean allTime, String adminUser) throws SQLException {
        ArrayList<Announcement> result = new ArrayList<>();
        String query;

        if (allTime) {
            query = "SELECT * FROM " + announcementsTable + " WHERE username LIKE ? ORDER BY creation_date DESC;";
        } else {
            query = "SELECT * FROM " + announcementsTable + " WHERE username LIKE ? AND creation_date BETWEEN ? AND ? ORDER BY creation_date DESC;";
        }

        PreparedStatement stmt = connection.prepareStatement(query);
        if (adminUser.equals("All")) {
            stmt.setString(1, "%");
        }
        else stmt.setString(1, adminUser);

        if (!allTime) {
            stmt.setTimestamp(2, startTime);
            stmt.setTimestamp(3, endTime);
        }

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int announcement_id = rs.getInt("announcement_id");
            String username = rs.getString("username");
            String announcement = rs.getString("announcement");
            Timestamp creationDate = rs.getTimestamp("creation_date");
            result.add(new Announcement(announcement_id, username, announcement, creationDate));
        }

        return result;
    }

    public void deleteUnseenAnnouncementsByUsername(String username) throws SQLException {
        String query = "DELETE FROM " + unseenAnnouncementsTable + " WHERE username = '" + username + "';";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.executeUpdate();
    }


    public void sawAnnouncements(String username, int num) throws SQLException {
        String query = "DELETE FROM " + unseenAnnouncementsTable + " WHERE username = '" + username + "' ORDER BY creation_date DESC LIMIT " + num + ";";

        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.executeUpdate();
    }


}
