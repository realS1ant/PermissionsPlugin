package net.eccentricmc.ranksbungee.SpigotCommunication;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PluginMSG {
    public static void sendPlayerPerms(ProxiedPlayer p) {
        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("UpdatePlayer");
            out.writeUTF(p.getName());
            p.getServer().getInfo().sendData("upr:updateplayer", out.toByteArray());
            ProxyServer.getInstance().getLogger().info("Sent message to update " + p.getName() + "'s permissions.");
        } catch(Exception e){
            e.printStackTrace();
            ProxyServer.getInstance().getLogger().severe("Error sending permissions to Spigot version.");
        }
    }
}
