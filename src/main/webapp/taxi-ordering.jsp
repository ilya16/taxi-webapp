<%--
  Created by IntelliJ IDEA.
  User: ilyaborovik
  Date: 04.03.2018
  Time: 17:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Taxi Ordering</title>
</head>
<body>
    <ul>
        <li><a href=".">Home</a></li>
        <li><a href="login">Sign In</a></li>
        <li><a href="sign-up">Sign Up</a></li>
        <li><a href="taxi-ordering">Taxi Ordering</a></li>
        <li><a href="order-history">Order History</a></li>
        <li><a href="logout">Log Out</a></li>
    </ul>
    <h3>Order the taxi below:</h3>
    <h4>Fill the parameters and the system will show you the list of possible options</h4>
    <p>${sessionScope.responseMessage}</p>
    <c:if test="${sessionScope.orderSuccess}">
        <p>You can check the order status in your account <a href="/order-history">history</a></p>
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
        From: <input type="text" name="locationFrom"/>
        To: <input type="text" name="locationTo"/><br>
        Phone: <input type="text" name="phoneNumber" id="phoneNumber"/>
        Save number: <input type="checkbox" name="savePhoneNumber" checked/><br>
        <div id="select_boxes">Service: </div>
        Child Seat: <input type="checkbox" name="childSeat"/><br>
        Comments: <textarea name="orderComments" rows="8" cols="64"></textarea><br>
        <input type="submit" id="place-order" value="Order Taxi" disabled/>
</form>
</body>
</html>

<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script type="text/javascript">
    var taxiServices = {};

    <c:forEach items="${requestScope.cityServices}" var="elem">
        taxiServices[${elem.key.id}] = {"values": []};
        <c:forEach items="${elem.value}" var="service">
            taxiServices[${elem.key.id}].values
                .push({"value": "${service.id}", "text": "${service.serviceType} from ${service.baseRate}₽"});
        </c:forEach>
    </c:forEach>

    $(document).ready(function(){
        $(function() {
            $("#city").val('${requestScope.user.cityId}');
            $("#phoneNumber").val('${requestScope.user.phoneNumber}');
            $('#place-order').prop('disabled', $(this).val() === '0');
        });
        $('#city').on('change', function(){
            var current = $(this).val();
            $('#place-order').prop('disabled', $(this).val() === '0');
            $('#select_boxes').html('<select name="service" id="service"></select>');
            if (current > 0) {
                var options = '';
                $.each(taxiServices[current].values, function (index, values) {
                    console.log(values);
                    options += '<option value="' + values.value + '">' + values.text + '</option>';
                });
                $('#service').html(options);
            }
        });
    });
</script>
