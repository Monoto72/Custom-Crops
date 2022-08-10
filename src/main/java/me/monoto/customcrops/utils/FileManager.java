package me.monoto.customcrops.utils;

import me.monoto.customcrops.CustomCrops;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

public class FileManager {

    private final CustomCrops plugin;
    private final ArrayList<String> defaultCrops = new ArrayList<>(Arrays.asList("coal.yml", "iron.yml", "gold.yml", "diamond.yml"));
    private final ArrayList<File> cropFiles = new ArrayList<>();

    public HashMap<String, YamlConfiguration> cropsTypes = new HashMap<>();

    public FileManager(CustomCrops plugin) {
        this.plugin = plugin;

        createCropFolder();
    }

    private void createCropFolder() {
        File folder = new File(plugin.getDataFolder(), "/crops");

        if (!folder.exists()) {
            if (!folder.mkdir()) {
                plugin.getLogger().severe("Crop folder could not be created!");
                return;
            }
        }

        if (folder.exists()) {
            File[] files = folder.listFiles();

            if (files != null) {
                if (files.length == 0) {
                    defaultCrops.forEach(file -> {
                        plugin.getLogger().info("Copying default crop file " + file + " to crops folder");
                        plugin.saveResource("crops/" + file, false);
                        cropFiles.add(new File(plugin.getDataFolder(), "/crops/" + file));
                    });
                } else {
                    plugin.getLogger().info("Found " + files.length + " crop files in crops folder");
                    cropFiles.addAll(Arrays.asList(files));
                }
                createCropFiles();
            }
        }
    }

    private void createCropFiles() {
        cropFiles.forEach(file -> {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            saveLanguageConfig(config, file);
            cropsTypes.put(getNameWithoutExtension(file.getName()), config);
        });
    }

    private void saveLanguageConfig(YamlConfiguration config, File file) {
        try {
            config.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private String getNameWithoutExtension(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    public HashMap<String, YamlConfiguration> getCropsTypes() {
        return cropsTypes;
    }
}
