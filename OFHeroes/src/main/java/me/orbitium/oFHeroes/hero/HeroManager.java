package me.orbitium.oFHeroes.hero;

import me.orbitium.oFHeroes.OFHeroes;
import me.orbitium.oFHeroes.hero.base.BaseHero;
import me.orbitium.oFHeroes.mysql.MySQL;
import me.orbitium.oFHeroes.skill.BaseSkill;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeroManager {

    private static Map<String, BaseHero> heroes = new HashMap<>();

    public static void addHero(BaseHero hero) {
        heroes.put(hero.id, hero);
    }

    public static BaseHero getHero(String heroID) {
        return heroes.get(heroID);
    }

    public static void onHeroSelected(Player player, String heroID) {
//        BaseHero baseHero = getHero(heroID);
        if (player.getPersistentDataContainer().has(OFHeroes.selectedHero, PersistentDataType.STRING)) {
            String selectedHero = player.getPersistentDataContainer().get(OFHeroes.selectedHero, PersistentDataType.STRING);
            if (selectedHero.equals(heroID)) {
                OFHeroes.sendMessage(player, "hero-already-selected");
                return;
            }
        }

        player.closeInventory();
        OFHeroes.sendMessage(player, "hero-selected", "%hero%", heroID);
        player.getPersistentDataContainer().set(OFHeroes.selectedHero, PersistentDataType.STRING, heroID);
        MySQL.setSelectedClass(player.getUniqueId().toString(), heroID);
    }

    public static void activateSkills() {
        System.out.println("Activating Skills");

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.closeInventory();
            String selectedClass = MySQL.getSelectedClass(onlinePlayer.getUniqueId().toString());
            if (selectedClass == null) {
                OFHeroes.sendMessage(onlinePlayer, "hero-abilities-activated-no-hero");
                continue;
            }

            OFHeroes.sendMessage(onlinePlayer, "hero-abilities-activated", "%hero%", selectedClass);

            for (BaseSkill skill : HeroManager.getHero(selectedClass).skills)
                if (skill != null)
                    skill.onPlayerAdded(onlinePlayer, 0);
        }

        OFHeroes.canSelectHero = false;
    }
}
