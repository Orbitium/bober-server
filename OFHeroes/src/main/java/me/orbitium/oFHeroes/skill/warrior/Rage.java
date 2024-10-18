package me.orbitium.oFHeroes.skill.warrior;

import me.orbitium.oFHeroes.OFHeroes;
import me.orbitium.oFHeroes.skill.BaseSkill;
import me.orbitium.oFHeroes.skill.SkillManager;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class Rage extends BaseSkill {
    public Rage() {
        super(Arrays.asList(
                PlayerSwapHandItemsEvent.class
        ));

        cooldown = 25;
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof PlayerSwapHandItemsEvent playerSwapHandItemsEvent) {
            Player player = playerSwapHandItemsEvent.getPlayer();

            if (!player.isSneaking())
                return;

            if (!SkillManager.checkCooldown(player, this))
                return;

            OFHeroes.sendMessage(player, "skill-activated", "%skill_name%", "Rage");
            playerSwapHandItemsEvent.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 5 * 20, 0));
            playerSwapHandItemsEvent.setCancelled(true);
        }
    }
}
