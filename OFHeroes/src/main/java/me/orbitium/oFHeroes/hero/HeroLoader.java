package me.orbitium.oFHeroes.hero;

import me.orbitium.oFHeroes.OFHeroes;
import me.orbitium.oFHeroes.hero.base.BaseHero;
import me.orbitium.oFHeroes.skill.BaseSkill;
import me.orbitium.oFHeroes.skill.SkillManager;
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
        File parentFile = new File(OFHeroes.instance.getDataFolder(), "/Heroes/");
        if (!parentFile.exists())
            parentFile.mkdirs();

        for (String heroId : loadIds) {
            File heroFile = new File(parentFile, heroId + ".yaml");
            if (!heroFile.exists()) {
                OFHeroes.instance.getLogger().log(Level.SEVERE, "Config for " + heroId + " is not found!");
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

        List<String> skillNames = config.getStringList("skills");
        List<BaseSkill> skills = new ArrayList<>();
        for (String skillName : skillNames) {
            BaseSkill skill = SkillManager.getSkill(skillName);
            if (skill != null) {
                System.out.println(skillName + " loaded");
                skills.add(skill);
            }
            else {
                System.out.println(skillName + " doesn't exists");
                System.out.println(skillName + " doesn't exists");
                System.out.println(skillName + " doesn't exists");
                System.out.println(skillName + " doesn't exists");
                System.out.println(skillName + " doesn't exists");
            }
        }


        // Create and return the BaseHero object
        return new BaseHero(id, displayName, lore, displayItem, skills);
    }
}