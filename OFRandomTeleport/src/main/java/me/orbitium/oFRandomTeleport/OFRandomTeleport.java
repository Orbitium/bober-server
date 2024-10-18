package me.orbitium.oFRandomTeleport;

import me.orbitium.oFRandomTeleport.commands.HomeCommand;
import me.orbitium.oFRandomTeleport.commands.TpaCommand;
import me.orbitium.oFRandomTeleport.listener.FirstLoginListener;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public final class OFRandomTeleport extends JavaPlugin {

    public static Random random = new Random();
    public static OFRandomTeleport instance;
    public static NamespacedKey homes;
    public static NamespacedKey homeCount;
    public static int setHomeCooldown;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        saveDefaultConfig();
        homes = new NamespacedKey(this, "player-homes");
        homeCount = new NamespacedKey(this, "home-count");
        getServer().getPluginManager().registerEvents(new FirstLoginListener(), this);

        TpaCommand tpaCommand = new TpaCommand();
        getCommand("tpa").setExecutor(tpaCommand);
        getCommand("tpaccept").setExecutor(tpaCommand);

        getCommand("tpa").setTabCompleter(tpaCommand);
        getCommand("tpaccept").setTabCompleter(tpaCommand);

        HomeCommand homeCommand = new HomeCommand();
        getCommand("home").setExecutor(homeCommand);
        getCommand("home").setTabCompleter(homeCommand);

        getCommand("sethome").setExecutor(homeCommand);
        getCommand("sethome").setTabCompleter(homeCommand);

        getCommand("delhome").setExecutor(homeCommand);
        getCommand("delhome").setTabCompleter(homeCommand);

        getCommand("homes").setExecutor(homeCommand);

        getCommand("rtp").setExecutor(homeCommand);

        setHomeCooldown = getConfig().getInt("set-home-cooldown");
    }

    public static void sendMessage(Player player, String path) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("messages." + path)));
    }

    public static void sendMessage(Player player, String path, String replaced, String replacer) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("messages." + path)
                .replaceAll(replaced, replacer)));
    }

    public static void sendTpaMessage(Player player, String path, String replaced, String replacer) {
        String message = ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("messages." + path)
                .replaceAll(replaced, replacer));

// Create a TextComponent with the message
        TextComponent textComponent = new TextComponent(message);

// Set the click event to run the /tpaccept command
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept"));

// Send the clickable message to the player
        player.spigot().sendMessage(textComponent);
    }

    public static long getSeconds() {
        return System.currentTimeMillis() / 1000;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
