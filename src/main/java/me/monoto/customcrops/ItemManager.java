package me.monoto.customcrops;

import me.monoto.customcrops.utils.Formatters;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collections;
import java.util.Locale;

public class ItemManager {

    public static ItemStack getSeed(String type, Integer amount) {
        ItemStack item = new ItemStack(Material.WHEAT_SEEDS, amount);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.displayName(Formatters.mini("<type> seed", "type", Component.text(type.replaceFirst(type.substring(0, 1), type.substring(0, 1).toUpperCase(Locale.ENGLISH)))));
            meta.lore(Collections.singletonList(Formatters.mini("<time> seconds", "time", Formatters.time(700))));

            PersistentDataContainer data = meta.getPersistentDataContainer();

            if (!data.has(new NamespacedKey(CustomCrops.getInstance(), "CUSTOM_CROP"), PersistentDataType.STRING)) {
                data.set(new NamespacedKey(CustomCrops.getInstance(), "CUSTOM_CROP"), PersistentDataType.STRING, type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase());
            }

            item.setItemMeta(meta);
        }

        return item;
    }
}
