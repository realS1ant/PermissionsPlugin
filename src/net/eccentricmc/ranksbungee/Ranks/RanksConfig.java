package net.eccentricmc.ranksbungee.Ranks;

import net.eccentricmc.ranksbungee.Main;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;


public class RanksConfig {
    private Configuration config;

    public void create(){
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(Main.getInstance().getResourceAsStream("ranks.yml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Configuration getConfig() { return config; }

    private static RanksConfig instance;
    public static RanksConfig getInstance() {
        if(instance == null){
            instance = new RanksConfig();
        }
        return instance;
    }
}