package com.redesweden.swedenspawners.data;

import com.redesweden.swedenspawners.SwedenSpawners;
import com.redesweden.swedenspawners.files.SpawnersFile;
import com.redesweden.swedenspawners.models.Spawner;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Spawners {
    private static final List<Spawner> spawnerList = new ArrayList<>();
    private static final Map<Spawner, Spawner> spawnersModificados = new HashMap<>();

    public static void addSpawner(Spawner spawner) {
        spawnerList.add(spawner);
    }

    public static Spawner getSpawnerPorId(String id) {
        return spawnerList.stream().filter((spawner) -> spawner.getId().equals(id)).findFirst().orElse(null);
    }

    public static Spawner getSpawnerPorLocal(Location local) {
        return spawnerList.stream().filter((spawner) -> spawner.getLocal().getX() == local.getX() && spawner.getLocal().getY() == local.getY() && spawner.getLocal().getZ() == local.getZ() && !spawner.getRetirado()).findFirst().orElse(null);
    }

    public static Map<Spawner, Spawner> getSpawnersModificados() {
        return spawnersModificados;
    }

    public static void salvarSpawnersModificados() {
        System.out.println("[SwedenSpawners] Salvando spawners modificados...");
        spawnersModificados.values().forEach(Spawner::salvarDados);
    }

    public static void iniciarSalvamentoAutomatico() {
        salvarSpawnersModificados();
        Bukkit.getScheduler().runTaskLater(SwedenSpawners.getPlugin(SwedenSpawners.class), Spawners::iniciarSalvamentoAutomatico, 20L * 1800L);
    }
}
