<%-- 
    Document   : ManageUsers
    Created on : Jan 28, 2014, 3:38:04 PM
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
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
             <link rel='stylesheet' href='mydefault.css' type='text/css'/> 

        <title>My Shopping Cart</title>
    </head>
    <body>
        <div id="bodyContainer">
            <h1>Users</h1>
            <table>
                <tr> <td>Id</td> <td>Name</td> <td>Email address</td> <td>Phone number</td></tr>              
                <sql:setDataSource var="name" driver="com.mysql.jdbc.Driver" url="${common.db_url}" user="${common.user}" password="${common.password}"/>
                <sql:query dataSource="${name}" var="itemName">
                    SELECT * FROM Customer;
                </sql:query>
                <c:forEach  var="row" items="${itemName.rows}">
                    <tr> <td>${row.cust_id}</td>  <td>${row.name}</td> <td>${row.email_adress}</td> <td>${row.phone}</td> </tr>  

                </c:forEach>
            </table>
        </div>
       </body>
     </html>
