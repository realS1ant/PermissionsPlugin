package net.eccentricmc.ranksspigot.API;


import net.eccentricmc.ranksspigot.PluginMessages.PMSender;
import net.eccentricmc.ranksspigot.Ranks.Rank;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class RanksAPI {

    private HashMap<String, Rank> cache = new HashMap<>();

    public void a(String uuid, Rank r){ cache.put(uuid, r); }

    public void loadHighestRank(Player p) {
        PMSender.requestHighestRank(p);
    }

    public Rank getHighestRank(String uuid) {
        return cache.get(uuid.replaceAll("-", ""));
    }

    private static RanksAPI instance;
    public static RanksAPI getInstance() {
        if(instance == null) instance = new RanksAPI();
        return instance;
    }
}
