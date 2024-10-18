package me.orbitium.oFHeroes.skill;

import me.orbitium.oFHeroes.OFHeroes;
import org.bukkit.entity.Player;

import java.util.*;

public class SkillManager {

    private static Map<UUID, List<SkillCooldown>> cooldownData = new HashMap<>();
    private static Map<String, BaseSkill> loadedSkills = new HashMap<>();

    public static void addSkill(BaseSkill baseSkill) {
        loadedSkills.put(baseSkill.skillName, baseSkill);
    }

    public static BaseSkill getSkill(String id) {
        return loadedSkills.get(id);
    }

    public static boolean checkCooldown(Player player, BaseSkill baseSkill) {
        List<SkillCooldown> skillCooldowns = cooldownData.getOrDefault(player.getUniqueId(), new ArrayList<>());

        SkillCooldown removeCooldown = null;

        for (SkillCooldown skillCooldown : skillCooldowns) {
            if (skillCooldown.skillID.equals(baseSkill.skillName)) {
                removeCooldown = skillCooldown;
                if ((skillCooldown.date + baseSkill.cooldown) > OFHeroes.getSeconds()) {

                    long totalSeconds = skillCooldown.date + baseSkill.cooldown - OFHeroes.getSeconds();
                    long hours = totalSeconds / 3600;
                    long minutes = (totalSeconds % 3600) / 60;
                    long seconds = totalSeconds % 60;

                    // Build the output string
                    StringBuilder result = new StringBuilder();
                    if (minutes > 0)
                        result.append(minutes).append(" minut ");
                    if (seconds > 0 || (hours == 0 && minutes == 0))
                        result.append(seconds).append(" sekund");

                    OFHeroes.sendMessage(player, "skill-cooldown-warning", "%cooldown%", result.toString());

                    return false;
                }
            }
        }

        if (removeCooldown != null)
            skillCooldowns.remove(removeCooldown);

        skillCooldowns.add(new SkillCooldown(baseSkill.skillName, OFHeroes.getSeconds()));

        cooldownData.put(player.getUniqueId(), skillCooldowns);

        return true;
    }

}
