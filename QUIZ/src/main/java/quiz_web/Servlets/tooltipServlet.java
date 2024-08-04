package quiz_web.Servlets;

import quiz_web.Models.Question;
import quiz_web.Models.questionTypes;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import static quiz_web.CONSTANTS.QUESTION_JSPS;

@WebServlet("/tooltipServlet")
public class tooltipServlet extends HttpServlet {
    protected synchronized void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String param1 = request.getParameter("param1");
        int questionNum = Integer.parseInt(param1);
        HttpSession session = request.getSession();
        List<Question> questions = (List<Question>) session.getAttribute("questions");
        Question currQuestion = questions.get(questionNum);
        questionTypes type = currQuestion.getQuestionType();
        response.sendRedirect("/quizPageCores/" + QUESTION_JSPS[type.getValue()] + "?queue_param=" + questionNum);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
