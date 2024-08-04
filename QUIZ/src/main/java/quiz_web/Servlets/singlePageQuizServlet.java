package quiz_web.Servlets;

import quiz_web.Database.DbConnection;
import quiz_web.Database.QuizDbManager;
import quiz_web.Database.QuizHistoryDbManager;
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
import java.util.HashMap;
import java.util.List;


@WebServlet("/singlePageFinished")
@SuppressWarnings("unchecked")
public class singlePageQuizServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        List<Question> questions = (List<Question>)session.getAttribute("questions");
        HashMap<Integer, ArrayList<Answer>> userAnswerMap = new HashMap<>();
        List<List<Answer>> realAnswers = (List<List<Answer>>)session.getAttribute("realAnswers");
        List<List<String>> options = (List<List<String>>)session.getAttribute("posAnswers");

        for (int i = 0; i < questions.size(); i++) {
            Question currQuestion = questions.get(i);
            nextQuestionServlet.extractAnswer(request, i, currQuestion, userAnswerMap, realAnswers);
        }

        DbConnection dbCon = (DbConnection)getServletContext().getAttribute("database_connection");
        nextQuestionServlet.resultPreparation(questions, session, userAnswerMap, realAnswers, options, dbCon);
        response.sendRedirect("/quizResults/singlePageResults.jsp");
    }
}
