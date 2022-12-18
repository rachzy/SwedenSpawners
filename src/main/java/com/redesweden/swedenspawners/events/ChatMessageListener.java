package com.redesweden.swedenspawners.events;

import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedeneconomia.models.PlayerSaldo;
import com.redesweden.swedenspawners.data.EventosEspeciais;
import com.redesweden.swedenspawners.data.Players;
import com.redesweden.swedenspawners.models.EventoPlayerCompraDeSpawners;
import com.redesweden.swedenspawners.models.SpawnerMeta;
import com.redesweden.swedenspawners.models.SpawnerPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class ChatMessageListener implements Listener {
    @EventHandler
    public void onPlayerMessage(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        EventoPlayerCompraDeSpawners evento = EventosEspeciais.getEventoInComprarQuantiaDeSpawnersByPlayer(player.getDisplayName());

        if(evento == null) return;
        EventosEspeciais.removePlayerFromComprarQuantiaDeSpawners(player);
        e.setCancelled(true);

        String mensagem = e.getMessage();
        BigDecimal quantia;

        try {
            quantia = new ConverterQuantia(mensagem).emNumeros();
        } catch (Exception ex) {
            player.sendMessage("§cQuantia inválida");
            return;
        }

        SpawnerPlayer spawnerPlayer = Players.getPlayerByUuid(player.getUniqueId().toString());
        BigDecimal limite = spawnerPlayer.getLimite();
        if(limite.compareTo(quantia) < 0) {
            player.sendMessage(String.format("§cSeu limite de compra de máquinas é de %s.", new ConverterQuantia(limite).emLetras()));
            return;
        }

        PlayerSaldo playerSaldo = com.redesweden.swedeneconomia.data.Players.getPlayer(player.getDisplayName());
        BigDecimal saldo = (BigDecimal) playerSaldo.getSaldo(false);
        SpawnerMeta spawner = evento.getSpawnerMeta();
        BigDecimal spawnerPreco = spawner.getPreco();

        if(saldo.compareTo(spawnerPreco.multiply(quantia)) < 0) {
            player.sendMessage("§cVocê não tem saldo suficiente para comprar essa quantidade de spawners.");
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
                player.sendMessage("§cVocê tem que ter pelo menos 1 slot vazio no seu inventário para efetuar a compra de um spawner.");
                return;
            }
        }

        playerSaldo.subSaldo(spawnerPreco.multiply(quantia));
        player.sendMessage(String.format("§aVocê comprou §f%sx %s", new ConverterQuantia(quantia).emLetras(), spawner.getTitle()));
    }
}
