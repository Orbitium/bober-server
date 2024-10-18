package me.orbitium.oFHeroesLobby;

import me.orbitium.oFHeroesLobby.command.HeroesCommandExecutor;
import me.orbitium.oFHeroesLobby.listener.ClassHeroSelectListener;
import me.orbitium.oFHeroesLobby.hero.HeroLoader;
import me.orbitium.oFHeroesLobby.listener.PlayerListener;
import me.orbitium.oFHeroesLobby.mysql.MySQL;
import me.orbitium.oFHeroesLobby.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.List;

public final class OFHeroesLobby extends JavaPlugin {

    public static OFHeroesLobby instance;
    public static NamespacedKey selectedHero;


    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        instance = this;
        selectedHero = new NamespacedKey(this, "selected-hero");

        getServer().getPluginManager().registerEvents(new ClassHeroSelectListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        ItemUtils.build();
        HeroLoader.loadHeroes();

        getCommand("hero").setExecutor(new HeroesCommandExecutor());

        try {
            MySQL.connect(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static void sendMessage(Player player, String messagePath) {
        String message = instance.getConfig().getString("messages." + messagePath);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void sendMessage(Player player, String messagePath, String replaced, String replacer) {
        String message = instance.getConfig().getString("messages." + messagePath).replaceAll(replaced, replacer);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static String getString(String path) {
        return ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString(path));
    }

}
