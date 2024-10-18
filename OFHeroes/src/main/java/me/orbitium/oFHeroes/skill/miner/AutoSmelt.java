package me.orbitium.oFHeroes.skill.miner;

import me.orbitium.oFHeroes.OFHeroes;
import me.orbitium.oFHeroes.skill.BaseSkill;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class AutoSmelt extends BaseSkill {
    List<Integer> smeltChances = Arrays.asList(80, 80, 80, 80);
    Map<Material, ItemStack> smeltRecipes = new HashMap<>();

    public AutoSmelt() {
        super(Arrays.asList(
                BlockBreakEvent.class
        ));

        smeltRecipes.put(Material.COPPER_ORE, new ItemStack(Material.COPPER_INGOT));
        smeltRecipes.put(Material.IRON_ORE, new ItemStack(Material.IRON_INGOT));
        smeltRecipes.put(Material.GOLD_ORE, new ItemStack(Material.GOLD_INGOT));
        smeltRecipes.put(Material.LAPIS_ORE, new ItemStack(Material.LAPIS_LAZULI, 3));
        smeltRecipes.put(Material.COAL_ORE, new ItemStack(Material.COAL));

        smeltRecipes.put(Material.DEEPSLATE_COPPER_ORE, new ItemStack(Material.COPPER_INGOT));
        smeltRecipes.put(Material.DEEPSLATE_IRON_ORE, new ItemStack(Material.IRON_INGOT));
        smeltRecipes.put(Material.DEEPSLATE_GOLD_ORE, new ItemStack(Material.GOLD_INGOT));
        smeltRecipes.put(Material.DEEPSLATE_LAPIS_ORE, new ItemStack(Material.LAPIS_LAZULI, 3));
        smeltRecipes.put(Material.DEEPSLATE_COAL_ORE, new ItemStack(Material.COAL));
//        smeltChances = OFHeroes.getConfigForSkill(skillName).getIntegerList("chances-by-level");

//        Iterator<Recipe> recipes = Bukkit.recipeIterator();
//        while (recipes.hasNext()) {
//            Recipe recipe = recipes.next();
//            if (!(recipe instanceof FurnaceRecipe furnaceRecipe)) {
//                continue;
//            }
//
////            System.out.println(furnaceRecipe.getInput() + " " + furnaceRecipe.getResult());
//            smeltRecipes.put(furnaceRecipe.getInput(), furnaceRecipe.getResult());
//        }
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof BlockBreakEvent blockBreakEvent) {
            Player player = blockBreakEvent.getPlayer();

            ItemStack breakingTool = player.getInventory().getItemInMainHand();
            if (breakingTool == null || !breakingTool.getType().name().contains("PICKAXE"))
                return;

            ItemMeta meta = breakingTool.getItemMeta();

            // Check if the item has the Silk Touch enchantment
            if (meta.hasEnchant(Enchantment.SILK_TOUCH))
                return;


            Material targetMaterial = blockBreakEvent.getBlock().getType();
            if (!smeltRecipes.containsKey(targetMaterial))
                return;

            int smeltChance = smeltChances.get(getLevel(player));
            if (!OFHeroes.checkChance(smeltChance))
                return;

            blockBreakEvent.setDropItems(false);

//            for (ItemStack stack : blockBreakEvent.getBlock().getDrops(player.getInventory().getItemInMainHand())) {
//                stack = smeltRecipes.getOrDefault(stack, stack);
//                player.getWorld().dropItemNaturally(blockBreakEvent.getBlock().getLocation(), stack);
//            }

            if (smeltRecipes.containsKey(targetMaterial)) {
                ItemStack stack = smeltRecipes.get(targetMaterial);
                player.getWorld().dropItemNaturally(blockBreakEvent.getBlock().getLocation(), stack);
            }
        }
    }

    public boolean shouldSmelt(Player player, Block block) {
        ItemStack breakingTool = player.getInventory().getItemInMainHand();
        if (breakingTool == null || !breakingTool.getType().name().contains("PICKAXE"))
            return false;

        ItemMeta meta = breakingTool.getItemMeta();

        // Check if the item has the Silk Touch enchantment
        if (meta.hasEnchant(Enchantment.SILK_TOUCH))
            return false;

        if (!ownerPlayers.containsKey(player))
            return false;

        int smeltChance = smeltChances.get(getLevel(player));

        if (OFHeroes.checkChance(smeltChance)) {
            for (ItemStack stack : block.getDrops(player.getInventory().getItemInMainHand())) {
                stack = smeltRecipes.getOrDefault(stack, stack);
                player.getWorld().dropItemNaturally(block.getLocation(), stack);
            }
            return true;
        }

        return false;
    }


}
