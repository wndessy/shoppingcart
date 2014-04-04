<%@page import="java.io.*,java.util.*" %>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="controler.DbModules"%>
<%@page import="controler.Item"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>

<%--  to in clude login or logout depending on whether the user is logged in or not --%>
<c:choose >
    <c:when test="${sessionScope.CurrentSessionUser.name == null }" >
     <%@include file="login.jsp" %>
    </c:when>
    <c:otherwise>
    <%@include file="logout.jsp" %>
    </c:otherwise>
</c:choose>

<c:if test="${sessionScope.CurrentSessionUser.name == 'Admin' }" var="test">
    <a href="Admin.jsp">Back to Admin</a>
</c:if>

<jsp:useBean class="controler.DbModules" id="FromDb"></jsp:useBean>
<jsp:useBean id="item" class="controler.Item"></jsp:useBean>
<jsp:useBean id="common" class="controler.Common"></jsp:useBean>



<!DOCTYPE html>
<html>
    <head>
        <link rel='stylesheet' href='mydefault.css' type='text/css'/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>My shopping cart</title>
                <script type="text/javascript" src="js/mod_general.js"></script>
        <script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
        <script type="text/javascript" src="js/jquery.js"></script>
        <script type="text/javascript" src="js/jquery-ui.min.js"></script>
        <script type="text/javascript" src="js/assets/prettify.js"></script>
        <script type="text/javascript" src="js/jquery.multiselect.js"></script>
        
        <link rel="stylesheet" type="text/css" href="js/jquery.multiselect.css" />
        <link rel="stylesheet" type="text/css" href="js/assets/style.css" />
        <link rel="stylesheet" type="text/css" href="js/assets/prettify.css" />
        <link rel="stylesheet" type="text/css" href="js/jquery-ui.css" />
    </head>
    <body>
        <div>
               
        </div>
        <div style="float: left;width:700px;height" id="bodyContainer">
            <%-- the begginingof a loop to display items --%>
            <sql:setDataSource var="name" driver="com.mysql.jdbc.Driver" url="${common.db_url}" user="${common.user}" password="${common.password}"/>

            <sql:query dataSource="${name}" var="itemName">
                select * from product;
            </sql:query>

                <c:forEach  var="row" items="${itemName.rows}" >
                <jsp:setProperty name="item" property="count" value="${item.count+1}"/>

                <%--to be looped depending on the number of items in stock--%>
                <div id="${"myDiv_".concat(item.count)}" style="width: 200px;height: 150x;float: left;margin: 8px ; padding: 2px">
                    <fmt>
                        <c:out value="${row.product_id}"></c:out>
                        <%--<img src="" width="10" height="10" alt="this item"/><br>--%>
                            <label name="<c:out value="${row.name}"/>"> <c:out value="${row.name}"> </c:out></label><br>
                        <label id="itemPrice"> Ksh<c:out value="${row.price}"></c:out> </label><br>

                            <form action="" method="post">
                                <button id="add_to_cart_button"  name ="add_to_cart_button" class="add_to_cart_button" type="button" onclick="General.buttonClicked('${row.product_id}', '${row.name}', '${row.price}')">Add Complete Item</button>
                            <button id="add_components_to_cart_button"  name ="add_components_to_cart_button" class="add_components_to_cart_button" type="button" onclick="General.outputSelected(${row.product_id})">Select Components</button>
                            <input type="hidden" name="${row.product_id}" value="${row.product_id}">

                            <sql:query dataSource="${name}" var="componentName">
                                select  name,price_per_item from product_component natural join component where product_id="${row.product_id}";
                            </sql:query>

                            <c:set var="number" value="${componentName.rowCount}"></c:set>
                            <c:if test="${ number > 0}" var="testing">
                                <select title="Basic example" multiple="multiple" id="${row.product_id}" size="${componentName.rowCount}">
                                    <c:forEach  var="row1" items="${componentName.rows}">
                                        <option value ="${row1.name}:${row1.price_per_item}"> ${row1.name}:Ksh ${row1.price_per_item}</option>
                                    </c:forEach>
                                </select>
                            </c:if>

                            <input type="hidden" name="${row.name}" value="${row.name}">
                            <input type="hidden" name="${row.price}" value="${row.price}">
                        </form>
                    </fmt>
                </div>
            </c:forEach>
        </div>
    <center>
        <table width="300" border="1" cellspacing="0" cellpadding="2" border="0" id="Cart">
            <caption><b>Shopping Cart Contents</b></caption>
            <th>Description</th><th>Price</th><th>Quantity</th><th></th>   
            <tr style="font-size: x-large"><td>Total</td><td id="totalCost" >Total</td><td></td><td><button id="buy_button"  disabled="true" name ="buy_button" class="buy_button" type="button" onclick="General.buttonBuyItems()">Buy</button></td></tr>
        </table>
    </center>
</body>
</html>
