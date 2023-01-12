package com.redesweden.swedenspawners.GUIs;

import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedenspawners.models.Spawner;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GerenciarDropsGUI {
    private final Inventory inventario = Bukkit.createInventory(null, 27, "§2Drops");

    public GerenciarDropsGUI(Spawner spawner) {
        ItemStack dropsItem = spawner.getSpawnerMeta().getDrop().clone();
        ItemMeta dropsItemMeta = dropsItem.getItemMeta();
        dropsItemMeta.setDisplayName("§eDrops do spawner");
        List<String> loreDropsItem = new ArrayList<>();
        loreDropsItem.add("");
        loreDropsItem.add(String.format(" §e| §fDrops armazenados: §e%s", new ConverterQuantia(spawner.getDropsAramazenados()).emLetras()));
        loreDropsItem.add("");
        loreDropsItem.add(String.format(" §2| §7Valor unitário: §2$%s", new ConverterQuantia(spawner.getSpawnerMeta().getPrecoPorDrop()).emLetras()));
        loreDropsItem.add(String.format(" §a| §fValor total: §a$%s", new ConverterQuantia(spawner.getDropsAramazenados().multiply(spawner.getSpawnerMeta().getPrecoPorDrop().multiply(BigDecimal.valueOf(spawner.getLevelValorDoDrop())))).emLetras()));
        loreDropsItem.add("");
        loreDropsItem.add("§aClique para vender");
        dropsItemMeta.setLore(loreDropsItem);

        dropsItem.setItemMeta(dropsItemMeta);

        ItemStack limparDropsItem = new ItemStack(Material.WEB, 1);
        ItemMeta limparDropsMeta = limparDropsItem.getItemMeta();
        limparDropsMeta.setDisplayName("§cLimpar Drops");
        List<String> loreLimparDrops = new ArrayList<>();
        loreLimparDrops.add("§7Limpa todos os drops do spawner");
        limparDropsMeta.setLore(loreLimparDrops);

        limparDropsItem.setItemMeta(limparDropsMeta);

        ItemStack voltarItem = new ItemStack(Material.ARROW, 1);
        ItemMeta voltarItemMeta = voltarItem.getItemMeta();
        voltarItemMeta.setDisplayName("§eVoltar");
        List<String> loreVoltar = new ArrayList<>();
        loreVoltar.add("§7Clique para voltar");
        voltarItemMeta.setLore(loreVoltar);

        voltarItem.setItemMeta(voltarItemMeta);

        inventario.setItem(12, dropsItem);
        inventario.setItem(14, limparDropsItem);
        inventario.setItem(22, voltarItem);
    }

    public Inventory get() {
        return this.inventario;
    }
}
