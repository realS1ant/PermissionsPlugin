package net.eccentricmc.ranksbungee.Cache;

import net.eccentricmc.ranksbungee.Main;
import net.eccentricmc.ranksbungee.SQL.SQLPermissions;
import net.eccentricmc.ranksbungee.Utils.MojangAPI;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class CachedPerms {
    Logger logger = Main.getInstance().getLogger();

    HashMap<String, ArrayList<String>> playerPermsCache;
    List<String> globalPermsCache;
    /*
       * Storage Method
       * Hashmap<str, list>
       *  str = Player's trimmed UUID (without "-")
       *  list = List of that player's permissions ["rank_default", "super_admin", "ADMIN_NOTIFICATIONS"] etc.
     */


    public void loadCache(){
        try {
            playerPermsCache = SQLPermissions.getAllPlayerPerms();
        } catch (SQLException e){
            logger.severe("Error loading cache with player's permissions. Error: ");
            e.printStackTrace();
            logger.severe("Error loading cache with player's permissions.");
        }
    }

    public void loadGPermsCache(){
        try {
            globalPermsCache = SQLPermissions.getAllPermisions();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error caching global permissions.");
        }
    }

    public List<String> getGloablPermissions(){
        return globalPermsCache;
    }

    public void addGlobalPerm(String perm){
        globalPermsCache.add(perm);
        try {
            SQLPermissions.addGlobalPerm(perm);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error adding global permission: " + perm);
        }
    }


    public void addPermission(String uuid, String perm){
        uuid = uuid.replaceAll("-", "");
        if(playerPermsCache.containsKey(uuid)){
            playerPermsCache.get(uuid).add(perm);
        }else{
            ArrayList<String> l = new ArrayList<>();
            l.add(perm);
            playerPermsCache.put(uuid, l);
        }
        try {
            SQLPermissions.addPermission(uuid, perm);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error adding permission to:\nUUID: " + uuid + "\nPermission: " + perm);
        }
    }
    public void removePermission(String uuid, String perm){
        uuid = uuid.replaceAll("-", "");
        if(!playerPermsCache.containsKey(uuid)) return;
        playerPermsCache.get(uuid).remove(perm);
        try {
            SQLPermissions.removePermission(uuid, perm);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error removinb permission from:\nUUID: " + uuid + "\nPermission: " + perm);
            System.out.println("Player name: " + MojangAPI.getName(uuid));
        }
    }

    public boolean hasPermission(String uuid, String perm){
        if(!playerPermsCache.containsKey(uuid.replaceAll("-", ""))) return false;
        return playerPermsCache.get(uuid.replaceAll("-", "")).contains(perm);
    }

    public List<String> getPermissions(String uuid){
        if(!playerPermsCache.containsKey(uuid.replaceAll("-", ""))) return null;
        return playerPermsCache.get(uuid.replaceAll("-", ""));
    }

    // Gets all players with certain permission - used for /ep players <rank> (I was confused when I looked back at why
    // I had this method)
    // I imagine this method could lag a bit once we have lots of logins.
    public List<String> getAllPlayers(String perm){
        ArrayList<String> list = new ArrayList<>();
        for(String key : playerPermsCache.keySet()){
            if(playerPermsCache.get(key).contains(perm)){
                list.add(MojangAPI.getName(key));
            }
        }
        return list;
    }

    private static CachedPerms instance;
    public static CachedPerms getInstance(){
        if(instance == null){
            instance = new CachedPerms();
        }
        return instance;
    }
}
