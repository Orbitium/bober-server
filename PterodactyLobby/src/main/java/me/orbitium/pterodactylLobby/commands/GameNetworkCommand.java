package me.orbitium.pterodactylLobby.commands;

import me.orbitium.pterodactylLobby.GameNetworkManager;
import me.orbitium.pterodactylLobby.PterodactylLobby;
import me.orbitium.pterodactylLobby.manager.ServerStatusCache;
import me.orbitium.pterodactylManager.api.GameServer;
import me.orbitium.pterodactylManager.utils.ServerStatus;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GameNetworkCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender instanceof Player)
            return false;

        if (args.length != 3)
            return false;

        String operation = args[0];

        if (operation.equals("statusinfo")) {
            String sender = args[1];
            String currentStatus = args[2];

            GameServer gameServer = new GameServer(sender, ServerStatus.valueOf(currentStatus));
            PterodactylLobby.instance.networkLobbyStartup.onGameStatusReceived(gameServer);
            ServerStatusCache.onServerStatusUpdate(gameServer);
            PterodactylLobby.prtInfo("Status info received from server: " + sender + " status: " + currentStatus);
        }

        if (operation.equals("statusupdate")) {
            String sender = args[1];
            String newStatus = args[2];
            PterodactylLobby.prtInfo("Status info received from server: " + sender + " status: " + newStatus);

            GameServer gameServer = new GameServer(sender, ServerStatus.valueOf(newStatus));
            ServerStatusCache.onServerStatusUpdate(gameServer);
            if (gameServer.getStatus() == ServerStatus.FINISHED)
                GameNetworkManager.refreshServer(gameServer);
        }

        return false;
    }

}
