package me.orbitium.oFHeroes.skill.mage;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import me.orbitium.oFHeroes.OFHeroes;
import me.orbitium.oFHeroes.skill.BaseSkill;
import me.orbitium.oFHeroes.skill.SkillManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.function.Consumer;

public class Heatwave extends BaseSkill {
    Map<Player, Long> playerActions = new HashMap<>();

    int seconds = 5;
    List<Integer> damageByLevel = Arrays.asList(5, 5, 5);

    public Heatwave() {
        super(Arrays.asList(PlayerToggleSneakEvent.class));
        cooldown = 20;
//        FileConfiguration config = OFHeroes.getConfigForSkill(skillName);
//        seconds = config.getInt("burn-time");
//        damageByLevel = config.getIntegerList("damage-by-level");
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof PlayerToggleSneakEvent playerToggleSneakEvent) {
            Player player = playerToggleSneakEvent.getPlayer();
            if (!playerToggleSneakEvent.isSneaking())
                return;

            Long lastAction = playerActions.getOrDefault(player, 0L);
            if (OFHeroes.getMillis() - lastAction > 250) {
                playerActions.put(player, OFHeroes.getMillis());
                return;
            }

            if (!SkillManager.checkCooldown(player, this))
                return;

            // Start moving the magma cloud
            spawnMovingParticleCube(player);
        }
    }


    public void spawnMovingParticleCube(Player player) {
        Bukkit.getRegionScheduler().runAtFixedRate(OFHeroes.instance, player.getLocation(), new Consumer<ScheduledTask>() {
            final Location location = player.getLocation().add(0, 1, 0); // Start at player's eye level
            final Location startLocation = player.getLocation().clone(); // Start at player's eye level
            final double direction = 0.3; // Movement increment
            final double fixedY = location.getY(); // Fix Y coordinate

            @Override
            public void accept(ScheduledTask scheduledTask) {
                // Spawn a 3x3x3 cube of redstone particles at the current location
                int ySize = 1;
                if (getLevel(player) > 1)
                    ySize = 3;

                for (int x = -ySize; x <= ySize; x++) {
                    for (int y = -1; y <= 1; y++) {
                        for (int z = -1; z <= 1; z++) {
                            Location particleLocation = location.clone().add(x, y, z);
                            particleLocation.setY(fixedY + y); // Ensure Y coordinate is fixed
                            player.getWorld().spawnParticle(Particle.LAVA, particleLocation, 0, 0, 0, 0);

                            // Check for player collisions
                            Collection<Entity> entities = player.getWorld().getNearbyEntities(location, 2, 2, 2);

                            for (Entity entity : entities) {
                                if (!(entity instanceof LivingEntity livingEntity))
                                    continue;

                                if (livingEntity instanceof Player victim)
                                    if (victim == player)
                                        continue;

                                if (livingEntity.getLocation().distance(location) < 1.8) {
                                    livingEntity.damage(damageByLevel.get(getLevel(player)));
                                    livingEntity.setFireTicks(seconds * 20);
                                }
                            }
                        }
                    }
                }

                // Move the particle location forward
                location.add(location.getDirection().setY(0).normalize().multiply(direction));

                // Stop the effect after moving a certain distance
                if (location.distance(startLocation) > 10)
                    scheduledTask.cancel();

            }
        }, 1L, 1L);
    }
}
