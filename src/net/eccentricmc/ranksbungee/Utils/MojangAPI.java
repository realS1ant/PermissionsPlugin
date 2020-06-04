package net.eccentricmc.ranksbungee.Utils;

import net.eccentricmc.ranksbungee.Main;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class MojangAPI {
    public static String getUUID(String username) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + username);
            URLConnection con = url.openConnection();
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(
                    new InputStreamReader((InputStream) con.getContent(), StandardCharsets.UTF_8)
            );
            return String.valueOf(jsonObject.get("id"));
        } catch(Exception e){
            Main.getInstance().getLogger().severe("[SQL] Mojang API either down, or player non-existent");
            e.printStackTrace();
            return null;
        }
    }
    public static String getName(String uuid) {
        try {
            URL url = new URL("https://api.mojang.com/user/profiles/" + uuid + "/names");
            URLConnection con = url.openConnection();
            JSONParser jsonParser = new JSONParser();
            JSONArray jsonArray = (JSONArray) jsonParser.parse(new InputStreamReader((InputStream) con.getContent(), StandardCharsets.UTF_8));
            JSONObject jsonObj = (JSONObject) jsonArray.get(jsonArray.size()-1);
            return jsonObj.get("name").toString();
        } catch(Exception e){
            Main.getInstance().getLogger().severe("[SQL] Mojang API either down, or player non-existent");
            e.printStackTrace();
            return null;
        }
    }
}
