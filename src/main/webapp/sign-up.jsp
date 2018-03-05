<%--
  Created by IntelliJ IDEA.
  User: ilyaborovik
  Date: 03.03.2018
  Time: 14:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Registration</title>
</head>
<body>
    <ul>
        <li><a href=".">Home</a></li>
        <li><a href="login">Sign In</a></li>
        <li><a href="sign-up">Sign Up</a></li>
        <li><a href="taxi-ordering">Taxi Ordering</a></li>
        <li><a href="logout">Log Out</a></li>
    </ul>
    <form method="post">
        <h2>Fill the registration form to create an account:</h2>
        <p>${sessionScope.responseMessage}</p>
        <c:remove var="responseMessage" scope="session" />
        Login: <input type="text" name="login"/><br>
        First name: <input type="text" name="firstName"/><br>
        Last name: <input type="text" name="lastName"/><br>
        Password: <input type="password" name="password"/><br>
        Confirm password: <input type="password" name="passwordConfirm"/><br>
        <input type="submit" value="Sign Up"/>
    </form>
</body>
</html>
