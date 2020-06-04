package net.eccentricmc.ranksbungee.API;

import net.eccentricmc.ranksbungee.Cache.CachedPerms;
import net.eccentricmc.ranksbungee.Players.Players;
import net.eccentricmc.ranksbungee.Ranks.Rank;
import net.eccentricmc.ranksbungee.Ranks.Ranks;

import java.util.List;
import java.util.UUID;

public class RanksAPI {

    private Ranks ranksClass = Ranks.getInstance();
    private Players playersClass = new Players();
    private CachedPerms cache = CachedPerms.getInstance();

    public void reloadPermissionChache(){
        cache.loadCache();
    }

    public List<String> getPermissions(UUID uuid){
        return cache.getPermissions(uuid.toString());
    }

    public void addRank(UUID uuid, String rankName){
        Rank rank = ranksClass.getRankByName(rankName);
        if(rank == null) return;
        if(!cache.getPermissions(uuid.toString()).contains("rank_"+rankName)) playersClass.addRank(
                uuid.toString().replaceAll("-", ""),
                rank);
    }







    static private RanksAPI instance;
    public static RanksAPI getInstance() {
        if(instance == null) instance = new RanksAPI();
        return instance;
    }
}
