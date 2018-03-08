<%--
  Created by IntelliJ IDEA.
  User: ilyaborovik
  Date: 04.03.2018
  Time: 17:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="base.jsp"/>
<html>
<head>
    <title>Taxi Ordering</title>
</head>
<body>
    <h3>Order the taxi below:</h3>
    <h4>Fill the parameters and the system will show you the list of possible options</h4>
    <p>${sessionScope.responseMessage}</p>
    <c:if test="${sessionScope.orderSuccess}">
        <p>
            Car: ${requestScope.ride.car.model} ${requestScope.ride.car.color} ${requestScope.ride.car.serialNumber}<br>
            Driver: ${requestScope.ride.car.driver.firstName} ${requestScope.ride.car.driver.lastName},
                ${requestScope.ride.car.driver.age}
        <p>Please, wait for the confirmation call from the driver</p>
        <p>Check the status of order in account <a href="history">history</a></p>
    </c:if>
    <c:remove var="responseMessage" scope="session" />
    <c:remove var="orderSuccess" scope="session" />
    <form method="post">
        <select name="city" id="city">
            <option value="0">Choose city: </option>
            <c:forEach items="${requestScope.cityServices}" var="elem">
                <option value="${elem.key.id}">${elem.key.name}</option>
            </c:forEach>
        </select>Save choice: <input type="checkbox" name="saveCity" checked/><br>
        From: <input type="text" name="locationFrom" required/>
        To: <input type="text" name="locationTo" required/><br>
        Phone: <input type="tel" name="phoneNumber" id="phoneNumber" value="${requestScope.user.phoneNumber}" required>
        Save number: <input type="checkbox" name="savePhoneNumber" checked/><br>
        <div id="select_boxes">Service: </div>
        Child Seat: <input type="checkbox" name="childSeat"/><br>
        Comments: <textarea name="orderComments" rows="8" cols="64"></textarea><br>
        <input type="submit" id="place-order" value="Order Taxi" disabled/>
</form>
</body>
</html>

<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery.maskedinput/1.4.1/jquery.maskedinput.js"></script>
<script type="text/javascript">
    var taxiServices = {};

    <c:forEach items="${requestScope.cityServices}" var="elem">
        taxiServices[${elem.key.id}] = {"values": []};
        <c:forEach items="${elem.value}" var="service">
            taxiServices[${elem.key.id}].values
                .push({"value": "${service.id}", "text": "${service.serviceType} from ${service.baseRate}₽"});
        </c:forEach>
    </c:forEach>

    function serviceOptions() {
        var current = $('#city').val();
        $('#place-order').prop('disabled', current === '0');
        $('#select_boxes').html('<select name="service" id="service"></select>');
        if (current > 0) {
            var options = '';
            $.each(taxiServices[current].values, function (index, values) {
                console.log(values);
                options += '<option value="' + values.value + '">' + values.text + '</option>';
            });
            $('#service').html(options);
        }
    }

    $(document).ready(function(){
        $(function() {
            $("#city").val('${requestScope.user.cityId}');
            $("#phoneNumber").mask("+7 (999) 999-9999");
            serviceOptions();
        });
        $('#city').on('change', function(){
            serviceOptions();
        });
    });
</script>
