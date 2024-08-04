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
<%@ page import="quiz_web.Database.UserDbManager" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
//    User curUser = (User)session.getAttribute("curUser");
    ArrayList<Announcement> announcements = null;
    DbConnection dbConnection = (DbConnection)application.getAttribute("database_connection");
    AnnouncementDbManager announcementDbManager = new AnnouncementDbManager(dbConnection.getConnection(), false);
    UserDbManager userDbManager = new UserDbManager(dbConnection.getConnection(), false);


    String fromDateStr = request.getParameter("fromDate");
    String toDateStr = request.getParameter("toDate");
    String adminUser = request.getParameter("admin_user");

    if (adminUser == null) adminUser = "All";

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Timestamp fromDate = null;
    Timestamp toDate = null;
    boolean allTime = true;


    Instant instant = Instant.now();

    Timestamp now = Timestamp.from(instant);
    List<User> admins = null;
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
    }catch(ParseException e){
        throw new RuntimeException(e);
    }

    try {
        announcements = announcementDbManager.getAnnouncementsByTimeRange(fromDate, toDate, allTime, adminUser);
        admins = userDbManager.getAllUsers(true);
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
%>

<html>
<head>
    <title>Announcements</title>
    <link rel="stylesheet" type="text/css" href="../style/allAnouncementsStyle.css">
</head>
<body>
<div class="home-link" style="margin-bottom: 36px">
    <a href="../homePage/homePage.jsp">
        <img src="../start/quizzard.png" alt="Quizzard" height="100" width="200">
    </a>
</div>

<form action="<%= request.getContextPath() %>/seeAnnouncements" method="post">
    <label for="fromDate">From Date:</label>
    <input type="date" id="fromDate" name="fromDate" value="<%= (fromDate != null) ? fromDateStr : "" %>">

    <label for="toDate">To Date:</label>
    <input type="date" id="toDate" name="toDate" value="<%= (toDate != null) ? toDateStr : "" %>">

    <label for="admin">Select Admin:</label>
    <select name="admin_user" id="admin">
        <option value="All">ALL</option>
        <% for (User admin : admins) {
            String selected = admin.getUserName().equalsIgnoreCase(adminUser) ? "selected" : "";
        %>
        <option value="<%= admin.getUserName() %>" <%= selected%>><%= admin.getUserName() %></option>
        <% } %>
    </select>


    <input type="submit" value="Update Statistics">
</form>
<%
    if (announcements != null) {
        for (Announcement cur : announcements) {
            String creator = cur.getAnnouncemetOwner();
            Timestamp time = cur.getCreationDate();

%>
        <div class="single_anouncement_div">
            <h4> By <%= creator %> at <%= time.toString().substring(0, 16) %></h4>
            <h5> <%= cur.getAnnouncement() %></h5>
        </div>
<%
        }
    }
%>
</body>
</html>
