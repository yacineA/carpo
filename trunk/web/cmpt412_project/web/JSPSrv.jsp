<?xml version="1.0" encoding="UTF-8"?>
<%@ page import="java.io.*, java.net.URL, java.net.URLConnection, org.json.JSONObject"
	language="java" contentType="text/xml; charset=UTF-8"
	pageEncoding="UTF-8"%>
<loginStat>
	<%
	String id=request.getParameter("id"); 
	//String username=request.getParameter("username"); 
	String token=request.getParameter("token");
	String userInfoStr="";
	boolean isLogged=false;
	try{
		URL url=new URL("https://graph.facebook.com/"+id+"?fields=username,updated_time&access_token="+token);
		URLConnection uc = url.openConnection();
		InputStreamReader reader = new InputStreamReader(new BufferedInputStream(url.openStream()));
		int c;
		while ((c = reader.read()) != -1)
			userInfoStr+=(char)c;
		reader.close();
	}catch(Exception e){
		//out.print("Error");
	}
	//out.print("<h1>"+name+id+"</h1>\n<h1>"+ token +"</h1>");
	try{
	JSONObject userInfo=new JSONObject(userInfoStr);
	//out.print("<h2>"+userInfo.get("updated_time").toString()+"</h2>");
	//out.print("<h2>"+userInfo.get("name").toString()+"</h2>");
	//if(userInfo.get("updated_time").toString()!=null&&userInfo.get("username").toString().equals(username))
	if(userInfo.get("updated_time").toString()!=null && userInfo.get("id").toString().equals(id))
            isLogged=true;
	}
	catch(Exception e2){
		//out.print("FALSE");
	}
        
            //out.print(isLogged);

	%>
        <Message>
            <%
                out.print(isLogged);
            %>
        </Message>
</loginStat>