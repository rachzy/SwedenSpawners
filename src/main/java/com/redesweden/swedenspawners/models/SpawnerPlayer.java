package com.redesweden.swedenspawners.models;

import java.math.BigDecimal;

public class SpawnerPlayer {
    private String uuid;
    private String nickname;
    private BigDecimal limite;
    private BigDecimal spawnersComprados;

    public SpawnerPlayer(String uuid, String nickname, BigDecimal limite, BigDecimal spawnersComprados) {
        this.uuid = uuid;
        this.nickname = nickname;
        this.limite = limite;
        this.spawnersComprados = spawnersComprados;
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

    public void addLimite(BigDecimal quantia) {
        this.limite = this.limite.add(quantia);
    }
}
