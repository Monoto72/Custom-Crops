package me.monoto.customcrops.listerners;

import me.monoto.customcrops.CustomCrops;
import me.monoto.customcrops.crops.Crop;
import me.monoto.customcrops.crops.CropManager;
import me.monoto.customcrops.utils.Formatters;
import org.apache.commons.lang.math.IntRange;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.List;

import static org.bukkit.Bukkit.getPluginManager;

public class PlayerInteractListener implements Listener {

    public PlayerInteractListener(CustomCrops plugin){
        getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void on(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;
        if (event.getItem() == null) return;

        ItemStack item = event.getItem();
        PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();

        if (data.has(new NamespacedKey(CustomCrops.getInstance(), "CUSTOM_CROP"), PersistentDataType.STRING)) {
            Bukkit.getScheduler().runTask(CustomCrops.getInstance(), () -> {
                String type = data.get(new NamespacedKey(CustomCrops.getInstance(), "CUSTOM_CROP"), PersistentDataType.STRING);
                if (type == null) return;

                Block block = event.getClickedBlock().getRelative(BlockFace.UP);
                BlockData blockData = block.getBlockData();

                if (blockData instanceof Ageable) {
                    if (CustomCrops.getInstance().getCropsTypes().get(type.toLowerCase()) != null) {
                        YamlConfiguration config = CustomCrops.getInstance().getCropsTypes().get(type.toLowerCase());

                        // todo: Add support for multiple drops
                        HashMap<Material, IntRange> cropDrops = new HashMap<>(); // Single drop for now
                        cropDrops.put(Material.valueOf(config.getString("drops.item.material")), Formatters.stringToRange(config.getString("drops.item.amount", "1-1")));

                        Crop crop = new Crop(
                                type,
                                cropDrops,
                                Formatters.stringToRange(config.getString("drops.seed.amount", "1-1")),
                                config.getInt("time"),
                                block.getLocation()
                        );

                        CropManager.addCrop(crop);
                    }
                }
            });
        }
    }

    private Crop getCrop(Location location) {
        for (Crop crop : CropManager.getCropList()) {
            if (crop.getLocation().equals(location)) {
                return crop;
            }
        }
        return null;
    }
}
