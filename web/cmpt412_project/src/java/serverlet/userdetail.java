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
@WebServlet(name = "userdetail", urlPatterns = {"/userdetail"})
public class userdetail extends HttpServlet {

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
        out.print("<Result>");
        try {
            if (request.getParameter("id") == null || request.getParameter("token") == null || request.getParameter("requestId") == null) {
                throw new Exception("Missing parameters!");
            }
            String id = request.getParameter("id");
            String token = request.getParameter("token");
            String requestId = request.getParameter("requestId");
            String userInfoStr = "";
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
            } catch (Exception eF) {
                throw new Exception(eF.toString() + " Facebook verification Error!");
            }
            Boolean isLogged = false;
            JSONObject userInfo = new JSONObject(userInfoStr);
            //out.print("<h2>"+userInfo.get("updated_time").toString()+"</h2>");
            //out.print("<h2>"+userInfo.get("name").toString()+"</h2>");
            //if(userInfo.get("updated_time").toString()!=null&&userInfo.get("username").toString().equals(username))
            if (userInfo.get("updated_time").toString() != null && userInfo.get("id").toString().equals(id)) {
                isLogged = true;
            }
            userInfo = null;
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://70.64.6.83:3306/test", "root", "test");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * from user_info where id=" + id);
            if (!rs.first()) {
                throw new Exception("User not registered.");
            }
            rs = stmt.executeQuery("SELECT * from test.user_info where id=" + requestId);
            if (!rs.first()) {
                throw new Exception("User with id " + requestId + " not found.");
            }
            rs = stmt.executeQuery("SELECT * from offer where creator=" + requestId);
            if (!rs.first()) {
                throw new Exception("The requested user does not have any offer records.");
            }
            int offerId[] = null;
            int i = 0;
            do {
                offerId[i] = rs.getInt("id");
                i++;
            } while (rs.next());
            rs = stmt.executeQuery("SELECT * from joined_passenger where user_id=" + id + " AND status=0");
            boolean permitted = false;
            while (rs.next()) {
                for (int j = 0; j < i + 1; j++) {
                    if (rs.getInt("offer_id") == offerId[j]) {
                        permitted = true;
                        break;
                    }
                }
            }
            if (permitted) {
                out.print("<User>");
                String tmpStr = rs.getString("id");
                out.print("<ID>" + tmpStr + "</ID>");
                tmpStr = rs.getString("name");
                out.print("<Name>" + tmpStr + "</Name>");
                tmpStr = rs.getString("email");
                out.print("<Email>" + tmpStr + "</Email>");
                tmpStr = rs.getString("status");
                out.print("<Status>" + tmpStr + "</Status>");
                tmpStr = rs.getString("type_code");
                out.print("<TypeCode>" + tmpStr + "</TypeCode>");
                tmpStr = rs.getString("driver_rating");
                out.print("<DriverRating>" + tmpStr + "</DriverRating>");
                tmpStr = rs.getString("PsgRating");
                out.print("<PsgRating>" + tmpStr + "</PsgRating>");
                out.print("</User>");
            }else{
                throw new Exception("You are not allowed to view the information for this user.");
            }
        } catch (Exception e) {
            out.print("<Error>" + e.toString() + "</Error>");
        } finally {
            out.print("</Result>");
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
