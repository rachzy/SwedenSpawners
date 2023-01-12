package com.redesweden.swedenspawners.models;

import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedenspawners.SwedenSpawners;
import com.redesweden.swedenspawners.data.Spawners;
import com.redesweden.swedenspawners.files.SpawnersFile;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class Spawner {
    private final String id;
    private Hologram holograma;
    private SpawnerPlayer dono;
    private final SpawnerMeta spawnerMeta;
    private Location local;
    private int levelTempoDeSpawn;
    private int levelValorDoDrop;
    private int levelMultiplicadorDeSpawn;
    private BigDecimal quantidadeStackada;
    private BigDecimal entidadesSpawnadas;
    private BigDecimal dropsAramazenados;
    private List<SpawnerAmigo> amigos;
    private Boolean ativado;
    private Boolean retirado;

    public Spawner(String id, SpawnerPlayer dono, SpawnerMeta spawnerMeta, Location local, int levelTempoDeSpawn, int levelValorDoDrop, int levelMultiplicadorDeSpawn, BigDecimal quantidadeStackada, BigDecimal entidadesSpawnadas, BigDecimal dropsAramazenados, List<SpawnerAmigo> amigos, Boolean ativado, Boolean retirado) {
        this.id = id;
        this.dono = dono;
        this.spawnerMeta = spawnerMeta;
        this.local = local;
        this.levelTempoDeSpawn = levelTempoDeSpawn;
        this.levelValorDoDrop = levelValorDoDrop;
        this.levelMultiplicadorDeSpawn = levelMultiplicadorDeSpawn;
        this.quantidadeStackada = quantidadeStackada;
        this.entidadesSpawnadas = entidadesSpawnadas;
        this.dropsAramazenados = dropsAramazenados;
        this.amigos = amigos;
        this.ativado = ativado;
        this.retirado = retirado;

        this.iniciar();
    }

    public void salvarDados() {
        SpawnersFile.get().set(String.format("spawners.%s.local.x", this.id), local.getX());
        SpawnersFile.get().set(String.format("spawners.%s.local.y", this.id), local.getY());
        SpawnersFile.get().set(String.format("spawners.%s.local.z", this.id), local.getZ());
        SpawnersFile.get().set(String.format("spawners.%s.melhorias.tempoDeSpawn", this.id), this.levelTempoDeSpawn);
        SpawnersFile.get().set(String.format("spawners.%s.melhorias.valorDoDrop", this.id), this.levelValorDoDrop);
        SpawnersFile.get().set(String.format("spawners.%s.melhorias.multiplicadorDeSpawn", this.id), this.levelMultiplicadorDeSpawn);
        SpawnersFile.get().set(String.format("spawners.%s.quantidadeStackada", this.id), this.quantidadeStackada.toString());
        SpawnersFile.get().set(String.format("spawners.%s.dropsArmazenados", this.id), this.dropsAramazenados.toString());
        SpawnersFile.get().set(String.format("spawners.%s.entidadesSpawnadas", this.id), this.entidadesSpawnadas.toString());

        amigos.forEach((amigo) -> {
            String uuid = amigo.getUuid();
            SpawnersFile.get().set(String.format("spawners.%s.amigos.%s.nickname", this.id, uuid), amigo.getNickname());
            SpawnersFile.get().set(String.format("spawners.%s.amigos.%s.permissaoVender", this.id, uuid), amigo.getPermissaoVender());
            SpawnersFile.get().set(String.format("spawners.%s.amigos.%s.permissaoMatar", this.id, uuid), amigo.getPermissaoMatar());
            SpawnersFile.get().set(String.format("spawners.%s.amigos.%s.permissaoRetirar", this.id, uuid), amigo.getPermissaoRetirar());
        });

        SpawnersFile.get().set(String.format("spawners.%s.ativado", this.id), ativado);
        SpawnersFile.get().set(String.format("spawners.%s.retirado", this.id), retirado);

        SpawnersFile.save();
    }

    public String getId() {
        return id;
    }

    public SpawnerPlayer getDono() {
        return dono;
    }

    public void setDono(SpawnerPlayer dono) {
        this.dono = dono;
    }

    public SpawnerMeta getSpawnerMeta() {
        return spawnerMeta;
    }

    public Location getLocal() {
        return local;
    }

    public void setLocal(Location local) {
        this.local = local;
    }

    public int getLevelTempoDeSpawn() {
        return levelTempoDeSpawn;
    }

    public void addLevelTempoDeSpawn() {
        this.levelTempoDeSpawn += 1;
    }

    public int getLevelValorDoDrop() {
        return levelValorDoDrop;
    }

    public void addLevelValorDoDrop() {
        this.levelValorDoDrop += 1;
    }

    public int getLevelMultiplicadorDeSpawn() {
        return levelMultiplicadorDeSpawn;
    }

    public void addLevelMultiplicadorDeSpawn() {
        this.levelMultiplicadorDeSpawn += 1;
    }

    public BigDecimal getQuantidadeStackada() {
        return quantidadeStackada;
    }

    public void setQuantidadeStackada(BigDecimal quantidade) {
        this.quantidadeStackada = quantidade;
        DHAPI.setHologramLine(this.holograma, 3, String.format("§fQuantia: §e%s", new ConverterQuantia(this.getQuantidadeStackada()).emLetras()));
    }

    public void addQuantidadesStackadas(BigDecimal quantidade) {
        setQuantidadeStackada(this.quantidadeStackada.add(quantidade));
    }

    public void subQuantidadesStackadas(BigDecimal quantidade) {
        setQuantidadeStackada(this.quantidadeStackada.subtract(quantidade));
    }

    public BigDecimal getEntidadesSpawnadas() {
        return entidadesSpawnadas;
    }

    public BigDecimal getDropsAramazenados() {
        return dropsAramazenados;
    }

    public List<SpawnerAmigo> getAmigos() {
        return amigos;
    }

    public SpawnerAmigo getAmigoPorNome(String nickname) {
        return amigos.stream().filter(amigo -> amigo.getNickname().equalsIgnoreCase(nickname)).findFirst().orElse(null);
    }

    public void addAmigo(String uuid, String nickname) throws Exception {
        if(this.getAmigoPorNome(nickname) != null) throw new Exception("§cEste jogador já é um amigo desse spawn.");
        this.amigos.add(new SpawnerAmigo(uuid, nickname, false, false, false));
    }

    public void removerAmigo(SpawnerAmigo amigo) {
        this.amigos = this.amigos.stream().filter(amigoIn -> !amigoIn.getNickname().equals(amigo.getNickname())).collect(Collectors.toList());
    }

    public Boolean getAtivado() {
        return ativado;
    }

    public void toggleAtivado() {
        this.setAtivado(!this.ativado);
    }

    public void setAtivado(Boolean ativado) {
        this.ativado = ativado;
        if(this.ativado) {
            DHAPI.setHologramLine(this.holograma, 4, "§fStatus: §aON");
        } else {
            DHAPI.setHologramLine(this.holograma, 4, "§fStatus: §cOFF");
        }

        Spawners.getSpawnersModificados().put(this, this);
    }

    public Boolean getRetirado() {
        return retirado;
    }

    public void setRetirado(Boolean retirado) {
        this.retirado = retirado;
        
        if(retirado) {
            try {
                holograma.delete();
            } catch (Exception e) {
                holograma.destroy();
            }
        }
    }

    public void zerarDropsArmazenados() {
        this.dropsAramazenados = new BigDecimal("0");
    }

    public void setarHolograma() {
        try {
            this.holograma = DHAPI.getHologram(this.getId());

            // Deleta o hologram caso ele já exista
            if (this.holograma != null) {
                this.holograma.delete();
            }

            Location hologramaLocal = this.local.clone();
            this.holograma = DHAPI.createHologram(this.getId(), hologramaLocal.add(0.5, 3, 0.5), false);

            DHAPI.addHologramLine(this.holograma, this.spawnerMeta.getTitulo());
            DHAPI.addHologramLine(this.holograma, spawnerMeta.getHead());
            DHAPI.addHologramLine(this.holograma, String.format("§fDono: §e%s", this.getDono().getNickname()));
            DHAPI.addHologramLine(this.holograma, String.format("§fQuantia: §e%s", new ConverterQuantia(this.getQuantidadeStackada()).emLetras()));
            if (this.ativado) {
                DHAPI.addHologramLine(this.holograma, "§fStatus: §aON");
            } else {
                DHAPI.addHologramLine(this.holograma, "§fStatus: §cOFF");
            }
        } catch (Exception e) {
            System.out.println("Erro ao ativar o hologrgama: " + e.getMessage());
        }
    }

    public void spawnarMob() {
        // Verifica se o spawner está ativo
        if (!this.ativado) return;

        // Verifica se o dono está online no servidor
        Player dono = Bukkit.getServer().getPlayer(this.getDono().getNickname());
        if(dono == null || !dono.isOnline()) return;

        Spawners.getSpawnersModificados().put(this, this);

        this.entidadesSpawnadas = this.entidadesSpawnadas.add(this.quantidadeStackada.multiply(BigDecimal.valueOf(this.levelMultiplicadorDeSpawn)));

        List<Entity> mobsPorPerto = new ArrayList<>(Bukkit.getWorld(this.getLocal().getWorld().getName()).getNearbyEntities(this.getLocal(), 2, 2, 2));
        AtomicReference<Boolean> mobSetado = new AtomicReference<>(false);
        mobsPorPerto.forEach((mob) -> {
            if (mob.getType() == this.getSpawnerMeta().getMob() && !mobSetado.get()) {
                mob.setCustomName(String.format("§e§l%s §f- §e%s", this.getSpawnerMeta().getId(), new ConverterQuantia(this.entidadesSpawnadas).emLetras()));
                mob.setCustomNameVisible(true);
                mobSetado.set(true);
            }
        });

        if (!mobSetado.get()) {
            Entity novoMob = Bukkit.getWorld(this.getLocal().getWorld().getName()).spawnEntity(this.getLocal().clone().add(2, 0, 2), this.getSpawnerMeta().getMob());
            novoMob.setCustomName(String.format("§e§l%s §f- §e%s", this.getSpawnerMeta().getId(), new ConverterQuantia(this.entidadesSpawnadas).emLetras()));
            novoMob.setCustomNameVisible(true);
            NBTEditor.set(novoMob, true, "NoAI");
        }
    }

    public void desespawnarMob() {
        List<Entity> mobsPorPerto = new ArrayList<>(Bukkit.getWorld(this.getLocal().getWorld().getName()).getNearbyEntities(this.getLocal(), 2, 2, 2));
        mobsPorPerto.forEach((mob) -> {
            if (mob.getType() == this.getSpawnerMeta().getMob()) {
                mob.remove();
            }
        });
    }

    public void startSpawnerLoop() {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        this.spawnarMob();
        scheduler.runTaskLater(SwedenSpawners.getPlugin(SwedenSpawners.class), this::startSpawnerLoop, (20L / this.levelTempoDeSpawn) * 5L);
    }

    public void matarEntidades(int looting) {
        if(looting == 0) {
            this.dropsAramazenados = this.dropsAramazenados.add(this.entidadesSpawnadas);
        } else if(looting == 1) {
            this.dropsAramazenados = this.dropsAramazenados.add(this.entidadesSpawnadas.multiply(BigDecimal.valueOf(1.25)));
        } else {
            this.dropsAramazenados = this.dropsAramazenados.add(this.entidadesSpawnadas.multiply(BigDecimal.valueOf(looting * 0.6)));
        }
        this.entidadesSpawnadas = new BigDecimal("0");
    }

    public void iniciar() {
        if(this.retirado) return;
        this.setarHolograma();
        this.startSpawnerLoop();
    }
}
