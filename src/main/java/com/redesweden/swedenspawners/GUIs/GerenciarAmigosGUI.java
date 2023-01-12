package com.redesweden.swedenspawners.GUIs;

import com.redesweden.swedenspawners.models.Spawner;
import com.redesweden.swedenspawners.models.SpawnerAmigo;
import dev.dbassett.skullcreator.SkullCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class GerenciarAmigosGUI {
    private final Inventory inventario = Bukkit.createInventory(null, 27, "§cAmigos");

    public GerenciarAmigosGUI(String senderNickname, Spawner spawner) {
        for(int i = 0; i < 5; i++) {
            try {
                SpawnerAmigo amigo = spawner.getAmigos().get(i);

                ItemStack amigoHead = SkullCreator.itemFromName(amigo.getNickname());
                SkullMeta amigoHeadMeta = (SkullMeta) amigoHead.getItemMeta();
                amigoHeadMeta.setDisplayName(String.format("§a%s", amigo.getNickname()));

                List<String> loreAmigoHead = new ArrayList<>();
                if(spawner.getDono().getNickname().equals(senderNickname)) {
                    loreAmigoHead.add("§7Clique para gerenciar");
                }

                amigoHeadMeta.setLore(loreAmigoHead);
                amigoHead.setItemMeta(amigoHeadMeta);

                inventario.setItem(i + 10, amigoHead);
            } catch (Exception e) {
                ItemStack vidro = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
                ItemMeta vidroMeta = vidro.getItemMeta();
                vidroMeta.setDisplayName("§eVaga");

                List<String> loreVidro = new ArrayList<>();
                loreVidro.add("§7Você pode adicionar um jogador");
                loreVidro.add("§7para preencher essa vaga");
                vidroMeta.setLore(loreVidro);

                vidro.setItemMeta(vidroMeta);

                inventario.setItem(i + 10, vidro);
            }
        }

        if(spawner.getDono().getNickname().equals(senderNickname)) {
            ItemStack adicionarAmigo = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA1NmJjMTI0NGZjZmY5OTM0NGYxMmFiYTQyYWMyM2ZlZTZlZjZlMzM1MWQyN2QyNzNjMTU3MjUzMWYifX19");
            SkullMeta adicionarAmigoMeta = (SkullMeta) adicionarAmigo.getItemMeta();
            adicionarAmigoMeta.setDisplayName("§aAdicionar Amigo");

            List<String> loreAdicionarAmigo = new ArrayList<>();
            loreAdicionarAmigo.add("§7Clique para adicionar um novo amigo");
            adicionarAmigoMeta.setLore(loreAdicionarAmigo);

            adicionarAmigo.setItemMeta(adicionarAmigoMeta);

            inventario.setItem(16, adicionarAmigo);
        }

        ItemStack voltar = new ItemStack(Material.ARROW, 1);
        ItemMeta voltarMeta = voltar.getItemMeta();
        voltarMeta.setDisplayName("§eVoltar");

        List<String> loreVoltar = new ArrayList<>();
        loreVoltar.add("§7Clique para voltar");
        voltarMeta.setLore(loreVoltar);

        voltar.setItemMeta(voltarMeta);

        inventario.setItem(22, voltar);
    }

    public Inventory get() {
        return inventario;
    }
}
