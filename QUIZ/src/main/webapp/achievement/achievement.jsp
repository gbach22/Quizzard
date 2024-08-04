<%@ page import="java.util.ArrayList" %>
<%@ page import="quiz_web.CONSTANTS" %>
<%@ page import="quiz_web.Models.User" %><%--
  Created by IntelliJ IDEA.
  User: giorgi
  Date: 7/5/24
  Time: 8:09 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    int type1 = Integer.parseInt(request.getParameter("type1")), type2 = -1;

    if(request.getParameter("type2") != null) {
        type2 = Integer.parseInt(request.getParameter("type2"));
    }

    ArrayList<String> achievements = new ArrayList<String>();
    ArrayList<String> achievementDescriptions = new ArrayList<String>();

    achievements.add(CONSTANTS.ACHIEVEMENTS[type1]);
    if(type2 != -1) {
        achievements.add(CONSTANTS.ACHIEVEMENTS[type2]);
    }

    achievementDescriptions.add(CONSTANTS.ACHIEVEMENT_DESCRIPTIONS[type1]);
    if(type2 != -1) {
        achievementDescriptions.add(CONSTANTS.ACHIEVEMENT_DESCRIPTIONS[type2]);
    }

    String username = ((User)session.getAttribute("curUser")).getUserName();
%>
<html>
<head>
    <title>Achievement!</title>
    <link rel="stylesheet" type="text/css" href="../style/achievementPageStyle.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>
</head>
<body>
    <div class="container">
        <div class="home-link">
            <a href="../homePage/homePage.jsp">
                <img src="../start/quizzard.png" alt="Quizzard" height="100" width="200">
            </a>
        </div>
        <table class="table">
            <thead>
                <th></th>
                <th>Achievement</th>
                <th>Description</th>
            </thead>
            <% if(achievements.size() == 1) { %>
                <h1>Congratulations <%=username%>, You have a new Achievement!</h1>
                <tr>
                    <td><i class="fas fa-eye"></i></td>
                    <td><%=achievements.get(0)%></td>
                    <td><%=achievementDescriptions.get(0)%></td>
                </tr>
            <% } else { %>
                <h1>Congratulations <%=username%>, You have new Achievements!</h1>
                <%for(int i = 0; i < achievements.size(); i++) { %>
                    <tr>
                        <td><i class="fas fa-eye"></i></td>
                        <td><%=achievements.get(i)%></td>
                        <td><%=achievementDescriptions.get(i)%></td>
                    </tr>
                <%}%>
            <%}%>
        </table>
    </div>
</body>
</html>
