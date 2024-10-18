package me.orbitium.oFHeroes.skill.mage;

import me.orbitium.oFHeroes.OFHeroes;
import me.orbitium.oFHeroes.skill.BaseSkill;
import me.orbitium.oFHeroes.skill.SkillManager;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;

public class Fireball extends BaseSkill {

    public static List<Integer> damage;
    public static int burnTime;

    public Fireball() {
        super(Arrays.asList(PlayerSwapHandItemsEvent.class));
        cooldown = 12;
//        FileConfiguration config = OFHeroes.getConfigForSkill(skillName);
//        damage = config.getIntegerList("damage-by-level");
//        burnTime = config.getInt("burn-time");
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof PlayerSwapHandItemsEvent playerSwapHandItemsEvent) {
            Player player = playerSwapHandItemsEvent.getPlayer();
            if (!SkillManager.checkCooldown(player, this))
                return;

            if (!player.isSneaking())
                return;

            spawnFireballInFrontOfPlayer(player);
        }
    }

    private void spawnFireballInFrontOfPlayer(Player player) {
        // Get the player's location and direction
        Location playerLocation = player.getLocation();
        Vector direction = playerLocation.getDirection().normalize();

        // Set the fireball spawn location in front of the player
        Location fireballLocation = playerLocation.add(direction.multiply(2));

        // Spawn the fireball
        org.bukkit.entity.Fireball fireball = (org.bukkit.entity.Fireball) player.getWorld().spawnEntity(fireballLocation, EntityType.FIREBALL);

        // Set the fireball's direction (make it move forward)
        fireball.setVelocity(direction.multiply(1.5)); // Adjust speed multiplier as needed
        fireball.setShooter(player); // Optional: set the player as the shooter
//        fireball.getPersistentDataContainer().set(SorcererBaseSkill.isAnOrb, PersistentDataType.STRING, "fire");
    }
}
