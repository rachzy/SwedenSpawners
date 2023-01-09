package com.redesweden.swedenspawners.models;

import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SpawnerMeta {
    private final String id;
    private final String titulo;
    private final EntityType mob;
    private final ItemStack head;
    private final String headBase64;
    private final ItemStack drop;
    private final ItemStack bloco;
    private final BigDecimal preco;
    private final BigDecimal precoPorDrop;

    public SpawnerMeta(String id, String titulo, EntityType mob, ItemStack head, String headBase64, ItemStack bloco, ItemStack drop, BigDecimal preco, BigDecimal precoPorDrop) {
        this.id = id;
        this.titulo = titulo;
        this.mob = mob;
        this.head = head;
        this.headBase64 = headBase64;
        this.bloco = bloco;
        this.drop = drop;
        this.preco = preco;
        this.precoPorDrop = precoPorDrop;
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public EntityType getMob() {
        return mob;
    }

    public ItemStack getHead() {
        return head;
    }

    public String getHeadBase64() {
        return headBase64;
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
        spawnerMeta.setDisplayName(this.titulo);

        List<String> spawnerLore = new ArrayList<>();
        spawnerLore.add("§7Quantidade: §a" + new ConverterQuantia(quantidade).emLetras());
        spawnerMeta.setLore(spawnerLore);

        // Remove o 'org.bukkit.entity' do nome da entidade
        String nomeDaEntidade = this.getMob().getEntityClass().getName().split("\\.")[3];

        spawner.setItemMeta(spawnerMeta);
        return spawner;
    }
}
