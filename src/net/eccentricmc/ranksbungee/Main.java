package net.eccentricmc.ranksbungee;

import net.eccentricmc.ranksbungee.Cache.CachedPerms;
import net.eccentricmc.ranksbungee.Listeners.JoinListener;
import net.eccentricmc.ranksbungee.Permissions.Permissions;
import net.eccentricmc.ranksbungee.Ranks.Ranks;
import net.eccentricmc.ranksbungee.Ranks.RanksConfig;
import net.eccentricmc.ranksbungee.SQL.Database;
import net.eccentricmc.ranksbungee.SQL.SQL;
import net.md_5.bungee.api.plugin.Plugin;

public class Main extends Plugin {

    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        SQL.setup();
        getLogger().finest("Enabled Successfully. ");
        getProxy().getPluginManager().registerCommand(this, new RankCommand());
        getProxy().getPluginManager().registerListener(this, new JoinListener());
        getProxy().registerChannel("upr:updateplayer");
        RanksConfig.getInstance().create();
        Ranks.getInstance().establish();
        CachedPerms.getInstance().loadCache();
        Permissions.getInstance().setUpAllPerms();
    }

    @Override
    public void onDisable() {
        new Database().closeConnection();
    }

    public static Main getInstance(){
        return instance;
    }
}
