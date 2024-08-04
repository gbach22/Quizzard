package quiz_web.Servlets;

import quiz_web.Models.Answer;
import quiz_web.Models.Question;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/startCreatingQuiz")
public class startCreatingQuizServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        session.setAttribute("create_quiz_name", null);
        session.setAttribute("create_quiz_desc", null);
        session.setAttribute("create_quiz_pic", null);
        session.setAttribute("create_category", null);
        session.setAttribute("create_is_multi", null);
        session.setAttribute("create_is_random", null);
        session.setAttribute("create_is_immediate", null);
        session.setAttribute("create_is_practice", null);

        List<String> tags = new ArrayList<>();
        List<Question> questions = new ArrayList<>();
        List<List<Answer>> answers = new ArrayList<>();
        List<List<String>> wrongAnswers = new ArrayList<>();

        session.setAttribute("create_questions", questions);
        session.setAttribute("create_answers", answers);
        session.setAttribute("create_wrong_answers", wrongAnswers);
        session.setAttribute("create_tags", tags);

        request.getRequestDispatcher("/createQuiz/quizInfo.jsp").forward(request, response);
    }
}
