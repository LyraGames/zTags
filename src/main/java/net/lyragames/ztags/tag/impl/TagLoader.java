package net.lyragames.ztags.tag.impl;

import net.lyragames.ztags.Tags;
import net.lyragames.ztags.handler.Loader;
import net.lyragames.ztags.tag.Tag;
import net.lyragames.ztags.utils.Color;
import org.bson.Document;

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
