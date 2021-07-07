package me.zowpy.ztags.tag;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.Setter;
import me.zowpy.ztags.Tags;
import me.zowpy.ztags.tag.impl.TagHandler;
import org.bson.Document;
import org.bukkit.ChatColor;

import java.util.UUID;

@Getter
public class Tag {

    private UUID id;
    private String name;
    private String prefix;
    private String permission;
    private Document tagDocument;

    public Tag(UUID id, String name, String prefix, String permission) {
        this.id = id;
        this.name = name;
        this.prefix = prefix;
        this.permission = permission;

        tagDocument = new Document("_id", id).append("name", name).append("prefix", prefix).append("permission", permission);
    }

    public void create() {
        Tags.getTags().insertOne(tagDocument);
    }

    public void setName(String name) {
        this.name = name;
        Document tag = Tags.getTags().find(new Document("_id", id.toString())).first();
        tag.put("name", name);
        Tags.getMongoExecutor().execute(() -> Tags.getTags().replaceOne(Filters.eq("_id", id.toString()), tag, new ReplaceOptions().upsert(true)));
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
        Document tag = Tags.getTags().find(new Document("_id", id.toString())).first();
        tag.put("prefix", prefix);
        Tags.getMongoExecutor().execute(() -> Tags.getTags().replaceOne(Filters.eq("_id", id.toString()), tag, new ReplaceOptions().upsert(true)));
    }

    public static Tag getByName(String name) {
        return TagHandler.getTags().stream().filter(tag -> tag.getName().equals(name)).findFirst().orElse(null);
    }

    public static Tag getById(String id) {
        return TagHandler.getTags().stream().filter(tag -> tag.getId().toString().equals(id)).findFirst().orElse(null);
    }
}
