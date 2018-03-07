<%--
  Created by IntelliJ IDEA.
  User: ilyaborovik
  Date: 07.03.2018
  Time: 19:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="base.jsp"/>
<html>
<head>
    <title>History</title>
</head>
<body>
    <h1>Order history</h1>
    <table>
        <c:forEach items="${requestScope.rides}" var="ride">
            <tr>
                <th>Date</th>
                <th>Car</th>
                <th>Driver</th>
                <th>City</th>
                <th>Taxi Service</th>
                <th>Ride Info</th>
                <th>Price</th>
                <th>Status</th>
                <th>Change Status</th>
            </tr>
            <tr>
                <td>
                    Order Date: ${ride.orderTime}
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
                    ${ride.taxiService.city.name}
                </td>
                <td>
                    ${ride.taxiService.serviceType}
                </td>
                <td>
                    Route:<br>
                    ${ride.locationFrom} -> ${ride.locationTo}<br>
                    Time:<br>
                    ${ride.timeStart} -> ${ride.timeEnd}<br>
                </td>
                <td>
                    ${ride.price}₽
                </td>
                <td>
                    ${ride.status}
                </td>
                <td>
                    <c:choose>
                        <c:when test="${ride.status == 'ordered'}">
                            <form method="post">
                                <input type="hidden" name="decline" value="${ride.id}"/>
                                <input type="submit" value="Decline"/>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <form method="post">
                                <input type="hidden" name="viewDetails" value="${ride.id}"/>
                                <input type="submit" value="View Details"/>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
