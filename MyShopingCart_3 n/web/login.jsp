<%-- 
    Document   : login.jsp
    Created on : Dec 30, 2013, 9:27:42 PM
    Author     : wndessy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        
        <div id="header">
            <div>
                <form action="controler" method="POST">
                    Email Address<input type="email" name="email" placeholder="Email Address" required="required"/>
                    Password<input type="password" name="password" placeholder="Your Password" required="required"/>
                <button type="submit" name="submit">Submit</button>
                </form>
            </div>
            
        <div id="link">
            <a href=signup.jsp? name="profile">Don't Have An Account?</a>
        </div>      
              </div>
      
    </body>
</html>
