package quiz_web.Database;

import quiz_web.Models.Achievement;
import quiz_web.Models.QuizHistory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static quiz_web.Database.DatabaseInfo.*;
import static quiz_web.Database.DatabaseInfo.ANNOUNCEMENTS_TABLE;

public class AchievementDbManager {
    private Connection connection;
    private String achievementsTable;
    private String answersTable;
    private String choicesTable;

    public AchievementDbManager(Connection connection, boolean testMode) {
        this.connection = connection;
        if (testMode) {
            achievementsTable = "test_" + ACHIEVEMENTS_TABLE;
        } else {
            achievementsTable = ACHIEVEMENTS_TABLE;
        }
    }

    public void addAchievement(String username, int achievementType) throws SQLException {
        String query = "INSERT INTO " + achievementsTable + "(username, achievement_type) "
                + "VALUES ('" + username + "', '" + achievementType + "');";

        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.executeUpdate();
    }


    public void deleteAll(String username) throws SQLException {
        String query = "DELETE FROM " + achievementsTable + " WHERE username = '" + username + "';";

        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.executeUpdate();
    }

    public ArrayList<Achievement> getAchievements(String userName, int num) throws SQLException {
        ArrayList<Achievement> result = new ArrayList<>();

        String query = "SELECT * FROM  " + achievementsTable + " WHERE username = '" + userName + "' ORDER BY received_date DESC;";
        if (num != -1) {
            query = "SELECT * FROM  " + achievementsTable + " WHERE username = '" + userName + "' ORDER BY received_date DESC LIMIT " + num + ";";
        }
        PreparedStatement stmt = connection.prepareStatement(query);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int achievement_id = rs.getInt("achievement_id");
            String username = rs.getString("username");
            int achievementType = rs.getInt("achievement_type");
            Timestamp received_date = rs.getTimestamp("received_date");
            result.add(new Achievement(achievement_id, username, achievementType, received_date));
        }

        return result;
    }

    public ArrayList<Achievement> getRecentlyEarnedAchievementsByFriends(ArrayList<String> friends, int num) throws SQLException {
        List<Achievement> result = new ArrayList<>();
        for (String friend: friends) {
            result.addAll(getAchievements(friend, num));
        }

        Collections.sort(result, new Comparator<Achievement>() {
            @Override
            public int compare(Achievement a1, Achievement a2) {
                return a1.getReceived_date().compareTo(a2.getReceived_date());
            }
        });

        return new ArrayList<>(result.subList(0, Math.min(result.size(), num)));
    }
}
