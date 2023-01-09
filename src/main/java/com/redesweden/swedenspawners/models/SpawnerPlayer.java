package com.redesweden.swedenspawners.models;

import com.redesweden.swedenspawners.data.Players;
import com.redesweden.swedenspawners.files.PlayersFile;

import java.math.BigDecimal;

public class SpawnerPlayer {
    private final String uuid;
    private final String nickname;
    private BigDecimal limite;
    private BigDecimal spawnersComprados;
    private Integer multiplicador;

    public SpawnerPlayer(String uuid, String nickname, BigDecimal limite, BigDecimal spawnersComprados, Integer multiplicador) {
        this.uuid = uuid;
        this.nickname = nickname;
        this.limite = limite;
        this.spawnersComprados = spawnersComprados;
        this.multiplicador = multiplicador;
    }

    public void save() {
        PlayersFile.get().set(String.format("players.%s.limite", this.uuid), limite.toString());
        PlayersFile.get().set(String.format("players.%s.spawnersComprados", this.uuid), spawnersComprados.toString());
        PlayersFile.get().set(String.format("players.%s.multiplicador", this.uuid), multiplicador);
        PlayersFile.save();
    }

    public String getUuid() {
        return uuid;
    }

    public String getNickname() {
        return nickname;
    }

    public BigDecimal getLimite() {
        return limite;
    }

    public BigDecimal getSpawnersComprados() {
        return spawnersComprados;
    }

    public Integer getMultiplicador() {
        return multiplicador;
    }

    public void addLimite(BigDecimal quantia) {
        this.setLimite(this.limite.add(quantia));
    }

    public void setLimite(BigDecimal quantia) {
        this.limite = quantia;
        Players.addPlayerModificado(this);
    }

    public void subLimite(BigDecimal quantia) {
        this.setLimite(this.limite.subtract(quantia));
    }

    public void addSpawnersComprados(BigDecimal quantia) {
        this.spawnersComprados = this.spawnersComprados.add(quantia);
        Players.addPlayerModificado(this);
    }

    public void setMultiplicador(Integer multiplicador) {
        this.multiplicador = multiplicador;
        Players.addPlayerModificado(this);
    }
}
