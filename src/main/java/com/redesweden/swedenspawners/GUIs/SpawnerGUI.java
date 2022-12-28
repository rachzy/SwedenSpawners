package com.redesweden.swedenspawners.GUIs;

import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedenspawners.models.Spawner;
import dev.dbassett.skullcreator.SkullCreator;
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
        gerenciarAmigosMeta.setDisplayName("§eGerenciar Amigos");
        List<String> loreGerenciarAmigosMeta = new ArrayList<>();
        loreGerenciarAmigosMeta.add("§aClique para acessar");
        gerenciarAmigosMeta.setLore(loreGerenciarAmigosMeta);

        gerenciarAmigos.setItemMeta(gerenciarAmigosMeta);

        ItemStack melhorias = new ItemStack(Material.BEACON, 1);
        ItemMeta boosterMeta = melhorias.getItemMeta();
        boosterMeta.setDisplayName("§eMelhorias");
        List<String> loreBoosterMeta = new ArrayList<>();
        loreBoosterMeta.add("§aClique para acessar");
        boosterMeta.setLore(loreBoosterMeta);

        melhorias.setItemMeta(boosterMeta);

        ItemStack info = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjQ3ZTJlNWQ1NWI2ZDA0OTQzNTE5YmVkMjU1N2M2MzI5ZTMzYjYwYjkwOWRlZTg5MjNjZDg4YjExNTIxMCJ9fX0=");;
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName("§eInformações do Spawner");
        List<String> loreInfoMeta = new ArrayList<>();
        loreInfoMeta.add("§7Entidade: §a" + spawner.getSpawnerMeta().getMob().getName());
        loreInfoMeta.add("§7Quantidade stackada: §a" + new ConverterQuantia(spawner.getQuantidadeStackada()).emLetras());
        loreInfoMeta.add("§7Quantidade de Amigos: §a" + spawner.getAmigos().toArray().length);
        infoMeta.setLore(loreInfoMeta);

        info.setItemMeta(infoMeta);

        ItemStack ligarOuDesligar;

        List<String> lore = new ArrayList<>();
        if(spawner.getAtivado()) {
            ligarOuDesligar = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTkwNzkzZjU2NjE2ZjEwMTUwMmRlMWQzNGViMjU0NGY2MDdkOTg5MDBlMzY5OTM2OTI5NTMxOWU2MzBkY2Y2ZCJ9fX0=");
            lore.add("§7Status do spawner: §aON");
        } else {
            ligarOuDesligar = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWVhNmFmNzBlZWVjNmZiMTkwMjJjODFlNTZmZjcyYmNjNWY4ZjBmM2UwODcyMWEyM2UzMGRkMWMxZjllMGNmMiJ9fX0=");
            lore.add("§7Status do spawner: §cOFF");
        }
        SkullMeta ligarOuDesligarMeta = (SkullMeta) ligarOuDesligar.getItemMeta();
        ligarOuDesligarMeta.setDisplayName("§eLigar ou desligar");

        ligarOuDesligarMeta.setLore(lore);

        ligarOuDesligar.setItemMeta(ligarOuDesligarMeta);

        inventario.setItem(10, gerenciarDrops);
        inventario.setItem(11, gerenciarAmigos);
        inventario.setItem(12, melhorias);
        inventario.setItem(13, info);
        inventario.setItem(15, ligarOuDesligar);
    }

    public Inventory get() {
        return inventario;
    }
}
