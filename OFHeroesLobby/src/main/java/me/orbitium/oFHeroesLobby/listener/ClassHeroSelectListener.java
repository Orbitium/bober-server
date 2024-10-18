package me.orbitium.oFHeroesLobby.listener;

import me.orbitium.oFHeroesLobby.gui.HeroSelectGui;
import me.orbitium.oFHeroesLobby.hero.HeroManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ClassHeroSelectListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        try {
            if (event.getClickedInventory().getHolder() != HeroSelectGui.holder)
                return;
        } catch (Exception ignored) {

        }

        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);
        int slot = event.getSlot();

        switch (slot) {
            case 11:
                HeroManager.onHeroSelected(player, "miner");
                break;

            case 13:
                HeroManager.onHeroSelected(player, "warrior");
                break;

            case 15:
                HeroManager.onHeroSelected(player, "mage");
                break;
        }
    }

}
