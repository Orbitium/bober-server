package me.orbitium.oFHeroes.skill.warrior;

import me.orbitium.oFHeroes.OFHeroes;
import me.orbitium.oFHeroes.skill.BaseSkill;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class WarriorBaseSkill extends BaseSkill {
    public WarriorBaseSkill() {
        super(Collections.emptyList());
    }

    @Override
    public void onEvent(Event event) {

    }

    @Override
    public void onPlayerAdded(Player player, int level) {
        OFHeroes.resetPlayerAttr(player, Attribute.GENERIC_MAX_HEALTH);
        OFHeroes.changePlayerAttr(player, Attribute.GENERIC_MAX_HEALTH, 4);
        player.getInventory().addItem(new ItemStack(Material.MACE));
    }
}
