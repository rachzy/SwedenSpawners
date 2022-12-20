package com.redesweden.swedenspawners.models;

public class SpawnerAmigo {
    private String uuid;
    private String nickname;
    private Boolean permissaoVender;
    private Boolean permissaoMatar;
    private Boolean permissaoQuebrar;

    public SpawnerAmigo(String uuid, String nickname, Boolean permissaoVender, Boolean permissaoMatar, Boolean permissaoQuebrar) {
        this.uuid = uuid;
        this.nickname = nickname;
        this.permissaoVender = permissaoVender;
        this.permissaoMatar = permissaoMatar;
        this.permissaoQuebrar = permissaoQuebrar;
    }

    public String getUuid() {
        return uuid;
    }

    public String getNickname() {
        return nickname;
    }

    public Boolean getPermissaoVender() {
        return permissaoVender;
    }

    public Boolean getPermissaoMatar() {
        return permissaoMatar;
    }

    public Boolean getPermissaoQuebrar() {
        return permissaoQuebrar;
    }

    public void setPermissaoVender(Boolean permissaoVender) {
        this.permissaoVender = permissaoVender;
    }

    public void setPermissaoMatar(Boolean permissaoMatar) {
        this.permissaoMatar = permissaoMatar;
    }

    public void setPermissaoQuebrar(Boolean permissaoQuebrar) {
        this.permissaoQuebrar = permissaoQuebrar;
    }

    public void togglePermissaoVender(Spawner spawner) {
        this.setPermissaoVender(!this.permissaoVender);
        spawner.save(String.format("amigos.%s.permissaoVender", this.uuid), this.permissaoVender);
    }

    public void togglePermissaoMatar(Spawner spawner) {
        this.setPermissaoMatar(!this.permissaoMatar);
        spawner.save(String.format("amigos.%s.permissaoMatar", this.uuid), this.permissaoMatar);
    }

    public void togglePermissaoQuebrar(Spawner spawner) {
        this.setPermissaoQuebrar(!this.permissaoQuebrar);
        spawner.save(String.format("amigos.%s.permissaoQuebrar", this.uuid), this.permissaoQuebrar);
    }
}
