package net.eccentricmc.ranksbungee.Players;


import net.eccentricmc.ranksbungee.Cache.CachedPerms;
import net.eccentricmc.ranksbungee.Ranks.Rank;
import net.eccentricmc.ranksbungee.Ranks.Ranks;
import net.md_5.bungee.api.ProxyServer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Players {
    Ranks ranks = Ranks.getInstance();
    CachedPerms cachedPerms = CachedPerms.getInstance();

    public void addRank(UUID uuid, Rank r){
        addRank(uuid.toString(), r);
    }
    public void addRank(String uuid, Rank r){
        List<String> perms = cachedPerms.getPermissions(uuid);
        if(perms != null && perms.contains("rank_"+r.getName().toLowerCase())) return;
        cachedPerms.addPermission(uuid, "rank_"+r.getName().toLowerCase());
    }

    public void removeRank(String uuid, Rank r){
        cachedPerms.removePermission(uuid, "rank_"+r.getName().toLowerCase());
    }

    public Rank getHighestRank(String uuid){
        List<String> rankList = getRanks(uuid);
        if(!rankList.contains(ranks.getDefaultRank().getName())){
            addRank(uuid, ranks.getDefaultRank());
        }
        Rank highestRank = null;
        for(String r : rankList){
            Rank rank = ranks.getRankByName(r);
            if(highestRank == null){
                highestRank = rank;
            }
            if(rank.getWeight() < highestRank.getWeight()){
                highestRank = rank;
            }
        }
        return highestRank;
    }

    public List<String> getRanks(UUID uuid){
        List<String> ranks = new ArrayList<>();
        List<String> results = cachedPerms.getPermissions(uuid.toString());
        if(results == null){
            return ranks;
        }
        for(String perm : results){
            if(perm.startsWith("rank_")){
                ranks.add(perm.replace("rank_", ""));
            }
        }
        if(!ranks.contains("default")) ranks.add("default");
        return ranks;
    }
    public List<String> getRanks(String uuid){
        List<String> ranks = new ArrayList<>();
        List<String> results = cachedPerms.getPermissions(uuid);
        if(results == null){
            return ranks;
        }
        for(String perm : results){
            if(perm.startsWith("rank_")){
                String rank = perm.replace("rank_", "");
                if(!Ranks.getInstance().getRankNames().contains(rank)){
                    ProxyServer.getInstance().getLogger().severe("User has permission starting with rank_, but rank not found!\n"+rank);
                } else {
                    ranks.add(rank);
                }
            }
        }
        if(!ranks.contains("default")) ranks.add("default");
        return ranks;
    }
}
