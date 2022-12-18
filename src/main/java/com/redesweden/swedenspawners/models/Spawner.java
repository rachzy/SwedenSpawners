package com.redesweden.swedenspawners.models;

import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedenspawners.SwedenSpawners;
import com.redesweden.swedenspawners.files.SpawnersFile;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitScheduler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Spawner {
    private String id;
    private Hologram holograma;
    private SpawnerPlayer dono;
    private SpawnerMeta spawnerMeta;
    private Location local;
    private BigDecimal quantidadeStackada;
    private BigDecimal entidadesSpawnadas;
    private BigDecimal dropsAramazenados;
    private List<SpawnerManager> managers;
    private Boolean ativado;
    private Thread spawnThread;
    private Thread startSpawnerThread;

    public Spawner(String id, SpawnerPlayer dono, SpawnerMeta spawnerMeta, Location local, BigDecimal quantidadeStackada, BigDecimal entidadesSpawnadas, BigDecimal dropsAramazenados, List<SpawnerManager> managers, Boolean ativado) {
        this.id = id;
        this.dono = dono;
        this.spawnerMeta = spawnerMeta;
        this.local = local;
        this.quantidadeStackada = quantidadeStackada;
        this.entidadesSpawnadas = entidadesSpawnadas;
        this.dropsAramazenados = dropsAramazenados;
        this.managers = managers;
        this.ativado = ativado;

        if(this.ativado) {
            this.iniciar();
        }
    }

    public void save(String key, Object valor) {
        SpawnersFile.get().set(String.format("spawners.%s.%s", this.id, key), valor);
        SpawnersFile.save();
    }

    public String getId() {
        return id;
    }

    public SpawnerPlayer getDono() {
        return dono;
    }

    public SpawnerMeta getSpawnerMeta() {
        return spawnerMeta;
    }

    public Location getLocal() {
        return local;
    }

    public BigDecimal getQuantidadeStackada() {
        return quantidadeStackada;
    }

    public BigDecimal getEntidadesSpawnadas() {
        return entidadesSpawnadas;
    }

    public BigDecimal getDropsAramazenados() {
        return dropsAramazenados;
    }

    public List<SpawnerManager> getManagers() {
        return managers;
    }

    public SpawnerManager getManagerPorNome(String nickname) {
        return managers.stream().filter(manager -> manager.getNickname().equals(nickname)).findFirst().orElse(null);
    }

    public void setAtivado(Boolean ativado) {
        this.ativado = ativado;
    }

    public void addQuantidadesStackadas(BigDecimal quantidade) {
        this.quantidadeStackada = this.quantidadeStackada.add(quantidade);

        DHAPI.setHologramLine(this.holograma, 3, String.format("§fQuantia: §e%s", new ConverterQuantia(this.getQuantidadeStackada()).emLetras()));
        save("quantiaStackada", quantidade.toString());
    }

    public void setarHolograma() {
        this.holograma = DHAPI.getHologram(this.getId());

        // Deleta o hologram caso ele já exista
        if(this.holograma != null) {
            this.holograma.delete();
        }

        Location hologramaLocal = this.local.clone();
        this.holograma = DHAPI.createHologram(this.getId(), hologramaLocal.add(-0.5, 3, 0.5), true);

        // Pegar head da entidade
        ItemStack spawnerItem = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta spawnerHeadMeta = (SkullMeta) spawnerItem.getItemMeta();
        // Remove o 'org.bukkit.entity' do nome da entidade
        String nomeDaEntidade = this.spawnerMeta.getMob().getEntityClass().getName().split("\\.")[3];
        spawnerHeadMeta.setOwner("MHF_" + nomeDaEntidade);

        DHAPI.addHologramLine(this.holograma, this.spawnerMeta.getTitle());
        DHAPI.addHologramLine(this.holograma, spawnerItem);
        DHAPI.addHologramLine(this.holograma, String.format("§fDono: §e%s", this.getDono().getNickname()));
        DHAPI.addHologramLine(this.holograma, String.format("§fQuantia: §e%s", new ConverterQuantia(this.getQuantidadeStackada()).emLetras()));
        if(this.ativado) {
            DHAPI.addHologramLine(this.holograma, "§fStatus: §aON");
        } else {
            DHAPI.addHologramLine(this.holograma, "§fStatus: §cOFF");
        }
    }

    public void startSpawnerLoop() {
        if(!this.ativado) return;
        BukkitScheduler scheduler = Bukkit.getScheduler();

        List<Entity> mobsPorPerto = new ArrayList<>(Bukkit.getWorld(this.getLocal().getWorld().getName()).getNearbyEntities(this.getLocal(), 2, 2, 2));
        AtomicReference<Boolean> mobSetado = new AtomicReference<>(false);
        mobsPorPerto.forEach((mob) -> {
            if(mob.getType() == this.getSpawnerMeta().getMob() && !mobSetado.get()) {
                mob.setCustomName("§e" + new ConverterQuantia(this.entidadesSpawnadas).emLetras());
                mob.setCustomNameVisible(true);
                mobSetado.set(true);
            }
        });

        if(!mobSetado.get()) {
            Entity novoMob = Bukkit.getWorld(this.getLocal().getWorld().getName()).spawnEntity(this.getLocal().clone().add(1, 0, 1), this.getSpawnerMeta().getMob());
            novoMob.setCustomName("§e" + new ConverterQuantia(this.entidadesSpawnadas).emLetras());
            novoMob.setCustomNameVisible(true);
            NBTEditor.set(novoMob, true, "NoAI");
            NBTEditor.set(novoMob, true, "Invunerable");
        }
        this.entidadesSpawnadas = this.entidadesSpawnadas.add(this.quantidadeStackada);
        scheduler.runTaskLater(SwedenSpawners.getPlugin(SwedenSpawners.class), this::startSpawnerLoop, 20L * 5L);
    }

    public void matarEntidades() {
        this.dropsAramazenados = this.dropsAramazenados.add(this.entidadesSpawnadas);
        this.entidadesSpawnadas = new BigDecimal("0");
    }

    public void iniciar() {
        this.ativado = true;
        this.setarHolograma();
        this.startSpawnerLoop();
    }
}
