package me.zowpy.ztags.tag.impl;

import me.clip.placeholderapi.PlaceholderAPI;
import me.zowpy.ztags.Tags;
import me.zowpy.ztags.profile.Profile;
import me.zowpy.ztags.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class TagListener implements Listener {

    @EventHandler
    public void on(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();

        Profile profile = Profile.getByUUID(player.getUniqueId());
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            String format = Tags.getInstance().getConfig().getString("chat-format");
            e.setFormat(Color.translate(PlaceholderAPI.setPlaceholders(player, format.replace("<player>", player.getName()).replace("<tag>", profile.getTag() != null ? Color.translate(profile.getTag().getPrefix()) : "").replace("<message>", e.getMessage()))));
        }
    }
}
