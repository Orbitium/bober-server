package me.orbitium.oFBoberBattlesLobby.utils;

import me.orbitium.oFBoberBattlesLobby.OFBoberBattlesLobby;
import me.orbitium.oFBoberBattlesLobby.queue.QueueManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class MessageManager {

    public static void sendGameTimerUpdate(int seconds, boolean hotbar, List<Player> players) {
        for (Player onlinePlayer : players)
            if (hotbar)
                OFBoberBattlesLobby.sendHotBarMessage(onlinePlayer, "game-starting", "%seconds%", seconds + "");
            else
                OFBoberBattlesLobby.sendMessage(onlinePlayer, "game-starting", "%seconds%", seconds + "");
    }

    public static void sendNoAvailableServers(List<Player> players) {
        for (Player onlinePlayer : players)
            OFBoberBattlesLobby.sendHotBarMessage(onlinePlayer, "no-available-server");
    }

//    public static void sendMorePlayersRequired(List<Player> players) {
//        String string = OFBoberBattlesLobby.getString("more-players-required");
//        string = string.replace("")
//        for (Player onlinePlayer : Bukkit.getOnlinePlayers())
//            OFBoberBattlesLobby.sendHotBarMessage(onlinePlayer, "more-players-required", "%amount%", QueueManager.minRequiredPlayers + "");
//    }

    public static void sendHelpMessage(Player player) {

    }

//    public static void sendBorderUpdate(BorderStage currentBorderStage) {
//        String message = OFBoberBattles.getString("messages.border-shrunk").replace("%x%", currentBorderStage.targetSize + "")
//                .replace("%z%", currentBorderStage.targetSize + "");
//
//        for (Player onlinePlayer : Bukkit.getOnlinePlayers())
//            onlinePlayer.sendMessage(message);
//    }
}
