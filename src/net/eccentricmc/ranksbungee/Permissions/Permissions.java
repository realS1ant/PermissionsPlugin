package net.eccentricmc.ranksbungee.Permissions;

import net.eccentricmc.ranksbungee.Cache.CachedPerms;
import net.eccentricmc.ranksbungee.Main;
import net.eccentricmc.ranksbungee.Players.Players;
import net.eccentricmc.ranksbungee.Ranks.Rank;
import net.eccentricmc.ranksbungee.Ranks.Ranks;
import net.eccentricmc.ranksbungee.SQL.SQLPermissions;
import net.eccentricmc.ranksbungee.SpigotCommunication.PluginMSG;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Permissions {

    private Main main = Main.getInstance();
    private Players playersClass = new Players();
    private Ranks ranks = Ranks.getInstance();
    private CachedPerms cachedPerms = CachedPerms.getInstance();

    public void setUpAllPerms(){
        List<String> allPerms = null;
        try {
            allPerms = SQLPermissions.getAllPermisions();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(allPerms == null){
                System.out.println("No Global permissions, adding all!");
                allPerms = new ArrayList<>();
            }
            //Adding Global perms
            for (Rank r : ranks.getRanks()) {
                for(String perm : r.getPermissions()){
                    if(!(perm.startsWith("-")) && !allPerms.contains(perm) && !perm.equals("*")){
                        main.getLogger().info("Adding global permission: " + perm);
                        try {
                            SQLPermissions.addGlobalPerm(perm);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        allPerms.add(perm);
                    }
                }
            }
        cachedPerms.loadGPermsCache();
    }

    public void setUp(ProxiedPlayer p) {
        try {
            List<String> playersCachedPerms = getPermissions(p.getUniqueId());
            if(playersCachedPerms == null) playersCachedPerms = new ArrayList<>();
            playersClass.addRank(p.getUniqueId().toString(), ranks.getDefaultRank());
            List<String> playersPerms = new ArrayList<>(playersCachedPerms);
            List<String> ranksList = playersClass.getRanks(p.getUniqueId());
            List<String> globalPerms = cachedPerms.getGloablPermissions();
            if(!playersPerms.contains("rank_default")) {
                playersPerms.add("rank_default");
            }
            if(globalPerms == null) globalPerms = new ArrayList<>();
            for(String rank : ranksList){
                for(String perm : ranks.getRankByName(rank).getPermissions()){
                    perm = perm.toLowerCase();
                    if(!playersPerms.contains(perm)) playersPerms.add(perm);
                }
            }
            if(!(playersPerms.contains("*"))){
                 for(String perm : playersPerms){
                     perm = perm.toLowerCase();
                     if(!perm.startsWith("-")){
                         p.setPermission(perm, true);
                         if(!globalPerms.contains(perm)) cachedPerms.addGlobalPerm(perm);
                     } else {
                         p.setPermission(perm.replaceFirst("-", ""), false);
                     }
                 }
                 for(String perm : globalPerms){
                     perm = perm.toLowerCase();
                     if(!(playersPerms.contains(perm))) {
                         p.setPermission(perm, false);
                     }
                 }
            } else {
                 for(String perm : globalPerms){
                     perm = perm.toLowerCase();
                     p.setPermission(perm, true);
                 }
                 for(String perm : playersPerms){
                     perm = perm.toLowerCase();
                     if(perm.startsWith("-") && !perm.equalsIgnoreCase("*")) p.setPermission(perm.replaceFirst("-", ""), false);
                 }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //Returns list of permissions as string.
    public List<String> getPermissions(UUID uuid){
        return cachedPerms.getPermissions(uuid.toString());
    }

    private static Permissions instance;
    public static Permissions getInstance(){
        if(instance == null){
            instance = new Permissions();
        }
        return instance;
    }

}
