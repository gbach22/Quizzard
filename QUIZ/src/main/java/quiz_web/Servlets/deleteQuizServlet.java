package quiz_web.Servlets;

import quiz_web.Database.DbConnection;
import quiz_web.Database.QuizDbManager;
import quiz_web.Database.QuizHistoryDbManager;
import quiz_web.Database.RatingReviewDbManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/deleteQuiz")
public class deleteQuizServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        int quizId = Integer.parseInt(httpServletRequest.getParameter("quizId"));

        DbConnection dbConnection = (DbConnection) httpServletRequest.getServletContext().getAttribute("database_connection");
        QuizDbManager quizDbManager = new QuizDbManager(dbConnection.getConnection(), false);
        QuizHistoryDbManager quizHistoryDbManager = new QuizHistoryDbManager(dbConnection.getConnection(), false);
        RatingReviewDbManager ratingReviewDbManager = new RatingReviewDbManager(dbConnection.getConnection(), false);

        try {
            ratingReviewDbManager.deleteAllRateReview(null, quizId);
            quizHistoryDbManager.deleteQuizHistory(null, quizId);
            quizDbManager.deleteQuiz(quizId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        httpServletResponse.sendRedirect("/homePage/homePage.jsp");
    }
}
