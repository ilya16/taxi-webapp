<%--
  Created by IntelliJ IDEA.
  User: ilyaborovik
  Date: 03.03.2018
  Time: 11:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<ul>
    <li><a href=".">Home</a></li>
    <li><a href="login">Sign In</a></li>
    <li><a href="sign-up">Sign Up</a></li>
    <li><a href="taxi-ordering">Taxi Ordering</a></li>
</ul>
<form method="post">
    <p>${requestScope.responseMessage}</p>
    <input type="text" name="login" /><br>
    <input type="password" name="password"/>
    <input type="submit" value="Sign In"/>
</form>
</body>
</html>
