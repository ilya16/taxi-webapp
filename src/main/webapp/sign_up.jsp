<%--
  Created by IntelliJ IDEA.
  User: ilyaborovik
  Date: 03.03.2018
  Time: 14:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registration</title>
</head>
<body>
<form method="post">
    <h2>Fill the registration form to create an account:</h2>
    <p>${requestScope.error}</p>
    Login: <input type="text" name="login"/><br>
    Password: <input type="password" name="password"/><br>
    Confirm password: <input type="password" name="passwordConfirm"/><br>
    <input type="submit" value="Sign Up"/>
</form>
</body>
</html>
