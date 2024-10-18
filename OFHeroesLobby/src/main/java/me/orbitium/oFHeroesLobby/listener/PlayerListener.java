package me.orbitium.oFHeroesLobby.listener;

import me.orbitium.oFHeroesLobby.OFHeroesLobby;
import me.orbitium.oFHeroesLobby.mysql.MySQL;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent event) {
        String selectedClass = MySQL.getSelectedClass(event.getPlayer().getUniqueId().toString());
        if (selectedClass != null)
            event.getPlayer().getPersistentDataContainer().set(OFHeroesLobby.selectedHero, PersistentDataType.STRING, selectedClass);
    }


}
