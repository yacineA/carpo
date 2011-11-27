/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serverlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import superclass.Events;

/**
 *
 * @author wen
 */
@WebServlet(name = "rating_OUT", urlPatterns = {"/rating_out"})
public class rating_OUT extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String id = request.getParameter("id");
        String token = request.getParameter("token");
        boolean isLogged = false;
        Double temp=0.0;
        int counter=0;
        try {
            Events events = new Events();
            isLogged = events.verify_token(id, token);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            if (isLogged) {

                Connection con;
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    con = DriverManager.getConnection("jdbc:mysql://70.64.6.83:3306/test", "root", "test");
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT rating FROM rating WHERE user_id ='" + id + "'");
                    while(rs.next()){
                        String r = rs.getString("rating");
                        temp = temp + Float.valueOf(r.trim()).floatValue();
                        counter++;
                    }
                    
                    temp = temp/counter;
                    out.println("<Ratings>");
                    out.println("<Rating>");
                    out.println(temp);
                    out.println("<Rating>");
                    out.println("</Ratings>");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }else{
                
                out.println("<Error>");
                out.println("<id>"+id+"</id>");
                out.println("</Error>");
                
            }
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
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
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
