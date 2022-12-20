package com.redesweden.swedenspawners.events;

import com.redesweden.swedenspawners.GUIs.SpawnerGUI;
import com.redesweden.swedenspawners.data.EventosEspeciais;
import com.redesweden.swedenspawners.data.Spawners;
import com.redesweden.swedenspawners.models.Spawner;
import com.redesweden.swedenspawners.models.SpawnerAmigo;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player player = e.getPlayer();
        ItemStack itemNaMao = player.getItemInHand();
        Block blocoClicado = e.getClickedBlock();

        Spawner spawner = Spawners.getSpawnerPorLocal(blocoClicado.getLocation().clone().add(1, 0 ,0));

        if(itemNaMao.getType() == Material.SKULL_ITEM || spawner == null) return;

        if(!spawner.getDono().getNickname().equals(player.getDisplayName())) {
            SpawnerAmigo amigo = spawner.getAmigoPorNome(player.getDisplayName());

            if(amigo == null) {
                player.sendMessage("§cVocê não tem permissão para acessar este spawner.");
                return;
            }
        }

        EventosEspeciais.addPlayerGerenciandoSpawner(player, spawner);
        player.openInventory(new SpawnerGUI(spawner).get());
    }
}
