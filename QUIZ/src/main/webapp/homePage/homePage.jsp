<%@ page import="java.util.ArrayList" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.util.List" %>
<%@ page import="quiz_web.Database.*" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.util.Map" %>
<%@ page import="quiz_web.Models.*" %>
<%@ page import="static quiz_web.CONSTANTS.ACHIEVEMENTS" %>
<%@ page import="java.time.LocalDate" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User curUser = (User)session.getAttribute("curUser");
    DbConnection dbConnection = (DbConnection)application.getAttribute("database_connection");
    UserDbManager userDbManager = new UserDbManager(dbConnection.getConnection(),false);
    QuizDbManager quizDbManager = new QuizDbManager(dbConnection.getConnection(), false);
    AdminStatisticDbManager adminStatisticDbBase = new AdminStatisticDbManager(dbConnection.getConnection(), false);
    StatisticsDbManager statisticsDbManager = new StatisticsDbManager(dbConnection.getConnection(), false);
    AnnouncementDbManager announcementDbManager = new AnnouncementDbManager(dbConnection.getConnection(), false);
    QuizHistoryDbManager quizHistoryDbManager = new QuizHistoryDbManager(dbConnection.getConnection(), false);
    RelationshipDbManager relationshipDbManager = new RelationshipDbManager(dbConnection.getConnection(), false);
    AchievementDbManager achievementDbManager = new AchievementDbManager(dbConnection.getConnection(), false);

    int numQuizzes = 0;
    ArrayList<String> friendsList = null;
    List<Quiz> popularQuizzes = null;
    List<Quiz> recentQuizzes = null;
    ArrayList<Announcement> announcements = null;
    ArrayList<QuizHistory> quizesTaken = null;
    List<Map<String, String>> notes = null;
    List<Quiz> yourQuizzes = null;
    ArrayList<Achievement> achievements = null;
    List<Quiz> recentlyCreatedQuizzesByFriends = null;
    List<QuizHistory> recentlyTakenQuizzesByFriends = null;
    List<Achievement> friendsRecentAchievements = null;

    try {
        popularQuizzes = statisticsDbManager.mostViewedQuizzes(4);
        recentQuizzes = statisticsDbManager.recentQuizzes(4);

        if (curUser != null) {
            friendsList = userDbManager.getFriendsList(curUser.getUserName());
            quizesTaken = quizHistoryDbManager.getQuizHistoryByUsername(curUser.getUserName(), 4);
            notes = relationshipDbManager.getReceivedNotes(curUser.getUserName());
            yourQuizzes = quizDbManager.getQuizzesByUser(curUser.getUserName(), 4);
            achievements = achievementDbManager.getAchievements(curUser.getUserName(), 3);

            int displayAnnouncementsNum = 3;
            announcements = announcementDbManager.getUnseenAnnouncements(curUser.getUserName(), displayAnnouncementsNum);
            announcementDbManager.sawAnnouncements(curUser.getUserName(), displayAnnouncementsNum);

            ArrayList<String> friends = userDbManager.getFriendsList(curUser.getUserName());

            recentlyCreatedQuizzesByFriends = quizDbManager.recentQuizzesCreatedByFriends(friends, 3);
            recentlyTakenQuizzesByFriends = quizHistoryDbManager.getQuizzesRecentlyTakenByFriends(friends, 3);
            friendsRecentAchievements = achievementDbManager.getRecentlyEarnedAchievementsByFriends(friends, 3);
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
%>
<html>
<head>
    <title>Home Page</title>
    <link rel="stylesheet" type="text/css" href="../style/homePageStyle.css">
</head>
<body>
<div class="header">
    <div class="home-link">
        <a href="../homePage/homePage.jsp">
            <img src="../start/quizzard.png" alt="Quizzard" height="100" width="200">
        </a>
    </div>

    <div class="buttons">
        <div class="logins">
            <% if (curUser == null) { %>
            <a href="../login/login.jsp" class="header-button">Login</a>
            <a href="../create/createAccount.jsp" class="header-button">Create Account</a>
            <% } %>
        </div>

        <% if (curUser != null) { %>
        <div class="head">
            <a href="../profile/profile.jsp?profileUsername=<%=curUser.getUserName()%>" class="header-buttonButton">My Profile</a>
            <form action="<%= request.getContextPath() %>/startCreatingQuiz" method="post">
                <input type="submit" value="Create New Quiz" class="header-buttonButton">
            </form>
            <form action="<%= request.getContextPath() %>/randomQuiz" method="post">
                <button type="submit" name="Random Quiz" value="Random Quiz" class="header-buttonButton">Random Quiz</button>
            </form>
        </div>
        <% } %>

        <div class="seeAll">
            <a href="../homePage/allAnnouncements.jsp" class="header-buttonButton">See All Announcements</a>
            <a href="../homePage/allQuizzes.jsp" class="header-buttonButton">See All Quizzes</a>
            <% if (curUser != null && curUser.isAdmin()) {%>
            <a href="../homePage/adminStatistic.jsp" class="header-buttonButton">Admin Statistics</a>
            <%}%>
        </div>

        <div class="logout">
            <% if (curUser != null) { %>
            <form action="<%= request.getContextPath() %>/Login" method="post">
                <input type="submit" name="button" class="logout-button" value="Log Out">
            </form>
            <% } %>
        </div>
    </div>
</div>

<div class="upper-container">
    <% if (curUser != null && curUser.isAdmin()) { %>
    <form action="<%= request.getContextPath() %>/addAnnouncement" method="post">
        <div class="makeAnnouncement">
            <img src=https://cdn-icons-png.flaticon.com/512/7653/7653930.png
                 alt="Empty" width="50" height="50">
            <textarea placeholder="Add new announcement" name="announcement" required></textarea>
            <input type="hidden" name="username" value="<%=curUser.getUserName()%>">
            <input type="submit" value="Add Announcement" class="button">
        </div>
    </form>
    <% } %>
</div>
<div class="main">
    <div class="left-container">
        <div class="statistics">
            <% if (popularQuizzes != null && !popularQuizzes.isEmpty()) { %>
            <h3 class="section-heading">Most Popular Quizzes:</h3>
            <div class="mostPopularQuizzes">
                <% for (int i = 0; i < popularQuizzes.size(); i++) {
                    Quiz quiz = popularQuizzes.get(i); %>
                <div class="quizCard">
                    <div class="pic-container">
                        <a href="../homePage/quiz.jsp?quizId=<%= quiz.getQuizId() %>">
                            <img src="<%= quiz.getPictureUrl() %>" alt="Quiz Photo" class="quiz-pic">
                        </a>
                    </div>
                    <div class="friend-card-container">
                        <h4><a href="../homePage/quiz.jsp?quizId=<%= quiz.getQuizId()%>">
                            <%= quiz.getQuizName() %>
                        </a></h4>
                    </div>
                </div>
                <% } %>
            </div>
            <% } %>
        </div>


        <div class="statistics">
            <% if (recentQuizzes != null && !recentQuizzes.isEmpty()) { %>
            <h3 class="section-heading">Recently Created Quizzes:</h3>
            <div class="mostPopularQuizzes">
                <% for (int i = 0; i < recentQuizzes.size(); i++) {
                    Quiz quiz = recentQuizzes.get(i); %>
                <div class="quizCard">
                    <div class="pic-container">
                        <a href="../homePage/quiz.jsp?quizId=<%= quiz.getQuizId() %>">
                            <img src="<%= quiz.getPictureUrl() %>" alt="Quiz Photo" class="quiz-pic">
                        </a>
                    </div>
                    <div class="friend-card-container">
                        <h4><a href="../homePage/quiz.jsp?quizId=<%= quiz.getQuizId()%>">
                            <%= quiz.getQuizName() %>
                        </a></h4>
                    </div>
                </div>
                <% } %>
            </div>
            <% } %>
        </div>

        <% if (curUser != null) { %>
        <div class="friends">
            <h2 class="section-heading">Friends:</h2>
            <% if (friendsList == null || friendsList.isEmpty()) { %>
                <img src="https://media2.giphy.com/media/13z8Ax00NhQREI/giphy.gif?cid=6c09b9529vbsg2jxzzz67xd0g4re4yin1wyxzayd4m5jjo69&ep=v1_internal_gif_by_id&rid=giphy.gif&ct=g" alt="Friend Photo" width="500px" height="300px">
            <% } else { %>
                <img src=https://cdni.iconscout.com/illustration/premium/thumb/group-of-friends-happy-on-winning-competition-2948505-2509262.png?f=webp
                     alt="Empty" width="460" height=200">
                <div class="friends-container">
                    <% for (int i = 0; i < friendsList.size(); i++) {
                        User friend = null;
                        try {
                            friend = userDbManager.getUser(friendsList.get(i));
                        } catch (SQLException e) {
                            out.println("<p>Error retrieving friend: " + friendsList.get(i) + "</p>");
                        }

                        if (friend != null) { %>
                    <div class="friend-card">
                        <div class="pic-container">
                            <a href="../profile/profile.jsp?profileUsername=<%= friend.getUserName() %>">
                                <img src="<%= friend.getPictureURL() %>" alt="Friend Photo" class="friend-photo">
                            </a>
                        </div>
                        <div class="friend-card-container">
                            <h4><a href="../profile/profile.jsp?profileUsername=<%= friend.getUserName() %>">
                                <%= friend.getUserName() %>
                            </a></h4>
                        </div>
                    </div>
                    <%  }
                    } %>
                </div>
            <% } %>
        </div>
        <% } %>


        <% if (curUser != null) { %>
            <div class="achievements">
                <% if (achievements != null && !achievements.isEmpty()) { %>
                    <h3 class="biggerh3">Your Achievements:</h3>
                    <img src=https://cdn-icons-png.flaticon.com/512/1312/1312197.png
                         alt="Empty" width="60" height="60">
                    <ul>
                        <% for (int i = 0; i < achievements.size(); i++) {
                            Achievement achievement = achievements.get(i);
                            String achievementType = ACHIEVEMENTS[achievement.getAchievementType()];
                        %>
                        <li>
                            <h4 class="biggerh4"><%=achievementType%></h4>
                        </li>
                        <% } %>
                    </ul>
                <% }
                    else {
                    %>
                    <h3 class="biggerh3">You Have No Achievements Yet!</h3>
                    <img src=https://cdn-icons-png.flaticon.com/512/1312/1312197.png
                         alt="Empty" width="60" height="60">
                    <h3 class="biggerh3">Try Harder!</h3>
                    <img src=https://cdn0.iconfinder.com/data/icons/business-motivation-9/512/hard-work-business-trying-512.png
                    alt="Empty" width="200" height="200"> <%
                } %>
            </div>
        <% } %>


        <% if (curUser != null) { %>

            <div class="statistics">
                <% if (quizesTaken != null && !quizesTaken.isEmpty()) { %>
                    <h3 class="section-heading">Recently Taken Quizzes:</h3>
                    <div class="mostPopularQuizzes">
                        <% for (int i = 0; i < quizesTaken.size(); i++) {
                            QuizHistory quizH = quizesTaken.get(i); %>
                            <div class="quizCard">
                                <div class="pic-container">
                                    <a href="../homePage/quiz.jsp?quizId=<%= quizH.getQuizId() %>">
                                        <img src="<%= quizH.getQuizPic() %>" alt="Quiz Photo" class="quiz-pic">
                                    </a>
                                </div>
                                <div class="friend-card-container">
                                    <h4><a href="../homePage/quiz.jsp?quizId=<%= quizH.getQuizId()%>">
                                        <%= quizH.getQuizName() %>
                                    </a></h4>
                                </div>
                            </div>
                        <%} %>
                    </div>
                <% } else {  %>
                    <h3 class="section-heading">Recently Taken Quizzes:</h3>
                    <h3 class="biggerh3">You Have Not Taken Any Quizzes Yet</h3>
                    <img src="https://cdn-icons-png.flaticon.com/512/8587/8587101.png" alt="Friend Photo" width="200px" height="200px">
                    <form action="<%= request.getContextPath() %>/randomQuiz" method="post">
                        <button type="submit" name="Random Quiz" value="Random Quiz" class="button">Random Quiz</button>
                    </form>
                <% } %>
            </div>

            <div class="statistics">
                <% if (yourQuizzes!= null && !yourQuizzes.isEmpty()) { %>
                    <h3 class="section-heading">Quizzes Created By You:</h3>
                    <div class="mostPopularQuizzes">
                        <% for (int i = 0; i < yourQuizzes.size(); i++) {
                            Quiz quiz = yourQuizzes.get(i); %>
                        <div class="quizCard">
                            <div class="pic-container">
                                <a href="../homePage/quiz.jsp?quizId=<%= quiz.getQuizId() %>">
                                    <img src="<%= quiz.getPictureUrl() %>" alt="Quiz Photo" class="quiz-pic">
                                </a>
                            </div>
                            <div class="friend-card-container">
                                <h4><a href="../homePage/quiz.jsp?quizId=<%= quiz.getQuizId()%>">
                                    <%= quiz.getQuizName() %>
                                </a></h4>
                            </div>
                        </div>
                        <% } %>
                    </div>
                <% }
                    else { %>
                        <h3 class="section-heading">Quizzes Created By You:</h3>
                        <h3 class="biggerh3">You Have Not Created Any Quizzes Yet</h3>
                        <img src="https://cdn-icons-png.freepik.com/256/8586/8586891.png?semt=ais_hybrid" alt="Friend Photo" width="200px" height="200px">
                        <form action="<%= request.getContextPath() %>/startCreatingQuiz" method="post">
                            <input type="submit" value="Create New Quiz" class="button">
                        </form>
                <%
                    }
                %>
            </div>
        <% } %>
    </div>

    <% if (curUser != null) { %>
    <div class="right-container">
        <div class="notifications">
            <% if (notes == null || notes.isEmpty()) { %>
            <h3>You Have No Unread Notifications:</h3>
            <img src=https://cdn-icons-png.freepik.com/256/11329/11329073.png?semt=ais_hybrid alt="Empty" width="230" height="200">
            <% } else { %>
            <h3>You Have <%=notes.size()%> Unread Notification(s):</h3>
            <ul>
                <%
                    Integer index = (Integer)session.getAttribute("note_ind");
                    if (index == null || index < 0 || index >= notes.size()) {
                        index = 0;
                        session.setAttribute("note_ind", 0);
                    }

                    Map<String, String> note = notes.get(index);
                    String senderName = note.get("senderName");
                    String content = note.get("content");
                    String timestamp = note.get("timestamp");
                    timestamp = timestamp.substring(5, 16);
                    String type = note.get("contentType");
                    String link = "../profile/profile.jsp?profileUsername=" + senderName;
                    String notifDisplay = "";
                    if (type.equals("note")) {
                        notifDisplay = "<b>Note from <a href=\"" + link + "\">" + senderName + "</a></b>: <br>" + content;
                    } else {
                        notifDisplay = "<b><a href=\"" + link + "\">" + senderName + "</a></b> " + content;
                    }
                %>
                <li>
                    <div class="single_message">
                        <h4 class="no_padding"><%=index + 1%> / <%=notes.size()%></h4>
                        <p><%=timestamp%></p>
                        <p><%=notifDisplay%></p>
                    </div>
                    <form action="<%= request.getContextPath() %>/markAsRead" method="post" class="form-buttons-close">
                        <button type="submit" name="button" value="Mark As Read<%=note.get("noteId")%>" class="button">Mark As Read</button>
                        <div class="nextPrevButtons">
                            <% if (index - 1 >= 0) { %>
                                <button type="submit" name="button" value="prev" class="button">Prev</button>
                            <% } %>

                            <% if (index + 1 < notes.size()) { %>
                                <button type="submit" name="button" value="next" class="button">Next</button>
                            <% } %>
                        </div>
                    </form>

                </li>
            </ul>
            <%} %>
        </div>

        <div class="announcements">
            <% if (announcements == null ||announcements.isEmpty()) { %>
            <h3>No New Announcements:</h3>
            <img src=https://cdn-icons-png.freepik.com/512/13982/13982727.png alt="Empty" width="230" height="200">
            <% } else { %>
            <h3>New Announcements:</h3>
            <%} %>

            <ul>
                <% for (int i = 0; (announcements != null) && i < announcements.size(); i++) {
                    Announcement cur = announcements.get(i);
                    try {
                        User creator = userDbManager.getUser(cur.getAnnouncemetOwner());%>
                <li>
                    <h4>Announcement by <a href="../profile/profile.jsp?profileUsername=<%= creator.getUserName() %>"><%= creator.getUserName() %></a> on <%=cur.getCreationDate().toLocalDateTime().toLocalDate()%></h4>
                    <h5><%= cur.getAnnouncement() %></h5>
                </li> <%
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            %>
                <% } %>
            </ul>
        </div>

        <div class="searchUser">
            <h3>Search for a user</h3>
            <form action="<%= request.getContextPath() %>/searchUser" method="post">
                <div class="search-inputs">
                    <input type="text" id="searchUser" placeholder="Search User" name="searchUser" class="searchUser-field">
                    <input type="submit" value="Search" class="searchUser-button">
                </div>
            </form>

            <%
                String searchedUsername = (String) session.getAttribute("searchUser");
                if (searchedUsername != null) { %>
            <ul>
                <%
                    try {
                        ArrayList<User> searchedUsers = userDbManager.getUserByUsernamePart(searchedUsername, 5);
                        for (User searchedUser : searchedUsers) { %>
                <li>
                    <h4><a href="../profile/profile.jsp?profileUsername=<%= searchedUser.getUserName() %>"><%= searchedUser.getUserName() %></a></h4>
                </li>
                <% }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                %>
            </ul>
            <%
                    session.setAttribute("searchUser", null);
                } %>
        </div>


        <% if (recentlyCreatedQuizzesByFriends != null && !recentlyCreatedQuizzesByFriends.isEmpty()) { %>
        <div class="recentlyByFriends">
            <h3>Recently Created Quizzes By Friends:</h3>
            <ul> <%
                for (int i = 0; i < recentlyCreatedQuizzesByFriends.size(); i++) {
                    Quiz quiz = recentlyCreatedQuizzesByFriends.get(i);%>
                <li>
                    <h4><a href="../profile/profile.jsp?profileUsername=<%= quiz.getCreatorUsername() %>"><%= quiz.getCreatorUsername() %></a> created <a href="../homePage/quiz.jsp?quizId=<%= quiz.getQuizId()%>"><%= quiz.getQuizName() %></a></h4>
                </li><%
                    } %>
            </ul>
        </div><%
        } %>


        <% if (recentlyTakenQuizzesByFriends != null && !recentlyTakenQuizzesByFriends.isEmpty()) { %>
        <div class="recentlyByFriends">
            <h3>Recently Taken By Friends:</h3>
            <ul> <%
                for (int i = 0; i < recentlyTakenQuizzesByFriends.size(); i++) {
                    QuizHistory quizH = recentlyTakenQuizzesByFriends.get(i);%>
                <li>
                    <h4><a href="../profile/profile.jsp?profileUsername=<%= quizH.getUsername() %>"><%= quizH.getUsername() %></a> took <a href="../homePage/quiz.jsp?quizId=<%= quizH.getQuizId()%>"><%= quizH.getQuizName() %></a></h4>
                </li><%
                    } %>
            </ul>
        </div><%
        } %>


        <% if (friendsRecentAchievements != null && !friendsRecentAchievements.isEmpty()) { %>
        <div class="recentlyByFriends">
            <h3>Recently Earned Achievements By Friends:</h3>
            <img src=https://cdn-icons-png.flaticon.com/512/1312/1312197.png
                 alt="Empty" width="50" height="50">
            <ul>
                <% for (int i = 0;  i < friendsRecentAchievements.size(); i++) {
                    Achievement achievement = friendsRecentAchievements.get(i);
                    String achievementType = ACHIEVEMENTS[achievement.getAchievementType()];
                %>
                <li>
                    <h4><a href="../profile/profile.jsp?profileUsername=<%=achievement.getUsername()%>"><%=achievement.getUsername()%></a> received <%=achievementType%></h4>
                </li>
                <% } %>
            </ul>
        </div>
        <% } %>

    </div><%
    } %>
</div>
</body>
</html>