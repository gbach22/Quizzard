package quiz_web.Servlets;

import quiz_web.Database.DbConnection;
import quiz_web.Database.RatingReviewDbManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/ratingAndReview")
public class addRatingAndReviewServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        int rating = Integer.parseInt(httpServletRequest.getParameter("rating"));
        int quizId = Integer.parseInt(httpServletRequest.getParameter("quizId"));
        String quizReview = httpServletRequest.getParameter("quizReview");
        String username = httpServletRequest.getParameter("username");

        DbConnection dbConnection = (DbConnection) httpServletRequest.getServletContext().getAttribute("database_connection");
        RatingReviewDbManager ratingReviewDbManager = new RatingReviewDbManager(dbConnection.getConnection(), false);

        try {
            ratingReviewDbManager.addRateAndReview(username, quizId, rating, quizReview);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String achievement = httpServletRequest.getParameter("achievement");

        if(achievement == null) {
            httpServletResponse.sendRedirect("/homePage/homePage.jsp");
        }else{
            httpServletRequest.getRequestDispatcher("/addAchievement").forward(httpServletRequest,httpServletResponse);
        }
    }
}
