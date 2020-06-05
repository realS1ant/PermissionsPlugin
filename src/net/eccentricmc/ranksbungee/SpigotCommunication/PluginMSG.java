package net.eccentricmc.ranksbungee.SpigotCommunication;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.eccentricmc.ranksbungee.API.RanksAPI;
import net.eccentricmc.ranksbungee.Players.Players;
import net.eccentricmc.ranksbungee.Ranks.Rank;
import net.eccentricmc.ranksbungee.Ranks.Ranks;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PluginMSG implements Listener {
    public static void sendPlayerPerms(ProxiedPlayer p) {
        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("PermissionsFor");
            out.writeUTF(p.getName());
            for(String perm : p.getPermissions()){
                out.writeUTF(perm);
            }
            p.getServer().getInfo().sendData("er:updateplayer", out.toByteArray());
            ProxyServer.getInstance().getLogger().info("Outgoing permissions for "+p.getName());
        } catch(Exception e){
            e.printStackTrace();
            ProxyServer.getInstance().getLogger().severe("Error sending permissions to Spigot version.");
        }
    }

    public static void sendPlayerrank(ProxiedPlayer p){
        Rank r = RanksAPI.getInstance().getHighestRank(p.getUniqueId().toString().replaceAll("-", ""));
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("RankFor");
        out.writeUTF(p.getUniqueId().toString().replaceAll("-", ""));
        //ORDER Name, Prefix, shortPrefix, suffix, nameColor, ChatColor, weight
        out.writeUTF(r.getName());
        out.writeUTF(r.getPrefix());
        out.writeUTF(r.getShortPrefix());
        out.writeUTF(r.getSuffix());
        out.writeUTF(r.getNameColor());
        out.writeUTF(r.getChatColor());
        out.writeInt(r.getWeight());
        p.getServer().getInfo().sendData("er:rankrequest",out.toByteArray());
        System.out.println("Outgoing rank for: " + p.getName());
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent e){
        if(e.getTag().equalsIgnoreCase("er:permrequest")) {
            ByteArrayDataInput in = ByteStreams.newDataInput(e.getData());
            if (!in.readUTF().equalsIgnoreCase("PermissionsRequest")) return;
            String name = in.readUTF();
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(name);
            if (player == null || !player.isConnected()) return;
            System.out.println("Incoming request for " + name + "'s permissions.");
            sendPlayerPerms(player);
        }
        if(e.getTag().equalsIgnoreCase("er:rankrequest")) {
            ByteArrayDataInput in = ByteStreams.newDataInput(e.getData());
            if (!in.readUTF().equalsIgnoreCase("H-RRequest")) return;
            String name = in.readUTF();
            System.out.println("Incoming request for " + name + "'s rank.");
            sendPlayerrank(ProxyServer.getInstance().getPlayer(name));
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(name);
            if (player == null || !player.isConnected()) return;
        }
    }

}
