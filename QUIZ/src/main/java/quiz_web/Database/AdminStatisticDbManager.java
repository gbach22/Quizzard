package quiz_web.Database;

import java.sql.*;
import java.util.HashMap;

import static quiz_web.Database.DatabaseInfo.*;

// to check line coverage you have to add AdminStatisticDbManage.java from Database package in modify run configuration

public class AdminStatisticDbManager {
    private String usersTable;
    private String quizzesTable;
    private String quizHistoryTable;
    private Connection connection;

    public AdminStatisticDbManager(Connection connection, boolean testMode) {
        this.connection = connection;
        if (testMode) {
            usersTable = "test_" + USERS_TABLE;
            quizzesTable = "test_" + QUIZZES_TABLE;
            quizHistoryTable = "test_" + QUIZ_HISTORY_TABLE;
        } else {
            usersTable = USERS_TABLE;
            quizzesTable = QUIZZES_TABLE;
            quizHistoryTable = QUIZ_HISTORY_TABLE;
        }
    }

    public int numberOfUsers(Timestamp fromDate, Timestamp toDate, boolean allTime) throws SQLException {
        String query = "SELECT COUNT(*) as cnt FROM " + usersTable;
        if (!allTime) {
            query = "SELECT COUNT(*) as cnt FROM " + usersTable + " WHERE creation_date BETWEEN '" + fromDate + "' AND '" + toDate + "'";
        }

        PreparedStatement preparedStatement = connection.prepareStatement(query);

        ResultSet resultSet = preparedStatement.executeQuery();
        int result = 0;
        if (resultSet.next()) {
            result = resultSet.getInt("cnt");
        }
        return result;
    }


    public int numberOfQuizzes(Timestamp fromDate, Timestamp toDate, boolean allTime) throws SQLException {
        String query = "SELECT COUNT(*) as cnt FROM " + quizzesTable;
        if(!allTime){
            query = "SELECT COUNT(*) as cnt FROM " + quizzesTable + " WHERE created_time BETWEEN '" + fromDate + "' AND '" + toDate + "'";
        }

        PreparedStatement preparedStatement = connection.prepareStatement(query);

        ResultSet resultSet = preparedStatement.executeQuery();
        int result = 0;
        if (resultSet.next()) {
            result = resultSet.getInt("cnt");
        }
        return result;
    }

    public int quizzesTakeCount(Timestamp fromDate, Timestamp toDate, boolean allTime) throws SQLException {
        String query = "SELECT SUM(taken) as total_takes FROM " + quizzesTable;

        if (!allTime) {
            query =  "SELECT COUNT(*) as total_takes FROM " + quizHistoryTable + " WHERE take_date BETWEEN '" + fromDate + "' AND '" + toDate + "';";
        }

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        int result = 0;
        if (resultSet.next()) {
            result = resultSet.getInt("total_takes");
        }

        return result;
    }


    public HashMap<String, Timestamp> registeredUsers(Timestamp fromDate, Timestamp toDate, boolean allTime) throws SQLException {
        HashMap<String, Timestamp> newUserMap = new HashMap<>();

        String query = "SELECT username, creation_date FROM " + usersTable;
        if(!allTime){
            query = "SELECT username, creation_date FROM " + usersTable + " WHERE creation_date BETWEEN '" + fromDate + "' AND '" + toDate + "'";
        }

        PreparedStatement preparedStatement = connection.prepareStatement(query);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String username = resultSet.getString("username");
            Timestamp registrationDate = resultSet.getTimestamp("creation_date");
            newUserMap.put(username, registrationDate);
        }

        return newUserMap;
    }



    public HashMap<Integer, Timestamp> createdQuizzes(Timestamp fromDate, Timestamp toDate, boolean allTime) throws SQLException {
        HashMap<Integer, Timestamp> newQuizMap = new HashMap<>();
        String query = "SELECT quiz_id, created_time FROM " + quizzesTable;

        if(!allTime){
            query = "SELECT quiz_id, created_time FROM " + quizzesTable + " WHERE created_time BETWEEN '" + fromDate + "' AND '" + toDate + "'";
        }

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int quizId = resultSet.getInt("quiz_id");
            Timestamp createdTime = resultSet.getTimestamp("created_time");
            newQuizMap.put(quizId, createdTime);
        }

        return newQuizMap;
    }

}
