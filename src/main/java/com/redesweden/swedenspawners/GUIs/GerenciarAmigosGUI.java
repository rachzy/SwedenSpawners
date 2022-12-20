package com.redesweden.swedenspawners.GUIs;

import com.redesweden.swedenspawners.models.Spawner;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class GerenciarAmigosGUI {
    private Inventory inventario = Bukkit.createInventory(null, 27, "§cAmigos");

    public GerenciarAmigosGUI(String senderNickname, Spawner spawner) {
        spawner.getAmigos().forEach(amigo -> {
            int index = spawner.getAmigos().indexOf(amigo);

            ItemStack amigoHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta amigoHeadMeta = (SkullMeta) amigoHead.getItemMeta();
            amigoHeadMeta.setOwner(amigo.getNickname());
            amigoHeadMeta.setDisplayName(String.format("§a%s", amigo.getNickname()));

            List<String> loreAmigoHead = new ArrayList<>();
            if(spawner.getDono().getNickname().equals(senderNickname)) {
                loreAmigoHead.add("§7Clique para gerenciar");
            }

            amigoHeadMeta.setLore(loreAmigoHead);
            amigoHead.setItemMeta(amigoHeadMeta);

            inventario.setItem(index + 10, amigoHead);
        });

        if(spawner.getDono().getNickname().equals(senderNickname)) {
            ItemStack adicionarAmigo = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta adicionarAmigoMeta = (SkullMeta) adicionarAmigo.getItemMeta();
            adicionarAmigoMeta.setOwner("Xester69");
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
