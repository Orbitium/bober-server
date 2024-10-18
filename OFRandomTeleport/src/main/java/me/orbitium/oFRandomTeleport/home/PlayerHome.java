package me.orbitium.oFRandomTeleport.home;

import org.bukkit.Location;

public class PlayerHome {
    public String homeName;
    public Location location;

    public PlayerHome(String homeName, Location location) {
        this.homeName = homeName;
        this.location = location;
    }
}
