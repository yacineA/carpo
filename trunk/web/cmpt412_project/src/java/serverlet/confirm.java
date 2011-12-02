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
 * @required id, token, updateStatus, pendingId
 */
@WebServlet(name = "confirm", urlPatterns = {"/confirm"})
public class confirm extends HttpServlet {

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
        out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Result>");
        try {
            if (request.getParameter("id") == null || request.getParameter("token") == null || request.getParameter("pendingId") == null || request.getParameter("updateStatus") == null) {
                throw new Exception("Missing parameters!");
            }
            int id = Integer.getInteger(request.getParameter("id"));
            String token = request.getParameter("token");
            String pendingId = request.getParameter("pendingId");
            int updateStatus = Integer.getInteger(request.getParameter("updateStatus"));
            if (updateStatus == 3||updateStatus==1) {
                throw new Exception("status parameter error");
            }
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
                throw new Exception(eF.toString() + " Facebook connection Error!");
            }
            JSONObject userInfo = new JSONObject(userInfoStr);
            //out.print("<h2>"+userInfo.get("updated_time").toString()+"</h2>");
            //out.print("<h2>"+userInfo.get("name").toString()+"</h2>");
            //if(userInfo.get("updated_time").toString()!=null&&userInfo.get("username").toString().equals(username))
            if (userInfo.get("updated_time").toString() == null || !userInfo.get("id").toString().equals(id)) {
                throw new Exception("Facebook verification Error!");
            }
            userInfo = null;
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://70.64.6.83:3306/test", "root", "test");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * from user_info where id=" + id);
            if (!rs.next()) {
                throw new Exception("User not registered.");
            }
            rs = stmt.executeQuery("SELECT * from test.joined_passenger where id=" + pendingId);
            if (rs.getInt("status") == 3) {//Passenger confirm driver's invitation
                if (rs.getInt("user_id") != id) {
                    throw new Exception("user id in pending does not match to passenger id");
                }
                stmt.executeUpdate("UPDATE test.joined_passenger SET `status` = " + updateStatus + " WHERE id =" + pendingId);
            } else {//Driver confirms passenger's request
                String offerId = rs.getString("offer_id");
                rs = stmt.executeQuery("Select * from test.offer wher id=" + offerId);
                if (id != rs.getInt("creator")) {
                    throw new Exception("You are not the driver who created the offer related to this pending.");
                } else {
                    stmt.executeUpdate("UPDATE test.joined_passenger SET `status` = " + updateStatus + " WHERE id =" + pendingId + " AND status<>3");
                    out.print("<Message>true</Message>");
                }

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
