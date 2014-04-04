<%-- 
    Document   : ManageComponents
    Created on : Jan 28, 2014, 3:20:15 PM
    Author     : wndessy
--%>

<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<jsp:useBean class="controler.Common" id="common"></jsp:useBean>


<%@include  file="Admin.jsp" %>


<html>
    <head>
          <link rel='stylesheet' href='mydefault.css' type='text/css'/> 
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>MY SHopping Cart</title>
        <script type="text/javascript" src="js/mod_general.js"></script>
        <script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
        <script type="text/javascript" src="js/jquery.js"></script>
        <script type="text/javascript" src="js/jquery-ui.min.js"></script>
    </head>
    <body>
        <h1> Components</h1>
        <div id="bodyContainer">
            <h2>Add new Components</h2>
             <form action="ManageComponent"  method="GET">

                 Component name  <input type="text" name="Name" required="required" />
                 Price per Component  <input type="number" name="price" required=""/>
                Description      <input type="text" name="description">
                <button type="submit" class="myclass"> Add </button> 
            </form>
        </div>
        <div id="bodyContainer">
            <h2>List of Component </h2>
            <table>
                <tr> <td>component_id</td>  <td>Name</td> <td>number_of_units</td> <td>price_per_item</td> <td>description</td></tr>                 
                <sql:setDataSource var="name" driver="com.mysql.jdbc.Driver" url="${common.db_url}" user="${common.user}" password="${common.password}"/>
                <sql:query dataSource="${name}" var="itemName">
                    SELECT * FROM component;
                </sql:query>
                <c:forEach  var="row" items="${itemName.rows}">
                    <tr> <td>${row.compnt_id}</td>  <td>${row.name}</td> <td>${row.number_of_units}</td> <td>${row.price_per_item}</td> <td>${row.description}</td></tr>  

                </c:forEach>

            </table>
        </div>
        <div id="bodyContainer">
            <h2> Add components units to Stock</h2>
            <form action="Addstock" method="GET">
                Item to update
            <select id="itemToAdd">
                <%-- a loop for piking itema from the database and adding to the dropdown--%>
                <%--!-- <sql:setDataSource var="name" driver="com.mysql.jdbc.Driver" url="${common.db_url}" user="${common.user} " password="${common.password}"/>--%>
                <sql:query dataSource="${name}" var="itemName">
                    SELECT name FROM component;
                </sql:query>
                <c:forEach  var="row" items="${itemName.rows}">
                    <option value="${row.name}" > <c:out value="${row.name}"> </c:out></option> 
                </c:forEach>   
            </select>  
            Quantity to add  
            <input type="number" id="quantity" required="required"/>
            <button id="Update_components_button"  name ="Update_components_button" class="Update_components_button" type="button" onclick="General.update_components()">Update</button>
        </form>
 
        </div>
    </body>
</html>
