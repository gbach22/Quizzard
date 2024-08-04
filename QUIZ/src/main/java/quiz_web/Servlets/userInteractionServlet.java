package quiz_web.Servlets;

import quiz_web.Database.DbConnection;
import quiz_web.Database.RelationshipDbManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;


@WebServlet("/userInteraction")
public class userInteractionServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DbConnection dbCon = (DbConnection)getServletContext().getAttribute("database_connection");
        RelationshipDbManager relDB = new RelationshipDbManager(dbCon.getConnection(), false);

        String senderName = request.getParameter("sender_name");
        String receiverName = request.getParameter("receiver_name");
        String content = request.getParameter("message_content");
        String friendship = request.getParameter("friendship");

        try {
            if (friendship.equals("true")) {
                String friendButton = request.getParameter("friend_button");

                switch (friendButton) {
                    case "accept_request":
                        relDB.respondToRequest(receiverName, senderName, "accepted");
                        break;
                    case "reject_request":
                        relDB.respondToRequest(receiverName, senderName, "rejected");
                        break;
                    case "add_friend":
                        relDB.sendFriendRequest(senderName, receiverName);
                        break;
                    case "cancel_request":
                        relDB.cancelRequest(senderName, receiverName);
                    default:
                        relDB.removeFriendship(senderName, receiverName);
                        break;
                }
            } else if (friendship.equals("false")) {
                relDB.sendNote(senderName, receiverName, content, "note");
            } else {
                // challenge
                int quizId = Integer.parseInt(request.getParameter("challenge_button"));
                String quizName = request.getParameter("quiz_name" + quizId);
                String maxScore = request.getParameter("max_score" + quizId);
                System.out.println(quizName);
                System.out.println(maxScore);
                String link = "<b><a href=/homePage/quiz.jsp?quizId=" + quizId + ">" + quizName + "</a></b>";
                String HTMLMessage = "has challenged you to take quiz " + link;
                HTMLMessage += "<br><br>Their best score is " + maxScore;
                relDB.sendNote(senderName, receiverName, HTMLMessage, "challenge");
            }

        } catch (SQLException e) {
                throw new RuntimeException(e);
        }

        response.sendRedirect("/profile/profile.jsp?profileUsername=" + receiverName);
    }
}
