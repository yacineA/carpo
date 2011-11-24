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
import java.net.URL;
import java.net.URLConnection;
import java.util.NoSuchElementException;
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
@WebServlet(name = "loginStat", urlPatterns = {"/loginStat"})
public class loginStat extends HttpServlet {

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
        String id = request.getParameter("id");
        String username = request.getParameter("username");
        String token = request.getParameter("token");
        String userInfoStr = "";
        boolean isLogged = false;
        try {
            URL url = new URL("https://graph.facebook.com/" + id + "?fields=username,email,updated_time&access_token=" + token);
            URLConnection uc = url.openConnection();
            InputStreamReader reader = new InputStreamReader(new BufferedInputStream(url.openStream()));
            int c;
            while ((c = reader.read()) != -1) {
                userInfoStr += (char) c;
            }
            reader.close();
            url = null;
            reader = null;
            //out.print(userInfoStr);
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
                try{
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con=DriverManager.getConnection("jdbc:mysql://70.64.6.83:3306/test","root","test");
                    Statement stmt=con.createStatement();
                    ResultSet rs=stmt.executeQuery("select * from user_info where id="+id);
                  
                    if(!rs.next()){
                        try{
                            username=userInfo.getString("username");
                        }catch(java.util.NoSuchElementException nse){
                            username="";
                        }
                        String email=userInfo.getString("email");
                        stmt.executeUpdate("INSERT INTO test.user_info (id, name, email, status, type_code, driver_rating, passenger_rating)VALUES ("+id+", '"+username+"', '"+email+"', 1, 0, DEFAULT, DEFAULT)");
                    }
                    
                     
                }catch(Exception sqle){
                    //out.print(sqle.toString());
                }
            }
            userInfo = null;
        } catch (Exception e2) {
            //out.print("FALSE");
        } finally {
            out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            out.print("<LoginStat><Message>");
            out.print(isLogged);
            out.print("</Message></LoginStat>");
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
