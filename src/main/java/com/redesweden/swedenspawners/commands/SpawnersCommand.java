package com.redesweden.swedenspawners.commands;

import com.redesweden.swedenspawners.GUIs.ComprarSpawnersGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnersCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cEsse comando pode ser executado apenas por players.");
            return true;
        }

        Player player = (Player) sender;
        player.openInventory(new ComprarSpawnersGUI(player.getDisplayName()).get());
        return true;
    }
}
