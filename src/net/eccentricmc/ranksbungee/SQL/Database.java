package net.eccentricmc.ranksbungee.SQL;

import net.eccentricmc.ranksbungee.Utils.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static Connection connection;

    private static String host;

    private static String database;

    private static String username;

    private static String password;

    private static Integer port;

    public Database() {
        host = "136.243.23.144";
        database = "s24_ranks";
        username = "u24_iaxBbB8Qzb";
        password = "kUh8Qk1E63q9ugtGyR2IJuZE";
        port = Integer.valueOf(3306);
    }

    public void openConnection() {
        try {
            synchronized (this) {
                    Class.forName("com.mysql.jdbc.Driver");
                    Utils.sendDevMSG("Connecting...");
                    setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", username, password));
                    Utils.sendDevMSG("Connected to Database Successfully.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (connection != null)
                getConnection().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        return connection;
    }

    public void setConnection(Connection connection) {
        Database.connection = connection;
    }
}
