package me.orbitium.oFHeroes.hero.base;

import me.orbitium.oFHeroes.skill.BaseSkill;
import org.bukkit.Material;

import java.util.List;

public class BaseHero {
    public String id;
    public String displayName;
    public List<String> displayLore;
    public Material displayItem;
    public List<BaseSkill> skills;

    public BaseHero(String id, String displayName, List<String> displayLore, Material displayItem, List<BaseSkill> skills) {
        this.id = id;
        this.displayName = displayName;
        this.displayLore = displayLore;
        this.displayItem = displayItem;
        this.skills = skills;
    }
}
