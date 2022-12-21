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

public class MelhoriasGUI {
    private Inventory inventario = Bukkit.createInventory(null, 27, "§eMelhorias");

    public MelhoriasGUI(Spawner spawner) {
        ItemStack melhoriaTempoDeSpawn = new ItemStack(Material.PAPER, 1);
        ItemMeta melhoriaTempoDeSpawnMeta = melhoriaTempoDeSpawn.getItemMeta();
        melhoriaTempoDeSpawnMeta.setDisplayName("§eTempo de Spawn");
        List<String> loreMelhoriaTempoDeSpawn = new ArrayList<>();
        loreMelhoriaTempoDeSpawn.add("§7Level atual: §a" + spawner.getLevelTempoDeSpawn());
        if(spawner.getLevelTempoDeSpawn() <= 5) {
            loreMelhoriaTempoDeSpawn.add("§7Preço do melhoria: §6" + new ConverterQuantia(new BigDecimal(400000 * spawner.getLevelTempoDeSpawn())).emLetras() + " Cash");
            loreMelhoriaTempoDeSpawn.add("");
            loreMelhoriaTempoDeSpawn.add("§bClique para evoluir");
        } else {
            loreMelhoriaTempoDeSpawn.add("§cLevel máximo atingido");
        }
        melhoriaTempoDeSpawnMeta.setLore(loreMelhoriaTempoDeSpawn);

        melhoriaTempoDeSpawn.setItemMeta(melhoriaTempoDeSpawnMeta);

        ItemStack melhoriaValorDoDrop = new ItemStack(Material.PAPER, 1);
        ItemMeta melhoriaValorDoDropMeta = melhoriaValorDoDrop.getItemMeta();
        melhoriaValorDoDropMeta.setDisplayName("§eValor do Drop");
        List<String> loreMelhoriaValorDoDrop = new ArrayList<>();
        loreMelhoriaValorDoDrop.add("§7Level atual: §a" + spawner.getLevelValorDoDrop());
        if(spawner.getLevelValorDoDrop() <= 5) {
            loreMelhoriaValorDoDrop.add("§7Preço do melhoria: §6" + new ConverterQuantia(new BigDecimal(400000 * spawner.getLevelValorDoDrop())).emLetras()  + " Cash");
            loreMelhoriaValorDoDrop.add("");
            loreMelhoriaValorDoDrop.add("§bClique para evoluir");
        } else {
            loreMelhoriaValorDoDrop.add("§cLevel máximo atingido");
        }
        melhoriaValorDoDropMeta.setLore(loreMelhoriaValorDoDrop);

        melhoriaValorDoDrop.setItemMeta(melhoriaValorDoDropMeta);

        ItemStack melhoriaMultiplicadorDeSpawn = new ItemStack(Material.PAPER, 1);
        ItemMeta melhoriaMultiplicadorDeSpawnMeta = melhoriaMultiplicadorDeSpawn.getItemMeta();
        melhoriaMultiplicadorDeSpawnMeta.setDisplayName("§eMultiplicador de Spawn");
        List<String> loreMelhoriaMultiplicadorDeSpawn = new ArrayList<>();
        loreMelhoriaMultiplicadorDeSpawn.add("§7Level atual: §a" + spawner.getLevelMultiplicadorDeSpawn());
        if(spawner.getLevelMultiplicadorDeSpawn() <= 5) {
            loreMelhoriaMultiplicadorDeSpawn.add("§7Preço do melhoria: §6" + new ConverterQuantia(new BigDecimal(400000 * spawner.getLevelMultiplicadorDeSpawn())).emLetras() + " Cash");
            loreMelhoriaMultiplicadorDeSpawn.add("");
            loreMelhoriaMultiplicadorDeSpawn.add("§bClique para evoluir");
        } else {
            loreMelhoriaMultiplicadorDeSpawn.add("§cLevel máximo atingido");
        }
        melhoriaMultiplicadorDeSpawnMeta.setLore(loreMelhoriaMultiplicadorDeSpawn);

        melhoriaMultiplicadorDeSpawn.setItemMeta(melhoriaMultiplicadorDeSpawnMeta);

        ItemStack voltarItem = new ItemStack(Material.ARROW, 1);
        ItemMeta voltarItemMeta = voltarItem.getItemMeta();
        voltarItemMeta.setDisplayName("§eVoltar");
        List<String> loreVoltarItem = new ArrayList<>();
        loreVoltarItem.add("§7Clique para voltar");
        voltarItemMeta.setLore(loreVoltarItem);

        voltarItem.setItemMeta(voltarItemMeta);

        this.inventario.setItem(12, melhoriaTempoDeSpawn);
        this.inventario.setItem(13, melhoriaValorDoDrop);
        this.inventario.setItem(14, melhoriaMultiplicadorDeSpawn);
        this.inventario.setItem(22, voltarItem);
    }

    public Inventory get() {
        return this.inventario;
    }
}
