/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controler;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import static org.apache.taglibs.standard.functions.Functions.trim;

/**
 *
 *
 * @author wndessy
 */
public class controler extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        DbModules db = new DbModules();
        HttpSession session = request.getSession(true);

        // the beggin of codeBlock for login
        if (request.getParameter("email") != null) {
            try {
                Item it = new Item();
                it.setEmail(request.getParameter("email"));
                it.setPassword(request.getParameter("password"));
                it = DbModules.validateLogin(it);
                
                if (it.isValid) {
                    it.setCustomerName(DbModules.findName(request.getParameter("email")));
                    it.setCustomerID(DbModules.findCustomerId(request.getParameter("email")));
                                        
                    session.setAttribute("customerID", it.getCustomerID());
                    session.setAttribute("CurrentSessionUser", it);
                    
                    //to test if the user is an administrator
                    String name= trim(it.Name);
                    out.println(name);
                       int result = name.compareToIgnoreCase( "Admin" );
                    if ( result==0) {
                        out.println("<font color =Green> <b>  Thank you for Loging in " + it.Name + ". You are logged in as an administratorS</b></font>");
                        RequestDispatcher rd = request.getRequestDispatcher("Admin.jsp");
                        rd.include(request, response);

                    } else {
                        out.println("<font color =red><b>  Thank you for Loging in " + it.Name + ".Now you can start buying</b></font>");
                        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
                        rd.include(request, response);
                    }
                } else {
                    out.println("<font color =red><b> You have entered an Incorerct Email or password,please try again</b></font>");
                    RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
                    rd.include(request, response);
                    
                }
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        out.close();

    }



// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
/**
 * Handles the HTTP <code>GET</code> method.
 *
 * @param request servlet request
 * @param response servlet response
 * @throws ServletException if a servlet-specific error occurs
 * @throws IOException if an I/O error occurs
 */
@Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
        public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
