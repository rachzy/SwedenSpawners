package com.redesweden.swedenspawners.models;

public class SpawnerManager {
    private String nickname;
    private Boolean permissaoVender;
    private Boolean permissaoMatar;
    private Boolean permissaoQuebrar;

    public SpawnerManager(String nickname, Boolean permissaoVender, Boolean permissaoMatar, Boolean permissaoQuebrar) {
        this.nickname = nickname;
        this.permissaoVender = permissaoVender;
        this.permissaoMatar = permissaoMatar;
        this.permissaoQuebrar = permissaoQuebrar;
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
}
