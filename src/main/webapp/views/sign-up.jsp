<%--
  Created by IntelliJ IDEA.
  User: ilyaborovik
  Date: 03.03.2018
  Time: 14:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<jsp:include page="base.jsp"/>
<html>
<head>
    <title>Registration</title>
</head>
<body>
<%--@elvariable id="signUpForm" type="ru.innopolis.forms.SignUpForm"--%>
    <form:form method="post" modelAttribute="signUpForm" action="/sign-up">
        <h2>Fill the registration form to create an account:</h2>
        <p>${requestScope.responseMessage}</p>
        Login: <form:input type="text" path="login" required="required"/><br>
        First name: <form:input type="text" path="firstName" required="required"/><br>
        Last name: <form:input type="text" path="lastName" required="required"/><br>
        Password: <form:input type="password" path="password" required="required"/><br>
        Confirm password: <form:input type="password" path="passwordConfirm" required="required"/><br>
        <input type="submit" value="Sign Up"/>
    </form:form>
</body>
</html>
