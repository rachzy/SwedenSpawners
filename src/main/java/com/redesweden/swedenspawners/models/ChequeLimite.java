package com.redesweden.swedenspawners.models;

import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChequeLimite {
    private final String autor;
    private final BigDecimal valor;
    private final LocalDateTime dataDeGeracao;

    public ChequeLimite(String autor, BigDecimal valor, LocalDateTime dataDeGeracao) {
        this.autor = autor;
        this.valor = valor;
        this.dataDeGeracao = dataDeGeracao;
    }

    public ItemStack gerar() {
        ItemStack cheque = new ItemStack(Material.PAPER, 1);
        ItemMeta chequeMeta = cheque.getItemMeta();
        chequeMeta.setDisplayName("§aCheque de §c§lLIMITE");

        List<String> loreCheque = new ArrayList<>();
        loreCheque.add("§7Gerado por: §a" + autor);
        loreCheque.add("§7Valor: §c✤" + new ConverterQuantia(valor).emLetras());
        loreCheque.add("§7Data de geração: §a" + dataDeGeracao);
        chequeMeta.setLore(loreCheque);

        cheque.setItemMeta(chequeMeta);

        return cheque;
    }
}
