/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serverlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
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
@WebServlet(name = "offers_IN", urlPatterns = {"/offers_in"})
public class offers_IN extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String id = request.getParameter("id");
        String token = request.getParameter("token");
        String creator = request.getParameter("creator");
        String stime = request.getParameter("start_time");
        String slat = request.getParameter("start_lat");
        String slog = request.getParameter("start_log");
        String status = request.getParameter("status");
        String capacity = request.getParameter("capacity");
        String share = request.getParameter("if_share");
        String elat = request.getParameter("end_lat");
        String elog = request.getParameter("end_log");

        boolean isLogged = false;
        try {

            Events events = new Events();
            isLogged = events.verify_token(id, token);

        } finally {
            if (isLogged) {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con = DriverManager.getConnection("jdbc:mysql://70.64.6.83:3306/test", "root", "test");
                    Statement stmt = con.createStatement();
                    
                    stmt.executeUpdate("INSERT INTO test.offer ( id, creator, start_time, start_lat, start_log, status, capacity, if_share, end_lat, end_log)VALUES "
                                + "('"+id+"', '"+creator+"', '"+stime+"','"+slat+"','"+slog+"','"+status+"','"+capacity+"', '"+share+"', '"+elat+"', '"+elog+"')"); 
                    out.println("<Message>");
                    out.println("true");
                    out.println("</Message");
                } catch (Exception e) {
                }

            } else {
                out.println("<Message>");
                out.println("false");
                out.println("</Message");
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
