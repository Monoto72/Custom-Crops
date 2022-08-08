package me.monoto.customcrops;

import me.monoto.customcrops.commands.SeedCommand;
import me.monoto.customcrops.listerners.CropBreak;
import me.monoto.customcrops.listerners.CropPlace;
import me.monoto.customcrops.panels.ViewPanel;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class CustomCrops extends JavaPlugin {

    private static CustomCrops instance;

    public CustomCrops() {
        instance = this;
    }

    public static CustomCrops getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        Objects.requireNonNull(this.getCommand("customcrops")).setExecutor(new SeedCommand());
        Objects.requireNonNull(this.getCommand("customcrops")).setTabCompleter(new SeedCommand());

        new CropPlace(this);
        new CropBreak(this);

        // todo: add listeners in an abstract way
        new ViewPanel();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
