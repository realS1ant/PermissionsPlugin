package net.eccentricmc.ranksspigot;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PermissionsHandler {
    private HashMap<UUID, PermissionAttachment> attachments = new HashMap<>();


    public void setUp(Player p, ArrayList<String> perms){
        if(attachments.containsKey(p.getUniqueId())){
            p.removeAttachment(attachments.get(p.getUniqueId()));
            attachments.remove(p.getUniqueId());
        }
        PermissionAttachment attachment = p.addAttachment(Main.getInstance());
        System.out.println(p.getName());
        System.out.println(perms);
        System.out.println(p.getName());
        for(String perm : perms){
            attachment.setPermission(perm, true);
        }
        attachments.put(p.getUniqueId(), attachment);
        p.recalculatePermissions();
    }

    public void disable(){
        for(UUID uuid : attachments.keySet()){
            Player p = Bukkit.getPlayer(uuid);
            p.removeAttachment(attachments.get(uuid));
        }
    }

    public void removeAttach(UUID uuid){
        attachments.remove(uuid);
    }

    private static PermissionsHandler instance;
    public static PermissionsHandler getInstance(){
        if(instance == null) instance = new PermissionsHandler();
        return instance;
    }
}
