package com.redesweden.swedenspawners;

import com.redesweden.swedenspawners.commands.SpawnersCommand;
import com.redesweden.swedenspawners.events.*;
import com.redesweden.swedenspawners.files.LojaFile;
import com.redesweden.swedenspawners.files.PlayersFile;
import com.redesweden.swedenspawners.files.SpawnersFile;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class SwedenSpawners extends JavaPlugin {
    private boolean useHolographicDisplays;
    @Override
    public void onEnable() {
        System.out.println("Ativando SwedenSpawners...");

        // Inicializa o Holographic Displays
        useHolographicDisplays = Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays");

        // Inicializa os arquivos
        LojaFile.setup();
        LojaFile.get().options().copyDefaults(true);
        LojaFile.save();

        PlayersFile.setup();
        PlayersFile.get().options().copyDefaults(true);
        PlayersFile.save();

        SpawnersFile.setup();
        SpawnersFile.get().options().copyDefaults(true);
        SpawnersFile.save();

        // Registrar eventos
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new ChatMessageListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new EntityKillListener(), this);

        //Registrar comandos
        getCommand("spawners").setExecutor(new SpawnersCommand());
    }

    @Override
    public void onDisable() {
        System.out.println("Desativando SwedenSpawners...");
    }
}
