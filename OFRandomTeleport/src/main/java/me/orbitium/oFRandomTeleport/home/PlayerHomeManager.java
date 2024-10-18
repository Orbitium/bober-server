package me.orbitium.oFRandomTeleport.home;

import me.orbitium.oFRandomTeleport.OFRandomTeleport;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class PlayerHomeManager {

    public static Map<Player, List<PlayerHome>> playerHomes = new HashMap<>();

    public static boolean createPlayerHome(Player player, String homeName) {
        List<PlayerHome> homes = playerHomes.get(player);

        for (PlayerHome home : homes) {
            if (home.homeName.equals(homeName)) {
                OFRandomTeleport.sendMessage(player, "duplicate-house-name");
                return false;
            }
        }

        int currentHomes = homes.size();

        int limit = 0;
        FileConfiguration config = OFRandomTeleport.instance.getConfig();
        Set<PermissionAttachmentInfo> playerPerms = player.getEffectivePermissions();

        for (String key : config.getConfigurationSection("homes").getKeys(false)) {
            if (key.equals("default"))
                limit = config.getInt("homes." + key);
            for (PermissionAttachmentInfo playerPerm : playerPerms) {
                if (playerPerm.getPermission().equals(key) && playerPerm.getValue()) {
                    limit = config.getInt("homes." + key);
                    break;
                }
            }
        }

        if (currentHomes >= limit) {
            OFRandomTeleport.sendMessage(player, "home-count-exceeded", "%amount%", limit + "");
            return false;
        }

        homes.add(new PlayerHome(homeName, player.getLocation()));
        playerHomes.put(player, homes);

        OFRandomTeleport.sendMessage(player, "home-created", "%home_name%", homeName);
        return true;
    }

    public static void teleportToHome(Player player, String homeName) {
        PlayerHome playerHome = null;
        List<PlayerHome> homes = playerHomes.get(player);

        for (PlayerHome home : homes) {
            if (home.homeName.equals(homeName)) {
                playerHome = home;
                break;
            }
        }

        if (playerHome == null) {
            OFRandomTeleport.sendMessage(player, "home-not-found");
            return;
        }

        OFRandomTeleport.sendMessage(player, "home-teleporting", "%home_name%", homeName);
        player.teleportAsync(playerHome.location);
    }

    public static void deleteHome(Player player, String homeName) {
        PlayerHome playerHome = null;
        List<PlayerHome> homes = playerHomes.get(player);

        for (PlayerHome home : homes) {
            if (home.homeName.equals(homeName)) {
                playerHome = home;
                break;
            }
        }

        if (playerHome == null) {
            OFRandomTeleport.sendMessage(player, "home-not-found");
            return;
        }

        homes.remove(playerHome);
        OFRandomTeleport.sendMessage(player, "home-deleted");
    }

    public static void savePlayerHomes(Player player) {
        List<PlayerHome> homes = playerHomes.get(player);
        if (homes != null) {
            StringBuilder sb = new StringBuilder();

            for (PlayerHome home : homes) {
                sb.append(serializeHome(home)).append(";");
            }

            // Remove the last semicolon
            if (!sb.isEmpty()) {
                sb.setLength(sb.length() - 1);
            }

            String serializedData = sb.toString();
            // Store the serialized homes data in the player's PersistentDataContainer
            PersistentDataContainer dataContainer = player.getPersistentDataContainer();
            dataContainer.set(OFRandomTeleport.homes, PersistentDataType.STRING, serializedData);
        }
    }

    // Serialize a single PlayerHome object into a string format
    private static String serializeHome(PlayerHome home) {
        Location location = home.location;
        return home.homeName + ":" +
                location.getWorld().getName() + "," +
                location.getX() + "," +
                location.getY() + "," +
                location.getZ();
    }

    public static void loadPlayerHomes(Player player) {
        // Retrieve the serialized homes data from the player's PersistentDataContainer
        PersistentDataContainer dataContainer = player.getPersistentDataContainer();
        if (!dataContainer.has(OFRandomTeleport.homes, PersistentDataType.STRING))
            dataContainer.set(OFRandomTeleport.homes, PersistentDataType.STRING, "");

        String serializedData = dataContainer.get(OFRandomTeleport.homes, PersistentDataType.STRING);

        if (serializedData != null) {
            List<PlayerHome> homes = deserializeHomes(serializedData);
            playerHomes.put(player, homes);
        }
    }

    // Deserialize the string back into a List<PlayerHome> object
    public static List<PlayerHome> deserializeHomes(String data) {
        List<PlayerHome> homes = new ArrayList<>();
        String[] homeEntries = data.split(";");

        for (String entry : homeEntries) {
            if (entry.isBlank())
                continue;

            String[] parts = entry.split(":");
            String homeName = parts[0];

            String[] locationParts = parts[1].split(",");
            String worldName = locationParts[0];
            double x = Double.parseDouble(locationParts[1]);
            double y = Double.parseDouble(locationParts[2]);
            double z = Double.parseDouble(locationParts[3]);

            Location location = new Location(Bukkit.getWorld(worldName), x, y, z);
            homes.add(new PlayerHome(homeName, location));
        }

        return homes;
    }

//    public static void setPlayerHome(Player player, String homeName) {
//        // {homeName:world-x-y-z},
//        int currentHomes = player.getPersistentDataContainer().get(OFRandomTeleport.homeCount, PersistentDataType.INTEGER);
//
//        FileConfiguration config = OFRandomTeleport.instance.getConfig();
//        Set<PermissionAttachmentInfo> playerPerms = player.getEffectivePermissions();
//
//        int limit = 0;
//
//        // Iterate over each entry in the homes section
//        for (String key : config.getConfigurationSection("homes").getKeys(false)) {
//            if (key.equals("default"))
//                limit = config.getInt("homes." + key);
//            for (PermissionAttachmentInfo playerPerm : playerPerms) {
//                if (playerPerm.getPermission().equals(key) && playerPerm.getValue() == true) {
//                    limit = config.getInt("homes." + key);
//                    break;
//                }
//            }
//        }
//
//        if (currentHomes >= limit) {
//            OFRandomTeleport.sendMessage(player, "home-count-exceeded", "%amount%", limit + "");
//            return;
//        }
//
//
//        OFRandomTeleport.instance.getConfig();
//
//        String homeData = player.getPersistentDataContainer().get(OFRandomTeleport.homes, PersistentDataType.STRING);
//
//        Location location = player.getLocation();
//        StringBuilder sb = new StringBuilder();
//        sb.append("{").append(homeName).append(":")
//                .append(location.getWorld().getName())
//                .append("-")
//                .append(location.getBlockX())
//                .append("-")
//                .append(location.getBlockY())
//                .append("-")
//                .append(location.getBlockZ()).append("},");
//
//        String newData = homeData + sb.toString();
//        newData = newData.substring(0, newData.length() - 1);
//        player.getPersistentDataContainer().set(OFRandomTeleport.homes, PersistentDataType.STRING, newData);
//    }
//
//    public static Map<String, Location> getPlayerHomes(Player player) {
//        return null;
//    }

}
