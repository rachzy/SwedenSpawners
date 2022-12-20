package com.redesweden.swedenspawners.events;

import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedenspawners.SwedenSpawners;
import com.redesweden.swedenspawners.data.SaleSpawners;
import com.redesweden.swedenspawners.data.Spawners;
import com.redesweden.swedenspawners.files.SpawnersFile;
import com.redesweden.swedenspawners.functions.GetBlocosPorPerto;
import com.redesweden.swedenspawners.models.Spawner;
import com.redesweden.swedenspawners.models.SpawnerMeta;
import org.bukkit.Material;
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
        List<Block> blocosAoRedor = new GetBlocosPorPerto(blocoColocado.getLocation(), 2).getBlocos();
        AtomicReference<Spawner> spawner = new AtomicReference<>(null);
        blocosAoRedor.forEach((bloco) -> {
            if (spawner.get() == null && Spawners.getSpawnerPorLocal(bloco.getLocation()) != null) {
                spawner.set(Spawners.getSpawnerPorLocal(bloco.getLocation()));
            }
        });

        // Se tiver algum spawner perto do bloco, retorne
        if (spawner.get() != null
                && e.getBlock().getType() != Material.SKULL
                && !itemBloco.hasItemMeta()
                && itemBloco.getItemMeta().getDisplayName() == null
                && itemBloco.getItemMeta().getLore() == null) {
            player.sendMessage("§cHá um spawner muito próximo!");
            e.setCancelled(true);
            return;
        }

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
                player.sendMessage("§cJá existe um spawner de outro tipo ou de outro player muito perto deste.");
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
            blocoColocado.setType(Material.AIR);
            player.sendMessage(String.format("§aVocê adicionou com sucesso §f%s §aspawner(s)", new ConverterQuantia(quantiaFinal).emLetras()));
            return;
        }

        new BukkitRunnable() {
            public void run() {
                blocoColocado.setType(spawnerMeta.getBloco().getType());
                blocoColocado.setData(spawnerMeta.getBloco().getData().getData());
                SpawnersFile.createNewSpawner(player, spawnerMeta, blocoColocado.getLocation().add(1, 0, 0), quantidade);
                player.sendMessage(String.format("§eVocê colocou com sucesso §a1 %s", spawnerMeta.getTitle()));
            }
        }.runTaskLater(SwedenSpawners.getPlugin(SwedenSpawners.class), 1);
    }
}
