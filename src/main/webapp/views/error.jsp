<%--
  Created by IntelliJ IDEA.
  User: ilyaborovik
  Date: 14.03.2018
  Time: 17:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="base.jsp"/>
<html>
<head>
    <title>Error</title>
</head>
<body>
    Error occurred while processing the page. Here is a brief description:
    <ul>
        <li>Exception: <c:out value="${requestScope['javax.servlet.error.exception']}" /></li>
        <li>Exception type: <c:out value="${requestScope['javax.servlet.error.exception_type']}" /></li>
        <li>Exception message: <c:out value="${requestScope['javax.servlet.error.message']}" /></li>
        <li>Request URI: <c:out value="${requestScope['javax.servlet.error.request_uri']}" /></li>
        <li>Servlet name: <c:out value="${requestScope['javax.servlet.error.servlet_name']}" /></li>
        <li>Status code: <c:out value="${requestScope['javax.servlet.error.status_code']}" /></li>
    </ul>
</body>
</html>
