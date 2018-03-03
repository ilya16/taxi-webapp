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
<form method="post">
    <input type="text" name="login" /><br>
    <input type="text" name="password"/>
    <input type="submit" value="Sign In"/>
    <p>${requestScope.error}</p>
</form>
</body>
</html>
