package me.orbitium.oFBoberBattlesLobby.listener;

import me.orbitium.oFBoberBattlesLobby.queue.QueueManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        QueueManager.onPlayerRemovedFromQueue(event.getPlayer());
    }

}
