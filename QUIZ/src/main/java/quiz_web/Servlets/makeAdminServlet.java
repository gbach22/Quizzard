package quiz_web.Servlets;

import quiz_web.Database.DbConnection;
import quiz_web.Database.UserDbManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/makeAdmin")
public class makeAdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String username = (String) httpServletRequest.getParameter("username");

        DbConnection dbConnection = (DbConnection) getServletContext().getAttribute("database_connection");
        UserDbManager userDbManager = new UserDbManager(dbConnection.getConnection(), false);

        try {
            userDbManager.makeAdmin(username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        httpServletResponse.sendRedirect(httpServletRequest.getHeader("Referer"));
    }
}
