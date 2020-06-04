package net.eccentricmc.ranksbungee.SQL;

import net.eccentricmc.ranksbungee.Cache.CachedPerms;
import net.eccentricmc.ranksbungee.Utils.MojangAPI;
import net.eccentricmc.ranksbungee.Utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SQLPermissions {
    private static Database database = new Database();

    private static String table;

    public static String getTable() {
        return table;
    }

    public static void setTable(String table) {
        SQLPermissions.table = table;
    }

    public static Connection getConnection() throws SQLException {
        if(!database.getConnection().isValid(4)) {
            Utils.sendDevMSG("connection not valid, reconnecting.");
            database.openConnection();
        }
        return database.getConnection();
    }

    public static HashMap<String, ArrayList<String>> getAllPlayerPerms() throws SQLException {
        PreparedStatement s = getConnection().prepareStatement("SELECT * FROM playerperms");
        ResultSet results = s.executeQuery();
        HashMap<String, ArrayList<String>> list = new HashMap<>();
        while(results.next()){
            String UUID = results.getString("UUID");
            String perm = results.getString("Permission");
            if(list.keySet().contains(UUID)){
                list.get(UUID).add(perm);
            } else{
                ArrayList<String> l = new ArrayList<>();
                l.add(perm);
                list.put(UUID, l);
            }
        }
        return list;
    }

    public static void addPermission(UUID uuid, String permission) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement("INSERT INTO playerperms (UUID,Permission) VALUES (?,?)");
        statement.setString(1, uuid.toString().replaceAll("-", ""));
        statement.setString(2, permission);
        statement.executeUpdate();
    }

    public static void addPermission(String uuid, String permission) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement("INSERT INTO playerperms (UUID,Permission) VALUES (?,?)");
        statement.setString(1, uuid.replaceAll("-", ""));
        statement.setString(2, permission);
        statement.executeUpdate();
    }

    public static void removePermission(UUID uuid, String permission) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement("DELETE FROM playerperms WHERE UUID=? AND Permission LIKE ?");
        statement.setString(1, uuid.toString().replaceAll("-", ""));
        statement.setString(2, permission);
        statement.execute();
    }

    public static void removePermission(String uuid, String permission) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement("DELETE FROM playerperms WHERE UUID=? AND Permission LIKE ?");
        statement.setString(1, uuid.replaceAll("-", ""));
        statement.setString(2, permission);
        statement.execute();
    }

    public static List<String> getAllPermisions() throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM perms");
        ResultSet result = statement.executeQuery();
        List<String> perms = new ArrayList<>();
        while(result.next()){
            perms.add(result.getString("permission"));
        }
        if (perms.size() == 0) {
            getConnection().close();
            return null;
        }
        return perms;

    }

    public static void addGlobalPerm(String permission) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement("INSERT INTO perms (permission) VALUES (?)");
        statement.setString(1, permission);
        statement.executeUpdate();
    }
}
