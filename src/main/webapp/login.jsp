<%--
  Created by IntelliJ IDEA.
  User: ilyaborovik
  Date: 03.03.2018
  Time: 11:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="base.jsp"/>
<html>
<head>
    <title>Login</title>
</head>
<body>
<form method="post">
    <p>${sessionScope.responseMessage}</p>
    <c:remove var="responseMessage" scope="session" />
    Login: <input type="text" name="login" /><br>
    Password: <input type="password" name="password"/><br>
    <input type="submit" value="Sign In"/>
</form>
</body>
</html>
