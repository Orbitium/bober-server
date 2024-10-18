package me.orbitium.oFHeroes.skill;

import me.orbitium.oFHeroes.OFHeroes;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.*;
import java.util.logging.Level;

public abstract class BaseSkill {
    public String skillName;
    public String friendlyName;
    public List<String> description;
    public int cooldown;
    public int requiredMana;
    public List<Class<? extends Event>> listeningEvents;
    public Map<Player, Integer> ownerPlayers;
    private Map<Integer, List<?>> levelBenefitReplacers = new HashMap<>();
    private List<Integer> costByLevel = new ArrayList<>();

    public BaseSkill(List<Class<? extends Event>> listeningEvents) {
        this.listeningEvents = listeningEvents;

        ownerPlayers = new HashMap<>();

        skillName = getClass().getSimpleName();
//        System.out.println("Skill Name: " + skillName);

        FileConfiguration config = OFHeroes.getConfigForSkill(skillName);
        cooldown = config.getInt("cooldown");
        requiredMana = config.getInt("required-mana");
        description = config.getStringList("description");
        description.replaceAll(textToTranslate -> ChatColor.translateAlternateColorCodes('&', textToTranslate));
        friendlyName = config.getString("name");

        for (String key : config.getKeys(false)) {
            if (!key.contains("by-level"))
                continue;

            levelBenefitReplacers.put(levelBenefitReplacers.size(), config.getList(key));
        }

        if (config.contains("cost-by-level"))
            costByLevel = config.getIntegerList("cost-by-level");
        else
            costByLevel = Collections.nCopies(8, 1);

//        if (!levelBenefitReplacers.isEmpty())
//            System.out.println(levelBenefitReplacers);

//        if (skillName.contains("Skill"))
//            return;

    }

    public abstract void onEvent(Event event);

    public List<String> getDescription(int level) {
        List<String> parsedDescription = new ArrayList<>();
        int itemsParsed = 0;
        for (String s : description) {
            if (s.contains("%level-advantage%")) {
                List<?> possibleParses = levelBenefitReplacers.get(itemsParsed);
                s = s.replaceAll("%level-advantage%", possibleParses.get(level).toString());
                itemsParsed++;
            }

            if (s.contains("%cost%"))
                s = s.replaceAll("%cost%", getCost(level) + "");

            parsedDescription.add(s);
        }
        return parsedDescription;
    }

    public int getCost(int level) {
        try {
            return costByLevel.get(level);
        } catch (Exception e) {
            OFHeroes.instance.getLogger().log(Level.SEVERE, "Error finding the cost for skill: " + skillName + " at level: " + level);
            return 1;
        }
    }

//    public void onPlayerAdded(Player player) {
//        if (!ownerPlayers.containsKey(player))
//            ownerPlayers.put(player, 0);
//    }

    public void onPlayerAdded(Player player, int level) {
        ownerPlayers.put(player, level);
    }

    public int getLevel(Player player) {
        return ownerPlayers.get(player);
    }
}
