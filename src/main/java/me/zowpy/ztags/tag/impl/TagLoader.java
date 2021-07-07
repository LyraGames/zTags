package me.zowpy.ztags.tag.impl;

import me.zowpy.ztags.Tags;
import me.zowpy.ztags.handler.Loader;
import me.zowpy.ztags.tag.Tag;
import me.zowpy.ztags.utils.Color;
import org.bson.Document;
import org.bukkit.ChatColor;

import java.util.UUID;

public class TagLoader implements Loader {

    @Override
    public void load() {
        if (Tags.getTags().find().first() == null) {
            return;
        }

        for (Document doc : Tags.getTags().find()) {
            Tag tag = new Tag(UUID.fromString(doc.getString("_id")), doc.getString("name"), Color.translate(doc.getString("prefix")), doc.getString("permission"));
            TagHandler.getTags().add(tag);
        }
    }
}
