package com.redesweden.swedenspawners.events;

import com.redesweden.swedeneconomia.data.Players;
import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedeneconomia.models.PlayerSaldo;
import com.redesweden.swedenspawners.GUIs.GerenciarDropsGUI;
import com.redesweden.swedenspawners.GUIs.GerenciarManagerGUI;
import com.redesweden.swedenspawners.GUIs.GerenciarManagersGUI;
import com.redesweden.swedenspawners.GUIs.SpawnerGUI;
import com.redesweden.swedenspawners.data.EventosEspeciais;
import com.redesweden.swedenspawners.data.SaleSpawners;
import com.redesweden.swedenspawners.models.Spawner;
import com.redesweden.swedenspawners.models.SpawnerManager;
import com.redesweden.swedenspawners.models.SpawnerMeta;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        // Retorne caso o invetário não seja a GUI de spawners
        String viewTitle = e.getView().getTitle().substring(2).toUpperCase();
        Player player = (Player) e.getWhoClicked();

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

        if (viewTitle.equals("GERENCIAR SPAWNER")) {
            e.setCancelled(true);

            if (e.getCurrentItem() == null
                    || !e.getCurrentItem().hasItemMeta()
                    || e.getCurrentItem().getItemMeta().getDisplayName() == null) return;

            String nomeDoItem = e.getCurrentItem().getItemMeta().getDisplayName().substring(2).toUpperCase();
            Spawner spawner = EventosEspeciais.getEventoGerenciarSpawnerByPlayer(player).getSpawner();

            if (nomeDoItem.equals("GERENCIAR DROPS")) {
                player.openInventory(new GerenciarDropsGUI(spawner).get());
                return;
            }

            if (nomeDoItem.equals("GERENCIAR MANAGERS")) {
                player.openInventory(new GerenciarManagersGUI(player.getDisplayName(), spawner).get());
                return;
            }

            if (nomeDoItem.equals("LIGAR OU DESLIGAR")) {
                spawner.toggleAtivado();

                if (spawner.getAtivado()) {
                    player.sendMessage("§aSpawner ativado com sucesso!");
                } else {
                    player.sendMessage("§cSpawner desligado com sucesso.");
                }
                player.closeInventory();
            }
        }

        if (viewTitle.equals("DROPS")) {
            e.setCancelled(true);

            if (e.getCurrentItem() == null
                    || !e.getCurrentItem().hasItemMeta()
                    || e.getCurrentItem().getItemMeta().getDisplayName() == null) return;

            String nomeDoItem = e.getCurrentItem().getItemMeta().getDisplayName().substring(2).toUpperCase();
            Spawner spawner = EventosEspeciais.getEventoGerenciarSpawnerByPlayer(player).getSpawner();

            if (nomeDoItem.equals("VOLTAR")) {
                player.openInventory(new SpawnerGUI(spawner).get());
                return;
            }

            player.closeInventory();

            if (!spawner.getDono().getNickname().equals(player.getDisplayName())) {
                SpawnerManager manager = spawner.getManagerPorNome(player.getDisplayName());

                if (manager == null || !manager.getPermissaoVender()) {
                    player.sendMessage("§cVocê não tem permissão para gerenciar os drops deste spawner.");
                    return;
                }
            }

            if (nomeDoItem.equals("DROPS DO SPAWNER")) {
                if (Objects.equals(spawner.getDropsAramazenados(), new BigDecimal("0"))) {
                    player.sendMessage("§cEste spawner não tem nenhum drop para vender.");
                    return;
                }
                PlayerSaldo playerSaldo = Players.getPlayer(player.getDisplayName());
                BigDecimal valorDaVenda = spawner.getDropsAramazenados().multiply(spawner.getSpawnerMeta().getPrecoPorDrop());
                playerSaldo.addSaldo(valorDaVenda);
                playerSaldo.addQuantiaMovimentada(valorDaVenda);
                player.sendMessage(String.format("§aVocê vendeu %s de drops deste spawner por $§f%s§a.", new ConverterQuantia(spawner.getDropsAramazenados()).emLetras(), new ConverterQuantia(valorDaVenda).emLetras()));
                spawner.zerarDropsArmazenados();
                return;
            }

            if (nomeDoItem.equals("LIMPAR DROPS")) {
                player.sendMessage("§cVocê limpou os drops deste spawner.");
                spawner.zerarDropsArmazenados();
            }
        }

        if (viewTitle.equals("MANAGERS")) {
            e.setCancelled(true);

            if (e.getCurrentItem() == null
                    || !e.getCurrentItem().hasItemMeta()
                    || e.getCurrentItem().getItemMeta().getDisplayName() == null) return;

            String nomeDoItem = e.getCurrentItem().getItemMeta().getDisplayName().substring(2).toUpperCase();
            Spawner spawner = EventosEspeciais.getEventoGerenciarSpawnerByPlayer(player).getSpawner();

            if (nomeDoItem.equals("VOLTAR")) {
                player.openInventory(new SpawnerGUI(spawner).get());
                return;
            }

            player.closeInventory();

            if (!spawner.getDono().getNickname().equals(player.getDisplayName())) {
                player.sendMessage("§cVocê não tem permissão para gerenciar os managers deste spawner.");
                return;
            }

            if (nomeDoItem.equals("ADICIONAR MANAGER")) {
                EventosEspeciais.addPlayerAdicionandoManager(player, spawner);
                player.sendMessage("");
                player.sendMessage(" §aDigite o nome do player que você deseja adicionar como manager.");
                player.sendMessage("");
                return;
            }

            SpawnerManager manager = spawner.getManagerPorNome(nomeDoItem);

            if(manager != null) {
                player.openInventory(new GerenciarManagerGUI(spawner, manager).get());
            }
        }

        if(viewTitle.equals("MANAGER")) {
            e.setCancelled(true);

            if (e.getCurrentItem() == null
                    || !e.getCurrentItem().hasItemMeta()
                    || e.getCurrentItem().getItemMeta().getDisplayName() == null) return;

            String nomeDoItem = e.getCurrentItem().getItemMeta().getDisplayName().substring(2).toUpperCase();
            Spawner spawner = EventosEspeciais.getEventoGerenciarSpawnerByPlayer(player).getSpawner();

            if (nomeDoItem.equals("VOLTAR")) {
                player.openInventory(new GerenciarManagersGUI(player.getDisplayName(), spawner).get());
                return;
            }

            if (!spawner.getDono().getNickname().equals(player.getDisplayName())) {
                player.sendMessage("§cVocê não tem permissão para gerenciar os managers deste spawner.");
                return;
            }

            String managerNick = e.getInventory().getItem(10).getItemMeta().getDisplayName().substring(2);
            SpawnerManager managerAlvo = spawner.getManagerPorNome(managerNick);

            if(nomeDoItem.equals("PERMISSÃO DE VENDER")) {
                managerAlvo.togglePermissaoVender(spawner);
                player.openInventory(new GerenciarManagerGUI(spawner, managerAlvo).get());
                return;
            }

            if(nomeDoItem.equals("PERMISSÃO DE MATAR")) {
                managerAlvo.togglePermissaoMatar(spawner);
                player.openInventory(new GerenciarManagerGUI(spawner, managerAlvo).get());
                return;
            }

            if(nomeDoItem.equals("PERMISSÃO DE QUEBRAR")) {
                managerAlvo.togglePermissaoQuebrar(spawner);
                player.openInventory(new GerenciarManagerGUI(spawner, managerAlvo).get());
                return;
            }

            if(nomeDoItem.equals("REMOVER MANAGER")) {
                spawner.removerManager(managerAlvo);
                player.openInventory(new GerenciarManagersGUI(player.getDisplayName(), spawner).get());
                player.sendMessage(String.format("§cVocê removeu %s da lista de Managers do seu spawner.", managerAlvo.getNickname()));
            }
        }
    }
}
