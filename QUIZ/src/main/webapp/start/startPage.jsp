<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quiz Start Page</title>
    <link rel="stylesheet" type="text/css" href="../style/startPageStyle.css">
</head>
<body>
<div class="container">
    <div class="image-container">
        <img src="../start/quizzard.png" alt="QUIZZARD">
    </div>

    <a href="../login/login.jsp" class="button">Login</a>
    <a href="../create/createAccount.jsp" class="button">Create Account</a>

    <form action="<%= request.getContextPath() %>/Login" method="post">
        <input type="submit" name="button" value="Guest" class="button">
    </form>
</div>

<div class="footer-links">
    <a href="../start/help.jsp">Help</a>
    <a href="../start/terms.jsp">Terms</a>
    <a href="../start/about.jsp">About</a>
</div>
</body>
</html>
