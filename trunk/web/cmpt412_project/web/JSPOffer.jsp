<?xml version="1.0" encoding="UTF-8"?>
<%@ page import="java.io.*, java.net.URL, java.net.URLConnection, org.json.JSONObject,java.sql.*"
	language="java" contentType="text/xml; charset=UTF-8"
	pageEncoding="UTF-8"%>
<makeOffer><%
    
    String name = "Wen";
    try{
        
        String connectionURL = "jdbc:mysql://70.64.6.83:3306/test";
        Connection connection = null;
        Statement stmt;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        connection = DriverManager.getConnection(connectionURL,"root","test");
        if(!connection.isClosed()){
            out.println("Database connect successful");
        }
        stmt = connection.createStatement();
       // stmt.executeUpdate("insert into books_details(book_name,author) values('"+bookname+"','"+author+"')");
       stmt .executeUpdate("insert into test(name) values('"+name+"')");
        //connection.close();
    }catch(Exception e){
        out.println("Database connect failed");
    }
        
    %>
</makeOffer>