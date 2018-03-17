<%--
  Created by IntelliJ IDEA.
  User: ilyaborovik
  Date: 03.03.2018
  Time: 11:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<jsp:include page="base.jsp"/>
<html>
<head>
    <title>Login</title>
</head>
<body>
<%--@elvariable id="signInForm" type="ru.innopolis.forms.SignInForm"--%>
<form:form method="post" modelAttribute="signInForm" action="/login">
    <p>${requestScope.responseMessage}</p>
    Login: <form:input type="text" path="login" required="required"/> <br>
    Password: <form:input type="password" path="password" required="required"/><br>
    <input type="submit" value="Sign In"/>
</form:form>
</body>
</html>
