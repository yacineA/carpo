/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serverlet;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
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
@WebServlet(name = "offers_OUT", urlPatterns = {"/offers_out"})
public class offers_OUT extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String id = request.getParameter("id");
        String token = request.getParameter("token");
        String count="10";
        if(request.getParameter("count")!=null)
            count=request.getParameter("count");
        
        boolean isLogged = false;
        PrintWriter out = response.getWriter();
        try {
            Events e = new Events();
            isLogged = e.verify_token(id, token);
           // out.print(isLogged);
             
        } catch(Exception e){
        }finally {
            
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            if(isLogged){
                out.println("<Offers>");
                try{
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con=DriverManager.getConnection("jdbc:mysql://70.64.6.83:3306/test","root","test");
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery("select * from offer LIMIT "+count);
                   
                    while(rs.next()){
                        int u_id = rs.getInt("id");
                        String e_creator = rs.getString("creator");
                        String e_stime = rs.getString("start_time");
                        String e_slat = rs.getString("start_lat");
                        String e_slon = rs.getString("start_log");
                        String u_status = rs.getString("status");
                        String u_capcity = rs.getString("capacity");
                        String u_share = rs.getString("if_share");
                        String e_elat = rs.getString("end_lat");
                        String e_elog = rs.getString("end_log");
                        
                        out.println("<Offer>");          
                        out.println("<Type>Offer</Type>");
                        out.println("<ID>"+u_id+"</ID>");
                        out.println("<Creator>"+e_creator+"</Creator>");
                        out.println("<StartTime>"+e_stime+"</StartTime>");
                        out.println("<StartLatitude>"+e_slat+"</StartLatitude>");
                        out.println("<StartLongitude>"+e_slon+"</StartLongitude>");
                        out.println("<Status>"+u_status+"</Status>");
                        out.println("<Capacity>"+u_capcity+"</Capacity>");
                        out.println("<Shared>"+u_share+"</Shared>");
                        out.println("<EndLatitude>"+e_elat+"</EndLatitude>");
                        out.println("<EndLongitude>"+e_elog+"</EndLongitude>");
                        out.println("</Offer>");
                                                
                    }
                    
                    
                    
                }catch(Exception e){
                    out.print(e.toString());
                }
                out.println("</Offers>");
            }else{
                
                out.println("<Error>");
                out.println("<id>"+id+"</id>");
                out.println("</Error>");
                
            }
             
       /*     try {
                Class.forName("com.mysql.jdbc.Driver");
                    Connection con=DriverManager.getConnection("jdbc:mysql://70.64.6.83:3306/test","root","test");
                    Statement stmt = con.createStatement();
                    //ResultSet rs = stmt.executeQuery("select * from offer");
                    
            for(int i=0; i<2000; i++){
                        int temp_id;
                        String temp_creator = "1234213411";
                        Random randomGenerator = new Random ();
                        int mon = randomGenerator.nextInt(11)+1;
                        int day = randomGenerator.nextInt(30)+1;
                        int hr = randomGenerator.nextInt(23)+1;
                        int min = randomGenerator.nextInt(59);
                        int sec = randomGenerator.nextInt(59);
                        String temp_stime = "2011-"+mon+"-"+day+" "+hr+":"+min+":"+sec;
                        double temp_lat =52+randomGenerator.nextInt(20000)*0.000001;
                        double temp_log =-106-randomGenerator.nextInt(20000)*0.000001; 
                        int temp_status = randomGenerator.nextInt(1);
                        int temp_capacity = randomGenerator.nextInt(10);
                        int temp_share = randomGenerator.nextInt(1);
                        stmt.executeUpdate("INSERT INTO test.offer ( creator, start_time, start_lat, start_log, status, capacity,if_share)VALUES "
                                + "('"+temp_creator+"', '"+temp_stime+"', '"+temp_lat+"','"+temp_log+"','"+temp_status+"','"+temp_capacity+"','"+temp_share+"')"); 
                        out.print("date"+temp_stime);
                }
            } catch (Exception e) {
                out.print(e.toString());
            }
         
             * 
             */
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
