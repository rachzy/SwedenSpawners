package com.redesweden.swedenspawners;

import com.redesweden.swedenspawners.commands.LimiteCommand;
import com.redesweden.swedenspawners.commands.SpawnersCommand;
import com.redesweden.swedenspawners.data.Players;
import com.redesweden.swedenspawners.events.*;
import com.redesweden.swedenspawners.files.ConfigFile;
import com.redesweden.swedenspawners.files.PlayersFile;
import com.redesweden.swedenspawners.files.SpawnersFile;
import org.bukkit.plugin.java.JavaPlugin;

public final class SwedenSpawners extends JavaPlugin {
    @Override
    public void onEnable() {
        System.out.println("Ativando SwedenSpawners...");

        getConfig().options().copyDefaults(true);
        saveConfig();

        // Inicializa os arquivos
        ConfigFile.setup();
        ConfigFile.get().options().copyDefaults(true);
        ConfigFile.save();

        PlayersFile.setup();
        PlayersFile.get().options().copyDefaults(true);
        PlayersFile.save();

        SpawnersFile.setup();
        SpawnersFile.get().options().copyDefaults(true);
        SpawnersFile.save();

        // Registrar eventos
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractionListener(), this);
        getServer().getPluginManager().registerEvents(new nChatMessageListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new EntityKillListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageListener(), this);

        //Registrar comandos
        getCommand("spawners").setExecutor(new SpawnersCommand());
        getCommand("limite").setExecutor(new LimiteCommand());
    }

    @Override
    public void onDisable() {
        Players.salvarPlayersModificados();
        System.out.println("Desativando SwedenSpawners...");
    }
}
