<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="quiz_web.Database.QuizHistoryDbManager" %>
<%@ page import="quiz_web.Database.DbConnection" %>
<%@ page import="quiz_web.Database.QuizDbManager" %>
<%@ page import="quiz_web.Models.*" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="static quiz_web.Models.questionTypes.MATCHING" %>
<%@ page import="static quiz_web.Models.questionTypes.MULTI_CHOICE_MULTI_ANS" %>
<%@ page import="static quiz_web.Models.questionTypes.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %><%--
  Created by IntelliJ IDEA.
  User: almasxitinalifa
  Date: 13.06.24
  Time: 21:12
  To change this template use File | Settings | File Templates.
--%>

<%
    List<Question> questions = (List<Question>)session.getAttribute("create_questions");
    Quiz quiz = (Quiz)session.getAttribute("create_quiz");
    List<List<Answer>> answers = (List<List<Answer>>)session.getAttribute("create_answers");
    List<List<String>> wrongAnswers = (List<List<String>>)session.getAttribute("create_wrong_answers");
    if (questions == null) questions = new ArrayList<Question>();
    if (answers == null) answers = new ArrayList<List<Answer>>();
    if (wrongAnswers == null) wrongAnswers = new ArrayList<List<String>>();
    String username = ((User)session.getAttribute("curUser")).getUserName();

    DbConnection db = (DbConnection) request.getServletContext().getAttribute("database_connection");
    QuizDbManager quizDbManager = new QuizDbManager(db.getConnection(), false);

    List<Quiz> quizzesByUser = new ArrayList<Quiz>();

    try {
        quizzesByUser = quizDbManager.getQuizzesByUser(username, -1);
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    int numCreatedQuiz = quizzesByUser.size() + 1;

%>

<html>
<head>
    <title>Your Quiz</title>
    <link rel="stylesheet" type="text/css" href="../style/finishCreatingQuizStyle.css">
</head>
<body>
<h1 class="quizName"><%=quiz.getQuizName()%></h1>
<h2 class="quizName"><%=quiz.getQuizDescription()%></h2>
<% if (quiz.getPictureUrl() != null && !quiz.getPictureUrl().isEmpty()) {
        String picUrl = quiz.getPictureUrl(); %>
        <div class="pic-container">
            <img src="<%= picUrl %>" alt="Quiz Picture" class="pic">
        </div>
<%
    }
%>

<li><a href="../createQuiz/quizInfo.jsp">Edit Quiz Info</a></li>

<%--<form action="<%= request.getContextPath() %>/editQuizInfo" method="post">--%>
<%--    <input type="submit" value="Edit Quiz Info"></form>--%>

<div class="mainbody">
    <br>
    <%
        for (int i = 0; i < questions.size(); i++) {
            Question curQuestion = questions.get(i);
            String questionText = curQuestion.getQuestionText();
            String[] clues = null;
            if (curQuestion.getQuestionType() == MATCHING) {
                if (!questionText.isEmpty()) {
                    questionText = questionText.trim();
                    clues = questionText.split(" # ");
                }

                questionText = "You Matched These Pairs";
            }
    %>
    <h2>Question <%=i + 1%>: <%=questionText%></h2>
    <div class="innerContent">
        <%
            String answersStr = " ";
            List<Answer> curAnswers = new ArrayList<Answer>();
            if (i < answers.size()) curAnswers = answers.get(i);
            for (int j = 0; j < curAnswers.size(); j++) {
                if (curQuestion.getQuestionType() == MATCHING) {
                    answersStr += clues[j] + " : " + curAnswers.get(j).getAnswer() + "; ";
                } else {
                    answersStr += curAnswers.get(j).getAnswer() + "; ";
                }
            }
        %>

        <%
            String wrongAnswerStr = " ";
            List<String> curWrongAnswers = new ArrayList<String>();
            if (wrongAnswers != null && i < wrongAnswers.size()) curWrongAnswers = wrongAnswers.get(i);
            for (int j = 0; j < curWrongAnswers.size(); j++) {
                wrongAnswerStr += curWrongAnswers.get(j) + "; ";
            }
        %>
    </div>
    <p>Answer(s): <%=answersStr%></p>
    <%
        if (curQuestion.getQuestionType() == MULTI_CHOICE_MULTI_ANS
                || curQuestion.getQuestionType() == MULTIPLE_CHOICE) {
    %> <p>Wrong Answer(s): <%=wrongAnswerStr%></p> <%
    }
%>
    <li><a href="../createQuiz/addQuestion.jsp?type=<%=curQuestion.getQuestionType().getValue()%>&ind=<%=i%>">Edit</a></li>

    <hr class="question-separator">

    <%
        }
    %>

    <br><br>
    <form action="<%= request.getContextPath() %>/submitQuiz" method="post">
        <%if(numCreatedQuiz == 1){%>
            <input type="hidden" id="achievement" name="achievement" value="0">
        <%}else if(numCreatedQuiz == 5){%>
            <input type="hidden" id="achievement" name="achievement" value="1">
        <%}else if(numCreatedQuiz == 10) {%>
            <input type="hidden" id="achievement" name="achievement" value="2">
        <%}%>
        <input type="submit" value="Submit Quiz">
    </form>

    <div class="bottom-buttons">
        <a href="../createQuiz/selectQuestionType.jsp?ind=<%=questions.size()%>" class="bottom-link"> Add Question</a>
        <a href="../homePage/homePage.jsp" class="bottom-link"> Delete Quiz</a>
    </div>

</div>
</body>
</html>
