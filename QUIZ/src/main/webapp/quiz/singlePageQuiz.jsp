<%@ page import="quiz_web.Models.Question" %>
<%@ page import="quiz_web.Models.Answer" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="quiz_web.Models.questionTypes" %>
<%@ page import="static quiz_web.CONSTANTS.QUESTION_JSPS" %>
<%@ page import="quiz_web.Database.QuizDbManager" %>
<%@ page import="quiz_web.Database.DbConnection" %>
<%--
  Created by IntelliJ IDEA.
  User: k
  Date: 14.06.24
  Time: 10:27 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<Question> questions = (List<Question>) session.getAttribute("questions");
    String quizName = (String)session.getAttribute("quizName");
    Integer numQuestions = (Integer)session.getAttribute("numQuestions");
%>

<html>
<head>
    <title>Single-Page Quiz</title>
    <link rel="stylesheet" href="../style/QuizPageStyle.css">
    <link rel="stylesheet" href="../style/SinglePageStyle.css">
</head>
<body>
    <h1 class="question_text"><%=quizName%></h1>
    <form action="/singlePageFinished" method="post">
        <div class=mainbody>
        <%
            for (int i = 0; i < questions.size(); i++) {
                session.setAttribute("queue", i);
                Question currQuestion = questions.get(i);
                questionTypes type = currQuestion.getQuestionType();
                assert type != null;
                String includePage = "../quizPageCores/" + QUESTION_JSPS[type.getValue()] + "?queue_param="+i;
        %>
        <h3 class="question_num">Question #<%=i+1%> out of <%=numQuestions%></h3>
                <jsp:include page="<%= includePage %>"></jsp:include>
                <br><br>
        <%
                // Add a separator after each question except the last one
                if (i < questions.size() - 1) {
        %>
                <hr class="question-separator">
        <%
                }
            }
        %>
        </div>
        <div class="buttons">
            <button class="single_button" value="submit_all" type="submit">Submit All and Finish</button>
        </div>

    </form>
</body>
</html>
