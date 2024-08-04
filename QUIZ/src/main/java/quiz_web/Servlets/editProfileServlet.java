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

@WebServlet("/editProfile")
public class editProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //super.doGet(httpServletRequest, httpServletResponse);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DbConnection dbCon = (DbConnection) request.getServletContext().getAttribute("database_connection");
        UserDbManager db = new UserDbManager(dbCon.getConnection());

        HttpSession session = request.getSession();
        User curUser = (User) session.getAttribute("curUser");


        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String pictureURL = request.getParameter("picture_link");
        String bio = request.getParameter("bio");
        String oldPassword = request.getParameter("old_password");
        String password = request.getParameter("password");
        String repeatPassword = request.getParameter("repeat_password");
        String button = request.getParameter("submit_button");
        int responseMessageId = 3;

        try {
            assert(db.usernameExists(curUser.getUserName()));
            if (!User.hashPassword(oldPassword).equals(curUser.getHashedPassword())) {
                responseMessageId = 0;
                response.sendRedirect("/profile/editProfile.jsp?responseMessageId=" + responseMessageId);
                return;
            }
            if (button.equals("Save Changes Above")) {

                if (!firstName.trim().isEmpty()) {
                    curUser.setFirstName(firstName);
                    db.setFirstName(curUser.getUserName(), firstName);
                    responseMessageId = 2;
                }
                if (!lastName.trim().isEmpty()) {
                    curUser.setLastName(lastName);
                    db.setLastName(curUser.getUserName(), lastName);
                    responseMessageId = 2;
                }
                if (!pictureURL.trim().isEmpty()) {
                    curUser.setPictureURL(pictureURL);
                    db.setPictureURL(curUser.getUserName(), pictureURL);
                    responseMessageId = 2;
                }
                if (!bio.trim().isEmpty()) {
                    curUser.setBio(bio);
                    db.setBio(curUser.getUserName(), bio);
                    responseMessageId = 2;
                }
            } else {
                if (!password.isEmpty() && password.equals(repeatPassword)) {
                    String hashedPw = User.hashPassword(password);
                    curUser.setHashedPassword(hashedPw);
                    db.setHashedPassword(curUser.getUserName(), hashedPw);
                    responseMessageId = 2;
                } else {
                    responseMessageId = 1;
                }
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        if (responseMessageId == 2) response.sendRedirect("/profile/profile.jsp?profileUsername=" + curUser.getUserName());
        else response.sendRedirect("/profile/editProfile.jsp?responseMessageId=" + responseMessageId);
    }
}
