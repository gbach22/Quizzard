<%@ page import="quiz_web.Models.Quiz" %>
<%@ page import="quiz_web.Models.User" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="quiz_web.Database.*" %>
<%@ page import="com.mysql.cj.interceptors.QueryInterceptor" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="quiz_web.Models.QuizHistory" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.time.Instant" %>
<%@ page import="java.text.ParseException" %>
<%@ page import="java.sql.Array" %>
<%@ page import="java.util.*" %>
<%@ page import="quiz_web.Models.Categories" %>
<%--
  Created by IntelliJ IDEA.
  User: giorgi
  Date: 6/10/24
  Time: 11:09 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String quizIdParam = request.getParameter("quizId");
    if (quizIdParam == null || quizIdParam.isEmpty()) {
        throw new RuntimeException("Quiz ID is missing");
    }

    int quizId = Integer.parseInt(quizIdParam);
    DbConnection dbConnection = (DbConnection) request.getServletContext().getAttribute("database_connection");
    QuizDbManager quizDbManager = new QuizDbManager(dbConnection.getConnection(), false);
    UserDbManager userDbManager = new UserDbManager(dbConnection.getConnection(), false);
    QuizHistoryDbManager quizHistoryDbManager = new QuizHistoryDbManager(dbConnection.getConnection(), false);
    RatingReviewDbManager ratingReviewDbManager = new RatingReviewDbManager(dbConnection.getConnection(), false);

    String orderBy = request.getParameter("orderBy");
    if (orderBy == null) orderBy = "score";

    String fromDateStr = request.getParameter("fromDate");
    String toDateStr = request.getParameter("toDate");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Timestamp fromDate = null;
    Timestamp toDate = null;
    boolean allTime = true;
    Instant instant = Instant.now();
    Timestamp now = Timestamp.from(instant);

    try {
        fromDate = parseDate(fromDateStr, dateFormat);
        toDate = parseDate(toDateStr, dateFormat);
        if (fromDate != null && toDate != null && toDate.after(fromDate) && now.after(toDate)) {
            allTime = false;
        }
    }catch(ParseException e){
        throw new RuntimeException(e);
    }

    User user = (User)session.getAttribute("curUser");
    DecimalFormat df = new DecimalFormat("#.#");
    User creator = null;
    Quiz quiz = null;
    String averageScore = null, rating = null;
    HashMap<String, String> reviews= null;
    ArrayList<QuizHistory> topPerformers = null, yourPerformances = null;
    ArrayList<Quiz> similarCategoryQuizzes = null, sameUserQuizzes = null;
    ArrayList<String> tags = null;
    int viewCount = 0, takeCount = 0;

    try {
        double averageRating = ratingReviewDbManager.getAverageRatingByQuizId(quizId);
        double averageScoreDouble = quizHistoryDbManager.getAverageScoreByQuizId(quizId);

        reviews = ratingReviewDbManager.getReviewsByQuizId(quizId);
        rating = Double.isNaN(averageRating)? "Not rated yet" : df.format(averageRating);
        averageScore = Double.isNaN(averageScoreDouble)? "0.0" : df.format(averageScoreDouble);

        quiz = quizDbManager.getQuizById(quizId);
        quizDbManager.incrementViews(quizId);
        similarCategoryQuizzes = quizDbManager.getQuizzesBy(quiz.getCategory().toString(), "", "", null, null, true);
        sameUserQuizzes = (ArrayList<Quiz>) quizDbManager.getQuizzesByUser(quiz.getCreatorUsername(), -1);

        tags = quizDbManager.getTags(quizId);
        viewCount = quizDbManager.viewCount(quizId);
        takeCount = quizDbManager.takeCount(quizId);

        if (user != null) {
            yourPerformances = quizHistoryDbManager.getQuizHistoryByUsernameAndQuizId(user.getUserName(), quizId, orderBy);
            if(orderBy.equals("time_needed")) Collections.reverse(yourPerformances);
        }

        creator = userDbManager.getUser(quiz.getCreatorUsername());
        topPerformers = quizHistoryDbManager.topPerformers(quizId, fromDate, toDate, allTime);
        session.setAttribute("quizName", quiz.getQuizName());
        session.setAttribute("quizPic", quiz.getPictureUrl());
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }

    String category = quiz.getCategory().name();
    ArrayList<String> usernames = new ArrayList<String>();
    ArrayList<String> scores = new ArrayList<String>();

    int maxNum = topPerformers.size();
    if(maxNum > 10) maxNum = 10;
    for (int i = 0; i < maxNum; i++) {
        if (user != null) usernames.add(topPerformers.get(i).getUsername());
        scores.add(Double.toString(topPerformers.get(i).getScore()));
    }

    StringBuilder usernamesJson = createJsonString(usernames);
    StringBuilder scoresJson = createJsonString(scores);

