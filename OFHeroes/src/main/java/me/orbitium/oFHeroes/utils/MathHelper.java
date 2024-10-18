package me.orbitium.oFHeroes.utils;

import org.bukkit.util.Vector;

public class MathHelper {
    public static Vector getCardinalDirection(Vector direction) {
        double x = direction.getX();
        double z = direction.getZ();

        // Compare the x and z values to determine the nearest cardinal direction
        if (Math.abs(x) > Math.abs(z)) {
            // Player is mostly facing east or west
            if (x > 0) {
                return new Vector(1, 0, 0);  // East
            } else {
                return new Vector(-1, 0, 0);  // West
            }
        } else {
            // Player is mostly facing north or south
            if (z > 0) {
                return new Vector(0, 0, 1);  // South
            } else {
                return new Vector(0, 0, -1);  // North
            }
        }
    }
}
