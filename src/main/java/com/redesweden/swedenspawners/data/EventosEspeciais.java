package com.redesweden.swedenspawners.data;

import com.redesweden.swedenspawners.models.EventoPlayerCompraDeSpawners;
import com.redesweden.swedenspawners.models.SpawnerMeta;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventosEspeciais {
    private static List<EventoPlayerCompraDeSpawners> comprarQuantiaDeSpawners = new ArrayList<>();

    public static void addPlayerToComprarQuantiaDeSpawners(Player player, SpawnerMeta spawner) {
        EventoPlayerCompraDeSpawners novoEvento = new EventoPlayerCompraDeSpawners(player, spawner);
        comprarQuantiaDeSpawners.add(novoEvento);
    }

    public static void removePlayerFromComprarQuantiaDeSpawners(Player player) {
        comprarQuantiaDeSpawners = comprarQuantiaDeSpawners.stream()
                .filter(evento -> evento.getPlayer() != player)
                .collect(Collectors.toList());
    }

    public static EventoPlayerCompraDeSpawners getEventoInComprarQuantiaDeSpawnersByPlayer(String nickname) {
        return comprarQuantiaDeSpawners.stream()
                .filter(evento -> evento.getPlayer().getDisplayName().equals(nickname))
                .findFirst()
                .orElse(null);
    }
}
