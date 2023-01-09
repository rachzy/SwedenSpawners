package com.redesweden.swedenspawners.events;

import com.redesweden.swedenspawners.data.Spawners;
import com.redesweden.swedenspawners.functions.GetBlocosPorPerto;
import com.redesweden.swedenspawners.models.Spawner;
import com.redesweden.swedenspawners.models.SpawnerAmigo;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class EntityDamageListener implements Listener {
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if(!(e.getDamager() instanceof Player) || e.getEntity() instanceof Player) return;
        Player player = (Player) e.getDamager();
        Entity entidade = e.getEntity();

        if(entidade.getCustomName() == null) return;

        List<Block> blocosAoRedor = new GetBlocosPorPerto(entidade.getLocation(), 2).getBlocos();
        AtomicReference<Spawner> spawner = new AtomicReference<>(null);
        blocosAoRedor.forEach(bloco -> {
            if(spawner.get() == null && Spawners.getSpawnerPorLocal(bloco.getLocation()) != null) {
                spawner.set(Spawners.getSpawnerPorLocal(bloco.getLocation()));
            }
        });

        if(spawner.get() == null) return;

        if(!spawner.get().getDono().getNickname().equals(player.getName())) {
            SpawnerAmigo amigo = spawner.get().getAmigoPorNome(player.getName());

            if(amigo == null || !amigo.getPermissaoMatar()) {
                player.sendMessage("§cVocê não tem permissão para matar os mobs deste spawner.");
                e.setCancelled(true);
            }
        }
    }
}
