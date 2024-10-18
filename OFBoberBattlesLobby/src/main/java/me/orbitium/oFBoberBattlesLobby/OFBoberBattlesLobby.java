package me.orbitium.oFBoberBattlesLobby;

import me.orbitium.oFBoberBattlesLobby.commands.BattleCommandExecutor;
import me.orbitium.oFBoberBattlesLobby.listener.PlayerListener;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Map;

public final class OFBoberBattlesLobby extends JavaPlugin {

    public static OFBoberBattlesLobby instance;
    public static Map<String, String> serverIDMap = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        ConfigurationSection section = getConfig().getConfigurationSection("plugin-id-map");
        for (String key : section.getKeys(false))
            serverIDMap.put(section.getString(key), key);

        System.out.println(serverIDMap);

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        BattleCommandExecutor battleCommand = new BattleCommandExecutor();
        getCommand("battle").setExecutor(battleCommand);
        getCommand("battle").setTabCompleter(battleCommand);

        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static void sendMessage(Player player, String path) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("messages." + path)));
    }

    public static void sendMessage(Player player, String path, String replaced, String replacer) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("messages." + path).replace(replaced, replacer)));
    }

    public static void sendHotBarMessage(Player player, String path, String replaced, String replacer) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("messages." + path).replace(replaced, replacer))));
    }

    public static void sendHotBarMessage(Player player, String path) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("messages." + path))));
    }

    public static int getInt(String path) {
        return instance.getConfig().getInt(path);
    }

    public static String getString(String path) {
        return ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString(path));
    }

    public static void sendPlayerToOtherServer(Player player, String serverID) {
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
