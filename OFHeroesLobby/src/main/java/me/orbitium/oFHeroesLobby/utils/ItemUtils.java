package me.orbitium.oFHeroesLobby.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemUtils {

    public static ItemStack emptyItem;

    public static void build() {
        emptyItem = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).build();
    }


}
