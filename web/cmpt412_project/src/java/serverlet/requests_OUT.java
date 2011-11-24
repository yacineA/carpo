/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serverlet;

import java.sql.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import superclass.Events;

/**
 *
 * @author wen
 */
@WebServlet(name = "requests_OUT", urlPatterns = {"/requests_out"})
public class requests_OUT extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String id = request.getParameter("id");
        String token = request.getParameter("token");
        boolean isLogged = false;
        PrintWriter out = response.getWriter();
        try {
            Events e = new Events();
            isLogged = e.verify_token(id, token);
        } finally {
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            if(isLogged){
                out.println("<Requests>");
                try{
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con=DriverManager.getConnection("jdbc:mysql://70.64.6.83:3306/test","root","test");
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery("select * from request");
                   
                    while(rs.next()){
                        int u_id = rs.getInt("id");                       
                        String e_stime = rs.getString("start_time");
                        String e_slat = rs.getString("start_lat");
                        String e_slon = rs.getString("start_log");
                        String u_status = rs.getString("status");
                        String u_creator = rs.getString("creator");
                        String e_elat = rs.getString("end_lat");
                        String e_elog = rs.getString("end_log");
                        
                        out.println("<Request>");                   
                        out.println("<id>"+u_id+"</id>");
                        out.println("<start_time>"+e_stime+"</start_time>");
                        out.println("<start_lat>"+e_slat+"</start_lat>");
                        out.println("<start_log>"+e_slon+"</start_log>");
                        out.println("<status>"+u_status+"</status>");
                        out.println("<creator>"+u_creator+"</creator>");
                        out.println("<end_lat>"+e_elat+"</end_lat>");
                        out.println("<end_log>"+e_elog+"</end_log>");
                        out.println("</Request>");
                                                
                    }
                    
                    
                    
                }catch(Exception e){
                    out.print(e.toString());
                }
                out.println("</Requests>");
                out.println(true);
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
