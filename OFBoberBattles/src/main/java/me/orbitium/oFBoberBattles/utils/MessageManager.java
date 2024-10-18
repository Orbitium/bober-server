package me.orbitium.oFBoberBattles.utils;

import me.orbitium.oFBoberBattles.OFBoberBattles;
import me.orbitium.oFBoberBattles.border.BorderStage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MessageManager {

    public static void sendGameTimerUpdate(int seconds, boolean hotbar) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers())
            if (hotbar)
                OFBoberBattles.sendHotBarMessage(onlinePlayer, "game-starting", "%seconds%", seconds + "");
            else
                OFBoberBattles.sendMessage(onlinePlayer, "game-starting", "%seconds%", seconds + "");
    }

    public static void sendBorderTimerUpdate(int seconds, BorderStage nextBorderStage, boolean hotbar) {
        String message = OFBoberBattles.getString("messages.border-timer").replace("%x%", nextBorderStage.targetSize + "")
                .replace("%z%", nextBorderStage.targetSize + "").replace("%seconds%", seconds + "");

        for (Player onlinePlayer : Bukkit.getOnlinePlayers())
            if (hotbar)
                OFBoberBattles.sendHotBarMessage(onlinePlayer, message);
            else
                onlinePlayer.sendMessage(message);
    }

    public static void sendBorderUpdate(BorderStage currentBorderStage) {
        String message = OFBoberBattles.getString("messages.border-" +
                        "shrunk").replace("%x%", currentBorderStage.targetSize + "")
                .replace("%z%", currentBorderStage.targetSize + "");

        for (Player onlinePlayer : Bukkit.getOnlinePlayers())
            onlinePlayer.sendMessage(message);
    }
}
