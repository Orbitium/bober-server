package me.orbitium.pterodactylLobby;

import me.orbitium.pterodactylLobby.manager.ServerStatusCache;
import me.orbitium.pterodactylManager.PterodactylManager;
import me.orbitium.pterodactylManager.api.GameServer;
import me.orbitium.pterodactylManager.api.PterodactylFileManager;
import me.orbitium.pterodactylManager.utils.RequestResult;
import me.orbitium.pterodactylManager.utils.ServerStatus;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameNetworkManager {

    public static Map<String, List<Integer>> gameServerTasks = new HashMap<>();

    public static void refreshServer(GameServer gameServer) {
        try {
            PterodactylFileManager networkFileManager = new PterodactylFileManager(PterodactylManager.instance.getApiKey(), gameServer.getId());
            PterodactylLobby.prtInfo("Refreshing " + gameServer.getId() + " server");
            scheduleServerClose(networkFileManager);
        } catch (Exception e) {
            PterodactylLobby.prtNetworkError(gameServer.getId() + " can't close the server!");
            e.printStackTrace();
        }
    }

    private static void scheduleServerClose(PterodactylFileManager networkFileManager) {
        PterodactylLobby.prtInfo("Closing " + networkFileManager.getTargetServerID() + " server");

        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskLaterAsynchronously(PterodactylLobby.instance, () -> {
            try {
                RequestResult statusCode = networkFileManager.closeServer();
                if (statusCode != RequestResult.OK)
                    scheduleServerClose(networkFileManager);
                else
                    scheduleFileDeletion(networkFileManager);
            } catch (Exception e) {
                e.printStackTrace();
                PterodactylLobby.prtNetworkError(networkFileManager.getTargetServerID() + " can't close the server!");
                scheduleServerClose(networkFileManager);
            }
        }, 20 * 5);

        addServerTask(networkFileManager.getTargetServerID(), bukkitTask);
    }

    private static void scheduleFileDeletion(PterodactylFileManager networkFileManager) {
        PterodactylLobby.prtInfo("Deleting the world file of " + networkFileManager.getTargetServerID() + " server");

        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskLaterAsynchronously(PterodactylLobby.instance, () -> {
            try {
                RequestResult statusCode = networkFileManager.deleteFile("world");
                if (statusCode != RequestResult.OK)
                    scheduleFileDeletion(networkFileManager);
                else {
                    scheduleFileUnpacking(networkFileManager);
                    ServerStatusCache.setServerStatusByID(networkFileManager.getTargetServerID(), ServerStatus.LOADING);
                }
            } catch (Exception e) {
                e.printStackTrace();
                PterodactylLobby.prtNetworkError(networkFileManager.getTargetServerID() + " can't delete the world folder!");
                scheduleFileDeletion(networkFileManager);
            }
        }, 20 * 10);

        addServerTask(networkFileManager.getTargetServerID(), bukkitTask);
    }

    private static void scheduleFileUnpacking(PterodactylFileManager networkFileManager) {
        PterodactylLobby.prtInfo("Unpacking a random pre-generated world file in " + networkFileManager.getTargetServerID() + " server");

        List<String> compressedWorldFiles = PterodactylLobby.instance.getConfig().getStringList("compressed-world-file-names");
        String selectedWorldFile = compressedWorldFiles.get(PterodactylLobby.random.nextInt(compressedWorldFiles.size()));
        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskLaterAsynchronously(PterodactylLobby.instance, () -> {
            try {
                RequestResult statusCode = networkFileManager.uncompressFile(selectedWorldFile);
                if (statusCode != RequestResult.OK)
                    scheduleFileUnpacking(networkFileManager);
                else
                    scheduleOpenServer(networkFileManager);
            } catch (Exception e) {
                e.printStackTrace();
                PterodactylLobby.prtNetworkError(networkFileManager.getTargetServerID() + " can't uncompress " + selectedWorldFile + " file!");
                scheduleFileUnpacking(networkFileManager);
            }
        }, 20 * 10L);

        addServerTask(networkFileManager.getTargetServerID(), bukkitTask);
    }

    private static void scheduleOpenServer(PterodactylFileManager networkFileManager) {
        PterodactylLobby.prtInfo("Opening " + networkFileManager.getTargetServerID() + " server");

        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskLaterAsynchronously(PterodactylLobby.instance, () -> {
            try {
                RequestResult statusCode = networkFileManager.openServer();
                if (statusCode != RequestResult.OK)
                    scheduleOpenServer(networkFileManager);
                else
                    cleanServerTasks(networkFileManager.getTargetServerID());
            } catch (Exception e) {
                e.printStackTrace();
                PterodactylLobby.prtNetworkError(networkFileManager.getTargetServerID() + " can't open the server!");

                scheduleOpenServer(networkFileManager);
            }
        }, 20 * 10L);

        addServerTask(networkFileManager.getTargetServerID(), bukkitTask);
    }

    private static void addServerTask(String server, BukkitTask task) {
        List<Integer> createdTasks = gameServerTasks.getOrDefault(server, new ArrayList<>());
        createdTasks.add(task.getTaskId());
        gameServerTasks.put(server, createdTasks);
    }

    private static void cleanServerTasks(String server) {
        // ! MAYBE cancel tasks but this technically can cancel wrongs tasks if ids are replaced by another task idk
        gameServerTasks.remove(server);
    }
}


/*
public enum ServerStatus {
    LOADING,
    IDLE, -> GameServer Sends
    IN_GAME, -> GameServer Sends
    FINISHED -> GameServer Sends
}

 */