package me.orbitium.oFBoberBattles.battle;

import me.orbitium.oFBoberBattles.OFBoberBattles;
import me.orbitium.oFRandomTeleport.listener.FirstLoginListener;
import me.orbitium.pterodactylBattles.PterodactylBattles;
import me.orbitium.pterodactylManager.PterodactylManager;
import me.orbitium.pterodactylManager.utils.ServerStatus;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Battle {
    public long startedDate;
    public List<String> players;
    public List<Player> alivePlayers = new ArrayList<>();
//    public List<String>

    public Battle(long startedDate, List<String> players) {
        this.startedDate = startedDate;
        this.players = players;
    }


    public void onGameStarted() {
        OFBoberBattles.announce("game-started");
        PterodactylBattles.instance.gameServer.setStatus(ServerStatus.IN_GAME);
//        randomTeleportEverybody();
    }

//    public void randomTeleportEverybody() {
//        for (Player onlinePlayer : Bukkit.getOnlinePlayers())
//            FirstLoginListener.teleportPlayerToRandomLocation(onlinePlayer);
//    }

    public void onPlayerEliminated(Player player) {
        alivePlayers.remove(player);
        player.getPersistentDataContainer().remove(OFBoberBattles.isBattlePlayers);
        players.remove(player.getName());

        OFBoberBattles.sendMessage(player, "eliminated");
        OFBoberBattles.announce("player-eliminated", "%player_name%", player.getName());

        validateGameEnd();
    }

    public void validateGameEnd() {
        if (alivePlayers.size() != 1)
            return;

        Player winner = alivePlayers.get(0);
        OFBoberBattles.announce("game-finished", "%player_name%", winner.getName());
        Bukkit.getGlobalRegionScheduler().cancelTasks(OFBoberBattles.instance);

        Bukkit.getGlobalRegionScheduler().runDelayed(OFBoberBattles.instance, (t) -> {
            for (Player player : Bukkit.getOnlinePlayers())
                OFBoberBattles.sendPlayerToOtherServer(player, "lobby");
        }, 15L * 20L);

        Bukkit.getGlobalRegionScheduler().runDelayed(OFBoberBattles.instance, (tt) -> {
            PterodactylManager.instance.commandSender.sendServerUpdate(ServerStatus.FINISHED);
        }, 20 * 20L);
    }
}
