package me.orbitium.oFBoberBattlesLobby.commands;

import me.orbitium.oFBoberBattlesLobby.queue.QueueManager;
import me.orbitium.oFBoberBattlesLobby.utils.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class BattleCommandExecutor implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender instanceof Player player) {
            if (args.length != 1)
                return false;

            switch (args[0].toLowerCase(Locale.US)) {
                case "join":
                    QueueManager.onPlayerAddedQueue(player);
                    return true;
                case "leave":
                    QueueManager.onPlayerRemovedFromQueue(player);
                    return true;
                case "help":
                    MessageManager.sendHelpMessage(player);
                    return true;
            }
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1)
            return Arrays.asList("join", "leave", "help");
        return Collections.emptyList();
    }
}



