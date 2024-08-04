package quiz_web.Database;

import quiz_web.Models.*;

import javax.servlet.http.HttpSession;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static quiz_web.Database.DatabaseInfo.*;

public class QuizDbManager implements QuizDb {
    private Connection connection;
    private String quizzesTable;
    private String questionsTable;
    private String answersTable;
    private String optionsTable;
    private String tagsTable;
    public QuizDbManager(Connection con, boolean testMode) {
        this.connection = con;

        if (testMode) {
            quizzesTable = "test_" + QUIZZES_TABLE;
            questionsTable = "test_" + QUESTIONS_TABLE;
            answersTable = "test_" + ANSWERS_TABLE;
            optionsTable = "test_" + CHOICES_TABLE;
            tagsTable = "test_" + TAGS_TABLE;
        } else {
            quizzesTable = QUIZZES_TABLE;
            questionsTable = QUESTIONS_TABLE;
            answersTable = ANSWERS_TABLE;
            optionsTable = CHOICES_TABLE;
            tagsTable = TAGS_TABLE;
        }
    }

    @Override
    public Quiz getQuizById(int id) throws SQLException {
        String query = "SELECT * FROM  " + quizzesTable + " WHERE quiz_id = '" + id + "';";
        PreparedStatement stmt = connection.prepareStatement(query);

        ResultSet rs = stmt.executeQuery();

        ArrayList<Quiz> quizzes = getQuizzesFromRs(rs);
        if (quizzes.isEmpty()) return null;
        return quizzes.get(0);
    }

    private ArrayList<Quiz> getQuizzesFromRs(ResultSet rs) throws SQLException {
        ArrayList<Quiz> res = new ArrayList<>();

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

            res.add(new Quiz(id, quizName, description, creatorUsername, pictureUrl, multiPage, random,
                    immediateCorrection, practiceMode, time, Categories.getByValue(category), views, taken));
        }

