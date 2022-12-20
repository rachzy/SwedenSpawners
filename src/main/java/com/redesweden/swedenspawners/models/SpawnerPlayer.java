package com.redesweden.swedenspawners.models;

import com.redesweden.swedenspawners.files.PlayersFile;

import java.math.BigDecimal;

public class SpawnerPlayer {
    private String uuid;
    private String nickname;
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

    public void save(String key, Object value) {
        PlayersFile.get().set(String.format("players.%s.%s", this.uuid, key), value);
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
        save("limite", this.limite.toString());
    }

    public void subLimite(BigDecimal quantia) {
        this.setLimite(this.limite.subtract(quantia));
    }

    public void addSpawnersComprados(BigDecimal quantia) {
        this.spawnersComprados = this.spawnersComprados.add(quantia);
        save("spawnersComprados", this.spawnersComprados.toString());
    }

    public void setMultiplicador(Integer multiplicador) {
        this.multiplicador = multiplicador;
        save("multiplicador", multiplicador);
    }
}
