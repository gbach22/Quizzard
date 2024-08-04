package quiz_web.Servlets;

import quiz_web.Database.DbConnection;
import quiz_web.Database.QuizDbManager;
import quiz_web.Models.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Array;
import java.sql.SQLException;
import java.util.*;

import static quiz_web.CONSTANTS.QUESTION_JSPS;
import static quiz_web.Models.questionTypes.*;

@WebServlet("/startQuiz")
public class startQuizServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        DbConnection dbConnection = (DbConnection)getServletContext().getAttribute("database_connection");
        QuizDbManager quizDbManager = new QuizDbManager(dbConnection.getConnection(), false);

        int quizId = Integer.parseInt(request.getParameter("quizId"));
        session.setAttribute("quiz_id_attr", quizId);
        String quizName;
        boolean isMultiplePageQuiz;
        boolean shouldRandomize;
        boolean isImmediateCorrection;
        List<Question> questions;
        List<List<String>> options = new ArrayList<>();
        List<List<Answer>> realAnswers = new ArrayList<>();
        ArrayList<Result> resultLocks = new ArrayList<>();
        ArrayList<Boolean> answerLocks = new ArrayList<>();

        boolean isPracticeMode = request.getParameter("practiceModeSwitch") != null;
        session.setAttribute("isPracticeMode", isPracticeMode);
        if (isPracticeMode) {
            Map<Integer, Integer> correctAnsCountMap = new HashMap<>();
            session.setAttribute("correctAnsCountMap", correctAnsCountMap);
        }

        try {
            quizName = quizDbManager.getQuizById(quizId).getQuizName();
            questions = quizDbManager.getQuizQuestions(quizId);
            isMultiplePageQuiz = quizDbManager.getQuizById(quizId).isMultiPage();
            shouldRandomize = quizDbManager.getQuizById(quizId).isRandom();
            isImmediateCorrection = quizDbManager.getQuizById(quizId).isImmediateCorrection();

            if (shouldRandomize) Collections.shuffle(questions);
            for (Question currQuestion : questions) {
                answerLocks.add(false);
                resultLocks.add(null);
                questionTypes type = currQuestion.getQuestionType();
                List<String> option = quizDbManager.getOptions(currQuestion.getQuestionId());
                List<Answer> answer = quizDbManager.getAnswers(currQuestion.getQuestionId());
                if (type == MATCHING || type == MULTIPLE_CHOICE || type == MULTI_CHOICE_MULTI_ANS)
                    Collections.shuffle(option);
                options.add(option);
                realAnswers.add(answer);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        session.setAttribute("result_locks", resultLocks);
        session.setAttribute("answer_locks", answerLocks);
        session.setAttribute("immediateCorrection", isImmediateCorrection);
        session.setAttribute("quizName", quizName);
        //session.setAttribute("queue", 0);
        session.setAttribute("questions", questions);
        session.setAttribute("quizId", quizId);
        session.setAttribute("posAnswers", options);
        session.setAttribute("realAnswers", realAnswers);
        session.setAttribute("numQuestions", questions.size());
        session.setAttribute("startTime", System.currentTimeMillis());

        Long endTime = System.currentTimeMillis();
        Long startTime = (Long)session.getAttribute("startTime");
        Integer elapsedTimeSeconds = (Integer)(int)((endTime - startTime) / 1000);
        session.setAttribute("elapsedTime", elapsedTimeSeconds);

        if (isMultiplePageQuiz || isPracticeMode) {
            HashMap<Integer, ArrayList<String>> answers = new HashMap<>(); // fill this by answers to check in the end
            questionTypes type = questions.get(0).getQuestionType();
            session.setAttribute("answers", answers);
            assert (type != null);
            response.sendRedirect("/quiz/multiPageQuiz.jsp?queue_param=" + 0);
        } else {
            response.sendRedirect("/quiz/singlePageQuiz.jsp");
        }
    }
}
