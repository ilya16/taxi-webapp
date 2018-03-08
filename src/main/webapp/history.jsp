<%--
  Created by IntelliJ IDEA.
  User: ilyaborovik
  Date: 07.03.2018
  Time: 19:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="base.jsp"/>
<html>
<head>
    <title>History</title>
</head>
<body>
    <h2>Order history</h2>
    <p>${sessionScope.responseMessage}</p>
    <c:remove var="responseMessage" scope="session" />
    <table>
        <tr>
            <th>Date</th>
            <th>City</th>
            <th>Route</th>
            <th>Car</th>
            <th>Driver</th>
            <th>Service</th>
            <th>Price</th>
            <th>Comments</th>
            <th>Status</th>
        </tr>
        <c:forEach items="${requestScope.rides}" var="ride">
            <tr>
                <td>
                    ${ride.orderTime}
                </td>
                <td>
                    ${ride.taxiService.city.name}
                </td>
                <td>
                    Route:<br>
                        ${ride.locationFrom} -> ${ride.locationTo}<br>
                        <%--Time:<br>--%>
                        <%--${ride.timeStart} -> ${ride.timeEnd}<br>--%>
                </td>
                <td>
                    Serial No.: ${ride.car.serialNumber}<br>
                    Model: ${ride.car.model}<br>
                    Color: ${ride.car.color}
                </td>
                <td>
                    Name: ${ride.car.driver.firstName} ${ride.car.driver.lastName}<br>
                    Age: ${ride.car.driver.age}
                </td>
                <td>
                        ${ride.taxiService.serviceType}
                </td>
                <td>
                    ${ride.price}₽
                </td>
                <td>
                    ${ride.orderComments}
                </td>
                <td>
                    ${ride.status}<br>
                    <c:if test="${ride.status == 'ordered'}">
                        <form method="post">
                            <input type="hidden" name="idToCancel" value="${ride.id}"/>
                            <input type="submit" value="Cancel"/>
                        </form>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
