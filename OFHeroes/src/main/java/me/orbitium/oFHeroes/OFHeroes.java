package me.orbitium.oFHeroes;

import me.orbitium.oFHeroes.command.HeroesCommandExecutor;
import me.orbitium.oFHeroes.gui.ClassHeroSelectListener;
import me.orbitium.oFHeroes.hero.HeroLoader;
import me.orbitium.oFHeroes.listener.ClassEventListeners;
import me.orbitium.oFHeroes.mysql.MySQL;
import me.orbitium.oFHeroes.skill.SkillLoader;
import me.orbitium.oFHeroes.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class OFHeroes extends JavaPlugin {

    public static OFHeroes instance;
    public static Random random = new Random();
    private final static String ATTR_KEY = "heroes_attr_";
    public static NamespacedKey selectedHero;
    public static boolean canSelectHero = true;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        instance = this;
        selectedHero = new NamespacedKey(this, "selected-hero");

        getServer().getPluginManager().registerEvents(new ClassEventListeners(), this);
        getServer().getPluginManager().registerEvents(new ClassHeroSelectListener(), this);

        ItemUtils.build();
        SkillLoader.loadSkills();
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

    public static FileConfiguration getConfigForSkill(String fileName) {
        File file = new File(instance.getDataFolder() + "/Skills/" + fileName + ".yaml");
        if (!file.exists()) {
//            instance.getLogger().log(Level.SEVERE, fileName + " config doesn't exists!");
//            instance.getLogger().log(Level.SEVERE, fileName + " config doesn't exists!");
//            instance.getLogger().log(Level.SEVERE, fileName + " config doesn't exists!");
//            instance.getLogger().log(Level.SEVERE, fileName + " config doesn't exists!");
//            instance.getLogger().log(Level.SEVERE, fileName + " config doesn't exists!");
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    public static boolean checkChance(int chance) {
        return chance > random.nextInt(100);
    }

    public static void sendMessage(Player player, String messagePath) {
        String message = instance.getConfig().getString("messages." + messagePath);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void sendMessage(Player player, String messagePath, String replaced, String replacer) {
        String message = instance.getConfig().getString("messages." + messagePath).replaceAll(replaced, replacer);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static long getSeconds() {
        return System.currentTimeMillis() / 1000;
    }

    public static long getMillis() {
        return System.currentTimeMillis();
    }

    public static void changePlayerAttr(Player player, Attribute atr, double change) {
        AttributeInstance attributeInstance = player.getAttribute(atr);

        int currentModifiers = 0;
        for (AttributeModifier modifier : attributeInstance.getModifiers()) {
            if (modifier.getKey().getKey().contains(ATTR_KEY))
                currentModifiers++;
        }

        NamespacedKey key = new NamespacedKey(instance, "classes_attr_" + atr.name() + "_" + (currentModifiers + 1));
        try {
            attributeInstance.addModifier(new AttributeModifier(key, change, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.ANY));
        } catch (Exception ignored) {

        }
    }

    public static void resetPlayerAttr(Player player, Attribute atr) {
        AttributeInstance attributeInstance = player.getAttribute(atr);

        List<AttributeModifier> removeModifiers = new ArrayList<>();
        for (AttributeModifier modifier : attributeInstance.getModifiers())
            if (modifier.getKey().getKey().contains(ATTR_KEY))
                removeModifiers.add(modifier);

        for (AttributeModifier removeModifier : removeModifiers)
            attributeInstance.removeModifier(removeModifier);
    }

    public static String getString(String path) {
        return ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString(path));
    }

}
