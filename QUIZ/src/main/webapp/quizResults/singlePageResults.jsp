<%@ page import="quiz_web.Models.Question" %>
<%@ page import="java.util.List" %>
<%@ page import="quiz_web.Models.Result" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="quiz_web.Models.Quiz" %>
<%@ page import="quiz_web.Models.User" %>
<%@ page import="quiz_web.Database.QuizHistoryDbManager" %>
<%@ page import="quiz_web.Database.DbConnection" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    DbConnection db = (DbConnection) request.getServletContext().getAttribute("database_connection");
    QuizHistoryDbManager quizHistoryDbManager = new QuizHistoryDbManager(db.getConnection(), false);

    List<Question> questions = (List<Question>) session.getAttribute("questions");
    HashMap<Integer, Result> resultsMap = (HashMap<Integer, Result>) session.getAttribute("resultsMap");
    String quizName = (String) session.getAttribute("quizName");
    Integer elapsedTime = (Integer) session.getAttribute("elapsedTime");

    DecimalFormat df = new DecimalFormat("##.##");
    double totalScore = (Double) session.getAttribute("totalScore");
    String totalScoreString = df.format(totalScore);

    double maxTotalScore = 0.0;
    for (Question q : questions) maxTotalScore += q.getPoint();
    Integer numCorrectAnswer = (Integer) session.getAttribute("numCorrectAns");
    int totalQuestions = questions.size();

    int quizId = (Integer) session.getAttribute("quizId");
    String username = ((User)session.getAttribute("curUser")).getUserName();

    int takeQuizCount = 0;
    double highestScore = 0;
    try {
        takeQuizCount = quizHistoryDbManager.tookQuizCount(username) + 1;
        highestScore = quizHistoryDbManager.highestScoreOnQuiz(quizId);
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
%>

<html>
<head>
    <title>Your Results</title>
    <link rel="stylesheet" type="text/css" href="../style/quizResultStyle.css">
</head>
    <body>
        <h1 class="quizName"><%=quizName%></h1>
        <div class="mainbody">
            <h2 class="headline">You earned total <%= totalScoreString %> points out of <%= maxTotalScore %> </h2>
            <br>
            <h3 class="elapsed">Elapsed time: <%=elapsedTime%>s</h3>
            <%
                for (int i = 0; i < questions.size(); i++) {
                    Result res = resultsMap.get(i);
                    res.isCorrect();
                    df = new DecimalFormat("##.##");
                    double Score = res.getScore();
                    String score = df.format(Score);
                    double maxScore = res.getMaxScore();
                    String dataUrl = "/tooltipServlet?param1=" + i;
            %>
                    <div class="innerContent">
                        <h3>For <span class="tooltip" data-url="<%= dataUrl %>">Question #<%= (i + 1) %></span>
                            your got <span class="point_span"><%=score%> pts out of <%=maxScore%></span></h3>
                        <p>Your answer: <%= res.userAnswerToString() %></p>
                        <p>Actual answer: <%= res.actualAnswerToString() %></p>
                    </div>
                    <hr class="question-separator">
            <% } %>
            <br><br>
            <div id="starRating">
                <form id="ratingForm" action="<%= request.getContextPath() %>/ratingAndReview" method="post">
                    <p class="rating-message">Rate this quiz:</p>
                    <div class="stars" data-rating="0">
                        <span class="star" data-value="1">&#9733;</span>
                        <span class="star" data-value="2">&#9733;</span>
                        <span class="star" data-value="3">&#9733;</span>
                        <span class="star" data-value="4">&#9733;</span>
                        <span class="star" data-value="5">&#9733;</span>
                    </div>
                    <input type="hidden" id="quizId" name="quizId" value="<%= quizId %>"> <!-- Assuming you have quizId available -->
                    <input type="hidden" id="rating" name="rating" value="0"> <!-- Will be updated dynamically -->
                    <input type="hidden" id="username" name="username" value="<%=username%>">

                    <div class="centered-container">
                        <button type="button" id="writeReviewBtn">Write Quiz Review</button>
                    </div>

                    <div id="reviewContainer" class="review-container">
                        <label for="quizReview">Leave a review:</label>
                        <textarea id="quizReview" name="quizReview" rows="4" cols="50" maxlength="100" oninput="updateCharCount()"></textarea>
                        <div id="charCount">100 characters remaining</div>
                    </div>

                    <%if(takeQuizCount == 10 && totalScore != highestScore){%>
                        <input type="hidden" id="achievement" name="achievement" value="3">
                    <%}else if(takeQuizCount != 10 && totalScore == highestScore){%>
                        <input type="hidden" id="achievement" name="achievement" value="4">
                    <%}else if(takeQuizCount == 10 && totalScore == highestScore) {%>
                        <input type="hidden" id="achievement" name="achievement" value="34">
                    <%}%>
                    <button type="submit" class="homeLink">Home</button>
                </form>
            </div>
        </div>

        <script>
            document.addEventListener('DOMContentLoaded', function() {
                const writeReviewBtn = document.getElementById('writeReviewBtn');
                const reviewContainer = document.getElementById('reviewContainer');

                writeReviewBtn.addEventListener('click', function() {
                    if (reviewContainer.style.display === 'none') {
                        reviewContainer.style.display = 'block';
                    } else {
                        reviewContainer.style.display = 'none';
                    }
                });
            });

            document.addEventListener('DOMContentLoaded', function() {
                const starsContainer = document.getElementById('starRating');
                const stars = starsContainer.querySelectorAll('.star');
                const ratingInput = document.getElementById('rating');

                stars.forEach(star => {
                    star.addEventListener('click', function() {
                        const value = parseInt(star.getAttribute('data-value'));
                        ratingInput.value = value;
                        stars.forEach(s => s.classList.remove('active'));
                        for (let i = 0; i < value; i++) {
                            stars[i].classList.add('active');
                        }
                    });
                });
            });

            document.addEventListener('DOMContentLoaded', function() {
                const tooltips = document.querySelectorAll('.tooltip');

                tooltips.forEach(tooltip => {
                    const tooltipContent = document.createElement('div');
                    tooltipContent.className = 'tooltip-content';
                    const iframe = document.createElement('iframe');
                    iframe.src = tooltip.getAttribute('data-url');
                    tooltipContent.appendChild(iframe);
                    tooltip.appendChild(tooltipContent);
                });
            });

            function updateCharCount() {
                const textarea = document.getElementById('quizReview');
                const charCount = document.getElementById('charCount');
                const remaining = textarea.maxLength - textarea.value.length;
                charCount.textContent = `${remaining} characters remaining`;
            }
        </script>
    </body>
</html>