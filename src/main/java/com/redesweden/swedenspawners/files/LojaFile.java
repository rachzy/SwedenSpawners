package com.redesweden.swedenspawners.files;

import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedenspawners.data.SaleSpawners;
import com.redesweden.swedenspawners.models.SpawnerMeta;
import dev.dbassett.skullcreator.SkullCreator;
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
        lojaFile.addDefault("spawners.PORCO.mobID", 90);
        lojaFile.addDefault("spawners.PORCO.head", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmVlODUxNDg5MmYzZDc4YTMyZTg0NTZmY2JiOGM2MDgxZTIxYjI0NmQ4MmYzOThiZDk2OWZlYzE5ZDNjMjdiMyJ9fX0=");
        lojaFile.addDefault("spawners.PORCO.dropID", 319);
        lojaFile.addDefault("spawners.PORCO.dropSubID", 0);
        lojaFile.addDefault("spawners.PORCO.cor", "PINK");
        lojaFile.addDefault("spawners.PORCO.preco", "10M");
        lojaFile.addDefault("spawners.PORCO.precoPorDrop", "1M");

        for(String spawner : lojaFile.getConfigurationSection("spawners").getKeys(false)) {
            String spawnerTitle = lojaFile.getString(String.format("spawners.%s.title", spawner));
            int spawnerMobID = lojaFile.getInt(String.format("spawners.%s.mobID", spawner));
            String spawnerHead = lojaFile.getString(String.format("spawners.%s.head", spawner));
            int spawnerDropID = lojaFile.getInt(String.format("spawners.%s.dropID", spawner));
            int spawnerDropSubID = lojaFile.getInt(String.format("spawners.%s.dropSubID", spawner));
            String spawnerCor = lojaFile.getString(String.format("spawners.%s.cor", spawner));
            String spawnerPreco = lojaFile.getString(String.format("spawners.%s.preco", spawner));
            String spawnerPrecoPDrop = lojaFile.getString(String.format("spawners.%s.precoPorDrop", spawner));

            EntityType spawnerMob = EntityType.fromId(spawnerMobID);

            ItemStack head = SkullCreator.itemFromBase64(spawnerHead);

            ItemStack drop = new ItemStack(Material.getMaterial(spawnerDropID), 1, (short) spawnerDropSubID);
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
                        spawnerMob,
                        head,
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
