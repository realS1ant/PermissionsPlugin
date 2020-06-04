package net.eccentricmc.ranksbungee.Ranks;

import net.eccentricmc.ranksbungee.Cache.CachedPerms;
import net.md_5.bungee.config.Configuration;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Ranks {
    static Ranks instance;
    RanksConfig ranksConfig = RanksConfig.getInstance();
    HashMap<String, Rank> ranks = new HashMap<>();

    public void establish(){
        Configuration config = ranksConfig.getConfig();
        if(config == null){
            System.out.println("Ranks Config is null?");
            return;
        }
        Collection<String> keys = config.getSection("ranks").getKeys();

        for(Object key : keys){
            Rank rank = new Rank(key.toString().toLowerCase(),
                config.getString("ranks."+key.toString()+".prefix"),
                config.getString("ranks."+key.toString()+".tabprefix"),
                config.getString("ranks."+key.toString()+".suffix"),
                config.getString("ranks."+key.toString()+".namecolor"),
                config.getString("ranks."+key.toString()+".chatcolor"),
                config.getInt("ranks."+key.toString() + ".weight"),
                config.getStringList("ranks."+key.toString()+".permissions"));
            ranks.put(rank.getName().toLowerCase(), rank);
        }
    }
    public boolean isRank(String name){
        return ranks.containsKey(name.toLowerCase());
    }
    public Rank getRankByName(String name){
        if(!ranks.containsKey(name)){
            return null;
        }
        return ranks.get(name);
    }
    public Rank getDefaultRank(){
        return getRankByName("default");
    }
    public Collection<Rank> getRanks(){ return ranks.values(); }
    public Collection<String> getRankNames(){ return ranks.keySet(); }
    public List<String> getPermissions(Rank r){
        return ranksConfig.getConfig().getStringList("ranks."+r.getName()+".permissions");
    }
    public List<String> getPlayers(Rank r){
        return CachedPerms.getInstance().getAllPlayers("rank_"+r.getName().toLowerCase());
    }

    public static Ranks getInstance() {
        if(instance == null){
            instance = new Ranks();
        }
        return instance;
    }
}
