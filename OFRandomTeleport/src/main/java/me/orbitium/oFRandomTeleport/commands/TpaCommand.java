package me.orbitium.oFRandomTeleport.commands;

import me.orbitium.oFRandomTeleport.OFRandomTeleport;
import me.orbitium.oFRandomTeleport.tpa.TpaRequest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TpaCommand implements CommandExecutor, TabCompleter {

    //    public static Map<Player>
    public static List<TpaRequest> requests = new ArrayList<>();
    public static Map<Player, Long> cooldown = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player sender)) {
            commandSender.sendMessage(ChatColor.RED + "You can't use this command on the console!");
            return false;
        }

        if (s.equals("tpa")) {
            if (cooldown.getOrDefault(sender, 0L) + 5 > OFRandomTeleport.getSeconds()) {
                OFRandomTeleport.sendMessage(sender, "tpa-cooldown");
                return false;
            }

            if (strings.length != 1) {
                commandSender.sendMessage(ChatColor.AQUA + "/tpa {nazwa_gracza}");
                return false;
            }

            Player receiver = Bukkit.getPlayer(strings[0]);
            if (receiver == null) {
                OFRandomTeleport.sendMessage(sender, "invalid-player");
                return false;
            }

            if (receiver == sender)
                return false;

            requests.add(new TpaRequest(sender, receiver, OFRandomTeleport.getSeconds()));
            cooldown.put(sender, OFRandomTeleport.getSeconds());
            OFRandomTeleport.sendMessage(sender, "tpa-sent");
            OFRandomTeleport.sendTpaMessage(receiver, "tpa-received", "%player_name%", sender.getName());
        }

        if (s.equals("tpaccept")) {
            TpaRequest deleteRequest = null;
            int timeOutTime = OFRandomTeleport.instance.getConfig().getInt("tpa-timeout");

            for (TpaRequest request : requests) {
                if (request.receiver.equals(sender) && OFRandomTeleport.getSeconds() - request.date < timeOutTime) {
//                    System.out.println("Receiver: " + request.receiver.getName());
//                    System.out.println("Sender: " + request.sender.getName());
                    request.sender.teleportAsync(request.receiver.getLocation());

                    OFRandomTeleport.sendMessage(request.sender, "tpa-accepted");
                    OFRandomTeleport.sendMessage(request.receiver, "tpa-accepted");
                } else {
                    OFRandomTeleport.sendMessage(sender, "tpa-timeout");
                }
                deleteRequest = request;
            }

            if (deleteRequest != null)
                requests.remove(deleteRequest);
            else
                OFRandomTeleport.sendMessage(sender, "tpa-not-found");
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 1)
            return null;
        return Collections.emptyList();
    }
}
