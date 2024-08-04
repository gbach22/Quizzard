package quiz_web.Database;

import quiz_web.Models.Achievement;
import quiz_web.Models.Announcement;
import quiz_web.Models.User;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static quiz_web.Database.DatabaseInfo.*;

public class UserDbManager {

    private Connection connection;
    private String usersTable;
    private String friendsTable;


    // to support testing
    public UserDbManager(Connection con, boolean testMode) {
        this.connection = con;
        if (testMode) {
            usersTable = "test_" + USERS_TABLE;
            friendsTable = "test_" + FRIENDS_TABLE;
        } else {
            usersTable = USERS_TABLE;
            friendsTable = FRIENDS_TABLE;
        }
    }

    public UserDbManager(Connection con) {
        this(con, false);
    }

    public void storeUser(User user) throws SQLException {
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String hashPassword = user.getHashedPassword();
        String userName = user.getUserName();
        String bio = user.getBio();
        String pictureURL = user.getPictureURL();

        String query = "INSERT INTO " + usersTable;
        query += " (username, first_name, last_name, biography, hashed_pw, picture_url) VALUES\n";
        query += "('" + userName + "', '" + firstName + "', '" + lastName + "', '" + bio + "', '" + hashPassword + "', '" + pictureURL + "');";

        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.executeUpdate();

    }

    public boolean usernameExists(String username) throws SQLException {
        String query = "SELECT first_name FROM  " + usersTable + " WHERE username = '" + username + "';";

        PreparedStatement stmt = connection.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }


    public ArrayList<User> getUserByUsernamePart(String usernamePart, int num) throws SQLException {
        String query = "SELECT * FROM users_table WHERE username LIKE ? LIMIT ?;";

        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, "%" + usernamePart + "%");
        stmt.setInt(2, num);
        ResultSet rs = stmt.executeQuery();

        ArrayList<User> result = new ArrayList<>();
        while (rs.next()) {
            result.add(new User(rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("username"),
                    rs.getString("hashed_pw"),
                    rs.getString("biography"),
                    rs.getString("picture_url"),
                    rs.getBoolean("is_admin")));
        }

        return result;
    }


    public User getUser(String username) throws SQLException {
        if (!usernameExists(username)) {
            System.out.println("username doesn't exist should have checked");
            return null;
        }

        String query = "SELECT first_name, last_name, biography, hashed_pw, picture_url, is_admin FROM  " + usersTable + " WHERE username = ?;";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        boolean isAdmin = rs.getInt("is_admin") == 1;
        return new User(rs.getString("first_name"), rs.getString("last_name"), username, rs.getString("hashed_pw"),
                rs.getString("biography"), rs.getString("picture_url"), isAdmin);

    }


    public ArrayList<String> getFriendsList(String username) throws SQLException {
        String query = "SELECT friend_username FROM  " + friendsTable + " WHERE username = '" + username + "' ";
        query += "AND status = 'accepted';";

        PreparedStatement stmt = connection.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        ArrayList<String> result = new ArrayList<>();
        while (rs.next()) {
            result.add(rs.getString("friend_username"));
        }
        return result;
    }

    public void setFirstName(String userName, String newFirstName) throws SQLException {
        String query = "UPDATE " + usersTable + " ";
        query += "SET first_name = '" + newFirstName + "' ";
        query += "WHERE username = '" + userName + "';";

        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.executeUpdate();
    }

    public void setLastName(String userName, String newLastName) throws SQLException {
        String query = "UPDATE " + usersTable + " ";
        query += "SET last_name = '" + newLastName + "' ";
        query += "WHERE username = '" + userName + "';";

        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.executeUpdate();
    }

    public void setBio(String userName, String newBio) throws SQLException {
        String query = "UPDATE " + usersTable + " ";
        query += "SET biography = '" + newBio + "' ";
        query += "WHERE username = '" + userName + "';";

        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.executeUpdate();
    }

    public void setPictureURL(String userName, String newPicture) throws SQLException {
        String query = "UPDATE " + usersTable + " ";
        query += "SET picture_url = '" + newPicture + "' ";
        query += "WHERE username = '" + userName + "';";

        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.executeUpdate();
    }

    public void setHashedPassword(String userName, String newHashedPassword) throws SQLException {
        String query = "UPDATE " + usersTable + " ";
        query += "SET hashed_pw = '" + newHashedPassword + "' ";
        query += "WHERE username = '" + userName + "';";

        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.executeUpdate();
    }

    public void deleteUser(String userName) throws SQLException {
        String query = "DELETE FROM " + friendsTable + " WHERE username = '" + userName + "' " +" OR friend_username = '" + userName + "';";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.executeUpdate();

        query = "DELETE FROM " + usersTable + " WHERE username = '" + userName + "';";
        stmt = connection.prepareStatement(query);
        stmt.executeUpdate();

    }

    public void makeAdmin(String username) throws SQLException {
        String query = "UPDATE " + usersTable + " SET is_admin = 1" + " WHERE username = '" + username + "';";

        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.executeUpdate();
    }

    public List<User> getAllUsers(boolean onlyAdmins) throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users_table WHERE is_admin >= ?";
        int admin = 0;
        if (onlyAdmins) admin = 1;

        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, admin);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            User user = new User(
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("username"),
                    rs.getString("hashed_pw"),
                    rs.getString("biography"),
                    rs.getString("picture_url"),
                    rs.getBoolean("is_admin")
            );

            users.add(user);
        }
        return users;
    }

}
