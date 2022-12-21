package com.redesweden.swedenspawners.files;

import com.redesweden.swedenspawners.data.Players;
import com.redesweden.swedenspawners.data.SaleSpawners;
import com.redesweden.swedenspawners.data.Spawners;
import com.redesweden.swedenspawners.models.Spawner;
import com.redesweden.swedenspawners.models.SpawnerAmigo;
import com.redesweden.swedenspawners.models.SpawnerMeta;
import com.redesweden.swedenspawners.models.SpawnerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SpawnersFile {
    private static File file;
    private static FileConfiguration spawnersFile;

    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("SwedenSpawners").getDataFolder(), "spawners.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                System.out.println("Não foi possível criar o arquivo spawners.yml..." + e.getMessage());
            }
        }

        spawnersFile = YamlConfiguration.loadConfiguration(file);

        if (spawnersFile.getConfigurationSection("spawners") == null) return;

        for (String spawnerId : spawnersFile.getConfigurationSection("spawners").getKeys(false)) {
            SpawnerPlayer dono = Players.getPlayerByName(spawnersFile.getString(String.format("spawners.%s.dono", spawnerId)));
            SpawnerMeta tipo = SaleSpawners.getSpawnerPorId(spawnersFile.getString(String.format("spawners.%s.tipo", spawnerId)));

            int x = spawnersFile.getInt(String.format("spawners.%s.local.x", spawnerId));
            int y = spawnersFile.getInt(String.format("spawners.%s.local.y", spawnerId));
            int z = spawnersFile.getInt(String.format("spawners.%s.local.z", spawnerId));
            String mundo = spawnersFile.getString(String.format("spawners.%s.local.mundo", spawnerId));
            Location local = new Location(Bukkit.getWorld(mundo), x, y, z);

            int melhoriaTempoDeSpawn = spawnersFile.getInt(String.format("spawners.%s.melhorias.tempoDeSpawn", spawnerId));
            int melhoriaValorDoDrop = spawnersFile.getInt(String.format("spawners.%s.melhorias.valorDoDrop", spawnerId));
            int melhoriaMultiplicadorDeSpawn = spawnersFile.getInt(String.format("spawners.%s.melhorias.multiplicadorDeSpawn", spawnerId));

            List<SpawnerAmigo> amigos = new ArrayList<>();

            if (spawnersFile.getConfigurationSection(String.format("spawners.%s.amigos", spawnerId)) != null) {
                for (String playerUUID : spawnersFile.getConfigurationSection(String.format("spawners.%s.amigos", spawnerId)).getKeys(false)) {
                    String nickname = spawnersFile.getString(String.format("spawners.%s.amigos.%s.nickname", spawnerId, playerUUID));
                    Boolean permissaoVender = spawnersFile.getBoolean(String.format("spawners.%s.amigos.%s.permissaoVender", spawnerId, playerUUID));
                    Boolean permissaoMatar = spawnersFile.getBoolean(String.format("spawners.%s.amigos.%s.permissaoMatar", spawnerId, playerUUID));
                    Boolean permissaoQuebrar = spawnersFile.getBoolean(String.format("spawners.%s.amigos.%s.permissaoQuebrar", spawnerId, playerUUID));

                    SpawnerAmigo amigo = new SpawnerAmigo(playerUUID, nickname, permissaoVender, permissaoMatar, permissaoQuebrar);
                    amigos.add(amigo);
                }
            }

            BigDecimal quantidadeStackada = new BigDecimal(spawnersFile.getString(String.format("spawners.%s.quantidadeStackada", spawnerId)));
            BigDecimal entidadesSpawnadas = new BigDecimal(spawnersFile.getString(String.format("spawners.%s.entidadesSpawnadas", spawnerId)));
            BigDecimal dropsArmazenados = new BigDecimal(spawnersFile.getString(String.format("spawners.%s.dropsArmazenados", spawnerId)));
            Boolean ativado = spawnersFile.getBoolean(String.format("spawners.%s.ativado", spawnerId));
            Boolean retirado = spawnersFile.getBoolean(String.format("spawners.%s.retirado", spawnerId));

            Spawner spawner = new Spawner(spawnerId,
                    dono,
                    tipo,
                    local,
                    melhoriaTempoDeSpawn,
                    melhoriaValorDoDrop,
                    melhoriaMultiplicadorDeSpawn,
                    quantidadeStackada, entidadesSpawnadas,
                    dropsArmazenados,
                    amigos,
                    ativado,
                    retirado);
            Spawners.addSpawner(spawner);
        }
    }

    public static void createNewSpawner(Player dono, SpawnerMeta tipo, Location local, BigDecimal quantidadeStackada) {
        System.out.println("[LOGGER] " + dono.getDisplayName() + " criou um novo spawner.");
        String id = dono.getUniqueId().toString().concat(String.valueOf(Math.round(Math.random() * 999999)));
        spawnersFile.set(String.format("spawners.%s.dono", id), dono.getDisplayName());
        spawnersFile.set(String.format("spawners.%s.tipo", id), tipo.getId());
        spawnersFile.set(String.format("spawners.%s.local.x", id), local.getX());
        spawnersFile.set(String.format("spawners.%s.local.y", id), local.getY());
        spawnersFile.set(String.format("spawners.%s.local.z", id), local.getZ());
        spawnersFile.set(String.format("spawners.%s.melhorias.tempoDeSpawn", id), 1);
        spawnersFile.set(String.format("spawners.%s.melhorias.valorDoDrop", id), 1);
        spawnersFile.set(String.format("spawners.%s.melhorias.multiplicadorDeSpawn", id), 1);
        spawnersFile.set(String.format("spawners.%s.local.mundo", id), local.getWorld().getName());
        spawnersFile.set(String.format("spawners.%s.quantidadeStackada", id), quantidadeStackada.toString());
        spawnersFile.set(String.format("spawners.%s.entidadesSpawnadas", id), "0");
        spawnersFile.set(String.format("spawners.%s.dropsArmazenados", id), "0");
        spawnersFile.set(String.format("spawners.%s.ativado", id), true);
        spawnersFile.set(String.format("spawners.%s.retirado", id), false);

        Spawner novoSpawner = new Spawner(id, Players.getPlayerByName(dono.getDisplayName()), tipo, local, 1, 1, 1, quantidadeStackada, new BigDecimal("0"), new BigDecimal("0"), new ArrayList<>(), true, false);
        Spawners.addSpawner(novoSpawner);
        save();
    }

    public static FileConfiguration get() {
        return spawnersFile;
    }

    public static void save() {
        try {
            spawnersFile.save(file);
        } catch (Exception e) {
            System.out.println("Não foi possível salvar o arquivo spawners.yml... " + e.getMessage());
        }
    }
}
