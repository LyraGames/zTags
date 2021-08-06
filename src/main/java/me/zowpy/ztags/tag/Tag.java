package me.zowpy.ztags.tag;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.Setter;
import me.zowpy.ztags.Tags;
import me.zowpy.ztags.profile.Profile;
import me.zowpy.ztags.profile.impl.ProfileHandler;
import me.zowpy.ztags.tag.impl.TagHandler;
import org.bson.Document;

import java.util.List;
import java.util.UUID;

@Getter @Setter
public class Tag {

    private UUID id;
    private String name;
    private String prefix;
    private String permission;

    public Tag(UUID id, String name, String prefix, String permission) {
        this.id = id;
        this.name = name;
        this.prefix = prefix;
        this.permission = permission;
    }

    public Document toBson() {
        return new Document("_id", id.toString())
                .append("name", name)
                .append("prefix", prefix)
                .append("permission", permission);
    }

    public void create() {
        Tags.getTags().insertOne(toBson());
    }

    public void delete() {
        Document tag = Tags.getTags().find(new Document("_id", id.toString())).first();

        if (tag != null) {
            Tags.getTags().deleteOne(tag);
        }

        for (Document document : Tags.getProfile().find()) {
            if (document.getString("tag").equalsIgnoreCase(id.toString())) {
                document.put("tag", "null");
                Tags.getMongoExecutor().execute(() -> Tags.getProfile().replaceOne(Filters.eq("_id", document.getString("_id")), document, new ReplaceOptions().upsert(true)));
            }
        }

        for (Profile profile : ProfileHandler.getProfiles()) {
            if (profile.getTag() == this) {
                profile.setTag(null);
            }
        }


    }

    public void save() {
        Document tag = Tags.getTags().find(new Document("_id", id.toString())).first();

        if (tag == null) {
            create();
            return;
        }

        tag.put("name", name);
        tag.put("prefix", prefix);
        tag.put("permission", permission);

        Tags.getMongoExecutor().execute(() -> Tags.getTags().replaceOne(Filters.eq("_id", id.toString()), tag, new ReplaceOptions().upsert(true)));
    }

    public static Tag getByName(String name) {
        return TagHandler.getTags().stream().filter(tag -> tag.getName().equals(name)).findFirst().orElse(null);
    }

    public static Tag getById(String id) {
        return TagHandler.getTags().stream().filter(tag -> tag.getId().toString().equals(id)).findFirst().orElse(null);
    }
}
