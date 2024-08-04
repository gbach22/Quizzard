package quiz_web.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import static quiz_web.Database.DatabaseInfo.*;

public class RatingReviewDbManager {
    private Connection connection;
    private String rateTable;
    private String quizzesTable;

    public RatingReviewDbManager(Connection connection, boolean testMode) {
        this.connection = connection;
        if (testMode) {
            quizzesTable = "test_" + QUIZZES_TABLE;
            rateTable = "test_" + RATE_TABLE;
        } else {
            quizzesTable = QUIZZES_TABLE    ;
            rateTable = RATE_TABLE;
        }
    }

    public void addRateAndReview(String username, int quizId, int rating, String review) throws SQLException {
        String insertQuery = "INSERT INTO rate (quiz_id, username, rating, review) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(insertQuery);
        statement.setInt(1, quizId);
        statement.setString(2, username);
        statement.setInt(3, rating);
        statement.setString(4, review);
        statement.executeUpdate();
    }

    public HashMap<String, String> getReviewsByQuizId(int quizId) throws SQLException {
        HashMap<String, String> reviews = new HashMap<>();
        String query = "SELECT username, review FROM " + rateTable + " WHERE quiz_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, quizId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            String username = resultSet.getString("username");
            String review = resultSet.getString("review");
            if(review != null && !review.isEmpty()) {
                reviews.put(username, review);
            }
        }

        return reviews;
    }

    public void deleteAllRateReview(String username, int quizId) throws SQLException {
        String query = null;

        if(username == null) { // delete by quizId
            query = "DELETE FROM " + rateTable + " WHERE quiz_id = '" + quizId + "';";
        }else if(quizId == -1) { // delete by username
            query = "DELETE FROM " + rateTable + " WHERE username = '" + username + "';";
        }

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.executeUpdate();
    }

    public double getAverageRatingByQuizId(int quizId) throws SQLException {
        String query = "SELECT rating FROM rate WHERE quiz_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, quizId);
        ResultSet resultSet = statement.executeQuery();

        int counter = 0, sum = 0;
        while (resultSet.next()) {
            int rating = resultSet.getInt("rating");
            if(rating != 0){
                counter++;
                sum += rating;
            }
        }
        return ((double) sum / counter);
    }
}
