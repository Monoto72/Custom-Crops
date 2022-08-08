package me.monoto.customcrops.crops;

import me.monoto.customcrops.CustomCrops;
import org.apache.commons.lang.math.IntRange;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class Crop {
    private String type;
    private HashMap<Material, IntRange> drops;
    private Integer timeToGrow;
    private final Location location;
    private Integer age;


    public Crop(String type, HashMap<Material, IntRange> drops, Integer timeToGrow, Location location) {
        this.type = type;
        this.drops = drops;
        this.timeToGrow = timeToGrow;
        this.location = location;
        this.age = location.getBlock().getBlockData() instanceof Ageable ? ((Ageable) location.getBlock().getBlockData()).getAge() : 0;

        setTimeToGrow(timeToGrow);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HashMap<Material, IntRange> getDrops() {
        return drops;
    }

    public void setDrops(HashMap<Material, IntRange> drops) {
        this.drops = drops;
    }

    public Integer getTimeToGrow() {
        return timeToGrow;
    }

    public void setTimeToGrow(Integer timeToGrow) {
        int time = timeToGrow / 6;

        new BukkitRunnable() {
            private int state = 1;

            @Override
            public void run() {
                if (getLocation().getBlock().getBlockData() instanceof Ageable) {

                    switch (state) {
                        case 1, 2, 3, 4, 5, 6 -> {
                            Ageable ageable = (Ageable) getLocation().getBlock().getBlockData();
                            ageable.setAge(state);
                            getLocation().getBlock().setBlockData(ageable);
                        }
                        case 7 -> {
                            getLocation().getBlock().setType(Material.FERN);
                            this.cancel();
                        }
                    }

                    setAge(state);
                    state++;
                } else CropManager.getCropList().remove(Crop.this);
            }
        }.runTaskTimer(CustomCrops.getInstance(), 0, 20L * time);

        // todo: add a timer runnable for upcoming hologram
    }

    public Location getLocation() {
        return location;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
