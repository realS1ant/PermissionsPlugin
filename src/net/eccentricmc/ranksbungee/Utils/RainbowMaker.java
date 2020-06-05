package net.eccentricmc.ranksbungee.Utils;

import net.md_5.bungee.api.ChatColor;

public class RainbowMaker {
    public static String rainbow(String s){
        String colors = "c6eabd";
        StringBuilder sb = new StringBuilder("");

        for(int i = 0; i<s.length(); i++){
            System.out.println(i);
            int num = i;
            if(num > colors.length()-1){
                num = num%colors.length();
            }
            sb.append("&").append(colors.charAt(num)).append("&l").append(s.charAt(i));
        }
        return ChatColor.translateAlternateColorCodes('&', sb.toString());
    }
}
