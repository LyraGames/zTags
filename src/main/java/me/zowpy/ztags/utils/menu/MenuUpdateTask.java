package me.zowpy.ztags.utils.menu;

import me.zowpy.ztags.Tags;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class MenuUpdateTask extends BukkitRunnable {

    public MenuUpdateTask() {
        runTaskTimerAsynchronously(Tags.getInstance(), 2L, 2L);
    }

    @Override
    public void run() {
        for (Map.Entry<String, Menu> entry : Menu.getCurrentlyOpenedMenus().entrySet()) {
            String key = entry.getKey();
            Menu value = entry.getValue();
            Player player = Bukkit.getPlayer(key);

            if (player != null) {
                if (value.isAutoUpdate()) {
                    value.openMenu(player);
                }
            }
        }
    }

}
