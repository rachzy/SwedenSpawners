package com.redesweden.swedenspawners.models;

public class SpawnerAmigo {
    private final String uuid;
    private final String nickname;
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

    public void togglePermissaoVender() {
        this.setPermissaoVender(!this.permissaoVender);
    }

    public void togglePermissaoMatar() {
        this.setPermissaoMatar(!this.permissaoMatar);
    }

    public void togglePermissaoRetirar() {
        this.setPermissaoRetirar(!this.permissaoRetirar);
    }
}
