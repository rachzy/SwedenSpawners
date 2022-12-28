package com.redesweden.swedenspawners.GUIs;

import com.redesweden.swedenspawners.models.SpawnerAmigo;
import dev.dbassett.skullcreator.SkullCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class GerenciarAmigoGUI {
    private final Inventory inventario = Bukkit.createInventory(null, 27, "§3Amigo");

    public GerenciarAmigoGUI(SpawnerAmigo amigo) {
        ItemStack amigoHead = SkullCreator.itemFromName(amigo.getNickname());
        SkullMeta amigoHeadMeta = (SkullMeta) amigoHead.getItemMeta();
        amigoHeadMeta.setDisplayName("§a" + amigo.getNickname());

        amigoHead.setItemMeta(amigoHeadMeta);

        ItemStack permissaoVenderHead;
        List<String> lorePermissaoVender = new ArrayList<>();
        if(amigo.getPermissaoVender()) {
            permissaoVenderHead = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTkwNzkzZjU2NjE2ZjEwMTUwMmRlMWQzNGViMjU0NGY2MDdkOTg5MDBlMzY5OTM2OTI5NTMxOWU2MzBkY2Y2ZCJ9fX0=");
            lorePermissaoVender.add("§aAtivada");
        } else {
            permissaoVenderHead = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWVhNmFmNzBlZWVjNmZiMTkwMjJjODFlNTZmZjcyYmNjNWY4ZjBmM2UwODcyMWEyM2UzMGRkMWMxZjllMGNmMiJ9fX0=");
            lorePermissaoVender.add("§cDesativada");
        }

        SkullMeta permissaoVenderHeadMeta = (SkullMeta) permissaoVenderHead.getItemMeta();
        permissaoVenderHeadMeta.setDisplayName("§ePermissão de Vender");

        lorePermissaoVender.add("");
        lorePermissaoVender.add("§7Clique para alterar");
        permissaoVenderHeadMeta.setLore(lorePermissaoVender);

        permissaoVenderHead.setItemMeta(permissaoVenderHeadMeta);

        ItemStack permissaoMatarHead;

        List<String> lorePermissaoMatar = new ArrayList<>();
        if(amigo.getPermissaoMatar()) {
            permissaoMatarHead = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTkwNzkzZjU2NjE2ZjEwMTUwMmRlMWQzNGViMjU0NGY2MDdkOTg5MDBlMzY5OTM2OTI5NTMxOWU2MzBkY2Y2ZCJ9fX0=");
            lorePermissaoMatar.add("§aAtivada");
        } else {
            permissaoMatarHead = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWVhNmFmNzBlZWVjNmZiMTkwMjJjODFlNTZmZjcyYmNjNWY4ZjBmM2UwODcyMWEyM2UzMGRkMWMxZjllMGNmMiJ9fX0=");
            lorePermissaoMatar.add("§cDesativada");
        }

        SkullMeta permissaoMatarHeadMeta = (SkullMeta) permissaoMatarHead.getItemMeta();
        permissaoMatarHeadMeta.setDisplayName("§ePermissão de Matar");

        lorePermissaoMatar.add("");
        lorePermissaoMatar.add("§7Clique para alterar");
        permissaoMatarHeadMeta.setLore(lorePermissaoMatar);

        permissaoMatarHead.setItemMeta(permissaoMatarHeadMeta);

        ItemStack permissaoQuebrarHead;
        List<String> lorePermissaoQuebrar = new ArrayList<>();
        if(amigo.getPermissaoQuebrar()) {
            permissaoQuebrarHead = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTkwNzkzZjU2NjE2ZjEwMTUwMmRlMWQzNGViMjU0NGY2MDdkOTg5MDBlMzY5OTM2OTI5NTMxOWU2MzBkY2Y2ZCJ9fX0=");
            lorePermissaoQuebrar.add("§aAtivada");
        } else {
            permissaoQuebrarHead = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWVhNmFmNzBlZWVjNmZiMTkwMjJjODFlNTZmZjcyYmNjNWY4ZjBmM2UwODcyMWEyM2UzMGRkMWMxZjllMGNmMiJ9fX0=");
            lorePermissaoQuebrar.add("§cDesativada");
        }

        SkullMeta permissaoQuebrarHeadMeta = (SkullMeta) permissaoQuebrarHead.getItemMeta();
        permissaoQuebrarHeadMeta.setDisplayName("§ePermissão de Quebrar");

        lorePermissaoQuebrar.add("");
        lorePermissaoQuebrar.add("§7Clique para alterar");
        permissaoQuebrarHeadMeta.setLore(lorePermissaoQuebrar);

        permissaoQuebrarHead.setItemMeta(permissaoQuebrarHeadMeta);

        ItemStack removerAmigo = new ItemStack(Material.BARRIER, 1);
        ItemMeta removerAmigoMeta = removerAmigo.getItemMeta();
        removerAmigoMeta.setDisplayName("§cRemover Amigo");
        List<String> loreRemoverAmigo = new ArrayList<>();
        loreRemoverAmigo.add("§7Clique para remover este");
        loreRemoverAmigo.add("§7jogador da lista de amigos.");
        removerAmigoMeta.setLore(loreRemoverAmigo);

        removerAmigo.setItemMeta(removerAmigoMeta);

        ItemStack voltar = new ItemStack(Material.ARROW, 1);
        ItemMeta voltarMeta = voltar.getItemMeta();
        voltarMeta.setDisplayName("§eVoltar");

        List<String> loreVoltar = new ArrayList<>();
        loreVoltar.add("§7Clique para voltar");
        voltarMeta.setLore(loreVoltar);

        voltar.setItemMeta(voltarMeta);

        inventario.setItem(10, amigoHead);
        inventario.setItem(12, permissaoVenderHead);
        inventario.setItem(13, permissaoMatarHead);
        inventario.setItem(14, permissaoQuebrarHead);
        inventario.setItem(16, removerAmigo);
        inventario.setItem(22, voltar);
    }

    public Inventory get() {
        return this.inventario;
    }
}
