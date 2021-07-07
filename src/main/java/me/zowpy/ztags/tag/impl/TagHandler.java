package me.zowpy.ztags.tag.impl;

import lombok.Getter;
import me.zowpy.ztags.Tags;
import me.zowpy.ztags.handler.Handler;
import me.zowpy.ztags.handler.Loader;
import me.zowpy.ztags.tag.Tag;
import me.zowpy.ztags.utils.ConfigFile;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TagHandler implements Handler {

    private TagLoader tagLoader;
    @Getter private static List<Tag> tags = new ArrayList<>();

    public TagHandler() {
        tagLoader = new TagLoader();
    }

    @Override
    public Loader getLoader() {
        return tagLoader;
    }

    public void export() {
        tags.forEach(tag -> {
            ConfigFile file = Tags.getTagFile();
            YamlConfiguration config = file.getConfig();
            ConfigurationSection cs = config.createSection("tags." + tag.getId().toString());
            cs.set("name", tag.getName());
            cs.set("prefix", tag.getPrefix());
            cs.set("permission", tag.getPermission());
            file.save();
        });
    }

    public void importTags() {
        ConfigFile file = Tags.getTagFile();
        for (String tag : file.getConfig().getConfigurationSection("tags").getKeys(false)) {
            ConfigurationSection cs = file.getConfig().getConfigurationSection("tags");
            Tag tag1 = new Tag(UUID.fromString(tag), cs.getString("name"), cs.getString("prefix"), cs.getString("permission"));
            tag1.create();
            tags.add(tag1);

        }
    }


}
