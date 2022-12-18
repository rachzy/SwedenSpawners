package com.redesweden.swedenspawners.models;

import org.bukkit.entity.Player;

public class EventoPlayerCompraDeSpawners {
    private Player player;
    private SpawnerMeta spawner;

    public EventoPlayerCompraDeSpawners(Player player, SpawnerMeta spawner) {
        this.player = player;
        this.spawner = spawner;
    }

    public Player getPlayer() {
        return player;
    }

    public SpawnerMeta getSpawnerMeta() {
        return spawner;
    }
}
