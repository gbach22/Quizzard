<%--
  Created by IntelliJ IDEA.
  User: giorgi
  Date: 6/7/24
  Time: 9:49 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Welcome!</title>
    <link rel="stylesheet" type="text/css" href="../style/loginStyle.css">
</head>
<body>
<h1>Welcome!</h1>
<p>Let's create your account</p>
<%if(request.getAttribute("error") != null) {%>
<p><%=request.getAttribute("error")%></p>
<%}%>
<form action="<%=request.getContextPath()%>/create" method="post">
    <input type="text" maxlength="30" id="firstName" name="firstName" placeholder="First name" required><br>
    <input type="text" maxlength="30" id="lastName" name="lastName" placeholder="Last name" required><br>
    <input type="text" maxlength="30" id="userName" name="userName" placeholder="Username" required><br>
    <input type="password" maxlength="30" id="password" name="password" placeholder="Password" required><br>

    <input type="submit" value="Create Account" id="createAccount">
</form>
</body>
</html>
