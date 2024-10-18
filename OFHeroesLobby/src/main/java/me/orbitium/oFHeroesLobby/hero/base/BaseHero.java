package me.orbitium.oFHeroesLobby.hero.base;

import org.bukkit.Material;

import java.util.List;

public class BaseHero {
    public String id;
    public String displayName;
    public List<String> displayLore;
    public Material displayItem;
    

    public BaseHero(String id, String displayName, List<String> displayLore, Material displayItem) {
        this.id = id;
        this.displayName = displayName;
        this.displayLore = displayLore;
        this.displayItem = displayItem;
    }
}
