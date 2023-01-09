package com.redesweden.swedenspawners.commands;

import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedenspawners.GUIs.ComprarSpawnersGUI;
import com.redesweden.swedenspawners.data.Players;
import com.redesweden.swedenspawners.data.SaleSpawners;
import com.redesweden.swedenspawners.models.SpawnerMeta;
import com.redesweden.swedenspawners.models.SpawnerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

public class SpawnersCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0) {
            if(!(sender instanceof Player)) {
                sender.sendMessage("§b§lSPAWNERS §e>> §cApenas players podem executar este comando.");
                return true;
            }

            Player player = (Player) sender;
            player.openInventory(new ComprarSpawnersGUI(player.getName()).get());
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 3.0F, 2.5F);
            return true;
        }

        if(args[0].equals("give") || args[0].equals("dar")) {
            if(sender instanceof Player && !sender.hasPermission("swedenspawners.admin")) {
                Player player = (Player) sender;
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§b§lSPAWNERS §e>> §cVocê não tem permissão para executar este comando.");
                return true;
            }

            if(args.length != 4) {
                if(sender instanceof Player) {
                    Player player = (Player) sender;
                    player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                }
                sender.sendMessage("§b§lSPAWNERS §e>> §cUso: /spawners dar <player> <tipo> <quantia>");
                return true;
            }

            Player alvo = Bukkit.getServer().getPlayer(args[1]);

            if(alvo == null || !alvo.isOnline()) {
                if(sender instanceof Player) {
                    Player player = (Player) sender;
                    player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                }
                sender.sendMessage(String.format("§b§lSPAWNERS §e>> §cNão foi possível encontrar um jogador online com o nick '%s'", args[1]));
                return true;
            }

            SpawnerMeta spawnerMeta = SaleSpawners.getSpawnerPorId(args[2].toUpperCase());

            if (spawnerMeta == null) {
                if(sender instanceof Player) {
                    Player player = (Player) sender;
                    player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                }
                sender.sendMessage("§b§lSPAWNERS §e>> §cNão foi possível encontrar um spawner desse tipo.");
                return true;
            }

            BigDecimal quantidade;

            try {
                quantidade = new ConverterQuantia(args[3]).emNumeros();
            } catch (Exception e) {
                if(sender instanceof Player) {
                    Player player = (Player) sender;
                    player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                }
                sender.sendMessage("§b§lSPAWNERS §e>> §cQuantia inválida.");
                return true;
            }

            alvo.getInventory().addItem(spawnerMeta.getSpawner(quantidade));
            if(sender instanceof Player) {
                Player player = (Player) sender;
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 3.0F, 2F);
            }
            sender.sendMessage(String.format("§b§lSPAWNERS §e>> §aVocê deu §e%s %s §apara §b%s§a.", new ConverterQuantia(quantidade).emLetras(), spawnerMeta.getTitulo(), alvo.getDisplayName()));
            return true;
        }

        return true;
    }
}
