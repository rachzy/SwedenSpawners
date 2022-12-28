package com.redesweden.swedenspawners.events;

import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedenspawners.SwedenSpawners;
import com.redesweden.swedenspawners.data.Players;
import com.redesweden.swedenspawners.data.SaleSpawners;
import com.redesweden.swedenspawners.data.Spawners;
import com.redesweden.swedenspawners.files.SpawnersFile;
import com.redesweden.swedenspawners.functions.GetBlocosPorPerto;
import com.redesweden.swedenspawners.functions.InstantFirework;
import com.redesweden.swedenspawners.models.Spawner;
import com.redesweden.swedenspawners.models.SpawnerMeta;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        // Execute apenas caso o evento não tenha sido cancelado.
        if (e.isCancelled()) return;

        Block blocoColocado = e.getBlock();
        Player player = e.getPlayer();
        ItemStack itemBloco = player.getItemInHand();

        // Verifica se tem algum spawner perto do bloco
        List<Block> blocosAoRedor = new GetBlocosPorPerto(blocoColocado.getLocation(), 5).getBlocos();
        AtomicReference<Spawner> spawner = new AtomicReference<>(null);
        blocosAoRedor.forEach((bloco) -> {
            if (spawner.get() == null && Spawners.getSpawnerPorLocal(bloco.getLocation()) != null) {
                spawner.set(Spawners.getSpawnerPorLocal(bloco.getLocation()));
            }
        });

        // Caso o item colocado não seja uma skull ou não tenha metadata, retorne
        if (e.getBlock().getType() != Material.SKULL
                || !itemBloco.hasItemMeta()
                || itemBloco.getItemMeta().getDisplayName() == null
                || itemBloco.getItemMeta().getLore() == null) return;

        String itemTitle = itemBloco.getItemMeta().getDisplayName().substring(2).toUpperCase();
        List<String> lore = itemBloco.getItemMeta().getLore();

        //Caso o titulo do item não começe com "Spawner", retorne
        if (!itemTitle.startsWith("SPAWNER")) return;

        SpawnerMeta spawnerMeta = SaleSpawners.getSpawnerPorTitulo(itemBloco.getItemMeta().getDisplayName());
        BigDecimal quantidade;
        try {
            quantidade = new ConverterQuantia(lore.get(0).split(" ")[1].substring(2)).emNumeros();
        } catch (Exception ex) {
            player.sendMessage("§cNão foi possível setar esses spawners. Por favor, entre em contato com a staff.");
            return;
        }

        if (spawnerMeta == null) return;

        if (spawner.get() != null) {
            if (!spawner.get().getDono().getNickname().equals(player.getDisplayName())
                    || spawner.get().getSpawnerMeta().getMob() != spawnerMeta.getMob()) {
                player.sendMessage("§cJá existe um spawner de outro tipo ou de outro player muito perto deste. (Menos de 5 blocos)");
                e.setCancelled(true);
                return;
            }

            BigDecimal quantiaFinal = quantidade;

            // Verificar se o jogador está no SHIFT para ativar função de colocar todos os spawners de uma vez
            if (player.isSneaking()) {
                quantiaFinal = quantidade.multiply(BigDecimal.valueOf(itemBloco.getAmount()));
                player.setItemInHand(new ItemStack(Material.AIR));
            }

            spawner.get().addQuantidadesStackadas(quantiaFinal);
            new BukkitRunnable() {
                public void run() {
                    blocoColocado.setType(Material.AIR);
                }
            }.runTaskLater(SwedenSpawners.getPlugin(SwedenSpawners.class), 1);
            player.sendMessage(String.format("§aVocê adicionou com sucesso §f%s §aspawner(s)", new ConverterQuantia(quantiaFinal).emLetras()));
            return;
        }

        new BukkitRunnable() {
            public void run() {
                blocoColocado.setType(spawnerMeta.getBloco().getType());
                blocoColocado.setData(spawnerMeta.getBloco().getData().getData());

                // Caso o spawner colocado já tenha um ID
                try {
                    String spawnerId = itemBloco.getItemMeta().getLore().get(1).substring(2);
                    Spawner spawner = Spawners.getSpawnerPorId(spawnerId);
                    spawner.setLocal(blocoColocado.getLocation());
                    spawner.setDono(Players.getPlayerByName(player.getDisplayName()));
                    spawner.setRetirado(false);
                    spawner.setAtivado(true);
                    spawner.iniciar();
                } catch (Exception ex) {
                    SpawnersFile.createNewSpawner(player, spawnerMeta, blocoColocado.getLocation(), quantidade);
                }

                // Summonar partículas
                for (int i = 0; i <= 25; i++) {
                    blocoColocado.getWorld().playEffect(blocoColocado.getLocation(), Effect.FIREWORKS_SPARK, 4);
                }

                // Summonar foguete
                new InstantFirework(FireworkEffect.builder().withColor(Color.FUCHSIA, Color.PURPLE).build(), blocoColocado.getLocation().clone().add(0.5, 1, 0.5));

                // Tocar som de level up
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 3.0F, 0.5F);

                player.sendMessage(String.format("§aVocê criou um novo %s", spawnerMeta.getTitulo()));
            }
        }.runTaskLater(SwedenSpawners.getPlugin(SwedenSpawners.class), 1);
    }
}
