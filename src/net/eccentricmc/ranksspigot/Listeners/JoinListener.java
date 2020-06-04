package net.eccentricmc.ranksspigot.Listeners;

import net.eccentricmc.ranksspigot.Main;
import net.eccentricmc.ranksspigot.PermissionsHandler;
import net.eccentricmc.ranksspigot.PluginMessages.PMSender;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Bukkit.getScheduler().runTaskLater(Main.getPlugin(
                Main.getInstance().getClass()),
                () -> {PMSender.requestPermissions(e.getPlayer());},
                10
        );
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        PermissionsHandler.getInstance().removeAttach(e.getPlayer().getUniqueId());
    }
    @EventHandler
    public void onLeave(PlayerKickEvent e){
        PermissionsHandler.getInstance().removeAttach(e.getPlayer().getUniqueId());
    }
}