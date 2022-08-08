package me.monoto.customcrops.listerners;

import me.monoto.customcrops.CustomCrops;
import me.monoto.customcrops.crops.Crop;
import me.monoto.customcrops.crops.CropManager;
import me.monoto.customcrops.panels.ViewPanel;
import org.apache.commons.lang.math.IntRange;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.bukkit.Bukkit.getPluginManager;

public class CropPlace implements Listener {

    public CropPlace(CustomCrops plugin){
        getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void on(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;

        if (getCrop(event.getClickedBlock().getLocation()) != null) {
            new ViewPanel().init(event.getPlayer(), Objects.requireNonNull(getCrop(event.getClickedBlock().getLocation())));
            return;
        }

        if (event.getItem() == null) return;

        ItemStack item = event.getItem();
        PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();

        if (data.has(new NamespacedKey(CustomCrops.getInstance(), "CUSTOM_CROP"), PersistentDataType.STRING)) {
            Bukkit.getScheduler().runTask(CustomCrops.getInstance(), () -> {
                String type = data.get(new NamespacedKey(CustomCrops.getInstance(), "CUSTOM_CROP"), PersistentDataType.STRING);
                Block block = event.getClickedBlock().getRelative(BlockFace.UP);
                BlockData blockData = block.getBlockData();
                if (blockData instanceof Ageable) {
                    HashMap<Material, IntRange> cropDrops = new HashMap<>();

                    cropDrops.put(Material.COAL, new IntRange(1, 3));

                    Crop crop = new Crop(
                            type,
                            cropDrops,
                            60,
                            block.getLocation()
                    );

                    CropManager.addCrop(crop);
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
