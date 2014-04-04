<%-- 
    Document   : create_account.jsp
    Created on : Nov 14, 2013, 4:24:25 AM
    Author     : wndessy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel='stylesheet' href='mydefault.css' type='text/css'/> 
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sign up page</title>
    </head>
    <body>
<a href="index.jsp" />Back to main page</a>

        <div id="bodyContainer">
            <form action="signUp" method="POST">
                Name:  <input type="text" name="Myname" required="required"  placeholder="FirstName  Surname"/><br>
                Email Address:  <input type="email" name="email" required="required" /><br>
                Telephone:  <input type="number" name="phone" required="required" /><br>
                Password:   <input type="password" name="password" required="required" /><br>
                Confirm Password<input type="password" required="required" name="password1"/>
                        <button type="submit" name="Signup"> Sign Up </button>            
            </form>
        </div>
        </body>
</html>
