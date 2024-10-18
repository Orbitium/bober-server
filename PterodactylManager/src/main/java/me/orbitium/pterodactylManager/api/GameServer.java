package me.orbitium.pterodactylManager.api;

import me.orbitium.pterodactylManager.utils.ServerStatus;

public class GameServer {
    private final String id;
    private ServerStatus status;

    public GameServer(String id, ServerStatus status) {
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public ServerStatus getStatus() {
        return status;
    }

    public void setStatus(ServerStatus status) {
        this.status = status;
    }
}
