<%-- 
    Document   : Admin
    Created on : Jan 29, 2014, 12:05:17 AM
    Author     : wndessy
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>

<%-- to check if a user is admin or not and redirect them aproapriatele--%>
<c:if test="${sessionScope.CurrentSessionUser.name != 'Admin' or sessionScope.CurrentSessionUser.name == null }" var="test">
    <c:redirect url="index.jsp" ></c:redirect>
</c:if>
<c:choose >
    <c:when test="${sessionScope.CurrentSessionUser.name == null }" >
     <%@include file="login.jsp" %>
    </c:when>
    <c:otherwise>
    <%@include file="logout.jsp" %>
    </c:otherwise>
</c:choose>


<!DOCTYPE html>
<html>
    <head>
        <link rel='stylesheet' href='mydefault.css' type='text/css'/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Admin Page</title>
    </head>
    <body>
        <div id="admin">
                <a href="index.jsp">Purchase item</a>
                <a href=manageItem.jsp? name="profile">Items</a>
                <a href=ManageComponents.jsp? name="profile">component</a>
               <a href=ManagePurchases.jsp? name="profile">Purchases</a>
                <a href=ManageUsers.jsp? name="profile">Users</a>
           
        </div>
    
    </body>
</html>
