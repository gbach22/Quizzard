<%@ page import="quiz_web.Models.Question" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="quiz_web.Models.Result" %><%--
  Created by IntelliJ IDEA.
  User: k
  Date: 15.06.24
  Time: 1:08 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String queueString = request.getParameter("queue_param");
    int queue = (queueString != null) ? Integer.parseInt(queueString) : -1;

    String disabledStr = "";
    Boolean immediateCorrection = (Boolean)session.getAttribute("immediateCorrection");
    Boolean ansLocked = ((ArrayList<Boolean>) session.getAttribute("answer_locks")).get(queue);
    if (immediateCorrection && ansLocked)
        disabledStr = "disabled";

    Question curQuestion = ((List<Question>)session.getAttribute("questions")).get(queue);
    double score = curQuestion.getPoint();
    String questionText = curQuestion.getQuestionText().trim();
%>
<html>
    <head>
        <title>Title</title>
	    <link rel="stylesheet" href="../style/QuizPageStyle.css">
	</head>
    <body>
        <h2 class="question_text"> <%= questionText %> <br><span class="point_span"><%=score%> pts</span></h2>
        <div class="inner_body">
            <input class="input_field" type="input" placeholder="Type in your answer here" name="question<%=queue%>answer" <%=disabledStr%>>
            <input type="hidden" name="hiddenqueue" value="<%=queue%>">
        </div>
    </body>
</html>
