package me.monoto.customcrops.listerners;

import me.monoto.customcrops.CustomCrops;
import me.monoto.customcrops.ItemManager;
import me.monoto.customcrops.crops.Crop;
import me.monoto.customcrops.crops.CropManager;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

import static org.bukkit.Bukkit.getPluginManager;

public class CropBreak implements Listener {

    public CropBreak(CustomCrops plugin){
        getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayer(BlockBreakEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
            Block block = event.getBlock();

            if (block.getType() == Material.WHEAT && getCrop(block.getLocation()) != null) {
                if (event.getPlayer().isSneaking()) {
                    Crop crop = getCrop(block.getLocation());

                    if (crop != null) {
                        CropManager.getCropList().remove(crop);
                        event.getBlock().getWorld().dropItemNaturally(block.getLocation(), ItemManager.getSeed(crop.getType(), 1));
                    }
                } else event.setCancelled(true);
            }


            if (block.getType() == Material.FERN && getCrop(block.getLocation()) != null) {
                Crop crop = getCrop(block.getLocation());

                if (crop != null) {
                    CropManager.getCropList().remove(crop);

                    block.getWorld().dropItemNaturally(event.getBlock().getLocation(), ItemManager.getSeed(crop.getType(), 1));
                    crop.getDrops().forEach((material, intRange) -> {
                        Random random = new Random();
                        int amount = random.nextInt(intRange.getMaximumInteger()) + intRange.getMinimumInteger();

                        block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(material, amount));
                    });
                }
            }
        }
    }
    /*
    @EventHandler
    public void onPhysics(BlockPhysicsEvent event) {
        if (getCrop(event.getSourceBlock().getLocation()) != null) {
            event.setCancelled(true);
        }
    }

     */

    private Crop getCrop(Location location) {
        for (Crop crop : CropManager.getCropList()) {
            if (crop.getLocation().equals(location)) {
                return crop;
            }
        }
        return null;
    }
}
