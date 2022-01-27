package net.lyragames.ztags;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import net.lyragames.ztags.command.TagCommand;
import net.lyragames.ztags.command.TagsCommand;
import net.lyragames.ztags.expansion.TagsExpansion;
import net.lyragames.ztags.profile.Profile;
import net.lyragames.ztags.profile.impl.ProfileHandler;
import net.lyragames.ztags.tag.Tag;
import net.lyragames.ztags.tag.impl.TagHandler;
import net.lyragames.ztags.tag.impl.TagListener;
import net.lyragames.ztags.utils.Color;
import net.lyragames.ztags.utils.ConfigFile;
import net.lyragames.ztags.utils.menu.ButtonListener;
import net.lyragames.ztags.utils.menu.MenuUpdateTask;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public final class Tags extends JavaPlugin {

    @Getter private static Tags instance;
    @Getter private MongoDatabase mongoDatabase;
    @Getter private MongoClient client;
    @Getter private static MongoCollection<Document> profile;
    @Getter private static MongoCollection<Document> tags;
    @Getter private static Executor mongoExecutor;
    @Getter private static TagHandler tagHandler;
    @Getter private static ConfigFile tagFile;


    @Override
    public void onEnable() {
        instance = this;

        getConfig().options().copyDefaults();
        saveDefaultConfig();

         tagFile = new ConfigFile("tags", this);

        ConsoleCommandSender sender = Bukkit.getConsoleSender();

        try {
            connect();
        } catch (Exception var2) {
            var2.printStackTrace();
            sender.sendMessage(Color.translate("&cFailed to connect to the database &7(MongoDB)"));
        }

        mongoExecutor = Executors.newFixedThreadPool(1);

        tagHandler = new TagHandler();
        tagHandler.getLoader().load();

        register();
        new MenuUpdateTask();

        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new TagsExpansion().register();
        }

    }

    @Override
    public void onDisable() {
        for (Profile profile : ProfileHandler.getProfiles()) {
            profile.save();
        }

        for (Tag tag : TagHandler.getTags()) {
            tag.save();
        }

        tagHandler.export();

        instance = null;
    }

    private void register() {
        Bukkit.getPluginManager().registerEvents(new ButtonListener(), this);
        Bukkit.getPluginManager().registerEvents(new TagListener(), this);
        Bukkit.getPluginManager().registerEvents(new ProfileHandler(), this);
        getCommand("tags").setExecutor(new TagsCommand());
        getCommand("tag").setExecutor(new TagCommand());
    }

    private void connect() {
        if (getConfig().getBoolean("mongodb.use-uri")) {
            client = new MongoClient(new MongoClientURI(getConfig().getString("mongodb.uri")));
        }else {
            if (getConfig().getBoolean("mongodb.authentication.enabled")) {
                client = new MongoClient(new ServerAddress(getConfig().getString("mongodb.host"), getConfig().getInt("mongodb.port")), Collections.singletonList(MongoCredential.createCredential(getConfig().getString("mongodb.authentication.username"), getConfig().getString("mongodb.authentication.database"), getConfig().getString("mongodb.authentication.password").toCharArray())));
            }else {
                client = new MongoClient(new ServerAddress(getConfig().getString("mongodb.host"), getConfig().getInt("mongodb.port")));
            }
        }
        mongoDatabase = client.getDatabase(getConfig().getString("mongodb.database"));
        profile = mongoDatabase.getCollection("profiles");
        tags = mongoDatabase.getCollection("tags");
    }
}
