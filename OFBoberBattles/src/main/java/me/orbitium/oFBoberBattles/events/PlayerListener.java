package me.orbitium.oFBoberBattles.events;

import me.orbitium.oFBoberBattles.OFBoberBattles;
import me.orbitium.oFBoberBattles.game.GameManager;
import me.orbitium.oFRandomTeleport.listener.FirstLoginListener;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerDie(PlayerDeathEvent event) {
        Player player = event.getPlayer();
//        System.out.println(player.getLastDamageCause());
//        if (player.getLastDamageCause().equals(EntityDamageEvent.DamageCause.WORLD_BORDER)) {
//            // Get the center and size of the border
//            double borderRadius = (OFBoberBattles.battleWorld.getWorldBorder().getSize() / 2) - (5 + OFBoberBattles.random.nextInt(60));
//            FirstLoginListener.teleportPlayerToRandomLocation(player, (int) borderRadius);
//            event.setCancelled(true);
//            return;
//        }

        if (!player.getPersistentDataContainer().has(OFBoberBattles.isBattlePlayers, PersistentDataType.BOOLEAN))
            return;

        event.setCancelled(true);
        event.setDeathMessage(null);

        for (ItemStack itemStack : event.getPlayer().getInventory())
            if (itemStack != null)
                event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), itemStack);
        event.getPlayer().getInventory().clear();

        GameManager.eliminatePlayer(event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        System.out.println(player.getPersistentDataContainer().has(OFBoberBattles.isBattlePlayers, PersistentDataType.BOOLEAN));
        if (!player.getPersistentDataContainer().has(OFBoberBattles.isBattlePlayers, PersistentDataType.BOOLEAN))
            return;

        GameManager.eliminatePlayer(player);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().getInventory().clear();
        if (GameManager.currentBattle == null)
            return;

        Player player = event.getPlayer();
        if (!GameManager.currentBattle.players.contains(player.getName())) {
            player.setGameMode(GameMode.SPECTATOR);
            OFBoberBattles.sendMessage(player, "joined-as-spectator");
            return;
        }

        player.setGameMode(GameMode.SURVIVAL);
        GameManager.currentBattle.players.remove(player.getName());
        FirstLoginListener.teleportPlayerToRandomLocation(player);
        player.getPersistentDataContainer().set(OFBoberBattles.isBattlePlayers, PersistentDataType.BOOLEAN, true);
        System.out.println(player.getPersistentDataContainer().has(OFBoberBattles.isBattlePlayers, PersistentDataType.BOOLEAN));
        GameManager.currentBattle.alivePlayers.add(player);
        OFBoberBattles.sendMessage(player, "joined-as-survivor");
    }

}
