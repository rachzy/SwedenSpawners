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
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
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
    private final SpawnerPlayer dono;
    private final SpawnerMeta spawnerMeta;
    private final Location local;
    private BigDecimal quantidadeStackada;
    private BigDecimal entidadesSpawnadas;
    private BigDecimal dropsAramazenados;
    private List<SpawnerAmigo> amigos;
    private Boolean ativado;

    public Spawner(String id, SpawnerPlayer dono, SpawnerMeta spawnerMeta, Location local, BigDecimal quantidadeStackada, BigDecimal entidadesSpawnadas, BigDecimal dropsAramazenados, List<SpawnerAmigo> amigos, Boolean ativado) {
        this.id = id;
        this.dono = dono;
        this.spawnerMeta = spawnerMeta;
        this.local = local;
        this.quantidadeStackada = quantidadeStackada;
        this.entidadesSpawnadas = entidadesSpawnadas;
        this.dropsAramazenados = dropsAramazenados;
        this.amigos = amigos;
        this.ativado = ativado;

        this.iniciar();
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

    public List<SpawnerAmigo> getAmigos() {
        return amigos;
    }

    public SpawnerAmigo getAmigoPorNome(String nickname) {
        return amigos.stream().filter(amigo -> amigo.getNickname().equalsIgnoreCase(nickname)).findFirst().orElse(null);
    }

    public Boolean getAtivado() {
        return ativado;
    }

    public void toggleAtivado() {
        this.setAtivado(!this.ativado);
    }

    public void addAmigo(String uuid, String nickname) throws Exception {
        if(this.getAmigoPorNome(nickname) != null) throw new Exception("§cEste jogador já é um amigo desse spawn.");
        this.amigos.add(new SpawnerAmigo(uuid, nickname, false, false, false));
        save(String.format("amigos.%s.nickname", uuid), nickname);
        save(String.format("amigos.%s.permissaoVender", uuid), false);
        save(String.format("amigos.%s.permissaoMatar", uuid), false);
        save(String.format("amigos.%s.permissaoQuebrar", uuid), false);
    }

    public void removerAmigo(SpawnerAmigo amigo) {
        this.amigos = this.amigos.stream().filter(amigoIn -> !amigoIn.getNickname().equals(amigo.getNickname())).collect(Collectors.toList());
        save(String.format("amigos.%s", amigo.getUuid()), null);
    }

    public void zerarDropsArmazenados() {
        this.dropsAramazenados = new BigDecimal("0");
        save("dropsArmazenados", "0");
    }

    public void setAtivado(Boolean ativado) {
        this.ativado = ativado;
        if(this.ativado) {
            DHAPI.setHologramLine(this.holograma, 4, "§fStatus: §aON");
        } else {
            DHAPI.setHologramLine(this.holograma, 4, "§fStatus: §cOFF");
        }
        save("ativado", ativado);
    }

    public void addQuantidadesStackadas(BigDecimal quantidade) {
        this.quantidadeStackada = this.quantidadeStackada.add(quantidade);

        DHAPI.setHologramLine(this.holograma, 3, String.format("§fQuantia: §e%s", new ConverterQuantia(this.getQuantidadeStackada()).emLetras()));
        save("quantidadeStackada", this.quantidadeStackada.toString());
    }

    public void setarHolograma() {
        try {
            this.holograma = DHAPI.getHologram(this.getId());

            // Deleta o hologram caso ele já exista
            if (this.holograma != null) {
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
            if (this.ativado) {
                DHAPI.addHologramLine(this.holograma, "§fStatus: §aON");
            } else {
                DHAPI.addHologramLine(this.holograma, "§fStatus: §cOFF");
            }
        } catch (Exception e) {
            //
        }
    }

    public void spawnarMob() {
        if (!this.ativado) return;

        if(Objects.equals(this.entidadesSpawnadas, new BigDecimal("0"))) {
            this.entidadesSpawnadas = new BigDecimal("1").multiply(this.quantidadeStackada);
        }

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
            Entity novoMob = Bukkit.getWorld(this.getLocal().getWorld().getName()).spawnEntity(this.getLocal().clone().add(1, 0, 1), this.getSpawnerMeta().getMob());
            novoMob.setCustomName("§e" + new ConverterQuantia(this.entidadesSpawnadas).emLetras());
            novoMob.setCustomNameVisible(true);
            NBTEditor.set(novoMob, true, "NoAI");
            NBTEditor.set(novoMob, true, "Invunerable");
        }
        this.entidadesSpawnadas = this.entidadesSpawnadas.add(this.quantidadeStackada);
        save("entidadesSpawnadas", this.entidadesSpawnadas.toString());
    }

    public void startSpawnerLoop() {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        this.spawnarMob();
        scheduler.runTaskLater(SwedenSpawners.getPlugin(SwedenSpawners.class), this::startSpawnerLoop, 20L * 5L);
    }

    public void matarEntidades() {
        this.dropsAramazenados = this.dropsAramazenados.add(this.entidadesSpawnadas);
        this.entidadesSpawnadas = new BigDecimal("0");
    }

    public void iniciar() {
        this.setarHolograma();
        this.startSpawnerLoop();
    }
}
