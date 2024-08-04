<%@ page import="quiz_web.Models.Answer" %>
<%@ page import="java.util.List" %>
<%@ page import="quiz_web.Models.Question" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="quiz_web.Models.questionTypes" %>
<%@ page import="static quiz_web.Models.questionTypes.MATCHING" %>
<%@ page import="static quiz_web.Models.questionTypes.FILL_IN_THE_BLANK" %>
<%@ page import="static quiz_web.Models.questionTypes.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    questionTypes type = questionTypes.getByValue(Integer.parseInt(request.getParameter("type")));
    int questionNum = Integer.parseInt(request.getParameter("ind"));

    List<List<Answer>> answers = (List<List<Answer>>) session.getAttribute("create_answers");
    List<Question> questions = (List<Question>) session.getAttribute("create_questions");
    List<List<String>> wrongAnswers = (List<List<String>>) session.getAttribute("create_wrong_answers");

    List<Answer> curAnswers = null;
    List<String> curWrongAnswer = null;
    Question curQuestion = null;
    String curQuestionText = "";
    String picUrl = "";

    if (questions != null && questionNum < questions.size()) {
        curQuestion = questions.get(questionNum);
        curQuestionText = curQuestion.getQuestionText();
        if (curQuestion.getPictureUrl() != null && !curQuestion.getPictureUrl().isEmpty()) {
            picUrl = curQuestion.getPictureUrl();
        }
    }

    if (answers != null && questionNum < answers.size()) {
        curAnswers = answers.get(questionNum);
    }

    if (wrongAnswers != null && questionNum < wrongAnswers.size()) {
        curWrongAnswer = wrongAnswers.get(questionNum);
    }

    String[] clues = null;
    if (type == MATCHING && !curQuestionText.isEmpty()) {
        curQuestionText = curQuestionText.trim();
        clues = curQuestionText.split(" # ");
    }
%>

<html>
<head>
    <title>Add Question</title>
    <link rel="stylesheet" type="text/css" href="../style/addQuestionStyle.css">
    <script>
        function restrictInput(event) {
            const invalidChars = ["#"];
            if (invalidChars.includes(event.key)) {
                event.preventDefault();
            }
        }
    </script>
</head>
<body>
<div class="mainbody">
    <form action="<%= request.getContextPath() %>/storeAnswer" method="post" class="inner_body">
        <input type="hidden" name="type" value="<%= type.getValue() %>">
        <input type="hidden" name="ind" value="<%= questionNum %>">

        <% if (type == FILL_IN_THE_BLANK) { %>
        <h3>Put Words That You Want To Be Filled In Brackets</h3>
        <h4>Example: The Longest River In The World Is River [Nile] [Nile River] [River Nile]</h4>
        <% } %>

        <% if (type != MATCHING) { %>
        <textarea id="question" maxlength="200" placeholder="Question" name="question"><%= curQuestionText %></textarea><br><br>

        <% } else { %>
        <h3>Enter The Pairs That You Want To Be Matched</h3>
        <div id="pairs">
            <div>
                <input type="text" maxlength="100" name="question" placeholder="Clue" onkeydown="restrictInput(event)">
                <input type="text" maxlength="100" name="answer" placeholder="Answer" onkeydown="restrictInput(event)">
            </div>
        </div>
        <% } %>

        <% if (type == PICTURE_RESPONSE) { %>
        <textarea maxlength="1000" d="picture" placeholder="Picture Url" name="picture"><%= picUrl %></textarea><br><br>
        <%
            } %>

        <% if (type == MULTI_ANSWER) { %>
        <h3>Remember, If The Order Matters, Enter The Answers In The Correct Order!</h3>
        <div class="checkbox-container">
            <input type="checkbox" name="order" value="Order Matters" id="order" <%= (curQuestion != null && curQuestion.isSortedRelevant())? "checked" : "" %>>
            <label for="order">Order Matters</label>
        </div>
        <% } %>

        <% if (type != MATCHING && type != FILL_IN_THE_BLANK) { %>
            <input type="text" maxlength="100" id="answer" placeholder="Answer" name="answer" <%= (type == MULTIPLE_CHOICE && curAnswers != null && !curAnswers.isEmpty()) ? "disabled" : "" %>><br><br>
        <% } %>

        <% if (type == MULTIPLE_CHOICE || type == MULTI_CHOICE_MULTI_ANS) { %>
            <input type="text" maxlength="100" id="wrongAnswer" placeholder="Wrong Answer" name="wrongAnswer"><br><br>
        <% } %>

        <div class="point_container">
            <label for="point" class="question_text">Point:</label><br>
            <input type="number" id="point" name="point" value="<%= curQuestion != null ? curQuestion.getPoint() : 1 %>" step="0.01" required><br><br>
        </div>

        <% if (type != FILL_IN_THE_BLANK) { %>
            <input type="submit" name="button" value="Add Answer">
        <% } %>
        <input type="submit" name="button" value="Submit Question">
        <input type="submit" name="button" value="Delete Question">

        <div class="list-container">
            <div class="list">
            <% if (type == MATCHING) { %>
            <h3>Clue<%= (clues != null && clues.length > 1) ? "s" : "" %>:</h3>
                <ul>
                    <% if (clues != null && curAnswers != null && clues.length == curAnswers.size()) {
                        for (int i = 0; i < clues.length; i++) {
                            String clue = clues[i];
                            Answer answer = curAnswers.get(i); %>
                    <li>
                        <%= clue %> : <%= answer.getAnswer() %>
                        <button type="submit" name="button" value="Delete Clue <%= i %>" class="delete-button">Delete</button>
                    </li>
                    <% }
                    } %>
                </ul>
            </div>
            <% } else if (type != FILL_IN_THE_BLANK) { %>
            <div class="list">
                <h3>Answer<%= (curAnswers != null && curAnswers.size() > 1) ? "s" : "" %>:</h3>
                <ul>
                    <% if (curAnswers != null) {
                        for (int i = 0; i < curAnswers.size(); i++) {
                            Answer answer = curAnswers.get(i); %>
                    <li>
                        <%= answer.getAnswer() %>
                        <button type="submit" name="button" value="Delete Answer <%= i %>" class="delete-button">Delete</button>
                    </li>
                    <% }
                    } %>
                </ul>
            </div>
            <% }
                if (type == MULTIPLE_CHOICE || type == MULTI_CHOICE_MULTI_ANS) { %>
            <div class="list">
                <h3>Wrong Answer<%= (curWrongAnswer != null && curWrongAnswer.size() > 1) ? "s" : "" %>:</h3>
                <ul>
                    <% if (curWrongAnswer != null) {
                        for (int i = 0; i < curWrongAnswer.size(); i++) {
                            String wrongAnswerStr = curWrongAnswer.get(i); %>
                    <li>
                        <%= wrongAnswerStr %>
                        <button type="submit" name="button" value="Delete Wrong Answer <%= i %>" class="delete-button">Delete</button>
                    </li>
                    <% }
                    } %>
                </ul>
            </div>
            <% } %>
        </div>
    </form>
</div>
</body>
</html>
