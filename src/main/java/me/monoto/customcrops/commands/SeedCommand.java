package me.monoto.customcrops.commands;

import me.monoto.customcrops.CustomCrops;
import me.monoto.customcrops.utils.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SeedCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.isOp() || sender instanceof ConsoleCommandSender || (command.getPermission() != null && sender.hasPermission(command.getPermission()))) {
            if (args.length == 3) {
                Player player = Bukkit.getPlayer(args[0]);

                if (player == null) {
                    sender.sendMessage("Player not found");
                    return true;
                }

                int amount = Integer.parseInt(args[2]);

                if (CustomCrops.getInstance().getCropsTypes().get(args[1].toLowerCase()) != null) {
                    if (amount > 0) {
                        if (hasAvailableSlot(player)) {
                            ItemStack seed = ItemManager.getSeed(args[1], amount);
                            player.getInventory().addItem(seed);
                            sender.sendMessage("You have received " + amount + "x " + args[1] + " seeds");
                        } else sender.sendMessage("Inventory is full");
                    } else sender.sendMessage("The amount must be greater than 0.");
                } else sender.sendMessage("Invalid seed type");
            } else sender.sendMessage("Usage: /" + command.getName() + " <player> <seed type> <amount>");
        } else sender.sendMessage("You do not have permission to use this command");
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
            completions.addAll(CustomCrops.getInstance().getCropsTypes().keySet());
        } else if (args.length == 3) {
            completions.addAll(Arrays.asList("1", "2", "4", "8", "16", "32", "64"));
        }

        Collections.sort(completions);

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
