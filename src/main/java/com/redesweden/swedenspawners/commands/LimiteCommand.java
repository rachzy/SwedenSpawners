package com.redesweden.swedenspawners.commands;

import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedenspawners.data.Players;
import com.redesweden.swedenspawners.models.SpawnerPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

public class LimiteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = null;
        SpawnerPlayer spawnerPlayer = null;
        SpawnerPlayer alvo = null;
        BigDecimal quantia = null;

        // Condição caso o comando a ser executado deva ser executado apenas por players
        if (args.length == 0 || args[0].equals("enviar") || args[0].equals("send")) {
            // Caso o sender não seja um player
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cApenas players podem executar este comando.");
                return true;
            }

            // Setar as variáveis que podem ser utilizadas em várias partes do código
            player = (Player) sender;
            spawnerPlayer = Players.getPlayerByName(player.getDisplayName());
        }

        // Condição caso o comando tenha a estrutura /<commando> <arg> <player> <quantia>
        if (args.length == 3 &&
                (args[0].equals("enviar")
                        || args[0].equals("send")
                        || args[0].equals("set")
                        || args[0].equals("setar")
                        || args[0].equals("give")
                        || args[0].equals("dar")
                        || args[0].equals("add"))
        ) {
            alvo = Players.getPlayerByName(args[1]);
            if (alvo == null) {
                sender.sendMessage(String.format("§cNão foi possível encontrar um jogador com o nick '%s'", args[1]));
                return true;
            }

            try {
                quantia = new ConverterQuantia(args[2]).emNumeros();
            } catch (Exception e) {
                sender.sendMessage("§cQuantia inválida.");
                return true;
            }
        }

        // Condição para o comando "/limite"
        if (args.length == 0) {
            player.sendMessage("§bVocê possui um limite de compra de Spawners de §f" + new ConverterQuantia(spawnerPlayer.getLimite()).emLetras());
            return true;
        }

        // Condição para o comando "/limites enviar"
        if (args[0].equals("enviar") || args[0].equals("send")) {
            if (args.length != 3) {
                player.sendMessage("§cUse: /limite enviar <player> <quantia>");
                return true;
            }

            if (alvo.getNickname().equals(player.getDisplayName())) {
                player.sendMessage("§cVocê não pode enviar limites de compra para si mesmo");
                return true;
            }

            if (spawnerPlayer.getLimite().compareTo(quantia) < 0) {
                player.sendMessage("§cVocê não possui limites suficientes para efetuar este envio.");
                return true;
            }

            alvo.addLimite(quantia);
            spawnerPlayer.subLimite(quantia);
            player.sendMessage(String.format("§bVocê enviou §f%s §bde limites de compra para §a%s", new ConverterQuantia(quantia).emLetras(), alvo.getNickname()));

            Player playerAlvo = player.getServer().getPlayer(alvo.getNickname());
            if (playerAlvo != null && playerAlvo.isOnline()) {
                playerAlvo.sendMessage(String.format("§bO jogador §a%s §bte enviou §f%s §bde limites de compra.", player.getDisplayName(), new ConverterQuantia(quantia).emLetras()));
            }
            return true;
        }

        // Condição para o comando "/limites set"
        if (args[0].equals("set") || args[0].equals("setar")) {
            if (sender instanceof Player && !sender.hasPermission("swedenspawners.admin")) {
                sender.sendMessage("§cVocê não tem permissão para utilizar este comando.");
                return true;
            }

            if (args.length != 3) {
                sender.sendMessage("§cUse: /limite setar <jogador> <quantia>");
                return true;
            }

            alvo.setLimite(quantia);
            sender.sendMessage(String.format("§bVocê setou a quantidade de limites de compra do jogador §a%s para §f%s", alvo.getNickname(), new ConverterQuantia(quantia).emLetras()));
            return true;
        }

        // Condição para o comando "/imites give"
        if (args[0].equals("give") || args[0].equals("dar") || args[0].equals("add")) {
            if (sender instanceof Player && !sender.hasPermission("swedenspawners.admin")) {
                sender.sendMessage("§cVocê não tem permissão para utilizar este comando.");
                return true;
            }

            if (args.length != 3) {
                sender.sendMessage("§cUse: /limite dar <jogador> <quantia>");
                return true;
            }

            alvo.addLimite(quantia);
            sender.sendMessage(String.format("§bVocê adicionou §f%s §bde limites ao jogador §a%s", new ConverterQuantia(quantia).emLetras(), alvo.getNickname()));
            return true;
        }

        // Condição para caso o comando seja "/limite <player>"
        alvo = Players.getPlayerByName(args[0]);
        if(alvo == null) {
            sender.sendMessage(String.format("§cNão foi possível encontrar um jogador com o nick '%s'", args[0]));
            return true;
        }

        if(sender instanceof Player && alvo.getNickname().equals(((Player) sender).getDisplayName())) {
            sender.sendMessage("§bVocê possui um limite de compra de Spawners de §f" + new ConverterQuantia(alvo.getLimite()).emLetras());
            return true;
        }

        sender.sendMessage(String.format("§a%s §bpossui §f%s §bde limite de compra.", alvo.getNickname(), new ConverterQuantia(alvo.getLimite()).emLetras()));
        return true;
    }
}
