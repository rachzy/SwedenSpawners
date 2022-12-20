package com.redesweden.swedenspawners.files;

import com.redesweden.swedenspawners.data.Players;
import com.redesweden.swedenspawners.models.SpawnerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.math.BigDecimal;

public class PlayersFile {
    private static File file;
    private static FileConfiguration playersFile;

    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("SwedenSpawners").getDataFolder(), "players.yml");

        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                System.out.println("Não foi possível criar o arquivo players.yml..." + e.getMessage());
            }
        }

        playersFile = YamlConfiguration.loadConfiguration(file);


        if(playersFile.getConfigurationSection("players") == null) return;
        for(String player : playersFile.getConfigurationSection("players").getKeys(false)) {
            String nickname = playersFile.getString(String.format("players.%s.nickname", player));
            String limite = playersFile.getString(String.format("players.%s.limite", player));
            String spawnersComprados = playersFile.getString(String.format("players.%s.spawnersComprados", player));
            Integer multiplicador = playersFile.getInt(String.format("players.%s.multiplicador", player));

            SpawnerPlayer newPlayer = new SpawnerPlayer(player, nickname, new BigDecimal(limite), new BigDecimal(spawnersComprados), multiplicador);
            Players.addPlayer(newPlayer, false);
        }
    }

    public static void createNewPlayer(String uuid, String nickname) {
        playersFile.set(String.format("players.%s.nickname", uuid), nickname);
        playersFile.set(String.format("players.%s.limite", uuid), "1");
        playersFile.set(String.format("players.%s.spawnersComprados", uuid), "0");
        playersFile.set(String.format("players.%s.multiplicador", uuid), 1);
        save();
    }

    public static FileConfiguration get() {
        return playersFile;
    }

    public static void save() {
        try {
            playersFile.save(file);
        } catch (Exception e) {
            System.out.println("Não foi possível salvar o arquivo players.yml... " + e.getMessage());
        }
    }
}
