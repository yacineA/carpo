/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serverlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.net.URL;
import java.net.HttpURLConnection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import superclass.Events;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

/**
 *
 * @author wen
 */
@WebServlet(name = "requests_IN", urlPatterns = {"/requests_in"})
public class requests_IN extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String id = request.getParameter("id");
        String token = request.getParameter("token");
        String stime = request.getParameter("start_time");
        String slat = request.getParameter("start_lat");
        String slog = request.getParameter("start_log");
        String status = request.getParameter("status");
        String creator = request.getParameter("creator");
        String elat = request.getParameter("end_lat");
        String elog = request.getParameter("end_log");
        PrintWriter out = response.getWriter();
        boolean isLogged = false;
        String start_address="";
        String end_address="";
         
        try {
            Events events = new Events();
            isLogged = events.verify_token(id, token);
        }catch (Exception e){
            
        }finally {
            if (isLogged) {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con = DriverManager.getConnection("jdbc:mysql://70.64.6.83:3306/test", "root", "test");
                    Statement stmt = con.createStatement();
                    
                    stmt.executeUpdate("INSERT INTO test.offer ( id, start_time, start_lat, start_log, status, creator, end_lat, end_log, start_address, end_address)VALUES "
                                + "('"+id+"', '"+stime+"','"+slat+"','"+slog+"','"+status+"', '"+creator+"', '"+elat+"', '"+elog+"', '" + start_address + "', '" + end_address + "')");
                    out.println("<Message>");
                    out.println("true");
                    out.println("</Message");
                } catch (Exception e) {
                }

            } else {
                
                out.println("<Error>");
                out.println("<id>"+id+"</id>");
                out.println("</Error>");
                
            }
            out.close();
        }
    }
    
    private String convert(String lat, String log){
        String address="";
         URL url = null;

        HttpURLConnection httpurlconnection = null;

        try {
            url = new URL("http://maps.google.com/maps/api/geocode/xml?latlng="+lat+","+log+"&sensor=true");
            
            httpurlconnection = (HttpURLConnection) url.openConnection();
            httpurlconnection.setDoOutput(true);
            httpurlconnection.setRequestMethod("GET");
            
            BufferedReader in = null;
            StringBuffer sb = new StringBuffer();
            in = new BufferedReader(new InputStreamReader(httpurlconnection.getInputStream()));
            String inputLine;
            while((inputLine = in.readLine())!=null){
                sb.append(inputLine);
            }
            
            StringReader read = new StringReader(sb.toString());
            InputSource source = new InputSource(read);
            SAXBuilder sax = new SAXBuilder();
            
            Document doc = sax.build(source);
            Element root = doc.getRootElement();
            address = root.getChild("result").getChildText("formatted_address");
            
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(httpurlconnection != null){
                httpurlconnection.disconnect();
            }
        }
        return address;
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
