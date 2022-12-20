package com.redesweden.swedenspawners.files;

import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedenspawners.data.SaleSpawners;
import com.redesweden.swedenspawners.models.SpawnerMeta;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.math.BigDecimal;

public class LojaFile {
    private static File file;
    private static FileConfiguration lojaFile;

    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("SwedenSpawners").getDataFolder(), "loja.yml");

        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                System.out.println("Não foi possível criar o arquivo loja.yml... " + e.getMessage());
            }
        }

        lojaFile = YamlConfiguration.loadConfiguration(file);

        lojaFile.addDefault("spawners.PORCO.title", "§eSpawner de §d§lPORCO");
        lojaFile.addDefault("spawners.PORCO.mob", "PIG");
        lojaFile.addDefault("spawners.PORCO.drop", "PORK");
        lojaFile.addDefault("spawners.PORCO.cor", "PINK");
        lojaFile.addDefault("spawners.PORCO.preco", "10M");
        lojaFile.addDefault("spawners.PORCO.precoPorDrop", "1M");

        for(String spawner : lojaFile.getConfigurationSection("spawners").getKeys(false)) {
            String spawnerTitle = lojaFile.getString(String.format("spawners.%s.title", spawner));
            String spawnerMob = lojaFile.getString(String.format("spawners.%s.mob", spawner));
            String spawnerDrop = lojaFile.getString(String.format("spawners.%s.drop", spawner));
            String spawnerCor = lojaFile.getString(String.format("spawners.%s.cor", spawner));
            String spawnerPreco = lojaFile.getString(String.format("spawners.%s.preco", spawner));
            String spawnerPrecoPDrop = lojaFile.getString(String.format("spawners.%s.precoPorDrop", spawner));

            ItemStack drop = new ItemStack(Material.getMaterial(spawnerDrop), 1);
            ItemStack spawnerBloco = new ItemStack(Material.STAINED_CLAY, 1, DyeColor.valueOf(spawnerCor).getData());

            BigDecimal spawnerPrecoBD;
            BigDecimal spawnerPrecoPDropBD;

            try {
                spawnerPrecoBD = new ConverterQuantia(spawnerPreco).emNumeros();
                spawnerPrecoPDropBD = new ConverterQuantia(spawnerPrecoPDrop).emNumeros();
            } catch (Exception e) {
                spawnerPrecoBD = null;
                spawnerPrecoPDropBD = null;
            }

            if(spawnerPrecoBD != null && spawnerPrecoPDropBD != null) {
                SpawnerMeta spawnerMeta = new SpawnerMeta(spawner,
                        spawnerTitle,
                        EntityType.valueOf(spawnerMob),
                        spawnerBloco,
                        drop,
                        spawnerPrecoBD,
                        spawnerPrecoPDropBD
                );
                SaleSpawners.addSpawner(spawnerMeta);
            }
        }
    }

    public static FileConfiguration get() {
        return lojaFile;
    }

    public static void save() {
        try {
            lojaFile.save(file);
        } catch (Exception e) {
            System.out.println("Não foi possível salvar o arquivo loja.yml... " + e.getMessage());
        }
    }
}
