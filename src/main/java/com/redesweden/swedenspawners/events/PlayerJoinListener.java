package com.redesweden.swedenspawners.events;

import com.redesweden.swedenspawners.data.Players;
import com.redesweden.swedenspawners.models.SpawnerPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.math.BigDecimal;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        if(Players.getPlayerByUuid(player.getUniqueId().toString()) == null) {
            System.out.println("[SwedenSpawners] Registrando jogador...");
            SpawnerPlayer newPlayer = new SpawnerPlayer(player.getUniqueId().toString(), player.getName(), new BigDecimal("1"), new BigDecimal("0"), 1);
            Players.addPlayer(newPlayer, true);
        }
    }
}
