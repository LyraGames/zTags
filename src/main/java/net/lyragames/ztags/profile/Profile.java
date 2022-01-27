package net.lyragames.ztags.profile;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.Setter;
import net.lyragames.ztags.Tags;
import net.lyragames.ztags.profile.impl.ProfileHandler;
import net.lyragames.ztags.tag.Tag;
import org.bson.Document;

import java.util.UUID;

@Getter @Setter
public class Profile {

    private UUID player;
    private Tag tag;

    public Profile(UUID player) {
        this.player = player;
    }

    public Document toBson() {
        return new Document("_id", player.toString())
                .append("tag", tag == null ? "null" : tag.getId().toString());
    }

    public void save() {
        Tags.getMongoExecutor().execute(() -> Tags.getProfile().replaceOne(Filters.eq("_id", player.toString()), toBson(), new ReplaceOptions().upsert(true)));

    }

    public void load() {
        Document document = Tags.getProfile().find(Filters.eq("_id", player.toString())).first();

        if (document == null) {
            save();
            return;
        }

        if (!document.getString("tag").equals("null")) {
            tag = Tag.getById(document.getString("tag"));
        }
    }

    public static Profile getByUUID(UUID uuid) {
        return ProfileHandler.getProfiles().stream().filter(profile -> profile.getPlayer().equals(uuid)).findFirst().orElse(null);
    }


}
