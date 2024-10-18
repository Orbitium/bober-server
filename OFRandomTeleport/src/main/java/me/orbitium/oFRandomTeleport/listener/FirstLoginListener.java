package me.orbitium.oFRandomTeleport.listener;

import me.orbitium.oFRandomTeleport.OFRandomTeleport;
import me.orbitium.oFRandomTeleport.home.PlayerHomeManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Random;

public class FirstLoginListener implements Listener {

    @EventHandler
    public void onFirstJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerHomeManager.loadPlayerHomes(player);

        if (event.getPlayer().hasPlayedBefore())
            return;

        // Create a TextComponent for the message
        String discordLink = OFRandomTeleport.instance.getConfig().getString("discord-link");
        TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&',
                OFRandomTeleport.instance.getConfig().getString("messages.first-join-teleported")));

        // Set the click event for the link to open the URL
        message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, discordLink));

        // Send the clickable message to the player
        player.spigot().sendMessage(message);

        teleportPlayerToRandomLocation(player);
    }

    @EventHandler
    public void onRespawn(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (event.getInventory().getType() != InventoryType.CRAFTING || !player.isDead() || !player.isConnected() || player.getHealth() > 0)
            return;

        if (!PlayerHomeManager.playerHomes.get(player).isEmpty()) {
            Bukkit.getGlobalRegionScheduler().runDelayed(OFRandomTeleport.instance, (e) -> {
                OFRandomTeleport.sendMessage(player, "teleported-to-home");
                player.teleportAsync(PlayerHomeManager.playerHomes.get(player).get(0).location);
            }, 8L);
            return;
        }

        if (player.getPotentialBedLocation() != null) {
            Bukkit.getRegionScheduler().run(OFRandomTeleport.instance, player.getPotentialBedLocation(), (e) -> {
                try {
                    if (player.getPotentialBedLocation() != null) {
                        if (!player.getPotentialBedLocation().getBlock().getType().name().contains("BED")) {
                            teleportPlayerToRandomLocation(player);
                            OFRandomTeleport.sendMessage(player, "death-teleported");
                        }
                    } else {
                        teleportPlayerToRandomLocation(player);
                        OFRandomTeleport.sendMessage(player, "death-teleported");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    teleportPlayerToRandomLocation(player);
                    OFRandomTeleport.sendMessage(player, "death-teleported");
                }
            });

            return;
        }

        teleportPlayerToRandomLocation(player);
        OFRandomTeleport.sendMessage(player, "death-teleported");

    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        PlayerHomeManager.savePlayerHomes(event.getPlayer());
    }


    public static void teleportPlayerToRandomLocation(Player player) {
        FileConfiguration config = OFRandomTeleport.instance.getConfig();

        // Get min and max boundaries from config
        int minX = config.getInt("teleportation-boundaries.min.x");
        int minZ = config.getInt("teleportation-boundaries.min.z");
        int maxX = config.getInt("teleportation-boundaries.max.x");
        int maxZ = config.getInt("teleportation-boundaries.max.z");

        // Generate random coordinates within the boundaries
        Random random = OFRandomTeleport.random;
        int x = random.nextInt((maxX - minX) + 1) + minX;
        int z = random.nextInt((maxZ - minZ) + 1) + minZ;

        Location regionLocation = new Location(Bukkit.getWorld("world"), x, 0, z);

        Bukkit.getRegionScheduler().run(OFRandomTeleport.instance, regionLocation, (e) -> {
            Material type = Bukkit.getWorld("world").getHighestBlockAt(x, z).getType();
            boolean canTeleport = type != Material.WATER && type != Material.LAVA;

            if (!canTeleport) {
                teleportPlayerToRandomLocation(player);
                return;
            }

            int y = player.getWorld().getHighestBlockYAt(x, z) + 3;

            // Create the location and teleport the player
            Location randomLocation = new Location(player.getWorld(), x, y, z);
            player.teleportAsync(randomLocation);
        });
    }

    public static void teleportPlayerToRandomLocation(Player player, int size) {
        FileConfiguration config = OFRandomTeleport.instance.getConfig();

        // Get min and max boundaries from config
//        int minX = -size;
//        int minZ = -size;
//        int maxX = size;
//        int maxZ = size;

        // Generate random coordinates within the boundaries
        Random random = OFRandomTeleport.random;
        size = Math.abs(size);
        int x = random.nextInt(-size, size);
        int z = random.nextInt(-size, size);
        System.out.println("Random Teleport size: " + size);

        Location regionLocation = new Location(Bukkit.getWorld("world"), x, 0, z);

        int finalSize = size;
        Bukkit.getRegionScheduler().run(OFRandomTeleport.instance, regionLocation, (e) -> {
            System.out.println("Trying to random teleport...");
            Material type = Bukkit.getWorld("world").getHighestBlockAt(x, z).getType();
//            boolean canTeleport = type != Material.WATER && type != Material.LAVA;
            boolean canTeleport = true;

            if (!canTeleport) {
                System.out.println("Can't teleport!");
                teleportPlayerToRandomLocation(player, finalSize);
                return;
            }

            int y = player.getWorld().getHighestBlockYAt(x, z) + 3;

            // Create the location and teleport the player
            Location randomLocation = new Location(player.getWorld(), x, y, z);
            player.teleportAsync(randomLocation);
        });
    }
}
