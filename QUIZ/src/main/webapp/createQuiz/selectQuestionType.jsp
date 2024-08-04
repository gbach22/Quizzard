<%--
  Created by IntelliJ IDEA.
  User: almasxitinalifa
  Date: 13.06.24
  Time: 14:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    int questionNum = Integer.parseInt(request.getParameter("ind"));
%>
<html>
<head>
    <title>Select The Question Type</title>
    <link rel="stylesheet" type="text/css" href="../style/selectQuestionTypeStyle.css">
</head>
<body>
<div class="container">
    <ul class="link-column">
        <li><a href="../createQuiz/addQuestion.jsp?type=0&ind=<%=questionNum%>">Question-Response</a></li>
        <li><a href="../createQuiz/addQuestion.jsp?type=1&ind=<%=questionNum%>">Fill in the Blank</a></li>
        <li><a href="../createQuiz/addQuestion.jsp?type=2&ind=<%=questionNum%>">Multiple Choice</a></li>
        <li><a href="../createQuiz/addQuestion.jsp?type=3&ind=<%=questionNum%>">Picture-Response Questions</a></li>
        <li><a href="../createQuiz/addQuestion.jsp?type=4&ind=<%=questionNum%>">Multi-Answer Questions</a></li>
        <li><a href="../createQuiz/addQuestion.jsp?type=5&ind=<%=questionNum%>">Multiple Choice with Multiple Answers</a></li>
        <li><a href="../createQuiz/addQuestion.jsp?type=6&ind=<%=questionNum%>">Matching</a></li>
    </ul>
    <div class="footer">
        <a href="../createQuiz/finishCreating.jsp">Finish Creating Quiz</a>
    </div>
</div>
</body>
</html>
