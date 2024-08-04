package quiz_web.Database;

import quiz_web.Models.Categories;
import quiz_web.Models.Quiz;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static quiz_web.Database.DatabaseInfo.*;
import static quiz_web.Database.DatabaseInfo.CHOICES_TABLE;

public class StatisticsDbManager {
    private Connection connection;
    private String quizzesTable;
    public StatisticsDbManager(Connection con, boolean testMode) {
        this.connection = con;

        if (testMode) {
            quizzesTable = "test_" + QUIZZES_TABLE;
        } else {
            quizzesTable = QUIZZES_TABLE;
        }
    }

    public List<Quiz> mostViewedQuizzes(int num) throws SQLException {
        List<Quiz> quizzes = new ArrayList<>();
        String query = "SELECT * FROM " + quizzesTable + " ORDER BY views DESC LIMIT " + num;
        PreparedStatement stmt = connection.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("quiz_id");
            String quizName = rs.getString("quiz_name");
            String description = rs.getString("description");
            String creatorUsername = rs.getString("creator_username");
            String pictureUrl = rs.getString("picture_url");
            boolean multiPage = rs.getBoolean("multi_page");
            boolean random = rs.getBoolean("random");
            boolean immediateCorrection = rs.getBoolean("immediate_correction");
            boolean practiceMode = rs.getBoolean("practice_mode");
            Timestamp time = rs.getTimestamp("created_time");
            int category = rs.getInt("category");
            int views = rs.getInt("views");
            int taken = rs.getInt("taken");

            quizzes.add(new Quiz(id, quizName, description, creatorUsername, pictureUrl, multiPage, random,
                    immediateCorrection, practiceMode, time, Categories.getByValue(category), views, taken));
        }

        return quizzes;
    }

    public List<Quiz> recentQuizzes(int num) throws SQLException {
        List<Quiz> quizzes = new ArrayList<>();
        String query = "SELECT * FROM " + quizzesTable + " ORDER BY created_time DESC LIMIT " + num;
        PreparedStatement stmt = connection.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("quiz_id");
            String quizName = rs.getString("quiz_name");
            String description = rs.getString("description");
            String creatorUsername = rs.getString("creator_username");
            String pictureUrl = rs.getString("picture_url");
            boolean multiPage = rs.getBoolean("multi_page");
            boolean random = rs.getBoolean("random");
            boolean immediateCorrection = rs.getBoolean("immediate_correction");
            boolean practiceMode = rs.getBoolean("practice_mode");
            Timestamp time = rs.getTimestamp("created_time");
            int category = rs.getInt("category");
            int views = rs.getInt("views");
            int taken = rs.getInt("taken");

            quizzes.add(new Quiz(id, quizName, description, creatorUsername, pictureUrl, multiPage, random,
                    immediateCorrection, practiceMode, time, Categories.getByValue(category), views, taken));
        }

        return quizzes;
    }

}
