package me.orbitium.oFBoberBattles.game;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import me.orbitium.oFBoberBattles.OFBoberBattles;
import me.orbitium.oFBoberBattles.battle.Battle;
import me.orbitium.oFBoberBattles.border.WorldBorderManager;
import me.orbitium.oFBoberBattles.utils.MessageManager;
import me.orbitium.oFHeroes.hero.HeroManager;
import me.orbitium.oFRandomTeleport.listener.FirstLoginListener;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;

public class GameManager {

    public static Battle currentBattle;

//    public static void schedulePreGameStart() {
//        int requiredMinPlayer = OFBoberBattles.getInt("minimum-players-required-to-start-a-battle");
//
//        Bukkit.getGlobalRegionScheduler().runAtFixedRate(OFBoberBattles.instance, new Consumer<>() {
//            int cooldown = OFBoberBattles.getInt("game-start-countdown");
//
//            @Override
//            public void accept(ScheduledTask scheduledTask) {
//                if (Bukkit.getOnlinePlayers().size() >= requiredMinPlayer) {
//                    cooldown--;
//
//                    if (cooldown == 30)
//                        MessageManager.sendGameTimerUpdate(cooldown, false);
//                    if (cooldown >= 0 && cooldown <= 5)
//                        MessageManager.sendGameTimerUpdate(cooldown, false);
//
//                    MessageManager.sendGameTimerUpdate(cooldown, true);
//
//                    if (cooldown == 0) {
//                        startGame();
//                        scheduledTask.cancel();
//                    }
//                } else {
//                    cooldown = OFBoberBattles.getInt("game-start-countdown");
//                    for (Player onlinePlayer : Bukkit.getOnlinePlayers())
//                        OFBoberBattles.sendHotBarMessage(onlinePlayer, "more-players-required", "%amount%", requiredMinPlayer + "");
//                }
//            }
//        }, 20L, 20L);
//    }

//    public static void onFirstPlayerJoin() {
//        Bukkit.getGlobalRegionScheduler().runDelayed(OFBoberBattles.instance, new Consumer<ScheduledTask>() {
//            @Override
//            public void accept(ScheduledTask scheduledTask) {
//                startGame();
//            }
//        }, 10 * 20);
//    }

    public static void startGame(List<String> playerNames) {
        currentBattle = new Battle(OFBoberBattles.getSeconds(), playerNames);
        currentBattle.onGameStarted();
        OFBoberBattles.battleWorld.setTime(0);
        OFBoberBattles.battleWorld.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        WorldBorderManager.startBorderShrink();

        OFBoberBattles.instance.getLogger().log(Level.INFO, "Battle started!");

        Bukkit.getGlobalRegionScheduler().runDelayed(OFBoberBattles.instance, (t) -> {
            currentBattle.players.clear();
            if (currentBattle.alivePlayers.size() < 2) {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    OFBoberBattles.sendMessage(onlinePlayer, "game-cancelled");
                    OFBoberBattles.sendPlayerToOtherServer(onlinePlayer, "lobby");
                }
            } else {
                HeroManager.activateSkills();
            }
        }, 20 * 60);
    }

//    public static void stopGame() {
//
//    }

    public static void eliminatePlayer(Player player) {
        player.setGameMode(GameMode.SPECTATOR);

//        if (currentBattle.players.contains(player.getName()))
        currentBattle.onPlayerEliminated(player);
        player.getPersistentDataContainer().remove(OFBoberBattles.isBattlePlayers);
    }

}
