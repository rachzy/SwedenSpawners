package com.redesweden.swedenspawners.data;

import com.redesweden.swedenspawners.files.SpawnersFile;
import com.redesweden.swedenspawners.models.Spawner;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Spawners {
    private static List<Spawner> spawnerList = new ArrayList<>();

    public static void addSpawner(Spawner spawner) {
        spawnerList.add(spawner);
    }

    public static Spawner getSpawnerPorLocal(Location local) {
        return spawnerList.stream().filter((spawner) -> spawner.getLocal().getX() == local.getX() && spawner.getLocal().getY() == local.getY() && spawner.getLocal().getZ() == local.getZ()).findFirst().orElse(null);
    }

    public static void removeSpawnerPorId(String id) {
        spawnerList = spawnerList.stream().filter((spawner) -> !spawner.getId().equals(id)).collect(Collectors.toList());
        SpawnersFile.get().set("spawners." + id, null);
        SpawnersFile.save();
    }
}
