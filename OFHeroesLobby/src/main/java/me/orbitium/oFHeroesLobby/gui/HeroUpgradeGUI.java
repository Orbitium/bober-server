package me.orbitium.oFHeroesLobby.gui;

import me.orbitium.oFHeroesLobby.OFHeroesLobby;
import me.orbitium.oFHeroesLobby.hero.base.BaseHero;
import me.orbitium.oFHeroesLobby.utils.HeroSelectHolder;
import me.orbitium.oFHeroesLobby.utils.HeroUpgradeHolder;
import me.orbitium.oFHeroesLobby.utils.ItemBuilder;
import me.orbitium.oFHeroesLobby.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class HeroUpgradeGUI {

    public static void open(Player player, BaseHero hero) {
        Inventory inventory = Bukkit.createInventory(new HeroUpgradeHolder(), 54, OFHeroesLobby.getString("hero-upgrade-gui.title"));

        for (int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, ItemUtils.emptyItem);

        ItemBuilder itemBuilder = new ItemBuilder(Material.DIAMOND)
                .withName(ChatColor.GOLD + hero.displayName)
                .withLore(hero.displayLore)
                .addLore("")
                .addLore(OFHeroesLobby.instance.getConfig().getStringList("hero-upgrade-gui.hero-lore"));

        ItemBuilder book = new ItemBuilder(Material.BOOK)
                .withName(OFHeroesLobby.instance.getConfig().getString("hero-upgrade-gui.info.name"))
                .withLore(OFHeroesLobby.instance.getConfig().getStringList("hero-upgrade-gui.info.lore"));

        inventory.setItem(4, itemBuilder.build());
        inventory.setItem(8, book.build());

        for (int i = 0; i < 3; i++) {
            inventory.setItem(19 + (i * 9), new ItemStack(Material.AIR));
            inventory.setItem(20 + (i * 9), new ItemStack(Material.AIR));
            inventory.setItem(21 + (i * 9), new ItemStack(Material.AIR));
            inventory.setItem(22 + (i * 9), new ItemStack(Material.AIR));
        }

//        hero.

        player.openInventory(inventory);
    }

}
