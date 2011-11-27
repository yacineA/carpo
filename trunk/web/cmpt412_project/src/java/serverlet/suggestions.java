/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serverlet;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONObject;

/**
 *
 * @author Executor
 */
@WebServlet(name = "suggestions", urlPatterns = {"/suggestions"})
public class suggestions extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        float currentLat=91.0f;//an invalid initial value
        float currentLong=181.0f;//an invalid initial value
        int countOffer=-1;
        int countRequest=-1;
        if(request.getParameter("lat")!=null)
            currentLat=Float.parseFloat(request.getParameter("lat"));
        if(request.getParameter("long")!=null)
            currentLong=Float.parseFloat(request.getParameter("long"));
        if(request.getParameter("countOffer")!=null)
            countOffer=Integer.parseInt(request.getParameter("countOffer"));
        if(request.getParameter("countRequest")!=null)
            countRequest=Integer.parseInt(request.getParameter("countRequest"));
        String id = request.getParameter("id");
        String token = request.getParameter("token");
        String userInfoStr = "";
        boolean isLogged = false;
        try {
            URL url = new URL("https://graph.facebook.com/" + id + "?fields=username,updated_time&access_token=" + token);
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
            //out.print("Error");
        }
        //out.print("<h1>"+name+id+"</h1>\n<h1>"+ token +"</h1>");
        try {
            JSONObject userInfo = new JSONObject(userInfoStr);
            //out.print("<h2>"+userInfo.get("updated_time").toString()+"</h2>");
            //out.print("<h2>"+userInfo.get("name").toString()+"</h2>");
            //if(userInfo.get("updated_time").toString()!=null&&userInfo.get("username").toString().equals(username))
            if (userInfo.get("updated_time").toString() != null && userInfo.get("id").toString().equals(id)) {
                isLogged = true;
            }
            userInfo = null;
        } catch (Exception e2) {
            //out.print("FALSE");
        } finally {
            out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            if(isLogged){
                out.print("<Suggestions>");
                try{
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con=DriverManager.getConnection("jdbc:mysql://70.64.6.83:3306/test","root","test");
                    Statement stmt=con.createStatement();
                    ResultSet rs=null;
                     double currentLatDown;
                        double currentLatUp;
                        double currentLongLeft;
                        double currentLongRight;
                    if(currentLat>=-90.0f&&currentLat<=90.0f&&currentLong>=-180.0f&&currentLong<=180.0f){
                        currentLatDown=currentLat-0.01;
                        currentLatUp=currentLat+0.01;
                        currentLongLeft=currentLong-0.01;
                        currentLongRight=currentLong+0.01;
                        if (countOffer<0)   
                            rs=stmt.executeQuery("select * from offer where start_lat>"+currentLatDown+" AND start_lat<"+currentLatUp+" AND start_log>"+currentLongLeft+" AND start_log<"+currentLongRight);
                        else
                            rs=stmt.executeQuery("select * from offer where start_lat>"+currentLatDown+" AND start_lat<"+currentLatUp+" AND start_log>"+currentLongLeft+" AND start_log<"+currentLongRight+" LIMIT "+countOffer);
                        
                    }else{
                        if(countOffer<0)
                            rs=stmt.executeQuery("select * from offer");
                        else
                            rs=stmt.executeQuery("select * from offer LIMIT "+countOffer);
                       }
                    while(rs.next()){
                        out.print("<Suggestion>");
                            out.print("<Type>Offer</Type>");
                            String tmpStr=rs.getString("id");
                            out.print("<ID>"+tmpStr+"</ID>");
                            tmpStr=rs.getString("creator");
                            out.print("<Creator>"+tmpStr+"</Creator>");
                            tmpStr=rs.getString("start_time");
                            out.print("<StartTime>"+tmpStr+"</StartTime>");
                            tmpStr=rs.getString("start_lat");
                            out.print("<StartLatitude>"+tmpStr+"</StartLatitude>");
                            tmpStr=rs.getString("start_log");
                            out.print("<StartLongitude>"+tmpStr+"</StartLongitude>");
                            tmpStr=rs.getString("status");
                            out.print("<Status>"+tmpStr+"</Status>");
                            tmpStr=rs.getString("capacity");
                            out.print("<Capacity>"+tmpStr+"</Capacity>");
                            tmpStr=rs.getString("if_share");
                            out.print("<Shared>"+tmpStr+"</Shared>");
                        out.print("</Suggestion>");
                    }
                    if(currentLat>=-90.0f&&currentLat<=90.0f&&currentLong>=-180.0f&&currentLong<=180.0f){
                        currentLatDown=currentLat-0.01;
                        currentLatUp=currentLat+0.01;
                        currentLongLeft=currentLong-0.01;
                        currentLongRight=currentLong+0.01;
                        if (countRequest<0)   
                            rs=stmt.executeQuery("select * from request where start_lat>"+currentLatDown+" AND start_lat<"+currentLatUp+" AND start_log>"+currentLongLeft+" AND start_log<"+currentLongRight);
                        else
                            rs=stmt.executeQuery("select * from request where start_lat>"+currentLatDown+" AND start_lat<"+currentLatUp+" AND start_log>"+currentLongLeft+" AND start_log<"+currentLongRight+" LIMIT "+countRequest);
                        
                    }else{
                          if(countRequest<0)
                            rs=stmt.executeQuery("select * from request");
                        else
                            rs=stmt.executeQuery("select * from request LIMIT "+countRequest);
                    }
                    while(rs.next()){
                        out.print("<Suggestion>");
                            out.print("<Type>Request</Type>");
                            String tmpStr=rs.getString("id");
                            out.print("<ID>"+tmpStr+"</ID>");
                            tmpStr=rs.getString("creator");
                            out.print("<Creator>"+tmpStr+"</Creator>");
                            tmpStr=rs.getString("start_time");
                            out.print("<StartTime>"+tmpStr+"</StartTime>");
                            tmpStr=rs.getString("start_lat");
                            out.print("<StartLatitude>"+tmpStr+"</StartLatitude>");
                            tmpStr=rs.getString("start_log");
                            out.print("<StartLongitude>"+tmpStr+"</StartLongitude>");
                            tmpStr=rs.getString("status");
                            out.print("<Status>"+tmpStr+"</Status>");
                            tmpStr=rs.getString("end_lat");
                            out.print("<EndLatitude>"+tmpStr+"</EndLatitude>");
                            tmpStr=rs.getString("end_log");
                            out.print("<EndLongitude>"+tmpStr+"</EndLongitude>");
                        out.print("</Suggestion>");
                    }
                }catch(Exception sqle){
                    out.print(sqle.getMessage());
                }
                out.print("</Suggestions>");
            }else{
                 out.print("<Error><ID>1</ID></Error>");
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
