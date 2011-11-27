/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serverlet;

import java.sql.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
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
@WebServlet(name = "rating_IN", urlPatterns = {"/rating_in"})
public class rating_IN extends HttpServlet {

   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        boolean isLogged = false;
        String id = request.getParameter("id");
        String token = request.getParameter("token");
        String rating_mark = request.getParameter("rate");
        String ratee_id = request.getParameter("ratee_id");
        String comment = request.getParameter("comment");
        int counter = 0;
        double temp = 0;
        double temp1;
        try {
            Events events = new Events();
            isLogged=events.verify_token(id, token);
            
        } catch(Exception e){
            e.printStackTrace();
        }finally {     
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            if(isLogged){
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con = DriverManager.getConnection("jdbc:mysql://70.64.6.83:3306/test", "root", "test");                    
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT rating FROM rating WHERE user_id ='"+id+"'");                    
                    while(rs.next()){
                        String r = rs.getString("rating");
                        temp = temp + Float.valueOf(r.trim()).floatValue();
                        counter++;
                    }
                    
                    temp = temp/counter;
                    out.print("rator avg = "+temp);
                    temp1 = Float.valueOf(rating_mark.trim()).floatValue();
                    
                    temp = Math.sqrt(temp1 * temp);
                    rating_mark = Double.toString(temp);
                    out.print("rating mark = "+rating_mark);
                    stmt.executeUpdate("INSERT INTO test.rating (user_id, rating, comment) VALUES"
                            +"('"+ratee_id+"','"+rating_mark+"','"+comment+"')");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                
                out.println("<Message>");
                out.println(true);
                out.println("</Message>");
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


//http://localhost:8080/cmpt412_project/rating_in?id=703521397&token=AAACEdEose0cBAHoK1YgNBlLLf1cdlKYtI7FbC1QvZCqg8aJiMP6Au8LYLfMyc7ZBkZBJXZCiWvLOPf57w4QmgXDHQJ0xVMmHZAtnZAIwjEqAZDZD&rate=3&ratee_id=722