package com.redesweden.swedenspawners.commands;

import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedenspawners.data.Players;
import com.redesweden.swedenspawners.models.ChequeLimite;
import com.redesweden.swedenspawners.models.SpawnerPlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class LimiteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = null;
        SpawnerPlayer spawnerPlayer = null;
        SpawnerPlayer alvo = null;
        BigDecimal quantia = null;

        // Condição caso o comando a ser executado deva ser executado apenas por players
        if (args.length == 0 || args[0].equals("enviar") || args[0].equals("send") || args[0].equals("cheque")) {
            // Caso o sender não seja um player
            if (!(sender instanceof Player)) {
                sender.sendMessage("§c§lLIMITE §e>> §cApenas players podem executar este comando.");
                return true;
            }

            // Setar as variáveis que podem ser utilizadas em várias partes do código
            player = (Player) sender;
            spawnerPlayer = Players.getPlayerByName(player.getName());
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
                sender.sendMessage(String.format("§c§lLIMITE §e>> §cNão foi possível encontrar um jogador com o nick '%s'", args[1]));
                return true;
            }

            try {
                quantia = new ConverterQuantia(args[2]).emNumeros();
            } catch (Exception e) {
                sender.sendMessage("§c§lLIMITE §e>> §cQuantia inválida.");
                return true;
            }
        }

        // Condição para o comando "/limite"
        if (args.length == 0) {
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 3.0F, 2F);
            player.sendMessage("§c§lLIMITE §e>> §7Seu limite de compra de Spawners é de §c✤" + new ConverterQuantia(spawnerPlayer.getLimite()).emLetras());
            return true;
        }

        // Condição para o comando "/limite enviar"
        if (args[0].equals("send") || args[0].equals("enviar")) {
            if (args.length != 3) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§c§lLIMITE §e>> §cUse: /limite enviar <player> <quantia>");
                return true;
            }

            if (alvo.getNickname().equals(player.getName())) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§c§lLIMITE §e>> §cVocê não pode enviar limites de compra para si mesmo");
                return true;
            }

            if (spawnerPlayer.getLimite().compareTo(quantia) < 0) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§c§lLIMITE §e>> §cVocê não possui limites suficientes para efetuar este envio.");
                return true;
            }

            alvo.addLimite(quantia);
            spawnerPlayer.subLimite(quantia);
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 3.0F, 2F);
            player.sendMessage(String.format("§c§lLIMITE §e>> §aVocê enviou §c✤%s §ade limites de compra para §b%s", new ConverterQuantia(quantia).emLetras(), alvo.getNickname()));

            Player playerAlvo = player.getServer().getPlayer(alvo.getNickname());
            if (playerAlvo != null && playerAlvo.isOnline()) {
                playerAlvo.playSound(playerAlvo.getLocation(), Sound.LEVEL_UP, 3.0F, 2F);
                playerAlvo.sendMessage(String.format("§c§lLIMITE §e>> §aO jogador §b%s §ate enviou §c✤%s §ade limites de compra.", player.getName(), new ConverterQuantia(quantia).emLetras()));
            }
            return true;
        }

        // Condição para o comando "/limite set"
        if (args[0].equals("set") || args[0].equals("setar")) {
            if (sender instanceof Player && !sender.hasPermission("swedenspawners.admin")) {
                Player playerIn = (Player) sender;
                playerIn.playSound(playerIn.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                sender.sendMessage("§c§lLIMITE §e>> §cVocê não tem permissão para utilizar este comando.");
                return true;
            }

            if (args.length != 3) {
                if (sender instanceof Player) {
                    Player playerIn = (Player) sender;
                    playerIn.playSound(playerIn.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                }
                sender.sendMessage("§c§lLIMITE §e>> §cUse: /limite setar <jogador> <quantia>");
                return true;
            }

            alvo.setLimite(quantia);
            if (sender instanceof Player) {
                Player playerIn = (Player) sender;
                playerIn.playSound(playerIn.getLocation(), Sound.NOTE_PLING, 3.0F, 2F);
            }
            sender.sendMessage(String.format("§c§lLIMITE §e>> §aVocê setou a quantidade de limites de compra do jogador §b%s §apara §c✤%s", alvo.getNickname(), new ConverterQuantia(quantia).emLetras()));
            return true;
        }

        // Condição para o comando "/limite give"
        if (args[0].equals("give") || args[0].equals("dar") || args[0].equals("add")) {
            if (sender instanceof Player && !sender.hasPermission("swedenspawners.admin")) {
                Player playerIn = (Player) sender;
                playerIn.playSound(playerIn.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                sender.sendMessage("§c§lLIMITE §e>> §cVocê não tem permissão para utilizar este comando.");
                return true;
            }

            if (args.length != 3) {
                if (sender instanceof Player) {
                    Player playerIn = (Player) sender;
                    playerIn.playSound(playerIn.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                }
                sender.sendMessage("§c§lLIMITE §e>> §cUse: /limite dar <jogador> <quantia>");
                return true;
            }

            alvo.addLimite(quantia);
            if (sender instanceof Player) {
                Player playerIn = (Player) sender;
                playerIn.playSound(playerIn.getLocation(), Sound.NOTE_PLING, 3.0F, 2F);
            }
            sender.sendMessage(String.format("§c§lLIMITE §e>> §aVocê adicionou §c✤%s §ade limites ao jogador §b%s", new ConverterQuantia(quantia).emLetras(), alvo.getNickname()));
            return true;
        }

        if(args[0].equals("cheque")) {
            if(args.length != 2) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§c§lLIMITE §e>> §cUso: /limite cheque <quantia>");
                return true;
            }

            BigDecimal valor;

            try {
                valor = new ConverterQuantia(args[1]).emNumeros();
            } catch (Exception e) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§c§lLIMITE §e>> §cQuantia inválida.");
                return true;
            }

            if(spawnerPlayer.getLimite().compareTo(valor) < 0) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§c§lLIMITE §e>> §cVocê não tem limites suficientes para criar um cheque neste valor.");
                return true;
            }

            ItemStack cheque = new ChequeLimite(player.getName(), valor, LocalDateTime.now()).gerar();

            try {
                player.getInventory().setItem(player.getInventory().firstEmpty(), cheque);
            } catch (Exception e) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§c§lLIMITE §e>> §cVocê precisa ter pelo menos 1 slot vazio em seu inventário para gerar um cheque.");
                return true;
            }

            spawnerPlayer.subLimite(valor);
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 3.0F, 2F);
            player.sendMessage(String.format("§c§lLIMITE §e>> §aVocê gerou um cheque no valor de §c✤%s§a.", new ConverterQuantia(valor).emLetras()));
            return true;
        }

        // Condição para o comando "/imite help"
        if (args[0].equals("help") || args[0].equals("ajuda")) {
            if (sender instanceof Player) {
                Player playerIn = (Player) sender;
                playerIn.playSound(playerIn.getLocation(), Sound.LEVEL_UP, 3.0F, 2.5F);
            }
            sender.sendMessage("");
            sender.sendMessage(" §c§lLIMITES ✤");
            sender.sendMessage("");
            sender.sendMessage(" §e- §9Os limites, como o próprio nome indica, limitam a quantidade de spawners que você pode comprar de uma só vez.");
            sender.sendMessage(" §e- §9Você pode obter limites por meio de §e§lMÁQUINAS§9 ou §b§lMINERANDO§9.");
            sender.sendMessage(" §e- §9Utilize §c/limite comandos §9para ver uma lista de comandos disponíveis.");
            sender.sendMessage("");
            return true;
        }

        if (args[0].equals("commands") || args[0].equals("comandos")) {
            if (sender instanceof Player) {
                Player playerIn = (Player) sender;
                playerIn.playSound(playerIn.getLocation(), Sound.NOTE_PLING, 3.0F, 2.5F);
            }
            sender.sendMessage("");
            sender.sendMessage(" §c§lLIMITES §6- §b§lCOMANDOS");
            sender.sendMessage("");
            sender.sendMessage(" §e- §a/limite <player>: §7Mostra quanto de limite um jogador possui.");
            sender.sendMessage(" §e- §a/limite enviar <player> <quantia>: §7Envia limites à um jogador.");
            if (sender.hasPermission("swedenspawners.admin")) {
                sender.sendMessage(" §e- §c/limite setar <player>: §7Seta os limites de um jogador.");
                sender.sendMessage(" §e- §c/limite dar <player> <quantia>: §7Dá limites à um jogdaor.");
            }
            sender.sendMessage("");
            return true;
        }

        // Condição para caso o comando seja "/limite <player>"
        alvo = Players.getPlayerByName(args[0]);
        if (alvo == null) {
            if (sender instanceof Player) {
                Player playerIn = (Player) sender;
                playerIn.playSound(playerIn.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
            }
            sender.sendMessage(String.format("§c§lLIMITE §e>> §cNão foi possível encontrar um jogador com o nick '%s'", args[0]));
            return true;
        }

        if (sender instanceof Player && alvo.getNickname().equals(((Player) sender).getName())) {
            Player playerIn = (Player) sender;
            playerIn.playSound(playerIn.getLocation(), Sound.NOTE_PLING, 3.0F, 2F);
            sender.sendMessage("§c§lLIMITE §e>> §aSeu limite de compra de Spawners é de §c✤" + new ConverterQuantia(alvo.getLimite()).emLetras());
            return true;
        }

        if (sender instanceof Player) {
            Player playerIn = (Player) sender;
            playerIn.playSound(playerIn.getLocation(), Sound.NOTE_PLING, 3.0F, 2F);
        }
        sender.sendMessage(String.format("§c§lLIMITE §e>> §e%s §7possui §c✤%s §7de limite de compra.", alvo.getNickname(), new ConverterQuantia(alvo.getLimite()).emLetras()));
        return true;
    }
}
