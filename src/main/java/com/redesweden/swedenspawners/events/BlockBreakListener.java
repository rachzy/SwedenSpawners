package com.redesweden.swedenspawners.events;

import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedenspawners.data.Players;
import com.redesweden.swedenspawners.data.SaleSpawners;
import com.redesweden.swedenspawners.data.Spawners;
import com.redesweden.swedenspawners.models.Spawner;
import com.redesweden.swedenspawners.models.SpawnerManager;
import com.redesweden.swedenspawners.models.SpawnerMeta;
import com.redesweden.swedenspawners.models.SpawnerPlayer;
import eu.decentsoftware.holograms.api.DHAPI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.math.BigDecimal;

public class BlockBreakListener implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if(e.isCancelled()) return;
        Player player = e.getPlayer();
        Location local = e.getBlock().getLocation().clone().add(1, 0, 0);

        Spawner spawner = Spawners.getSpawnerPorLocal(local);
        if(spawner == null) return;
        e.setCancelled(true);

        if(!spawner.getDono().getNickname().equals(player.getDisplayName())) {
            SpawnerManager manager = spawner.getManagerPorNome(player.getDisplayName());

            if(manager == null || !manager.getPermissaoQuebrar()) {
                player.sendMessage("§cVocê não tem permissão para quebrar esse spawner");
                return;
            }
        }

        SpawnerMeta spawnerMeta = SaleSpawners.getSpawnerPorTitulo(spawner.getSpawnerMeta().getTitle());
        BigDecimal quantiaStackada = spawner.getQuantidadeStackada();

        e.getBlock().setType(Material.AIR);
        player.getInventory().addItem(spawnerMeta.getSpawner(quantiaStackada));
        player.sendMessage(String.format("§aVocê retirou §f%s §aspawners.", new ConverterQuantia(quantiaStackada).emLetras()));
        DHAPI.removeHologram(spawner.getId());
        spawner.setAtivado(false);
        Spawners.removeSpawnerPorId(spawner.getId());
    }
}
