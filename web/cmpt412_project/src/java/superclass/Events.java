/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package superclass;

import java.io.BufferedInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONObject;




/**
 *
 * @author wen
 */
public class Events {
  
   public boolean verify_token(String id, String token){
       boolean isLogged = false;
       String userInfoStr = "";
       //System.out.println("Hello");
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
                
            }

            try {
                JSONObject userInfo = new JSONObject(userInfoStr);
                if (userInfo.get("updated_time").toString() != null) {
                    isLogged = true;
                    
                }
                userInfo = null;
            } catch (Exception e) {
                String a= "";
                a= "b";
            }
       return isLogged;
       
   }
}
