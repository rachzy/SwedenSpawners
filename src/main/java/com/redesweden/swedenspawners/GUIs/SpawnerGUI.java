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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SpawnerGUI {
    private final Inventory inventario = Bukkit.createInventory(null, 27, "§9Gerenciar Spawner");

    public SpawnerGUI(Spawner spawner) {
        ItemStack gerenciarDrops = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDgwN2YyMDk0MmQ4NGEzYjczM2ExYTJkNjZkZTQ3NWJmNDE2YzJlNmM1NTQyMTM2Nzc0MmIxNzcwNmJhMjE1OSJ9fX0=");
        ItemMeta gerenciarDropsMeta = gerenciarDrops.getItemMeta();
        gerenciarDropsMeta.setDisplayName("§eGerenciar Drops");
        List<String> loreGerenciarDropsMeta = new ArrayList<>();
        loreGerenciarDropsMeta.add("§7Clique para acessar");
        gerenciarDropsMeta.setLore(loreGerenciarDropsMeta);

        gerenciarDrops.setItemMeta(gerenciarDropsMeta);

        ItemStack gerenciarAmigos = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzZjYmFlNzI0NmNjMmM2ZTg4ODU4NzE5OGM3OTU5OTc5NjY2YjRmNWE0MDg4ZjI0ZTI2ZTA3NWYxNDBhZTZjMyJ9fX0=");
        ItemMeta gerenciarAmigosMeta = gerenciarAmigos.getItemMeta();
        gerenciarAmigosMeta.setDisplayName("§eGerenciar Amigos");
        List<String> loreGerenciarAmigosMeta = new ArrayList<>();
        loreGerenciarAmigosMeta.add("§7Clique para acessar");
        gerenciarAmigosMeta.setLore(loreGerenciarAmigosMeta);

        gerenciarAmigos.setItemMeta(gerenciarAmigosMeta);

        ItemStack melhorias = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2I0NWM1ZWI3OGRmZjZmYzQzZjdmOGUzOTg3Mjk0MTQ0MjJhOGViNmYzMTQ1MDVkZjhmZjlhMzNiZGQ2ZDEyZiJ9fX0=");
        ItemMeta boosterMeta = melhorias.getItemMeta();
        boosterMeta.setDisplayName("§eMelhorias");
        List<String> loreBoosterMeta = new ArrayList<>();
        loreBoosterMeta.add("§7Clique para acessar");
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

        ItemStack retirarSpawners = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2VkMWFiYTczZjYzOWY0YmM0MmJkNDgxOTZjNzE1MTk3YmUyNzEyYzNiOTYyYzk3ZWJmOWU5ZWQ4ZWZhMDI1In19fQ==");
        ItemMeta retirarSpawnersMeta = retirarSpawners.getItemMeta();

        if(spawner.getQuantidadeStackada().compareTo(new BigDecimal("1")) <= 0) {
            retirarSpawnersMeta.setDisplayName("§cRetirar spawner");
        } else {
            retirarSpawnersMeta.setDisplayName("§cRetirar spawners");
        }

        List<String> loreRetirar = new ArrayList<>();
        loreRetirar.add("§7Clique para retirar um ou mais spawners");
        loreRetirar.add("§7da quantia total de geradores stackados");
        retirarSpawnersMeta.setLore(loreRetirar);

        retirarSpawners.setItemMeta(retirarSpawnersMeta);

        inventario.setItem(10, gerenciarDrops);
        inventario.setItem(11, gerenciarAmigos);
        inventario.setItem(12, melhorias);
        inventario.setItem(13, info);
        inventario.setItem(15, ligarOuDesligar);
        inventario.setItem(16, retirarSpawners);
    }

    public Inventory get() {
        return inventario;
    }
}
