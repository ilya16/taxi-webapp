<%--
  Created by IntelliJ IDEA.
  User: ilyaborovik
  Date: 03.03.2018
  Time: 14:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="base.jsp"/>
<html>
<head>
    <title>Registration</title>
</head>
<body>
    <form method="post">
        <h2>Fill the registration form to create an account:</h2>
        <p>${sessionScope.responseMessage}</p>
        <c:remove var="responseMessage" scope="session" />
        Login: <input type="text" name="login" value="${requestScope.login}" required/><br>
        First name: <input type="text" name="firstName" value="${requestScope.firstName}" required/><br>
        Last name: <input type="text" name="lastName" value="${requestScope.lastName}" required/><br>
        Password: <input type="password" name="password" required/><br>
        Confirm password: <input type="password" name="passwordConfirm" required/><br>
        <input type="submit" value="Sign Up"/>
    </form>
</body>
</html>
