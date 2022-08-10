package me.monoto.customcrops.utils;

import me.monoto.customcrops.CustomCrops;
import me.monoto.customcrops.utils.Formatters;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;

public class ItemManager {

    public static ItemStack getSeed(String type, Integer amount) {
        ItemStack item = new ItemStack(Material.WHEAT_SEEDS, amount);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            if (CustomCrops.getInstance().getCropsTypes().get(type.toLowerCase()) != null) {
                YamlConfiguration config = CustomCrops.getInstance().getCropsTypes().get(type.toLowerCase());

                meta.displayName(Formatters.mini(config.getString("name")));
                ArrayList<Component> seedLore = new ArrayList<>();

                for (String lore : config.getStringList("item.lore")) {
                    seedLore.add(Formatters.mini(lore));
                }

                meta.lore(seedLore);
                PersistentDataContainer data = meta.getPersistentDataContainer();

                if (!data.has(new NamespacedKey(CustomCrops.getInstance(), "CUSTOM_CROP"), PersistentDataType.STRING)) {
                    data.set(new NamespacedKey(CustomCrops.getInstance(), "CUSTOM_CROP"), PersistentDataType.STRING, type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase());
                }

                item.setItemMeta(meta);
            }
        }

        return item;
    }
}
