<%-- 
    Document   : ManagePurchases
    Created on : Jan 29, 2014, 7:27:37 PM
    Author     : wndessy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- to check if a user is admin or not and redirect them aproapriatele--%>

<%@include  file="Admin.jsp" %>
<jsp:useBean id="common" class="controler.Common"></jsp:useBean>
<!DOCTYPE html>
<html>
    <head>
          <link rel='stylesheet' href='mydefault.css' type='text/css'/> 
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Manage Purchases</title>
    </head>
    <body>
        <h1>Purchases </h1>
        <div id="bodyContainer">
            <table>
                <tr> <td>ID</td> <td>Date</td> <td>Items</td> <td> Customer</td></tr>
                <sql:setDataSource var="name" driver="com.mysql.jdbc.Driver" url="${common.db_url}" user="${common.user}" password="${common.password}"/>
                <sql:query dataSource="${name}" var="itemName">
                SELECT * FROM `customer` natural join `order`  ;    
                </sql:query>
                <c:forEach  var="row" items="${itemName.rows}">
                    <tr> <td>${row.order_id}</td>  <td>${row.order_date}</td> <td>${row.Items}</td> <td>${row.name}</td> </tr>      
                </c:forEach>
            </table>
        </div>
    </body>
</html>
