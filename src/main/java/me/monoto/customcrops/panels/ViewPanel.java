package me.monoto.customcrops.panels;

import me.monoto.customcrops.ItemManager;
import me.monoto.customcrops.crops.Crop;
import me.monoto.customcrops.utils.Formatters;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.HashMap;

public class ViewPanel implements Listener {

    private HashMap<Player, Inventory> panelMap = new HashMap<>();

    public void init(Player player, Crop crop) {
        Inventory panel = Bukkit.createInventory(player, 27, Component.text(crop.getType() + " Seed"));

        initializeItems(panel, crop);
        player.openInventory(panel);

        panelMap.put(player, panel);
    }

    private void initializeItems(Inventory panel, Crop crop) {
        ItemStack fillerItem = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
        ItemMeta fillerMeta = fillerItem.getItemMeta();

        ItemStack seedItem = ItemManager.getSeed(crop.getType(), 1);
        ItemMeta seedMeta = seedItem.getItemMeta();

        if (fillerMeta != null) {
            fillerMeta.displayName(Component.text(""));
            fillerItem.setItemMeta(fillerMeta);
        }

        if (seedMeta != null) {
            seedMeta.displayName(Component.text(crop.getType() + " Seed"));
            seedMeta.lore(Collections.singletonList(Formatters.time(crop.getTimeToGrow())));
            seedItem.setItemMeta(seedMeta);
        }

        for (int i = 0; i < panel.getSize(); i++) {
            panel.setItem(i, fillerItem);
        }

        panel.setItem(13, seedItem);
    }

    @EventHandler
    private void onInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            panelMap.remove(player);
        }
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if (panelMap.containsKey(player)) {
                event.setCancelled(true);
            }
        }
    }

    public HashMap<Player, Inventory> getPanelMap() {
        return panelMap;
    }

    public void setPanelMap(HashMap<Player, Inventory> panelMap) {
        this.panelMap = panelMap;
    }
}
