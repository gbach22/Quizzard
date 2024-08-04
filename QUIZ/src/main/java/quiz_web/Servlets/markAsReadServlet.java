package quiz_web.Servlets;

import quiz_web.Database.DbConnection;
import quiz_web.Database.RelationshipDbManager;
import quiz_web.Database.UserDbManager;
import quiz_web.Models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@WebServlet("/markAsRead")
public class markAsReadServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        User curUser = (User)session.getAttribute("curUser");
        DbConnection dbConnection = (DbConnection)getServletContext().getAttribute("database_connection");

        RelationshipDbManager relationshipDbManager = new RelationshipDbManager(dbConnection.getConnection(), false);
        String button = request.getParameter("button");
        if (button != null) {
            if (button.equals("prev")) {
                Integer index = (Integer)session.getAttribute("note_ind");
                if (index == null) index = 0;
                session.setAttribute("note_ind", index - 1);
            } else if (button.equals("next")) {
                Integer index = (Integer)session.getAttribute("note_ind");
                if (index == null) index = 0;
                session.setAttribute("note_ind", index + 1);
            } else {
                int id = storeQuestionAnswerServlet.getDeleteIndex(button);

                try {
                    relationshipDbManager.deleteNote(id);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        response.sendRedirect("/homePage/homePage.jsp");

    }
}
