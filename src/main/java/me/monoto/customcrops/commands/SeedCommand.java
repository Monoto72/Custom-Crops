package me.monoto.customcrops.commands;

import me.monoto.customcrops.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SeedCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            if (args.length == 3) {
                Player player = Bukkit.getPlayer(args[0]);

                if (player == null) {
                    sender.sendMessage("Player not found");
                    return true;
                }

                int amount = Integer.parseInt(args[2]);

                if (!args[1].isEmpty()) {
                    if (amount > 0) {
                        if (hasAvailableSlot(player)) {
                            ItemStack seed = ItemManager.getSeed(args[1], amount);
                            player.getInventory().addItem(seed);
                            sender.sendMessage("You have received " + amount + "x " + args[1] + " seeds");
                        } else sender.sendMessage("Inventory is full");
                    } else sender.sendMessage("The amount must be greater than 0.");
                } else sender.sendMessage("Invalid seed type");
            } else sender.sendMessage("Usage: /seed <player> <seed type> <amount>");
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                completions.add(player.getName());
            }
        } else if (args.length == 2) {
            completions.add("Coal");
            completions.add("Iron");
            completions.add("Gold");
            completions.add("Diamond");
            completions.add("Emerald");
            completions.add("Redstone");
            completions.add("Lapis");
        } else if (args.length == 3) {
            completions = new ArrayList<>(Arrays.asList("1", "2", "4", "8", "16", "32", "64"));
        }

        return completions;
    }

    public boolean hasAvailableSlot(Player player) {
        Inventory inv = player.getInventory();

        for (ItemStack item: inv.getContents()) {
            if(item == null) {
                return true;
            }
        }

        return false;
    }
}
