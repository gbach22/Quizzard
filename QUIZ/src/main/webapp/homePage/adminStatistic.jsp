<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.ParseException" %>
<%@ page import="quiz_web.Models.Quiz" %>
<%@ page import="quiz_web.Models.User" %>
<%@ page import="java.time.Instant" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.time.Instant" %>
<%@ page import="quiz_web.Database.*" %>
<%@ page import="quiz_web.Models.QuizHistory" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="quiz_web.Models.Question" %>
<%--Created by IntelliJ IDEA.
  User: giorgi
  Date: 6/18/24
  Time: 4:51 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    DbConnection connection = (DbConnection) request.getServletContext().getAttribute("database_connection");
    AdminStatisticDbManager adminStatisticDbBase = new AdminStatisticDbManager(connection.getConnection(), false);
    QuizDbManager quizDbManager = new QuizDbManager(connection.getConnection(), false);
    UserDbManager userDbManager = new UserDbManager(connection.getConnection(), false);
    QuizHistoryDbManager quizHistoryDbManager = new QuizHistoryDbManager(connection.getConnection(), false);

    String fromDateStr = request.getParameter("fromDate");
    String toDateStr = request.getParameter("toDate");
    String infoMode = request.getParameter("infoMode");

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Timestamp fromDate = null;
    Timestamp toDate = null;
    boolean allTime = true;
    Instant instant = Instant.now();
    Timestamp now = Timestamp.from(instant);

    String userStat = request.getParameter("userStat");
    String quizStat = request.getParameter("quizStat");
    String quizStatName = null;

    try {
        fromDate = parseDate(fromDateStr, dateFormat);
        toDate = parseDate(toDateStr, dateFormat);
        if (fromDate != null && toDate != null && toDate.after(fromDate) && now.after(toDate)) {
            allTime = false;
        }
    }catch(ParseException e){
        throw new RuntimeException(e);
    }


    int usersNum = 0, quizzesNum = 0, quizzesTakeCount = 0, quizStatId = -1;
    HashMap<String, Timestamp> usersMap = new HashMap<String,Timestamp>();
    HashMap<Integer, Timestamp> quizzesMap = new HashMap<Integer,Timestamp>();
    ArrayList<QuizHistory> userStatHistory = null;
    ArrayList<QuizHistory> quizStatHistory = null;

    try {
        if(userStat != null) userStatHistory = quizHistoryDbManager.getQuizHistoryByUsername(userStat, -1);
        if(quizStat != null) {
            quizStatId = Integer.parseInt(quizStat);
            if (quizStatId != -1) {
                quizStatName = quizDbManager.getQuizById(quizStatId).getQuizName();
                quizStatHistory = quizHistoryDbManager.getQuizHistoryByQuizId(quizStatId);
            }
        }
        quizzesNum = adminStatisticDbBase.numberOfQuizzes(fromDate, toDate, allTime);
        usersNum = adminStatisticDbBase.numberOfUsers(fromDate, toDate, allTime);
        quizzesTakeCount = adminStatisticDbBase.quizzesTakeCount(fromDate, toDate, allTime);
        usersMap = adminStatisticDbBase.registeredUsers(fromDate, toDate, allTime);
        quizzesMap = adminStatisticDbBase.createdQuizzes(fromDate, toDate, allTime);
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }

    double averageQuizTakenByUser = ((double) quizzesTakeCount / usersNum);
    DecimalFormat df = new DecimalFormat("#.#");
    String formattedAverage = df.format(averageQuizTakenByUser);

    //for quiz stat
    ArrayList<Double> quizScores = new ArrayList<Double>();
    ArrayList<Timestamp> quizDates = new ArrayList<Timestamp>();
    if(quizStatHistory != null) {
        for(int i = 0; i < quizStatHistory.size(); i++) {
            quizScores.add(quizStatHistory.get(i).getScore());
            quizDates.add(quizStatHistory.get(i).getTakeDate());
        }
    }

    StringBuilder quizDatesJson = createJsonStringFromTimeStamps(quizDates);
    StringBuilder quizScoresJson = createJsonStringFromDoubles(quizScores);

    // for user stat
    ArrayList<Timestamp> userDates = new ArrayList<Timestamp>();
    ArrayList<Double> userScores = new ArrayList<Double>();
    ArrayList<Integer> quizIds = new ArrayList<Integer>();
    if(userStatHistory != null) {
        for (int i = 0; i < userStatHistory.size(); i++) {
            userDates.add(userStatHistory.get(i).getTakeDate());
            userScores.add(userStatHistory.get(i).getScore());
            quizIds.add(userStatHistory.get(i).getQuizId());
        }
    }
    ArrayList<Double> quizMaxscores = null;
    try {
        quizMaxscores = quizMaxScores(quizIds, connection);
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    ArrayList<Double> percentages = definePercentages(quizMaxscores, userScores);
    Collections.reverse(userDates);
    Collections.reverse(percentages);
    StringBuilder userDatesJson = createJsonStringFromTimeStamps(userDates);
    StringBuilder userNumbersJson = createJsonStringFromDoubles(percentages);
%>

<%!
    private ArrayList<Double> definePercentages(ArrayList<Double> quizMaxscores, ArrayList<Double> userScores) {
        ArrayList<Double> result = new ArrayList<Double>();
        for(int i = 0; i < quizMaxscores.size(); i++) {
            double maxScore = quizMaxscores.get(i);
            double score = userScores.get(i);
            double percent = (score * 100) / maxScore;
            result.add(percent);
        }
        return result;
    }

    private ArrayList<Double> quizMaxScores(ArrayList<Integer> quizIds, DbConnection dbConnection) throws SQLException {
        QuizDbManager quizDb = new QuizDbManager(dbConnection.getConnection(), false);
        ArrayList<Double> result = new ArrayList<Double>();
        for(int i = 0; i < quizIds.size(); i++) {
            List<Question> questions = quizDb.getQuizQuestions(quizIds.get(i));
            double counter = 0;
            for(int j = 0; j < questions.size(); j++) {
                counter += questions.get(j).getPoint();
            }
            result.add(counter);
        }

        return result;
    }

    private StringBuilder createJsonStringFromDoubles(ArrayList<Double> list) {
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

    private StringBuilder createJsonStringFromTimeStamps(ArrayList<Timestamp> list){
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
<html>
<head>
    <title>Admin Statistics</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>
    <link rel="stylesheet" type="text/css" href="../style/adminStatisticsStyle.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js@3.8.0/dist/chart.min.js"></script>
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
                    <h2>Total Amount of quizzes</h2>
                    <p><%= quizzesNum%></p>
                </div>
                <div class="icon">
                    <i class="fas fa-question"></i>
                </div>
            </div>
            <div class="minStat">
                <div class="stat-content">
                    <h2>Total Quiz_Take</h2>
                    <p><%= quizzesTakeCount %></p>
                </div>
                <div class="icon">
                    <i class="fas fa-edit"></i>
                </div>
            </div>
            <div class="minStat">
                <div class="stat-content">
                    <h2>Total Amount of Users</h2>
                    <p><%= usersNum %></p>
                </div>
                <div class="icon">
                    <i class="fas fa-users"></i>
                </div>
            </div>
            <div class="minStat">
                <div class="stat-content">
                    <h2>Avarage Quiz Taken By User</h2>
                    <p><%= formattedAverage %></p>
                </div>
                <div class="icon">
                    <i class="fas fa-chart-line"></i>
                </div>
            </div>
        </div>
    </div>



    <div class="filter-container">
        <div class="filter">
            <h4>Filter by (Quiz/User) creation date</h4>
            <form action="<%= request.getContextPath() %>/adminStatistics" method="post">
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
                <div class="submit-btn">
                    <input type="submit" value="Update Statistics">
                </div>
            </form>
        </div>
    </div>

    <div class="main-container">
        <div class="left">
            <h1>Statistic of Users</h1>
            <p>Click on the user row and see its statistics</p>
            <div class="users-table">
                <table class="table">
                    <thead>
                    <tr>
                        <th>Photo</th>
                        <th>Username</th>
                        <% if ("full".equals(request.getParameter("infoMode"))) { %>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <% } %>
                        <th>Registration Date</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% for (Map.Entry<String, Timestamp> entry : usersMap.entrySet()) {
                        String username = entry.getKey();
                        Timestamp registrationDate = entry.getValue();
                        User user = null;
                        try {
                            user = userDbManager.getUser(username);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    %>
                    <tr class="user-row1" data-username="<%= username %>" onclick="redirectToUserStat('<%= username %>')">
                        <td><a href="../profile/profile.jsp?profileUsername=<%=username%>"><img src="<%= user.getPictureURL() %>" alt="User Photo" width="60" height="50"></a></td>
                        <td><a href="../profile/profile.jsp?profileUsername=<%=username%>"><%= username %></a></td>
                        <% if ("full".equals(request.getParameter("infoMode"))) { %>
                        <td><%= user.getFirstName() %></td>
                        <td><%= user.getLastName() %></td>
                        <% } %>
                        <td><%= registrationDate %></td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>

            <div class="chart">
                <canvas id="userChart" width="400" height="400"></canvas>
            </div>
        </div>

        <div class="right">
            <h1>Statistics of Quizzes</h1>
            <p>Click on the quiz row and see its statistics</p>
            <div class="quiz-table">
                <table class="table">
                    <thead>
                    <tr>
                        <th>Photo</th>
                        <th>Quiz Name</th>
                        <th>Quiz Id</th>
                        <th>Creator Username</th>
                        <th>MultiPage</th>
                        <th>Random</th>
                        <th>Immediate Correction</th>
                        <th>Practice Mode</th>
                        <th>Views</th>
                        <th>Take</th>
                        <th>Creation Date</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% for (Map.Entry<Integer, Timestamp> entry : quizzesMap.entrySet()) {
                        int quizId = entry.getKey();
                        Timestamp creationDate = entry.getValue();
                        Quiz quiz = null;
                        try {
                            quiz = quizDbManager.getQuizById(quizId);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        %>
                    <tr class="user-row2" data-username="<%= quizId %>" onclick="redirectToQuizStat('<%= quizId %>')">
                        <td><a href="../homePage/quiz.jsp?quizId=<%=quizId%>"><img src="<%= quiz.getPictureUrl() %>" alt="Quiz Photo" width="60" height="50"></a></td>
                        <td><a href="../homePage/quiz.jsp?quizId=<%=quizId%>"><%= quiz.getQuizName() %></a></td>
                        <td><%= quizId %></td>
                        <td><a href="../profile/profile.jsp?profileUsername=<%=quiz.getCreatorUsername()%>"><%=quiz.getCreatorUsername()%></a></td>
                        <td><%= quiz.isMultiPage() %></td>
                        <td><%= quiz.isRandom() %></td>
                        <td><%= quiz.isImmediateCorrection() %></td>
                        <td><%= quiz.isPracticeMode() %></td>
                        <td><%= quiz.getViews() %></td>
                        <td><%= quiz.getTaken() %></td>
                        <td><%= creationDate %></td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>

            <div class="chart">
                <canvas id="quizChart" width="400" height="400"></canvas>
            </div>
        </div>
    </div>
</body>
<script>


    const quizDates = JSON.parse('<%=quizDatesJson%>');
    const quizScores = JSON.parse('<%=quizScoresJson%>');

    const maxIntervals = 5;
    const quizInterval = Math.ceil(quizDates.length / maxIntervals);


    const quizLabelsToDisplay = [];
    const quizDataToDisplay = [];

    for (let i = 0; i < quizDates.length; i += quizInterval) {
        quizLabelsToDisplay.push(quizDates[i]);
        quizDataToDisplay.push(quizScores[i]);
    }


    const ctx2 = document.getElementById('quizChart').getContext('2d');

    const chart2 = new Chart(ctx2, {
        type: 'line',
        data: {
            labels: quizLabelsToDisplay,
            datasets: [{
                label: 'Activity graph of <%=quizStatName%>',
                data: quizDataToDisplay,
                fill: true,
                borderColor: 'rgba(31, 40, 51, 1)',
                tension: 0.1
            }]
        },
        options: {
            scales: {
                x: {
                    ticks: {
                        color: 'rgba(31, 40, 51, 1)' // Color of x-axis labels
                    }
                },
                y: {
                    beginAtZero: true,
                    ticks: {
                        color: 'rgba(31, 40, 51, 1)' // Color of y-axis labels
                    }
                }
            },
            plugins: {
                legend: {
                    labels: {
                        color: 'rgba(31, 40, 51, 1)' // Color of legend labels
                    }
                }
            }
        }
    });

    const userDates = JSON.parse('<%=userDatesJson%>');
    const userScores = JSON.parse('<%=userNumbersJson%>');

    const userIntervalSize = Math.ceil(userDates.length / maxIntervals);

    // Select dates evenly across the intervals
    const userLabelsToDisplay = [];
    const userDataToDisplay = [];

    for (let i = 0; i < userDates.length; i += userIntervalSize) {
        userLabelsToDisplay.push(userDates[i]);
        userDataToDisplay.push(userScores[i]);
    }

    // Get canvas context
    const ctx1 = document.getElementById('userChart').getContext('2d');

    // Create Chart.js instance
    const chart1 = new Chart(ctx1, {
        type: 'line',
        data: {
            labels: userLabelsToDisplay,
            datasets: [{
                label: '<%=userStat%>\'s scores on quizzes',
                data: userDataToDisplay,
                fill: true,
                borderColor: 'rgba(31, 40, 51, 1)',
                tension: 0.1,
                pointBackgroundColor: 'rgba(31, 40, 51, 1)', // Color of data points
                pointBorderColor: 'rgba(31, 40, 51, 1)', // Border color of data points
            }]
        },
        options: {
            scales: {
                x: {
                    ticks: {
                        color: 'rgba(31, 40, 51, 1)' // Color of x-axis labels
                    }
                },
                y: {
                    beginAtZero: true,
                    ticks: {
                        color: 'rgba(31, 40, 51, 1)', // Color of y-axis labels
                        callback: function(value) {
                            return value + '%'; // Append '%' to y-axis labels
                        }
                    }
                }
            },
            plugins: {
                legend: {
                    labels: {
                        color: 'rgba(31, 40, 51, 1)' // Color of legend labels
                    }
                }
            }
        }
    });

    function redirectToUserStat(username) {
        window.location.href = `../homePage/adminStatistic.jsp?userStat=${username}&quizStat=<%=quizStatId%>`;
    }

    function redirectToQuizStat(quizId) {
        window.location.href = `../homePage/adminStatistic.jsp?quizStat=${quizId}&userStat=<%=userStat%>`;
    }

</script>
</html>