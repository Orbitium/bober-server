package me.orbitium.oFHeroes.command;

import me.orbitium.oFHeroes.OFHeroes;
import me.orbitium.oFHeroes.gui.ClassHeroSelectGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HeroesCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player))
            return false;

        if (!OFHeroes.canSelectHero) {
            OFHeroes.sendMessage(player, "cant-select-hero");
            return true;
        }

        ClassHeroSelectGui.createClassSelectGUI(player);
        return true;
    }
}
