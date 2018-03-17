<%--
  Created by IntelliJ IDEA.
  User: ilyaborovik
  Date: 04.03.2018
  Time: 17:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<jsp:include page="base.jsp"/>
<html>
<head>
    <title>Taxi Ordering</title>
</head>
<body>
    <h2>Order the taxi below:</h2>
    <h4>Fill the parameters and the system will provide you with the most suitable taxi driver</h4>
    <p>${requestScope.responseMessage}</p>
    <c:if test="${requestScope.orderSuccess}">
        <p>
            Car: ${requestScope.ride.car.model} ${requestScope.ride.car.color} ${requestScope.ride.car.serialNumber}<br>
            Driver: ${requestScope.ride.car.driver.firstName} ${requestScope.ride.car.driver.lastName},
                ${requestScope.ride.car.driver.age}
        <p>Please, wait for the confirmation call from the driver</p>
        <p>Check the status of order in account <a href="history">history</a></p>
    </c:if>
    <c:remove var="responseMessage" scope="session" />
    <c:remove var="orderSuccess" scope="session" />
    <%--@elvariable id="taxiOrderForm" type="ru.innopolis.forms.TaxiOrderForm"--%>
    <form:form method="post" modelAttribute="taxiOrderForm" action="/order-taxi">
        <form:select path="cityId" id="city">
            <form:option value="0" disabled="disabled">Choose city: </form:option>
            <c:forEach items="${requestScope.cityServices}" var="elem">
                <form:option value="${elem.key.id}">${elem.key.name}</form:option>
            </c:forEach>
        </form:select>
        Save choice: <form:checkbox path="saveCity"/><br>
        From: <form:input type="text" path="locationFrom" required="required"/>
        To: <form:input type="text" path="locationTo" required="required"/><br>
        Phone: <form:input type="tel" path="phoneNumber" id="phoneNumber" required="required"/>
        Save number: <form:checkbox path="savePhoneNumber"/><br>
        <div id="select_boxes">Service: </div>
        Child Seat: <form:checkbox path="childSeat"/><br>
        Comments: <form:textarea path="orderComments" rows="8" cols="64"/><br>
        <input type="submit" id="place-order" value="Order Taxi" disabled="disabled"/>
    </form:form>
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
        $('#select_boxes').html('<select name="serviceId" id="service"></select>');
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
            $("#city").val('${requestScope.cityId}');
            $("#phoneNumber").mask("+7 (999) 999-9999");
            serviceOptions();
        });
        $('#city').on('change', function(){
            serviceOptions();
        });
    });
</script>
