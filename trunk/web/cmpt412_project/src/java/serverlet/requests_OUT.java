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
        String start_time = request.getParameter("startdatetime");
        String count="10";
        if(request.getParameter("count")!=null)
            count=request.getParameter("count");
        boolean isLogged = false;
        PrintWriter out = response.getWriter();
        try {
            Events e = new Events();
            isLogged = e.verify_token(id, token);
        }catch (Exception e){
        }finally {
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            if(isLogged){
                out.println("<Requests>");
                try{
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con=DriverManager.getConnection("jdbc:mysql://70.64.6.83:3306/test","root","test");
                    Statement stmt = con.createStatement();
                    //ResultSet rs = stmt.executeQuery("select * from request LIMIT "+count);
                   ResultSet rs = stmt.executeQuery("select * from request where start_time < "+start_time+" LIMIT "+count);
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
                        out.println("<Type>Request</Type>");
                        out.println("<ID>"+u_id+"</ID>");
                        out.println("<Creator>"+u_creator+"</Creator>");
                        out.println("<StartTime>"+e_stime+"</StartTime>");
                        out.println("<StartLatitude>"+e_slat+"</StartLatitude>");
                        out.println("<StartLongitude>"+e_slon+"</StartLongitude>");
                        out.println("<Status>"+u_status+"</Status>");
                        
                        out.println("<EndLatitude>"+e_elat+"</EndLatitude>");
                        out.println("<EndLongitude>"+e_elog+"</EndLongitude>");
                        out.println("</Request>");
                                                
                    }
                    
                    
                    
                }catch(Exception e){
                    out.print(e.toString());
                }
                out.println("</Requests>");
                //out.println(true);
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
