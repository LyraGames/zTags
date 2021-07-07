package me.zowpy.ztags.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@Getter
public class ConfigFile {

    private File file;
    private YamlConfiguration config;

    @SneakyThrows
    public ConfigFile(String fileName, JavaPlugin plugin) {
        file = new File(plugin.getDataFolder(), fileName + ".yml");

        if (!file.exists()) {
            file.createNewFile();
            plugin.saveResource(fileName + ".yml", true);
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    @SneakyThrows
    public void save() {
        config.save(file);
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }
}
