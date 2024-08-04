<%@ page import="quiz_web.Models.Question" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%--
  Created by IntelliJ IDEA.
  User: k
  Date: 15.06.24
  Time: 12:42 AM
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
    String[] parts = questionText.split("#");
    int counter = 0;
%>
<html>
    <head>
        <title>Title</title>
        <link rel="stylesheet" type="text/css" href="../style/QuizPageStyle.css">
    </head>
    <body>
        <h2 class="question_text">Fill in the Blank <br><span class="point_span"><%=score%> pts</span></h2>
        <div class="inner_body">
            <p class="fill_in_text">
                <%for(int i = 0; i < parts.length; i++) {
                    out.println(parts[i]);
                    if(i < parts.length - 1) { %>
                        <input class="fill_in_field" type="text" name="question<%=queue%>answer<%=counter%>" value="" placeholder="fill in" <%=disabledStr%>>
                        <%counter++;
                    }
                }%>

                <%if(questionText.charAt(questionText.length() - 1) == '#') {%>
                    <input class="fill_in_field" type="text" name="question<%=queue%>answer<%=counter%>" placeholder="fill in" <%=disabledStr%>>
                <%}%>
            </p>
            <input type="hidden" name="hiddenqueue" value="<%=queue%>">
        </div>
    </body>
</html>
