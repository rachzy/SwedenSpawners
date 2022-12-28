package com.redesweden.swedenspawners.models;

import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SpawnerMeta {
    private String id;
    private String title;
    private EntityType mob;
    private ItemStack head;
    private ItemStack drop;
    private ItemStack bloco;
    private BigDecimal preco;
    private BigDecimal precoPorDrop;

    public SpawnerMeta(String id, String title, EntityType mob, ItemStack head, ItemStack bloco, ItemStack drop, BigDecimal preco, BigDecimal precoPorDrop) {
        this.id = id;
        this.title = title;
        this.mob = mob;
        this.head = head;
        this.bloco = bloco;
        this.drop = drop;
        this.preco = preco;
        this.precoPorDrop = precoPorDrop;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public EntityType getMob() {
        return mob;
    }

    public ItemStack getHead() {
        return head;
    }

    public ItemStack getBloco() {
        return bloco;
    }

    public ItemStack getDrop() {
        return drop;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public BigDecimal getPrecoPorDrop() {
        return precoPorDrop;
    }

    public ItemStack getSpawner(BigDecimal quantidade) {
        ItemStack spawner = head;
        SkullMeta spawnerMeta = (SkullMeta) spawner.getItemMeta();
        spawnerMeta.setDisplayName(this.title);

        List<String> spawnerLore = new ArrayList<>();
        spawnerLore.add("§fQuantidade: §a" + new ConverterQuantia(quantidade).emLetras());
        spawnerMeta.setLore(spawnerLore);

        // Remove o 'org.bukkit.entity' do nome da entidade
        String nomeDaEntidade = this.getMob().getEntityClass().getName().split("\\.")[3];

        spawner.setItemMeta(spawnerMeta);
        return spawner;
    }
}
