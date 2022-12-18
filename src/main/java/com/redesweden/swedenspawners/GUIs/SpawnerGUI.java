package com.redesweden.swedenspawners.GUIs;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class SpawnerGUI {
    private Inventory inventario;

    public SpawnerGUI() {
        this.inventario = Bukkit.createInventory(null, 36, "§9Gerenciar Spawner");
    }
}
