package quiz_web.Servlets;

import quiz_web.Models.Answer;
import quiz_web.Models.Question;
import quiz_web.Models.Quiz;
import quiz_web.Database.DbConnection;
import quiz_web.Database.QuizDbManager;
import quiz_web.Models.questionTypes;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import static quiz_web.Models.questionTypes.*;

@WebServlet("/submitQuiz")
public class submitQuizServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        Quiz quiz = (Quiz) session.getAttribute("create_quiz");
        List<Question> questions = (List<Question>)session.getAttribute("create_questions");
        List<List<Answer>> answers = (List<List<Answer>>)session.getAttribute("create_answers");
        List<List<String>> wrongAnswers = (List<List<String>>)session.getAttribute("create_wrong_answers");
        List<String> tags = (List<String>)session.getAttribute("create_tags");
        session.setAttribute("create_is_practice", null);
        Integer questionNumInteger = (Integer)session.getAttribute("question_num");
        int questionNum = (questionNumInteger != null) ? questionNumInteger : -1;

        DbConnection dbConnection = (DbConnection)getServletContext().getAttribute("database_connection");
        QuizDbManager quizDbManager = new QuizDbManager(dbConnection.getConnection(), false);

        try {
            int quiz_id = quizDbManager.addQuizInfo(quiz);
            quizDbManager.addTags(tags, quiz_id);
            System.out.println("QUIZ ID RETURNED " + quiz_id);

            for (int i = 0; i < questions.size(); i++) {
                Question curQuestion = questions.get(i);
                curQuestion.setQuizId(quiz_id);
                int question_id = quizDbManager.addQuestion(curQuestion);

                // get answers of this question
                List<Answer> curAnswers = answers.get(i);
                for (int j = 0; j < curAnswers.size(); j++) {
                    Answer curAnswer = curAnswers.get(j);
                    curAnswer.setQuestionId(question_id);
                    int answerNum = curAnswer.getAnswerNum();
                    if ((curQuestion.getQuestionType() == MULTI_ANSWER && curQuestion.isSortedRelevant())
                            || curQuestion.getQuestionType() == MATCHING) answerNum = j + 1;
                    curAnswer.setAnswerNum(answerNum);
                    quizDbManager.addAnswer(curAnswer);

                    if (curQuestion.getQuestionType() == MULTIPLE_CHOICE ||
                            curQuestion.getQuestionType() == MULTI_ANSWER ||
                            curQuestion.getQuestionType() == MULTI_CHOICE_MULTI_ANS ||
                            curQuestion.getQuestionType() == MATCHING)
                        quizDbManager.addOption(question_id, curAnswer.getAnswer());
                }

                if (curQuestion.getQuestionType() == MULTIPLE_CHOICE ||
                        curQuestion.getQuestionType() == MULTI_ANSWER ||
                        curQuestion.getQuestionType() == MULTI_CHOICE_MULTI_ANS ||
                        curQuestion.getQuestionType() == MATCHING) {
                    List<String> curOptions = wrongAnswers.get(i);
                    for (int j = 0; j < curOptions.size(); j++) {
                        String curOption = curOptions.get(j);
                        quizDbManager.addOption(question_id, curOption);
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String achievement = request.getParameter("achievement");
        System.out.println(achievement);
        if(achievement != null) {
            request.getRequestDispatcher("/addAchievement").forward(request, response);
        }else{
            response.sendRedirect("/homePage/homePage.jsp");
        }
    }
}
