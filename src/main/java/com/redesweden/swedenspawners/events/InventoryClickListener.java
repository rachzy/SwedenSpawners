package com.redesweden.swedenspawners.events;

import com.redesweden.swedeneconomia.data.Players;
import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedeneconomia.models.PlayerSaldo;
import com.redesweden.swedenspawners.data.EventosEspeciais;
import com.redesweden.swedenspawners.data.SaleSpawners;
import com.redesweden.swedenspawners.models.SpawnerMeta;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        // Retorne caso o invetário não seja a GUI de spawners
        String viewTitle = e.getView().getTitle().substring(2).toUpperCase();

        if (viewTitle.equals("SPAWNERS")) {
            e.setCancelled(true);

            // Retorne caso o player não tenha clicado em nenhum item
            if (e.getCurrentItem() == null
                    || e.getCurrentItem().getItemMeta() == null
                    || e.getCurrentItem().getItemMeta().getDisplayName() == null) return;

            // Retorne caso o player não tenha clicado em um spawner
            String spawnerTitle = e.getCurrentItem().getItemMeta().getDisplayName();
            SpawnerMeta spawner = SaleSpawners.getSpawnerPorTitulo(spawnerTitle);
            if (spawner == null) return;

            Player player = (Player) e.getWhoClicked();

            if (e.getClick().isLeftClick()) {
                player.closeInventory();
                EventosEspeciais.addPlayerToComprarQuantiaDeSpawners(player, spawner);
                player.sendMessage("");
                player.sendMessage(" §aDigite a quantia de spawners que você deseja comprar: ");
                player.sendMessage("");
                return;
            }

            PlayerSaldo playerSaldo = Players.getPlayer(player.getDisplayName());
            BigDecimal saldo = (BigDecimal) playerSaldo.getSaldo(false);
            BigDecimal spawnerPreco = spawner.getPreco();
            BigDecimal quantidade = new BigDecimal("1");
            if (e.getClick().isKeyboardClick()) {
                quantidade = com.redesweden.swedenspawners.data.Players.getPlayerByUuid(player.getUniqueId().toString()).getLimite();
            }
            BigDecimal precoFinal = spawnerPreco.multiply(quantidade);
            ItemStack spawnerItem = spawner.getSpawner(quantidade);

            if (saldo.compareTo(precoFinal) < 0) {
                BigDecimal diff = precoFinal.subtract(saldo);
                player.sendMessage(String.format("§cVocê precisa de $%s para comprar essa quantia deste spawner.", new ConverterQuantia(diff).emLetras()));
                return;
            }

            AtomicReference<Boolean> inventarioJaContemSpawner = new AtomicReference<>(false);
            Arrays.stream(player.getInventory().getContents()).forEach(item -> {
                if (!inventarioJaContemSpawner.get()
                        && item != null
                        && item.hasItemMeta()
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

            if (!inventarioJaContemSpawner.get()) {
                try {
                    player.getInventory().setItem(player.getInventory().firstEmpty(), spawnerItem);
                } catch (Exception ex) {
                    player.sendMessage("§cVocê tem que ter pelo menos 1 slot vazio no seu inventário para efetuar a compra deste spawner.");
                    return;
                }
            }

            playerSaldo.subSaldo(precoFinal);
            player.sendMessage(String.format("§aVocê comprou §f%s %s", new ConverterQuantia(quantidade).emLetras(), spawner.getTitle()));
            return;
        }
    }
}
