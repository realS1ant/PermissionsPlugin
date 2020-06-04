package net.eccentricmc.ranksbungee;

import net.eccentricmc.ranksbungee.Cache.CachedPerms;
import net.eccentricmc.ranksbungee.Permissions.Permissions;
import net.eccentricmc.ranksbungee.Players.Players;
import net.eccentricmc.ranksbungee.Ranks.Rank;
import net.eccentricmc.ranksbungee.Ranks.Ranks;
import net.eccentricmc.ranksbungee.SpigotCommunication.PluginMSG;
import net.eccentricmc.ranksbungee.Utils.MojangAPI;
import net.eccentricmc.ranksbungee.Utils.Utils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.List;

public class RankCommand extends Command {
    public RankCommand() {
        super("ep");
    }

    Ranks ranks = Ranks.getInstance();
    Players player = new Players();
    Permissions permissions = Permissions.getInstance();
    CachedPerms cachedPerms = CachedPerms.getInstance();
    String prefix = ChatColor.translateAlternateColorCodes('&', "&a❖ &lEccentricMC &8● &7");

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer) sender.sendMessage(Utils.chat(String.valueOf(cachedPerms.getPermissions(((ProxiedPlayer)sender).getUniqueId().toString()))));
        sender.sendMessage(Utils.chat(String.valueOf(sender.getPermissions())));

        if (!(sender.hasPermission("user_cmd") || sender.hasPermission("SUPER_ADMIN"))) {
            sender.sendMessage(Utils.chat(prefix + "&cYou do not have permission to do that!"));

            return;
        }
        //up
        if (args.length == 0) {
            sender.sendMessage(Utils.chat("&2&lECCENTRIC PERMISSIONS &7(1/1)\n" +
                    "&a/ep ranks &8- &7List all ranks\n" +
                    "&a/ep players <rank> &8- &7List all players of specified rank.\n" +
                    "&a/ep perms &8- &7List all permissions.\n" +
                    "&a/ep perms <rank> &8- &7List rank's permissions.\n" +
                    "&a/ep info <user> &8- &7Get user information\n" +
                    "&a/ep addperm <user> <perm> &8- &7Add a permission\n" +
                    "&a/ep delperm <user> <perm> &8- &7Remove a permission\n" +
                    "&a/ep addrank <user> <rank> &8- &7Add a rank\n" +
                    "&a/ep delrank <user> <rank> &8- &7Remove a rank"
            ));
            return;
        }
        switch (args[0]) {
            //---------- /up ranks
            case "ranks":
                //Version Requested by Mateo (Hard Coded to order ranks manually.)
                sender.sendMessage(Utils.chat(ChatColor.GREEN + ("Owner, Admin, Dev, SrMod, Mod, Helper, Builder, " +
                        "VIP, Media, God, Champion, Legend, Hero, Player")
                        .replaceAll(",", ChatColor.GRAY + "," + ChatColor.GOLD)));
                return;
            case "perms":
                if(args.length >= 2){
                    if(ranks.getRankNames().contains(args[1])){
                        Rank r = ranks.getRankByName(args[1]);
                        String s;
                        if(r.getRank() == ranks.getDefaultRank()){
                            s = ChatColor.GRAY +  "Default";
                        } else {
                            s = r.getPrefix();
                        }

                        StringBuilder sb2 = new StringBuilder(s + ChatColor.RESET + ChatColor.GREEN + "'s Perms: ");
                        boolean add = false;
                        for(String perm : ranks.getPermissions(r)){
                            if(add){
                                sb2.append(ChatColor.GRAY + ", ");
                            } else{
                                add = true;
                            }
                            sb2.append(ChatColor.GOLD + perm);
                        }
                        sender.sendMessage(Utils.chat(sb2.toString()));
                    } else {
                        sender.sendMessage(Utils.chat(prefix + ChatColor.DARK_GREEN + args[1] + ChatColor.RED + " Isn't a rank!"));
                    }
                    return;
                }
                //------------------------------------------ DONE ------------------------------------------
                ProxyServer.getInstance().getScheduler().runAsync(Main.getInstance(), () -> {
                    List<String> perms = cachedPerms.getGloablPermissions();
                    if(perms == null){
                        sender.sendMessage(Utils.chat(prefix + "&cError getting permissions from the database."));
                        return;
                    }
                    StringBuilder sb2 = new StringBuilder(ChatColor.GREEN + "Available Perms: ");
                    int i2 = 0;
                    for (String perm : perms) {
                        if (i2 != 0) {
                            sb2.append(ChatColor.GRAY + ", ");
                        }
                        i2++;
                        sb2.append(ChatColor.GOLD + "").append(perm.toUpperCase());
                    }
                    sender.sendMessage(Utils.chat(sb2.toString()));
                });
                return;
            //---------- /up players <rank>
            case "players":
                if (args.length < 2) {
                    sender.sendMessage(Utils.chat(ChatColor.RED + "Error: Invalid Args!"));
                    return;
                }
                if (!ranks.isRank(args[1])) {
                    sender.sendMessage(Utils.chat(ChatColor.RED + "That isn't a rank!"));
                    return;
                }
                ProxyServer.getInstance().getScheduler().runAsync(Main.getInstance(), () -> {
                    List<String> playerNames = ranks.getPlayers(ranks.getRankByName(args[1]));
                    if (playerNames == null) {
                        sender.sendMessage(Utils.chat(ChatColor.GRAY + "There aren't any players with rank " + ChatColor.DARK_GREEN + args[1].toUpperCase()));
                        return;
                    }

                    sender.sendMessage(Utils.chat(ChatColor.GRAY + "Players with rank " + ChatColor.DARK_GREEN + args[1].toUpperCase() + ChatColor.GRAY + ":"));
                    for (String playerName : playerNames) {
                        sender.sendMessage(Utils.chat(ChatColor.GRAY + " - " + ChatColor.GRAY + playerName));
                    }
                });

                return;
            //---------- /up info <player>
            case "info":
                if (args.length < 2) {
                    sender.sendMessage(Utils.chat(ChatColor.RED + "Error: Invalid Args!"));
                    return;
                }
                String name = args[1];
                ProxyServer.getInstance().getScheduler().runAsync(Main.getInstance(), () -> {
                    try {
                        String uuid = MojangAPI.getUUID(name);
                        List<String> perms = cachedPerms.getPermissions(uuid);

                        if(perms == null){
                            sender.sendMessage(Utils.chat(prefix + ChatColor.RED + "That user isn't in the system!"));
                            return;
                        }
                        List<String> ranks = player.getRanks(uuid);
                        sender.sendMessage(Utils.chat(prefix + ChatColor.GRAY + "Permissions for: " + player.getHighestRank(uuid).getNameColor() + MojangAPI.getName(uuid)));
                        sender.sendMessage(Utils.chat(ChatColor.GREEN+"Ranks:"));
                            for(String rank : ranks){
                                if(!rank.equalsIgnoreCase("default")) {
                                    sender.sendMessage(Utils.chat(ChatColor.WHITE + " - " + Ranks.getInstance().getRankByName(rank).getPrefix()));
                                } else {
                                    sender.sendMessage(Utils.chat(ChatColor.WHITE + " - " + ChatColor.GRAY + "Default"));
                                }
                            }
                        sender.sendMessage(Utils.chat(ChatColor.GREEN + "Permissions:"));
                        for(String perm : perms){
                            if(!perm.startsWith("rank_")) sender.sendMessage(Utils.chat(ChatColor.WHITE + " - " + ChatColor.GOLD + perm));
                        }

                    } catch (Exception e) {
                        sender.sendMessage(Utils.chat(prefix + ChatColor.RED + "Player not found!"));
                        e.printStackTrace();
                    }
                });
                return;
            //---------- /up addrank <player> <rank>
            case "addrank":
                if(args.length < 3){
                    sender.sendMessage(Utils.chat(prefix + ChatColor.RED + "Error: Invalid Arguments!"));
                    return;
                }
                if(!ranks.isRank(args[2])){
                    sender.sendMessage(Utils.chat(prefix + ChatColor.RED + "Rank not found!"));
                    return;
                }
                ProxyServer.getInstance().getScheduler().runAsync(Main.getInstance(), () -> {
                    try {
                        String uuid = MojangAPI.getUUID(args[1]);
                        String rank = args[2];

                        if(cachedPerms.hasPermission(uuid, "rank_"+rank)){
                            sender.sendMessage(Utils.chat(prefix + ChatColor.DARK_GREEN + args[1] + ChatColor.RED + " already has that rank!"));
                            return;
                        }

                        player.addRank(uuid, ranks.getRankByName(rank));
                        if(!rank.equalsIgnoreCase("default")) {
                            sender.sendMessage(Utils.chat(prefix + ChatColor.GRAY + "Added rank " + Ranks.getInstance().getRankByName(rank).getPrefix()));
                        } else {
                            sender.sendMessage(Utils.chat(prefix + ChatColor.GRAY + "Added rank " + ChatColor.GRAY + "Default"));
                        }
                        for(ProxiedPlayer p : BungeeCord.getInstance().getPlayers()){
                            if(p.hasPermission("ADMIN_NOTIFICATIONS")){
                                p.sendMessage(Utils.chat(prefix + ChatColor.DARK_GREEN + sender.getName() +
                                        " &7has added rank " + ChatColor.DARK_GREEN +
                                        args[2].toUpperCase() + " &7to " + ChatColor.DARK_GREEN + args[1]));
                            }
                        }
                        Runnable runnable = () -> {
                            ProxiedPlayer p = BungeeCord.getInstance().getPlayer(args[1]);
                            if(p != null){
                                permissions.setUp(p);
                                PluginMSG.sendPlayerPerms(p);
                            }
                        };
                        runnable.run();
                    } catch(Exception e){
                        sender.sendMessage(Utils.chat(prefix + ChatColor.RED + "Player not found!"));
                        e.printStackTrace();
                    }
                });
                return;

            case "delrank":
                if(args.length < 3){
                    sender.sendMessage(Utils.chat(prefix + ChatColor.RED + "Error: Invalid Arguments!"));
                    return;
                }
                if(!ranks.isRank(args[2])){
                    sender.sendMessage(Utils.chat(prefix + ChatColor.RED + "Rank not found!"));
                    return;
                }
                ProxyServer.getInstance().getScheduler().runAsync(Main.getInstance(), () -> {
                    try {
                        String uuid = MojangAPI.getUUID(args[1]);
                        String rank = args[2];

                        if(!cachedPerms.hasPermission(uuid, "rank_"+rank)){
                            sender.sendMessage(Utils.chat(prefix + ChatColor.DARK_GREEN + args[1] + ChatColor.RED + " doesn't have that rank!"));
                            return;
                        }

                        player.removeRank(uuid, ranks.getRankByName(rank));
                        if(!rank.equalsIgnoreCase("default")) {
                            sender.sendMessage(Utils.chat(prefix + ChatColor.GRAY + "Removed rank " + Ranks.getInstance().getRankByName(rank).getPrefix()));
                        } else {
                            sender.sendMessage(Utils.chat(prefix + ChatColor.GRAY + "Removed rank " + "&7Default"));
                        }
                        for(ProxiedPlayer p : BungeeCord.getInstance().getPlayers()){
                            if(p.hasPermission("ADMIN_NOTIFICATIONS")){
                                p.sendMessage(Utils.chat(prefix + ChatColor.DARK_GREEN + sender.getName() +
                                        " &7has added rank " + ChatColor.DARK_GREEN +
                                        args[2].toUpperCase() + " &7to " + ChatColor.DARK_GREEN + args[1]));
                            }
                        }
                        Runnable runnable = () -> {
                            ProxiedPlayer p = BungeeCord.getInstance().getPlayer(args[1]);
                            if(p != null){
                                permissions.setUp(p);
                                PluginMSG.sendPlayerPerms(p);
                            }
                        };
                        runnable.run();
                    } catch(Exception e){
                        sender.sendMessage(Utils.chat(prefix + ChatColor.RED + "Player not found!"));
                        e.printStackTrace();
                    }
                });
                return;

            case "addperm":
                if(args.length < 3){
                    sender.sendMessage(Utils.chat(prefix + ChatColor.RED + "Error: Invalid Arguments!"));
                    return;
                }
                ProxyServer.getInstance().getScheduler().runAsync(Main.getInstance(), () -> {
                    try {
                        String uuid = MojangAPI.getUUID(args[1]);
                        String perm = args[2];

                        if(cachedPerms.hasPermission(uuid, perm)){
                            sender.sendMessage(Utils.chat(prefix + ChatColor.DARK_GREEN + args[1] + ChatColor.RED + " already has that permission!"));
                            return;
                        }
                        if(perm.startsWith("rank_")){
                            sender.sendMessage(Utils.chat(prefix + ChatColor.RED + "Sorry! Cannot add a rank via /addperm."));
                            return;
                        }

                        cachedPerms.addPermission(uuid, perm);
                        if(!cachedPerms.getGloablPermissions().contains(perm)){
                            cachedPerms.addGlobalPerm(perm);
                        }
                        sender.sendMessage(Utils.chat(prefix + ChatColor.GRAY + "Added permission " + ChatColor.DARK_GREEN + perm.toUpperCase() + ChatColor.GRAY + " to " + ChatColor.DARK_GREEN + args[1]));
                        for(ProxiedPlayer p : BungeeCord.getInstance().getPlayers()){
                            if(p.hasPermission("ADMIN_NOTIFICATIONS")){
                                p.sendMessage(Utils.chat(prefix + ChatColor.DARK_GREEN + sender.getName() +
                                        " &7has given permission " + ChatColor.DARK_GREEN +
                                        args[2].toUpperCase() + " &7to " + ChatColor.DARK_GREEN + args[1]));
                            }
                        }
                        Runnable runnable = () -> {
                            ProxiedPlayer p = BungeeCord.getInstance().getPlayer(args[1]);
                            if(p != null){
                                permissions.setUp(p);
                                PluginMSG.sendPlayerPerms(p);
                            }
                        };
                        runnable.run();
                    } catch(Exception e){
                        sender.sendMessage(Utils.chat(prefix + ChatColor.RED + "Player not found!"));
                        e.printStackTrace();
                    }
                });
                return;

            case "delperm":
                if(args.length < 3){
                    sender.sendMessage(Utils.chat(prefix + ChatColor.RED + "Error: Invalid Arguments!"));
                    return;
                }
                ProxyServer.getInstance().getScheduler().runAsync(Main.getInstance(), () -> {
                    try {
                        String uuid = MojangAPI.getUUID(args[1]);
                        String perm = args[2];

                        if(!cachedPerms.hasPermission(uuid, perm)){
                            sender.sendMessage(Utils.chat(prefix + ChatColor.DARK_GREEN + args[1] + ChatColor.RED + " doesn't have that permission!"));
                            return;
                        }

                        if(perm.startsWith("rank_")){
                            sender.sendMessage(Utils.chat(prefix + ChatColor.RED + "Sorry! Cannot remove a rank via delperm."));
                            return;
                        }

                        cachedPerms.removePermission(uuid, perm);

                        sender.sendMessage(Utils.chat(prefix + ChatColor.GRAY + "Removed permission " + ChatColor.DARK_GREEN + perm.toUpperCase() + ChatColor.GRAY + " from " + ChatColor.DARK_GREEN + args[1]));
                        for(ProxiedPlayer p : BungeeCord.getInstance().getPlayers()){
                            if(p.hasPermission("ADMIN_NOTIFICATIONS")){
                                p.sendMessage(Utils.chat(prefix + ChatColor.DARK_GREEN + sender.getName() +
                                        " &7has taken permission " + ChatColor.DARK_GREEN +
                                        args[2].toUpperCase() + " &7from " + ChatColor.DARK_GREEN + args[1]));
                            }
                        }
                        Runnable runnable = () -> {
                            ProxiedPlayer p = BungeeCord.getInstance().getPlayer(args[1]);
                            if(p != null){
                                permissions.setUp(p);
                                PluginMSG.sendPlayerPerms(p);
                            }
                        };
                        runnable.run();
                    } catch(Exception e){
                        sender.sendMessage(Utils.chat(prefix + "&cPlayer not found!"));
                        e.printStackTrace();
                    }
                });
                return;
            default:
                sender.sendMessage(Utils.chat(prefix + "&cError: Invalid Syntax"));
                break;
        }
    }
}