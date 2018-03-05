<%--
  Created by IntelliJ IDEA.
  User: ilyaborovik
  Date: 04.03.2018
  Time: 17:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Taxi Ordering</title>
</head>
<body>
    <ul>
        <li><a href=".">Home</a></li>
        <li><a href="login">Sign In</a></li>
        <li><a href="sign-up">Sign Up</a></li>
        <li><a href="taxi-ordering">Taxi Ordering</a></li>
        <li><a href="logout">Log Out</a></li>
    </ul>
    <h3>Order the taxi below:</h3>
    <h4>Fill the parameters and the system will show you the list of possible options</h4>
    <form method="post">
        <p>${requestScope.responseMessage}</p>
        Login: <input type="text" name="login"/><br>
        First name: <input type="text" name="firstName"/><br>
        Last name: <input type="text" name="lastName"/><br>
        Password: <input type="password" name="password"/><br>
        Confirm password: <input type="password" name="passwordConfirm"/><br>
        <input type="submit" value="Sign Up"/>
</form>
</body>
</html>
