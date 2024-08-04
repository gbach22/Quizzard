package quiz_web.Servlets;

import quiz_web.Database.AnnouncementDbManager;
import quiz_web.Database.DbConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/addAnnouncement")
public class adminAddAnnouncementServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String username = (String) httpServletRequest.getParameter("username");
        String announcement = (String)httpServletRequest.getParameter("announcement");
        DbConnection dbConnection = (DbConnection) httpServletRequest.getServletContext().getAttribute("database_connection");
        AnnouncementDbManager announcementDbManager = new AnnouncementDbManager(dbConnection.getConnection(), false);

        try {
            announcementDbManager.addAnnouncement(username, announcement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        httpServletResponse.sendRedirect("/homePage/homePage.jsp");
    }
}
