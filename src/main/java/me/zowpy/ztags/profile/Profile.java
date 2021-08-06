package me.zowpy.ztags.profile;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.Setter;
import me.zowpy.ztags.Tags;
import me.zowpy.ztags.profile.impl.ProfileHandler;
import me.zowpy.ztags.tag.Tag;
import org.bson.Document;

import javax.print.Doc;
import java.util.List;
import java.util.UUID;

@Getter @Setter
public class Profile {

    private UUID player;
    private Tag tag;

    public Profile(UUID player, Tag tag) {
        this.player = player;
        this.tag = tag;
    }

    public Document toBson() {
        return new Document("_id", player.toString())
                .append("tag", tag == null ? "null" : tag.getId().toString());
    }

    public void create() {
        Tags.getProfile().insertOne(toBson());
    }

    public void save() {
        Document doc = Tags.getProfile().find(new Document("_id", player.toString())).first();

        if (doc == null) {
            create();
            return;
        }

        doc.put("tag", tag == null ? "null" : tag.getId().toString());

        Tags.getMongoExecutor().execute(() -> Tags.getProfile().replaceOne(Filters.eq("_id", player.toString()), doc, new ReplaceOptions().upsert(true)));

    }

    public static Profile getByUUID(UUID uuid) {
        return ProfileHandler.getProfiles().stream().filter(profile -> profile.getPlayer().equals(uuid)).findFirst().orElse(null);
    }


}
