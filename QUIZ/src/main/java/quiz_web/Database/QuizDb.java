package quiz_web.Database;

import quiz_web.Models.Answer;
import quiz_web.Models.Question;
import quiz_web.Models.Quiz;
import quiz_web.Models.User;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public interface QuizDb {
    public Quiz getQuizById(int id) throws SQLException;
    public List<Quiz> getQuizzesByUser(String username, int num) throws SQLException;
    public List<Question> getQuizQuestions(int quizId) throws SQLException;
    public List<String> getOptions(int questionId) throws SQLException;
    public List<Answer> getAnswers(int questionId) throws SQLException;

    // add quiz

    public int addQuizInfo(Quiz quiz) throws SQLException;
    public int addQuestion(Question question) throws SQLException;
    public void addAnswer(Answer answer) throws SQLException;
    public void addOption(int questionId, String optionText) throws SQLException;
    public void addTags(List<String> tags, int quizId) throws SQLException;
    public ArrayList<Quiz> getQuizzesBy(String category, String name, String tag, Timestamp fromDate, Timestamp toDate, boolean allTime) throws SQLException;
}
