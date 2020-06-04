package net.eccentricmc.ranksspigot;

import net.eccentricmc.ranksspigot.Listeners.JoinListener;
import net.eccentricmc.ranksspigot.PluginMessages.PMListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;
    @Override
    public void onEnable() {
        instance = this;
        getServer().getMessenger().registerOutgoingPluginChannel(this, "er:permrequest");
        getServer().getMessenger().registerIncomingPluginChannel(this, "er:updateplayer", new PMListener());
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
    }

    @Override
    public void onDisable() {
        PermissionsHandler.getInstance().disable();
    }

    public static Main getInstance(){
        return instance;
    }
}

