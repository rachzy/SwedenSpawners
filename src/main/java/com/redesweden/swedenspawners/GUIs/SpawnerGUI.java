package com.redesweden.swedenspawners.GUIs;

import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedenspawners.models.Spawner;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class SpawnerGUI {
    private final Inventory inventario = Bukkit.createInventory(null, 27, "§9Gerenciar Spawner");

    public SpawnerGUI(Spawner spawner) {
        ItemStack gerenciarDrops = new ItemStack(Material.MAGMA_CREAM, 1);
        ItemMeta gerenciarDropsMeta = gerenciarDrops.getItemMeta();
        gerenciarDropsMeta.setDisplayName("§eGerenciar Drops");
        List<String> loreGerenciarDropsMeta = new ArrayList<>();
        loreGerenciarDropsMeta.add("§aClique para acessar");
        gerenciarDropsMeta.setLore(loreGerenciarDropsMeta);

        gerenciarDrops.setItemMeta(gerenciarDropsMeta);

        ItemStack gerenciarAmigos = new ItemStack(Material.ARMOR_STAND, 1);
        ItemMeta gerenciarAmigosMeta = gerenciarAmigos.getItemMeta();
        gerenciarAmigosMeta.setDisplayName("§eGerenciar Managers");
        List<String> loreGerenciarAmigosMeta = new ArrayList<>();
        loreGerenciarAmigosMeta.add("§aClique para acessar");
        gerenciarAmigosMeta.setLore(loreGerenciarAmigosMeta);

        gerenciarAmigos.setItemMeta(gerenciarAmigosMeta);

        ItemStack info = new ItemStack(Material.SIGN, 1);
        ItemMeta infoMeta = gerenciarAmigos.getItemMeta();
        infoMeta.setDisplayName("§eInformações do Spawner");
        List<String> loreInfoMeta = new ArrayList<>();
        loreInfoMeta.add("§7Entidade: §a" + spawner.getSpawnerMeta().getMob().getName());
        loreInfoMeta.add("§7Quantidade stackada: §a" + new ConverterQuantia(spawner.getQuantidadeStackada()).emLetras());
        loreInfoMeta.add("§7Quantidade de Managers: §a" + spawner.getManagers().toArray().length);
        infoMeta.setLore(loreInfoMeta);

        info.setItemMeta(infoMeta);

        ItemStack ligarOuDesligar = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta ligarOuDesligarMeta = (SkullMeta) ligarOuDesligar.getItemMeta();
        ligarOuDesligarMeta.setDisplayName("§eLigar ou desligar");

        List<String> lore = new ArrayList<>();
        if(spawner.getAtivado()) {
            ligarOuDesligarMeta.setOwner("Xester69");
            lore.add("§7Status da máquina: §aON");
        } else {
            ligarOuDesligarMeta.setOwner("Bosscartoon180");
            lore.add("§7Status da máquina: §cOFF");
        }
        ligarOuDesligarMeta.setLore(lore);

        ligarOuDesligar.setItemMeta(ligarOuDesligarMeta);

        inventario.setItem(10, gerenciarDrops);
        inventario.setItem(11, gerenciarAmigos);
        inventario.setItem(12, info);
        inventario.setItem(15, ligarOuDesligar);
    }

    public Inventory get() {
        return inventario;
    }
}
