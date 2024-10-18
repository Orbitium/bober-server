package me.orbitium.oFHeroes.skill.miner;

import me.orbitium.oFHeroes.OFHeroes;
import me.orbitium.oFHeroes.skill.BaseSkill;
import me.orbitium.oFHeroes.skill.SkillLoader;
import me.orbitium.oFHeroes.skill.SkillManager;
import me.orbitium.oFHeroes.utils.MathHelper;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Tunnelling extends BaseSkill {

    NamespacedKey tunnellingKey = new NamespacedKey(OFHeroes.instance, "isTunnellingOn");
    List<Integer> breakingSizeX = new ArrayList<>(Arrays.asList(1, 1, 1));
    List<Integer> breakingSizeY = new ArrayList<>(Arrays.asList(1, 1, 1));

//    private static AutoSmelt autoSmelt;

    public Tunnelling() {
        super(Arrays.asList(
                PlayerSwapHandItemsEvent.class,
                BlockBreakEvent.class
        ));

//        for (String string : OFHeroes.getConfigForSkill(skillName).getStringList("extra-blocks-mined-by-level")) {
//            String[] data = string.split("-");
//            int horizontal = Integer.parseInt(data[0].trim());
//            int vertical = Integer.parseInt(data[1].trim());
//
//            breakingSizeY.add(vertical);
//            breakingSizeX.add(horizontal);
//        }

//        Bukkit.getGlobalRegionScheduler().runDelayed(OFHeroes.instance, (t) -> {
//            autoSmelt = (AutoSmelt) SkillManager.getSkill("AutoSmelt");
//        }, 30L);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof PlayerSwapHandItemsEvent playerSwapHandItemsEvent) {
            Player player = playerSwapHandItemsEvent.getPlayer();

            if (!player.isSneaking())
                return;

            boolean previousState = player.getPersistentDataContainer().get(tunnellingKey, PersistentDataType.BOOLEAN);

            if (previousState)
                OFHeroes.sendMessage(player, "skill-deactivated", "%skill_name%", "Tunnelling");
            else
                OFHeroes.sendMessage(player, "skill-activated", "%skill_name%", "Tunnelling");

            player.getPersistentDataContainer().set(tunnellingKey, PersistentDataType.BOOLEAN, !previousState);
            playerSwapHandItemsEvent.setCancelled(true);
        }

        if (event instanceof BlockBreakEvent blockBreakEvent) {
            Player player = blockBreakEvent.getPlayer();

            Material type = blockBreakEvent.getBlock().getType();
            if (type.getHardness() == 0)
                return;

            ItemStack breakingTool = player.getInventory().getItemInMainHand();
            if (breakingTool == null || !breakingTool.getType().name().contains("PICKAXE"))
                return;


            if (!player.getPersistentDataContainer().get(tunnellingKey, PersistentDataType.BOOLEAN))
                return;


            int vertical = breakingSizeY.get(getLevel(player));
            int horizontal = breakingSizeX.get(getLevel(player));

            breakBlocks(player, breakingTool, blockBreakEvent.getBlock(), horizontal, vertical);
        }
    }

    public static void breakBlocks(Player player, ItemStack itemStack, Block startBlock, int horizontal, int vertical) {
        startBlock.setType(Material.AIR);
        // Get the cardinal direction from the block's location (assuming some default direction like north for simplicity)
        Vector direction = player.getLocation().getDirection();  // Assuming north for simplicity, or you can use a more complex method to get direction if needed

        Location startLocation = startBlock.getLocation().clone();
        // Get the nearest cardinal direction (north, south, east, west)
        Vector cardinalDirection = MathHelper.getCardinalDirection(direction);

        if (horizontal == 0) {
            Block targetBlock = startBlock.getLocation().clone().add(0, -1, 0).getBlock();

//            if (autoSmelt.shouldSmelt(player, targetBlock))
//                targetBlock.setType(Material.AIR);
//            else
//                targetBlock.breakNaturally(itemStack);

            addDamage(player, 1);
            return;
        }

        // Set the starting block to be the block that was broken
//        Block startBlock = brokenBlock.getRelative(cardinalDirection.getBlockX(), 0, cardinalDirection.getBlockZ());

        // Offset by 1 block in the broken block's cardinal facing direction
//        startBlock = startBlock.getRelative(cardinalDirection.getBlockX(), 0, cardinalDirection.getBlockZ());

        // Build a 5x3x3 wall and replace those blocks with air
        for (int width = -horizontal; width <= horizontal; width++) {  // 5 blocks wide
            for (int height = -vertical; height <= vertical; height++) {  // 3 blocks tall
                for (int depth = 0; depth < 1; depth++) {  // 3 blocks deep
                    // Place each block relative to the broken block's position
                    Block wallBlock = startBlock.getRelative(
                            cardinalDirection.getBlockZ() * width,  // Left-right based on cardinal direction
                            height,  // Vertical placement
                            cardinalDirection.getBlockX() * width  // Forward-backward based on direction
                    );

                    // Further offset based on depth
                    wallBlock = wallBlock.getRelative(
                            cardinalDirection.getBlockX() * depth,  // Adjust depth based on facing direction
                            0,  // No change in height for depth
                            cardinalDirection.getBlockZ() * depth  // Adjust depth
                    );

//                    if (wallBlock.getLocation() == startLocation) {
//                        System.out.println("AAAAAAAA");
//                        continue;
//                    }

//                    if (autoSmelt.shouldSmelt(player, wallBlock))
//                        wallBlock.setType(Material.AIR);
//                    else
                    wallBlock.breakNaturally(itemStack);
                    addDamage(player, 1);
                }
            }
        }
    }

    public static void breakWallInFrontOfPlayer(Player player, int horizontal, int vertical) {
        // Get the block the player is looking at
        Block targetBlock = player.getTargetBlockExact(4);
        if (targetBlock == null) {
            player.sendMessage("No block in sight to break!");
            return;
        }

        ItemStack itemStack = player.getInventory().getItemInMainHand();

        BlockFace blockFace = player.getFacing();

        int x = 0;
        int z = 0;
        System.out.println(blockFace);
        if (blockFace == BlockFace.EAST || blockFace == BlockFace.WEST)
            z++;
        else
            x++;

//        System.out.println(x);
//        System.out.println(z);

        // Get the starting location from the target block
        Location startLocation = targetBlock.getLocation();

        // Loop through the width and height to break the wall
        for (int w = -horizontal; w < horizontal; w++) {
            for (int h = -vertical; h < vertical; h++) {
                System.out.println(w);
                System.out.println(h);
                // Calculate the block location relative to the target block
                Location targetLocation = startLocation.clone().add(x * horizontal, h, z * horizontal);

                // Get the block at the current location and break it
                Block block = targetLocation.getBlock();
                if (block.getType() != Material.AIR) { // Only break non-air blocks
                    block.breakNaturally(itemStack);
                    addDamage(player, 1);
                }
            }
        }
    }

    @Override
    public void onPlayerAdded(Player player, int level) {
        super.onPlayerAdded(player, level);

        if (!player.getPersistentDataContainer().has(tunnellingKey))
            player.getPersistentDataContainer().set(tunnellingKey, PersistentDataType.BOOLEAN, false);
    }

    public static boolean addDamage(Player player, int damage) {
        ItemStack item = player.getInventory().getItemInMainHand();
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta instanceof Damageable) {
            Damageable damageMeta = (Damageable) itemMeta;
            int damageChance = 100;

            if (itemMeta.getEnchants().containsKey(Enchantment.UNBREAKING))
                damageChance -= itemMeta.getEnchantLevel(Enchantment.UNBREAKING) * 8;

            int generated = OFHeroes.random.nextInt(100);
            if (damageChance < generated)
                return true;

            damageMeta.setDamage(damageMeta.getDamage() + damage);
            item.setItemMeta(itemMeta);

            if (damageMeta.getDamage() >= item.getType().getMaxDurability()) {
                player.playEffect(EntityEffect.BREAK_EQUIPMENT_MAIN_HAND);
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                item.setAmount(0);
                return false;
            }
        }

        return true;
    }
}
