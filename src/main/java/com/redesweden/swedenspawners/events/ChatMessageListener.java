package com.redesweden.swedenspawners.events;

import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedeneconomia.models.PlayerSaldo;
import com.redesweden.swedenspawners.data.EventosEspeciais;
import com.redesweden.swedenspawners.data.Players;
import com.redesweden.swedenspawners.functions.GerenciadorDeSpawner;
import com.redesweden.swedenspawners.models.EventoPlayerCompraDeSpawners;
import com.redesweden.swedenspawners.models.Spawner;
import com.redesweden.swedenspawners.models.SpawnerMeta;
import com.redesweden.swedenspawners.models.SpawnerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class ChatMessageListener implements Listener {
    @EventHandler
    public void onPlayerMessage(PlayerChatEvent e) {
        Player player = e.getPlayer();

        if(EventosEspeciais.getEventoInComprarQuantiaDeSpawnersByPlayer(player.getDisplayName()) != null) {
            EventoPlayerCompraDeSpawners evento = EventosEspeciais.getEventoInComprarQuantiaDeSpawnersByPlayer(player.getDisplayName());

            e.setCancelled(true);

            if(e.getMessage().equalsIgnoreCase("CANCELAR")) {
                EventosEspeciais.removePlayerFromComprarQuantiaDeSpawners(player);
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§cVocê cancelou a operação atual.");
                return;
            }

            String mensagem = e.getMessage();
            BigDecimal quantia;

            try {
                quantia = new ConverterQuantia(mensagem).emNumeros();
            } catch (Exception ex) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§cQuantia inválida");
                player.sendMessage("§7(Digite 'CANCELAR' para cancelar esta operação)");
                return;
            }

            SpawnerPlayer spawnerPlayer = Players.getPlayerByUuid(player.getUniqueId().toString());
            BigDecimal limite = spawnerPlayer.getLimite();
            if(limite.compareTo(quantia) < 0) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage(String.format("§cSeu limite de compra de máquinas é de %s.", new ConverterQuantia(limite).emLetras()));
                player.sendMessage("§7(Digite 'CANCELAR' para cancelar esta operação)");
                return;
            }

            PlayerSaldo playerSaldo = com.redesweden.swedeneconomia.data.Players.getPlayer(player.getDisplayName());
            BigDecimal saldo = (BigDecimal) playerSaldo.getSaldo(false);
            SpawnerMeta spawner = evento.getSpawnerMeta();
            BigDecimal spawnerPreco = spawner.getPreco();

            if(saldo.compareTo(spawnerPreco.multiply(quantia)) < 0) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§cVocê não tem saldo suficiente para comprar essa quantidade de spawners.");
                player.sendMessage("§7(Digite 'CANCELAR' para cancelar esta operação)");
                return;
            }

            ItemStack spawnerItem = spawner.getSpawner(quantia);
            AtomicReference<Boolean> inventarioJaContemSpawner = new AtomicReference<>(false);
            Arrays.stream(player.getInventory().getContents()).forEach(item -> {
                if(!inventarioJaContemSpawner.get()
                        && item != null
                        && item.getItemMeta() != null
                        && item.getItemMeta().getDisplayName() != null
                        && item.getItemMeta().getLore() != null
                        && item.getItemMeta().getDisplayName().equals(spawnerItem.getItemMeta().getDisplayName())
                        && item.getItemMeta().getLore().toArray()[0].equals(spawnerItem.getItemMeta().getLore().toArray()[0])
                        && item.getAmount() < 64) {
                    inventarioJaContemSpawner.set(true);
                    item.setAmount(item.getAmount() + 1);
                    player.updateInventory();
                }
            });

            if(!inventarioJaContemSpawner.get()) {
                try {
                    player.getInventory().setItem(player.getInventory().firstEmpty(), spawnerItem);
                } catch (Exception ex) {
                    player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                    player.sendMessage("§cVocê tem que ter pelo menos 1 slot vazio no seu inventário para efetuar a compra de um spawner.");
                    player.sendMessage("§7(Digite 'CANCELAR' para cancelar esta operação)");
                    return;
                }
            }

            EventosEspeciais.removePlayerFromComprarQuantiaDeSpawners(player);
            playerSaldo.subSaldo(spawnerPreco.multiply(quantia));
            player.playSound(player.getLocation(), Sound.VILLAGER_YES, 3.0F, 1F);
            player.sendMessage(String.format("§aVocê comprou §f%sx %s", new ConverterQuantia(quantia).emLetras(), spawner.getTitulo()));
            return;
        }

        if(EventosEspeciais.getEventoAdicionarAmigoByPlayer(player) != null) {
            Spawner spawner = EventosEspeciais.getEventoAdicionarAmigoByPlayer(player).getSpawner();
            e.setCancelled(true);

            if(e.getMessage().equalsIgnoreCase("CANCELAR")) {
                EventosEspeciais.removePlayerAdicionandoAmigo(player);
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§cVocê cancelou a operação atual.");
                return;
            }

            String nomeDoJogador = e.getMessage();

            if(nomeDoJogador.equals(player.getDisplayName())) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§cVocê não pode se adicionar como amigo.");
                player.sendMessage("§7(Digite 'CANCELAR' para cancelar esta operação)");
                return;
            }

            if(Players.getPlayerByName(nomeDoJogador) == null) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§cNenhum jogador com esse nome foi encontrado.");
                player.sendMessage("§7(Digite 'CANCELAR' para cancelar esta operação)");
                return;
            }

            try {
                spawner.addAmigo(Bukkit.getOfflinePlayer(nomeDoJogador).getUniqueId().toString(), nomeDoJogador);
            } catch (Exception ex) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage(ex.getMessage());
                player.sendMessage("§7(Digite 'CANCELAR' para cancelar esta operação)");
                return;
            }

            EventosEspeciais.removePlayerAdicionandoAmigo(player);
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 3.0F, 1F);
            player.sendMessage(String.format("§aVocê adicionou o jogador §e%s §aà lista de amigos de seu spawner.", nomeDoJogador));
            return;
        }

        if(EventosEspeciais.getPlayerSetandoMultiplicador(player) != null) {
            e.setCancelled(true);
            int quantia;

            if(e.getMessage().equalsIgnoreCase("CANCELAR")) {
                EventosEspeciais.removePlayerSetandoMultiplicador(player);
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§cVocê cancelou a operação atual.");
                return;
            }

            try {
              quantia = Integer.parseInt(e.getMessage());
            } catch (Exception ex) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§cA quantia inserida precisa ser um número.");
                player.sendMessage("§7(Digite 'CANCELAR' para cancelar esta operação)");
                return;
            }

            if(quantia <= 0 || quantia > 10) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§cA quantia precisa ser dentre 1 a 10.");
                player.sendMessage("§7(Digite 'CANCELAR' para cancelar esta operação)");
                return;
            }

            if(!player.hasPermission("swedenspawners.multiplicador." + quantia)) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage(String.format("§cVocê não tem permissão para definir seu multiplicador para %s", quantia));
                player.sendMessage("§7(Digite 'CANCELAR' para cancelar esta operação)");
                return;
            }

            EventosEspeciais.removePlayerSetandoMultiplicador(player);
            SpawnerPlayer spawnerPlayer = Players.getPlayerByName(player.getDisplayName());
            spawnerPlayer.setMultiplicador(quantia);
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 3.0F, 2F);
            player.sendMessage("§7Você definiu com sucesso seu multiplicador para §a" + quantia);
            return;
        }

        if(EventosEspeciais.getPlayerRemovendoSpawners(player) != null) {
            e.setCancelled(true);

            if(e.getMessage().equalsIgnoreCase("CANCELAR")) {
                EventosEspeciais.removePlayerRemovendoSpawners(player);
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§cVocê cancelou a operação atual.");
                return;
            }

            BigDecimal quantia;

            try {
                quantia = new ConverterQuantia(e.getMessage()).emNumeros();
            } catch (Exception ex) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§cQuantia inválida");
                player.sendMessage("§7(Digite 'CANCELAR' para cancelar esta operação)");
                return;
            }

            Spawner spawner = EventosEspeciais.getPlayerRemovendoSpawners(player).getSpawner();

            new GerenciadorDeSpawner(player, spawner).removerQuantia(quantia);
        }
    }
}
