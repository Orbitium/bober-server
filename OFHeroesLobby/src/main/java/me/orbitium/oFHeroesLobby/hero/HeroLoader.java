package me.orbitium.oFHeroesLobby.hero;

import me.orbitium.oFHeroesLobby.OFHeroesLobby;
import me.orbitium.oFHeroesLobby.hero.base.BaseHero;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class HeroLoader {

    private static List<String> loadIds = Arrays.asList("miner", "warrior", "mage");

    public static void loadHeroes() {
        File parentFile = new File(OFHeroesLobby.instance.getDataFolder(), "/Heroes/");
        if (!parentFile.exists())
            parentFile.mkdirs();

        for (String heroId : loadIds) {
            File heroFile = new File(parentFile, heroId + ".yaml");
            if (!heroFile.exists()) {
                OFHeroesLobby.instance.getLogger().log(Level.SEVERE, "Config for " + heroId + " is not found!");
                continue;
            }

            BaseHero baseHero = parseHero(heroFile);
            HeroManager.addHero(baseHero);
        }
    }

    private static BaseHero parseHero(File yamlFile) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(yamlFile);

        // Extract data from YAML
        String id = config.getString("id");
        String displayName = config.getString("name");

        List<String> lore = config.getStringList("lore");
        Material displayItem = Material.matchMaterial(config.getString("display-item"));

        // Create and return the BaseHero object
        return new BaseHero(id, displayName, lore, displayItem);
    }
}