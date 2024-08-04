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
import java.util.*;

import static quiz_web.Models.questionTypes.*;

@WebServlet("/nextQuestion")
@SuppressWarnings("unchecked")
public class nextQuestionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        int queue = Integer.parseInt(request.getParameterValues("hiddenqueue")[0]);
        //session.setAttribute("queue", queue);

        List<Question> questions = (List<Question>)session.getAttribute("questions");
        assert(questions != null);
        Question curQuestion = questions.get(queue);



        if (request.getParameter("submit_button").equals("prev_button")) {
            redirect(queue, questions, session, response, request, -1);
            return;
        }

        boolean isPracticeMode =(boolean)session.getAttribute("isPracticeMode");
        boolean isImmediateCorrection = (boolean)session.getAttribute("immediateCorrection");

        HashMap<Integer, ArrayList<Answer>> userAnswerMap = (HashMap<Integer, ArrayList<Answer>>)session.getAttribute("answers");
        List<List<Answer>> realAnswers = (List<List<Answer>>)session.getAttribute("realAnswers");
        ArrayList<Boolean> answerLocks = (ArrayList<Boolean>)session.getAttribute("answer_locks");
        ArrayList<Result> resultLocks = (ArrayList<Result>) session.getAttribute("result_locks");
        List<List<String>> options = (List<List<String>>)session.getAttribute("posAnswers");

        assert(userAnswerMap != null && realAnswers != null);

        if (isPracticeMode) {
            Map<Integer, Integer> correctAnsCountMap = (Map<Integer, Integer>)session.getAttribute("correctAnsCountMap");
            extractAnswer(request, queue, curQuestion, userAnswerMap, realAnswers);
            Result res = new Result(curQuestion, userAnswerMap.get(queue), (ArrayList<Answer>) realAnswers.get(queue), options.get(queue));
            if (res.isCorrect()) {
                int currCount = (correctAnsCountMap.get(queue) != null) ? correctAnsCountMap.get(queue) : 0;
                correctAnsCountMap.put(queue, currCount+1);
            }
            if (request.getParameter("submit_button").equals("remove_set")) {
                correctAnsCountMap.put(queue, 3);
            }
            redirect(queue, questions, session, response, request, 1);
            return;
        }

        if (isImmediateCorrection) {
            assert(answerLocks != null);
            if (request.getParameter("submit_button").equals("lock_in")) {
                extractAnswer(request, queue, curQuestion, userAnswerMap, realAnswers);
                Result res = new Result(curQuestion, userAnswerMap.get(queue), (ArrayList<Answer>) realAnswers.get(queue), options.get(queue));
                res.isCorrect();
                resultLocks.set(queue, res);
                answerLocks.set(queue, true);
                redirect(queue, questions, session, response, request, 0);
                return;
            }
        }

        if (!isImmediateCorrection || !answerLocks.get(queue)) {
            extractAnswer(request, queue, curQuestion, userAnswerMap, realAnswers);
            session.setAttribute("answers", userAnswerMap);
        }
        redirect(queue, questions, session, response, request, 1);
    }

    static void extractAnswer(HttpServletRequest request, int queue, Question curQuestion, HashMap<Integer, ArrayList<Answer>> userAnswerMap, List<List<Answer>> realAnswers) {
        ArrayList<Answer> answerList = new ArrayList<>();
        questionTypes type = curQuestion.getQuestionType();

        if (type == FILL_IN_THE_BLANK || type == MULTI_ANSWER || type == MATCHING) {
            int numAnswers = realAnswers.get(queue).size();
            for(int i = 0; i < numAnswers; i++) {
                String curAnswer = request.getParameter("question" + queue + "answer" + i);
                if(curAnswer == null) curAnswer = "";
                answerList.add(new Answer(curQuestion.getQuestionId(), curAnswer, i+1));
            }
        } else if (type == QUESTION_RESPONSE || type == PICTURE_RESPONSE) {
            String curAnswer = request.getParameter("question" + queue + "answer");
            if(curAnswer == null) curAnswer = "";
            answerList.add(new Answer(curQuestion.getQuestionId(), curAnswer, -1));
        } else if (type == MULTI_CHOICE_MULTI_ANS) {
            String[] selectedOptions = request.getParameterValues("question" + queue + "selectedOption");
            if(selectedOptions == null) selectedOptions = new String[0];
            Answer[] selectedAnswers = new Answer[selectedOptions.length];
            for (int j = 0; j < selectedOptions.length; j++) {
                selectedAnswers[j] = new Answer(curQuestion.getQuestionId(), selectedOptions[j], j+1);
            }
            answerList.addAll(Arrays.asList(selectedAnswers));
        }  else if (type == MULTIPLE_CHOICE) {
            String curAnswer = request.getParameter("question" + queue + "selectedOption");
            if(curAnswer == null) curAnswer = "";
            answerList.add(new Answer(curQuestion.getQuestionId(), curAnswer, -1));
        }

        userAnswerMap.put(queue, answerList);
    }

    private void redirect(int queue, List<Question> questions, HttpSession session, HttpServletResponse response, HttpServletRequest request, int next) throws IOException, ServletException {
        boolean isPracticeMode =(boolean)session.getAttribute("isPracticeMode");
        int nextQueue = queue + next;

        if(isPracticeMode) {
            Map<Integer, Integer> correctAnsCountMap = (Map<Integer, Integer>)session.getAttribute("correctAnsCountMap");
            int count = 0;
            if (nextQueue >= questions.size()) nextQueue = 0;
            while (count < questions.size() && correctAnsCountMap.get(nextQueue) != null && correctAnsCountMap.get(nextQueue) >= 3) {
                nextQueue += 1;
                count++;
                if (nextQueue >= questions.size()) nextQueue = 0;
            }
            if (count >= questions.size())
                nextQueue = 1000;
        }

        //session.setAttribute("queue", queue + next);

        Long endTime = System.currentTimeMillis();
        Long startTime = (Long)session.getAttribute("startTime");
        Integer elapsedTimeSeconds = (Integer)(int)((endTime - startTime) / 1000);
        session.setAttribute("elapsedTime", elapsedTimeSeconds);

        if (nextQueue < questions.size()) {
            response.sendRedirect("/quiz/multiPageQuiz.jsp?queue_param=" + (nextQueue));
        } else {
            if (isPracticeMode) {
                Integer quizId = (Integer) session.getAttribute("quiz_id_attr");
//                response.sendRedirect("/homePage/quiz.jsp?quizId=" + quizId);
                request.getRequestDispatcher("/addAchievement?achievement=5").forward(request, response);
                return;
            }
            HashMap<Integer, ArrayList<Answer>> answers = (HashMap<Integer, ArrayList<Answer>>)session.getAttribute("answers");
            List<List<Answer>> realAnswers = (List<List<Answer>>)session.getAttribute("realAnswers");
            List<List<String>> options = (List<List<String>>)session.getAttribute("posAnswers");
            DbConnection dbCon = (DbConnection)getServletContext().getAttribute("database_connection");
            resultPreparation(questions, session, answers, realAnswers, options, dbCon);
            response.sendRedirect("/quizResults/singlePageResults.jsp");
        }
    }

    static void resultPreparation(List<Question> questions, HttpSession session, HashMap<Integer, ArrayList<Answer>> answers, List<List<Answer>> realAnswers, List<List<String>> options, DbConnection dbConnection) {
        HashMap<Integer, Result> resultsMap = new HashMap<>();
        int numCorrectAnswers = 0;
        double score = 0;

        for (int i = 0; i < questions.size(); i++) {
            ArrayList<Answer> userAnswers = answers.get(i);
            List<Answer> realAnswer = realAnswers.get(i);
            List<String> option = options.get(i);
            Question currQuestion = questions.get(i);
            Result res = new Result(currQuestion, userAnswers, (ArrayList<Answer>) realAnswer, option);
            resultsMap.put(i, res);
            if (res.isCorrect()) {
                numCorrectAnswers++;
            }
            score += res.getScore();
        }

        Long endTime = System.currentTimeMillis();
        Long startTime = (Long)session.getAttribute("startTime");
        Integer elapsedTimeSeconds = (Integer)(int)((endTime - startTime) / 1000);
        session.setAttribute("elapsedTime", elapsedTimeSeconds);

        storeResultInDb(session, numCorrectAnswers, score, dbConnection, elapsedTimeSeconds);
        session.setAttribute("totalScore", score);
        session.setAttribute("numCorrectAns", numCorrectAnswers);
        session.setAttribute("resultsMap", resultsMap);

    }

    private static void storeResultInDb(HttpSession session, int numCorrectAnswers, double score, DbConnection dbConnection, int elapsedTime) {
        int quizId = (int) session.getAttribute("quizId");
        String quizName = (String)session.getAttribute("quizName");
        String quizPic = (String)session.getAttribute("quizPic");
        User curUser = (User)session.getAttribute("curUser");
        String username = curUser.getUserName();

        QuizHistoryDbManager historyDbManager = new QuizHistoryDbManager(dbConnection.getConnection(), false);
        QuizDbManager quizDbManager = new QuizDbManager(dbConnection.getConnection(), false);
        try {
            quizDbManager.incrementTaken(quizId);
            historyDbManager.addQuizHistory(new QuizHistory(username, quizName, quizPic, quizId, score, null, elapsedTime));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
