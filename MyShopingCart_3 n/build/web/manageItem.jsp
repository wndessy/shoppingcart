<%-- 
    Document   : manageItem.jsp
    Created on : Jan 4, 2014, 10:48:30 PM
    Author     : wndessy
--%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>

<%@include  file="Admin.jsp" %>
<jsp:useBean id="common" class="controler.Common"></jsp:useBean>
    <!DOCTYPE html>
    <html>
        <head>
            <link rel='stylesheet' href='mydefault.css' type='text/css'/> 
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>JSP Page</title>
            <script type="text/javascript" src="js/mod_general.js"></script>
            <script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
            <script type="text/javascript" src="js/jquery.js"></script>
        </head>        
        <body>
            <h1>Items</h1>
            <div id="bodyContainer">
                <h2> Add new item</h2>
                <form action="manageItem"  method="GET">

                    Item name  
                    <input type="text" id="ItemName" placeholder="Item name" required="required"/>

                    Components 
                    <select multiple  id="components"   >
                    <%-- a loop for picking items from the database and adding to the dropdown--%>
                    <sql:setDataSource var="name" driver="com.mysql.jdbc.Driver" url="${common.db_url}" user="${common.user}" password="${common.password}"/>
                    <sql:query dataSource="${name}" var="itemName">
                        SELECT * FROM component;
                    </sql:query>
                    <c:forEach  var="row" items="${itemName.rows}">
                        <option value="${row.compnt_id}" > <c:out value="${row.name}"> </c:out></option> 
                    </c:forEach>   
                </select>  
                price  
                <input type="number" id="price" placeholder="value"  required="required"/>

                <button type="Button" class="myclass" onclick="General.addItem('components')"> Add</button> 
            </form>
        </div>
        <div id="bodyContainer">
            <h2> List of items</h2>
            <table>
                <tr> <td>Id</td> <td>item name</td> <td>Components</td> <td> Price</td></tr>
                <sql:query dataSource="${name}" var="itemName">
                    SELECT * FROM product;
                </sql:query>
                <c:forEach  var="row" items="${itemName.rows}">
                    <tr> <td>${row.product_id}</td>  <td>${row.name}</td> <td>${row.components}</td> <td>${row.price}</td> </tr>      
                </c:forEach>
            </table>
        </div>
    </body>
</html>
