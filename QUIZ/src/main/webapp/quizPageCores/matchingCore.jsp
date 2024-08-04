<%@ page import="quiz_web.Models.Question" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %>
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

    List<String> curOptions = ((List<List<String>>)session.getAttribute("posAnswers")).get(queue);
    Question curQuestion = ((List<Question>)session.getAttribute("questions")).get(queue);
    double score = curQuestion.getPoint();
    String questionText = curQuestion.getQuestionText().trim();
    String[] parts = questionText.split("#");
    List<String> leftWords = Arrays.asList(parts);
%>

<html>
    <head>
        <title>Title</title>
        <link rel="stylesheet" href="../style/QuizPageStyle.css">
    </head>
    <body>
        <h2 class="question_text">Match the words <br><span class="point_span"><%=score%> pts</span></h2>
        <div class="inner_body">
            <table class="matching_table">
                <tr>
                    <th>Left Words</th>
                    <th>Right Words</th>
                </tr>
                <%
                for (int i = 0; i < leftWords.size(); i++) {
                    String leftWord = leftWords.get(i);
                    String funcCall = "disableOptions(" + i + ")";
                    int j = 0;
                %>
                    <tr>
                        <td><%= leftWord %></td>
                        <td>
                            <select class="select_option" onchange="<%=funcCall%>" name="question<%=queue%>answer<%= i %>" <%=disabledStr%>>
                                <option value="_">Select a match</option>
                                <% for (String rightWord : curOptions) { %>
                                        <option value="<%=rightWord%>" class="<%=j%>"><%= rightWord %></option>
                                <% j++; } %>
                            </select>
                        </td>
                    </tr>
                <% } %>
            </table>
        </div>
        <input type="hidden" name="hiddenqueue" value="<%=queue%>">

        <script>
            function disableOptions(num) {
                let selectArr = document.getElementsByClassName("select_option");
                let globalArr = [];
                for (let i = 0; i < selectArr.length; i++) {
                    let select = selectArr[i];
                    let selectedIdx = select.selectedIndex;
                    globalArr.push(selectedIdx);
                }

                let select = selectArr[num];
                let selectedIdx = select.selectedIndex; // 3 (before 2)
                for (let i = 0; i < selectArr.length; i++) {
                    if (i === num) continue;
                    let optionArr = selectArr[i].options;
                    for (let j = 0; j < optionArr.length; j++) {
                        if (optionArr[j].value !== "_" && j === selectedIdx)
                            optionArr[j].disabled = true;
                        else if (optionArr[j].value !== "_" && !globalArr.includes(j))
                            optionArr[j].disabled = false;
                    }
                }
            }
        </script>
    </body>
</html>

