package quiz_web.Servlets;

import quiz_web.Database.DbConnection;
import quiz_web.Database.QuizDbManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;

@WebServlet("/randomQuiz")
public class randomQuizServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // You can handle GET requests here if needed
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DbConnection connection = (DbConnection) getServletContext().getAttribute("database_connection");
        QuizDbManager quizDbManager = new QuizDbManager(connection.getConnection(), false);
        try {
            int numQuizzes = quizDbManager.getQuizzesBy("", "", "", null, null, true).size();
            Random random = new Random();
            int randomQuizId = random.nextInt(numQuizzes) + 1;
            response.sendRedirect("/homePage/quiz.jsp?quizId=" + randomQuizId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
