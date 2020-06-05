package net.eccentricmc.ranksbungee.API;

import net.eccentricmc.ranksbungee.Cache.CachedPerms;
import net.eccentricmc.ranksbungee.Players.Players;
import net.eccentricmc.ranksbungee.Ranks.Rank;
import net.eccentricmc.ranksbungee.Ranks.Ranks;

import java.util.Collection;
import java.util.List;

public class RanksAPI {

    private Ranks ranksClass = Ranks.getInstance();
    private Players playersClass = new Players();
    private CachedPerms cache = CachedPerms.getInstance();

    public void reloadPermissionChache(){
        cache.loadCache();
    }
    public void reloadAllPermsCache() { cache.loadGPermsCache(); }

    public List<String> getPermissions(String uuid){
        return cache.getPermissions(uuid);
    }
    public List<String> getRankPermissions(Rank r) { return ranksClass.getPermissions(r); }
    public List<String> getPlayersWithRank(Rank r) { return ranksClass.getPlayers(r); }
    public List<String> getRanks(String uuid) {return playersClass.getRanks(uuid); }
    public Rank getHighestRank(String uuid) { return playersClass.getHighestRank(uuid); }
    public Collection<String> getAllRanks() { return ranksClass.getRankNames(); }
    public Rank getRankByName(String rankName) { return ranksClass.getRankByName(rankName); }
    public Rank getDefaultRank() { return ranksClass.getDefaultRank(); }

    static private RanksAPI instance;
    public static RanksAPI getInstance() {
        if(instance == null) instance = new RanksAPI();
        return instance;
    }
}
