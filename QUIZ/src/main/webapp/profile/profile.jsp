<%@ page import="java.sql.SQLException" %>
<%@ page import="quiz_web.Database.*" %>
<%@ page import="quiz_web.Models.*" %>
<%@ page import="java.util.*" %>
<%@ page import="static quiz_web.CONSTANTS.ACHIEVEMENTS" %><%--
  Created by IntelliJ IDEA.
  User: giorgi
  Date: 6/14/24
  Time: 10:53 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    DbConnection dbConnection = (DbConnection)application.getAttribute("database_connection");
    UserDbManager userDbManager = new UserDbManager(dbConnection.getConnection(),false);
    QuizDbManager quizDbManager = new QuizDbManager(dbConnection.getConnection(), false);
    QuizHistoryDbManager quizHistory = new QuizHistoryDbManager(dbConnection.getConnection(), false);
    AnnouncementDbManager announcementDbManager = new AnnouncementDbManager(dbConnection.getConnection(), false);
    AchievementDbManager achievementDbManager = new AchievementDbManager(dbConnection.getConnection(), false);
    RelationshipDbManager relationshipDbManager = new RelationshipDbManager(dbConnection.getConnection(), false);
    User curUser = (User)session.getAttribute("curUser");
    User profileUser = null;
    ArrayList<Achievement> dupedAchievements = null;
    ArrayList<Achievement> achievements = new ArrayList<Achievement>();
    ArrayList<Announcement> announcements = null;
    List<Quiz> quizzes = null;
    ArrayList<String> friendsList = null;
    ArrayList<String> myFriendList = null;
    ArrayList<QuizHistory> myQuizHistory = null;
    Map<String, List<QuizHistory>> myQuizMap = new HashMap<String, List<QuizHistory>>();


    boolean sentMeRequest = false;
    boolean requestSent = false;
    try {


        profileUser = userDbManager.getUser((String)request.getParameter("profileUsername"));
        friendsList = userDbManager.getFriendsList(profileUser.getUserName());

        announcements = announcementDbManager.getAnnouncements(profileUser.getUserName());
        dupedAchievements = achievementDbManager.getAchievements(profileUser.getUserName(), -1);
        for (Achievement a : dupedAchievements) {
            if (!achievements.contains(a)) achievements.add(a);
        }


        quizzes = quizDbManager.getQuizzesByUser(profileUser.getUserName(), -1);

        if (curUser != null) {
            myFriendList = userDbManager.getFriendsList(curUser.getUserName());
            sentMeRequest = relationshipDbManager.sentRequest(curUser.getUserName(), profileUser.getUserName());
            myQuizHistory = quizHistory.getQuizHistoryByUsername(curUser.getUserName(), -1);
            requestSent = relationshipDbManager.sentRequest(profileUser.getUserName(), curUser.getUserName());

            for (QuizHistory qh : myQuizHistory) {
                if (myQuizMap.containsKey(qh.getQuizName())) {
                    myQuizMap.get(qh.getQuizName()).add(qh);
                } else {
                    List<QuizHistory> newAl = new ArrayList<QuizHistory>();
                    newAl.add(qh);
                    myQuizMap.put(qh.getQuizName(), newAl);
                }
            }
        }

//        myQuizHistory.sort(new Comparator<>() {
//            @Override
//            public int compare(QuizHistory o1, QuizHistory o2) {
//                return o1.getCreatedTime().compareTo(o2.getCreatedTime());
//            }
//        });
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    boolean ownPage = (curUser != null && curUser.getUserName().equals(profileUser.getUserName()));
    boolean isFriend = false;
    if (!ownPage && curUser != null) isFriend = myFriendList.contains(profileUser.getUserName());
    String friendButton = "";
    if (!ownPage)  {
        friendButton = "add_friend";
        if (isFriend) friendButton = "remove_friend";
        else if (requestSent) friendButton = "cancel_request";
        else if (sentMeRequest) friendButton = "accept_reject";
    }

    String challengeAction = "onclick=\"openTab('challenge')\"";
    if (ownPage) {
        challengeAction = "href=\"https://youtu.be/OKNQRbJeGNY?si=1r1Q5yZKuDJhqdcL\" target=\"_blank\"";
    }
%>
<html>
<head>
    <title><%=profileUser.getUserName()%>'s profile</title>
    <link rel="stylesheet" type="text/css" href="../style/userProfileStyle.css">
    <style>

        /* Style the tab content */
        .tabcontent {
            display: none;
            padding: 6px 12px;
            border: 0px solid #ccc;
            border-top: none;
        }

        /* Show the specific tab content */
        .show {
            display: block;
        }

        .profile-page .content__avatar {
            width: 12rem;
            height: 12rem;
            position: absolute;
            bottom: 0;
            left: 50%;
            z-index: 2;
            transform: translate(-50%, 50%);
            background: #8f6ed5 url("<%=profileUser.getPictureURL()%>") center center no-repeat;
            background-size: cover;
            border-radius: 50%;
            box-shadow: 0 15px 35px rgba(50,50,93,0.1), 0 5px 15px rgba(0,0,0,0.07);
        }


    </style>

    <script>
        function openTab(tabName) {
            var tab = document.getElementById(tabName);
            var displayStyle = tab.style.display;

            if (displayStyle === 'block') {
                tab.style.display = 'none';
                tab.style.visibility = 'hidden';
            } else {
                var tabContents = document.querySelectorAll('.tabcontent');
                tabContents.forEach(content => {
                    content.style.display = 'none';
                });
                tab.style.display = 'block';
                tab.style.visibility = 'visible';
                window.scrollTo({
                    top: document.body.scrollHeight,
                    behavior: "smooth"
                });
            }
        }



    </script>
</head>
<body>


<div class="profile-page">
    <div class="home-link" style="width: 200px">
        <a href="../homePage/homePage.jsp">
            <img src="../start/quizzard.png" alt="Quizzard" height="100" width="200">
        </a>
    </div>
    <div class="temp_div">


    <div class="content">
        <div class="content__cover">
            <div class="content__avatar"></div>
            <div class="content__bull"><span></span><span></span><span></span><span></span><span></span>
            </div>
        </div>

        <div class="content__actions">
            <a <%=challengeAction%>>
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 512">
                    <path fill="currentColor" d="M192 256A112 112 0 1 0 80 144a111.94 111.94 0 0 0 112 112zm76.8 32h-8.3a157.53 157.53 0 0 1-68.5 16c-24.6 0-47.6-6-68.5-16h-8.3A115.23 115.23 0 0 0 0 403.2V432a48 48 0 0 0 48 48h288a48 48 0 0 0 48-48v-28.8A115.23 115.23 0 0 0 268.8 288z"></path>
                    <path fill="currentColor" d="M480 256a96 96 0 1 0-96-96 96 96 0 0 0 96 96zm48 32h-3.8c-13.9 4.8-28.6 8-44.2 8s-30.3-3.2-44.2-8H432c-20.4 0-39.2 5.9-55.7 15.4 24.4 26.3 39.7 61.2 39.7 99.8v38.4c0 2.2-.5 4.3-.6 6.4H592a48 48 0 0 0 48-48 111.94 111.94 0 0 0-112-112z"></path>
                </svg><span>Challenge</span>
            </a>
            <a onclick="openTab('send_note')">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512">
                    <path fill="currentColor" d="M208 352c-41 0-79.1-9.3-111.3-25-21.8 12.7-52.1 25-88.7 25a7.83 7.83 0 0 1-7.3-4.8 8 8 0 0 1 1.5-8.7c.3-.3 22.4-24.3 35.8-54.5-23.9-26.1-38-57.7-38-92C0 103.6 93.1 32 208 32s208 71.6 208 160-93.1 160-208 160z"></path>
                    <path fill="currentColor" d="M576 320c0 34.3-14.1 66-38 92 13.4 30.3 35.5 54.2 35.8 54.5a8 8 0 0 1 1.5 8.7 7.88 7.88 0 0 1-7.3 4.8c-36.6 0-66.9-12.3-88.7-25-32.2 15.8-70.3 25-111.3 25-86.2 0-160.2-40.4-191.7-97.9A299.82 299.82 0 0 0 208 384c132.3 0 240-86.1 240-192a148.61 148.61 0 0 0-1.3-20.1C522.5 195.8 576 253.1 576 320z"></path>
                </svg><span>Message</span>
            </a>
        </div>

        <div class="content__title">
            <h1><%=profileUser.getFirstName()%> <%=profileUser.getLastName()%></h1><span><%=profileUser.getUserName()%></span>
        </div>


            <div class="content__description">
                <% if (profileUser.isAdmin()){%>
                    <p class="admin">Admin</p>
                <%}%>
                <% if (!profileUser.getBio().isEmpty()){%>
                    <p><%=profileUser.getBio()%></p>
                <%}%>
            </div>


        <ul class="content__list">
            <li><a onclick="openTab('friends')"><span><%=friendsList.size()%></span>Friends</a></li>
            <li><a onclick="openTab('achievements')"><span><%=achievements.size()%></span>Achievements</a></li>
            <% if (curUser != null && profileUser.isAdmin()) {
                String link = "../homePage/allAnnouncements.jsp?admin_user=" + profileUser.getUserName();
            %>
                <li><a href="<%=link%>"><span><%=announcements.size()%></span>Announcements</a></li>
            <% } %>
            <li><a onclick="openTab('quizzes')"> <span><%=quizzes.size()%></span>Quizzes</a></li>
        </ul>


            <% if (!ownPage && curUser != null){%>
            <div class="friendship_buttons">
            <form action="<%=request.getContextPath()%>/userInteraction" method="post">
                <input type="hidden" name="friendship" value="true">
                <input type="hidden" name="receiver_name" value="<%=profileUser.getUserName()%>">
                <input type="hidden" name="sender_name" value="<%=curUser.getUserName()%>">
                <div class="friend_button_div">
                    <%if(friendButton.equals("accept_reject")){%>
                        <button class="single_button" type="submit" name="friend_button" value="accept_request">Accept Request</button>
                        <button class="single_button" type="submit" name="friend_button" value="reject_request">Reject Request</button>
                    <%} else if (friendButton.equals("add_friend")) {%>
                        <button class="single_button" type="submit" name="friend_button" value="add_friend">Add Friend</button>
                    <% } else if (friendButton.equals("cancel_request")) { %>
                        <button class="single_button" type="submit" name="friend_button" value="cancel_request">Cancel Request</button>
                    <% } else {%>
                        <button class="single_button" type="submit" name="friend_button" value="remove_friend">Remove Friend</button>
                    <% } %>
                </div>
            </form>
            </div>
            <%}%>

        <div class="buttons">
    <% if (curUser != null && (ownPage || curUser.isAdmin() && !profileUser.isAdmin())){%>

        <form action="<%=request.getContextPath()%>/deleteUser" method="post">
            <input type="hidden" name="username" value="<%=profileUser.getUserName()%>">
            <input class="single_button" type="submit" value="Delete Account">
        </form>
    <%}%>

            <% if (curUser != null && ownPage){%>
                <a href="editProfile.jsp?responseMessageId=4"><button class="single_button" type="button">Edit Profile</button></a>
            <%}%>

    <% if (curUser != null && !ownPage && curUser.isAdmin() && !profileUser.isAdmin()){%>
        <form action="<%=request.getContextPath()%>/makeAdmin" method="post">
            <input type="hidden" name="username" value="<%=profileUser.getUserName()%>">
            <input class="single_button" type="submit" value="Make Admin">
        </form>
    <%}%>
        </div>





    </div>
<%--tabcontents started--%>
    <div id="challenge" class="tabcontent">
        <% if (!ownPage && isFriend && curUser != null){%>
        <form action="<%=request.getContextPath()%>/userInteraction" method="post">
            <input type="hidden" name="friendship" value="challenge">
            <input type="hidden" name="receiver_name" value="<%=profileUser.getUserName()%>">
            <input type="hidden" name="sender_name" value="<%=curUser.getUserName()%>">
            <h3>Choose from the Quizzes you have taken</h3>
            <%
                if (myQuizMap.keySet().isEmpty()) {
            %>
                    <h4>Take some quizzes yourself</h4>
            <% } %>

                <div id="taken_quizzes">
                    <%
                        for (String s : myQuizMap.keySet()) {
                            List<QuizHistory> qhList = myQuizMap.get(s);
                            int quizId = qhList.get(0).getQuizId();
                            String quizName = qhList.get(0).getQuizName();
                            String quizUrl = qhList.get(0).getQuizPic();
                            double myMaxScore = 0;
                            for (QuizHistory qh : qhList) {
                                myMaxScore = Math.max(myMaxScore, qh.getScore());
                            }
                            String quizNameParamString = "quiz_name" + quizId;
                            String maxScoreParamString = "max_score" + quizId;

                    %>
                    <input type="hidden" name="<%=quizNameParamString%>" value="<%=quizName%>">
                    <input type="hidden" name="<%=maxScoreParamString%>" value="<%=myMaxScore%>">
                    <button type="submit" name="challenge_button" value="<%=quizId%>">
                        <img src="<%=quizUrl%>" alt="Quiz Photo" width="180" height="120">
                        <p><%=quizName%></p>
                    </button>
                    <% } %>
                </div>
        </form>
        <% } else {%>
            <h3>Befriend them first!</h3>
        <% } %>

    </div>

    <div id="send_note" class="tabcontent">
        <% if (curUser != null && !ownPage && isFriend){%>
        <form action="<%=request.getContextPath()%>/userInteraction" method="post">
            <label>
                <textarea maxlength="300" name="message_content" class="all_text_area" placeholder="What's up loser..."></textarea>
            </label>
            <input type="hidden" name="receiver_name" value="<%=profileUser.getUserName()%>">
            <input type="hidden" name="sender_name" value="<%=curUser.getUserName()%>">
            <input type="hidden" name="friendship" value="false">
            <br>
            <button class="single_button" type="submit" value="send_note">Send Note</button>
        </form>
        <%} else if (ownPage) {%>
            <h3>You can't keep talking to yourself!</h3>
        <% } else {%>
            <h3>Befriend them first!</h3>
        <% } %>
    </div>

    <div id="quizzes" class="tabcontent">
        <h3>Quizzes</h3>
        <% if (quizzes.size() == 0) {%>
        <h4> No quizzes created :( </h4>


        <% } else {%>
        <div id="quiz_cards">
        <%for(int i = 0; i < quizzes.size() && i < 6; i++) {
            Quiz quiz = quizzes.get(i);%>
            <a href="../homePage/quiz.jsp?quizId=<%=quiz.getQuizId()%>">
                <img src="<%=quiz.getPictureUrl()%>" alt="Quiz Photo" width="180" height="120">
                <p><%=quiz.getQuizName()%></p>
            </a>
        <%}%>
        </div>
        <%}%>

    </div>

    <div id="achievements" class="tabcontent">
        <% if (achievements.size() == 0) {%>

        <h3 class="biggerh3">No Achievements Yet!</h3>
        <img src=https://cdn-icons-png.flaticon.com/512/1312/1312197.png
             alt="Empty" width="60" height="60">
        <h3 class="biggerh3">They must still be happy!</h3>
        <img src=https://cdn0.iconfinder.com/data/icons/business-motivation-9/512/hard-work-business-trying-512.png
             alt="Empty" width="200" height="200">

        <% } else {%>
        <h3 class="biggerh3">Achievements</h3>
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
        <%}%>
    </div>


    <div id="friends" class="tabcontent">
        <h3>Friends</h3>

        <% if (friendsList.isEmpty()) {%>
            <h4>No friends</h4>
        <% } else {%>
        <div id="friends_cards">
            <%for(int i = 0; i < friendsList.size(); i++) {
                String friend = friendsList.get(i);
                User u;
                try {
                    u = userDbManager.getUser(friend);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            %>
                <a href="../profile/profile.jsp?profileUsername=<%=friend%>">
                    <img src="<%=u.getPictureURL()%>" alt="Quiz Photo" width="120" height="120">
                    <p><%=friend%></p>
                </a>
            <%}%>
        </div>
        <%}%>

    </div>
</div>
</div>
</body>
</html>
