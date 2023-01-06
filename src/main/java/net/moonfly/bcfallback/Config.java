package net.moonfly.bcfallback;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

public class Config {
    private final Plugin plugin;
    private Configuration configuration;

    public Config(Plugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    private void loadConfig() {
        File resourceFile = loadResource();
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(resourceFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File loadResource() {
        File folder = plugin.getDataFolder();
        if (!folder.exists()) {
            folder.mkdir();
        }

        File resourceFile = new File(folder, "config.yml");
        if (!resourceFile.exists()) {
            try (InputStream in = plugin.getResourceAsStream("config.yml")) {
                if (in != null) {
                    Files.copy(in, resourceFile.toPath());
                } else {
                    plugin.getLogger().severe("config.yml resource not found in the plugin jar.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resourceFile;
    }

    public boolean getKey(String keyname) {
        return configuration != null && configuration.getBoolean(keyname);
    }

    public String getString(String keyname) {
        return configuration != null ? configuration.getString(keyname) : null;
    }

    public List<?> getList(String path) {
        return configuration != null ? configuration.getList(path) : null;
    }
}