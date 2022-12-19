package com.redesweden.swedenspawners.data;

import com.redesweden.swedenspawners.models.EventoGerenciarSpawner;
import com.redesweden.swedenspawners.models.EventoPlayerCompraDeSpawners;
import com.redesweden.swedenspawners.models.Spawner;
import com.redesweden.swedenspawners.models.SpawnerMeta;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventosEspeciais {
    private static List<EventoPlayerCompraDeSpawners> comprarQuantiaDeSpawners = new ArrayList<>();
    private static List<EventoGerenciarSpawner> playersGerenciandoSpawners = new ArrayList<>();
    private static List<EventoGerenciarSpawner> playersAdicionandoManagers = new ArrayList<>();

    public static EventoPlayerCompraDeSpawners getEventoInComprarQuantiaDeSpawnersByPlayer(String nickname) {
        return comprarQuantiaDeSpawners.stream()
                .filter(evento -> evento.getPlayer().getDisplayName().equals(nickname))
                .findFirst()
                .orElse(null);
    }

    public static void addPlayerToComprarQuantiaDeSpawners(Player player, SpawnerMeta spawner) {
        removePlayerFromComprarQuantiaDeSpawners(player);
        EventoPlayerCompraDeSpawners novoEvento = new EventoPlayerCompraDeSpawners(player, spawner);
        comprarQuantiaDeSpawners.add(novoEvento);
    }

    public static void removePlayerFromComprarQuantiaDeSpawners(Player player) {
        comprarQuantiaDeSpawners = comprarQuantiaDeSpawners.stream()
                .filter(evento -> evento.getPlayer() != player)
                .collect(Collectors.toList());
    }

    public static EventoGerenciarSpawner getEventoGerenciarSpawnerByPlayer(Player player) {
        return playersGerenciandoSpawners
                .stream()
                .filter(playerIn -> playerIn.getNick().equals(player.getDisplayName()))
                .findFirst()
                .orElse(null);
    }

    public static void addPlayerGerenciandoSpawner(Player player, Spawner spawner) {
        removePlayerGerenciandoSpawner(player);
        playersGerenciandoSpawners.add(new EventoGerenciarSpawner(player.getDisplayName(), spawner));
    }

    public static void removePlayerGerenciandoSpawner(Player player) {
        playersGerenciandoSpawners = playersGerenciandoSpawners
                .stream()
                .filter(playerIn -> !playerIn.getNick().equals(player.getDisplayName()))
                .collect(Collectors.toList());
    }

    public static EventoGerenciarSpawner getEventoAdicionarManagerByPlayer(Player player) {
        return playersAdicionandoManagers
                .stream()
                .filter(playerIn -> playerIn.getNick().equals(player.getDisplayName()))
                .findFirst()
                .orElse(null);
    }

    public static void addPlayerAdicionandoManager(Player player, Spawner spawner) {
        removePlayerAdicionandoManager(player);
        playersAdicionandoManagers.add(new EventoGerenciarSpawner(player.getDisplayName(), spawner));
    }

    public static void removePlayerAdicionandoManager(Player player) {
        playersAdicionandoManagers = playersAdicionandoManagers
                .stream()
                .filter(playerIn -> !playerIn.getNick().equals(player.getDisplayName()))
                .collect(Collectors.toList());
    }

}
