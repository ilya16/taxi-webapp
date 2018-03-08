<%--
  Created by IntelliJ IDEA.
  User: ilyaborovik
  Date: 07.03.2018
  Time: 21:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Title</title>
</head>
<style>
    li {
        display: inline;
    }
</style>
<body>
<ul>
    <li><a href=".">Home</a></li>
    <li><a href="order-taxi">Order Taxi</a></li>
    <li><a href="history">History</a></li>
    <c:choose>
        <c:when test="${sessionScope.userLogin == null}">
            <li><a href="login">Sign In</a></li>
            <li><a href="sign-up">Sign Up</a></li>
        </c:when>
        <c:otherwise>
            <li><a href="logout">Log Out</a></li>
        </c:otherwise>
    </c:choose>
</ul>

</body>
</html>
