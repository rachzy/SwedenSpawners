package com.redesweden.swedenspawners.events;

import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedenspawners.data.Spawners;
import com.redesweden.swedenspawners.functions.GetBlocosPorPerto;
import com.redesweden.swedenspawners.models.Spawner;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class EntityKillListener implements Listener {
    @EventHandler
    public void onEntityKill(EntityDeathEvent e) {
        e.getDrops().clear();
        Location mobLocal = e.getEntity().getLocation();
        List<Block> blocosPorPerto = new GetBlocosPorPerto(mobLocal, 2).getBlocos();

        AtomicReference<Spawner> spawner = new AtomicReference<>(null);
        blocosPorPerto.forEach(bloco -> {
            Spawner spawnerPerto = Spawners.getSpawnerPorLocal(bloco.getLocation());
            if(spawnerPerto != null
                    && spawnerPerto.getSpawnerMeta().getMob() == e.getEntity().getType()
            ) {
                spawner.set(Spawners.getSpawnerPorLocal(bloco.getLocation()));
            }
        });

        if(spawner.get() == null) return;
        spawner.get().matarEntidades();
    }
}
