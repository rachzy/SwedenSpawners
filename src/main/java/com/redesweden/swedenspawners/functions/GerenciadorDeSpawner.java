package com.redesweden.swedenspawners.functions;

import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedenspawners.data.EventosEspeciais;
import com.redesweden.swedenspawners.data.SaleSpawners;
import com.redesweden.swedenspawners.models.Spawner;
import com.redesweden.swedenspawners.models.SpawnerAmigo;
import com.redesweden.swedenspawners.models.SpawnerMeta;
import eu.decentsoftware.holograms.api.DHAPI;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.util.List;

public class GerenciadorDeSpawner {
    private final Player player;
    private final Spawner spawner;

    public GerenciadorDeSpawner(Player player, Spawner spawner) {
        this.player = player;
        this.spawner = spawner;
    }

    public void removerQuantia(BigDecimal quantia) {
        if(!spawner.getDono().getNickname().equals(player.getDisplayName())) {
            SpawnerAmigo amigo = spawner.getAmigoPorNome(player.getDisplayName());

            if (amigo == null || !amigo.getPermissaoRetirar()) {
                EventosEspeciais.removePlayerRemovendoSpawners(player);
                player.sendMessage("§cVocê não tem permissão para retirar esse spawner");
                return;
            }
        }

            if(spawner.getQuantidadeStackada().compareTo(quantia) < 0) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§cEste spawner não possui essa quantia stackada.");
                player.sendMessage("§7(Digite 'CANCELAR' para cancelar esta operação)");
                return;
            }

            if(player.getInventory().firstEmpty() == -1) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§cVocê precisa ter pelo menos 1 slot vazio em seu inventário para retirar o spawner.");
                player.sendMessage("§7(Digite 'CANCELAR' para cancelar esta operação)");
                return;
            }

            if(quantia.compareTo(spawner.getQuantidadeStackada()) == 0) {
                removerPorCompleto();
                EventosEspeciais.removePlayerRemovendoSpawners(player);
                return;
            }

            spawner.subQuantidadesStackadas(quantia);
            ItemStack novosSpawners = spawner.getSpawnerMeta().getSpawner(quantia);
            player.getInventory().addItem(novosSpawners);

            player.playSound(player.getLocation(), Sound.NOTE_PLING, 3.0F, 2F);
            player.sendMessage(String.format("§cVocê removeu §f%s §cspawner(s)", new ConverterQuantia(quantia).emLetras()));

            Player dono = player.getServer().getPlayer(spawner.getDono().getNickname());
            if(dono != null && dono.isOnline() && !dono.getDisplayName().equals(player.getDisplayName())) {
                dono.playSound(player.getLocation(), Sound.NOTE_PLING, 3.0F, 2F);
                dono.sendMessage(String.format("§e%s §cremoveu §f%s %s", player.getDisplayName(), new ConverterQuantia(quantia).emLetras(), spawner.getSpawnerMeta().getTitulo()));
            }

    }

    public void removerPorCompleto() {
        if(!spawner.getDono().getNickname().equals(player.getDisplayName())) {
            SpawnerAmigo amigo = spawner.getAmigoPorNome(player.getDisplayName());

            if(amigo == null || !amigo.getPermissaoRetirar()) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§cVocê não tem permissão para retirar esse spawner");
                return;
            }
        }

        if(player.getInventory().firstEmpty() == -1) {
            player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
            player.sendMessage("§cVocê precisa ter pelo menos 1 slot vazio em seu inventário para retirar o spawner.");
            return;
        }

        SpawnerMeta spawnerMeta = SaleSpawners.getSpawnerPorTitulo(spawner.getSpawnerMeta().getTitulo());
        BigDecimal quantidadeStackada = spawner.getQuantidadeStackada();

        ItemStack spawnerComId = spawnerMeta.getSpawner(quantidadeStackada);
        ItemMeta spawnerComIdMeta = spawnerComId.getItemMeta();
        List<String> spawnerComIdLore = spawnerComIdMeta.getLore();
        spawnerComIdLore.add("§9" + spawner.getId());
        spawnerComIdLore.add("§7(Este spawner já foi utilizado)");
        spawnerComIdMeta.setLore(spawnerComIdLore);
        spawnerComId.setItemMeta(spawnerComIdMeta);

        spawner.getLocal().getBlock().setType(Material.AIR);
        player.getInventory().addItem(spawnerComId);
        player.sendMessage(String.format("§aVocê retirou §f%s §aspawners.", new ConverterQuantia(quantidadeStackada).emLetras()));

        // Remover players de eventos especiais relacionados a spawners
        EventosEspeciais.getPlayersAdicionandoAmigos().forEach(evento -> {
            if(evento.getSpawner().getId().equals(spawner.getId())) {
                Player playerIn = player.getServer().getPlayer(evento.getNick());
                if(playerIn == null || !playerIn.isOnline()) return;
                EventosEspeciais.removePlayerAdicionandoAmigo(playerIn);
                playerIn.sendMessage("§cSeu spawner foi quebrado, logo este evento foi cancelado.");
            }
        });

        EventosEspeciais.getPlayersGerenciandoSpawners().forEach(evento -> {
            if(evento.getSpawner().getId().equals(spawner.getId())) {
                Player playerIn = player.getServer().getPlayer(evento.getNick());
                if(playerIn == null || !playerIn.isOnline()) return;
                EventosEspeciais.removePlayerGerenciandoSpawner(playerIn);
                playerIn.closeInventory();
            }
        });

        spawner.setAtivado(false);
        spawner.desespawnarMob();
        spawner.setRetirado(true);
    }
}
