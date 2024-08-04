<%--
  Created by IntelliJ IDEA.
  User: almasxitinalifa
  Date: 07.06.24
  Time: 20:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Log Into Your Account</title>
    <link rel="stylesheet" type="text/css" href="../style/loginStyle.css">
</head>
<body>
    <form action="<%= request.getContextPath() %>/Login" method="post">
        <div>
            <input type="input" placeholder="Username" name="Username">
        </div>
        <div>
            <input type="password" placeholder="Password" name="Password">
        </div>
        <div>
            <input type="submit" name="button" value="Login">
        </div>
        <a href="../create/createAccount.jsp"> Create new Account </a>
    </form>
</body>
</html>
