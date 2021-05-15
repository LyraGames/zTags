package me.zowpy.ztags.profile;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import me.zowpy.ztags.Tags;
import me.zowpy.ztags.profile.impl.ProfileHandler;
import me.zowpy.ztags.tag.Tag;
import org.bson.Document;

import javax.print.Doc;
import java.util.List;
import java.util.UUID;

@Getter
public class Profile {

    private UUID player;
    private Tag tag;
    private Document profileDocument;

    public Profile(UUID player, Tag tag) {
        this.player = player;
        this.tag = tag;

        profileDocument = new Document("_id", player.toString()).append("tag", tag == null ? "null" : tag.getId());

    }

    public void create() {
        Tags.getProfile().insertOne(profileDocument);
    }

    public void setTag(Tag tag) {
        this.tag = tag;
        Document doc = Tags.getProfile().find(new Document("_id", player.toString())).first();
        doc.put("tag", tag == null ? "null" : tag.getId());
        Tags.getMongoExecutor().execute(() -> Tags.getProfile().replaceOne(Filters.eq("_id", player.toString()), doc, new ReplaceOptions().upsert(true)));
    }

    public static Profile getByUUID(UUID uuid) {
        return ProfileHandler.getProfiles().stream().filter(profile -> profile.getPlayer().equals(uuid)).findFirst().orElse(null);
    }


}
