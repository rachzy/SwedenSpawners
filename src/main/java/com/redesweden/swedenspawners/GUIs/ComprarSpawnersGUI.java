package com.redesweden.swedenspawners.GUIs;

import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedenspawners.data.Players;
import com.redesweden.swedenspawners.data.SaleSpawners;
import com.redesweden.swedenspawners.models.SpawnerMeta;
import com.redesweden.swedenspawners.models.SpawnerPlayer;
import dev.dbassett.skullcreator.SkullCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class ComprarSpawnersGUI {
    final private Inventory inventario = Bukkit.createInventory(null, 36, "§9Spawners");
    public ComprarSpawnersGUI(String playerNickname) {
        SpawnerPlayer player = Players.getPlayerByName(playerNickname);

        // Item que mostra o limite ao jogador
        ItemStack limiteItem = new ItemStack(Material.NAME_TAG, 1);
        ItemMeta limiteItemMeta = limiteItem.getItemMeta();
        limiteItemMeta.setDisplayName("§eLimite de Compra");

        List<String> loreItemMeta = new ArrayList<>();
        loreItemMeta.add("§7Seu limite: §a" + new ConverterQuantia(player.getLimite()).emLetras());
        limiteItemMeta.setLore(loreItemMeta);

        limiteItem.setItemMeta(limiteItemMeta);

        for(int i = 0; i <= 13; i++) {
            int saltRounds = 10;
            if(i >= 7) {
                saltRounds = 12;
            }
            SpawnerMeta spawner;
            try {
                spawner = SaleSpawners.getSpawners().get(i);
            } catch (Exception e) {
                spawner = null;
            }

            ItemStack spawnerItem;
            if(spawner == null) {
                spawnerItem = new ItemStack(Material.BARRIER, 1);
                ItemMeta barrierMeta = spawnerItem.getItemMeta();
                barrierMeta.setDisplayName("§cSpawner ainda não disponível.");
                spawnerItem.setItemMeta(barrierMeta);
            } else {
                spawnerItem = spawner.getHead();
                SkullMeta spawnerHeadMeta = (SkullMeta) spawnerItem.getItemMeta();

                // Remove o 'org.bukkit.entity' do nome da entidade
                String nomeDaEntidade = spawner.getMob().getEntityClass().getName().split("\\.")[3];

                spawnerHeadMeta.setDisplayName(spawner.getTitulo());

                List<String> headLore = new ArrayList<>();
                headLore.add(String.format("§7Preço: §a$%s", new ConverterQuantia(spawner.getPreco()).emLetras()));
                headLore.add(String.format("§7Valor por drop: §a$%s", new ConverterQuantia(spawner.getPrecoPorDrop()).emLetras()));
                headLore.add("");
                headLore.add("§7Clique §a§lESQUERDO §7para comprar 1");
                headLore.add("§7Clique §a§lDIREITO §7para inserir quantidade");
                headLore.add("§7Aperte §a§lQ §7para comprar o limite");
                spawnerHeadMeta.setLore(headLore);

                spawnerItem.setItemMeta(spawnerHeadMeta);
            }
            inventario.setItem(i + saltRounds, spawnerItem);
        }

        ItemStack multiplicadorItem = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGRmOWM5YTU4NTJiNDVkNzY4NmRmYzYzNjg5ODhmMDY5NDE3MGMyNDY3ZTJhODJiYzQwYzRjZjk3YzVlNjc0In19fQ==");
        SkullMeta multiplicadorItemMeta = (SkullMeta) multiplicadorItem.getItemMeta();
        multiplicadorItemMeta.setDisplayName("§eMultiplicador de Compra");

        List<String> loreMultiplicador = new ArrayList<>();
        loreMultiplicador.add("§7O multiplicador permite que você compre");
        loreMultiplicador.add("§7vários spawners de uma vez, multiplicando");
        loreMultiplicador.add("§7seu limite de compra em X vezes");
        loreMultiplicador.add("");
        loreMultiplicador.add("§eSeu multiplicador atual: §a" + player.getMultiplicador());
        loreMultiplicador.add("§aClique para alterar");
        multiplicadorItemMeta.setLore(loreMultiplicador);

        multiplicadorItem.setItemMeta(multiplicadorItemMeta);

        inventario.setItem(4, limiteItem);
        inventario.setItem(31, multiplicadorItem);
    }
    public Inventory get() {
        return this.inventario;
    }
}
