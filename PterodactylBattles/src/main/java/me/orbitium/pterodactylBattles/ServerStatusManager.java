package me.orbitium.pterodactylBattles;

import me.orbitium.pterodactylManager.PterodactylManager;
import me.orbitium.pterodactylManager.api.PterodactylFileManager;
import me.orbitium.pterodactylManager.utils.ServerStatus;

public class ServerStatusManager {
    private ServerStatus status;
    PterodactylFileManager networkFileManager = PterodactylManager.instance.getNetworkFileManager();

    public ServerStatusManager(ServerStatus status) {
        this.status = status;
    }

    public void updateStatus(ServerStatus newStatus) {
        status = newStatus;

//        networkFileManager
    }

    public ServerStatus getStatus() {
        return status;
    }
}
