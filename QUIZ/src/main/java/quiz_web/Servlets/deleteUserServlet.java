package quiz_web.Servlets;

import quiz_web.Database.*;
import quiz_web.Models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/deleteUser")
public class deleteUserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String userToDelete = (String) httpServletRequest.getParameter("username");
        User curUser = (User) httpServletRequest.getSession().getAttribute("curUser");
        DbConnection dbConnection = (DbConnection) getServletContext().getAttribute("database_connection");
        UserDbManager userDbManager = new UserDbManager(dbConnection.getConnection(), false);
        AnnouncementDbManager announcementDbManager = new AnnouncementDbManager(dbConnection.getConnection(), false);
        AchievementDbManager  achievementDbManager = new AchievementDbManager(dbConnection.getConnection(), false);
        QuizHistoryDbManager quizHistoryDbManager = new QuizHistoryDbManager(dbConnection.getConnection(), false);
        RatingReviewDbManager ratingReviewDbManager = new RatingReviewDbManager(dbConnection.getConnection(), false);
        RelationshipDbManager relationshipDbManager = new RelationshipDbManager(dbConnection.getConnection(), false);

        try {
            announcementDbManager.deleteUnseenAnnouncementsByUsername(userToDelete);
            relationshipDbManager.deleteNoteByUsername(userToDelete);
            ratingReviewDbManager.deleteAllRateReview(userToDelete, -1);
            achievementDbManager.deleteAll(userToDelete);
            announcementDbManager.deleteAll(userToDelete);
            quizHistoryDbManager.deleteQuizHistory(userToDelete, -1);
            userDbManager.deleteUser(userToDelete);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        if(userToDelete.equals(curUser.getUserName())) {
            httpServletRequest.getSession().setAttribute("curUser", null);
            httpServletResponse.sendRedirect("/start/startPage.jsp");
        }else{
            httpServletResponse.sendRedirect("/homePage/homePage.jsp");
        }
    }
}
