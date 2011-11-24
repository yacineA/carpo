/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serverlet;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/**
 *
 * @author wen
 */
@WebServlet(name = "offers", urlPatterns = {"/offers"})
public class offers extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String id = request.getParameter("id");
        String token = request.getParameter("token");
        String userInfoStr = "";
        boolean isLogged = false;
        PrintWriter out = response.getWriter();
        try {



            try {
                URL url = new URL("https://graph.facebook.com/" + id + "?fields=updated_time&access_token=" + token);
                URLConnection uc = url.openConnection();
                InputStreamReader reader = new InputStreamReader(new BufferedInputStream(url.openStream()));
                int c;
                while ((c = reader.read()) != -1) {
                    userInfoStr += (char) c;
                }
                reader.close();
                url = null;
                reader = null;


            } catch (Exception e) {
                out.print(e.toString());
            }

            try {
                JSONObject userInfo = new JSONObject(userInfoStr);
                if (userInfo.get("updated_time").toString() != null) {
                    isLogged = true;
                }
                userInfo = null;
            } catch (Exception e) {
                out.print(e.toString());
            }
             
             
        } finally {
            
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            if(isLogged){
                out.println("<Offers>");
                try{
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con=DriverManager.getConnection("jdbc:mysql://70.64.6.83:3306/test","root","test");
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery("select * from offer");
                   
                    while(rs.next()){
                        int u_id = rs.getInt("id");
                        String e_creator = rs.getString("creator");
                        String e_stime = rs.getString("start_time");
                        String e_slat = rs.getString("start_lat");
                        String e_slon = rs.getString("start_log");
                        String u_status = rs.getString("status");
                        String u_capcity = rs.getString("capacity");
                        String u_share = rs.getString("if_share");
                        
                        out.println("<Offer>");                   
                        out.println("<id>"+u_id+"</id>");
                        out.println("<creator>"+e_creator+"</creator>");
                        out.println("<start_time>"+e_stime+"</start_time>");
                        out.println("<start_lat>"+e_slat+"</start_lat>");
                        out.println("<start_log>"+e_slon+"</start_log>");
                        out.println("<status>"+u_status+"</status>");
                        out.println("<capacity>"+u_capcity+"</capacity>");
                        out.println("<if_share>"+u_share+"if_share");
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
