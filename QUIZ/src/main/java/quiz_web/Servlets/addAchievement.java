package quiz_web.Servlets;

import quiz_web.Database.AchievementDbManager;
import quiz_web.Database.DbConnection;
import quiz_web.Models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/addAchievement")
public class addAchievement extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String achievement = httpServletRequest.getParameter("achievement");
        int achievementType, achievementType2 = -1;
        if(achievement.length() > 1) {
            achievementType = Character.getNumericValue(achievement.charAt(0));
            achievementType2 = Character.getNumericValue(achievement.charAt(1));
        } else {
            achievementType = Integer.parseInt(httpServletRequest.getParameter("achievement"));
        }

        HttpSession session = httpServletRequest.getSession();
        String username = ((User)session.getAttribute("curUser")).getUserName();

        DbConnection db = (DbConnection) httpServletRequest.getServletContext().getAttribute("database_connection");
        AchievementDbManager achievementDbManager = new AchievementDbManager(db.getConnection(), false);

        try {
            achievementDbManager.addAchievement(username, achievementType);
            if(achievementType2 != -1) {
                achievementDbManager.addAchievement(username, achievementType2);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        httpServletResponse.sendRedirect("/achievement/achievement.jsp?type1=" + achievementType + "&type2=" + achievementType2);
    }
}