%>

<%!
    private StringBuilder createJsonString(ArrayList<String> list){
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            json.append("\"").append(list.get(i)).append("\"");
            if (i < list.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        return json;
    }

    private Timestamp parseDate(String dateStr, SimpleDateFormat dateFormat) throws ParseException {
        if (dateStr != null && !dateStr.isEmpty()) {
            Date dateParsed = dateFormat.parse(dateStr);
            return new Timestamp(dateParsed.getTime());
        }
        return null;
    }
%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quiz Page</title>
    <link rel="stylesheet" type="text/css" href="../style/quizSummaryPageStyle.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js@3.8.0/dist/chart.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>
</head>
<body>
    <div class="header">
        <div class="home-link">
            <a href="../homePage/homePage.jsp">
                <img src="../start/quizzard.png" alt="Quizzard" height="100" width="200">
            </a>
        </div>

        <div class="minStats">
            <div class="minStat">
                <div class="stat-content">
                    <h2>View Count:</h2>
                    <p><%= viewCount %></p>
                </div>
                <div class="icon">
                    <i class="fas fa-eye"></i>
                </div>
            </div>

            <div class="minStat">
                <div class="stat-content">
                    <h2>Take Count:</h2>
                    <p><%= takeCount %></p>
                </div>
                <div class="icon">
                    <i class="fas fa-pen"></i>
                </div>
            </div>

            <div class="minStat">
                <div class="stat-content">
                    <h2>Category:</h2>
                    <p><%= category %></p>
                </div>
                <div class="icon">
                    <i class="fas fa-folder"></i>
                </div>
            </div>

            <div class="minStat">
                <div class="stat-content">
                    <h2>Average Score:</h2>
                    <p><%= averageScore %></p>
                </div>
                <div class="icon">
                    <i class="fas fa-chart-line"></i> <!-- Example: Replace with appropriate icon class -->
                </div>
            </div>

            <div class="minStat">
                <div class="stat-content">
                    <h2>Rating:</h2>
                    <p><%= rating %></p>
                </div>
                <div class="icon">
                    <i class="fas fa-star"></i> <!-- Example: Replace with appropriate icon class -->
                </div>
            </div>
        </div>
    </div>



    <div class="main-container">
        <div class="leftSide">
            <div class="yourPerformance">
                <h1>Your Top 3 Performances:</h1>
                <div class="sortOptions">
                    <form id="sortForm" action="../homePage/quiz.jsp" method="GET">
                        <input type="hidden" name="quizId" value="<%=quizId%>">
                        <input type="hidden" id="selectedOption" name="orderBy">
                        <input type="button" class="sortButton" name="sortOption" value="Sort by Take Date" onclick="sortBoard('take_date')">
                        <input type="button" class="sortButton" name="sortOption" value="Sort by Score" onclick="sortBoard('score')">
                        <input type="button" class="sortButton" name="sortOption" value="Sort by Time Needed" onclick="sortBoard('time_needed')">
                    </form>
                </div>

                <table class="leaderboard">
                    <tr>
                        <th>Rank</th>
                        <th>Take Date</th>
                        <th>Time Needed</th>
                        <th>Score</th>
                    </tr>
                    <%
                        for (int i = 0; i < 3; i++) {
                            double score = 0;
                            int timeNeeded = 0;
                            Timestamp take_date = null;
                            String medalClass = "";
                            String medalIcon = "";
                            if (yourPerformances != null && i < yourPerformances.size()) {
                                score = yourPerformances.get(i).getScore();
                                timeNeeded = yourPerformances.get(i).getTimeNeeded();
                                take_date = yourPerformances.get(i).getTakeDate();
                            }
                            if (i == 0) {
                                medalClass = "gold";
                                medalIcon = "fa-medal";
                            } else if (i == 1) {
                                medalClass = "silver";
                                medalIcon = "fa-medal";
                            } else if (i == 2) {
                                medalClass = "bronze";
                                medalIcon = "fa-medal";
                            }
                    %>
                    <tr>
                        <td class="spaced-column <%= medalClass %>">
                            <i class="fas <%= medalIcon %> medal-icon"></i>
                        </td>
                        <td class="spaced-column"><%= take_date != null ? take_date.toString() : "Empty" %></td>
                        <td class="spaced-column"><%= timeNeeded != 0 ? timeNeeded : "Empty" %></td>
                        <td class="spaced-column"><%= score != 0 ? score : "Empty" %></td>
                    </tr>
                    <% } %>
                </table>
            </div>

            <div class="similarQuizzes">
                <h1>Quizzes you may be interested in:</h1>
                <%int quizCounter = 0;
                    Set<Integer> alreadyUsed = new HashSet<Integer>();
                %>
                <%for(int i = 0; i < similarCategoryQuizzes.size(); i++) {
                    Quiz curQuiz = similarCategoryQuizzes.get(i);
                    if(curQuiz.getQuizId() == quizId || quizCounter > 2) continue;
                    alreadyUsed.add(curQuiz.getQuizId());
                    quizCounter++;%>
                <div class="quizCard">
                    <a href="../homePage/quiz.jsp?quizId=<%= curQuiz.getQuizId() %>">
                        <img src="<%= curQuiz.getPictureUrl() %>" alt="Quiz Photo" width="200" height="100">
                    </a>
                    <div class="container">
                        <a href="../homePage/quiz.jsp?quizId=<%= curQuiz.getQuizId() %>">
                            <%= curQuiz.getQuizName() %>
                        </a>
                    </div>
                </div>
                <%}%>

                <%for(int i = 0; i < sameUserQuizzes.size(); i++) {
                    Quiz curQuiz = sameUserQuizzes.get(i);
                    if(curQuiz.getQuizId() == quizId || quizCounter > 2 || alreadyUsed.contains(curQuiz.getQuizId())) continue;
                    quizCounter++;%>
                <div class="quizCard">
                    <a href="../homePage/quiz.jsp?quizId=<%= curQuiz.getQuizId() %>">
                        <img src="<%= curQuiz.getPictureUrl() %>" alt="Quiz Photo" width="200" height="100">
                    </a>
                    <div class="container">
                        <a href="../homePage/quiz.jsp?quizId=<%= curQuiz.getQuizId() %>">
                            <%= curQuiz.getQuizName() %>
                        </a>
                    </div>
                </div>
                <%}%>

                <%if(quizCounter == 0) {%>
                    <h2>There are no Similar Quizzes :((</h2>
                <%}%>
            </div>
        </div>

        <div class="quiz-main-info">
            <h1><%= quiz.getQuizName() %></h1>
            <div class="quiz-photo">
                <img src="<%= quiz.getPictureUrl() %>" alt="Quiz Picture" width="350" height="350">
            </div>
            <% if (quiz.getQuizDescription() != null && !quiz.getQuizDescription().isEmpty()) { %>
            <div class="description">
                <h2>Quiz Description:</h2>
                <p><%= quiz.getQuizDescription() %></p>
            </div>
            <% } %>

            <% if (!tags.isEmpty()) {
                int tagCounter = 0;
            %>
            <div class="tags">
                <p>Tags:
                 <%for(int i = 0; i < tags.size(); i++) {
                    String tag = tags.get(i);
                    if(tagCounter == 3) continue;
                    tagCounter++;
                 %>
                    <a href="../homePage/allQuizzes.jsp?tag=<%=tag%>"><%= "#" + tag + "." %></a>
                <%}%>
                </p>
            </div>
            <% } %>

            <h3>Created by:</h3>
            <div class="creator-card">
                <a href="../profile/profile.jsp?profileUsername=<%= quiz.getCreatorUsername() %>">
                    <img src="<%= creator.getPictureURL() %>" alt="Quiz Photo" width="60" height="60">
                </a>
                <div class="container">
                    <a href="../profile/profile.jsp?profileUsername=<%= quiz.getCreatorUsername() %>">
                        <%= quiz.getCreatorUsername() %>
                    </a>
                </div>
            </div>

            <form action="<%= request.getContextPath() %>/startQuiz" method="post">
                <% if (quiz.isPracticeMode()) { %>
                <div class="switch-div">
                    <label class="switch">
                        <input type="checkbox" name="practiceModeSwitch">
                        <span class="slider round"></span>
                    </label>
                    <label class="switch-label"> Practice Mode </label>
                </div>
                <% } %>
                <br>
                <input type="hidden" name="quizId" value="<%=quizId%>">
                <% if (user != null) { %>
                <input class="btn" type="submit" value="Start Quiz">
                <% } %>
            </form>
            <% if (user != null && (user.isAdmin() || user.getUserName().equals(quiz.getCreatorUsername()))) { %>
            <form action="<%= request.getContextPath() %>/deleteQuiz" method="post">
                <input type="hidden" name="quizId" value="<%=quizId%>">
                <input class="btn" type="submit" value="Delete Quiz">
            </form>
            <% } %>
        </div>


        <div class="topPerformers">
            <h2>Top Performers:</h2>
            <div class="filter-container">
                <div class="filter">
                    <form action="<%= request.getContextPath() %>/topPerformers" method="post">
                    <div class="date-fields">
                        <div>
                            <label for="fromDate">From Date:</label>
                            <input type="date" id="fromDate" name="fromDate" value="<%= (fromDate != null) ? fromDateStr : "" %>">
                        </div>
                        <div>
                            <label for="toDate">To Date:</label>
                            <input type="date" id="toDate" name="toDate" value="<%= (toDate != null) ? toDateStr : "" %>">
                        </div>
                    </div>
                    <input type="hidden" name="quizId" id="quizId" value="<%=quizId%>">
                    <div class="submit-btn">
                        <input type="submit" value="Update Statistics">
                    </div>
                    </form>
                </div>
            </div>
            <div class="chart-container">
                <canvas id="performersChart"></canvas>
            </div>
        </div>

        <%if(!reviews.isEmpty()){%>
        <div class="reviews">
                <h2>Reviews:</h2>
            <%int reviewCounter = 0;%>
            <div class="review-cards">
                <%for(String username : reviews.keySet()) {
                    String review = reviews.get(username);
                    User reviewAuthor = creator;
                    if(reviewCounter == 5) continue;
                    reviewCounter++;
                    try {
                        if (user != null) reviewAuthor = userDbManager.getUser(username);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                %>
                    <%if (user != null) { %>
                        <div class="review-card">
                            <a href="../profile/profile.jsp?profileUsername=<%= reviewAuthor.getUserName() %>">
                                <img src="<%= reviewAuthor.getPictureURL() %>" alt="Quiz Photo" width="200" height="200">
                            </a>
                            <div class="review-container">
                                <a href="../profile/profile.jsp?profileUsername=<%= reviewAuthor.getUserName() %>"><%= reviewAuthor.getUserName() %></a>
                                <p><%=review%></p>
                            </div>
                        </div><%
                        }
                    %>
                <%}%>
            </div>
        </div>
        <%}%>
    </div>


</body>
    <script>
        const names = JSON.parse('<%= usernamesJson.toString() %>');
        const values = JSON.parse('<%= scoresJson.toString() %>');

        const ctx = document.getElementById('performersChart');

        const chart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: names,
                datasets: [{
                    label: '<%= quiz.getQuizName()%> \'s best Performers',
                    data: values,
                    backgroundColor: [
                        'rgba(75, 192, 192, 0.8)', // Bright Teal
                        'rgba(255, 99, 132, 0.8)', // Bright Pink
                        'rgba(255, 206, 86, 0.8)', // Bright Yellow
                        'rgba(54, 162, 235, 0.8)', // Bright Blue
                        'rgba(153, 102, 255, 0.8)', // Bright Purple
                        'rgba(255, 159, 64, 0.8)'  // Bright Orange
                    ],
                    borderColor: [
                        'rgba(75, 192, 192, 1)',
                        'rgba(255, 99, 132, 1)',
                        'rgba(255, 206, 86, 1)',
                        'rgba(54, 162, 235, 1)',
                        'rgba(153, 102, 255, 1)',
                        'rgba(255, 159, 64, 1)'
                    ],
                    borderWidth: 1
                }]
            },
            options: {
                onClick: (e, activeEls) => {
                    if (activeEls.length > 0) {
                        const index = activeEls[0].index;
                        const name = names[index];
                        window.location.href = `../profile/profile.jsp?profileUsername=${name}`;
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });

        function sortBoard(orderbyValue) {
            document.getElementById("selectedOption").value = orderbyValue;
            document.getElementById("sortForm").submit();
        }

    </script>
</script>
</html>