package com.redesweden.swedenspawners.events;

import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedenspawners.GUIs.SpawnerGUI;
import com.redesweden.swedenspawners.data.EventosEspeciais;
import com.redesweden.swedenspawners.data.Players;
import com.redesweden.swedenspawners.data.Spawners;
import com.redesweden.swedenspawners.models.Spawner;
import com.redesweden.swedenspawners.models.SpawnerAmigo;
import com.redesweden.swedenspawners.models.SpawnerPlayer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;

public class PlayerInteractionListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if(e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = e.getPlayer();
        ItemStack itemNaMao = player.getItemInHand();

        if(itemNaMao.getType() == Material.PAPER && itemNaMao.hasItemMeta() && itemNaMao.getItemMeta().getDisplayName().equals("§aCheque de §c§lLIMITE")) {
            SpawnerPlayer playerSpawner = Players.getPlayerByName(player.getName());
            BigDecimal valor;

            try {
                // Pega o valor do cheque na lore
                valor = new ConverterQuantia(itemNaMao.getItemMeta().getLore().get(1).split(" ")[1].substring(3)).emNumeros();

                if(player.isSneaking()) {
                    valor = valor.multiply(BigDecimal.valueOf(itemNaMao.getAmount()));
                }
            } catch (Exception ex) {
                player.sendMessage("§c§lLIMITE §e>> §cOcorreu um erro ao tentar ativar esse cheque...");
                System.out.println(ex.getMessage());
                return;
            }

            playerSpawner.addLimite(valor);

            // Remove o cheque do inventário do player
            if(player.isSneaking() || player.getItemInHand().getAmount() == 1) {
                player.getInventory().setItemInHand(new ItemStack(Material.AIR));
            } else {
                player.getInventory().getItemInHand().setAmount(itemNaMao.getAmount() - 1);
            }


            //Envia a mensagem de confirmação
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 3.0F, 1F);
            player.sendMessage(String.format("§c§lLIMITE §e>> §aVocê ativou um cheque no valor de §c✤%s", new ConverterQuantia(valor).emLetras()));
            return;
        }

        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block blocoClicado = e.getClickedBlock();
        Spawner spawner = Spawners.getSpawnerPorLocal(blocoClicado.getLocation().clone());

        if(itemNaMao.getType() == Material.SKULL_ITEM || spawner == null) return;

        if(!spawner.getDono().getNickname().equals(player.getName())) {
            SpawnerAmigo amigo = spawner.getAmigoPorNome(player.getName());

            if(amigo == null) {
                player.sendMessage("§cVocê não tem permissão para acessar este spawner.");
                return;
            }
        }

        EventosEspeciais.addPlayerGerenciandoSpawner(player, spawner);
        player.openInventory(new SpawnerGUI(spawner).get());
        player.playSound(player.getLocation(), Sound.CLICK, 3.0F, 2.5F);
    }
}
