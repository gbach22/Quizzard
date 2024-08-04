<%@ page import="static quiz_web.CONSTANTS.EDIT_RESPONSE_MESSAGES" %>
<%@ page import="quiz_web.Models.User" %><%--
  Created by IntelliJ IDEA.
  User: k
  Date: 09.06.24
  Time: 11:56 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String responseMessage = "";
    int responseMessageId = Integer.parseInt(request.getParameter("responseMessageId"));
    responseMessage = EDIT_RESPONSE_MESSAGES[responseMessageId];
    User curUser = (User)session.getAttribute("curUser");
    String myProfileLink = "profile.jsp?profileUsername=" + curUser.getUserName();


%>
<html>
<head>
    <title>Edit Profile</title>
    <link rel="stylesheet" type="text/css" href="../style/userProfileStyle.css">
</head>
<body>
    <div class="profile-page">
        <h2  style="color: #66FCF1;"><%=responseMessage%></h2>
        <div class="content">
            <a style="text-decoration: none; color: #66FCF1" href="<%=myProfileLink%>"><img src=https://cdn-icons-png.flaticon.com/512/8121/8121260.png
                                              alt="Empty" width="60" height="60"><br>Back to My Profile</a>
            <h2>Anything left blank, will not be changed.</h2>
    <form class="edit_profile_form" action="<%= request.getContextPath() %>/editProfile" method="post">
        <label for="first_name">New First Name:</label>
        <input type="text" maxlength="30" id="first_name" name="first_name" placeholder="William"><br><br>

        <label for="last_name">New Last Name:</label>
        <input type="text" maxlength="30" id="last_name" name="last_name" placeholder="Butler"><br><br>

        <label for="picture_link">New Picture URL:</label>
        <textarea maxlength="1000" class="all_text_area" id="picture_link" name="picture_link" placeholder="https://i.pinimg.com/736x/5d/50/4b/5d504bd3c9995fe7792fc6c4a6866974.jpg"></textarea><br><br>

        <label for="bio">New Bio:</label>
        <textarea maxlength="300" class="all_text_area" id="bio" name="bio" placeholder="Skilled drug addict..."></textarea><br><br>

        <h4>Old password is needed for every change.</h4>

        <label for="old_password">Enter Old Password:</label>
        <input type="password" id="old_password" name="old_password" placeholder="password"><br><br>
        <input class="single_button" type="submit" value="Save Changes Above" name="submit_button">

        <label for="password">Enter New Password:</label>
        <input type="password" id="password" name="password" placeholder="password"><br><br>

        <label for="repeat_password">Repeat New Password:</label>
        <input type="password" id="repeat_password" name="repeat_password" placeholder="password"><br><br><br>

        <input class="single_button" type="submit" value="Update Password" name="submit_button">
    </form>
    </div>
    </div>
</body>
</html>
