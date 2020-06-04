package net.eccentricmc.ranksbungee.Utils;

import net.eccentricmc.ranksbungee.Main;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.YamlConfiguration;

public class SpecialCharacters {
    //private static YamlConfiguration config = YamlConfiguration.loadConfiguration(Main.getInstance().getResource("symbols.yml"));
    private static Configuration config = YamlConfiguration.getProvider(YamlConfiguration.class).load(Main.getInstance().getResourceAsStream("symbols.yml"));
    public static String getSymbol(String symbolName){ return config.getString("Symbols." + symbolName); }
    public static String getEmote(String emoteName){ return config.getString("Emotes." + emoteName); }
}
