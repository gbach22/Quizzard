package quiz_web.Database;

import quiz_web.Models.QuizHistory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static quiz_web.Database.DatabaseInfo.QUIZ_HISTORY_TABLE;

public class QuizHistoryDbManager {
    private Connection connection;
    private String quizHistoryTable;

    public QuizHistoryDbManager(Connection connection, boolean testMode) {
        this.connection = connection;
        if(testMode) {
            quizHistoryTable = "test_" + QUIZ_HISTORY_TABLE;
        }else{
            quizHistoryTable = QUIZ_HISTORY_TABLE;
        }
    }

    public ArrayList<QuizHistory> getQuizHistoryByQuizId(int quizId) throws SQLException {
        String query = "SELECT * FROM " + quizHistoryTable + " WHERE quiz_id = ? ORDER BY take_date;";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, quizId);
        ResultSet rs = preparedStatement.executeQuery();
        ArrayList<QuizHistory> quizHistories = new ArrayList<>();

        while(rs.next()) {
            String username = rs.getString("username");
            String quizName = rs.getString("quiz_name");
            String pictureUrl = rs.getString("picture_url"); // Corrected column name
            double score = rs.getDouble("score");
            Timestamp time = rs.getTimestamp("take_date");
            int timeNeeded = rs.getInt("time_needed");
            QuizHistory quizHistory = new QuizHistory(username, quizName, pictureUrl, quizId, score, time, timeNeeded);
            quizHistories.add(quizHistory);
        }

        return quizHistories;
    }

    public ArrayList<QuizHistory> getQuizHistoryByUsername(String username, int num) throws SQLException {
        String query = "SELECT * FROM " + quizHistoryTable + " WHERE username = '" + username + "' ORDER BY take_date DESC;";
        if (num != -1) {
            query = "SELECT * FROM " + quizHistoryTable + " WHERE username = '" + username + "' ORDER BY take_date DESC LIMIT " + num + ";";
        }

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();
        ArrayList<QuizHistory> quizHistories = new ArrayList<>();

        while(rs.next()) {
            String userName = rs.getString("username");
            String quiz_name = rs.getString("quiz_name");
            String pictureUrl = rs.getString("picture_url"); // Corrected column name
            int quiz_id = rs.getInt("quiz_id");
            double score = rs.getDouble("score");
            Timestamp time = rs.getTimestamp("take_date");
            int time_needed = rs.getInt("time_needed");
            QuizHistory quizHistory = new QuizHistory(userName, quiz_name, pictureUrl, quiz_id, score, time, time_needed);
            quizHistories.add(quizHistory);
        }

        return quizHistories;
    }

    public void deleteQuizHistory(String username, int quizId) throws SQLException {
        String query = null;

        if (username == null) { // delete by quiz id
            query = "DELETE FROM " + quizHistoryTable + " WHERE quiz_id = '" + quizId + "';";
        } else if (quizId == -1) { // delete by username
            query = "DELETE FROM " + quizHistoryTable + " WHERE username = '" + username + "';";
        }

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.executeUpdate();
    }

    public double getAverageScoreByQuizId(int quizId) throws SQLException {
        String query = "SELECT score FROM " + quizHistoryTable + " WHERE quiz_id = '" + quizId + "';";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();

        double sum = 0;
        int counter = 0;
        while(rs.next()) {
            double curScore = rs.getDouble("score");
            counter++;
            sum += curScore;
        }

        return (sum / counter);
    }

    public ArrayList<QuizHistory> getQuizHistoryByUsernameAndQuizId(String username, int quizId, String orderBy) throws SQLException {
        ArrayList<QuizHistory> result = new ArrayList<>();
        String query = "SELECT quiz_name, picture_url, score, time_needed, take_date FROM " + quizHistoryTable + " WHERE quiz_id = '" + quizId +"' AND username = '" + username + "' ORDER BY " + orderBy + " desc;";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();

        while(rs.next()) {
            String quizName = rs.getString("quiz_name");
            String pictureUrl = rs.getString("picture_url"); // Corrected column name
            double score = rs.getDouble("score");
            int timeNeeded = rs.getInt("time_needed");
            Timestamp takeDate = rs.getTimestamp("take_date");
            result.add(new QuizHistory(username, quizName, pictureUrl, quizId, score, takeDate, timeNeeded));
        }
        return result;
    }

    public ArrayList<QuizHistory> topPerformers(int quizId, Timestamp from, Timestamp to, boolean allTime) throws SQLException {
        ArrayList<QuizHistory> result = new ArrayList<>();

        String query = "SELECT username, quiz_name, picture_url, score, time_needed, take_date FROM " + quizHistoryTable + " WHERE quiz_id = '" + quizId + "';";

        if(!allTime) {
            query = "SELECT username, quiz_name, picture_url, score, time_needed, take_date FROM " + quizHistoryTable
                    + " WHERE quiz_id = '" + quizId + "' AND take_date BETWEEN '" + from + "' AND '" + to + "' ORDER BY score desc";
        }
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();

        while(rs.next()) {
            String username = rs.getString("username");
            String quizName = rs.getString("quiz_name");
            String picture_url = rs.getString("picture_url");
            double score = rs.getDouble("score");
            int timeNeeded = rs.getInt("time_needed");
            Timestamp takeDate = rs.getTimestamp("take_date");
            result.add(new QuizHistory(username, quizName, picture_url, quizId, score, takeDate, timeNeeded));
        }

        return result;
    }

    public void addQuizHistory(QuizHistory quizH) throws SQLException {
        String query = "INSERT INTO " + quizHistoryTable + " (quiz_id, quiz_name, picture_url, username, score, time_needed) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement =connection.prepareStatement(query);
        preparedStatement.setInt(1, quizH.getQuizId());
        preparedStatement.setString(2, quizH.getQuizName());
        preparedStatement.setString(3, quizH.getQuizPic());
        preparedStatement.setString(4, quizH.getUsername());
        preparedStatement.setDouble(5, quizH.getScore());
        preparedStatement.setInt(6, quizH.getTimeNeeded());
        preparedStatement.executeUpdate();
    }

    public ArrayList<QuizHistory> getQuizzesRecentlyTakenByFriends(ArrayList<String> friends, int num) throws SQLException {
        List<QuizHistory> result = new ArrayList<>();
        for (String friend: friends) {
            result.addAll(getQuizHistoryByUsername(friend, num));
        }

        // Sort quizzes by createdTime in descending order (newest first)
        Collections.sort(result, new Comparator<QuizHistory>() {
            @Override
            public int compare(QuizHistory quiz1, QuizHistory quiz2) {
                return quiz2.getTakeDate().compareTo(quiz1.getTakeDate());
            }
        });

        return new ArrayList<>(result.subList(0, Math.min(result.size(), num)));
    }

    public int tookQuizCount(String username) throws SQLException {
        String query = "SELECT COUNT(*) FROM " + quizHistoryTable + " WHERE username = '" + username + "';";
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        ResultSet resultSet = preparedStatement.executeQuery();
        int result = 0;

        if(resultSet.next()) {
            result = resultSet.getInt(1);
        }

        return result;
    }

    public double highestScoreOnQuiz(int quizId) throws SQLException {
        String query = "SELECT MAX(score) FROM " + quizHistoryTable + " WHERE quiz_id = '" + quizId + "';";

        PreparedStatement preparedStatement = connection.prepareStatement(query);

        ResultSet resultSet = preparedStatement.executeQuery();
        double result;
        if (resultSet.next()) {
            result = resultSet.getDouble(1);
        } else {
            result = 0.0;
        }

        return  result;
    }

}
