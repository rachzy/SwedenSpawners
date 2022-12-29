package com.redesweden.swedenspawners.data;

import com.redesweden.swedenspawners.models.SpawnerMeta;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class SaleSpawners {
    private static final List<SpawnerMeta> spawners = new ArrayList<>();

    public static List<SpawnerMeta> getSpawners() {
        return spawners;
    }

    public static SpawnerMeta getSpawnerPorId(String id) {
        return spawners.stream().filter(spawner -> spawner.getId().equals(id)).findFirst().orElse(null);
    }

    public static SpawnerMeta getSpawnerPorTitulo(String title) {
        return spawners.stream().filter(spawner -> spawner.getTitulo().equals(title)).findFirst().orElse(null);
    }

    public static SpawnerMeta getSpawnerPorBase64(String base64) {
        return spawners.stream().filter(spawner -> spawner.getHeadBase64().equals(base64)).findFirst().orElse(null);
    }

    public static SpawnerMeta getSpawnerPorEntidade(EntityType entity) {
        return spawners.stream().filter(spawner -> spawner.getMob() == entity).findFirst().orElse(null);
    }

    public static void addSpawner(SpawnerMeta spawnerMeta) {
        spawners.add(spawnerMeta);
    }
}
