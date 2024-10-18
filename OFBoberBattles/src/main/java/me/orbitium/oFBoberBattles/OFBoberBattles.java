package me.orbitium.oFBoberBattles;

import me.orbitium.oFBoberBattles.border.WorldBorderManager;
import me.orbitium.oFBoberBattles.events.PlayerListener;
import me.orbitium.oFBoberBattles.game.GameManager;
import me.orbitium.pterodactylBattles.PterodactylBattles;
import me.orbitium.pterodactylManager.PterodactylManager;
import me.orbitium.pterodactylManager.utils.ServerStatus;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Random;

public final class OFBoberBattles extends JavaPlugin {

    public static OFBoberBattles instance;
    public static World battleWorld;
    public static Random random = new Random();
    public static NamespacedKey isBattlePlayers;

    @Override
    public void onEnable() {
        battleWorld = getServer().getWorld("world");
        // Plugin startup logic
        instance = this;
        isBattlePlayers = new NamespacedKey(this, "battle-player");
        saveDefaultConfig();
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        WorldBorderManager.loadStages();
//        GameManager.schedulePreGameStart();

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        Bukkit.getGlobalRegionScheduler().runDelayed(OFBoberBattles.instance, (t) -> {
            PterodactylManager.instance.commandSender.sendServerUpdate(ServerStatus.IDLE);
            PterodactylBattles.instance.gameServer.setStatus(ServerStatus.IDLE);
        }, 5 * 20);
    }

    @Override
    public void onDisable() {
        PterodactylManager.instance.commandSender.sendServerUpdate(ServerStatus.LOADING);
        PterodactylBattles.instance.gameServer.setStatus(ServerStatus.LOADING);
    }

    public static long getSeconds() {
        return System.currentTimeMillis() / 1000;
    }

    public static int getInt(String path) {
        return instance.getConfig().getInt(path);
    }

    public static String getString(String path) {
        return ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString(path));
    }

    public static void sendMessage(Player player, String path) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig()
                .getString("messages." + path)));
    }

    public static void sendMessage(Player player, String path, String replaced, String replacer) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig()
                .getString("messages." + path).replace(replaced, replacer)));
    }

    public static void announce(String path, String replaced, String replacer) {
        String message = ChatColor.translateAlternateColorCodes('&', instance.getConfig()
                .getString("messages." + path).replace(replaced, replacer));

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendMessage(message);
        }
    }

    public static void announce(String path) {
        String message = ChatColor.translateAlternateColorCodes('&', instance.getConfig()
                .getString("messages." + path));

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendMessage(message);
        }
    }

    public static void sendHotBarMessage(Player player, String path, String replaced, String replacer) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&', instance.getConfig()
                .getString("messages." + path).replace(replaced, replacer))));
    }

    public static void sendHotBarMessage(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }

    public static void sendPlayerToOtherServer(Player player, String serverID) {
        System.out.println("Sending player to " + serverID);
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("Connect");
            out.writeUTF(serverID);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        player.sendPluginMessage(instance, "BungeeCord", b.toByteArray());
    }
}
