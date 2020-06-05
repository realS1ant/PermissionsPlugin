package net.eccentricmc.ranksspigot.PluginMessages;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.eccentricmc.ranksspigot.Main;
import org.bukkit.entity.Player;


public class PMSender {

    static public void requestPermissions(Player p){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PermissionsRequest");
        out.writeUTF(p.getName());
        System.out.println("Outgoing request for "+p.getName()+"'s permissions.");
        p.sendPluginMessage(Main.getInstance(), "er:permrequest", out.toByteArray());
    }

    static public void requestHighestRank(Player p){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("H-RRequest");
        out.writeUTF(p.getName());
        System.out.println("Outgoing request for "+p.getName()+"'s rank.");
        p.sendPluginMessage(Main.getInstance(), "er:rankrequest", out.toByteArray());
    }

}
