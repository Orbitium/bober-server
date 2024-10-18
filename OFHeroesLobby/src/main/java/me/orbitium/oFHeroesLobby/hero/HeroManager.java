package me.orbitium.oFHeroesLobby.hero;

import me.orbitium.oFHeroesLobby.OFHeroesLobby;
import me.orbitium.oFHeroesLobby.hero.base.BaseHero;
import me.orbitium.oFHeroesLobby.mysql.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
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
        if (player.getPersistentDataContainer().has(OFHeroesLobby.selectedHero, PersistentDataType.STRING)) {
            String selectedHero = player.getPersistentDataContainer().get(OFHeroesLobby.selectedHero, PersistentDataType.STRING);
            if (selectedHero.equals(heroID)) {
                OFHeroesLobby.sendMessage(player, "hero-already-selected");
                return;
            }
        }


        player.closeInventory();
        OFHeroesLobby.sendMessage(player, "hero-selected", "%hero%", heroID);
        player.getPersistentDataContainer().set(OFHeroesLobby.selectedHero, PersistentDataType.STRING, heroID);
        MySQL.setSelectedClass(player.getUniqueId().toString(), heroID);
    }
}
