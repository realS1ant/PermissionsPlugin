package net.eccentricmc.ranksspigot.Ranks;

import java.util.List;

public class Rank {

    String name,prefix,shortPrefix,suffix,nameColor,chatColor;
    int weight;
    List<String> permissions;
    public Rank(String name, String prefix, String shortPrefix, String suffix, String nameColor, String chatColor, int weight, List<String> permissions){
        this.name = name;
        this.prefix = prefix;
        this.shortPrefix = shortPrefix;
        this.suffix = suffix;
        this.nameColor = nameColor;
        this.chatColor = chatColor;
        this.weight = weight;
        this.permissions = permissions;
    }
    public String getName(){ return this.name; }
    public String getPrefix(){ return this.prefix; }
    public String getShortPrefix() { return this.shortPrefix; }
    public String getSuffix(){ return this.suffix; }
    public String getNameColor(){ return this.nameColor; }
    public String getChatColor(){ return this.chatColor; }
    public Integer getWeight() { return this.weight; }
    public List<String> getPermissions() {return this.permissions; }
    public Rank getRank(){ return this; }
}
