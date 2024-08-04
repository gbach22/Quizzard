<%@ page import="quiz_web.Models.Announcement" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="quiz_web.Database.AnnouncementDbManager" %>
<%@ page import="quiz_web.Database.DbConnection" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="quiz_web.Models.User" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.time.Instant" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.ParseException" %>
<%@ page import="quiz_web.Models.Quiz" %>
<%@ page import="quiz_web.Database.QuizDbManager" %>
<%@ page import="quiz_web.Models.Categories" %>
<%@ page import="quiz_web.Database.AdminStatisticDbManager" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    ArrayList<Quiz> quizzes = null;
    DbConnection dbConnection = (DbConnection)application.getAttribute("database_connection");
    QuizDbManager quizDbManager = new QuizDbManager(dbConnection.getConnection(), false);

    String fromDateStr = request.getParameter("fromDate");
    String toDateStr = request.getParameter("toDate");

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Timestamp fromDate = null;
    Timestamp toDate = null;
    boolean allTime = true;

    Instant instant = Instant.now();

    Timestamp now = Timestamp.from(instant);
    int usersNum = 0, quizzesNum = 0, quizzesTakeCount = 0, quizStatId = -1;
    HashMap<String, Timestamp> usersMap = new HashMap<String,Timestamp>();
    HashMap<Integer, Timestamp> quizzesMap = new HashMap<Integer,Timestamp>();
    AdminStatisticDbManager adminStatisticDbBase = new AdminStatisticDbManager(dbConnection.getConnection(), false);


    try {
        if (fromDateStr != null && !fromDateStr.isEmpty()) {
            Date fromDateParsed = dateFormat.parse(fromDateStr);
            fromDate = new Timestamp(fromDateParsed.getTime());
        }
        if (toDateStr != null && !toDateStr.isEmpty()) {
            Date toDateParsed = dateFormat.parse(toDateStr);
            toDate = new Timestamp(toDateParsed.getTime());
        }

        if (fromDate != null && toDate != null && toDate.after(fromDate) && now.after(toDate)) {
            allTime = false;
        }

        quizzesNum = adminStatisticDbBase.numberOfQuizzes(fromDate, toDate, allTime);
        usersNum = adminStatisticDbBase.numberOfUsers(fromDate, toDate, allTime);
        quizzesTakeCount = adminStatisticDbBase.quizzesTakeCount(fromDate, toDate, allTime);
        usersMap = adminStatisticDbBase.registeredUsers(fromDate, toDate, allTime);
        quizzesMap = adminStatisticDbBase.createdQuizzes(fromDate, toDate, allTime);
    } catch(ParseException e){
        throw new RuntimeException(e);
    }

    String category = request.getParameter("category");
    String name = request.getParameter("name");
    String tag = request.getParameter("tag");

    double averageQuizTakenByUser = ((double) quizzesTakeCount / usersNum);
    DecimalFormat df = new DecimalFormat("#.#");
    String formattedAverage = df.format(averageQuizTakenByUser);

    try {
        quizzes = quizDbManager.getQuizzesBy(category, name, tag, fromDate, toDate, allTime);
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
%>

<html>
<head>
    <title>All Quizzes</title>
    <link rel="stylesheet" type="text/css" href="../style/seeAllQuizzesStyle.css">
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
                <h2>Avarage Quiz Taken By User</h2>
                <p><%= formattedAverage %></p>
            </div>
            <div class="icon">
                <i class="fas fa-chart-line"></i>
            </div>
        </div>
    </div>
</div>
<div class="container">
    <form action="<%= request.getContextPath() %>/seeAllQuizzes" method="post">
        <label for="fromDate">From Date:</label>
        <input type="date" id="fromDate" name="fromDate" value="<%= (fromDate != null) ? fromDateStr : "" %>">

        <label for="toDate">To Date:</label>
        <input type="date" id="toDate" name="toDate" value="<%= (toDate != null) ? toDateStr : "" %>">

        <label for="category">Category:</label>
        <select name="category" id="category">
            <option value="All">ALL</option>
            <% for (Categories cur : Categories.values()) {
                String selected = cur.name().equalsIgnoreCase(category) ? "selected" : "";
                out.println("Processing category: " + cur.name() + " | Selected: " + selected);
            %>
            <option value="<%= cur.name() %>" <%= selected %>><%= cur.name() %></option>
            <% } %>
        </select>

        <label for="name">Quiz Name:</label>
        <input type="text" id="name" name="name" value="<%= (name != null) ? name : "" %>">

        <label for="tag">Quiz Tag:</label>
        <input type="text" id="tag" name="tag" value="<%= (tag != null) ? tag : "" %>">

        <input type="submit" value="Update Statistics" class="button">
    </form>
    <% if (quizzes != null) { %>
            <div class="quiz-table">
                <table class="table">
                    <thead>
                    <tr>
                        <th>Photo</th>
                        <th>Quiz Name</th>
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
                    <% for (Quiz quiz: quizzes) {%>
                        <tr class="user-row2" data-username="<%= quiz.getQuizId() %>">
                            <td><a href="../homePage/quiz.jsp?quizId=<%=quiz.getQuizId()%>"><img src="<%= quiz.getPictureUrl() %>" alt="Quiz Photo" width="60" height="50"></a></td>
                            <td><a href="../homePage/quiz.jsp?quizId=<%=quiz.getQuizId()%>"><%= quiz.getQuizName() %></a></td>
                            <td><a href="../profile/profile.jsp?profileUsername=<%=quiz.getCreatorUsername()%>"><%=quiz.getCreatorUsername()%></a></td>
                            <td><%= quiz.isMultiPage() %></td>
                            <td><%= quiz.isRandom() %></td>
                            <td><%= quiz.isImmediateCorrection() %></td>
                            <td><%= quiz.isPracticeMode() %></td>
                            <td><%= quiz.getViews() %></td>
                            <td><%= quiz.getTaken() %></td>
                            <td><%= quiz.getCreatedTime() %></td>
                        </tr>
                <% } %>
                </tbody>
                </table>
            </div> <%
        }
    %>
</div>
</body>
</html>

