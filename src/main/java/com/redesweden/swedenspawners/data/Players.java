package com.redesweden.swedenspawners.data;

import com.redesweden.swedenspawners.SwedenSpawners;
import com.redesweden.swedenspawners.files.PlayersFile;
import com.redesweden.swedenspawners.models.SpawnerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Players {
    private static final List<SpawnerPlayer> playerList = new ArrayList<>();
    private static final Map<SpawnerPlayer, SpawnerPlayer> playersModificados = new HashMap<>();

    public static void addPlayer(SpawnerPlayer player, Boolean save) {
        playerList.add(player);
        if(save) {
            PlayersFile.createNewPlayer(player.getUuid(), player.getNickname());
        }
    }

    public static SpawnerPlayer getPlayerByUuid(String uuid) {
        return playerList.stream().filter(player -> player.getUuid().equals(uuid)).findFirst().orElse(null);
    }

    public static SpawnerPlayer getPlayerByName(String nickname) {
        return playerList.stream().filter(player -> player.getNickname().equals(nickname)).findFirst().orElse(null);
    }

    public static void addPlayerModificado(SpawnerPlayer player) {
        playersModificados.put(player, player);
    }

    public static void salvarPlayersModificados() {
        System.out.println("[SwedenSpawners] Salvando jogadores modificados...");
        playersModificados.values().forEach(SpawnerPlayer::save);
    }

    public static void iniciarSalvamentoAutomatico() {
        salvarPlayersModificados();
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskLater(SwedenSpawners.getPlugin(SwedenSpawners.class), Players::iniciarSalvamentoAutomatico, 20L * 1800L);
    }

}
