package me.monoto.customcrops;

import me.monoto.customcrops.commands.SeedCommand;
import me.monoto.customcrops.listerners.BlockBreakListener;
import me.monoto.customcrops.listerners.PlayerInteractListener;
import me.monoto.customcrops.utils.FileManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Objects;

public final class CustomCrops extends JavaPlugin {

    private static CustomCrops instance;

    private HashMap<String, YamlConfiguration> cropsTypes = new HashMap<>();

    public CustomCrops() {
        instance = this;
    }

    public static CustomCrops getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            if (!getDataFolder().mkdir()) {
                getLogger().severe("Could not create a resource folder");
                return;
            }
        }

        Objects.requireNonNull(this.getCommand("customcrops")).setExecutor(new SeedCommand());
        Objects.requireNonNull(this.getCommand("customcrops")).setTabCompleter(new SeedCommand());

        new PlayerInteractListener(this);
        new BlockBreakListener(this);


        cropsTypes.putAll(new FileManager(this).getCropsTypes());
        System.out.println("Loaded " + cropsTypes.size() + " crops");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public HashMap<String, YamlConfiguration> getCropsTypes() {
        return cropsTypes;
    }
}
