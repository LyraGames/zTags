package net.lyragames.ztags.expansion;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.lyragames.ztags.profile.Profile;
import org.bukkit.entity.Player;

/**
 * This Project is property of Zowpy © 2022
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 1/27/2022
 * Project: zTags
 */

public class TagsExpansion extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "ztag";
    }

    @Override
    public String getAuthor() {
        return "Zowpy";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player p, String params) {
        if (params.equals("tag")) {
            Profile profile = Profile.getByUUID(p.getUniqueId());

            return profile.getTag().getPrefix();
        }

        return null;
    }
}
