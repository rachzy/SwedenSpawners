package com.redesweden.swedenspawners.models;

public class SpawnerAmigo {
    private String uuid;
    private String nickname;
    private Boolean permissaoVender;
    private Boolean permissaoMatar;
    private Boolean permissaoRetirar;

    public SpawnerAmigo(String uuid, String nickname, Boolean permissaoVender, Boolean permissaoMatar, Boolean permissaoRetirar) {
        this.uuid = uuid;
        this.nickname = nickname;
        this.permissaoVender = permissaoVender;
        this.permissaoMatar = permissaoMatar;
        this.permissaoRetirar = permissaoRetirar;
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

    public Boolean getPermissaoRetirar() {
        return permissaoRetirar;
    }

    public void setPermissaoVender(Boolean permissaoVender) {
        this.permissaoVender = permissaoVender;
    }

    public void setPermissaoMatar(Boolean permissaoMatar) {
        this.permissaoMatar = permissaoMatar;
    }

    public void setPermissaoRetirar(Boolean permissaoRetirar) {
        this.permissaoRetirar = permissaoRetirar;
    }

    public void togglePermissaoVender(Spawner spawner) {
        this.setPermissaoVender(!this.permissaoVender);
        spawner.save(String.format("amigos.%s.permissaoVender", this.uuid), this.permissaoVender);
    }

    public void togglePermissaoMatar(Spawner spawner) {
        this.setPermissaoMatar(!this.permissaoMatar);
        spawner.save(String.format("amigos.%s.permissaoMatar", this.uuid), this.permissaoMatar);
    }

    public void togglePermissaoRetirar(Spawner spawner) {
        this.setPermissaoRetirar(!this.permissaoRetirar);
        spawner.save(String.format("amigos.%s.permissaoRetirar", this.uuid), this.permissaoRetirar);
    }
}
