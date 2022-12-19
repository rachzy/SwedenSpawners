package com.redesweden.swedenspawners.models;

public class EventoGerenciarSpawner {
    private String nick;
    private Spawner spawner;

    public EventoGerenciarSpawner(String nick, Spawner spawner) {
        this.nick = nick;
        this.spawner = spawner;
    }

    public String getNick() {
        return nick;
    }

    public Spawner getSpawner() {
        return spawner;
    }
}
