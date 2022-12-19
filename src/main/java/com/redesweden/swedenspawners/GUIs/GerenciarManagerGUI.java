package com.redesweden.swedenspawners.GUIs;

import com.redesweden.swedenspawners.models.Spawner;
import com.redesweden.swedenspawners.models.SpawnerManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class GerenciarManagerGUI {
    private Inventory inventario = Bukkit.createInventory(null, 27, "§3Manager");

    public GerenciarManagerGUI(Spawner spawner, SpawnerManager manager) {
        ItemStack managerHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta managerHeadMeta = (SkullMeta) managerHead.getItemMeta();
        managerHeadMeta.setOwner(manager.getNickname());
        managerHeadMeta.setDisplayName("§a" + manager.getNickname());

        managerHead.setItemMeta(managerHeadMeta);

        ItemStack permissaoVenderHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta permissaoVenderHeadMeta = (SkullMeta) permissaoVenderHead.getItemMeta();
        permissaoVenderHeadMeta.setDisplayName("§ePermissão de Vender");
        List<String> lorePermissaoVender = new ArrayList<>();
        if(manager.getPermissaoVender()) {
            permissaoVenderHeadMeta.setOwner("Xester69");
            lorePermissaoVender.add("§aAtivada");
        } else {
            permissaoVenderHeadMeta.setOwner("Bosscartoon180");
            lorePermissaoVender.add("§cDesativada");
        }
        lorePermissaoVender.add("");
        lorePermissaoVender.add("§7Clique para alterar");
        permissaoVenderHeadMeta.setLore(lorePermissaoVender);

        permissaoVenderHead.setItemMeta(permissaoVenderHeadMeta);

        ItemStack permissaoMatarHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta permissaoMatarHeadMeta = (SkullMeta) permissaoMatarHead.getItemMeta();
        permissaoMatarHeadMeta.setDisplayName("§ePermissão de Matar");
        List<String> lorePermissaoMatar = new ArrayList<>();
        if(manager.getPermissaoMatar()) {
            permissaoMatarHeadMeta.setOwner("Xester69");
            lorePermissaoMatar.add("§aAtivada");
        } else {
            permissaoMatarHeadMeta.setOwner("Bosscartoon180");
            lorePermissaoMatar.add("§cDesativada");
        }
        lorePermissaoMatar.add("");
        lorePermissaoMatar.add("§7Clique para alterar");
        permissaoMatarHeadMeta.setLore(lorePermissaoMatar);

        permissaoMatarHead.setItemMeta(permissaoMatarHeadMeta);

        ItemStack permissaoQuebrarHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta permissaoQuebrarHeadMeta = (SkullMeta) permissaoQuebrarHead.getItemMeta();
        permissaoQuebrarHeadMeta.setDisplayName("§ePermissão de Quebrar");
        List<String> lorePermissaoQuebrar = new ArrayList<>();
        if(manager.getPermissaoQuebrar()) {
            permissaoQuebrarHeadMeta.setOwner("Xester69");
            lorePermissaoQuebrar.add("§aAtivada");
        } else {
            permissaoQuebrarHeadMeta.setOwner("Bosscartoon180");
            lorePermissaoQuebrar.add("§cDesativada");
        }
        lorePermissaoQuebrar.add("");
        lorePermissaoQuebrar.add("§7Clique para alterar");
        permissaoQuebrarHeadMeta.setLore(lorePermissaoQuebrar);

        permissaoQuebrarHead.setItemMeta(permissaoQuebrarHeadMeta);

        ItemStack removerManager = new ItemStack(Material.BARRIER, 1);
        ItemMeta removerManagerMeta = removerManager.getItemMeta();
        removerManagerMeta.setDisplayName("§cRemover Manager");
        List<String> loreRemoverManager = new ArrayList<>();
        loreRemoverManager.add("§7Clique para remover este");
        loreRemoverManager.add("§7jogador da lista de managers.");
        removerManagerMeta.setLore(loreRemoverManager);

        removerManager.setItemMeta(removerManagerMeta);

        ItemStack voltar = new ItemStack(Material.ARROW, 1);
        ItemMeta voltarMeta = voltar.getItemMeta();
        voltarMeta.setDisplayName("§eVoltar");

        List<String> loreVoltar = new ArrayList<>();
        loreVoltar.add("§7Clique para voltar");
        voltarMeta.setLore(loreVoltar);

        voltar.setItemMeta(voltarMeta);

        inventario.setItem(10, managerHead);
        inventario.setItem(12, permissaoVenderHead);
        inventario.setItem(13, permissaoMatarHead);
        inventario.setItem(14, permissaoQuebrarHead);
        inventario.setItem(16, removerManager);
        inventario.setItem(22, voltar);
    }

    public Inventory get() {
        return this.inventario;
    }
}
