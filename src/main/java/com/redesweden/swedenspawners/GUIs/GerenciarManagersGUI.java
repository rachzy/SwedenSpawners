package com.redesweden.swedenspawners.GUIs;

import com.redesweden.swedenspawners.models.Spawner;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class GerenciarManagersGUI {
    private Inventory inventario = Bukkit.createInventory(null, 27, "§cManagers");

    public GerenciarManagersGUI(String senderNickname, Spawner spawner) {
        spawner.getManagers().forEach(manager -> {
            int index = spawner.getManagers().indexOf(manager);

            ItemStack managerHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta managerHeadMeta = (SkullMeta) managerHead.getItemMeta();
            managerHeadMeta.setOwner(manager.getNickname());
            managerHeadMeta.setDisplayName(String.format("§a%s", manager.getNickname()));

            List<String> loreManagerHead = new ArrayList<>();
            if(spawner.getDono().getNickname().equals(senderNickname)) {
                loreManagerHead.add("§7Clique para gerenciar");
            }

            managerHeadMeta.setLore(loreManagerHead);
            managerHead.setItemMeta(managerHeadMeta);

            inventario.setItem(index + 10, managerHead);
        });

        if(spawner.getDono().getNickname().equals(senderNickname)) {
            ItemStack adicionarManager = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta adicionarManagerMeta = (SkullMeta) adicionarManager.getItemMeta();
            adicionarManagerMeta.setOwner("Xester69");
            adicionarManagerMeta.setDisplayName("§aAdicionar Manager");

            List<String> loreAdicionarManager = new ArrayList<>();
            loreAdicionarManager.add("§7Clique para adicionar um novo manager");
            adicionarManagerMeta.setLore(loreAdicionarManager);

            adicionarManager.setItemMeta(adicionarManagerMeta);

            inventario.setItem(16, adicionarManager);
        }

        ItemStack voltar = new ItemStack(Material.ARROW, 1);
        ItemMeta voltarMeta = voltar.getItemMeta();
        voltarMeta.setDisplayName("§eVoltar");

        List<String> loreVoltar = new ArrayList<>();
        loreVoltar.add("§7Clique para voltar");
        voltarMeta.setLore(loreVoltar);

        voltar.setItemMeta(voltarMeta);

        inventario.setItem(22, voltar);
    }

    public Inventory get() {
        return inventario;
    }
}
