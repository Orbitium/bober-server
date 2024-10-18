package me.orbitium.oFRandomTeleport.commands;

import me.orbitium.oFRandomTeleport.OFRandomTeleport;
import me.orbitium.oFRandomTeleport.home.PlayerHome;
import me.orbitium.oFRandomTeleport.home.PlayerHomeManager;
import me.orbitium.oFRandomTeleport.listener.FirstLoginListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class HomeCommand implements CommandExecutor, TabCompleter {

    public Map<Player, Long> playerCooldownData = new HashMap<>();
    public Map<Player, Long> playerHomeCooldownData = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player sender)) {
            commandSender.sendMessage(ChatColor.RED + "You can't use this command on the console!");
            return false;
        }

        if (s.equals("rtp")) {
            if (strings.length == 1 && commandSender.isOp()) {
                if (strings[0].equals("all")) {
                    Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
                    for (Player onlinePlayer : onlinePlayers) {
                        if (onlinePlayer.isOp())
                            continue;

                        FirstLoginListener.teleportPlayerToRandomLocation(onlinePlayer);
                        OFRandomTeleport.sendMessage(onlinePlayer, "random-teleporting-admin");
                        sender.sendMessage(ChatColor.AQUA + "Player(s) random teleported!");
                    }
                } else {
                    Player player = Bukkit.getPlayer(strings[0]);
                    if (player == null) {
                        OFRandomTeleport.sendMessage(sender, "invalid-player");
                        return false;
                    }

                    FirstLoginListener.teleportPlayerToRandomLocation(player);
                    OFRandomTeleport.sendMessage(player, "random-teleporting-admin");
                    sender.sendMessage(ChatColor.AQUA + "Player(s) random teleported!");
                }

                return true;
            }

            if (OFRandomTeleport.getSeconds() - playerCooldownData.getOrDefault(sender, 0L) > OFRandomTeleport.instance.getConfig().getInt("rtp-command-cooldown")) {
                for (PermissionAttachmentInfo effectivePermission : sender.getEffectivePermissions()) {
                    if (sender.isOp() || (effectivePermission.getPermission().equals("rtp.use") && effectivePermission.getValue())) {
                        if (sender.getWorld() != Bukkit.getWorld("world")) {
                            OFRandomTeleport.sendMessage(sender, "not-allowed-in-other-world");
                            return true;
                        }
                        FirstLoginListener.teleportPlayerToRandomLocation(sender);
                        OFRandomTeleport.sendMessage(sender, "random-teleporting");
                        playerCooldownData.put(sender, OFRandomTeleport.getSeconds());
                        return true;
                    }
                }
            } else {
                OFRandomTeleport.sendMessage(sender, "command-cooldown");
                return true;
            }

            OFRandomTeleport.sendMessage(sender, "not-enough-permission");
        }

        if (s.equals("homes")) {
            if (PlayerHomeManager.playerHomes.getOrDefault(sender, new ArrayList<>()).size() == 0) {
                OFRandomTeleport.sendMessage(sender, "no-homes-to-list");
                return true;
            }

            OFRandomTeleport.sendMessage(sender, "your-homes");

            for (PlayerHome playerHome : PlayerHomeManager.playerHomes.getOrDefault(sender, new ArrayList<>())) {
                String messageFormat = ChatColor.translateAlternateColorCodes('&', OFRandomTeleport.instance.getConfig().getString("messages.home-list-format"));

                // Replace placeholders with actual values
                String formattedMessage = messageFormat
                        .replace("%home_name%", playerHome.homeName)
                        .replace("%location_world%", playerHome.location.getWorld().getName())
                        .replace("%location_x%", String.format("%.2f", playerHome.location.getX()))
                        .replace("%location_y%", String.format("%.2f", playerHome.location.getY()))
                        .replace("%location_z%", String.format("%.2f", playerHome.location.getZ()));

                // Send the formatted message to the player
                sender.sendMessage(formattedMessage);
            }
        }

        if (s.equals("sethome") && strings.length == 1) {
            Player player = ((Player) commandSender);
            long date = playerHomeCooldownData.getOrDefault(player, 0L);
            if (OFRandomTeleport.getSeconds() - date > OFRandomTeleport.setHomeCooldown) {
                OFRandomTeleport.sendMessage(player, "sethome-command-cooldown");
                return false;
            }

            playerHomeCooldownData.put(player, OFRandomTeleport.getSeconds());
            String homeName = strings[0];
            homeName = homeName.toLowerCase(Locale.US);

            if (PlayerHomeManager.createPlayerHome(((Player) commandSender), homeName))
                playerHomeCooldownData.put(player, OFRandomTeleport.getSeconds());
            return true;
        }

        if (s.equals("sethome") && strings.length == 0) {
            Player player = ((Player) commandSender);
            long date = playerHomeCooldownData.getOrDefault(player, 0L);
            if (OFRandomTeleport.getSeconds() - date < OFRandomTeleport.setHomeCooldown) {
                OFRandomTeleport.sendMessage(player, "sethome-command-cooldown");
                return false;
            }

            String homeName = "home";
            homeName = homeName.toLowerCase(Locale.US);

            if (PlayerHomeManager.createPlayerHome(((Player) commandSender), homeName))
                playerHomeCooldownData.put(player, OFRandomTeleport.getSeconds());
            return true;
        }

        if (s.equals("delhome") && strings.length == 1) {
            String homeName = strings[0];
            homeName = homeName.toLowerCase(Locale.US);

            PlayerHomeManager.deleteHome(((Player) commandSender), homeName);
            return true;
        }

        if (strings.length == 1) {
            String firstArg = strings[0];

            if (!firstArg.equals("create") && !firstArg.equals("reload") && !firstArg.equals("delete")) {
                PlayerHomeManager.teleportToHome(((Player) commandSender), firstArg.toLowerCase(Locale.US));
            }

            if (firstArg.equals("reload") && commandSender.isOp()) {
                OFRandomTeleport.instance.reloadConfig();
                OFRandomTeleport.setHomeCooldown = OFRandomTeleport.instance.getConfig().getInt("set-home-cooldown");
                commandSender.sendMessage(ChatColor.GREEN + "OFRandomTeleport plugin reloaded!");
            }
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 1 && s.equals("home")) {
            List<String> suggestions = new ArrayList<>(List.of("reload"));
            List<PlayerHome> homes = PlayerHomeManager.playerHomes.get((Player) commandSender);
            for (PlayerHome home : homes)
                suggestions.add(home.homeName);

            if (!commandSender.isOp())
                suggestions.remove("reload");

            return suggestions;
        } else if (strings.length == 1 && s.equals("sethome")) {
            return Collections.singletonList("home-name");
        } else if (strings.length == 1 && s.equals("delhome")) {
            List<String> suggestions = new ArrayList<>();
            List<PlayerHome> homes = PlayerHomeManager.playerHomes.get((Player) commandSender);
            for (PlayerHome home : homes)
                suggestions.add(home.homeName);

            return suggestions;
        }

        if (s.equals("rtp"))
            if (commandSender.isOp())
                if (strings.length == 1)
                    return null;

        return Collections.emptyList();
    }
}
