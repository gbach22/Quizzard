package quiz_web.Servlets;

import quiz_web.Database.DbConnection;
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

@WebServlet("/Login")
public class loginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("Username");
        String password = request.getParameter("Password");

        HttpSession session = request.getSession();

        DbConnection dbCon = (DbConnection) request.getServletContext().getAttribute("database_connection");
        UserDbManager db = new UserDbManager(dbCon.getConnection());
        User user = null;

        String buttonPressed = request.getParameter("button");

        if (buttonPressed == null || buttonPressed.equals("Login")) {
            try {
                user = db.getUser(username);
                if (!db.usernameExists(username)) {
                    request.getRequestDispatcher("/login/notExist.jsp").forward(request, response);
                } else {
                    try {
                        if (!user.getHashedPassword().equals(User.hashPassword(password))){
                            request.getRequestDispatcher("/login/incorrectPassword.jsp").forward(request, response);
                        } else {
                            session.setAttribute("curUser", user);
                            request.getRequestDispatcher("/homePage/homePage.jsp").forward(request, response);
                        }
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (buttonPressed.equals("Log Out")){
            session.setAttribute("curUser", null);
            request.getRequestDispatcher("/start/startPage.jsp").forward(request, response);
        } else {
            session.setAttribute("curUser", null);
            request.getRequestDispatcher("/homePage/homePage.jsp").forward(request, response);
        }


    }

}
