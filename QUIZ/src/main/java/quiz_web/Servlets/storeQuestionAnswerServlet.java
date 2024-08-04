package quiz_web.Servlets;

import quiz_web.Models.Answer;
import quiz_web.Models.Question;
import quiz_web.Models.questionTypes;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static quiz_web.Models.questionTypes.*;


@WebServlet("/storeAnswer")
public class storeQuestionAnswerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        String question = request.getParameter("question");
        String answer = request.getParameter("answer");
        String wrongAnswer = request.getParameter("wrongAnswer");
        String pic_url = request.getParameter("picture");
        String buttonPressed = request.getParameter("button");
        double point = Double.parseDouble(request.getParameter("point"));
        int questionTypeNum = Integer.parseInt(request.getParameter("type"));
        questionTypes questionType = questionTypes.getByValue(questionTypeNum);
        int questionNum = Integer.parseInt(request.getParameter("ind"));
        String orderStr = request.getParameter("order");
        boolean order = (orderStr != null);


        if (buttonPressed == null || buttonPressed.equals("Add Answer")) {
            addQuestion(question, questionType, questionNum, session, pic_url, point, order);
            addWrongAnswers(wrongAnswer, questionNum, session);
            addAnswers(answer, questionType, questionNum, session, question);

            response.sendRedirect("/createQuiz/addQuestion.jsp?type=" + questionType.getValue() + "&ind=" + questionNum);
        } else if (buttonPressed.startsWith("Delete Answer")) {
            int index = getDeleteIndex(buttonPressed);
            deleteAnswer(index, session, questionNum);

            response.sendRedirect("/createQuiz/addQuestion.jsp?type=" + questionType.getValue() + "&ind=" + questionNum);
        } else if (buttonPressed.startsWith("Delete Wrong Answer")) {
            int index = getDeleteIndex(buttonPressed);
            deleteWrongAnswers(index, session, questionNum);

            response.sendRedirect("/createQuiz/addQuestion.jsp?type=" + questionType.getValue() + "&ind=" + questionNum);
        } else if (buttonPressed.startsWith("Delete Clue")) {
            int index = getDeleteIndex(buttonPressed);
            deleteClue(index, session, questionNum);

            response.sendRedirect("/createQuiz/addQuestion.jsp?type=" + questionType.getValue() + "&ind=" + questionNum);
        } else if (buttonPressed.startsWith("Delete Question")) {
            deleteQuestion(questionNum, session);

            response.sendRedirect("/createQuiz/selectQuestionType.jsp?ind=" + questionNum);
        } else {
            addQuestion(question, questionType, questionNum, session, pic_url, point, order);
            addWrongAnswers(wrongAnswer, questionNum, session);
            addAnswers(answer, questionType, questionNum, session, question);

            response.sendRedirect("/createQuiz/selectQuestionType.jsp?ind=" + (questionNum + 1));
        }

    }

    private void deleteQuestion(int questionNum, HttpSession session) {
        List<Question> curQuestions = (List<Question>)session.getAttribute("create_questions");
        List<List<Answer>> answers = (List<List<Answer>>)session.getAttribute("create_answers");
        List<List<String>> wrongAnswers = (List<List<String>>)session.getAttribute("create_wrong_answers");


        if (questionNum < curQuestions.size()) {
            curQuestions.remove(questionNum);
            session.setAttribute("create_questions", curQuestions);
        }

        if (questionNum < answers.size()) {
            answers.remove(questionNum);
            session.setAttribute("create_answers", answers);
        }

        if (questionNum < wrongAnswers.size()) {
            wrongAnswers.remove(questionNum);
            session.setAttribute("create_wrong_answers", wrongAnswers);
        }


    }

    private void deleteClue(int index, HttpSession session, int questionNum) {
        deleteAnswer(index, session, questionNum);

        List<Question> curQuestions = (List<Question>)session.getAttribute("create_questions");
        Question curQuestion = curQuestions.get(questionNum);
        String question = curQuestion.getQuestionText();
        //System.out.println("In the beginning: " + question);

        question = question.trim();
        String[] clues = question.split(" # ");

        List<String> clueList = new ArrayList<>(Arrays.asList(clues));
        //System.out.println(clueList.toString());
        clueList.remove(index);
        //System.out.println(clueList.toString());

        String newQuestion = "";

        for (int i = 0;  i < clueList.size(); i++) {
            if (newQuestion.isEmpty()) {
                newQuestion = clueList.get(i);
            } else {
                newQuestion = newQuestion + " # " + clueList.get(i);
            }

            //System.out.println(newQuestion);
        }

        curQuestion.setQuestionText(newQuestion);
        curQuestions.set(questionNum, curQuestion);
        session.setAttribute("create_questions", curQuestions);
    }



    private void deleteAnswer(int index, HttpSession session, int questionNum) {
        List<List<Answer>> answers = (List<List<Answer>>)session.getAttribute("create_answers");
        List<Answer> curAnswers = answers.get(questionNum);
        curAnswers.remove(index);
        answers.set(questionNum, curAnswers);
        session.setAttribute("create_answers", answers);
    }

    private void deleteWrongAnswers(int index, HttpSession session, int questionNum) {
        List<List<String>> wrongAnswers = (List<List<String>>)session.getAttribute("create_wrong_answers");
        List<String> curWrongAnswers = wrongAnswers.get(questionNum);
        curWrongAnswers.remove(index);
        wrongAnswers.set(questionNum,  curWrongAnswers);
        session.setAttribute("create_wrong_answers", wrongAnswers);
    }

    public static int getDeleteIndex(String buttonPressed) {
        int startInd = 0;
        for (int i = 0; i < buttonPressed.length(); i++) {
            if (Character.isDigit(buttonPressed.charAt(i))) break;
            startInd++;
        }

        return Integer.parseInt(buttonPressed.substring(startInd));
    }

    private void addAnswers(String answer, questionTypes questionType, int questionNum, HttpSession session, String question) {
        List<List<Answer>> answers = (List<List<Answer>>)session.getAttribute("create_answers");
        List<Answer> curAnswers = new ArrayList<>();

        if (question != null && !question.equals("") && questionType == FILL_IN_THE_BLANK) {
            if (questionNum < answers.size()) {
                curAnswers = extractStrings(question, questionNum);
                answers.set(questionNum, curAnswers);
            } else {
                curAnswers = extractStrings(question, questionNum);
                answers.add(curAnswers);
                session.setAttribute("create_answers", answers);
            }
        } else if (answer != null && !answer.equals("")) {
            Answer newAnswer = new Answer(questionNum, answer, -1);

            if (questionNum < answers.size()) {
                curAnswers = answers.get(questionNum);
                curAnswers.add(newAnswer);
                answers.set(questionNum, curAnswers);
            } else {
                curAnswers.add(newAnswer);
                answers.add(curAnswers);
            }

            session.setAttribute("create_answers", answers);
        }
    }

    private void addWrongAnswers(String wrongAnswer, int questionNum, HttpSession session) {
        List<List<String>> wrongAnswers = (List<List<String>>)session.getAttribute("create_wrong_answers");

        List<String> curWrongAnswers = new ArrayList<>();
        if (questionNum < wrongAnswers.size()) {
            curWrongAnswers = wrongAnswers.get(questionNum);
            if (wrongAnswer != null && !wrongAnswer.equals("")) {
                curWrongAnswers.add(wrongAnswer);
                wrongAnswers.set(questionNum, curWrongAnswers);
            }
        } else {
            if (wrongAnswer != null && !wrongAnswer.equals("")) curWrongAnswers.add(wrongAnswer);
            wrongAnswers.add(curWrongAnswers);
        }

        session.setAttribute("create_wrong_answers", wrongAnswers);
    }

    private void addQuestion(String question, questionTypes questionType, int questionNum, HttpSession session, String pic_url, double point, boolean orderMatters) {
        if (question != null && !question.equals("")) {
            List<Question> curQuestions = (List<Question>)session.getAttribute("create_questions");
            Question newQuestion = null;
            boolean sortedRelevant = (questionType == FILL_IN_THE_BLANK || questionType == MATCHING
                    || orderMatters);

            if (questionType == FILL_IN_THE_BLANK) question = replaceBrackets(question);

            if (questionNum < curQuestions.size()) {
                newQuestion = curQuestions.get(questionNum);

                if (questionType == MATCHING) {
                    if (newQuestion.getQuestionText().isEmpty()) {
                        newQuestion.setQuestionText(question);
                    } else {
                        newQuestion.setQuestionText(newQuestion.getQuestionText() + " # " + question);
                    }
                } else {
                    newQuestion.setQuestionText(question);
                }

                if (pic_url != null && !pic_url.equals("")) newQuestion.setPictureUrl(pic_url);
                newQuestion.setPoint(point);
                curQuestions.set(questionNum, newQuestion);
            } else {
                newQuestion = new Question(questionNum, question, questionType, pic_url, sortedRelevant, -1, point);
                curQuestions.add(newQuestion);
            }


            session.setAttribute("create_questions", curQuestions);
        }
    }

    private String replaceBrackets(String question) {
        while (question.contains("[") && question.contains("]")) {
            int start = question.indexOf('[');
            int end = question.indexOf(']');

            if (hasHashBefore(question, start)) {
                question = question.substring(0, start) + question.substring(end + 1);
            } else {
                question = question.substring(0, start) + "#" + question.substring(end + 1);
            }
        }

        return question;
    }


    private static List<Answer> extractStrings(String question, int questionNum) {
        List<Answer> answers = new ArrayList<>();

        while (question.contains("[") && question.contains("]")) {
            int start = question.indexOf('[');
            int end = question.indexOf(']');
            String word = question.substring(start + 1, end);


            if (hasHashBefore(question, start)) {
                question = question.substring(0, start) + question.substring(end + 1);
            } else {
                question = question.substring(0, start) + "#" + question.substring(end + 1);
            }

            int answerNum = 0;

            if (!answers.isEmpty()) answerNum = answers.get(answers.size() - 1).getAnswerNum();
            if (!hasHashBefore(question, start)) answerNum++;


            Answer newAnswer = new Answer(questionNum, word, answerNum);
            answers.add(newAnswer);
        }

        return answers;
    }

    private static boolean hasHashBefore(String question, int start) {

        while (start > 0 && question.charAt(start - 1) == ' ') {
            start--;
        }

        if (start == 0) return false;
        return (question.charAt(start - 1) == '#');
    }
}
