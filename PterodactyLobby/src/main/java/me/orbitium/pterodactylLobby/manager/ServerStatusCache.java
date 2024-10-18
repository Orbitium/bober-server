package me.orbitium.pterodactylLobby.manager;

import me.orbitium.pterodactylManager.api.GameServer;
import me.orbitium.pterodactylManager.utils.ServerStatus;

import java.util.HashMap;
import java.util.Map;

public class ServerStatusCache {
    public static Map<String, ServerStatus> cachedServers = new HashMap<>();

    public static void onServerStatusUpdate(GameServer gameServer) {
        ServerStatus status = gameServer.getStatus();
        cachedServers.entrySet().removeIf(entry -> entry.getKey().equals(gameServer.getId()));
        cachedServers.put(gameServer.getId(), status);
    }

    public static void setServerStatusByID(String id, ServerStatus newStatus) {
        if (!cachedServers.containsKey(id))
            throw new IllegalArgumentException("Server id " + id + " is not cached! Cannot update the server status!");

        cachedServers.put(id, newStatus);
    }
}
