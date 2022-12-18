package com.redesweden.swedenspawners.data;

import com.redesweden.swedenspawners.files.PlayersFile;
import com.redesweden.swedenspawners.models.SpawnerPlayer;

import java.util.ArrayList;
import java.util.List;

public class Players {
    private static final List<SpawnerPlayer> playerList = new ArrayList<>();

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
}
