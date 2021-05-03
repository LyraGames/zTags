package me.zowpy.ztags;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import me.zowpy.ztags.command.TagCommand;
import me.zowpy.ztags.command.TagsCommand;
import me.zowpy.ztags.profile.impl.ProfileHandler;
import me.zowpy.ztags.tag.impl.TagHandler;
import me.zowpy.ztags.tag.impl.TagListener;
import me.zowpy.ztags.utils.Color;
import me.zowpy.ztags.utils.menu.ButtonListener;
import me.zowpy.ztags.utils.menu.MenuUpdateTask;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public final class Tags extends JavaPlugin {

    public static Random RANDOM;
    @Getter private static Tags instance;
    @Getter private MongoDatabase mongoDatabase;
    @Getter private MongoClient client;
    @Getter private static MongoCollection<Document> profile;
    @Getter private static MongoCollection<Document> tags;
    @Getter private static Executor mongoExecutor;
    @Getter private static TagHandler tagHandler;

    @Override
    public void onEnable() {
        instance = this;

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        RANDOM = new Random();

        ConsoleCommandSender sender = Bukkit.getConsoleSender();

        try {
            connect();
        } catch (Exception var2) {
            sender.sendMessage(Color.translate("&cFailed to connect to the database &7(MongoDB)"));
            var2.printStackTrace();
        }

        mongoExecutor = Executors.newFixedThreadPool(1);

        tagHandler = new TagHandler();
        tagHandler.getLoader().load();

        register();
        MenuUpdateTask menuUpdateTask = new MenuUpdateTask();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void register() {
        Bukkit.getPluginManager().registerEvents(new ButtonListener(), this);
        Bukkit.getPluginManager().registerEvents(new TagListener(), this);
        Bukkit.getPluginManager().registerEvents(new ProfileHandler(), this);
        getCommand("tags").setExecutor(new TagsCommand());
        getCommand("tag").setExecutor(new TagCommand());
    }

    private void connect() {
        client = new MongoClient(new MongoClientURI(getConfig().getString("mongodb.uri")));
        mongoDatabase = client.getDatabase(getConfig().getString("mongodb.database"));
        profile = mongoDatabase.getCollection("profiles");
        tags = mongoDatabase.getCollection("tags");
    }
}
