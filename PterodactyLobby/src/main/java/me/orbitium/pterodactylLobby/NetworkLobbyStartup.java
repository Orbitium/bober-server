package me.orbitium.pterodactylLobby;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import me.orbitium.pterodactylLobby.manager.ServerStatusCache;
import me.orbitium.pterodactylManager.PterodactylManager;
import me.orbitium.pterodactylManager.api.GameServer;
import me.orbitium.pterodactylManager.utils.RequestResult;
import me.orbitium.pterodactylManager.utils.ServerStatus;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class NetworkLobbyStartup {

    public static final int MAX_RESPONSE_DELAY = 10 * 20;
    private Map<String, ScheduledTask> waitingResponse = new HashMap<>();

    public void fetchServerStatus() {
        for (String serverID : PterodactylLobby.instance.getConfig().getStringList("controlled-servers")) {
            RequestResult requestResult = PterodactylManager.instance.commandSender.requestStatusInfo(serverID);
            if (requestResult == RequestResult.OK)
                scheduleRefreshServer(serverID);
            else
                refreshServer(serverID);
        }
    }

    public void onGameStatusReceived(GameServer gameServer) {
        if (!waitingResponse.containsKey(gameServer.getId()))
            return;

        waitingResponse.get(gameServer.getId()).cancel();
        waitingResponse.remove(gameServer.getId());

        ServerStatus serverStatus = gameServer.getStatus();
        boolean shouldRefreshServer = serverStatus != ServerStatus.IN_GAME && serverStatus != ServerStatus.IDLE;
        // If server shouldn't restart, basically don't do anything
        if (shouldRefreshServer)
            refreshServer(gameServer.getId());
    }

    private void scheduleRefreshServer(String serverID) {
        ScheduledTask task = Bukkit.getGlobalRegionScheduler().runDelayed(PterodactylLobby.instance, (t) -> {
            refreshServer(serverID);
        }, MAX_RESPONSE_DELAY);

        waitingResponse.put(serverID, task);
    }

    private void refreshServer(String serverID) {
        waitingResponse.remove(serverID);

        GameServer gameServer = new GameServer(serverID, ServerStatus.LOADING);
        ServerStatusCache.onServerStatusUpdate(gameServer);
        GameNetworkManager.refreshServer(gameServer);
    }

}
