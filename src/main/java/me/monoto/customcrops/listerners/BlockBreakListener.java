package me.monoto.customcrops.listerners;

import me.monoto.customcrops.CustomCrops;
import me.monoto.customcrops.utils.ItemManager;
import me.monoto.customcrops.crops.Crop;
import me.monoto.customcrops.crops.CropManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

import static org.bukkit.Bukkit.getPluginManager;

public class BlockBreakListener implements Listener {

    public BlockBreakListener(CustomCrops plugin){
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
                        event.setDropItems(false);
                        block.getWorld().dropItemNaturally(block.getLocation(), ItemManager.getSeed(crop.getType(), 1));

                        CropManager.getCropList().remove(crop);
                        Bukkit.getScheduler().cancelTask(crop.getTaskId());
                    }
                } else event.setCancelled(true);
            }


            if (block.getType() == Material.FERN && getCrop(block.getLocation()) != null) {
                Crop crop = getCrop(block.getLocation());

                if (crop != null) {
                    Random seedRand = new Random();
                    int seedAmount = seedRand.nextInt(crop.getSeedDrops().getMaximumInteger()) + crop.getSeedDrops().getMinimumInteger();

                    block.getWorld().dropItemNaturally(block.getLocation(), ItemManager.getSeed(crop.getType(), seedAmount));
                    crop.getItemDrops().forEach((material, intRange) -> {
                        System.out.println(intRange.getMaximumInteger());
                        Random dropRand = new Random();
                        int amount = dropRand.nextInt(intRange.getMaximumInteger()) + intRange.getMinimumInteger();
                        System.out.println(amount);

                        event.setDropItems(false);
                        block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(material, amount));
                    });

                    CropManager.getCropList().remove(crop);
                    Bukkit.getScheduler().cancelTask(crop.getTaskId());
                }
            }
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
