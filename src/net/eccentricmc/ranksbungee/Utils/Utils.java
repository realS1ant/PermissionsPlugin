package net.eccentricmc.ranksbungee.Utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.UUID;

import static net.md_5.bungee.api.ChatColor.translateAlternateColorCodes;

public class Utils {
    private static String prefix = ChatColor.translateAlternateColorCodes('&', "&a❖ &c&lDEV &8● &c");

    public static BaseComponent[] chat(String s) { return TextComponent.fromLegacyText(translateAlternateColorCodes('&', s)); }

    public static UUID uuidFromString(String s){
        StringBuilder sb = new StringBuilder(s);
        sb.insert(20, "-").insert(16, "-").insert(12, "-").insert(8, "-");
        return UUID.fromString(sb.toString());
    }

    public static void sendDevMSG(String msg){
        ProxyServer.getInstance().getPlayers().forEach(p -> {
            if(p.hasPermission("rank_dev") || p.hasPermission("rank_developer")){
                p.sendMessage(chat(prefix + msg));
            }
        });
        ProxyServer.getInstance().getConsole().sendMessage(chat(prefix + msg));
    }


}
