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

public class UpgradesGUI {
    private Inventory inventario = Bukkit.createInventory(null, 27, "§eUpgrades");

    public UpgradesGUI(Spawner spawner) {
        ItemStack upgradeTempoDeSpawn = new ItemStack(Material.PAPER, 1);
        ItemMeta upgradeTempoDeSpawnMeta = upgradeTempoDeSpawn.getItemMeta();
        upgradeTempoDeSpawnMeta.setDisplayName("§eTempo de Spawn");
        List<String> loreUpgradeTempoDeSpawn = new ArrayList<>();
        loreUpgradeTempoDeSpawn.add("§7Nível atual: §a" + spawner.getLevelTempoDeSpawn());
        loreUpgradeTempoDeSpawn.add("§7Preço do upgrade: §6" + new ConverterQuantia(new BigDecimal(400000 * spawner.getLevelTempoDeSpawn())).emLetras() + " Cash");
        loreUpgradeTempoDeSpawn.add("");
        loreUpgradeTempoDeSpawn.add("§bClique para evoluir");
        upgradeTempoDeSpawnMeta.setLore(loreUpgradeTempoDeSpawn);

        upgradeTempoDeSpawn.setItemMeta(upgradeTempoDeSpawnMeta);

        ItemStack upgradeValorDoDrop = new ItemStack(Material.PAPER, 1);
        ItemMeta upgradeValorDoDropMeta = upgradeValorDoDrop.getItemMeta();
        upgradeValorDoDropMeta.setDisplayName("§eValor do Drop");
        List<String> loreUpgradeValorDoDrop = new ArrayList<>();
        loreUpgradeValorDoDrop.add("§7Nível atual: §a" + spawner.getLevelValorDoDrop());
        loreUpgradeValorDoDrop.add("§7Preço do upgrade: §6" + new ConverterQuantia(new BigDecimal(400000 * spawner.getLevelValorDoDrop())).emLetras()  + " Cash");
        loreUpgradeValorDoDrop.add("");
        loreUpgradeValorDoDrop.add("§bClique para evoluir");
        upgradeValorDoDropMeta.setLore(loreUpgradeValorDoDrop);

        upgradeValorDoDrop.setItemMeta(upgradeValorDoDropMeta);

        ItemStack upgradeMultiplicadorDeSpawn = new ItemStack(Material.PAPER, 1);
        ItemMeta upgradeMultiplicadorDeSpawnMeta = upgradeMultiplicadorDeSpawn.getItemMeta();
        upgradeMultiplicadorDeSpawnMeta.setDisplayName("§eMultiplicador de Spawn");
        List<String> loreUpgradeMultiplicadorDeSpawn = new ArrayList<>();
        loreUpgradeMultiplicadorDeSpawn.add("§7Nível atual: §a" + spawner.getLevelMultiplicadorDeSpawn());
        loreUpgradeMultiplicadorDeSpawn.add("§7Preço do upgrade: §6" + new ConverterQuantia(new BigDecimal(400000 * spawner.getLevelMultiplicadorDeSpawn())).emLetras() + " Cash");
        loreUpgradeMultiplicadorDeSpawn.add("");
        loreUpgradeMultiplicadorDeSpawn.add("§bClique para evoluir");
        upgradeMultiplicadorDeSpawnMeta.setLore(loreUpgradeMultiplicadorDeSpawn);

        upgradeMultiplicadorDeSpawn.setItemMeta(upgradeMultiplicadorDeSpawnMeta);

        ItemStack voltarItem = new ItemStack(Material.ARROW, 1);
        ItemMeta voltarItemMeta = voltarItem.getItemMeta();
        voltarItemMeta.setDisplayName("§eVoltar");
        List<String> loreVoltarItem = new ArrayList<>();
        loreVoltarItem.add("§7Clique para voltar");
        voltarItemMeta.setLore(loreVoltarItem);

        voltarItem.setItemMeta(voltarItemMeta);

        this.inventario.setItem(12, upgradeTempoDeSpawn);
        this.inventario.setItem(13, upgradeValorDoDrop);
        this.inventario.setItem(14, upgradeMultiplicadorDeSpawn);
        this.inventario.setItem(22, voltarItem);
    }

    public Inventory get() {
        return this.inventario;
    }
}
