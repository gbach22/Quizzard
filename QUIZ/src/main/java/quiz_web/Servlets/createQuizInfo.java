package quiz_web.Servlets;

import quiz_web.Database.DbConnection;
import quiz_web.Database.QuizDbManager;
import quiz_web.Models.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/createQuizInfo")
public class createQuizInfo extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String quizName = request.getParameter("quiz_name");
        String quizDescription = request.getParameter("quiz_description");
        String quizPic = request.getParameter("quiz_picture");
        String multiStr = request.getParameter("multi");
        String randomStr = request.getParameter("random");
        String immediateStr = request.getParameter("immediate");
        String practiceStr = request.getParameter("practice");
        String categoryStr = request.getParameter("category");
        String tag = request.getParameter("tag");
        String buttonPressed = request.getParameter("button");
        Categories category = Categories.getByName(categoryStr);
        boolean multi = (multiStr != null);
        boolean random = (randomStr != null);
        boolean immediate = (immediateStr != null);
        boolean practice = (practiceStr != null);

        HttpSession session = request.getSession();

        session.setAttribute("create_quiz_name", quizName);
        session.setAttribute("create_quiz_desc", quizDescription);
        session.setAttribute("create_quiz_pic", quizPic);
        session.setAttribute("create_category", categoryStr);
        session.setAttribute("create_is_multi", multi);
        session.setAttribute("create_is_random", random);
        session.setAttribute("create_is_immediate", immediate);
        session.setAttribute("create_is_practice", practice);

        List<String> tags = (List<String>)session.getAttribute("create_tags");

        if (buttonPressed == null || buttonPressed.equals("Add Tag")) {
            if (tag != null && !tag.isEmpty()) {
                tags.add(tag);
                session.setAttribute("create_tags", tags);
            }

            response.sendRedirect("/createQuiz/quizInfo.jsp");
        } else if (buttonPressed.startsWith("Delete Tag")) {
            int index = storeQuestionAnswerServlet.getDeleteIndex(buttonPressed);
            tags.remove(index);

            session.setAttribute("create_tags", tags);

            response.sendRedirect("/createQuiz/quizInfo.jsp");
        } else {
            if (tag != null && !tag.isEmpty()) {
                tags.add(tag);
                session.setAttribute("create_tags", tags);
            }


            String curUsername = ((User)session.getAttribute("curUser")).getUserName();
            Quiz curQuiz = new Quiz(-1, quizName, quizDescription, curUsername, quizPic, multi, random, immediate, practice, null, category,0, 0);
            session.setAttribute("create_quiz", curQuiz);

            response.sendRedirect("/createQuiz/selectQuestionType.jsp?ind=0");
        }
    }
}
