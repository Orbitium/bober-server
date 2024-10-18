package me.orbitium.pterodactylBattles;

import me.orbitium.pterodactylBattles.command.GameNetworkCommand;
import me.orbitium.pterodactylManager.PterodactylManager;
import me.orbitium.pterodactylManager.api.GameServer;
import me.orbitium.pterodactylManager.utils.ServerStatus;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Level;

public final class PterodactylBattles extends JavaPlugin {

    public static PterodactylBattles instance;
    public GameServer gameServer;

    @Override
    public void onEnable() {
        // Plugin startup logic
        if (PterodactylManager.instance == null) {
            getLogger().log(Level.SEVERE, "Manager is not loaded correctly, disabling PterodactylBattles plugin...");
            return;
        }

        instance = this;
        gameServer = new GameServer(PterodactylManager.instance.getSender(), ServerStatus.LOADING);

        getCommand("gamenetwork").setExecutor(new GameNetworkCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
