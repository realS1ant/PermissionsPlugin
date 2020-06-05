package net.eccentricmc.ranksspigot.PluginMessages;

import net.eccentricmc.ranksspigot.API.RanksAPI;
import net.eccentricmc.ranksspigot.PermissionsHandler;
import net.eccentricmc.ranksspigot.Ranks.Rank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;


public class PMListener implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player wrongplayer, byte[] input) {
        if(channel.equalsIgnoreCase("er:updateplayer")) {
            ArrayList<String> perms = new ArrayList<>();
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(input));
            try {
                if (!in.readUTF().equalsIgnoreCase("PermissionsFor")) return;
                String name = in.readUTF();
                Player pl = Bukkit.getPlayer(name);
                if (!Bukkit.getOnlinePlayers().contains(pl)) return;
                System.out.println("Incoming permissions for " + name + ".");
                while (in.available() > 0) {
                    perms.add(in.readUTF());
                }
                PermissionsHandler.getInstance().setUp(pl, perms);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(channel.equalsIgnoreCase("er:rankrequest")){
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(input));
            try {
                if (!in.readUTF().equalsIgnoreCase("RankFor")) return;
                String uuid = in.readUTF();
                //ORDER Name, Prefix, shortPrefix, suffix, nameColor, ChatColor, weight
                Rank rank = new Rank(in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(),
                        in.readUTF(), in.readUTF(), in.readInt(), null);
                RanksAPI.getInstance().a(uuid, rank);
                System.out.println("Incoming rank for: " + wrongplayer.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
