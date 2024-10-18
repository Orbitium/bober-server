package me.orbitium.pterodactylLobby;

import me.orbitium.pterodactylLobby.commands.GameNetworkCommand;
import me.orbitium.pterodactylLobby.manager.ServerStatusCache;
import me.orbitium.pterodactylManager.PterodactylManager;
import me.orbitium.pterodactylManager.api.GameServer;
import me.orbitium.pterodactylManager.utils.ServerStatus;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;
import java.util.logging.Level;

public final class PterodactylLobby extends JavaPlugin {

    //    public static PterodactylManager networkManager;
    public static PterodactylLobby instance;
    public static Random random = new Random();
    public NetworkLobbyStartup networkLobbyStartup;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();

        if (PterodactylManager.instance == null) {
            getLogger().log(Level.SEVERE, "Manager is not loaded correctly, disabling PterodactylLobby plugin...");
            return;
        }

        instance = this;

        getCommand("gamenetwork").setExecutor(new GameNetworkCommand());

        getLogger().log(Level.INFO, "Starting up game servers...");
        networkLobbyStartup = new NetworkLobbyStartup();
        networkLobbyStartup.fetchServerStatus();
//        startupGameServers();
    }

//    private void startupGameServers() {
//        for (String serverID : getConfig().getStringList("controlled-servers")) {
//            GameServer gameServer = new GameServer(serverID, ServerStatus.LOADING);
//            ServerStatusCache.onServerStatusUpdate(gameServer);
//            GameNetworkManager.refreshServer(gameServer);
//        }
//    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static void prtNetworkError(String error) {
        instance.getLogger().log(Level.WARNING, error);
    }

    public static void prtInfo(String info) {
        instance.getLogger().log(Level.INFO, info);
    }
}
