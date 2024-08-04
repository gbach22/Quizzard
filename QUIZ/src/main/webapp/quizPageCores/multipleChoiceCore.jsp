<%@ page import="quiz_web.Models.Question" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="quiz_web.Models.Result" %><%--
  Created by IntelliJ IDEA.
  User: k
  Date: 15.06.24
  Time: 1:15 AM
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

    List<String> curOptions = ((List<List<String>>)session.getAttribute("posAnswers")).get(queue);
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
        <h2 class="question_text"><%= questionText %> <br><span class="point_span"><%=score%> pts</span></h2>
        <div class="inner_body">
            <div class="radio_label">
                <%
                for (int i = 0; i < curOptions.size(); i++) {
                    String option = curOptions.get(i);
                %>
                    <input type="radio" name="question<%=queue%>selectedOption" value="<%= option %>" id="multiChoiceOption<%= i %>" <%=disabledStr%>/>
                    <label class="radio_label_label" for="multiChoiceOption<%= i %>"><%= option %></label><br>
                <%}%>
            </div>
        </div>
        <input type="hidden" name="hiddenqueue" value="<%=queue%>">
    </body>
</html>
