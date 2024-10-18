package me.orbitium.oFBoberBattlesLobby.queue;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import me.orbitium.oFBoberBattlesLobby.GameStartManager;
import me.orbitium.oFBoberBattlesLobby.OFBoberBattlesLobby;
import me.orbitium.oFBoberBattlesLobby.utils.MessageManager;
import me.orbitium.pterodactylLobby.manager.ServerStatusCache;
import me.orbitium.pterodactylManager.utils.ServerStatus;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class QueueManager {
    public static final int minRequiredPlayers = OFBoberBattlesLobby.getInt("minimum-players-required-to-start-a-battle");
    public static final List<Player> playersInQueue = new ArrayList<>();

    public static void onPlayerAddedQueue(Player player) {
        if (playersInQueue.contains(player)) {
            OFBoberBattlesLobby.sendMessage(player, "already-in-queue");
            return;
        }

        playersInQueue.add(player);
        OFBoberBattlesLobby.sendMessage(player, "player-queued");
        checkGameStart();
    }

    public static void onPlayerRemovedFromQueue(Player player) {
        if (!playersInQueue.contains(player)) {
            OFBoberBattlesLobby.sendMessage(player, "not-in-queue");
            return;
        }

        playersInQueue.remove(player);
        OFBoberBattlesLobby.sendMessage(player, "player-dequeued");
//        checkGameStart();
    }

    public static void clearQueue() {
        playersInQueue.clear();
    }

    private static void checkGameStart() {
//        String idleServerID = null;
//        for (Map.Entry<String, ServerStatus> entry : ServerStatusCache.cachedServers.entrySet())
//            if (entry.getValue() == ServerStatus.IDLE)
//                idleServerID = entry.getKey();
//
//        if (idleServerID == null) {
//            MessageManager.sendNoAvailableServers(playersInQueue);
//            return;
//        }

//        int totalPlayersInQueue = playersInQueue.size();
//        if (totalPlayersInQueue >= minRequiredPlayers)
        GameStartManager.schedulePreGameStart();
    }


}
