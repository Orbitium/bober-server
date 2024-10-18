package me.orbitium.oFBoberBattles.border;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import me.orbitium.oFBoberBattles.utils.MessageManager;
import me.orbitium.oFBoberBattles.OFBoberBattles;
import me.orbitium.oFRandomTeleport.listener.FirstLoginListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class WorldBorderManager {

    //    private static Map<Integer, Integer> borderStages;
    private static List<BorderStage> borderStages;

    public static void loadStages() {
//        borderStages = new HashMap<>();
        borderStages = new ArrayList<>();

        // Get the border section from the config
        Map<String, Object> borders = OFBoberBattles.instance.getConfig().getConfigurationSection("game.border").getValues(false);

        // Loop through the entries and load them into the map
        for (Map.Entry<String, Object> entry : borders.entrySet()) {
            // Parse the key and value as Integer
            int time = Integer.parseInt(entry.getKey());
            int borderSize = ((Integer) entry.getValue());

            // Put the values into the borderStages map
            borderStages.add(new BorderStage(time, borderSize));
        }


        Bukkit.getRegionScheduler().run(OFBoberBattles.instance, Bukkit.getWorld("world").getSpawnLocation(), (t) -> {
            OFBoberBattles.battleWorld.getWorldBorder().setCenter(0, 0);
            OFBoberBattles.battleWorld.getWorldBorder().setSize(borderStages.getFirst().targetSize * 2);
        });
    }

    public static void startBorderShrink() {
        final int runFrequency = 1;

        Bukkit.getGlobalRegionScheduler().runAtFixedRate(OFBoberBattles.instance, new Consumer<>() {
            BorderStage currentBorderStage = borderStages.getFirst();
            BorderStage nextBorderStage = borderStages.get(1);
            int stageCounter = 1;

            int secondsPassed = currentBorderStage.targetSeconds - nextBorderStage.targetSeconds;

            @Override
            public void accept(ScheduledTask scheduledTask) {
                secondsPassed -= runFrequency;

                if (nextBorderStage == null) {
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers())
                        Bukkit.getRegionScheduler().runDelayed(OFBoberBattles.instance, onlinePlayer.getLocation(), (t) -> {
                            onlinePlayer.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 200000, 1));
                        }, 60 * 3 * 20L);
                    return;
                }

                int remaining = secondsPassed;
                if (remaining == 30)
                    MessageManager.sendBorderTimerUpdate(remaining, nextBorderStage, false);
                if (remaining >= 0 && remaining <= 5)
                    MessageManager.sendBorderTimerUpdate(remaining, nextBorderStage, false);

                MessageManager.sendBorderTimerUpdate(remaining, nextBorderStage, true);

                if (remaining == 0) {
                    updateWorldBorder(nextBorderStage);
                    currentBorderStage = nextBorderStage;
                    nextBorderStage = getNextStage(++stageCounter);

                    if (nextBorderStage == null) {
                        for (Player onlinePlayer : Bukkit.getOnlinePlayers())
                            Bukkit.getRegionScheduler().runDelayed(OFBoberBattles.instance, onlinePlayer.getLocation(), (t) -> {
                                onlinePlayer.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 200000, 1));
                            }, 60 * 3 * 20L);
                        return;
                    }

                    secondsPassed = currentBorderStage.targetSeconds - nextBorderStage.targetSeconds;
                }
            }
        }, runFrequency * 20, runFrequency * 20);
    }

    private static @Nullable BorderStage getNextStage(int updatedStage) {
        if (updatedStage > borderStages.size() - 1)
            return null;

        return borderStages.get(updatedStage);
    }

    private static void updateWorldBorder(BorderStage borderStage) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers())
            Bukkit.getRegionScheduler().run(OFBoberBattles.instance, onlinePlayer.getLocation(), (t) -> {
                teleportPlayerInsideBorder(onlinePlayer, borderStage);
            });

        Bukkit.getRegionScheduler().runDelayed(OFBoberBattles.instance, Bukkit.getWorld("world").getSpawnLocation(), (t) -> {
            MessageManager.sendBorderUpdate(borderStage);

            OFBoberBattles.battleWorld.getWorldBorder().setSize(borderStage.targetSize * 2);
            OFBoberBattles.battleWorld.getWorldBorder().setDamageAmount(0);
            OFBoberBattles.battleWorld.getWorldBorder().setDamageBuffer(0);
        }, 1L);
    }

    private static void teleportPlayerInsideBorder(Player player, BorderStage borderStage) {
        // Get the player's world and its world border

        // Get the center and size of the border
//        Location borderCenter = new Location(OFBoberBattles.battleWorld, 0, 0, 0);
        int borderRadius = Math.abs(borderStage.targetSize - 5);
        // Get the player's current location
        Location playerLocation = player.getLocation();
        int playerX = playerLocation.getBlockX();
        int playerZ = playerLocation.getBlockZ();

//        System.out.println("Border radius: " + borderRadius);
//        System.out.println("x: " + playerX);
//        System.out.println("z: " + playerZ);


//        System.out.println("X Check: " + (playerX < -borderRadius || playerX > borderRadius));
//        System.out.println("Z Check: " + (playerZ < -borderRadius || playerZ > borderRadius));

        if (playerX < -borderRadius || playerX > borderRadius)
            FirstLoginListener.teleportPlayerToRandomLocation(player, borderRadius);
        else if (playerZ < -borderRadius || playerZ > borderRadius)
            FirstLoginListener.teleportPlayerToRandomLocation(player, borderRadius);

//
//            double distanceX = Math.abs(playerLocation.getX() - borderCenter.getX());
//        double distanceZ = Math.abs(playerLocation.getZ() - borderCenter.getZ());
//
//        // Check if the player is outside the border radius
//        if (distanceX > borderRadius || distanceZ > borderRadius) {
//            // Calculate the new safe position inside the border
//            double safeX = Math.max(borderCenter.getX() - borderRadius, Math.min(borderCenter.getX() + borderRadius, playerLocation.getX()));
//            double safeZ = Math.max(borderCenter.getZ() - borderRadius, Math.min(borderCenter.getZ() + borderRadius, playerLocation.getZ()));
//
//            FirstLoginListener.teleportPlayerToRandomLocation(player, (int) safeX);
//            // Keep the Y coordinate the same or adjust it for safety if necessary
////            Location safeLocation = new Location(OFBoberBattles.battleWorld, safeX, playerLocation.getY(), safeZ);
////
////            Bukkit.getRegionScheduler().run(OFBoberBattles.instance, safeLocation, (t) -> {
////                // Find a safe Y position (on ground or inside air)
////                Location finalLocation = OFBoberBattles.battleWorld.getHighestBlockAt(safeLocation).getLocation().add(0, 1, 0);
////
////                // Teleport the player to the final safe location
////                player.teleportAsync(finalLocation);
////
////                // Optionally, send the player a message
////                OFBoberBattles.sendMessage(player, "border-teleport");
////            });
    }
}