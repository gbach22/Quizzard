<%@ page import="quiz_web.Models.Question" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.lang.*" %>
<%@ page import="quiz_web.Models.Result" %>
<%@ page import="quiz_web.Models.questionTypes" %>
<%@ page import="static quiz_web.CONSTANTS.*" %>
<%@ page import="quiz_web.Models.Answer" %>
<%--
  Created by IntelliJ IDEA.
  User: giorgi
  Date: 6/10/24
  Time: 11:04 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String queueString = request.getParameter("queue_param");
    int queue = (queueString != null) ? Integer.parseInt(queueString) : -1;

    Integer elapsedTimeSeconds = (Integer)session.getAttribute("elapsedTime");
    int hours = elapsedTimeSeconds / 3600;
    int minutes = (elapsedTimeSeconds % 3600) / 60;
    int seconds = elapsedTimeSeconds % 60;
    String hoursString = hours < 10 ? "0" + hours : "" + hours;
    String minutesString = minutes < 10 ? "0" + minutes : "" + minutes;
    String secondsString = seconds < 10 ? "0" + seconds : "" + seconds;


    Integer numQuestions = (Integer)session.getAttribute("numQuestions");
    Boolean immediateCorrection = (Boolean)session.getAttribute("immediateCorrection");
    Boolean isPracticeMode = (Boolean)session.getAttribute("isPracticeMode");
    Integer quizId = (Integer) session.getAttribute("quiz_id_attr");
    String quizLink = "../homePage/quiz.jsp?quizId=" + quizId;

    List<Answer> answers = ((List<List<Answer>>)session.getAttribute("realAnswers")).get(queue);
    String answerStr = answers.toString();

    Question curQuestion = ((List<Question>)session.getAttribute("questions")).get(queue);
    questionTypes curType = curQuestion.getQuestionType();
    String title = QUESTION_TYPE_TITLES[curType.getValue()];
    String includePage = "../quizPageCores/" + QUESTION_JSPS[curType.getValue()];


    Result singleResult = null;
    String disabledStr = "";
    Boolean ansLocked = ((ArrayList<Boolean>) session.getAttribute("answer_locks")).get(queue);
    if (immediateCorrection && ansLocked) {
        singleResult = ((ArrayList<Result>) session.getAttribute("result_locks")).get(queue);
        disabledStr = "disabled";
    }
%>

<html>
    <head>
        <title><%=title%></title>
        <link rel="stylesheet" href="../style/MultiPageStyle.css">
    </head>
    <body>
        <% if (isPracticeMode) { %>
            <h3 class="question_num">Practice Mode</h3>
            <p class="practice_desc">
                Each question of the quiz will be looped indefinitely.
                You can manually remove a question if you feel confident.
                Or it will be removed automatically when you answer a
                question correctly 3 times. You will not know if you answered
                any questions correctly, but you can see the answer anytime.
                Practice mode will end once all questions are removed.
                Results will not be saved.
            </p>
            <br>
        <% } else { %>
            <h3 class="question_num">Question #<%=queue+1%> out of <%=numQuestions%></h3>
        <% } %>
        <div class="stopwatch">
            <div id="display"><%=hoursString%>:<%=minutesString%>:<%=secondsString%></div>
        </div>
        <form action="<%=request.getContextPath()%>/nextQuestion" method="post">
            <div class="mainbody">
                <jsp:include page="<%=includePage%>"></jsp:include>

                <%
                    if (immediateCorrection && ansLocked) {
                        out.println("<div class=\"correction_div\">");
                        singleResult.isCorrect();
                        double score = singleResult.getScore();
                        String message = "<h3>You received <span class=\"point_span\">" + score +" pts</span></h3>";
                        out.println(message);
                        out.println("<p>Your Answer: <span class=\"point_span\">" + singleResult.userAnswerToString() + "</span></p>");
                        out.println("<p>Actual Answer: <span class=\"point_span\">" + singleResult.actualAnswerToString() + "</span></p>");
                        out.println("</div>");
                    } else if (isPracticeMode) {
                        out.println("<div class=\"tooltip-container\">");
                        out.println("Hold here to see the answer");
                        out.println("<span class=\"tooltip-text\">" + answerStr + "</span>");
                        out.println("</div>");
                    }
                %>
            </div>
            <div class="buttons">
                <button id="nextButtonId" class="single_button" type="submit" value="next" name="submit_button">Next</button>
                <% if (queue != 0 && !isPracticeMode) { %>
                    <button class="single_button" type="submit" value="prev_button" name="submit_button">Previous</button>
                <% } %>

                <% if (isPracticeMode) { %>
                        <a href="<%=quizLink%>">
                            <button class="single_button" type="button" value="prev_button">Leave</button>
                        </a>
                    <button class="single_button" type="submit" value="remove_set" name="submit_button">Remove Question</button>
                <% } %>
                <% if (!isPracticeMode && immediateCorrection) { %>
                    <button class="single_button" type="submit" value="lock_in" name="submit_button" <%=disabledStr%>>Lock In</button>
                <% } %>

            </div>

        </form>

        <br>
        <br>

        <script>

            let startTime;
            let updatedTime;
            let difference;
            let tInterval;
            let running = false;
            const display = document.getElementById('display');

            function updateDisplay() {
                updatedTime = new Date().getTime();
                difference = updatedTime - startTime;

                let hours = Math.floor((difference % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
                let minutes = Math.floor((difference % (1000 * 60 * 60)) / (1000 * 60));
                let seconds = Math.floor((difference % (1000 * 60)) / 1000);

                hours = (hours < 10) ? "0" + hours : hours;
                minutes = (minutes < 10) ? "0" + minutes : minutes;
                seconds = (seconds < 10) ? "0" + seconds : seconds;

                display.textContent = hours + ':' + minutes + ':' + seconds;
            }



            function startStopwatch(hours = 0, minutes = 0, seconds = 0) {
                const initialTimeInSeconds = (hours * 3600) + (minutes * 60) + seconds;
                startTime = new Date().getTime() - (initialTimeInSeconds * 1000);
                tInterval = setInterval(updateDisplay, 1000);
                running = true;
            }
            window.addEventListener('DOMContentLoaded', (event) => {
                startStopwatch(<%=hours%>, <%=minutes%>, <%=seconds%>)
            });


            ddocument.addEventListener('DOMContentLoaded', () => {
                const tooltipContainers = document.querySelectorAll('.tooltip-container');

                tooltipContainers.forEach(container => {
                    container.addEventListener('click', () => {
                        // Toggle the active class on click
                        container.classList.toggle('active');
                    });

                    // Close the tooltip if clicking outside of it
                    document.addEventListener('click', (event) => {
                        if (!container.contains(event.target)) {
                            container.classList.remove('active');
                        }
                    });
                });
            });


            // document.getElementById('textInput').addEventListener('keydown', function(event) {
            //     if (event.key === 'Enter') {
            //         event.preventDefault();
            //         document.getElementById('nextButtonId').click();
            //     }
            // });

        </script>
    </body>
</html>