        return res;
    }

    @Override
    public List<Quiz> getQuizzesByUser(String username, int num) throws SQLException {
        String query = "SELECT * FROM  " + quizzesTable + " WHERE creator_username = '" + username + "';";
        if (num != -1) {
            query = "SELECT * FROM " + quizzesTable + " WHERE creator_username = '" + username + "' ORDER BY created_time DESC LIMIT " + num + ";";
        }
        PreparedStatement stmt = connection.prepareStatement(query);

        ResultSet rs = stmt.executeQuery();
        return getQuizzesFromRs(rs);
    }

    @Override
    public List<Question> getQuizQuestions(int quizId) throws SQLException {
        List<Question> res = new ArrayList<>();
        String query = "SELECT * FROM  " + questionsTable + " WHERE quiz_id = '" + quizId + "';";
        PreparedStatement stmt = connection.prepareStatement(query);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int questionId = rs.getInt("question_id");
            String questionText = rs.getString("question_text");
            int questionType = rs.getInt("question_type");
            String picUrl = rs.getString("picture_url");
            boolean sortedRelevant = rs.getBoolean("sorted_relevant");
            double point = rs.getDouble("score");

            res.add(new Question(questionId, questionText, questionTypes.getByValue(questionType), picUrl, sortedRelevant, quizId, point));
        }

        return res;
    }

    @Override
    public List<String> getOptions(int questionId) throws SQLException {
        List<String> res = new ArrayList<>();
        String query = "SELECT * FROM  " + optionsTable + " WHERE question_id = '" + questionId + "';";
        PreparedStatement stmt = connection.prepareStatement(query);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            res.add(rs.getString("answer"));
        }

        return res;
    }

    @Override
    public List<Answer> getAnswers(int questionId) throws SQLException {
        List<Answer> res = new ArrayList<>();
        String query = "SELECT * FROM  " + answersTable + " WHERE question_id = '" + questionId + "';";
        PreparedStatement stmt = connection.prepareStatement(query);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            String answer = rs.getString("answer");
            int answerNum = rs.getInt("answer_num");
            res.add(new Answer(questionId, answer, answerNum));
        }

        return res;
    }

    public int addQuizInfo(Quiz quiz) throws SQLException {
        String query = "";
        PreparedStatement stmt;
        if (quiz.getPictureUrl() == null || quiz.getPictureUrl().isEmpty()) {
            query = "INSERT INTO " + quizzesTable + " (quiz_name, description, creator_username, " +
                    "multi_page, random, immediate_correction, practice_mode, category) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            stmt = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            stmt = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, quiz.getQuizName());
            stmt.setString(2, quiz.getQuizDescription());
            stmt.setString(3, quiz.getCreatorUsername());
            stmt.setBoolean(4, quiz.isMultiPage());
            stmt.setBoolean(5, quiz.isRandom());
            stmt.setBoolean(6, quiz.isImmediateCorrection());
            stmt.setBoolean(7, quiz.isPracticeMode());
            stmt.setInt(8, quiz.getCategory().getValue());
        } else {
            query = "INSERT INTO " + quizzesTable + " (quiz_name, description, creator_username, " +
                    "picture_url, multi_page, random, immediate_correction, practice_mode, category) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            stmt = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, quiz.getQuizName());
            stmt.setString(2, quiz.getQuizDescription());
            stmt.setString(3, quiz.getCreatorUsername());
            stmt.setString(4, quiz.getPictureUrl());
            stmt.setBoolean(5, quiz.isMultiPage());
            stmt.setBoolean(6, quiz.isRandom());
            stmt.setBoolean(7, quiz.isImmediateCorrection());
            stmt.setBoolean(8, quiz.isPracticeMode());
            stmt.setInt(9, quiz.getCategory().getValue());
        }


        int affectedRows = stmt.executeUpdate();

        if (affectedRows > 0) {
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        return -1; // or throw an exception if no key was generated
    }

    public int addQuestion(Question question) throws SQLException {
        String query = "INSERT INTO " + questionsTable + "(question_text, question_type, picture_url, sorted_relevant, quiz_id, score) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        stmt.setString(1, question.getQuestionText());
        stmt.setInt(2, question.getQuestionType().getValue());
        stmt.setString(3, question.getPictureUrl());
        stmt.setBoolean(4, question.isSortedRelevant());
        stmt.setInt(5, question.getQuizId());
        stmt.setDouble(6, question.getPoint());

        int affectedRows = stmt.executeUpdate();

        if (affectedRows > 0) {
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Return the generated question_id
            }
        }

        throw new SQLException("Creating question failed, no ID obtained.");
    }

    public void addAnswer(Answer answer) throws SQLException {
        String query = "INSERT INTO " + answersTable + "(question_id, answer, answer_num) VALUES (?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        stmt.setInt(1, answer.getQuestionId());
        stmt.setString(2, answer.getAnswer());
        stmt.setInt(3, answer.getAnswerNum());

        stmt.executeUpdate();
    }

    public void deleteQuiz(int quizId) throws SQLException {
        String query =  "DELETE FROM " + answersTable + " WHERE question_id IN "
                + "(SELECT question_id FROM " + questionsTable + " WHERE quiz_id = '" + quizId + "');";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.executeUpdate();

        query = "DELETE FROM " + tagsTable + " WHERE quiz_id = '" + quizId + "';";
        stmt = connection.prepareStatement(query);
        stmt.executeUpdate();

        query = "DELETE FROM " + optionsTable + " WHERE question_id IN "
                + "(SELECT question_id FROM " + questionsTable + " WHERE quiz_id = '" + quizId + "');";
        stmt = connection.prepareStatement(query);
        stmt.executeUpdate();

        query = "DELETE FROM " + questionsTable + " WHERE quiz_id = '" + quizId + "';";
        stmt = connection.prepareStatement(query);
        stmt.executeUpdate();

        query = "DELETE FROM " + quizzesTable + " WHERE quiz_id = '" + quizId + "';";
        stmt = connection.prepareStatement(query);
        stmt.executeUpdate();
    }

    public void addOption(int questionId, String optionText) throws SQLException {
        String query = "INSERT INTO " + optionsTable + "(question_id, answer) VALUES (?, ?)";
        PreparedStatement stmt = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        stmt.setInt(1, questionId);
        stmt.setString(2, optionText);

        stmt.executeUpdate();
    }

    public void incrementViews(int id) throws SQLException {
        String query = "UPDATE " + quizzesTable + " SET views = views + 1 WHERE quiz_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void incrementTaken(int id) throws SQLException {
        String query = "UPDATE " + quizzesTable + " SET taken = taken + 1 WHERE quiz_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public int viewCount(int quizId) throws SQLException {
        int result = 0;
        String query = "SELECT views FROM " + quizzesTable + " WHERE quiz_id = '" + quizId + "';";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        if(resultSet.next()) {
            result = resultSet.getInt("views");
        }

        return result;
    }

    public int takeCount(int quizId) throws SQLException {
        int result = 0;
        String query = "SELECT taken FROM " + quizzesTable + " WHERE quiz_id = '" + quizId + "';";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        if(resultSet.next()) {
            result = resultSet.getInt("taken");
        }

        return result;
    }

    public void addTags(List<String> tags, int quizId) throws SQLException {
        for (String tag: tags) {
            String query = "INSERT INTO " + tagsTable + "(tag_name, quiz_id) VALUES (?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, tag);
            stmt.setInt(2,quizId);

            stmt.executeUpdate();
        }
    }

    public ArrayList<String> getTags(int quizId) throws SQLException {
        ArrayList<String> result = new ArrayList<String>();
        String query = "SELECT tag_name FROM " + tagsTable + " WHERE quiz_id = '" + quizId + "';";

        PreparedStatement preparedStatement = connection.prepareStatement(query);

        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()) {
            result.add(rs.getString("tag_name"));
        }

        return result;
    }

    public ArrayList<Quiz> getQuizzesBy(String category, String name, String tag, Timestamp fromDate, Timestamp toDate, boolean allTime) throws SQLException {
        StringBuilder query = new StringBuilder("SELECT * FROM " + quizzesTable  + " WHERE 1=1");

        List<Object> parameters = new ArrayList<>();

        if (category != null && !category.isEmpty() && !category.equals("ALL")) {
            Categories c = Categories.getByName(category);
            if (c != null) {
                int categoryNum = c.getValue();
                query.append(" AND category = ?");
                parameters.add(categoryNum);
            }
        }

        if (name != null && !name.isEmpty()) {
            query.append(" AND quiz_name LIKE ?");
            parameters.add("%" + name + "%");
        }

        if (tag != null && !tag.isEmpty()) {
            query.append(" AND quiz_id IN (SELECT quiz_id FROM tags WHERE tag_name = ?)");
            parameters.add(tag);
        }

        if (!allTime) {
            System.out.println(fromDate);
            System.out.println(toDate);
            if (fromDate != null) {
                query.append(" AND created_time >= ?");
                parameters.add(fromDate.toString());
            }
            if (toDate != null) {
                query.append(" AND created_time <= ?");
                parameters.add(toDate.toString());
            }
        }

        PreparedStatement stmt = connection.prepareStatement(query.toString());

        for (int i = 0; i < parameters.size(); i++) {
            stmt.setObject(i + 1, parameters.get(i));
        }

        System.out.println(stmt);

        ResultSet rs = stmt.executeQuery();
        return getQuizzesFromRs(rs);
    }

    public List<Quiz> recentQuizzesCreatedByFriends(ArrayList<String> friends, int num) throws SQLException {
        List<Quiz> result = new ArrayList<>();
        for (String friend: friends) {
            result.addAll(getQuizzesByUser(friend, num));
        }

        Collections.sort(result, new Comparator<Quiz>() {
            @Override
            public int compare(Quiz quiz1, Quiz quiz2) {
                return quiz2.getCreatedTime().compareTo(quiz1.getCreatedTime());
            }
        });

        return new ArrayList<>(result.subList(0, Math.min(result.size(), num)));
    }
}
