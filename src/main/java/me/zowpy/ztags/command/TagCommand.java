package me.zowpy.ztags.command;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import me.zowpy.ztags.Tags;
import me.zowpy.ztags.tag.Tag;
import me.zowpy.ztags.tag.impl.TagHandler;
import me.zowpy.ztags.utils.Color;
import me.zowpy.ztags.utils.menu.CC;
import org.bson.Document;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TagCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("tag")) {
            if (sender instanceof Player) {
                Player player = ((Player) sender).getPlayer();

                if (args.length == 0) {
                    player.sendMessage(CC.PRIMARY + "Tags");
                    player.sendMessage(Color.translate("&7&m--------------"));
                    player.sendMessage(CC.PRIMARY + "/tag create <tag> :" + " creates a new tag");
                    player.sendMessage(CC.PRIMARY + "/tag delete <tag> :" + " deleted a tag");
                    player.sendMessage(CC.PRIMARY + "/tag prefix <tag> <prefix> :" + " sets a prefix of a tag");
                    player.sendMessage(CC.PRIMARY + "/tag import : adds all tags that are in tags.yml");
                    player.sendMessage(CC.PRIMARY + "/tag export : saves all tags to tags.yml");
                    player.sendMessage(Color.translate("&7&m--------------"));
                }else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("import")) {
                        Tags.getTagHandler().importTags();
                        player.sendMessage(CC.GREEN + "Successfully imported tags from tags.yml!");
                    }else if (args[0].equalsIgnoreCase("export")) {
                        Tags.getTagHandler().export();
                        player.sendMessage(CC.GREEN + "Successfully exported tags to tags.yml");
                    }
                }else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("create")) {
                        if (Tag.getByName(args[1]) != null) {
                            player.sendMessage(CC.RED + "this tag already exists!");
                            return false;
                        }

                        String tag = args[1];
                        Tag t = new Tag(UUID.randomUUID(), tag, " ", "tag." + tag);
                        t.create();
                        TagHandler.getTags().add(t);
                        player.sendMessage(CC.PRIMARY + "Successfully created " + CC.SECONDARY + tag + CC.PRIMARY +  " tag!");
                    }else if (args[0].equalsIgnoreCase("delete")) {
                        if (Tag.getByName(args[1]) == null) {
                            player.sendMessage(CC.RED + "this tag does not exist!");
                            return false;
                        }

                        Tag tag = Tag.getByName(args[1]);
                        for (Document prof : Tags.getProfile().find()) {
                            if (Tag.getByName(prof.getString("tag")) == tag) {
                                prof.put("tag", null);
                                Tags.getMongoExecutor().execute(() -> Tags.getProfile().replaceOne(Filters.eq("_id", prof.getString("_id")), prof, new ReplaceOptions().upsert(true)));
                            }
                        }
                        Tags.getTags().deleteOne(new Document("_id", tag.getId().toString()));
                        TagHandler.getTags().remove(tag);
                        player.sendMessage(CC.PRIMARY + "Successfully deleted " + CC.SECONDARY + tag.getName() + CC.PRIMARY + " tag!");
                    }else {
                        player.sendMessage(CC.PRIMARY + "Tags");
                        player.sendMessage(Color.translate("&7&m--------------"));
                        player.sendMessage(CC.PRIMARY + "/tag create <tag> :" + " creates a new tag");
                        player.sendMessage(CC.PRIMARY + "/tag delete <tag> :" + " deleted a tag");
                        player.sendMessage(CC.PRIMARY + "/tag prefix <tag> <prefix> :" + " sets a prefix of a tag");
                        player.sendMessage(Color.translate("&7&m--------------"));
                    }
                }else if (args.length == 3) {
                    if (args[0].equalsIgnoreCase("prefix")) {
                        if (Tag.getByName(args[1]) == null) {
                            player.sendMessage(CC.RED + "this tag does not exist!");
                            return false;
                        }

                        Tag tag = Tag.getByName(args[1]);
                        tag.setPrefix(Color.translate(args[2]));
                        player.sendMessage(CC.PRIMARY + "Successfully set " + CC.SECONDARY + tag.getName() + "`s" + CC.PRIMARY + " to " + Color.translate(args[2]));
                    }else {
                        player.sendMessage(CC.PRIMARY + "Tags");
                        player.sendMessage(Color.translate("&7&m--------------"));
                        player.sendMessage(CC.PRIMARY + "/tag create <tag> :" + " creates a new tag");
                        player.sendMessage(CC.PRIMARY + "/tag delete <tag> :" + " deleted a tag");
                        player.sendMessage(CC.PRIMARY + "/tag prefix <tag> <prefix> :" + " sets a prefix of a tag");
                        player.sendMessage(Color.translate("&7&m--------------"));
                    }
                }else {
                    player.sendMessage(CC.PRIMARY + "Tags");
                    player.sendMessage(Color.translate("&7&m--------------"));
                    player.sendMessage(CC.PRIMARY + "/tag create <tag> :" + " creates a new tag");
                    player.sendMessage(CC.PRIMARY + "/tag delete <tag> :" + " deleted a tag");
                    player.sendMessage(CC.PRIMARY + "/tag prefix <tag> <prefix> :" + " sets a prefix of a tag");
                    player.sendMessage(Color.translate("&7&m--------------"));
                }
            }
        }





        return false;
    }
}
