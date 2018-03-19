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
    <h2>Error occurred while accessing the page.</h2>
    <h3>Description: ${requestScope.errorMessage}</h3>
</body>
</html>
