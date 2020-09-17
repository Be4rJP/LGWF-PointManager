package be4r.lgwf.pointmanager;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.apache.commons.io.IOUtils;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Be4rJP
 */
public class UUID {
    public static String getUuid(String name){
        String url = "https://api.mojang.com/users/profiles/minecraft/"+name;
        try {
            
            String UUIDJson = IOUtils.toString(new URL(url), "UTF-8");
            if(UUIDJson.isEmpty()) return "invalid name";                       
            JSONObject UUIDObject = (JSONObject) JSONValue.parseWithException(UUIDJson);
            return UUIDObject.get("id").toString();
            
        } catch (IOException e) {
        } catch (ParseException e) {
        }
       
        return "error";
    }
}
