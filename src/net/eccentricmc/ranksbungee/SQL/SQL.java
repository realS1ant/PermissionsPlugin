package net.eccentricmc.ranksbungee.SQL;

public class SQL {
    public static void setup() {
        Database database = new Database();
        SQLPermissions.setTable("playerperms");
        database.openConnection();
    }
}
