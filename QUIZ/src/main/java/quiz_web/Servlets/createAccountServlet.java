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

@WebServlet("/create")
public class createAccountServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

    }

    private boolean isEmpty(String value) {
        return (value == null || value.trim().isEmpty());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DbConnection dbCon = (DbConnection) request.getServletContext().getAttribute("database_connection");
        UserDbManager db = new UserDbManager(dbCon.getConnection());

        HttpSession session = request.getSession();

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");

        boolean userExists = false;
        try {
            userExists = db.usernameExists(userName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(isEmpty(userName) || isEmpty(password) || isEmpty(firstName) || isEmpty(lastName)) {
            request.setAttribute("error", "Invalid input");
            request.getRequestDispatcher("/create/invalidCreation.jsp").forward(request, response);
        }else if(userExists){ //if(user with username= userName exists (search in userDao)) {
            request.setAttribute("error", "Account with current usernmae already exists");
            request.getRequestDispatcher("/create/invalidCreation.jsp").forward(request, response);
        }else{
            /*
             * თუ მსგავსი username არ მოიძებნა და input იც ვალიდურია მაშინ ბაზაში ვამატებთ account ს
             * ვქმნით ახალ session ს რომელსაც დავუსეტავთ საჭირო ინფორმაციას ამ user ის შესახებ
             * და გადავამისამართებთ უკვე homePage.jsp ზე
             * */
            String hashedPw = "";
            try {
                hashedPw = User.hashPassword(password);
                //System.out.println(hashedPw);
                User newUser = new User(firstName, lastName, userName, hashedPw, "", "", false);
                db.storeUser(newUser);
                session.setAttribute("curUser", newUser);
                response.sendRedirect("/homePage/homePage.jsp");
            } catch (NoSuchAlgorithmException | SQLException e) {
                throw new RuntimeException(e);
            }


        }
    }
}
