package com.redesweden.swedenspawners.events;

import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedenspawners.data.EventosEspeciais;
import com.redesweden.swedenspawners.data.Players;
import com.redesweden.swedenspawners.data.SaleSpawners;
import com.redesweden.swedenspawners.data.Spawners;
import com.redesweden.swedenspawners.models.Spawner;
import com.redesweden.swedenspawners.models.SpawnerAmigo;
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
            SpawnerAmigo amigo = spawner.getAmigoPorNome(player.getDisplayName());

            if(amigo == null || !amigo.getPermissaoQuebrar()) {
                player.sendMessage("§cVocê não tem permissão para quebrar esse spawner");
                return;
            }
        }

        if(player.getInventory().firstEmpty() == -1) {
            player.sendMessage("§cVocê precisa ter pelo menos 1 slot vazio em seu inventário para retirar o spawner.");
            e.setCancelled(true);
            return;
        }

        SpawnerMeta spawnerMeta = SaleSpawners.getSpawnerPorTitulo(spawner.getSpawnerMeta().getTitle());
        BigDecimal quantidadeStackada = spawner.getQuantidadeStackada();

        e.getBlock().setType(Material.AIR);
        player.getInventory().addItem(spawnerMeta.getSpawner(quantidadeStackada));
        player.sendMessage(String.format("§aVocê retirou §f%s §aspawners.", new ConverterQuantia(quantidadeStackada).emLetras()));

        // Remover players de eventos especiais relacionados a spawners
        EventosEspeciais.getPlayersAdicionandoAmigos().forEach(evento -> {
            if(evento.getSpawner().getId().equals(spawner.getId())) {
                Player playerIn = player.getServer().getPlayer(evento.getNick());
                if(playerIn == null || !playerIn.isOnline()) return;
                EventosEspeciais.removePlayerAdicionandoAmigo(playerIn);
                playerIn.sendMessage("§cSeu spawner foi quebrado, logo este evento foi cancelado.");
            }
        });

        EventosEspeciais.getPlayersGerenciandoSpawners().forEach(evento -> {
            if(evento.getSpawner().getId().equals(spawner.getId())) {
                Player playerIn = player.getServer().getPlayer(evento.getNick());
                if(playerIn == null || !playerIn.isOnline()) return;
                EventosEspeciais.removePlayerGerenciandoSpawner(playerIn);
                playerIn.closeInventory();
            }
        });

        // Remover o holograma do spawner
        DHAPI.removeHologram(spawner.getId());

        spawner.setAtivado(false);
        Spawners.removeSpawnerPorId(spawner.getId());
    }
}
