package com.redesweden.swedenspawners.events;

import com.redesweden.swedencash.models.PlayerCash;
import com.redesweden.swedeneconomia.data.Players;
import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedeneconomia.models.PlayerSaldo;
import com.redesweden.swedenspawners.GUIs.*;
import com.redesweden.swedenspawners.data.EventosEspeciais;
import com.redesweden.swedenspawners.data.SaleSpawners;
import com.redesweden.swedenspawners.functions.GerenciadorDeSpawner;
import com.redesweden.swedenspawners.functions.InstantFirework;
import com.redesweden.swedenspawners.models.Spawner;
import com.redesweden.swedenspawners.models.SpawnerAmigo;
import com.redesweden.swedenspawners.models.SpawnerMeta;
import com.redesweden.swedenspawners.models.SpawnerPlayer;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
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
            String itemTitle = e.getCurrentItem().getItemMeta().getDisplayName();
            SpawnerMeta spawner = SaleSpawners.getSpawnerPorTitulo(itemTitle);
            if (spawner == null) {
                // Verificar se o player clicou no Multiplicador de Compra
                if (e.getCurrentItem().getType() != Material.SKULL_ITEM
                        || !itemTitle.substring(2).equalsIgnoreCase("multiplicador de compra"))
                    return;

                if (!player.hasPermission("swedenspawners.usarmultiplicador")) {
                    player.sendMessage("§cApenas §aVIPs §cpodem utilizar o Multiplicador de Compra.");
                    return;
                }

                player.closeInventory();
                EventosEspeciais.addPlayerSetandoMultiplicador(player);
                player.sendMessage("");
                player.sendMessage(" §aDigite a quantia que você deseja definir seu multiplicador para tal:");
                player.sendMessage("§7(Digite 'CANCELAR' para cancelar a operação)");
                player.sendMessage("");
                player.playSound(player.getLocation(), Sound.CLICK, 3.0F, 2F);
                return;
            }

            if (e.getClick().isRightClick()) {
                player.closeInventory();
                EventosEspeciais.addPlayerToComprarQuantiaDeSpawners(player, spawner);
                player.sendMessage("");
                player.sendMessage(" §aDigite a quantia de spawners que você deseja comprar: ");
                player.sendMessage("§7(Digite 'CANCELAR' para cancelar a operação)");
                player.sendMessage("");
                player.playSound(player.getLocation(), Sound.CLICK, 3.0F, 2F);
                return;
            }

            PlayerSaldo playerSaldo = Players.getPlayer(player.getName());
            SpawnerPlayer playerSpawner = com.redesweden.swedenspawners.data.Players.getPlayerByName(player.getName());
            BigDecimal saldo = (BigDecimal) playerSaldo.getSaldo(false);
            BigDecimal spawnerPreco = spawner.getPreco();
            BigDecimal quantidade = new BigDecimal("1");

            if (e.getClick().isKeyboardClick()) {
                quantidade = playerSpawner.getLimite().multiply(new BigDecimal(playerSpawner.getMultiplicador()));
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
            playerSpawner.addSpawnersComprados(quantidade);
            player.openInventory(new ComprarSpawnersGUI(player.getName()).get());
            player.sendMessage(String.format("§aVocê comprou §f%s §a%s", new ConverterQuantia(quantidade).emLetras(), spawner.getTitulo()));
            player.playSound(player.getLocation(), Sound.VILLAGER_YES, 3.0F, 1F);
            return;
        }

        if (viewTitle.equals("GERENCIAR SPAWNER")) {
            e.setCancelled(true);

            if (e.getCurrentItem() == null
                    || !e.getCurrentItem().hasItemMeta()
                    || e.getCurrentItem().getItemMeta().getDisplayName() == null) return;

            String nomeDoItem = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);
            Spawner spawner = EventosEspeciais.getEventoGerenciarSpawnerByPlayer(player).getSpawner();

            if (nomeDoItem.equalsIgnoreCase("GERENCIAR DROPS")) {
                player.openInventory(new GerenciarDropsGUI(spawner).get());
                player.playSound(player.getLocation(), Sound.CLICK, 3.0F, 1.5F);
                return;
            }

            if (nomeDoItem.equalsIgnoreCase("GERENCIAR AMIGOS")) {
                player.openInventory(new GerenciarAmigosGUI(player.getName(), spawner).get());
                player.playSound(player.getLocation(), Sound.CLICK, 3.0F, 1.5F);
                return;
            }

            if (nomeDoItem.equalsIgnoreCase("MELHORIAS")) {
                player.openInventory(new MelhoriasGUI(spawner).get());
                player.playSound(player.getLocation(), Sound.CLICK, 3.0F, 1.5F);
                return;
            }

            if (nomeDoItem.equalsIgnoreCase("LIGAR OU DESLIGAR")) {
                spawner.toggleAtivado();

                if (spawner.getAtivado()) {
                    player.sendMessage("§aSpawner ativado com sucesso!");
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 3.0F, 1.5F);
                } else {
                    player.sendMessage("§cSpawner desligado com sucesso.");
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 3.0F, 0.5F);
                }
                player.closeInventory();
                return;
            }

            if (nomeDoItem.startsWith("Retirar spawner")) {
                if (!spawner.getDono().getNickname().equals(player.getName())) {
                    SpawnerAmigo amigo = spawner.getAmigoPorNome(player.getName());

                    if (amigo == null || !amigo.getPermissaoRetirar()) {
                        EventosEspeciais.removePlayerRemovendoSpawners(player);
                        player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                        player.sendMessage("§cVocê não tem permissão para retirar esse spawner");
                        return;
                    }
                }

                if (spawner.getQuantidadeStackada().compareTo(new BigDecimal("1")) <= 0) {
                    new GerenciadorDeSpawner(player, spawner).removerPorCompleto();
                    return;
                }

                EventosEspeciais.addPlayerRemovendoSpawners(player, spawner);
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 3.0F, 2F);
                player.sendMessage(" ");
                player.sendMessage(" §aDigite a quantidade de spawners que deseja remover: ");
                player.sendMessage("§7(Ou digite 'CANCELAR' para cancelar a operação)");
                player.sendMessage("");
            }

            return;
        }

        if (viewTitle.equals("DROPS")) {
            e.setCancelled(true);

            if (e.getCurrentItem() == null
                    || !e.getCurrentItem().hasItemMeta()
                    || e.getCurrentItem().getItemMeta().getDisplayName() == null) return;

            String nomeDoItem = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);
            Spawner spawner = EventosEspeciais.getEventoGerenciarSpawnerByPlayer(player).getSpawner();

            if (nomeDoItem.equalsIgnoreCase("VOLTAR")) {
                player.openInventory(new SpawnerGUI(spawner).get());
                player.playSound(player.getLocation(), Sound.CLICK, 3.0F, 2.5F);
                return;
            }

            player.closeInventory();

            if (!spawner.getDono().getNickname().equals(player.getName())) {
                SpawnerAmigo amigo = spawner.getAmigoPorNome(player.getName());

                if (amigo == null || !amigo.getPermissaoVender()) {
                    player.sendMessage("§cVocê não tem permissão para gerenciar os drops deste spawner.");
                    return;
                }
            }

            if (nomeDoItem.equalsIgnoreCase("DROPS DO SPAWNER")) {
                if (Objects.equals(spawner.getDropsAramazenados(), new BigDecimal("0"))) {
                    player.sendMessage("§cEste spawner não tem nenhum drop para vender.");
                    return;
                }
                PlayerSaldo playerSaldo = Players.getPlayer(player.getName());
                BigDecimal valorDaVenda = spawner.getDropsAramazenados().multiply(spawner.getSpawnerMeta().getPrecoPorDrop().multiply(BigDecimal.valueOf(spawner.getLevelValorDoDrop())));
                playerSaldo.addSaldo(valorDaVenda);
                playerSaldo.addQuantiaMovimentada(valorDaVenda);
                player.sendMessage(String.format("§aVocê vendeu %s de drops deste spawner por $§f%s§a.", new ConverterQuantia(spawner.getDropsAramazenados()).emLetras(), new ConverterQuantia(valorDaVenda).emLetras()));
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 3.0F, 1.5F);
                spawner.zerarDropsArmazenados();
                return;
            }

            if (nomeDoItem.equalsIgnoreCase("LIMPAR DROPS")) {
                player.sendMessage("§cVocê limpou os drops deste spawner.");
                player.playSound(player.getLocation(), Sound.CAT_MEOW, 3.0F, 0.5F);
                spawner.zerarDropsArmazenados();
            }
        }

        if (viewTitle.equals("AMIGOS")) {
            e.setCancelled(true);

            if (e.getCurrentItem() == null
                    || !e.getCurrentItem().hasItemMeta()
                    || e.getCurrentItem().getItemMeta().getDisplayName() == null) return;

            String nomeDoItem = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);
            Spawner spawner = EventosEspeciais.getEventoGerenciarSpawnerByPlayer(player).getSpawner();

            if (nomeDoItem.equalsIgnoreCase("VOLTAR")) {
                player.openInventory(new SpawnerGUI(spawner).get());
                player.playSound(player.getLocation(), Sound.CLICK, 3.0F, 2.5F);
                return;
            }

            if (!spawner.getDono().getNickname().equals(player.getName())) {
                player.sendMessage("§cVocê não tem permissão para gerenciar os amigos deste spawner.");
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                return;
            }

            if (nomeDoItem.equalsIgnoreCase("ADICIONAR AMIGO")) {
                player.playSound(player.getLocation(), Sound.CLICK, 3.0F, 2.5F);
                if (spawner.getAmigos().toArray().length >= 5) {
                    player.sendMessage("§cEste spawner já atingiu seu limite de amigos (5).");
                    return;
                }
                player.closeInventory();
                EventosEspeciais.addPlayerAdicionandoAmigo(player, spawner);
                player.sendMessage("");
                player.sendMessage(" §aDigite o nome do player que você deseja adicionar como amigo.");
                player.sendMessage("§7(Digite 'CANCELAR' para cancelar a operação)");
                player.sendMessage("");
                return;
            }

            SpawnerAmigo amigo = spawner.getAmigoPorNome(nomeDoItem);

            if (amigo != null) {
                player.openInventory(new GerenciarAmigoGUI(amigo).get());
                player.playSound(player.getLocation(), Sound.CLICK, 3.0F, 2.5F);
            }
        }

        if (viewTitle.equals("AMIGO")) {
            e.setCancelled(true);

            if (e.getCurrentItem() == null
                    || !e.getCurrentItem().hasItemMeta()
                    || e.getCurrentItem().getItemMeta().getDisplayName() == null) return;

            String nomeDoItem = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);
            Spawner spawner = EventosEspeciais.getEventoGerenciarSpawnerByPlayer(player).getSpawner();

            if (nomeDoItem.equalsIgnoreCase("VOLTAR")) {
                player.openInventory(new GerenciarAmigosGUI(player.getName(), spawner).get());
                player.playSound(player.getLocation(), Sound.CLICK, 3.0F, 2.5F);
                return;
            }

            if (!spawner.getDono().getNickname().equals(player.getName())) {
                player.sendMessage("§cVocê não tem permissão para gerenciar os amigos deste spawner.");
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                return;
            }

            String amigoNick = e.getInventory().getItem(10).getItemMeta().getDisplayName().substring(2);
            SpawnerAmigo amigoAlvo = spawner.getAmigoPorNome(amigoNick);

            if (nomeDoItem.equalsIgnoreCase("PERMISSÃO DE VENDER")) {
                amigoAlvo.togglePermissaoVender(spawner);
                player.openInventory(new GerenciarAmigoGUI(amigoAlvo).get());
                player.playSound(player.getLocation(), Sound.CLICK, 3.0F, 2.5F);
                return;
            }

            if (nomeDoItem.equalsIgnoreCase("PERMISSÃO DE MATAR")) {
                amigoAlvo.togglePermissaoMatar(spawner);
                player.openInventory(new GerenciarAmigoGUI(amigoAlvo).get());
                player.playSound(player.getLocation(), Sound.CLICK, 3.0F, 2.5F);
                return;
            }

            if (nomeDoItem.equalsIgnoreCase("PERMISSÃO DE RETIRAR")) {
                amigoAlvo.togglePermissaoRetirar(spawner);
                player.openInventory(new GerenciarAmigoGUI(amigoAlvo).get());
                player.playSound(player.getLocation(), Sound.CLICK, 3.0F, 2.5F);
                return;
            }

            if (nomeDoItem.equalsIgnoreCase("REMOVER AMIGO")) {
                spawner.removerAmigo(amigoAlvo);
                player.openInventory(new GerenciarAmigosGUI(player.getName(), spawner).get());
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 3.0F, 2F);
                player.sendMessage(String.format("§cVocê removeu %s da lista de Amigos do seu spawner.", amigoAlvo.getNickname()));
            }
            return;
        }

        if (viewTitle.equals("MELHORIAS")) {
            e.setCancelled(true);

            if (e.getCurrentItem() == null
                    || !e.getCurrentItem().hasItemMeta()
                    || e.getCurrentItem().getItemMeta().getDisplayName() == null) return;

            String nomeDoItem = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);
            Spawner spawner = EventosEspeciais.getEventoGerenciarSpawnerByPlayer(player).getSpawner();

            if (nomeDoItem.equalsIgnoreCase("VOLTAR")) {
                player.openInventory(new SpawnerGUI(spawner).get());
                player.playSound(player.getLocation(), Sound.CLICK, 3.0F, 2.5F);
                return;
            }

            player.closeInventory();
            int levelFinal = 0;

            BigDecimal precoMelhoria;

            if (nomeDoItem.equalsIgnoreCase("TEMPO DE SPAWN")
                    || nomeDoItem.equalsIgnoreCase("VALOR DO DROP")
                    || nomeDoItem.equalsIgnoreCase("MULTIPLICADOR DE SPAWN")
            ) {
                try {
                    String preco = Arrays.stream(e.getCurrentItem().getItemMeta().getLore().get(1).split(" ")).toArray()[3].toString().substring(3);
                    precoMelhoria = new ConverterQuantia(preco).emNumeros();

                    PlayerCash playerCash = com.redesweden.swedencash.data.Players.getPlayerPorNickname(player.getName());
                    if(playerCash.getCash().compareTo(precoMelhoria) < 0) {
                        player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 2F);
                        player.sendMessage("§cVocê não possui CASH suficiente para comprar este upgrade.");
                        return;
                    }
                    playerCash.subCash(precoMelhoria);
                } catch (Exception ex) {
                    player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 2F);
                    player.sendMessage("§cEste spawner já está em seu level máximo neste melhoria.");
                    return;
                }
            }

            if (nomeDoItem.equalsIgnoreCase("TEMPO DE SPAWN")) {
                spawner.addLevelTempoDeSpawn();
                levelFinal = spawner.getLevelTempoDeSpawn();
            }

            if (nomeDoItem.equalsIgnoreCase("VALOR DO DROP")) {
                spawner.addLevelValorDoDrop();
                levelFinal = spawner.getLevelValorDoDrop();
            }

            if (nomeDoItem.equalsIgnoreCase("MULTIPLICADOR DE SPAWN")) {
                spawner.addLevelMultiplicadorDeSpawn();
                levelFinal = spawner.getLevelValorDoDrop();
            }

            if (levelFinal == 0) return;

            player.playSound(player.getLocation(), Sound.LEVEL_UP, 3.0F, 0.5F);
            new InstantFirework(FireworkEffect.builder().withColor(Color.LIME, Color.YELLOW).build(), spawner.getLocal().clone().add(0.5, 1, 0.5));
            player.sendMessage(String.format("§aMelhoria aplicado com sucesso! Seu spawner agora está no level §6%s §ade %s.", levelFinal, nomeDoItem));
        }
    }
}
