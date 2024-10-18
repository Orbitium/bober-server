package me.orbitium.oFBoberBattlesLobby;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import me.orbitium.oFBoberBattlesLobby.queue.QueueManager;
import me.orbitium.oFBoberBattlesLobby.utils.MessageManager;
import me.orbitium.pterodactylLobby.manager.ServerStatusCache;
import me.orbitium.pterodactylManager.PterodactylManager;
import me.orbitium.pterodactylManager.utils.ServerStatus;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class GameStartManager {
    private static ScheduledTask currentTask;

    public static void schedulePreGameStart() {
        if (currentTask != null && !currentTask.isCancelled())
            return;

        currentTask = Bukkit.getGlobalRegionScheduler().runAtFixedRate(OFBoberBattlesLobby.instance, new Consumer<>() {
            int cooldown = OFBoberBattlesLobby.getInt("game-start-countdown");
            String availableServer = getAvailableServer();

            @Override
            public void accept(ScheduledTask scheduledTask) {
                int playersInQueue = QueueManager.playersInQueue.size();

                if (playersInQueue == 0)
                    scheduledTask.cancel();

                if (availableServer == null) {
                    MessageManager.sendNoAvailableServers(QueueManager.playersInQueue);
                    availableServer = getAvailableServer();
                    return;
                }

                if (playersInQueue >= QueueManager.minRequiredPlayers) {
                    cooldown--;

                    if (cooldown == 30)
                        MessageManager.sendGameTimerUpdate(cooldown, false, QueueManager.playersInQueue);
                    if (cooldown >= 0 && cooldown <= 5)
                        MessageManager.sendGameTimerUpdate(cooldown, false, QueueManager.playersInQueue);

                    MessageManager.sendGameTimerUpdate(cooldown, true, QueueManager.playersInQueue);

                    if (cooldown == 0) {
                        startGame(availableServer);
                        scheduledTask.cancel();
                    }
                } else {
                    cooldown = OFBoberBattlesLobby.getInt("game-start-countdown");
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers())
                        OFBoberBattlesLobby.sendHotBarMessage(onlinePlayer, "more-players-required", "%current-players%", playersInQueue + "");
                }
            }
        }, 20L, 20L);
    }

    public static void startGame(String availableServer) {
        List<String> playerNames = new ArrayList<>();
        for (Player player : QueueManager.playersInQueue)
            playerNames.add(player.getName());

        String mappedServerName = OFBoberBattlesLobby.serverIDMap.get(availableServer);
        PterodactylManager.instance.commandSender.sendStartGame(availableServer, playerNames);
        Bukkit.getGlobalRegionScheduler().runDelayed(OFBoberBattlesLobby.instance, (t) -> {
            for (Player player : QueueManager.playersInQueue)
                OFBoberBattlesLobby.sendPlayerToOtherServer(player, mappedServerName);

            QueueManager.clearQueue();
        }, 20L);
    }

    private static String getAvailableServer() {
        String idleServerID = null;
        for (Map.Entry<String, ServerStatus> entry : ServerStatusCache.cachedServers.entrySet())
            if (entry.getValue() == ServerStatus.IDLE)
                idleServerID = entry.getKey();

        return idleServerID;
    }

}
