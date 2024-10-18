package me.orbitium.pterodactylBattles.command;

import me.orbitium.oFBoberBattles.game.GameManager;
import me.orbitium.pterodactylBattles.PterodactylBattles;
import me.orbitium.pterodactylManager.PterodactylManager;
import me.orbitium.pterodactylManager.utils.ServerStatus;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameNetworkCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender instanceof Player)
            return false;

//        if (args.length != 2)
//            return false;

        // gamenetwork getstatus sender
        if (args[0].equals("getstatus")) {
            String sentFrom = args[1];
            PterodactylManager.instance.commandSender.sendStatusInfo(PterodactylBattles.instance.gameServer, sentFrom);
        }

        if (args[0].equals("startgame")) {
//            String sentFrom = args[1];
//            List<String> playerNames = new ArrayList<>(List.of(Arrays.copyOfRange(args, 1, args.length)));
            List<String> playerNames = new ArrayList<>(List.of(args));

            PterodactylBattles.instance.gameServer.setStatus(ServerStatus.IN_GAME);
            PterodactylManager.instance.commandSender.sendServerUpdate(ServerStatus.IN_GAME);
            GameManager.startGame(playerNames);
        }

        // gamenetwork getstatus <server-id>
        // No response or error, server is offline
        // If there's a response, use the response


        return false;
    }
}
