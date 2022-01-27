package net.lyragames.ztags.profile.impl;

import lombok.Getter;
import net.lyragames.ztags.profile.Profile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

public class ProfileHandler implements Listener {

    @Getter
    private static final List<Profile> profiles = new ArrayList<>();

    @EventHandler
    public void onAsyncLogin(AsyncPlayerPreLoginEvent e) {
        Profile profile = new Profile(e.getUniqueId());
        profile.load();
        profiles.add(profile);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Profile profile = Profile.getByUUID(event.getPlayer().getUniqueId());
        profiles.remove(profile);
    }

}
