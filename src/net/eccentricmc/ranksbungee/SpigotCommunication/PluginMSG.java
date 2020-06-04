package net.eccentricmc.ranksbungee.SpigotCommunication;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
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

    @EventHandler
    public void onPluginMessage(PluginMessageEvent e){
        if(!e.getTag().equalsIgnoreCase("er:permrequest")) return;
        ByteArrayDataInput in = ByteStreams.newDataInput(e.getData());
        if(!in.readUTF().equalsIgnoreCase("PermissionsRequest")) return;
        String name = in.readUTF();
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(name);
        if(player == null || !player.isConnected()) return;
        System.out.println("Incoming request for "+name+"'s permissions.");
        sendPlayerPerms(player);

    }
}
