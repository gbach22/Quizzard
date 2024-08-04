<%@ page import="quiz_web.Models.Question" %>
<%@ page import="quiz_web.Models.Answer" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%--
  Created by IntelliJ IDEA.
  User: k
  Date: 15.06.24
  Time: 1:14 AM
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

    int numTextFields = ((List<List<Answer>>)session.getAttribute("realAnswers")).get(queue).size();
    Question curQuestion = ((List<Question>)session.getAttribute("questions")).get(queue);
    double score = curQuestion.getPoint();
    String questionText = curQuestion.getQuestionText().trim();
    String orderMatters = "Remember, the order does not matter!";
    if (curQuestion.isSortedRelevant())
        orderMatters = "Remember, the order matters!";
%>

<html>
    <head>
        <title>Title</title>
        <link rel="stylesheet" href="../style/QuizPageStyle.css">
    </head>
    <body>
        <h2 class="question_text"> <%= questionText %> <br><span class="point_span"><%=score%> pts</span></h2>
        <div class="inner_body_multi_ans">
            <h3 class="radio_label"> <%= orderMatters %></h3>
            <% for (int i = 0; i < numTextFields; i++) { %>
                <input class="multi_answer_field" type="text" placeholder="answer <%=i+1%>" name="question<%=queue%>answer<%= i %>" <%=disabledStr%>>
            <% } %>
        </div>
        <input type="hidden" name="hiddenqueue" value="<%=queue%>">
    </body>
</html>
