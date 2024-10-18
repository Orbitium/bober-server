package me.orbitium.oFHeroesLobby.gui;

import me.orbitium.oFHeroesLobby.OFHeroesLobby;
import me.orbitium.oFHeroesLobby.hero.HeroManager;
import me.orbitium.oFHeroesLobby.hero.base.BaseHero;
import me.orbitium.oFHeroesLobby.utils.HeroSelectHolder;
import me.orbitium.oFHeroesLobby.utils.ItemBuilder;
import me.orbitium.oFHeroesLobby.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class HeroSelectGui {

    public static HeroSelectHolder holder = new HeroSelectHolder();

    public static void createClassSelectGUI(Player player) {
        Inventory inventory = Bukkit.createInventory(holder, 27, OFHeroesLobby.getString("hero-select-gui-title"));

        for (int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, ItemUtils.emptyItem);

        String playerClass = "";
        if (player.getPersistentDataContainer().has(OFHeroesLobby.selectedHero, PersistentDataType.STRING))
            playerClass = player.getPersistentDataContainer().get(OFHeroesLobby.selectedHero, PersistentDataType.STRING);

        inventory.setItem(11, getClassItem(HeroManager.getHero("miner"), playerClass));
        inventory.setItem(13, getClassItem(HeroManager.getHero("warrior"), playerClass));
        inventory.setItem(15, getClassItem(HeroManager.getHero("mage"), playerClass));

        player.openInventory(inventory);
    }

    private static ItemStack getClassItem(BaseHero baseHero, String playerClass) {
//        System.out.println(baseHero.id);
        ItemBuilder itemBuilder = new ItemBuilder(baseHero.displayItem).withName(baseHero.displayName).withLore(baseHero.displayLore);
        if (baseHero.id.equals(playerClass)) {
            itemBuilder.addLore("");
            itemBuilder.addLore("&6Wybrano");
        }
        return itemBuilder.build();
    }

}
