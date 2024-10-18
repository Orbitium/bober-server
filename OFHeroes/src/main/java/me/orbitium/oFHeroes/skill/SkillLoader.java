package me.orbitium.oFHeroes.skill;

import me.orbitium.oFHeroes.listener.ClassEventListeners;
import me.orbitium.oFHeroes.skill.mage.Fireball;
import me.orbitium.oFHeroes.skill.mage.Heatwave;
import me.orbitium.oFHeroes.skill.miner.AutoSmelt;
import me.orbitium.oFHeroes.skill.miner.Tunnelling;
import me.orbitium.oFHeroes.skill.warrior.Rage;
import me.orbitium.oFHeroes.skill.warrior.WarriorBaseSkill;
import org.bukkit.event.Event;

import java.util.*;

public class SkillLoader {

    //    public static final Map<String, BaseSkill> loadedSkills = new HashMap<>();
    public static List<BaseSkill> skills;

    public static void loadSkills() {
        skills = Arrays.asList(
                new Tunnelling(),
                new AutoSmelt(),

                new WarriorBaseSkill(),
                new Rage(),

                new Fireball(),
                new Heatwave()
        );

        for (BaseSkill skillInstance : skills) {
            for (Class<? extends Event> listeningEvent : skillInstance.listeningEvents) {
                List<BaseSkill> listeners = ClassEventListeners.baseSkills.getOrDefault(listeningEvent, new ArrayList<>());
                listeners.add(skillInstance);

                ClassEventListeners.baseSkills.put(listeningEvent, listeners);
            }

            SkillManager.addSkill(skillInstance);
        }
    }

//    public static BaseSkill getSkill(String skillID) {
//        return loadedSkills.get(skillID);
//    }

}
